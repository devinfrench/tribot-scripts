package scripts.smither.gui;

import com.allatori.annotations.DoNotRename;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import scripts.api.script.JavaFXGUI;
import scripts.api.util.Internet;
import scripts.api.util.ScriptSettings;
import scripts.smither.data.Bar;
import scripts.smither.data.Item;
import scripts.smither.data.Vars;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUI extends JavaFXGUI implements Initializable {

    // Selection Pane
    @FXML
    @DoNotRename
    private AnchorPane paneSelection;
    @FXML
    @DoNotRename
    private Button btnSmelting;
    @FXML
    @DoNotRename
    private Button btnSmithing;
    @FXML
    @DoNotRename
    private Button btnPrevious;

    // Smelting Pane
    @FXML
    @DoNotRename
    private AnchorPane paneSmelting;
    @FXML
    @DoNotRename
    private ComboBox<Bar> cbSmelting;
    @FXML
    @DoNotRename
    private Button btnStartSmelting;

    private ObservableList<Bar> cbSmeltingData = FXCollections.observableArrayList(Bar.values());

    // Smithing Pane
    @FXML
    @DoNotRename
    private AnchorPane paneSmithing;
    @FXML
    @DoNotRename
    private ComboBox<Item> cbSmithing;
    @FXML
    @DoNotRename
    private ComboBox<Bar> cbSmithingBar;
    @FXML
    @DoNotRename
    private Button btnStartSmithing;

    private ObservableList<Item> cbSmithingData = FXCollections.observableArrayList(Item.values());

    private Vars vars;
    private ScriptSettings settings;

    public GUI(Vars vars) {
        super(vars.script);
        this.vars = vars;
        settings = new ScriptSettings(vars.script);
        init();
    }

    @Override
    public String getFxml() {
        return Internet.getText("http://encodedscripting.com/resources/smither/gui.fxml");
    }

    @Override
    public String[] getStyleSheets() {
        return new String[0];
    }

    @Override
    public Object getController() {
        return this;
    }

    @Override
    public Image getIcon() {
        return Internet.getJavaFXImage("http://encodedscripting.com/resources/smither/gui_icon.png");
    }

    @Override
    public StageStyle getStageStyle() {
        return StageStyle.DECORATED;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSmelting.setItems(cbSmeltingData);
        cbSmelting.getSelectionModel().selectFirst();
        cbSmithing.setItems(cbSmithingData);
        cbSmithing.getSelectionModel().selectFirst();
        ArrayList<Bar> cbSmithingBarData = new ArrayList<>();
        for (Bar bar : Bar.values()) {
            if (bar != Bar.CANNONBALL && bar != Bar.SILVER && bar != Bar.GOLD) {
                cbSmithingBarData.add(bar);
            }
        }
        cbSmithingBar.setItems(FXCollections.observableArrayList(cbSmithingBarData));
        cbSmithingBar.getSelectionModel().selectFirst();

        loadSettings();

        btnSmelting.setOnAction(event -> {
            paneSelection.setVisible(false);
            paneSmelting.setVisible(true);
        });

        btnSmithing.setOnAction(event -> {
            paneSelection.setVisible(false);
            paneSmithing.setVisible(true);
        });

        btnPrevious.setOnAction(event -> handlePreviousStart());
        btnStartSmelting.setOnAction(event -> handleSmeltingStart());
        btnStartSmithing.setOnAction(event -> handleSmithingStart());
    }

    private void handlePreviousStart() {
        vars.bar = vars.isSmelting ? cbSmelting.getValue() : cbSmithingBar.getValue();
        vars.item = cbSmithing.getValue();
        close();
    }

    private void handleSmeltingStart() {
        vars.isSmelting = true;
        vars.bar = cbSmelting.getValue();
        saveSettings();
        close();
    }

    private void handleSmithingStart() {
        vars.isSmelting = false;
        vars.item = cbSmithing.getValue();
        vars.bar = cbSmithingBar.getValue();
        saveSettings();
        close();
    }

    private void loadSettings() {
        cbSmelting.getSelectionModel().select(Integer.parseInt(settings.get("bar", "0")));
        cbSmithing.getSelectionModel().select(Integer.parseInt(settings.get("item", "0")));
        cbSmithingBar.getSelectionModel().select(Integer.parseInt(settings.get("smith_bar", "0")));
        vars.isSmelting = Boolean.valueOf(settings.get("smelting", "true"));
    }

    private void saveSettings() {
        settings.set("bar", String.valueOf(cbSmelting.getSelectionModel().getSelectedIndex()));
        settings.set("item", String.valueOf(cbSmithing.getSelectionModel().getSelectedIndex()));
        settings.set("smith_bar", String.valueOf(cbSmithingBar.getSelectionModel().getSelectedIndex()));
        settings.set("smelting", String.valueOf(vars.isSmelting));
        settings.save();
    }
}
