package gui.controllers.popups.messaging;

import controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.popups.ConfirmBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

public class SendNewMessage implements ConfirmBox, Controllers {

    static boolean answer;


    public static boolean display(){
        Stage window = new Stage();
        Label message = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("start messaging");
        window.setResizable(true);
        window.getIcons().add(icon);
        message.setText("enter the name of the receiver(s)");

        //creat textFields
        TextField receiver = new TextField();
        TextField messageText = new TextField();
        TextField faction = new TextField();

        Label userError = new Label();
        userError.setTextFill(Color.RED);

        Label addedUsers = new Label();
        Label addedFactions = new Label();

        Label factionError = new Label();
        factionError.setTextFill(Color.RED);

        Label sendError = new Label();
        sendError.setTextFill(Color.RED);

        //create buttons
        Button addUserButton = new Button("Add");
        Button addFactionButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        Button sendButton = new Button("Send");
        Button addImage = new Button("Image");

        ImageView addedImage = new ImageView();
        addedImage.setPreserveRatio(true);
        addedImage.setFitWidth(50);

        List<String> users = new ArrayList<>();
        List<String> factions = new ArrayList<>();

        addFactionButton.setOnAction(e -> {
            String factionName = faction.getText();
            if(!factionName.equals("")){
                if (!MESSAGE_CONTROLLER.canSendMessageToGroup(factionName)) {
                    factionError.setText("Can't message " + factionName);
                    faction.setText("");
                }
                else if(factions.contains(factionName)) {
                    factionError.setText("duplicate name");
                }
                else{
                    factions.add(factionName);
                    factionError.setText("");
                    addedFactions.setText(String.join(", " , factions));
                }
            }
        });

        addUserButton.setOnAction(e -> {
            String username = receiver.getText();
            if(!username.equals("")){
                if (!MESSAGE_CONTROLLER.canSendMessageToUser(username)) {
                    userError.setText("Can't message " + username );
                    receiver.setText("");
                }
                else if(users.contains(username)) {
                    userError.setText("duplicate name");
                }
                else{
                    users.add(username);
                    userError.setText("");
                    addedUsers.setText(String.join(", " , users));
                }
            }
        });

        cancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        final byte[][] finalImage = {null};

        addImage.setOnAction(event -> {
            try {
                finalImage[0] = ImageController.pickImage();
                addedImage.setImage(ImageController.byteArrayToImage(finalImage[0]));
                sendError.setText("");

            } catch (SizeLimitExceededException e) {
                sendError.setText("Image size too large!");
            }
        });


        sendButton.setOnAction(event -> {
            String messageTextString = messageText.getText();
            if (users.size() == 0 && factions.size() ==0){
                sendError.setText("choose receiver idiot");
            }
            else if (messageTextString.equals("") && finalImage[0] ==null){
                sendError.setText("write something idiot");
            }
            else {
                MESSAGE_CONTROLLER.sendMessage(messageTextString , finalImage[0] , users, factions);
                answer = true;
                window.close();
            }
        });


        VBox layout = new VBox(5);
        HBox usersRow = new HBox(5);
        HBox factionsRow = new HBox(5);
        HBox buttons = new HBox(5);
        HBox messageRow = new HBox(5);

        messageRow.getChildren().addAll(messageText , addImage , addedImage);
        buttons.getChildren().addAll(cancelButton, sendButton);
        usersRow.getChildren().addAll(new Label("user:"),receiver , addUserButton);
        factionsRow.getChildren().addAll(new Label("faction:"),faction , addFactionButton);
        layout.getChildren().addAll(message , usersRow , userError, addedUsers , factionsRow, factionError , addedFactions, messageRow , sendError, buttons);

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
