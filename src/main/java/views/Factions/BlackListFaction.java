package views.Factions;

import controllers.FactionsController;
import models.User;
import views.Menu;
import views.profiles.BlockedProfile;

import java.util.HashMap;
import java.util.List;

public class BlackListFaction extends Menu {
    private final FactionsController factionsController;

    public BlackListFaction() {
        factionsController = new FactionsController();
    }

    @Override
    public void run() {
        List<User> blockedUsers = factionsController.getActiveBlockedUsers();
        if (blockedUsers.size() == 0) {
            System.out.println("you have no blocked users! enter any key to back previous menu!");
            scanner.nextLine();
            return;
        }
        for (User blockedUser : blockedUsers) {
            System.out.println(blockedUser.getUsername());
        }
        HashMap<String, User> usernameToUser = extractUsernameToUser(blockedUsers);
        while (true) {
            System.out.println("enter username to see profile! or \"*back\"");
            String input = scanner.nextLine();
            if (input.equals("*back"))
                return;
            if(!usernameToUser.containsKey(input)) {
                System.out.println("username is invalid!");
                continue;
            }
            new BlockedProfile(usernameToUser.get(input), new BlackListFaction()).run();
            break;
        }
    }

    private HashMap<String, User> extractUsernameToUser(List<User> followers) {
        HashMap<String, User> usernameToUser = new HashMap<>();
        followers.forEach(user -> usernameToUser.put(user.getUsername(),user));
        return usernameToUser;
    }

    @Override
    public Menu getMenu(int option) {
        return null;
    }
}
