package gui.controllers.popups.password;

import gui.controllers.popups.ConfirmBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewPasswordBoxSimple implements ConfirmBox {

    public static void display() {
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("security alert");
        window.setResizable(false);
        window.setMinWidth(300);
        window.getIcons().add(icon);
        message.setText("enter your new password");

        //creat two textFields
        PasswordField newPasswordField = new PasswordField();

        //create two buttons
        Button ConfirmButton = new Button("Change Password");
        Button CancelButton = new Button("Cancel");

        ConfirmButton.setOnAction(e -> {
            String newPassword = newPasswordField.getText();
            SETTING_CONTROLLER.changePassword(newPassword);
            window.close();
        });

        CancelButton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(ConfirmButton, CancelButton);
        layout.getChildren().addAll(message, newPasswordField, hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

    }

}
