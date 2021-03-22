package nl.exam.ui.scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.exam.data.Database;
import nl.exam.logic.StockService;
import nl.exam.model.GuitarType;
import nl.exam.model.Stock;
import nl.exam.model.StyledScene;
import nl.exam.ui.windows.PopUp;

public class StockScene {
    private TableView<Stock> table = new TableView<>();
    private StockService stockService;
    private Scene scene;
    private Stock selectStockEm;
    private PopUp popUp;
    private ObservableList<Stock> stocks;
    private Database db;
    public Scene getScene() {
        return scene;
    }

    public String returnTitle() {
        return "Guitarshop FX - Stock maintenance";
    }


    public StockScene(Database db) {
        this.db = db;
        stockService = new StockService(db);
        stocks = FXCollections.observableArrayList(stockService.getStocksDB());
        VBox layOut = new VBox();
        layOut.setPadding(new Insets(10));
        layOut.setSpacing(20);
        Label titleLabel = new Label("Stock maintenance");
        titleLabel.setId("Title");
        HBox editStockItemMenu = new HBox();
        Label quantityLabel = new Label("Quantity");
        TextField userInput = new TextField();
        editStockItemMenu.setPadding(new Insets(20,0,0,0));
        editStockItemMenu.setSpacing(10);
        Button addStockButton = new Button("Add");
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Stock>() {
            @Override
            public void changed(ObservableValue<? extends Stock> observableValue, Stock stock, Stock selectedStock) {
                selectStockEm = selectedStock;
            }
        });
        CheckBox cbNegate = new CheckBox("Negate");
        editStock(userInput, addStockButton, cbNegate);
        editStockItemMenu.getChildren().addAll(quantityLabel, userInput, checkBoxNegate(addStockButton, cbNegate), addStockButton);

        layOut.getChildren().addAll(titleLabel, tableStock(table), editStockItemMenu);
        scene = new StyledScene(layOut);
    }

    private void editStock(TextField userInput, Button addStockButton, CheckBox CBNegate) {
        addStockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (selectStockEm.getStockAmount() != 0) {
                        try {
                            int amount  = Integer.parseInt(userInput.getText());
                            if (CBNegate.isSelected()) {
                                if ((selectStockEm.getStockAmount() - amount) > 0) {
                                    selectStockEm.setStockAmount((selectStockEm.getStockAmount() - amount));
                                } else {
                                    popUp = new PopUp("Amount cant be negative, try again.");
                                    popUp.getStage().show();
                                    throw new ArithmeticException("Amount cant be negative, try again.");
                                }
                            } else {
                                selectStockEm.setStockAmount((selectStockEm.getStockAmount() + amount));
                            }
                            stockService.editStock(selectStockEm);
                            stocks.clear();
                            stocks = FXCollections.observableArrayList(stockService.getStocksDB());
                            populateLV();
                            table.getSelectionModel().clearSelection();
                        } catch (NumberFormatException ex) {
                            popUp = new PopUp("Could not read the number, try again.");
                            popUp.getStage().show();
                        }
                    }

                } catch (Exception ex) {
                    popUp = new PopUp("Select an item to change the amount, try again.");
                    popUp.getStage().show();
                }
            }
        });
    }

    private CheckBox checkBoxNegate(Button addStockButton, CheckBox cbNegate) {
        cbNegate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cbNegate.isSelected()) {
                    addStockButton.setText("Remove");
                } else {
                    addStockButton.setText("Add");
                }
            }
        });
        return cbNegate;
    }

    private TableView<Stock> tableStock(TableView<Stock> tableView) {

        tableView.setEditable(false);
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn amountCol = new TableColumn("Quantity");
        amountCol.setMinWidth(100);
        amountCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("stockAmount"));

        TableColumn brandCol = new TableColumn("Brand");
        brandCol.setMinWidth(200);
        brandCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("brand"));

        TableColumn modelCol = new TableColumn("Model");
        modelCol.setMinWidth(200);
        modelCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("model"));

        TableColumn isAcousticCol = new TableColumn("Acoustic");
        isAcousticCol.setMinWidth(100);
        isAcousticCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("isAcoustic"));

        TableColumn guitarTypeCol = new TableColumn("Guitar Type");
        guitarTypeCol.setMinWidth(130);
        guitarTypeCol.setCellValueFactory(new PropertyValueFactory<Stock, GuitarType>("guitarType"));


        tableView.getColumns().addAll(amountCol, brandCol, modelCol, isAcousticCol, guitarTypeCol);
        populateLV();
        return tableView;
    }

    private void populateLV() {
        table.setItems(stocks);
    }
}
