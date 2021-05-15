package views.profiles;

import controllers.NotificationController;
import controllers.SettingController;
import controllers.UserController;
import models.User;
import views.ConsoleColors;
import views.ExplorerMenu;
import views.Menu;

import java.util.Arrays;

public class PendingRequestProfile extends Menu {
    private final User user;
    private final SettingController settingController;
    private final UserController userController;
    private final NotificationController notificationController;
    private final Menu previousMenu;
    private String lastSeen;
    private String phoneNumber;
    private String email;
    private String birthday;
    private String bio;


    public PendingRequestProfile(User user, Menu previousMenu) {
        this.user = user;
        this.previousMenu = previousMenu;
        settingController = new SettingController();
        userController = new UserController();
        this.lastSeen = settingController.lastSeenForLoggedUser(user);
        this.phoneNumber = settingController.phoneNumberForLoggedUser(user);
        this.email = settingController.emailForLoggedUser(user);
        this.birthday = settingController.birthdayForLoggedUser(user);
        notificationController = new NotificationController();
        this.bio = user.getBio();
        options = Arrays.asList("Remove Request", "Block", "Report User", "Back");
    }

    @Override
    public void run() {
        showInfo();
        boolean isValid;
        String input;
        do {
            for (int i = 1; i < options.size() + 1; i++) {
                System.out.println(i + " : " + options.get(i-1));
            }
            input = scanner.nextLine();
            isValid = checkValidation(input);
        } while(!isValid);
        int inputInt = Integer.parseInt(input);
        switch (inputInt) {
            case 1 -> takeBackRequest();
            case 2 -> blockUser();
            case 3 -> reportUser();
            case 4 -> getMenu(1).run();
            default -> getMenu(0).run();
        }


    }

    private void takeBackRequest() {
        notificationController.deleteRequest(user);
        new PrivateProfile(user,previousMenu).run();
    }


    private void reportUser() {
        userController.reportUser(user);
        System.out.println("user reported. this does not mean you unfollowed them or will not see their tweets henceforth ");
        run();
    }

    private void blockUser() {
        System.out.println("remove your request first");
        run();

    }

    public void showInfo(){
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + user.getUsername() + ConsoleColors.RESET + " Profile" +
                "\nName: " + user.getFullName() + "\nLast seen: " + lastSeen);
        if (!bio.equals(""))
            System.out.println("Bio: " + bio);
        if (!email.equals("not visible"))
            System.out.println("Email: " + email);

        if (!(user.getBirthday() == null) && (!birthday.equals("not visible")))
            System.out.println("Birthday: " + birthday);

        if (!(user.getPhoneNumber().equals("")) && (!phoneNumber.equals("not visible")))
            System.out.println("Phone Number: " + phoneNumber);

    }


    @Override
    public Menu getMenu(int option) {
        if (option == 1)
            return new ExplorerMenu();
        return null;
    }

    @Override
    public boolean checkValidation(String... input) {
        try{
            int inputInt = Integer.parseInt(input[0]);
            if (inputInt > 0 && inputInt < 5) {
                return true;
            }
            System.out.println("input must be between 1 and 4");
        } catch (Exception e) {
            System.out.println("You haven't entered a number!");
        }
        return false;
    }
}
