package views.Message;

import controllers.ChatController;
import models.Chat;
import models.LoggedUser;
import models.User;
import models.UserChat;
import views.Menu;

import java.util.HashMap;
import java.util.List;

public class PeopleChatListMenu extends Menu{
    private final ChatController chatController;

    public PeopleChatListMenu() {
        chatController = new ChatController();
    }

    @Override
    public void run() {
        System.out.println("**Your Chats With People**");
        List<Chat> chats = chatController.getChats();
        if (chats.size() == 0) {
            System.out.println("YOU HAVE NO CHAT\nyou get back to message menu! enter any key to continue!");
            scanner.nextLine();
            return;
        }
        showChats(chats);
        HashMap<String, Chat> usernameToChat = extractUserFromChat(chats);
        while (true) {
            System.out.println("enter username to see the chat! or enter *back for back to message menu!");
            String input = scanner.nextLine();
            if (input.equals("*back"))
                break;
            if (!usernameToChat.containsKey(input)) {
                System.out.println("no user found!\nenter any key to continue!");
                scanner.nextLine();
            } else
                new ChatMenu(usernameToChat.get(input), new PeopleChatListMenu()).run();
        }
    }

    private HashMap<String, Chat> extractUserFromChat(List<Chat> chats) {
        HashMap<String, Chat> usernameToChat = new HashMap<>();
        for (Chat chat : chats) {
            User userToShow = chat.getUserChats().get(0).getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())
                    ? chat.getUserChats().get(1).getUser()
                    : chat.getUserChats().get(0).getUser();
            usernameToChat.put(userToShow.getUsername(), chat);
        }
        return usernameToChat;
    }

    @Override
    public Menu getMenu(int option) {
        return new MessageMenu();
    }

    public void showChats(List<Chat> chats) {
        for (Chat chat : chats) {
            UserChat userToShow = chat.getUserChats().get(0).getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())
                    ? chat.getUserChats().get(1)
                    : chat.getUserChats().get(0);

            UserChat userToSee = chat.getUserChats().get(0).getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())
                    ? chat.getUserChats().get(0)
                    : chat.getUserChats().get(1);

            if (userToSee.isHasSeen())
                System.out.println(userToShow.getUser().getUsername());
            else if (userToSee.getUnseenCount()!=0)
                System.out.println(userToShow.getUser().getUsername() + "\t" + userToSee.getUnseenCount() + " Unseen Message");
        }
    }
}
