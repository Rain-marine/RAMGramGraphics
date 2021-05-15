package views.Factions;

import controllers.FactionsController;
import models.User;
import views.Menu;
import views.profiles.FollowingProfile;

import java.util.HashMap;
import java.util.List;

public class FollowingsFaction extends Menu {
    private final FactionsController factionsController;

    public FollowingsFaction() {
        factionsController = new FactionsController();
    }

    @Override
    public void run() {
        List<User> followings = factionsController.getActiveFollowings();
        if (followings.size() == 0) {
            System.out.println("you have no followings! enter any key to back previous menu!");
            scanner.nextLine();
            return;
        }
        for (User following : followings) {
            System.out.println(following.getUsername());
        }
        HashMap<String, User> usernameToUser = extractUsernameToUser(followings);
        while (true) {
            System.out.println("enter username to see profile! or \"*back\"");
            String input = scanner.nextLine();
            if (input.equals("*back"))
                return;
            if(!usernameToUser.containsKey(input)) {
                System.out.println("username is invalid!");
                continue;
            }
            new FollowingProfile(usernameToUser.get(input), new FollowingsFaction()).run();
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
