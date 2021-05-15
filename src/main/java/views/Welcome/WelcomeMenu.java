package views.Welcome;

import views.ConsoleColors;
import views.Menu;
import views.profiles.FollowingProfile;

import java.util.Arrays;

public class WelcomeMenu extends Menu {

    public WelcomeMenu() {
        options = Arrays.asList("Login", "Register");
    }

    @Override
    public void run() {
        System.out.println(ConsoleColors.PURPLE_BOLD +"Hi. choose what you want to do");
        String input;
        boolean isValid;
        do {
            for (int i = 1; i < options.size() + 1; i++) {
                System.out.println(i + " :" + options.get(i-1));
            }
            input = scanner.nextLine();
            isValid = checkValidation(input);
        } while (!isValid);
        int inputInt = Integer.parseInt(input);
        Menu nextMenu = getMenu(inputInt);
        //nextMenu.setScanner(scanner);
        nextMenu.run();
    }


    @Override
    public Menu getMenu(int option) {
        return switch (option) {
            case 1 -> new LoginMenu();
            case 2 -> new RegisterMenu();
            default -> null;
        };
    }

    @Override
    public boolean checkValidation(String... input) {
        try {
            int inputInt = Integer.parseInt(input[0]);
            if (inputInt == 1 || inputInt == 2) {
                return true;
            }
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "input should be either 1 or 2! press Enter to try again"+ConsoleColors.RESET);
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT +"You haven't entered a number! press Enter to try again"+ConsoleColors.RESET);
            scanner.nextLine();
        }
        return false;
    }


}