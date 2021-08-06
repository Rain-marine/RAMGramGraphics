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

public class AddMemberToGroupChat implements Controllers , ConfirmBox {

    static boolean answer;


    public static boolean display(long chatId){
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("new member");
        window.setResizable(false);
        window.getIcons().add(icon);
        message.setText("enter the name of the member");

        //creat textFields
        TextField memberNameTextField = new TextField();


        Label userError = new Label();
        userError.setTextFill(Color.RED);

        Label sendError = new Label();
        sendError.setTextFill(Color.RED);

        //create buttons
        Button addUserButton = new Button("Add");
        Button cancelButton = new Button("Cancel");


        addUserButton.setOnAction(e -> {
            String username = memberNameTextField.getText();
            if(!username.equals("")){
                if (MESSAGE_CONTROLLER.canSendMessageToUser(username)) {
                    CHAT_CONTROLLER.addMemberToGroupChat(username , chatId);
                    answer = true;
                    window.close();
                }
                else {
                    userError.setText("Can't add " + username + " to group" );
                    memberNameTextField.setText("");
                }
            }
        });

        cancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        VBox layout = new VBox(5);
        HBox usersRow = new HBox(5);
        HBox buttons = new HBox(5);

        buttons.getChildren().addAll(cancelButton, addUserButton);
        usersRow.getChildren().addAll(new Label("member:"), memberNameTextField);
        layout.getChildren().addAll(message , usersRow , userError , sendError, buttons);

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
