package ru.alexsumin.northwest.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.alexsumin.northwest.math.Solver;
import ru.alexsumin.northwest.model.Good;
import ru.alexsumin.northwest.model.NumberTextField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Controller {

    @FXML
    TableView<Good> tableView;
    @FXML
    TableColumn<Good, String> info = new TableColumn<>("info");
    @FXML
    TableColumn<Good, String> needs1 = new TableColumn<>("Consumer1");
    @FXML
    TableColumn<Good, String> needs2 = new TableColumn<>("Consumer2");
    @FXML
    TableColumn<Good, String> needs3 = new TableColumn<>("Consumer3");
    @FXML
    TableColumn<Good, String> needs4 = new TableColumn<>("Consumer4");
    @FXML
    private NumberTextField consumer1Field = new NumberTextField();
    @FXML
    private NumberTextField consumer2Field = new NumberTextField();
    @FXML
    private NumberTextField consumer3Field = new NumberTextField();
    @FXML
    private NumberTextField consumer4Field = new NumberTextField();
    @FXML
    private NumberTextField supplier1Field = new NumberTextField();
    @FXML
    private NumberTextField supplier2Field = new NumberTextField();
    @FXML
    private NumberTextField supplier3Field = new NumberTextField();
    @FXML
    private NumberTextField supplier4Field = new NumberTextField();
    private ObservableList<Good> data;
    @FXML
    private ArrayList<NumberTextField> fields;
    private int[] test = {30, 40, 20, 10, 20, 30, 40, 10};
    //потребитель
    private int consumers[] = new int[4];
    //поставщик
    private int suppliers[] = new int[4];
    private Good selectedItem;
    private int columnIndex;
    private Solver solver;
    private int[][] solution;
    private boolean isCalculated;


    @FXML
    private void initialize() {
        data = FXCollections.observableArrayList(
                good("Supplier1"),
                good("Supplier2"),
                good("Supplier3"),
                good("Supplier4")
        );

        tableView.setEditable(true);
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        tableView.getItems().setAll(data);

        info.setCellValueFactory(new PropertyValueFactory<>("name"));
        needs1.setCellValueFactory(new PropertyValueFactory<>("first"));
        needs2.setCellValueFactory(new PropertyValueFactory<>("second"));
        needs3.setCellValueFactory(new PropertyValueFactory<>("third"));
        needs4.setCellValueFactory(new PropertyValueFactory<>("fourth"));


        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedItem = newValue;
            }
        });

        tableView.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                openNewValueDialog();
            }
        });

        tableView.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getTableColumn() != null) {
                columnIndex = newVal.getColumn();
            }
        });


        solver = new Solver();
    }

    @FXML
    private void openNewValueDialog() {
        try {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedItem.getCost(columnIndex - 1)));
            dialog.setTitle("Setting C");
            dialog.setHeaderText("Set C: ");
            dialog.setContentText("Please enter new value of C: ");
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/view/styles.css").toExternalForm());
            dialog.showAndWait().ifPresent(newCost -> {
                if (newCost != null) {
                    selectedItem.setCost(columnIndex - 1, Integer.valueOf(newCost));
                }
            });
            tableView.refresh();
        } catch (NumberFormatException ex) {

        } catch (Exception e) {

        }
    }


    @FXML
    private void setTestData() {

        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setNumber(BigDecimal.valueOf(test[i]));
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                data.get(i).setCost(j, (int) (Math.random() * 10));
            }
        }
        tableView.refresh();
    }


    @FXML
    private void calculate() {
        for (int i = 0; i < 4; i++) {
            consumers[i] = fields.get(i).getNumber().intValue();
            suppliers[i] = fields.get(i + 4).getNumber().intValue();
        }
        if (Arrays.stream(suppliers).sum() == Arrays.stream(consumers).sum()) {

            solver.setStorageStock(getStorageStock());
            solver.setShopNeeds(getShopNeeds());
            solver.setCostTable(getCostTable());
            solution = solver.calcNW();

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    data.get(i).setValueByIndex(j, solution[j][i]);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong balance condition!");
            alert.setHeaderText("The amounts of demands and suppliers are not equals!");
            alert.showAndWait();
        }
        isCalculated = true;
        tableView.refresh();
    }


    @FXML
    private void potentialsMethod() {
        int optimizedCost;
        if (isCalculated) {
            List result;
            result = solver.solveTask(64);
            optimizedCost = (int) result.get(0);
            int[][] routes = (int[][]) result.get(1);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    data.get(i).setValueByIndex(j, routes[j][i]);
                }
            }
            tableView.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("The solution was optimized");
            alert.setHeaderText("The solution was optimized! \n" +
                    "The total cost of transportation is " + optimizedCost);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("The problem is not solved");
            alert.setHeaderText("You should first solve problem before optimizing!");

            alert.showAndWait();
        }
    }

    public List getCostTable() {
        List storageCostList = new ArrayList();
        for (int i = 0; i < 4; i++) {
            List shopCost = new ArrayList();
            for (int j = 0; j < 4; j++) {
                int temp = data.get(i).getCost(j);
                shopCost.add(temp);
            }
            storageCostList.add(shopCost);
        }
        return storageCostList;
    }

    public List getStorageStock() {
        List<Integer> storageList = new ArrayList();
        for (int i = 0; i < 4; i++) {
            storageList.add(fields.get(i).getNumber().intValue());
        }
        return storageList;
    }

    public List getShopNeeds() {
        List<Integer> shopsList = new ArrayList();
        for (int i = 4; i < 8; i++) {
            shopsList.add(fields.get(i).getNumber().intValue());
        }
        return shopsList;
    }


    @FXML
    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setGraphic((Node) null);
        alert.setHeaderText("This simple program implements  " +
                "a northwest algorithm for solving the transportation problem.\n" +
                "Also, it gives an opportunity to optimize solution by potentials method.\n" +
                "Authors: Krivobokova Anastasia, Sharipova Milyausha, Sumin Aleksandr.\n" +
                "SPb SIT 2017");
        alert.showAndWait();
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void clear() {
        data.stream().forEach(good -> good.clear());
        fields.stream().forEach(numberTextField -> numberTextField.setNumber(BigDecimal.valueOf(0)));
        tableView.refresh();
        isCalculated = false;
    }

    private Good good(String name) {
        return new Good(name);
    }
}

