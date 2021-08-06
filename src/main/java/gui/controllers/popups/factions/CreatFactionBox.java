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

import java.util.ArrayList;
import java.util.List;

public class CreatFactionBox implements ConfirmBox, Controllers {
    static boolean answer;


    public static boolean display() {
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("enter names");
        window.setResizable(false);
        window.setMinWidth(300);
        window.getIcons().add(icon);
        message.setText("enter name of faction");

        //creat textFields
        TextField usernameField = new TextField();
        TextField factionName = new TextField();

        Label error = new Label();
        error.setTextFill(Color.RED);

        Label info = new Label("enter name of users");
        Label addedUsers = new Label();


        //create two buttons
        Button confirmButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        Button createButton = new Button("Create");

        ArrayList<String> existingFactions = FACTIONS_CONTROLLER.getFactionNames();
        List<String> users = new ArrayList<>();


        confirmButton.setOnAction(e -> {
            String username = usernameField.getText();
            if (!username.equals("")) {
                if (!FACTIONS_CONTROLLER.canAddToGroup(username)) {
                    error.setText("Cannot add " + username + " to the group!.\nYou must follow The user");
                    usernameField.setText("");
                }
                else {
                    users.add(username);
                    error.setText("user Added");
                    addedUsers.setText(String.join(", " , users));
                }

            }
        });

        cancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        createButton.setOnAction(event -> {
            String name = factionName.getText();
            if (!name.equals("")){
                if(users.size() == 0){
                    error.setText("faction should have at least 1 member");
                }
                else if (!existingFactions.contains(name)){
                    FACTIONS_CONTROLLER.insertNewFaction(name , users);
                    answer =  true;
                    window.close();
                }
                else {
                    error.setText("faction name exists");
                }
            }

        });

        VBox layout = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(confirmButton, cancelButton, createButton);
        layout.getChildren().addAll(message, factionName, info, error, usernameField, addedUsers, hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
