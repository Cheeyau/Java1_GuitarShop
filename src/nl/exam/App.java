package nl.exam;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.exam.data.Database;
import nl.exam.logic.UserSession;
import nl.exam.ui.windows.LoginUI;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Database db = new Database();
        System.setProperty("prism.lcdtext", "false");
        UserSession userSession = new UserSession();
        userSession.logOut();
        LoginUI loginUI = new LoginUI(userSession, db);
        loginUI.getStage().show();
    }
}