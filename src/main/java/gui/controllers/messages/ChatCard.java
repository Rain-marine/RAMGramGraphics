package gui.controllers.messages;

import gui.controllers.Controllers;
import javafx.scene.layout.VBox;

public class ChatCard implements Controllers {

    private long chatId;
    private VBox card;

    public ChatCard(Long chatId) {
        this.chatId = chatId;
    }

    public VBox getCard() {
        return card;
    }

    public void setCard(VBox card) {
        this.card = card;
    }
}
