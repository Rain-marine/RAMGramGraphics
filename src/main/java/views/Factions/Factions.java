package views.Factions;

import controllers.FactionsController;
import models.Group;
import models.User;
import views.Menu;
import views.PersonalPageMenu;
import views.profiles.FollowingProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Factions extends Menu {
    private final FactionsController factionsController;

    public Factions() {
        factionsController = new FactionsController();
    }

    @Override
    public void run() {
        while (true) {
            List<Group> groups = factionsController.getFactions();
            HashMap<String, Group> groupNameToGroup = extractGroupName(groups);
            showFactions(groups);
            System.out.println("enter faction name! or \"new faction\"! or \"*back\" for back to previous menu!");
            String input = scanner.nextLine();
            if (input.equals("*back"))
                break;
            if (input.equals("new faction")) {
                createNewFaction(groupNameToGroup);
                continue;
            }
            if(!groupNameToGroup.containsKey(input)){
                System.out.println("Invalid input!");
                continue;
            }
            switch (input) {
                case "followers" -> showFollowers();
                case "followings" -> showFollowings();
                case "black list" -> showBlackList();
                default -> showUserFaction(groupNameToGroup.get(input));
            }
        }
        new PersonalPageMenu().run();
    }

    private void showUserFaction(Group group) {
        new UserFaction(group).run();
        System.out.println("enter any key to continue!");
        scanner.nextLine();
    }

    private void showBlackList() {
        new BlackListFaction().run();
        System.out.println("enter any key to continue!");
        scanner.nextLine();
    }

    private void showFollowings() {
        new FollowingsFaction().run();
        System.out.println("enter any key to continue!");
        scanner.nextLine();
    }

    private void showFollowers() {
        new FollowersFaction().run();
        System.out.println("enter any key to continue!");
        scanner.nextLine();
    }

    private HashMap<String, User> extractUsernameToUser(List<User> followers) {
        HashMap<String, User> usernameToUser = new HashMap<>();
        followers.forEach(user -> usernameToUser.put(user.getUsername(),user));
        return usernameToUser;
    }

    private void createNewFaction(HashMap<String, Group> groupNameToGroup) {
        String name ;
        while (true) {
            System.out.println("enter fraction name!");
            name = scanner.nextLine();
            if (groupNameToGroup.containsKey(name)) {
                System.out.println("you have a group with this name! enter another name");
                continue;
            }
            if (name.equals("*back"))
                return;
            if (name.equals("")) {
                System.out.println("name is invalid");
                continue;
            }
            break;
        }
        List<String> users = new ArrayList<>();
        while (true) {
            System.out.println("enter username to add. just enter to finish");
            String username = scanner.nextLine();
            if (username.equals("*back"))
                return;
            if (username.equals("")) {
                if (users.size() == 0) {
                    System.out.println("group has no member. add at least 1 user!");
                    continue;
                }
                factionsController.insertNewFaction(name, users);
                System.out.println("group created!. you get back to previous menu!");
                return;
            }
            if (factionsController.canAddToGroup(username)) {
                users.add(username);
                System.out.println("user added!");
                continue;
            }
            System.out.println("can not add this user to group!. you have to follow the user!");
        }
    }

    private HashMap<String, Group> extractGroupName(List<Group> groups) {
        HashMap<String, Group> groupNameToGroup = new HashMap<>();
        for (Group group : groups) {
            groupNameToGroup.put(group.getName(), group);
        }
        groupNameToGroup.put("followers", null);
        groupNameToGroup.put("followings", null);
        groupNameToGroup.put("black list", null);
        return groupNameToGroup;
    }

    private void showFactions(List<Group> groups) {
        System.out.println( "followers\nfollowings\nblack list");
        for (Group group : groups) {
            System.out.println(group.getName());
        }
    }

    @Override
    public Menu getMenu(int option) {
        return null;
    }
}
