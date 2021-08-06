package gui.controllers.messages;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import util.ConfigLoader;

public class ChatCard implements Controllers {

    private final long chatId;
    private VBox card;
    private final boolean isGroup;

    public ChatCard(Long chatId) {
        this.chatId = chatId;
        this.card = new VBox(5);
        this.isGroup = CHAT_CONTROLLER.isGroup(chatId);

        Label name = new Label(CHAT_CONTROLLER.getChatName(chatId));
        name.setFont(Font.font(14));
        name.setTextFill(Color.INDIGO);

        Label unseenCountLabel = new Label(CHAT_CONTROLLER.getUnseenCount(chatId));
        unseenCountLabel.setFont(Font.font(11));
        unseenCountLabel.setTextFill(Color.MEDIUMVIOLETRED);

        Button goToChat = new Button("Show");
        goToChat.setId(String.valueOf(this.chatId));
        goToChat.setOnAction(event -> {
            if (this.isGroup){
                GroupChatShowerGuiController.setGroupId(chatId);
                SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("groupChat"), event);
            }
            else{
                ChatShowerGuiController.setChatId(chatId);
                SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("chat"), event);
            }

        });

        HBox row = new HBox(10);
        row.getChildren().addAll(name, goToChat);

        card.getChildren().addAll(row, unseenCountLabel);
    }

    public VBox getCard() {
        return card;
    }

    public void setCard(VBox card) {
        this.card = card;
    }
}
