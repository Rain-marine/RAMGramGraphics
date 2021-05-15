package views.profiles;

import controllers.*;
import models.Chat;
import models.Tweet;
import models.User;
import views.*;
import views.Message.ChatMenu;

import java.util.Arrays;
import java.util.List;

public class FollowingProfile extends Menu {
    private final User user;
    private final SettingController settingController;
    private final UserController userController;
    private final MessageController messageController;
    private final NotificationController notificationController;
    private final TweetController tweetController;
    private final Menu previousMenu;
    private String lastSeen;
    private String phoneNumber;
    private String email;
    private String birthday;
    private String bio;


    public FollowingProfile(User user, Menu previousMenu) {
        this.user = user;
        settingController = new SettingController();
        userController = new UserController();
        this.lastSeen = settingController.lastSeenForLoggedUser(user);
        this.phoneNumber = settingController.phoneNumberForLoggedUser(user);
        this.email = settingController.emailForLoggedUser(user);
        this.birthday = settingController.birthdayForLoggedUser(user);
        notificationController = new NotificationController();
        this.bio = user.getBio();
        this.previousMenu = previousMenu;
        options = Arrays.asList("Unfollow And Notify User", "Unfollow Without Notification", "Message", "Tweets"
                , "Block", "Report User", "Back");
        tweetController = new TweetController();
        messageController = new MessageController();
    }


    @Override
    public void run() {
        showInfo();
        boolean isValid;
        String input;
        do {
            for (int i = 1; i < options.size() + 1; i++) {
                System.out.println(i + " : " + options.get(i - 1));
            }
            input = scanner.nextLine();
            isValid = checkValidation(input);
        } while (!isValid);
        int inputInt = Integer.parseInt(input);
        switch (inputInt) {
            case 1 -> unfollowUserWithNotif();
            case 2 -> unfollowUserWithoutNotif();
            case 3 -> message();
            case 4 -> showTweets();
            case 5 -> blockUser();
            case 6 -> reportUser();
            case 7 -> previousMenu.run();
            default -> getMenu(0).run();
        }


    }

    private void message() {
        Chat chat = messageController.getChatWithUsername(user.getUsername());
        if (chat == null)
            chat = messageController.getChatWithUsername(user.getUsername());
        new ChatMenu(chat, new FollowingProfile(user, previousMenu)).run();
    }

    private void unfollowUserWithoutNotif() {
        notificationController.unfollowUserWithoutNotification(user);
        ProfileAccessController profileAccessController = new ProfileAccessController(previousMenu,user);
        profileAccessController.checkAccessibility().run();
    }

    private void unfollowUserWithNotif() {
        notificationController.unfollowUserWithNotification(user);
        ProfileAccessController profileAccessController = new ProfileAccessController(previousMenu,user);
        profileAccessController.checkAccessibility().run();
    }

    private void showTweets() {
        List<Tweet> listOfTweets = tweetController.getAllTweets(user);
        new TweetMenu(listOfTweets, 1).run();
    }


    private void reportUser() {
        userController.reportUser(user);
        System.out.println("user reported. this does not mean you unfollowed them or will not see their tweets henceforth ");
        run();
    }

    private void blockUser() {
        userController.blockUser(user);
        System.out.println("user blocked");
        new BlockedProfile(user, previousMenu).run();

    }


    public void showInfo() {
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
        return new MainMenu();
    }

    @Override
    public boolean checkValidation(String... input) {
        try {
            int inputInt = Integer.parseInt(input[0]);
            if (inputInt > 0 && inputInt < 8) {
                return true;
            }
            System.out.println("input must be between 1 and 7");
        } catch (Exception e) {
            System.out.println("You haven't entered a number!");
        }
        return false;
    }
}

