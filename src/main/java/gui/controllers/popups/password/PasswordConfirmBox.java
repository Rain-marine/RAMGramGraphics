package gui.controllers.popups.password;

import gui.controllers.popups.ConfirmBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PasswordConfirmBox implements ConfirmBox {
    static boolean answer;


    public static boolean display(){
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("security alert");
        window.setResizable(false);
        window.setMinWidth(300);
        window.getIcons().add(icon);
        message.setText("enter your current password");

        //creat two textFields
        PasswordField passwordField = new PasswordField();

        Label passwordError = new Label();
        passwordError.setTextFill(Color.RED);

        //create two buttons
        Button ConfirmButton = new Button("Confirm");
        Button CancelButton = new Button("Cancel");

        ConfirmButton.setOnAction(e -> {
            String password = passwordField.getText();
            if(!password.equals("") && SETTING_CONTROLLER.isPasswordCorrect(password)){
                answer = true;
                window.close();
            }
            else {
                passwordError.setText("Password is incorrect");
                passwordField.setText("");
            }

        });

        CancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(ConfirmButton,CancelButton);
        layout.getChildren().addAll(message,passwordError,passwordField, hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }


}
