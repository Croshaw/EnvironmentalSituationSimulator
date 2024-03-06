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

import java.net.URL;
import java.util.ResourceBundle;

public class MapSettingsController implements Initializable {
    @FXML
    TextField columnsField;
    @FXML
    TextField rowsField;
    @FXML
    GridPane table;
    MapSettings curMapSettings;

    private void setupTable() {
        table.getChildren().clear();
        table.getColumnConstraints().clear();
        table.getRowConstraints().clear();
        for(int i = 0; i < curMapSettings.getRows();i++) {
            table.addRow(i);
        }
        for(int i = 0; i < curMapSettings.getColumns();i++) {
            Node[] nodes = new Node[curMapSettings.getRows()];
            for(int j = 0; j < curMapSettings.getRows(); j++) {
                var button = new Button(curMapSettings.getMap()[j][i] ? "X" : "");
                int finalJ = j;
                int finalI = i;
                button.setOnAction(actionEvent -> {
                    try {
                        System.out.print("Click");
                        if (button.getText().equals("X") && curMapSettings.getCompanyCount() > 5) {
                            curMapSettings.removeCompany(finalJ, finalI);
                            button.setText("");
                            System.out.println(":remove");
                        }
                        else if(button.getText().isEmpty() && curMapSettings.getCompanyCount() < 12) {
                            curMapSettings.addCompany(finalJ, finalI);
                            button.setText("X");
                            System.out.println(":add");
                        }
                    }
                    catch (WrongCoordinatesException e) {
                        System.out.println("excep");
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
        curMapSettings.resize(rows, cols);
        setupTable();
    }
    @FXML
    private void fillRandomly() {
        curMapSettings.fillRandomly(SimulationSettings.RANDOM);
        rowsField.textProperty().setValue(String.valueOf(curMapSettings.getRows()));
        columnsField.textProperty().setValue(String.valueOf(curMapSettings.getColumns()));
        setupTable();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        curMapSettings = SimulationSettings.MAP.clone();
        rowsField.setTextFormatter(new TextFormatter<>(Filters.integerFilter));
        rowsField.setText(Integer.toString(curMapSettings.getRows()));
        columnsField.setTextFormatter(new TextFormatter<>(Filters.integerFilter));
        columnsField.setText(Integer.toString(curMapSettings.getColumns()));
        loadMap();
    }
    @FXML
    private void save() {
        SimulationSettings.MAP = curMapSettings;
    }
}
