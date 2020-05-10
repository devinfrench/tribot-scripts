package scripts.woodcutter.gui;

import com.allatori.annotations.DoNotRename;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSTile;
import scripts.api.script.JavaFXGUI;
import scripts.api.script.frameworks.tree.DecisionTree;
import scripts.api.types.RSArea;
import scripts.api.util.Internet;
import scripts.api.util.ScriptSettings;
import scripts.api.util.Timer;
import scripts.woodcutter.data.Location;
import scripts.woodcutter.data.Option;
import scripts.woodcutter.data.Tree;
import scripts.woodcutter.data.Vars;
import scripts.woodcutter.decisions.BankDecisionNode;
import scripts.woodcutter.decisions.CutDecisionNode;
import scripts.woodcutter.decisions.InventoryDecisionNode;
import scripts.woodcutter.decisions.NestDecisionNode;
import scripts.woodcutter.decisions.TreesDecisionNode;
import scripts.woodcutter.runnables.BankRunnable;
import scripts.woodcutter.runnables.CutRunnable;
import scripts.woodcutter.runnables.CuttingRunnable;
import scripts.woodcutter.runnables.DropRunnable;
import scripts.woodcutter.runnables.NestRunnable;
import scripts.woodcutter.runnables.WalkBankRunnable;
import scripts.woodcutter.runnables.WalkTreesRunnable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class GUI extends JavaFXGUI implements Initializable {

    private static final ObservableList<Location> TREE_LOCATIONS = FXCollections.observableArrayList(
        Location.LUMBRIDGE_TREE,
        Location.VARROCK_WEST_TREE,
        Location.SEERS_VILLAGE_TREE
    );
    private static final ObservableList<Location> OAK_LOCATIONS = FXCollections.observableArrayList(
        Location.VARROCK_WEST_OAK,
        Location.SEERS_VILLAGE_OAK
    );
    private static final ObservableList<Location> WILLOW_LOCATIONS = FXCollections.observableArrayList(
        Location.DRAYNOR_WILLOW,
        Location.PORT_SARIM_WILLOW,
        Location.CATHERBY_WILLOW,
        Location.SEERS_VILLAGE_WILLOW,
        Location.BARBARIAN_OUTPOST_WILLOW
    );
    private static final ObservableList<Location> MAPLE_LOCATIONS = FXCollections.observableArrayList(
        Location.SEERS_VILLAGE_MAPLE
    );
    private static final ObservableList<Location> TEAK_LOCATIONS = FXCollections.observableArrayList(
        Location.CASTLE_WARS_TEAK
    );
    private static final ObservableList<Location> YEW_LOCATIONS = FXCollections.observableArrayList(
        Location.VARROCK_CASTLE_YEW,
        Location.EDGEVILLE_YEW,
        Location.RIMMINGTON_YEW,
        Location.CATHERBY_YEW,
        Location.SEERS_VILLAGE_YEW,
        Location.WOODCUTTING_GUILD_YEW
    );
    private static final ObservableList<Location> MAGIC_LOCATIONS = FXCollections.observableArrayList(
        Location.RANGING_GUILD_MAGIC,
        Location.SORCERERS_TOWER_MAGIC,
        Location.WOODCUTTING_GUILD_MAGIC
    );

    @FXML
    @DoNotRename
    private Button btnStart;

    @FXML
    @DoNotRename
    private TabPane tabPane;

    @FXML
    @DoNotRename
    private ComboBox<Location> cbPresetLocation;

    @FXML
    @DoNotRename
    private ComboBox<Tree> cbPresetTree;

    @FXML
    @DoNotRename
    private ComboBox<Option> cbPresetOption;

    @FXML
    @DoNotRename
    private ComboBox<Tree> cbCustomTree;

    @FXML
    @DoNotRename
    private ComboBox<Option> cbCustomOption;

    @FXML
    @DoNotRename
    private CheckBox chkbxReactionTimes;

    @FXML
    @DoNotRename
    private CheckBox chkbxTimedActions;

    @FXML
    @DoNotRename
    private CheckBox chkbxNests;

    @FXML
    @DoNotRename
    private ComboBox<Inventory.DROPPING_PATTERN> cbDropPattern;

    @FXML
    @DoNotRename
    private TextField tfLevel;

    @FXML
    @DoNotRename
    private TextField tfHours;

    private Vars vars;
    private ScriptSettings settings;
    private int lastSelectedTabIndex;

    public GUI(Vars vars) {
        super(vars.script);
        this.vars = vars;
        settings = new ScriptSettings(vars.script);
        init();
    }

    @Override
    public String getFxml() {
        return Internet.getText("http://encodedscripting.com/resources/woodcutter/woodcutter.fxml");
    }

    @Override
    public String[] getStyleSheets() {
        return new String[] {
          "http://encodedscripting.com/resources/woodcutter/woodcutter.css"
        };
    }

    @Override
    public Object getController() {
        return this;
    }

    @Override
    public Image getIcon() {
        return Internet.getJavaFXImage("http://encodedscripting.com/resources/woodcutter/img/icon.png");
    }

    @Override
    public StageStyle getStageStyle() {
        return StageStyle.DECORATED;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbPresetTree.setItems(FXCollections.observableArrayList(Tree.values()));
        cbPresetTree.getSelectionModel().selectFirst();
        cbPresetLocation.setItems(TREE_LOCATIONS);
        cbPresetLocation.getSelectionModel().selectFirst();
        cbPresetOption.setItems(FXCollections.observableArrayList(Option.values()));
        cbPresetOption.getSelectionModel().selectFirst();

        cbCustomTree.setItems(FXCollections.observableArrayList(Tree.values()));
        cbCustomTree.getSelectionModel().selectFirst();
        cbCustomOption.setItems(FXCollections.observableArrayList(Option.values()));
        cbCustomOption.getSelectionModel().selectFirst();

        ObservableList<Inventory.DROPPING_PATTERN> cbDropPatternData = FXCollections.observableArrayList(
            Inventory.DROPPING_PATTERN.TOP_TO_BOTTOM,
            Inventory.DROPPING_PATTERN.LEFT_TO_RIGHT
        );
        cbDropPattern.setItems(cbDropPatternData);
        cbDropPattern.getSelectionModel().selectFirst();

        loadSettings();

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

        cbPresetTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cbPresetLocation.setItems(getPresetLocations(newValue));
            cbPresetLocation.getSelectionModel().selectFirst();
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            int index = tabPane.getSelectionModel().getSelectedIndex();
            if (index != 2) {
                lastSelectedTabIndex = index;
            }
            if (vars.isSelectingTrees = index == 1) {
                vars.tree = cbCustomTree.getValue();
            }
        });
        cbCustomTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> vars.tree = newValue);

        btnStart.setOnAction((event) -> handleStart());
    }

    private void handleStart() {
        int tabIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (tabIndex == 1 || tabIndex == 2 && lastSelectedTabIndex == 1) {
            vars.location = null;
            vars.tree = cbCustomTree.getValue();
            vars.option = cbCustomOption.getValue();
            if (vars.treeTileList.isEmpty() && settings.get("tiles") != null) {
                deserializeTiles(settings.get("tiles"));
            }
            vars.customTreeArea = getCustomTreeArea();
        } else {
            vars.location = cbPresetLocation.getValue();
            vars.tree = cbPresetTree.getValue();
            vars.option = cbPresetOption.getValue();
        }
        vars.isWaitingReactionTime = chkbxReactionTimes.isSelected();
        vars.isTimedActions = chkbxTimedActions.isSelected();
        vars.isPickingUpNests = chkbxNests.isSelected();
        vars.droppingPattern = cbDropPattern.getValue();
        DecisionTree decisionTree = createDecisionTree();
        long stopTime = tfHours.getText().length() > 0 ? Long.parseLong(tfHours.getText()) * 3600000L + General.random(-300000, 300000) : Long.MAX_VALUE;
        Timer stopTimer = new Timer(stopTime);
        int stopLevel = tfLevel.getText().length() > 0 ? Integer.parseInt(tfLevel.getText()) : 100;
        decisionTree.setStopCondition(() -> stopTimer.isFinished() || Skills.SKILLS.WOODCUTTING.getActualLevel() >= stopLevel);
        vars.script.setDecisionTree(decisionTree);
        saveSettings();
        vars.isSelectingTrees = false;
        close();
    }

    private DecisionTree createDecisionTree() {
        InventoryDecisionNode inventoryDecisionNode = new InventoryDecisionNode();
        BankDecisionNode bankDecisionNode = new BankDecisionNode();
        CutDecisionNode cutDecisionNode = new CutDecisionNode();
        NestDecisionNode nestDecisionNode = new NestDecisionNode();
        TreesDecisionNode treesDecisionNode = new TreesDecisionNode();

        BankRunnable bankRunnable = new BankRunnable();
        CutRunnable cutRunnable = new CutRunnable();
        CuttingRunnable cuttingRunnable = new CuttingRunnable();
        DropRunnable dropRunnable = new DropRunnable();
        NestRunnable nestRunnable = new NestRunnable();
        WalkBankRunnable walkBankRunnable = new WalkBankRunnable();
        WalkTreesRunnable walkTreesRunnable = new WalkTreesRunnable();

        inventoryDecisionNode.setNodes(vars.option == Option.BANK ? bankDecisionNode : dropRunnable, treesDecisionNode);
        bankDecisionNode.setNodes(bankRunnable, walkBankRunnable);
        treesDecisionNode.setNodes(vars.isPickingUpNests ? nestDecisionNode : cutDecisionNode, walkTreesRunnable);
        nestDecisionNode.setNodes(nestRunnable, cutDecisionNode);
        cutDecisionNode.setNodes(cutRunnable, cuttingRunnable);

        return new DecisionTree(inventoryDecisionNode);
    }

    private ObservableList<Location> getPresetLocations(Tree tree) {
        switch (tree) {
            case TREE:
                return TREE_LOCATIONS;
            case OAK:
                return OAK_LOCATIONS;
            case WILLOW:
                return WILLOW_LOCATIONS;
            case MAPLE:
                return MAPLE_LOCATIONS;
            case TEAK:
                return TEAK_LOCATIONS;
            case YEW:
                return YEW_LOCATIONS;
            case MAGIC:
                return MAGIC_LOCATIONS;
            default:
                return TREE_LOCATIONS;
        }
    }

    private void loadSettings() {
        if (settings.get("preset_tree") != null) {
            cbPresetTree.getSelectionModel().select(Integer.valueOf(settings.get("preset_tree")));
            cbPresetLocation.setItems(getPresetLocations(cbPresetTree.getValue()));
        }
        if (settings.get("preset_location") != null) {
            cbPresetLocation.getSelectionModel().select(Integer.valueOf(settings.get("preset_location")));
        }
        if (settings.get("preset_option") != null) {
            cbPresetOption.getSelectionModel().select(Integer.valueOf(settings.get("preset_option")));
        }
        if (settings.get("custom_tree") != null) {
            cbCustomTree.getSelectionModel().select(Integer.valueOf(settings.get("custom_tree")));
        }
        if (settings.get("custom_option") != null) {
            cbCustomOption.getSelectionModel().select(Integer.valueOf(settings.get("custom_option")));
        }
        if (settings.get("reaction_times") != null) {
            chkbxReactionTimes.setSelected(Boolean.valueOf(settings.get("reaction_times")));
        }
        if (settings.get("timed_actions") != null) {
            chkbxTimedActions.setSelected(Boolean.valueOf(settings.get("timed_actions")));
        }
        if (settings.get("nests") != null) {
            chkbxNests.setSelected(Boolean.valueOf(settings.get("nests")));
        }
        if (settings.get("dropping_pattern") != null) {
            cbDropPattern.getSelectionModel().select(Integer.valueOf(settings.get("dropping_pattern")));
        }
    }

    private void saveSettings() {
        settings.set("preset_tree", String.valueOf(cbPresetTree.getSelectionModel().getSelectedIndex()));
        settings.set("preset_location", String.valueOf(cbPresetLocation.getSelectionModel().getSelectedIndex()));
        settings.set("preset_option", String.valueOf(cbPresetOption.getSelectionModel().getSelectedIndex()));
        settings.set("custom_tree", String.valueOf(cbCustomTree.getSelectionModel().getSelectedIndex()));
        settings.set("custom_option", String.valueOf(cbCustomOption.getSelectionModel().getSelectedIndex()));
        settings.set("reaction_times", String.valueOf(chkbxReactionTimes.isSelected()));
        settings.set("timed_actions", String.valueOf(chkbxTimedActions.isSelected()));
        settings.set("nests", String.valueOf(chkbxNests.isSelected()));
        settings.set("dropping_pattern", String.valueOf(cbDropPattern.getSelectionModel().getSelectedIndex()));
        settings.set("tiles", serializeTiles());
        settings.save();
    }

    private RSArea getCustomTreeArea() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (RSTile tile : vars.treeTileList) {
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
            new RSTile(minX - 5, minY - 5, 0),
            new RSTile(maxX + 6, maxY + 6, 0)
        );
    }

    private String serializeTiles() {
        String serialized = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            for (RSTile tile : vars.treeTileList) {
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
                vars.treeTileList.add((RSTile) obj);
            }
            ois.close();
        } catch (IOException | ClassNotFoundException ignored) {
        }
    }
}
