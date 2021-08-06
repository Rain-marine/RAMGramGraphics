package gui.controllers;

import controllers.Controllers;

public class InfoLoader implements Controllers {

    public static String load(long userId){
        String lastSeen = SETTING_CONTROLLER.lastSeenForLoggedUser(userId);
        String phoneNumber = SETTING_CONTROLLER.phoneNumberForLoggedUser(userId);
        String email = SETTING_CONTROLLER.emailForLoggedUser(userId);
        String birthday = SETTING_CONTROLLER.birthdayForLoggedUser(userId);
        String bio = USER_CONTROLLER.getUserBio(userId);

        String infoText = USER_CONTROLLER.getUsername(userId) + "'s Profile" +
                "\nName: " + USER_CONTROLLER.UserFullName(userId) + "\nLast seen: " + lastSeen;
        if (!bio.equals(""))
            infoText += ("\nBio: " + bio);
        if (!email.equals("not visible"))
            infoText += ("\nEmail: " + email);
        if (!birthday.equals("not visible"))
            infoText += ("\nBirthday: " + birthday);
        if (!phoneNumber.equals("not visible"))
            infoText += ("\nPhone Number: " + phoneNumber);

        return infoText;
    }
}
