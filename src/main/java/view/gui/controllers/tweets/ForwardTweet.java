package view.gui.controllers.tweets;

import controllers.Controllers;
import view.popups.ConfirmBox;
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

public class ForwardTweet implements ConfirmBox, Controllers {

    static boolean answer;


    public static boolean display(long tweetId){
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("new member");
        window.setResizable(false);
        window.getIcons().add(icon);
        message.setText("enter the name of the receiver");

        //creat textFields
        TextField receiverNameTextField = new TextField();


        Label userError = new Label();
        userError.setTextFill(Color.RED);

        Label sendError = new Label();
        sendError.setTextFill(Color.RED);

        //create buttons
        Button addUserButton = new Button("Add");
        Button cancelButton = new Button("Cancel");


        addUserButton.setOnAction(e -> {
            String username = receiverNameTextField.getText();
            if(!username.equals("")){
                if (MESSAGE_CONTROLLER.canSendMessageToUser(username)) {
                    MESSAGE_CONTROLLER.forwardTweet(tweetId , username);
                    answer = true;
                    window.close();
                }
                else {
                    userError.setText("you can't message " + username );
                    receiverNameTextField.setText("");
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
        usersRow.getChildren().addAll(new Label("member:"), receiverNameTextField);
        layout.getChildren().addAll(message , usersRow , userError , sendError, buttons);

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

}
