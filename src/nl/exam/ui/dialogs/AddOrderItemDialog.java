package nl.exam.ui.dialogs;

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
import javafx.stage.Stage;
import nl.exam.data.Database;
import nl.exam.logic.StockService;
import nl.exam.model.*;
import nl.exam.model.StyledScene;
import nl.exam.ui.windows.PopUp;

import java.util.ArrayList;

public class AddOrderItemDialog {
    private Stage stage;
    private Database db;
    private StockService stockService;
    private Label titleLabel = new Label("Add item");
    private ObservableList<Stock> stocksOb;
    private Stock selectedStockItemEM;
    private OrderItem selectedOrderItemEM;
    private TableView<Stock> table = new TableView<>();
    private ArrayList<Stock> stocks;
    private TextField userInput;
    private PopUp popUp;
    private int amount;

    public Stage getStage() {
        return stage;
    }

    public AddOrderItemDialog(Database inDb) {
        this.db = inDb;
        this.stockService = new StockService(db);
        stocks = stockService.getStocksDB();
        stocksOb = FXCollections.observableArrayList(stocks);
        stage = new Stage();
        stage.setTitle("Guitarshop FX - Add item");
        stage.setMinWidth(1024);
        stage.setHeight(680);
        titleLabel.setId("Title");
        VBox layOut = container();
        selectOrderItem();
        Scene addOrderItemScene = new StyledScene(layOut);
        addOrderItemScene.getStylesheets().add("resources/css/style.css");
        stage.setScene(addOrderItemScene);
    }

    private void selectOrderItem() {
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Stock>() {
            @Override
            public void changed(ObservableValue<? extends Stock> observableValue, Stock OI, Stock selectedOI) {
                selectedStockItemEM = selectedOI;
            }
        });
    }

    private VBox container() {
        VBox layOut = new VBox();
        layOut.setPadding(new Insets(10));
        layOut.setSpacing(20);
        HBox controller = new HBox();
        controller.setPadding(new Insets(10));
        controller.setSpacing(20);
        userInput = new TextField();
        controller.getChildren().addAll(userInput, btnAdd(), btnCancel());
        layOut.getChildren().addAll(titleLabel, tableStocks(), controller);
        return layOut;
    }

    private Button btnAdd() {
        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (selectedStockItemEM.getStockAmount() != 0) {
                        try {
                            amount = Integer.parseInt(userInput.getText());
                            if ((selectedStockItemEM.getStockAmount() - amount) >= 0) {
                                selectedOrderItemEM = new OrderItem(amount, selectedStockItemEM.getStockItem());
                                selectedStockItemEM.setStockAmount(selectedStockItemEM.getStockAmount() - amount);
                                stockService.editStock(selectedStockItemEM);
                                stage.close();
                            } else {
                                popUp = new PopUp("Amount cant be negative, try again.");
                                popUp.getStage().show();
                            }
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
        return btnAdd;
    }

    private Button btnCancel() {
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedStockItemEM = null;
                selectedOrderItemEM = null;
                stage.close();
            }
        });
        return btnCancel;
    }

    public OrderItem getSelectedOrderItemEM() {
        return selectedOrderItemEM;
    }

    private TableView<Stock> tableStocks() {

        table.setEditable(false);
        table.getSelectionModel().setCellSelectionEnabled(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn amountCol = new TableColumn("In stock");
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

        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(130);
        priceCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("price"));

        table.getColumns().addAll(amountCol, brandCol, modelCol, isAcousticCol, guitarTypeCol, priceCol);
        populateLV();
        return table;
    }

    private void populateLV() {
        table.setItems(stocksOb);
    }
}