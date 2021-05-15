package views.Message;

import controllers.ChatController;
import models.Chat;
import models.LoggedUser;
import models.Message;
import models.User;
import views.ConsoleColors;
import views.Menu;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ChatMenu extends Menu {
    private final Chat chat;
    private final User frontUser;
    private final ChatController chatController;
    private final Menu previousMenu;

    public ChatMenu(Chat chat, Menu previousMenu) {
        this.chat = chat;
        this.frontUser = chat.getUserChats().get(0).getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())
                ? chat.getUserChats().get(1).getUser()
                : chat.getUserChats().get(0).getUser();
        this.previousMenu = previousMenu;
        chatController = new ChatController();
    }

    @Override
    public void run() {
        chatController.seeChat(chat);
        List<Message> messages = chatController.getMessages(chat);
        System.out.println(frontUser.getUsername()); // fix last seen
        showMessages(messages);
        while (true) {
            System.out.println("You are in " + frontUser.getUsername() + " direct!\n type \"new message\" to send message or any key to back");
            String input = scanner.nextLine();
            if (input.equals("new message")){
                getNewMessage();
            } else
                break;
        }
        previousMenu.run();
    }

    private void getNewMessage() {
        System.out.println("Type your message!");
        String message = scanner.nextLine();
        chatController.addMessageToChat(chat.getId(), message,null, frontUser);
        System.out.println("You : " + message + " Date : " + new Date());
        System.out.println("press any key to continue!");
        scanner.nextLine();
    }

    private void showMessages(List<Message> messages) {
        if (messages.size() == 0) {
            System.out.println("There is no message to show!");
            return;
        }
        for (Message message : messages) {
            String usernameToShow = message.getSender().getUsername().equals(LoggedUser.getLoggedUser().getUsername()) ?
                    "You" : message.getSender().getUsername();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+ usernameToShow + " : " + ConsoleColors.BLUE_BOLD+ message.getText()
                    +ConsoleColors.CYAN + "\tDate : " + message.getDate().toString()+ConsoleColors.RESET);
        }
    }

    @Override
    public Menu getMenu(int option) {
        return null;
    }
}
