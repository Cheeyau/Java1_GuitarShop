package nl.exam.ui.windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import nl.exam.data.Database;
import nl.exam.logic.UserLogic;
import nl.exam.logic.UserSession;
import nl.exam.model.User;
import nl.exam.model.StyledScene;

import java.util.ArrayList;


public class LoginUI {
    private Stage stage;
    private UserLogic Userdb;
    private ArrayList<User> users;

    private Label userLabel = new Label("User name: ");
    private Label titleLabel = new Label("Login");
    private Label passwordLabel = new Label("User password: ");
    private Label errorUserLabel = new Label(null);
    private Label errorPasswordLabel = new Label(null);
    private TextField userInput = new TextField();
    private PasswordField passwordInput = new PasswordField();
    private Button loginButton = new Button("Log in");
    private String userName;
    private int counter;

    public Stage getStage() {
        return stage;
    }

    public LoginUI(UserSession userSession, Database db) {
        this.Userdb = new UserLogic(db);
        users = Userdb.getDb();
        counter = userSession.getCounter();
        GridPane grid = new GridPane();
        setStage(grid);
        stage = new Stage();
        stage.setTitle("Guitarshop FX - Login");
        stage.setMinWidth(768);
        stage.setHeight(540);
        titleLabel.setId("Title");
        GridPane.setConstraints(titleLabel, 0, 0);
        GridPane.setConstraints(userLabel, 0, 1);
        GridPane.setConstraints(passwordLabel, 0, 2);
        GridPane.setConstraints(userInput, 1, 1);
        GridPane.setConstraints(passwordInput, 1, 2);
        GridPane.setConstraints(loginButton, 1, 3);
        GridPane.setConstraints(errorUserLabel, 2, 1);
        GridPane.setConstraints(errorPasswordLabel, 2, 2);
        grid.getChildren().addAll(titleLabel, userLabel, userInput, passwordLabel, passwordInput, loginButton, errorPasswordLabel, errorUserLabel);
        buttonLogin(userSession, db);
        Scene loginScene = new StyledScene(grid);
        loginScene.getStylesheets().add("resources/css/style.css");
        stage.setScene(loginScene);
    }

    private void buttonLogin(UserSession userSession, Database db) {
        userName = userInput.getText();
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                errorUserLabel.textProperty().setValue(checkName(userInput));
                errorPasswordLabel.textProperty().setValue(checkPassword(passwordInput));
                User user = searchUser(userInput);
                if (!userName.equals(user.getUserName())) {
                    userSession.setCounter(1);
                }
                if (counter < 4) {
                    if ((userSession.login(user, passwordInput)) && (userSession.getLoggedUser() != null)) {
                        MainWindow MW = new MainWindow(user, db);
                        MW.getStage().show();
                        stage.close();
                    } else if (user == null) {
                        errorUserLabel.textProperty().setValue("user couldn't not been found.");
                    } else {
                        errorPasswordLabel.textProperty().setValue("The password doesnt match user name.");
                    }
                }
            }
        });
    }

    private User searchUser(TextField name) {
        for (User user : users) {
            if (user.getUserName().equals(name.getText())) {
                return user;
            }
        }
        return null;
    }

    private String checkName(TextField name) {
        if (name.getText() == null || name.getText().trim().isEmpty()) {
            return "User name was empty.";
        }
        return null;
    }

    private String checkPassword(PasswordField password) {
        if (password.getText() == null || password.getText().trim().isEmpty()) {
            return "User password was empty.";
        }
        return null;
    }

    private void setStage(GridPane grid) {
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(8);
        grid.setAlignment(Pos.CENTER);
    }
}
