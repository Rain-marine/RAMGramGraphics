package views.Factions;

import controllers.FactionsController;
import controllers.UserController;
import models.User;
import views.Menu;
import views.profiles.PrivateProfile;
import views.profiles.PublicProfile;

import java.util.HashMap;
import java.util.List;

public class FollowersFaction extends Menu {
    private final FactionsController factionsController;
    private final UserController userController;

    public FollowersFaction() {
        factionsController = new FactionsController();
        userController = new UserController();

    }

    @Override
    public void run() {
        List<User> followers = factionsController.getActiveFollowers();
        if (followers.size() == 0) {
            System.out.println("you have no followers! enter any key to back previous menu!");
            scanner.nextLine();
            return;
        }
        for (User follower : followers) {
            System.out.println(follower.getUsername());
        }
        HashMap<String, User> usernameToUser = extractUsernameToUser(followers);
        while (true) {
            System.out.println("enter username to see profile! or \"*back\"");
            String input = scanner.nextLine();
            if (input.equals("*back"))
                return;
            if(!usernameToUser.containsKey(input)) {
                System.out.println("username is invalid!");
                continue;
            }
            if(userController.isAccountPublic(input)){
                new PublicProfile(usernameToUser.get(input), new FollowersFaction()).run();
            } else {
                new PrivateProfile(usernameToUser.get(input), new FollowersFaction()).run();
            }
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
