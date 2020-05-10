package scripts.miner.gui;

import com.allatori.annotations.DoNotRename;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSTile;
import scripts.api.Timing;
import scripts.api.script.JavaFXGUI;
import scripts.api.script.frameworks.tree.DecisionTree;
import scripts.api.types.RSArea;
import scripts.api.util.Internet;
import scripts.api.util.ScriptSettings;
import scripts.api.util.Timer;
import scripts.miner.data.Location;
import scripts.miner.data.Option;
import scripts.miner.data.Vars;
import scripts.miner.decisions.BankDecisionNode;
import scripts.miner.decisions.InventoryDecisionNode;
import scripts.miner.decisions.MineDecisionNode;
import scripts.miner.decisions.RocksDecisionNode;
import scripts.miner.runnables.BankRunnable;
import scripts.miner.runnables.DropRunnable;
import scripts.miner.runnables.MineRunnable;
import scripts.miner.runnables.MiningRunnable;
import scripts.miner.runnables.WalkBankRunnable;
import scripts.miner.runnables.WalkRocksRunnable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GUI extends JavaFXGUI implements Initializable {

    private static final ObservableList<Option> OPTIONS_DROP = FXCollections.observableArrayList(
      Option.SHIFT_DROP,
      Option.MOUSE_KEYS_DROP,
      Option.ABC_DROP
    );

    private static final ObservableList<Option> OPTIONS_BANK = FXCollections.observableArrayList(
      Option.BANK,
      Option.SHIFT_DROP,
      Option.MOUSE_KEYS_DROP,
      Option.ABC_DROP
    );

    @FXML
    @DoNotRename
    private ComboBox<Option> cbOption;

    @FXML
    @DoNotRename
    private TextField tfLevel;

    @FXML
    @DoNotRename
    private TextField tfHours;

    @FXML
    @DoNotRename
    private ComboBox<Inventory.DROPPING_PATTERN> cbDropPattern;

    @FXML
    @DoNotRename
    private CheckBox chkbxStationary;

    @FXML
    @DoNotRename
    private CheckBox chkbxTimedActions;

    @FXML
    @DoNotRename
    private CheckBox chkbxReactionTimes;

    @FXML
    @DoNotRename
    private Button btnStart;

    private Vars vars;
    private ScriptSettings settings;
    private int previousOptionIndex;

    public GUI(Vars vars) {
        super(vars.script);
        this.vars = vars;
        settings = new ScriptSettings(vars.script);
        init();
    }

    @Override
    public String getFxml() {
        return Internet.getText("http://encodedscripting.com/resources/miner/miner.fxml");
    }

    @Override
    public String[] getStyleSheets() {
        return new String[]{"http://encodedscripting.com/resources/miner/miner.css"};
    }

    @Override
    public Object getController() {
        return this;
    }

    @Override
    public Image getIcon() {
        return Internet.getJavaFXImage("http://encodedscripting.com/resources/miner/img/icon.png");
    }

    @Override
    public StageStyle getStageStyle() {
        return StageStyle.DECORATED;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbOption.setItems(OPTIONS_DROP);
        cbOption.getSelectionModel().selectFirst();

        ObservableList<Inventory.DROPPING_PATTERN> cbDropPatternData = FXCollections.observableArrayList(
          Inventory.DROPPING_PATTERN.TOP_TO_BOTTOM,
          Inventory.DROPPING_PATTERN.LEFT_TO_RIGHT
        );
        cbDropPattern.setItems(cbDropPatternData);
        cbDropPattern.getSelectionModel().selectFirst();

        loadSettings();

        vars.isSelectingRocks = true;

        tfHours.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 3) {
                tfHours.setText(oldValue);
            }
        });
        tfLevel.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.matches("\\d*") || Integer.parseInt(newValue) > 99) {
                    tfLevel.setText(oldValue);
                }
            } catch (NumberFormatException e) {
                tfLevel.setText(newValue);
            }
        });

        startRockObserver();

        btnStart.setOnAction((event) -> handleStart());
    }

    private void handleStart() {
        vars.option = cbOption.getSelectionModel().getSelectedItem();
        vars.isWaitingReactionTime = chkbxReactionTimes.isSelected();
        vars.isStationary = chkbxStationary.isSelected();
        vars.isTimedActions = chkbxTimedActions.isSelected();
        vars.droppingPattern = cbDropPattern.getValue();

        if (vars.rockTileList.isEmpty()) {
            loadPreviousTiles();
            vars.option = getPreviousOption();
        }

        if (vars.option == Option.BANK) {
            for (RSTile tile : vars.rockTileList) {
                for (Location location : Location.values()) {
                    if (location.getArea().contains(tile)) {
                        vars.location = location;
                    }
                }
            }
        }

        vars.miningArea = vars.location != null && vars.location.getPath() != null ? vars.location.getArea() : getMiningArea();

        DecisionTree decisionTree = createDecisionTree();
        long stopTime = tfHours.getText().length() > 0 ? Long.parseLong(tfHours.getText()) * 3600000L + General.random(-300000, 300000) : Long.MAX_VALUE;
        Timer stopTimer = new Timer(stopTime);
        int stopLevel = tfLevel.getText().length() > 0 ? Integer.parseInt(tfLevel.getText()) : 100;
        decisionTree.setStopCondition(() -> stopTimer.isFinished() || Skills.SKILLS.MINING.getActualLevel() >= stopLevel);
        saveSettings();
        vars.script.setDecisionTree(decisionTree);
        vars.isSelectingRocks = false;
        close();
    }

    private DecisionTree createDecisionTree() {
        InventoryDecisionNode inventoryDecisionNode = new InventoryDecisionNode();
        BankDecisionNode bankDecisionNode = new BankDecisionNode();
        RocksDecisionNode rocksDecisionNode = new RocksDecisionNode();
        MineDecisionNode mineDecisionNode = new MineDecisionNode();
        BankRunnable bankRunnable = new BankRunnable();
        DropRunnable dropRunnable = new DropRunnable();
        MineRunnable mineRunnable = new MineRunnable();
        MiningRunnable miningRunnable = new MiningRunnable();
        WalkBankRunnable walkBankRunnable = new WalkBankRunnable();
        WalkRocksRunnable walkRocksRunnable = new WalkRocksRunnable();

        inventoryDecisionNode.setNodes(vars.option == Option.BANK ? bankDecisionNode : dropRunnable, rocksDecisionNode);
        bankDecisionNode.setNodes(bankRunnable, walkBankRunnable);
        rocksDecisionNode.setNodes(mineDecisionNode, walkRocksRunnable);
        mineDecisionNode.setNodes(mineRunnable, miningRunnable);

        return new DecisionTree(inventoryDecisionNode);
    }

    private void loadSettings() {
        if (settings.get("option") != null) {
            int option = Integer.valueOf(settings.get("option"));
            previousOptionIndex = option;
            if (option > 0 && settings.get("banking") != null && Boolean.valueOf(settings.get("banking"))) {
                --option;
            }
            cbOption.getSelectionModel().select(option);
        }
        if (settings.get("drop_pattern") != null) {
            cbDropPattern.getSelectionModel().select(Integer.valueOf(settings.get("drop_pattern")));
        }
        if (settings.get("stationary") != null) {
            chkbxStationary.setSelected(Boolean.valueOf(settings.get("stationary")));
        }
        if (settings.get("timed_actions") != null) {
            chkbxTimedActions.setSelected(Boolean.valueOf(settings.get("timed_actions")));
        }
        if (settings.get("reaction_times") != null) {
            chkbxReactionTimes.setSelected(Boolean.valueOf(settings.get("reaction_times")));
        }
    }

    private void saveSettings() {
        settings.set("option", String.valueOf(cbOption.getSelectionModel().getSelectedIndex()));
        settings.set("banking", String.valueOf(cbOption.getItems().contains(Option.BANK)));
        settings.set("drop_pattern", String.valueOf(cbDropPattern.getSelectionModel().getSelectedIndex()));
        settings.set("stationary", String.valueOf(chkbxStationary.isSelected()));
        settings.set("timed_actions", String.valueOf(chkbxTimedActions.isSelected()));
        settings.set("reaction_times", String.valueOf(chkbxReactionTimes.isSelected()));
        settings.set("tiles", serializeTiles());
        settings.save();
    }

    private void startRockObserver() {
        Thread rocksObserver = new Thread(() -> {
            Timing.waitCondition(this::isOpen, 5000);
            while (isOpen() && vars.isSelectingRocks) {
                General.sleep(500);
                Platform.runLater(this::updateOptions);
            }
        });
        rocksObserver.setName("Auto Miner GUI Rocks Observer Thread");
        rocksObserver.start();
    }

    private void updateOptions() {
        boolean isBankingSupported = false;
        for (RSTile tile : vars.rockTileList) {
            for (Location location : Location.values()) {
                if (location.getArea().contains(tile)) {
                    isBankingSupported = true;
                }
            }
        }
        if (isBankingSupported && cbOption.getItems().size() == 3) {
            cbOption.setItems(OPTIONS_BANK);
            cbOption.getSelectionModel().selectFirst();
        } else if (!isBankingSupported && cbOption.getItems().size() == 4) {
            cbOption.setItems(OPTIONS_DROP);
            cbOption.getSelectionModel().selectFirst();
        }
    }

    private RSArea getMiningArea() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int plane = 0;
        for (RSTile tile : vars.rockTileList) {
            plane = tile.getPlane();
            int x = tile.getX();
            int y = tile.getY();
            if (x < minX) {
                minX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        return new RSArea(
          new RSTile(minX - 2, minY - 2, plane),
          new RSTile(maxX + 2, maxY + 2, plane)
        );
    }

    private String serializeTiles() {
        String serialized = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            for (RSTile tile : vars.rockTileList) {
                os.writeObject(tile);
            }
            serialized = bos.toString();
            os.close();
            return serialized;
        } catch (IOException ignored) {
        }
        return serialized;
    }

    private void deserializeTiles(String tiles) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(tiles.getBytes());
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj;
            while ((obj = ois.readObject()) != null) {
                vars.rockTileList.add((RSTile) obj);
            }
            ois.close();
        } catch (IOException | ClassNotFoundException ignored) {
        }
    }

    private void loadPreviousTiles() {
        if (settings.get("tiles") != null) {
            deserializeTiles(settings.get("tiles"));
            if (!vars.rockTileList.isEmpty()) {
                updateOptions();
                cbOption.getSelectionModel().select(previousOptionIndex);
            }
        }
    }

    private Option getPreviousOption() {
        if (settings.get("option") != null && settings.get("banking") != null) {
            if (Integer.valueOf(settings.get("option")) == 0
              && Boolean.valueOf(settings.get("banking"))) {
                return Option.BANK;
            }
        }
        return cbOption.getSelectionModel().getSelectedItem();
    }
}
