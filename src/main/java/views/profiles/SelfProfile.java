package views.profiles;

import controllers.UserController;
import models.LoggedUser;
import models.User;
import repository.UserRepository;
import views.ConsoleColors;
import views.MainMenu;
import views.Menu;
import views.PersonalPageMenu;

public class SelfProfile extends Menu{

    private final Menu previousMenu;
    private final UserController userController;
    public SelfProfile(Menu previousMenu) {
        this.previousMenu = previousMenu;
        userController = new UserController();
    }

    @Override
    public void run() {
        User user = userController.getUserByUsername(LoggedUser.getLoggedUser().getUsername());
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+"Hello "+user.getFullName()+". This is your profile");
        try {
            String birthday = user.getBirthday().toString();
            System.out.println("Username: " + user.getUsername() + "\nName: " + user.getFullName()
                    + "\nEmail: " + user.getEmail()
                    + "\nBirthday: " + birthday
                    + "\nPhone Number: " + user.getPhoneNumber()
                    + "\nBio: " + user.getBio());
        } catch (Exception e) {
            System.out.println("Username: " + user.getUsername() + "\nName: " + user.getFullName()
                    + "\nEmail: " + user.getEmail()
                    + "\nBirthday: NOT SET "
                    + "\nPhone Number: " + user.getPhoneNumber()
                    + "\nBio: " + user.getBio());
        } finally {
            System.out.println("press enter to go back"+ConsoleColors.RESET);
            scanner.nextLine();
            previousMenu.run();
        }
    }

    @Override
    public Menu getMenu(int option) {
        return new MainMenu();
    }
}
