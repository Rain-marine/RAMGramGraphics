package views.Message;

import controllers.MessageController;
import models.LoggedUser;
import models.Message;
import views.ConsoleColors;
import views.Menu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedMessageMenu extends Menu {
    private final MessageController messageController;

    public SavedMessageMenu() {
        messageController = new MessageController();
        options = Arrays.asList("back", "new message");
    }


    @Override
    public void run() {
        System.out.println("**Saved Messages**");
        showOption();
        List<Message> savedMessage = messageController.getSavedMessage();
        showMessages(savedMessage);
        while (true) {
            System.out.println("You are in saved messages menu, type your request");
            String input = scanner.nextLine();
            if (!options.contains(input)) {
                System.out.println("Invalid input!");
                continue;
            }
            if ("new message".equals(input))
                getNewMessage();
            else
                break;
        }
    }


    private void showOption() {
        System.out.println("new message -> add message for yourself");
        System.out.println("back -> back to previous menu!");
    }

    private void getNewMessage() {
        System.out.println("type your message");
        String message = scanner.nextLine();
        Message messageToSave = new Message(message,null, LoggedUser.getLoggedUser(), LoggedUser.getLoggedUser());
        ArrayList<Message> messages = new ArrayList<>() {
            {
                add(messageToSave);
            }
        };
        messageController.insertSavedMessage(messageToSave);
        showMessages(messages);
        System.out.println("enter any key to continue!");
        scanner.nextLine();
    }

    @Override
    public Menu getMenu(int option) {
        if (option == 1)
            return new MessageMenu();
        else
            return new SavedMessageMenu();
    }

    public void showMessages(List<Message> messages) {
        if (messages.size() == 0) {
            System.out.println("YOU HAVE NO SAVED MESSAGES, TYPE \"new message\" TO ADD NEW MESSAGE");
            return;
        }
        for (Message message : messages) {
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + message.getSender().getUsername() + " : " + message.getText() +
                     ConsoleColors.BLUE + "\tDate : " + message.getDate().toString()+ConsoleColors.RESET);
        }
    }
}
