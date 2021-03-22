package nl.exam.ui.scenes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import nl.exam.data.Database;
import nl.exam.logic.OrderService;
import nl.exam.logic.StockService;
import nl.exam.model.*;
import nl.exam.ui.dialogs.AddOrderItemDialog;
import nl.exam.ui.dialogs.ConfirmDialog;
import nl.exam.ui.dialogs.CustomerListDialog;
import nl.exam.ui.windows.MainWindow;
import nl.exam.ui.windows.PopUp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderScene {
    private Scene scene;

    private Label firstNameLabel = new Label("First name: ");
    private Label streetLabel = new Label("Street address: ");
    private Label phoneLabel = new Label("Phone number: ");
    private Label lastNameLabel = new Label("Last name: ");
    private Label cityLabel = new Label("City: ");
    private Label emailLabel = new Label("Email address: ");
    private Label orderDetailLabel = new Label("Order details: ");
    private Label titleLabel = new Label("Create order");
    private Button searchButton = new Button("Search");

    private TableView<OrderItem> table = new TableView<>();
    private OrderService orderService;
    private StockService stockService;
    private Database db;
    private ObservableList<OrderItem> itemsOL;
    private ArrayList<OrderItem> shopWallet;
    private ArrayList<Stock> stocks;

    private Customer selectedCustomer;
    private OrderItem selectedOrderItemEM;
    private Order newOrder;
    private User user;
    private int orderId;
    private PopUp popUp;
    private MainWindow MW;
    private OrderScene OS;

    private HBox editItemsMenu = new HBox();

    public Scene getScene() {
        return scene;
    }

    public String returnTitle() {
        return "Guitarshop FX - Create an order";
    }

    public OrderScene(User user, Database dbIn) {
        this.db = dbIn;
        this.user = user;
        stockService = new StockService(db);
        stocks = stockService.getStocksDB();
        shopWallet = new ArrayList<>();
        itemsOL = FXCollections.observableArrayList(shopWallet);
        orderService = new OrderService(db);
        orderId = orderService.getLastOrderId();
        titleLabel.setText("Create order: # " + orderId);
        GridPane grid = new GridPane();
        titleLabel.setId("Title");

        VBox layOut = new VBox();
        layOut.setPadding(new Insets(10));
        layOut.setSpacing(20);

        makeGridMake(grid);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderItem>() {
            @Override
            public void changed(ObservableValue<? extends OrderItem> observableValue, OrderItem OI, OrderItem SOI) {
                selectedOrderItemEM = SOI;
            }
        });
        layOut.getChildren().addAll(titleLabel, searchCustomer(grid), orderDetailLabel, tableStock(), bottomRow());
        scene = new StyledScene(layOut);
    }


    private HBox bottomRow() {
        editItemsMenu.setSpacing(20);
        editItemsMenu.getChildren().addAll(btnAdd(), btnDelete(), btnConfirm(), btnReset());
        return editItemsMenu;
    }

    private Button btnAdd() {
        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AddOrderItemDialog addOrderItemDialog = new AddOrderItemDialog(db);
                addOrderItemDialog.getStage().initModality(Modality.APPLICATION_MODAL);
                addOrderItemDialog.getStage().showAndWait();
                if (addOrderItemDialog.getSelectedOrderItemEM() != null) {
                    shopWallet.add(addOrderItemDialog.getSelectedOrderItemEM());
                }
                itemsOL = FXCollections.observableArrayList(shopWallet);
                populateLV();

            }
        });
        return btnAdd;
    }


    private Button btnDelete() {
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restoreItem();
                itemsOL = FXCollections.observableArrayList(shopWallet);
                populateLV();
            }
        });
        return btnDelete;
    }

    private void restoreItem() {
        try {
            if (selectedOrderItemEM != null) {
                for (Stock s : stocks) {
                    if (s.getStockItemId() == selectedOrderItemEM.getStockItemId()) {
                        s.setStockAmount(s.getStockAmount() + selectedOrderItemEM.getOrderAmount());
                        stockService.editStock(s);
                    }
                }
            }
        } catch (NullPointerException e) {
            popUp = new PopUp("There is nothing selected.");
            popUp.getStage().show();
        }
        shopWallet.remove(selectedOrderItemEM);
    }

    private void restoreItems() {
        for (Stock s : stocks) {
            for (OrderItem t : shopWallet) {
                if (s.getStockItemId() == t.getStockItemId()) {
                    s.setStockAmount(s.getStockAmount() + t.getOrderAmount());
                    stockService.editStock(s);
                }
            }
        }
    }

    private Button btnConfirm() {
        Button btnConfirm = new Button("Confirm");
        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (selectedCustomer != null) {
                        try {
                            if (shopWallet.size() > 0) {
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:MM:ss");
                                LocalDateTime now = LocalDateTime.now();
                                newOrder = new Order(orderId, now, selectedCustomer, shopWallet);
                                ConfirmDialog confirmDialog = new ConfirmDialog(db, newOrder);
                                confirmDialog.getStage().initModality(Modality.APPLICATION_MODAL);
                                confirmDialog.getStage().showAndWait();
                                createMW();
                                confirmDialog(confirmDialog);
                                orderId = orderService.getLastOrderId();
                                titleLabel.setText("Create order: # " + orderId);
                                db = confirmDialog.getDb();
                                bindSelectCustomer();
                            }
                        } catch (NullPointerException e) {
                            popUp = new PopUp("There are no items selected.");
                            popUp.getStage().show();
                        }
                    }
                } catch (NullPointerException ex) {
                    popUp = new PopUp("Customer is not selected.");
                    popUp.getStage().show();
                }
            }
        });
        return btnConfirm;
    }

    public Database getDb() {
        return db;
    }

    private void confirmDialog(ConfirmDialog confirmDialog) {
        if (confirmDialog.isConfirm()) {
            selectedCustomer = null;
            shopWallet.clear();
            itemsOL.clear();
        }
    }

    public Order getNewOrder() {
        return newOrder;
    }


    private void createMW() {
        MainWindow MW = new MainWindow(user, db);
        OrderScene OS = new OrderScene(user, db);
        MW.getLayOut().getChildren().remove(1);
        MW.getStage().setTitle(returnTitle());
        MW.getLayOut().getChildren().add(OS.getScene().getRoot());
    }

    private Button btnReset() {
        Button btnReset = new Button(" Reset");
        btnReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                createMW();
                selectedCustomer = null;
                restoreItems();
                bindSelectCustomer();
                shopWallet.clear();
                itemsOL.clear();
            }
        });
        return btnReset;
    }

    private void SearchCustomerButton(TextField input) {
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String userInput = input.getText();
                CustomerListDialog customerListDialog = new CustomerListDialog(userInput, db);
                customerListDialog.getStage().initModality(Modality.APPLICATION_MODAL);
                customerListDialog.getStage().showAndWait();
                selectedCustomer = customerListDialog.getSelectedCustomerEM();
                bindSelectCustomer();
            }
        });
    }

    private void bindSelectCustomer() {
        StringProperty FNP = new SimpleStringProperty();
        StringProperty SP = new SimpleStringProperty();
        StringProperty PP = new SimpleStringProperty();
        StringProperty LNP = new SimpleStringProperty();
        StringProperty CP = new SimpleStringProperty();
        StringProperty EMP = new SimpleStringProperty();
        if (selectedCustomer != null) {
            FNP.setValue("First name: " + selectedCustomer.getFirstName());
            SP.setValue("Street address: " + selectedCustomer.getStreetName());
            PP.setValue("Phone number: " + selectedCustomer.getPhone());
            LNP.setValue("Last Name: " + selectedCustomer.getLastName());
            CP.setValue("City: " + selectedCustomer.getCity());
            EMP.setValue("Email address: " + selectedCustomer.getEmail());
        } else {
            FNP.setValue("First name: ");
            SP.setValue("Street address: ");
            PP.setValue("Phone number: ");
            LNP.setValue("Last Name: ");
            CP.setValue("City: ");
            EMP.setValue("Email address: ");
        }
        firstNameLabel.textProperty().bind(FNP);
        streetLabel.textProperty().bind(SP);
        phoneLabel.textProperty().bind(PP);
        lastNameLabel.textProperty().bind(LNP);
        cityLabel.textProperty().bind(CP);
        emailLabel.textProperty().bind(EMP);
    }

    private HBox searchCustomer(GridPane grid) {
        HBox vContainer = new HBox();
        HBox hContainer = new HBox();
        vContainer.setPadding(new Insets(10));
        TextField input = new TextField();
        vContainer.getChildren().addAll(input, searchButton);
        hContainer.getChildren().addAll(vContainer, grid);
        SearchCustomerButton(input);
        return hContainer;
    }

    private void makeGridMake(GridPane grid) {
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(50);
        grid.setAlignment(Pos.CENTER);
        GridPane.setConstraints(firstNameLabel, 0, 0);
        GridPane.setConstraints(streetLabel, 0, 1);
        GridPane.setConstraints(phoneLabel, 0, 2);
        GridPane.setConstraints(lastNameLabel, 1, 0);
        GridPane.setConstraints(cityLabel, 1, 1);
        GridPane.setConstraints(emailLabel, 1, 2);
        grid.getChildren().addAll(titleLabel, firstNameLabel, streetLabel, phoneLabel, lastNameLabel, cityLabel, emailLabel);
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

        TableColumn isAcousticCol = new TableColumn("Acoustic");
        isAcousticCol.setMinWidth(100);
        isAcousticCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("isAcoustic"));

        TableColumn guitarTypeCol = new TableColumn("Guitar Type");
        guitarTypeCol.setMinWidth(130);
        guitarTypeCol.setCellValueFactory(new PropertyValueFactory<OrderItem, GuitarType>("guitarType"));

        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("price"));

        table.getColumns().addAll(amountCol, brandCol, modelCol, isAcousticCol, guitarTypeCol, priceCol);
        populateLV();
        return table;
    }

    private void populateLV() {
        table.setItems(itemsOL);
    }
}
