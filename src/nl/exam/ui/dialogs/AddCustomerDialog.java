package nl.exam.ui.dialogs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import nl.exam.data.Database;
import nl.exam.logic.CustomerService;
import nl.exam.model.Customer;
import nl.exam.model.StyledScene;
import nl.exam.ui.scenes.OrderListScene;
import nl.exam.ui.windows.PopUp;

public class AddCustomerDialog {
    private Database db;
    private Stage stage;

    private VBox layOut;
    private Label titleLabel = new Label("Add customer");
    private TextField tfFirstName;
    private TextField tfLastName;
    private TextField tfStreet;
    private TextField tfCity;
    private TextField tfPhone;
    private TextField tfEmail;

    private Label lblFirstName = new Label("First name");
    private Label lblLastName = new Label("Last Name");
    private Label lblStreet = new Label("Street name");
    private Label lblCity = new Label("City");
    private Label lblPhone = new Label("Phone Number");
    private Label lblEmail = new Label("Email Address");

    private CustomerService customerService;
    private Customer newCustomer;
    private PopUp popUp;
    private int customerId;

    public Stage getStage() {
        return stage;
    }


    public AddCustomerDialog(Database db) {
        this.db = db;
        customerService = new CustomerService(db);
        customerId = customerService.getNewCustomerId();
        stage = new Stage();
        stage.setTitle("Guitarshop FX - Add Customer");
        stage.setMinHeight(150);
        stage.setMinWidth(500);
        layOut = new VBox();
        layOut.setPadding(new Insets(20));
        layOut.setSpacing(20);
        titleLabel.setId("Title");

        layOut.getChildren().addAll(titleLabel, containerGrid(), buttonContainer());
        Scene mainScene = new StyledScene(layOut);
        stage.setScene(mainScene);
    }

    private HBox containerGrid() {
        HBox container = new HBox();
        VBox col1 = new VBox();
        VBox col2 = new VBox();
        GridPane grid = new GridPane();
        tfFirstName = new TextField();
        tfLastName = new TextField();
        tfStreet = new TextField();
        tfCity = new TextField();
        tfPhone = new TextField();
        tfEmail = new TextField();
        col1.getChildren().addAll(
                lblFirstName,
                lblLastName,
                lblStreet,
                lblCity,
                lblPhone,
                lblEmail
        );
        col2.getChildren().addAll(
                tfFirstName,
                tfLastName,
                tfStreet,
                tfCity,
                tfPhone,
                tfEmail
        );
        container.getChildren().addAll( col1, col2);
        return container;
    }

    private HBox buttonContainer() {
        HBox container = new HBox();
        container.setPadding(new Insets(20));
        container.setSpacing(20);
        container.getChildren().addAll(btnAddCustomer(), btnClose());
        return container;
    }

    private Button btnAddCustomer() {
        Button btnAddCustomer = new Button("Add Customer");
        btnAddCustomer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (checkTextField()) {
                        newCustomer = new Customer(
                                customerId,
                                tfFirstName.getText(),
                                tfLastName.getText(),
                                tfCity.getText(),
                                tfStreet.getText(),
                                tfPhone.getText(),
                                tfEmail.getText()
                        );
                        customerService.addCustomerService(newCustomer);
                        stage.close();
                    }
                } catch (NullPointerException ex) {
                    popUp = new PopUp("The fields cant be empty.");
                    popUp.getStage().show();
                }
            }
        });
        return btnAddCustomer;
    }

    public Customer getNewCustomer() {
        return newCustomer;
    }

    private boolean checkTextField() {
        if (!(tfFirstName.getText() == null || tfFirstName.getText().trim().isEmpty() &&
                tfLastName.getText() == null || tfLastName.getText().trim().isEmpty() &&
                tfStreet.getText() == null || tfStreet.getText().trim().isEmpty() &&
                tfPhone.getText() == null || tfPhone.getText().trim().isEmpty() &&
                tfEmail.getText() == null || tfEmail.getText().trim().isEmpty())
        ) {
            return true;
        }
        return false;
    }

    private Button btnClose() {
        Button btnClose = new Button("Close");
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        return btnClose;
    }
}
