package views.Message;

import controllers.MessageController;
import models.Tweet;
import views.MainMenu;
import views.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageMenu extends Menu {
    private final MessageController messageController;

    public MessageMenu() {
        options = Arrays.asList("saved messages", "saved tweets", "new message", "people messages", "back");
        messageController = new MessageController();
    }

    @Override
    public void run() {

        while (true) {
            System.out.println("**Message Menu**");
            System.out.println("You are in message menu, type your request");
            showOption();

            String input = scanner.nextLine();
            if (!options.contains(input)) {
                System.out.println("Invalid input!");
                continue;
            }
            switch (input) {
                case "saved messages" -> getMenu(1).run();
                case "saved tweets" -> showSavedTweets();
                case "new message" -> sendMessage();
                case "people messages" -> getMenu(2).run();
                default -> getMenu(3).run();
            }
        }

    }

    private void sendMessage() {
        System.out.println("Type users and groups of receiver like this -> \n" +
                " User : \"UsernameToSend\" or Group : \"GroupNameToSend\"," +
                "type \"write message\" to write your message!" +
                " type \"*back\" to back to message menu");
        List<String> users = new ArrayList<>();
        List<String> groups = new ArrayList<>();
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("*back"))
                return;
            if (input.equals("write message")) {
                if (users.size() == 0 && groups.size() == 0) {
                    System.out.println("First specify receiver(s)");
                    continue;
                }
                System.out.println("Type your message!");
                String message = scanner.nextLine();
                messageController.sendMessage(message,null, users, groups);
                System.out.println("Message has sent! enter any key to back to message menu");
                scanner.nextLine();
                break;
            }
            String[] inputSplit = input.split(":");
            if (inputSplit.length != 2 || !inputSplit[0].trim().equals("User") && !inputSplit[0].trim().equals("Group")) {
                System.out.println("Invalid input!! type again");
                continue;
            }
            if (inputSplit[0].trim().equals("User")) {
                if (messageController.canSendMessageToUser(inputSplit[1].trim())) {
                    if (!users.contains("inputSplit[1].trim()")) {
                        users.add(inputSplit[1].trim());
                        System.out.println("Added! type another username or groupName!");
                    }
                    else
                        System.out.println("duplicate name!");
                } else
                    System.out.println("You cannot send message to this user! type another username!");
                continue;
            }
            if (messageController.canSendMessageToGroup(inputSplit[1].trim())) {
                if(!groups.contains(inputSplit[1].trim())) {
                    groups.add(inputSplit[1].trim());
                    System.out.println("Added! type another username or groupName!");
                }
                else
                    System.out.println("duplicate name");
            }
            else {
                System.out.println("You cannot send message to this Group! type another GroupName!");
            }
        }
    }

    private void showSavedTweets() {
        List<Tweet> tweets = messageController.getSavedTweets();
        if (tweets.size() == 0)
            System.out.println("You have no saved tweets to show!");
        else
            for (Tweet tweet : tweets)
                System.out.println(tweet.getUser().getUsername() + " : " + tweet.getText() + "\tDate: " + tweet.getTweetDateTime().toString());

        System.out.println("enter any key to back to message menu!");
        scanner.nextLine();
    }

    private void showOption() {
        System.out.println("saved messages -> see messages that you type for yourself or forwarded from your chats!\n" +
                "saved tweets -> see tweets that saved for yourself!\n" +
                "new message -> send message to your follower/following or groups!\n" +
                "people messages -> see your chats with your follower/following\n" +
                "back -> back to previous menu!");
    }

    @Override
    public Menu getMenu(int option) {
        return switch (option) {
            case 1 -> new SavedMessageMenu();
            case 2 -> new PeopleChatListMenu();
            case 3 -> new MainMenu();
            default -> new MessageMenu();
        };
    }
}
