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
import nl.exam.logic.CustomerService;
import nl.exam.model.Customer;
import nl.exam.model.StyledScene;

import java.util.ArrayList;


public class CustomerListDialog {
    private Stage stage;
    private Database db;
    private CustomerService customerService;
    private Label titleLabel = new Label("Customer List");
    private ObservableList<Customer> customers;
    private Customer selectedCustomerEM;
    private Customer newCustomer;
    private TableView<Customer> table = new TableView<>();


    public Stage getStage() {
        return stage;
    }

    public CustomerListDialog(String input, Database db) {
        this.db = db;
        this.customerService = new CustomerService(db);
        ArrayList<Customer> getCustomers = customerService.getCustomers();
        customers = searchCustomer(getCustomers, input);

        stage = new Stage();
        stage.setTitle("Guitarshop FX - Search customer");
        stage.setMinWidth(1024);
        stage.setHeight(680);
        titleLabel.setId("Title");
        VBox layOut = new VBox();
        layOut.setPadding(new Insets(10));
        layOut.setSpacing(20);
        selectCustomer();
        layOut.getChildren().addAll(titleLabel, tableCustomers(), buttonContainer());
        Scene customerListScene = new StyledScene(layOut);
        customerListScene.getStylesheets().add("resources/css/style.css");
        stage.setScene(customerListScene);
    }

    private HBox buttonContainer() {
        HBox container = new HBox();
        container.getChildren().addAll(btnAddCustomer());
        return container;
    }

    private Button btnAddCustomer() {
        Button btnAddCustomer = new Button("Add Customer");
        btnAddCustomer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AddCustomerDialog addCustomerDialog = new AddCustomerDialog(db);
                addCustomerDialog.getStage().showAndWait();
                customers.clear();
                customers = FXCollections.observableArrayList(customerService.getCustomers());
                customers.add(addCustomerDialog.getNewCustomer());

            }
        });
        return btnAddCustomer;
    }


    private void selectCustomer() {
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observableValue, Customer customer, Customer selectedCustomer) {
                selectedCustomerEM = selectedCustomer;
                stage.close();
            }
        });
    }

    private ObservableList<Customer> searchCustomer(ArrayList<Customer> customers, String input) {
        ArrayList<Customer> newList = new ArrayList<>();
        if (input == null || input.length() == 0) {
            return FXCollections.observableArrayList(customers);
        } else {
            for (Customer c : customers) {
                if (c.getFirstName().toLowerCase().contains(input.toLowerCase()) ||
                        c.getLastName().toLowerCase().contains(input.toLowerCase())) {
                    newList.add(c);
                }
            }
            return FXCollections.observableArrayList(newList);
        }
    }



    public Customer getSelectedCustomerEM() {
        return selectedCustomerEM;
    }

    private TableView<Customer> tableCustomers() {

        table.setEditable(false);
        table.getSelectionModel().setCellSelectionEnabled(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn firstCol = new TableColumn("First Name");
        firstCol.setMinWidth(100);
        firstCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));

        TableColumn lastCol = new TableColumn("Last Name");
        lastCol.setMinWidth(150);
        lastCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));

        TableColumn streetCol = new TableColumn("Street Name");
        streetCol.setMinWidth(150);
        streetCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("streetName"));

        TableColumn cityCol = new TableColumn("City");
        cityCol.setMinWidth(200);
        cityCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));

        TableColumn phoneCol = new TableColumn("Phone #");
        phoneCol.setMinWidth(200);
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));

        table.getColumns().addAll(firstCol, lastCol, streetCol, cityCol, phoneCol, emailCol);
        populateLV();
        return table;
    }
    private void populateLV() {
        table.setItems(customers);
    }
}
