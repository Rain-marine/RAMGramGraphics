package gui.controllers.popups.factions;

import controllers.Controllers;
import gui.controllers.popups.ConfirmBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddUserToFactionBox implements ConfirmBox, Controllers {
    static boolean answer;


    public static boolean display(int factionId){
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("enter name");
        window.setResizable(false);
        window.setMinWidth(300);
        window.getIcons().add(icon);
        message.setText("enter the name of the user");

        //creat textFields
        TextField textField = new TextField();

        Label error = new Label();
        error.setTextFill(Color.RED);

        //create two buttons
        Button ConfirmButton = new Button("Add");
        Button CancelButton = new Button("Cancel");

        ConfirmButton.setOnAction(e -> {
            String username = textField.getText();
            if(!username.equals("")){
                if (!FACTIONS_CONTROLLER.canAddToGroup(username)) {
                    error.setText("Cannot add " + username + " to the group!.\nYou must follow The user");
                    textField.setText("");
                }
                else if(FACTIONS_CONTROLLER.getGroupMembers(factionId).stream().noneMatch(it -> it.getUsername().equals(username))) {
                    FACTIONS_CONTROLLER.addUserToFaction(factionId, username);
                    error.setText("user Added");
                    answer = true;
                    window.close();
                }
                else{
                    error.setText("user is already in faction");
                }
            }
        });

        CancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(ConfirmButton,CancelButton);
        layout.getChildren().addAll(message,error,textField, hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
