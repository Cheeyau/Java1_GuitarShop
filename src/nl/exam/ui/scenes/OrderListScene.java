package nl.exam.ui.scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import nl.exam.data.Database;
import nl.exam.logic.OrderService;
import nl.exam.model.*;


import java.util.ArrayList;

public class OrderListScene {
    private Scene scene;
    private OrderService orderService;
    private Label titleLabel;
    private Label titleDetailLabel;
    private Database db;

    private TableView<Order> orderTable = new TableView<>();
    private TableView<OrderItem> orderItemsTable = new TableView<>();

    private ObservableList<OrderItem> orderItemsOL;
    private ObservableList<Order> ordersOL;
    private ArrayList<OrderItem> orderItems;
    private ArrayList<Order> orders;
    private Order selectedOderEM;
    private OrderScene orderScene;

    public Scene getScene() {
        return scene;
    }

    public String returnTitle() {
        return "Guitarshop FX - Order overview";
    }

    public OrderListScene(User user, Database dbIn) {
        this.db = dbIn;
        orderScene = new OrderScene(user, db);
        orderService = new OrderService(db);
        orders = orderService.getOrdersService();
        ordersOL = FXCollections.observableArrayList(orders);
        titleLabel = new Label("Order Overview");
        titleLabel.setId("Title");
        titleDetailLabel = new Label("Detail");
        titleDetailLabel.setId("Title");
        VBox layOut = new VBox();
        layOut.setPadding(new Insets(10));
        layOut.setSpacing(20);
        selectOrderItem();
        layOut.getChildren().addAll(titleLabel, tableOrder(), titleDetailLabel, tableOrderItem());
        scene = new StyledScene(layOut);
    }


    private void selectOrderItem() {
        orderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
            @Override
            public void changed(ObservableValue<? extends Order> observableValue, Order order, Order selectedOrder) {
                selectedOderEM = selectedOrder;
                orderItems = selectedOderEM.getOrderItems();
                populateOITable();
            }
        });
    }

    private TableView<Order> tableOrder() {
        orderTable.setEditable(false);
        orderTable.getSelectionModel().setCellSelectionEnabled(false);
        orderTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn orderIdCol = new TableColumn("Id");
        orderIdCol.setMinWidth(30);
        orderIdCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderId"));

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setMinWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderTime"));

        TableColumn customerNameCol = new TableColumn("Customer name");
        customerNameCol.setMinWidth(150);
        customerNameCol.setCellValueFactory(new PropertyValueFactory<Order, String>("fullName"));

        TableColumn cityCol = new TableColumn("City");
        cityCol.setMinWidth(130);
        cityCol.setCellValueFactory(new PropertyValueFactory<Order, String>("city"));

        TableColumn phoneCol = new TableColumn("Phone");
        phoneCol.setMinWidth(120);
        phoneCol.setCellValueFactory(new PropertyValueFactory<Order, String>("phone"));

        TableColumn emailCol = new TableColumn("Email Address");
        emailCol.setMinWidth(150);
        emailCol.setCellValueFactory(new PropertyValueFactory<Order, String>("email"));

        TableColumn countCol = new TableColumn("count");
        countCol.setMinWidth(75);
        countCol.setCellValueFactory(new PropertyValueFactory<Order, String>("count"));

        TableColumn totalPriceCol = new TableColumn("Total");
        totalPriceCol.setMinWidth(100);
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<Order, String>("totalPriceOrder"));

        orderTable.getColumns().addAll(orderIdCol, dateCol, customerNameCol, phoneCol, emailCol, countCol, totalPriceCol);
        populateOTable();
        return orderTable;
    }

    private void populateOTable() {
        ordersOL = FXCollections.observableArrayList(orders);
        orderTable.setItems(ordersOL);
    }

    private TableView<OrderItem> tableOrderItem() {
        orderItemsTable.setEditable(false);
        orderItemsTable.getSelectionModel().setCellSelectionEnabled(false);
        TableColumn unitIdCol = new TableColumn("Unit Id");
        unitIdCol.setMinWidth(80);
        unitIdCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("unitId"));

        TableColumn amountCol = new TableColumn("Quantity");
        amountCol.setMinWidth(100);
        amountCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("orderAmount"));

        TableColumn brandCol = new TableColumn("Brand");
        brandCol.setMinWidth(120);
        brandCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("brand"));

        TableColumn modelCol = new TableColumn("Model");
        modelCol.setMinWidth(130);
        modelCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("model"));

        TableColumn isAcousticCol = new TableColumn("Acoustic");
        isAcousticCol.setMinWidth(90);
        isAcousticCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("isAcoustic"));

        TableColumn guitarTypeCol = new TableColumn("Guitar Type");
        guitarTypeCol.setMinWidth(110);
        guitarTypeCol.setCellValueFactory(new PropertyValueFactory<OrderItem, GuitarType>("guitarType"));

        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("price"));

        orderItemsTable.getColumns().addAll(unitIdCol, brandCol, modelCol, isAcousticCol, guitarTypeCol, priceCol, amountCol);
        return orderItemsTable;
    }

    private void populateOITable() {
        if (orderItemsOL != null) {
            orderItemsOL.clear();
        }
        orderItemsOL = FXCollections.observableArrayList(orderItems);
        orderItemsTable.setItems(orderItemsOL);
    }
}