package me.croshaw.ess.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import me.croshaw.ess.exception.WrongCoordinatesException;
import me.croshaw.ess.settings.MapSettings;
import me.croshaw.ess.settings.SimulationSettings;
import me.croshaw.ess.util.Filters;
import me.croshaw.ess.util.RandomUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class MapSettingsController implements Initializable {
    @FXML
    TextField columnsField;
    @FXML
    TextField rowsField;
    @FXML
    GridPane table;
    MapSettings mapSettings;

    private void setupTable() {
        table.getChildren().clear();
        table.getColumnConstraints().clear();
        table.getRowConstraints().clear();
        for(int i = 0; i < mapSettings.getRows(); i++) {
            table.addRow(i);
        }
        for(int i = 0; i < mapSettings.getColumns(); i++) {
            Node[] nodes = new Node[mapSettings.getRows()];
            for(int j = 0; j < mapSettings.getRows(); j++) {
                var button = new Button(mapSettings.getMap()[j][i] == 0 ? "" : Integer.toString(mapSettings.getMap()[j][i]));
                int finalJ = j;
                int finalI = i;
                button.setOnAction(actionEvent -> {
                    try {
                        if(button.getText().isEmpty() && mapSettings.getCompanyCount() < 12) {
                            int priority = mapSettings.getFreePriorities().getFirst();
                            mapSettings.addCompany(finalJ, finalI, priority);
                            button.setText(Integer.toString(priority));
                        } else if( mapSettings.getCompanyCount() > 5) {
                            mapSettings.removeCompany(finalJ, finalI);
                            button.setText("");
                        }
                    }
                    catch (WrongCoordinatesException e) {
                        System.err.println(e.getMessage());
                    }
                });
                nodes[j] = button;
                GridPane.setFillHeight(button, true);
                GridPane.setFillWidth(button, true);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);
            }
            table.addColumn(i, nodes);
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            colConstraints.setHalignment(HPos.CENTER);
            colConstraints.setFillWidth(true);

            table.getColumnConstraints().add(colConstraints);
        }
    }
    @FXML
    private void loadMap() {
        int rows = Integer.parseInt(rowsField.getText());
        int cols = Integer.parseInt(columnsField.getText());
        mapSettings.resize(rows, cols);
        setupTable();
    }
    @FXML
    private void fillRandomly() {
        mapSettings.fillRandomly(RandomUtils.RANDOM);
        rowsField.textProperty().setValue(String.valueOf(mapSettings.getRows()));
        columnsField.textProperty().setValue(String.valueOf(mapSettings.getColumns()));
        setupTable();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapSettings = SimulationSettings.MAP.clone();
        rowsField.setTextFormatter(new TextFormatter<>(Filters.integerFilter));
        rowsField.setText(Integer.toString(mapSettings.getRows()));
        columnsField.setTextFormatter(new TextFormatter<>(Filters.integerFilter));
        columnsField.setText(Integer.toString(mapSettings.getColumns()));
        loadMap();
    }
    @FXML
    private void save() {
        SimulationSettings.MAP = mapSettings;
    }
}
