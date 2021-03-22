package nl.exam.ui.windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.exam.model.StyledScene;

public class PopUp {
    private Stage stage;
    private VBox layOut;

    public Stage getStage() {
        return stage;
    }

    public PopUp(String message) {
        stage = new Stage();
        stage.setTitle("Guitarshop FX - Error");
        stage.setMinHeight(150);
        stage.setMinWidth(300);
        layOut = new VBox();
        layOut.setPadding(new Insets(20));
        layOut.setSpacing(20);
        Label messageLabel = new Label(message);
        Button btnClose = new Button("Close");
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        layOut.getChildren().addAll(messageLabel, btnClose);
        Scene mainScene = new StyledScene(layOut);
        stage.setScene(mainScene);
    }
}
