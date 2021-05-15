package views;

import controllers.RegisterManager;
import controllers.UserController;
import models.LoggedUser;
import models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class EditInfo extends Menu {
    //back to personal page
    private final UserController userController;
    private final User user;
    private final RegisterManager registerManager;

    public EditInfo() {
        this.userController = new UserController();
        this.user = LoggedUser.getLoggedUser();
        options = Arrays.asList("username", "name", "birthday", "email", "phone number", "bio", "back");
        registerManager = new RegisterManager();
    }

    @Override
    public void run() {
        System.out.println("**Edit Your Information**");
        Input:
        while (true) {
            System.out.println("type what you want to change. to change password go to setting");
            for (String option : options) {
                System.out.println(ConsoleColors.CYAN + option + ConsoleColors.RESET);
            }
            String input = scanner.nextLine();
            if (!options.contains(input)) {
                System.out.println("unknown input!");
                continue;
            }
            switch (input) {
                case "username" -> changeUsername();
                case "name" -> changeName();
                case "birthday" -> changeBirthday();
                case "email" -> changeEmail();
                case "phone number" -> changePhoneNumber();
                case "bio" -> changeBio();
                case "back" -> getMenu(3).run();
                default -> {
                    break Input;
                }
            }
        }

    }

    private void changePhoneNumber() {
        System.out.println("enter your new phone number. type \"*back*\" to go back");
        String newNumber = scanner.nextLine();
        if (newNumber.equals("*back*")) {
            run();
        }
        else{
            if (registerManager.isPhoneNumberAvailable(newNumber)){
                userController.changeNumber(newNumber);
                System.out.println("phone number changed successfully.");
            }
            else{
                System.out.println("phone number already exists!");
            }
        }
        System.out.println("press enter to go back.");
        scanner.nextLine();
        run();
    }

    private void changeEmail() {
        System.out.println("enter your new email. type \"*back*\" to go back");
        String newEmail = scanner.nextLine();
        if (newEmail.equals("*back*")) {
            run();
        }
        else {
            if(!newEmail.contains("@") || !newEmail.contains(".")){
                System.out.println("invalid email address");
            }
            else if (registerManager.isEmailAvailable(newEmail)){
                userController.changeEmail(newEmail);
                System.out.println("email changed successfully.");
            }
            else {
                System.out.println("email is already registered");
            }
            System.out.println("press enter to go back.");
            scanner.nextLine();
            run();
        }

    }

    private void changeBirthday() {
        System.out.println("enter your new birthday in yyyy/MM/dd format. type \"*back*\" to go back");
        String newBirthday = scanner.nextLine();
        if (newBirthday.equals("*back*")) {
            run();
        }
        else {
            Date birthday;
            try {
                birthday = new SimpleDateFormat("yyyy/MM/dd").parse(newBirthday);
                userController.changeBirthday(birthday);
                System.out.println("birthday changed successfully.");
            }
            catch (ParseException e) {
                System.out.println("birthday format not valid");
            }
            finally {
                System.out.println("press enter to go back.");
                scanner.nextLine();
                run();
            }

        }
    }

    private void changeName() {

        System.out.println("enter your new name. type \"*back*\" to go back");
        String newName = scanner.nextLine();
        if (newName.equals("*back*")) {
            run();
        } else {
            userController.changeName(newName);
            System.out.println("name changed successfully. press enter to go back.");
            scanner.nextLine();
            run();
        }
    }

    private void changeUsername() {
        System.out.println("type your new username. type \"*back*\" to go back");
        String newUsername = scanner.nextLine();
        if (newUsername.equals("*back*")) {
            run();
        } else {
            if (registerManager.isUsernameAvailable(newUsername)) {
                userController.ChangeUsername(newUsername);
                System.out.println("Username successfully changed. log out and log in to see changes \npress enter to continue");
            } else {
                System.out.println("username already exists. press enter to go back to Edit Page");
            }
            scanner.nextLine();
            run();
        }
    }

    private void changeBio() {
        System.out.println("enter your new bio. type \"*back*\" to go back");
        String newBio = scanner.nextLine();
        if (newBio.equals("*back*")) {
            run();
        } else {
            userController.changeBio(newBio);
            System.out.println("bio changed successfully. press enter to go back.");
            scanner.nextLine();
            run();
        }

    }


    @Override
    public Menu getMenu(int option) {
        if (option == 3)
            return new PersonalPageMenu();
        return new MainMenu();
    }
}
