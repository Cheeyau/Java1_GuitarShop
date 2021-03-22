package nl.exam.ui.windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.exam.data.Database;
import nl.exam.model.StyledScene;
import nl.exam.model.User;
import nl.exam.ui.scenes.*;

public class MainWindow {
    private Stage stage;
    private VBox layOut;
    private Database db;
    private User user;

    public Stage getStage() {
        return stage;
    }

    public VBox getLayOut() {
        return layOut;
    }

    public void setLayOut(VBox layOut) {
        this.layOut = layOut;
    }

    public MainWindow(User inUser, Database db) {
        this.db = db;
        this.user = inUser;
        stage = new Stage();
        stage.setTitle("Guitarshop FX - Dashboard");
        stage.setHeight(540);
        stage.setWidth(800);
        layOut = new VBox();
        DashBoard dashBoard = new DashBoard(user, db);
        Button btnHome = new Button("Home");
        btnHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                layOut.getChildren().remove(1);
                stage.setTitle(dashBoard.returnTitle());
                layOut.getChildren().add(dashBoard.getScene().getRoot());
            }
        });
        layOut.getChildren().addAll(showMenuOptions(btnHome), dashBoard.getScene().getRoot());

        Scene mainScene = new StyledScene(layOut);
        stage.setScene(mainScene);
    }

    private HBox showMenuOptions(Button home) {
        HBox menu = new HBox();
        menu.setPadding(new Insets(0, 0, 10, 0));
        switch (user.getAccessLevel()) {
            case Manager:
                menu.getChildren().addAll(home, btnOrderOverview(), btnStock());
                break;
            case Sales:
                menu.getChildren().addAll(home, btnOrderOverview(), btnSales());
                break;
            default:
        }
        return menu;
    }

    private Button btnOrderOverview() {
        Button btnOrderOverview = new Button("Order Overview");
        btnOrderOverview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                OrderListScene OLS = new OrderListScene(user, db);
                layOut.getChildren().remove(1);
                stage.setTitle(OLS.returnTitle());
                layOut.getChildren().add(OLS.getScene().getRoot());
            }
        });
        return btnOrderOverview;
    }

    private Button btnSales() {
        Button btnSales = new Button("Create Order");
        btnSales.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                OrderScene OS = new OrderScene(user, db);
                layOut.getChildren().remove(1);
                stage.setTitle(OS.returnTitle());
                layOut.getChildren().add(OS.getScene().getRoot());
                db = OS.getDb();
            }
        });
        return btnSales;
    }

    private Button btnStock() {
        Button btnStock = new Button("Stock");
        btnStock.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                StockScene SC = new StockScene(db);
                layOut.getChildren().remove(1);
                stage.setTitle(SC.returnTitle());
                layOut.getChildren().add(SC.getScene().getRoot());
            }
        });
        return btnStock;
    }
}