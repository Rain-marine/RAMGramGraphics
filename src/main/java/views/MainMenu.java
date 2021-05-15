package views;

import views.Message.MessageMenu;
import views.Setting.SettingMenu;
import views.profiles.FollowingProfile;

import java.util.Arrays;

public class MainMenu extends Menu {

    public MainMenu() {
        options = Arrays.asList("Personal page", "Timeline", "Explorer", "Message", "Setting");
    }

    @Override
    public void run() {
        System.out.println("**Main Menu**");
        boolean isValid;
        String input;
        do {
            for (int i = 1; i < options.size() + 1; i++) {
                System.out.println(i + " : " + options.get(i - 1));
            }
            input = scanner.nextLine();
            isValid = checkValidation(input);
        } while (!isValid);
        process(Integer.parseInt(input));

    }

    private void process(int request) {
        switch (request) {
            case 1 -> getMenu(1).run();
            case 2 -> getMenu(2).run();
            case 3 -> getMenu(3).run();
            case 4 -> getMenu(4).run();
            default -> getMenu(5).run();
        }
    }

    @Override
    public Menu getMenu(int option) {
        return switch (option) {
            case 1 -> new PersonalPageMenu();
            case 2 -> new TimelineMenu();
            case 3 -> new ExplorerMenu();
            case 4 -> new MessageMenu();
            default -> new SettingMenu();
        };
    }

    @Override
    public boolean checkValidation(String... input) {
        try{
            int inputInt = Integer.parseInt(input[0]);
            if (inputInt > 0 && inputInt < 6) {
                return true;
            }
            System.out.println("input must be between 1 and 5");
        } catch (Exception e) {
            System.out.println("You haven't entered a number!");
        }
        return false;
    }
}