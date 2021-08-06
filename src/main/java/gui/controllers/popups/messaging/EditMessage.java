package gui.controllers.popups.messaging;

import controllers.Controllers;
import gui.controllers.popups.ConfirmBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditMessage implements ConfirmBox, Controllers {

    static boolean answer;


    public static boolean display(long messageId) {
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("edit");
        window.setResizable(false);
        window.setMinWidth(300);
        window.getIcons().add(icon);
        message.setText("enter new text");

        //creat textFields
        TextField newText = new TextField();

        //create two buttons
        Button confirmButton = new Button("Edit");
        Button cancelButton = new Button("Cancel");


        confirmButton.setOnAction(e -> {
            String text = newText.getText();
            MESSAGE_CONTROLLER.editMessage(messageId ,text );
            answer = true;
            window.close();
        });

        cancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        VBox layout = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(confirmButton, cancelButton );
        layout.getChildren().addAll(message, newText, hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
