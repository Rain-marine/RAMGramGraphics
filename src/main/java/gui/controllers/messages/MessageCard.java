package gui.controllers.messages;

import controllers.MessageController;
import controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.popups.messaging.EditMessage;
import gui.controllers.popups.messaging.Forward;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class MessageCard implements Controllers {

    private long messageId;
    private VBox card;
    private Label messageText;
    private Label messageDate;
    private ImageView messageImage;
    private ImageView profilePhoto;
    private HBox buttonRow;
    private HBox header;
    private byte[] imageArray;
    private byte[] profileImageArray;
    private MessageController.TYPE type;
    private ArrayList<Button> buttons;

    public MessageCard(long messageId) {
        this.messageId = messageId;
        this.card = new VBox(5);
        this.header = new HBox(3);
        this.messageImage = new ImageView();
        this.buttonRow = new HBox(3);
        this.messageImage = new ImageView();
        this.buttons = new ArrayList<>();

        loadCard();

    }

    private void loadCard() {
        card.getChildren().clear();
        buttonRow.getChildren().clear();
        header.getChildren().clear();
        this.messageText = new Label(MESSAGE_CONTROLLER.getMessageText(messageId));
        this.messageDate = new Label(MESSAGE_CONTROLLER.getMessageDate(messageId));

        imageArray = MESSAGE_CONTROLLER.getMessageImage(messageId);

        if(imageArray != null){
            messageImage.setImage(ImageController.byteArrayToImage(imageArray));
            messageImage.setPreserveRatio(true);
            messageImage.setFitWidth(200);
        }

        profileImageArray = MESSAGE_CONTROLLER.getSenderProfile(messageId);


        messageText.setTextFill(Color.PURPLE);
        messageText.setFont(Font.font(14));


        messageDate.setTextFill(Color.DARKVIOLET);
        messageDate.setFont(Font.font(9));

        String sender = MESSAGE_CONTROLLER.getMessageSender(messageId);
        String grandSender = MESSAGE_CONTROLLER.getMessageGrandSender(messageId);
        this.type = MESSAGE_CONTROLLER.getMessageType(messageId);

        profilePhoto = new ImageView();
        profilePhoto.setFitHeight(30);
        profilePhoto.setFitWidth(30);
        Rectangle clip = new Rectangle(
                profilePhoto.getFitWidth(), profilePhoto.getFitHeight()
        );
        clip.setArcWidth(1000);
        clip.setArcHeight(1000);
        profilePhoto.setClip(clip);
        profilePhoto.setImage(ImageController.byteArrayToImage(profileImageArray));

        String forwardInfo = sender.equals(grandSender) ? "" : "forwarded from "+grandSender;

        header.getChildren().addAll(profilePhoto , new Label(sender + ": ") , new Label(forwardInfo));

        initializeButtonRow();

        card.getChildren().addAll(header , messageText, messageImage , messageDate,buttonRow);
        card.setId(String.valueOf(this.messageId));
    }

    private void initializeButtonRow() {
        Button delete = new Button("delete");
        Button edit = new Button("edit");
        Button forward = new Button("forward");
        Button save = new Button("save");
        buttons.add(forward);
        buttons.add(save);
        buttonRow.getChildren().add(save);

        switch (type){
            case NONE -> buttonRow.getChildren().add(forward);
            case DELETE -> {
                buttonRow.getChildren().addAll(forward, delete);
                buttons.add(delete);
            }
            case EDIT -> {
                buttonRow.getChildren().addAll(forward, edit);
                buttons.add(edit);
            }
            case BOTH -> {
                buttonRow.getChildren().addAll(forward, edit, delete);
                buttons.add(delete);
                buttons.add(edit);
            }
        }

        for (Button button : buttons) {
            button.setId(String.valueOf(this.messageId));
            button.setFont(Font.font(10));
        }

        delete.setOnAction(event -> {
            long id = Long.parseLong(delete.getId());
            MESSAGE_CONTROLLER.deleteMessage(id);
            //TODO:     REFRESH
        });

        edit.setOnAction(event -> {
            boolean isEdited = EditMessage.display(Long.parseLong(edit.getId()));
            if (isEdited) {
                loadCard();
            }

        });

        forward.setOnAction(event -> {
            Forward.display(Long.parseLong(forward.getId()));
        });

        save.setOnAction(event -> {
            MESSAGE_CONTROLLER.insertSavedMessage(Long.parseLong(save.getId()));
        });

    }

    public VBox getCard() {
        return card;
    }

    public void setCard(VBox card) {
        this.card = card;
    }
}
