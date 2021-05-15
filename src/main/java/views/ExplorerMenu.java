package views;

import controllers.ProfileAccessController;
import controllers.TweetController;
import controllers.UserController;
import models.User;
import views.profiles.FollowingProfile;
import views.profiles.ProfileNotVisible;

import java.util.Arrays;

public class ExplorerMenu extends Menu {
    public ExplorerMenu() {
        options = Arrays.asList("Search", "Interested tweets", "Back");
    }

    private TweetController tweetController = new TweetController();
    private UserController userController = new UserController();


    @Override
    public void run() {
        System.out.println("**Explorer**");
        boolean isValid;
        String input;

        do {
            for (int i = 0; i < options.size(); i++) {
                System.out.println(i + " : " + options.get(i));
            }
            input = scanner.nextLine();
            isValid = checkValidation(input);
        } while (!isValid);

        int intInput = Integer.parseInt(input);
        if (intInput == 0)
            search();
        else if (intInput == 1)
            showTopTweets();
        else
            getMenu(3).run();

    }

    private void showTopTweets() {
        TweetMenu tweetMenu = new TweetMenu(tweetController.getTopTweets(),1);
        tweetMenu.run();
    }

    private void search() {
        System.out.println("type username you want to find or type " + ConsoleColors.CYAN_BOLD + "*back*" + ConsoleColors.PURPLE_BOLD_BRIGHT
                + " to go back to explorer page");
        String usernameToFind = scanner.nextLine();
        if (usernameToFind.equals("*back*")) {
            getMenu(0).run();
        }
        else {
            User user = userController.getUserByUsername(usernameToFind);
            if (user == null) {
                new ProfileNotVisible().run();
            } else {
                ProfileAccessController profileAccessController = new ProfileAccessController(getMenu(0),user);
                profileAccessController.checkAccessibility().run();
            }
        }
    }

    @Override
    public Menu getMenu(int option) {
        if (option == 0)
            return new ExplorerMenu();
        return new MainMenu();
    }

    @Override
    public boolean checkValidation(String... input) {
        try {
            int inputInt = Integer.parseInt(input[0]);
            if (inputInt == 0 || inputInt == 1 || inputInt == 2) {
                return true;
            }
            System.out.println("input should be between 0 to 2");
        } catch (Exception e) {
            System.out.println("You haven't entered a number!");
        }
        return false;
    }
}
