package views.Setting;

import controllers.SettingController;
import views.MainMenu;
import views.Menu;
import views.Welcome.WelcomeMenu;
import views.profiles.FollowingProfile;

import java.util.Arrays;

public class SettingMenu extends Menu {
    private final SettingController settingsController;
    public SettingMenu() {
        settingsController = new SettingController();
        options = Arrays.asList("privacy/security", "delete account", "log out", "back");
    }

    @Override
    public void run() {
        System.out.println("**Setting**");
        Input:
        while (true) {
            System.out.println("You are in setting menu, type your request");
            showOption();
            String input = scanner.nextLine();
            if (!options.contains(input)) {
                System.out.println("unknown input!");
                continue;
            }
            switch (input) {
                case "privacy/security" -> getMenu(1).run();
                case "delete account" -> deleteAccount();
                case "log out" -> logout();
                case "back" -> getMenu(0).run();
                default -> {
                    break Input;
                }
            }
        }
    }

    private void showOption() {
        System.out.println("privacy/security -> change account visibility,last seen, password or deActive account");
        System.out.println("delete account -> delete your account forever!");
        System.out.println("log out -> log out and close app!");
        System.out.println("back -> back to main menu");
    }

    private void logout() {
        System.out.println("Are you sure?!(Y/N)");
        String input = scanner.nextLine();
        if (input.equals("Y")) {
            settingsController.logout();
            getMenu(3).run();
            return;
        }
        System.out.println("You get back to setting menu");
        System.out.println("press any key to continue!");
        scanner.nextLine();
    }

    private void deleteAccount() {
        System.out.println("Are you sure?!(Y/N)");
        String input = scanner.nextLine();
        if (input.equals("Y")) {
            settingsController.deleteAccount();
            getMenu(3).run();
            return;
        }
        System.out.println("You get back to setting menu");
        System.out.println("press any key to continue!");
        scanner.nextLine();
    }

    @Override
    public Menu getMenu(int option) {
        if (option == 1)
            return new PrivacySettingMenu();
        if (option == 3)
            return new WelcomeMenu();
        return new MainMenu();
    }

    @Override
    public boolean checkValidation(String... input) {
        return false;
    }
}
