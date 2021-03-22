package nl.exam.ui.scenes;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nl.exam.data.Database;
import nl.exam.logic.DashBoardService;
import nl.exam.model.StyledScene;
import nl.exam.model.User;
import nl.exam.model.WelcomeMessage;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DashBoard {
    private Scene scene;
    private Database db;
    private DashBoardService dashBoardService;
    private ArrayList<WelcomeMessage> welcomeMessages;

    public Scene getScene() {
        return scene;
    }


    public String returnTitle() {
        return "Guitarshop FX - Dashboard";
    }

    public DashBoard(User user, Database inDb) {
        this.db = inDb;
        dashBoardService = new DashBoardService(db);
        welcomeMessages = dashBoardService.getWMService();
        VBox layOut = new VBox();
        layOut.setPadding(new Insets(10));
        layOut.setSpacing(20);

        //title with full name
        String fullName = "";
        Label titleLabel = new Label("Welcome " + user.getFullName());
        titleLabel.setId("Title");
        //get role of user
        Label roleLabel = new Label("Your role is: " + user.getAccessLevel());
        //get current date and time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:MM:ss");
        LocalDateTime now = LocalDateTime.now();
        Label timeLabel = new Label("Today is: " + now.format(dtf));

        VBox WM = new VBox();
        Label idLabel;
        Label headLabel;
        Label bodyLabel;
        for (WelcomeMessage welcomeMessage : welcomeMessages) {
            idLabel = new Label(String.valueOf(welcomeMessage.getId()));
            headLabel = new Label(welcomeMessage.getTitle());
            bodyLabel = new Label(welcomeMessage.getContent());
            WM.getChildren().addAll(idLabel, headLabel, bodyLabel);
        }

        layOut.getChildren().addAll(titleLabel, roleLabel, timeLabel, WM);
        scene = new StyledScene(layOut);
    }

}
