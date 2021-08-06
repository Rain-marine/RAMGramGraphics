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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class NewGroup implements ConfirmBox, Controllers {

    static boolean answer;


    public static boolean display(){
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("new group");
        window.setResizable(false);
        window.getIcons().add(icon);
        message.setText("enter the name of the members");

        //creat textFields
        TextField membersTextField = new TextField();
        TextField groupNameTextfield = new TextField();


        Label userError = new Label();
        userError.setTextFill(Color.RED);

        Label addedUsers = new Label();

        Label sendError = new Label();
        sendError.setTextFill(Color.RED);

        //create buttons
        Button addUserButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        Button creatButton = new Button("Create");

        List<String> users = new ArrayList<>();


        addUserButton.setOnAction(e -> {
            String username = membersTextField.getText();
            if(!username.equals("")){
                if (!MESSAGE_CONTROLLER.canSendMessageToUser(username)) {
                    userError.setText("Can't add " + username + " to group" );
                    membersTextField.setText("");
                }
                else if(users.contains(username)) {
                    userError.setText("duplicate name");
                }
                else{
                    users.add(username);
                    addedUsers.setText(String.join(", " , users));
                }
            }
        });

        cancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        creatButton.setOnAction(event -> {
            String groupName = groupNameTextfield.getText();
            if (users.size() == 0){
                sendError.setText("choose members idiot");
            }
            else if (groupName.equals("")){
                sendError.setText("choose a name idiot");
            }
            else {
                CHAT_CONTROLLER.createGroupChat(users,groupName);
                answer = true;
                window.close();
            }
        });


        VBox layout = new VBox(5);
        HBox usersRow = new HBox(5);
        HBox buttons = new HBox(5);

        buttons.getChildren().addAll(cancelButton, creatButton);
        usersRow.getChildren().addAll(new Label("member:"),membersTextField , addUserButton);
        layout.getChildren().addAll(message , usersRow , userError, addedUsers, groupNameTextfield , sendError, buttons);

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
