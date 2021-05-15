package views.Setting;

import controllers.SettingController;
import models.LoggedUser;
import models.User;
import views.Menu;
import views.Welcome.WelcomeMenu;

import java.util.ArrayList;
import java.util.Arrays;

public class PrivacySettingMenu extends Menu {
    private final SettingController settingController;
    private final  ArrayList<String> status = new ArrayList<>() {
        {
            add("nobody");
            add("everybody");
            add("following");
        }
    };

    public PrivacySettingMenu() {
        settingController = new SettingController();
        options = Arrays.asList("private/public", "lastSeen/online", "activation", "change password",
                "change birthday visibility", "change email visibility","change number visibility","back");
    }

    @Override
    public void run() {
        System.out.println("**Privacy & Security**");
        Input:
        while (true) {
            System.out.println("You are in privacy & security menu, type your request");
            showOption();
            String input = scanner.nextLine();
            if (!options.contains(input)) {
                System.out.println("unknown input!");
                continue;
            }
            switch (input) {
                case "private/public" -> privatePublicChecker();
                case "lastSeen/online" -> lastSeenChecker();
                case "activation" -> activationChecker();
                case "change password" -> changePassword();
                case "change birthday visibility" -> changeBirthdayVisibility();
                case "change email visibility" -> changeEmailVisibility();
                case "change number visibility" -> changeNumberVisibility();
                default -> {
                    break Input;
                }
            }
        }
    }

    private void changeNumberVisibility() {
        User.Level numberStatus = settingController.getUserNumberStatus(LoggedUser.getLoggedUser());
        System.out.println("Your phone number is visible for " + numberStatus);
        System.out.println("Do you wanna change your status?(type the status) or any other character to go back");
        for (String state : status) {
            System.out.println(state);
        }
        String input = scanner.nextLine();
        if (!status.contains(input))
            System.out.println("Invalid input! You will get back to privacy setting!");
        else {
            settingController.changeNumberStatus(input);
            System.out.println("Now your phone number is visible for "
                    + settingController.getUserNumberStatus(LoggedUser.getLoggedUser()));
        }
        System.out.println("press any key to continue");
        scanner.nextLine();
    }

    private void changeEmailVisibility() {
        User.Level numberStatus = settingController.getUserEmailStatus(LoggedUser.getLoggedUser());
        System.out.println("Your email is visible for " + numberStatus);
        System.out.println("Do you wanna change your status?(type the status) or any other character to go back");
        for (String state : status) {
            System.out.println(state);
        }
        String input = scanner.nextLine();
        if (!status.contains(input))
            System.out.println("Invalid input! You will get back to privacy setting!");
        else {
            settingController.changeEmailStatus(input);
            System.out.println("Now your email is visible for "
                    + settingController.getUserEmailStatus(LoggedUser.getLoggedUser()));
        }
        System.out.println("press any key to continue");
        scanner.nextLine();
    }

    private void changeBirthdayVisibility() {
        User.Level numberStatus = settingController.getUserBirthdayStatus(LoggedUser.getLoggedUser());
        System.out.println("Your birthday is visible for " + numberStatus);
        System.out.println("Do you wanna change your status?(type the status) or any other character to go back");
        for (String state : status) {
            System.out.println(state);
        }
        String input = scanner.nextLine();
        if (!status.contains(input))
            System.out.println("Invalid input! You will get back to privacy setting!");
        else {
            settingController.changeBirthdayStatus(input);
            System.out.println("Now your birthday is visible for "
                    + settingController.getUserBirthdayStatus(LoggedUser.getLoggedUser()));
        }
        System.out.println("press any key to continue");
        scanner.nextLine();
    }

    private void changePassword() {
        System.out.println("Enter your password! if You wanna back, Enter 'B' ");
        boolean isValid = false;
        while (!isValid) {
            String input = scanner.nextLine();
            if (input.equals("B"))
                return;
            isValid = settingController.doesPasswordExist(input);
            if (!isValid)
                System.out.println("Password is incorrect! Enter again!");
        }

        System.out.println("Enter your new password!");
        String newPassword = scanner.nextLine();
        settingController.changePassword(newPassword);
        System.out.println("Password changed!");
        System.out.println("press any key to continue");
        scanner.nextLine();
    }

    private void activationChecker() {
        System.out.println("Do you wanna deActive your account?(Y/N)\nYour account will be ghosted until you active it again!");
        String input = scanner.nextLine();
        if (input.equals("Y")) {
            settingController.deActiveAccount();
            new WelcomeMenu().run();
            return;
        }
        System.out.println("Account is still active and you get back to privacy setting!");
        System.out.println("press any key to continue");
        scanner.nextLine();
    }

    private void lastSeenChecker() {
        String lastSeenStatus = settingController.getUserLastSeenStatus(LoggedUser.getLoggedUser().getUsername());
        System.out.println("Your lastSeen is visible for " + lastSeenStatus);
        System.out.println("Do you wanna change your status?(type the status) or any other character to go back");
        for (String state : status) {
            if (!state.equals(lastSeenStatus)) {
                System.out.println(state);
            }
        }

        String input = scanner.nextLine();
        if (input.equals(lastSeenStatus) || !status.contains(input))
            System.out.println("Nothing changed! You get back to privacy setting!");
        else {
            settingController.changeLastSeenStatus(input);
            System.out.println("Now your lastSeen is visible for " + input);
        }
        System.out.println("press any key to continue");
        scanner.nextLine();
    }

    private void privatePublicChecker() {
        boolean isPublic = settingController.isAccountPublic(LoggedUser.getLoggedUser().getUsername());
        if (isPublic)
            System.out.println("Your Account is Public! change to Private?(Y/N)");
        else
            System.out.println("Your Account is Private! change to Public?(Y/N)");
        String input = scanner.nextLine();
        if (input.equals("Y")) {
            settingController.changeAccountVisibility(!isPublic);
            System.out.println("Account visibility changed!");
        } else
            System.out.println("No change! \nYou get back to privacy setting!");
        System.out.println("press enter to continue");
        scanner.nextLine();
    }

    private void showOption() {
        System.out.println("private/public -> change account visibility!");
        System.out.println("lastSeen/online -> change your last seen status!");
        System.out.println("activation -> deActive your account!");
        System.out.println("change password -> change your account password!");
        System.out.println("change birthday visibility -> change who can see your birthday");
        System.out.println("change number visibility -> change who can see your phone number");
        System.out.println("change email visibility -> change who can see your email");
        System.out.println("back -> back to setting");
    }

    @Override
    public Menu getMenu(int option) {
        return new SettingMenu();
    }
}