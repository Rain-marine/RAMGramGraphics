package controllers;

import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.Repository;

import java.util.Date;

public class RegisterManager  implements Repository {
    private final static Logger log = LogManager.getLogger(RegisterManager.class);


    public void makeNewUser(String fullName, String username, String password, String email, String phoneNumber, String bio, String birthday)   {
        Date birthdayDate = DateFormat.stringToDate(birthday);
        User user = new User(username,fullName,email,password,phoneNumber, bio, birthdayDate);
        USER_REPOSITORY.insert(user);
        log.info(" new User: " + username);
    }

    public boolean isUsernameAvailable(String username) {
        User user = USER_REPOSITORY.getByUsername(username);
        return (user == null);
    }

    public boolean isEmailAvailable(String email) {
        User user = USER_REPOSITORY.getByEmail(email);
        return user == null;
    }

    public boolean isPhoneNumberAvailable(String phoneNumber) {
        User user = USER_REPOSITORY.getByPhoneNumber(phoneNumber);
        return user == null;
    }
}
