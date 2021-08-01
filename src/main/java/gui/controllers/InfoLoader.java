package gui.controllers;

import controllers.SettingController;
import controllers.TweetController;
import controllers.UserController;
import models.User;

public class InfoLoader {

    private final static SettingController SETTING_CONTROLLER = new SettingController();
    private static String lastSeen;
    private static String phoneNumber;
    private static String email;
    private static String birthday;
    private static String bio;

    public static String load(User user){
        lastSeen = SETTING_CONTROLLER.lastSeenForLoggedUser(user.getId());
        phoneNumber = SETTING_CONTROLLER.phoneNumberForLoggedUser(user);
        email = SETTING_CONTROLLER.emailForLoggedUser(user);
        birthday = SETTING_CONTROLLER.birthdayForLoggedUser(user);
        bio = user.getBio();

        String infoText = user.getUsername() + "'s Profile" +
                "\nName: " + user.getFullName() + "\nLast seen: " + lastSeen;
        if (!bio.equals(""))
            infoText += ("\nBio: " + bio);
        if (!email.equals("not visible"))
            infoText += ("\nEmail: " + email);
        if (!(user.getBirthday() == null) && (!birthday.equals("not visible")))
            infoText += ("\nBirthday: " + birthday);
        if (!(user.getPhoneNumber().equals("")) && (!phoneNumber.equals("not visible")))
            infoText += ("\nPhone Number: " + phoneNumber);

        return infoText;
    }
}
