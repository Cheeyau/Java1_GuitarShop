package nl.exam.ui.dialogs;

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
import nl.exam.logic.OrderService;
import nl.exam.model.GuitarType;
import nl.exam.model.Order;
import nl.exam.model.OrderItem;
import nl.exam.model.StyledScene;

import java.util.ArrayList;

public class ConfirmDialog {

    private Stage stage;
    private VBox layOut;
    private Label titleLabel;
    private Order order;
    private Database db;
    private OrderService orderService;
    private TableView<OrderItem> table = new TableView<>();
    private ObservableList<OrderItem> itemsOL;
    private ArrayList<OrderItem> shopWallet;
    private boolean confirm;

    public Stage getStage() {
        return stage;
    }

    public ConfirmDialog(Database inDb, Order inOrder) {
        this.db = inDb;
        order = inOrder;
        stage = new Stage();
        orderService = new OrderService(db);
        shopWallet = order.getOrderItems();
        itemsOL = FXCollections.observableArrayList(shopWallet);
        confirm = false;
        stage.setTitle("Guitarshop FX - Confirm order");
        stage.setMinHeight(768);
        stage.setMinWidth(540);
        container(db);
        Scene mainScene = new StyledScene(layOut);
        stage.setScene(mainScene);
    }

    public boolean isConfirm() {
        return confirm;
    }

    private void container(Database db) {
        layOut = new VBox();
        layOut.setPadding(new Insets(20));
        layOut.setSpacing(20);
        HBox buttonRow = new HBox();
        buttonRow.setPadding(new Insets(20));
        buttonRow.setSpacing(20);
        titleLabel = new Label("Confirm order: # " + order.getOrderId());
        titleLabel.setId("Title");
        buttonRow.getChildren().addAll(btnConfirm(db), btnClose());
        layOut.getChildren().addAll(titleLabel, customerInfo(), tableStock(), lblTotalPrice(), buttonRow);
    }

    private HBox customerInfo() {
        HBox container = new HBox();
        VBox col1 = new VBox();
        VBox col2 = new VBox();
        VBox col3 = new VBox();
        Label customerLabel = new Label("Customer:");
        customerLabel.setId("Title");
        Label firstLabel = new Label("First name: " + order.getFirstName());
        Label streetLabel = new Label("Street address: " + order.getStreetName());
        Label phoneLabel = new Label("Phone number: " + order.getPhone());
        Label lastLabel = new Label("Last Name: " + order.getLastName());
        Label cityLabel = new Label("City: " + order.getCity());
        Label emailLabel = new Label("Email address: " + order.getEmail());
        col1.getChildren().addAll(customerLabel);
        col1.setMinWidth(300);
        col2.setMinWidth(225);
        col3.setMinWidth(225);
        col2.getChildren().addAll(firstLabel, streetLabel, phoneLabel);
        col3.getChildren().addAll(lastLabel, cityLabel, emailLabel);
        container.getChildren().addAll(col1, col2, col3);
        return container;
    }

    private Label lblTotalPrice() {
        Label totalPrice = new Label("Total Price: " + order.getTotalPriceOrder());
        totalPrice.setId("Title");
        return totalPrice;
    }

    private Button btnConfirm(Database inDb) {
        Button btnConfirm = new Button("Confirm");
        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                orderService.addOrderService(order);
                confirm = true;
                db = inDb;
                stage.close();
            }
        });
        return btnConfirm;
    }

    public Database getDb() {
        return db;
    }

    private Button btnClose() {
        Button btnClose = new Button("Cancel");
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        return btnClose;
    }

    private TableView<OrderItem> tableStock() {
        table.setEditable(false);
        table.getSelectionModel().setCellSelectionEnabled(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn amountCol = new TableColumn("Quantity");
        amountCol.setMinWidth(100);
        amountCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("orderAmount"));

        TableColumn brandCol = new TableColumn("Brand");
        brandCol.setMinWidth(150);
        brandCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("brand"));

        TableColumn modelCol = new TableColumn("Model");
        modelCol.setMinWidth(150);
        modelCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("model"));

        TableColumn guitarTypeCol = new TableColumn("Guitar Type");
        guitarTypeCol.setMinWidth(130);
        guitarTypeCol.setCellValueFactory(new PropertyValueFactory<OrderItem, GuitarType>("guitarType"));

        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("price"));

        table.getColumns().addAll(amountCol, brandCol, modelCol, guitarTypeCol, priceCol);
        populateLV();
        return table;
    }

    private void populateLV() {
        table.setItems(itemsOL);
    }
}
