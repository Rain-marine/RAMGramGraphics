package controllers;

import models.User;
import repository.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterManager {
    private final UserRepository userRepository = new UserRepository();

    public void makeNewUser(String fullName, String username, String password, String email, String phoneNumber, String bio, String birthDate)   {
        Date birthday = null ;
        if (birthDate!=null && !birthDate.equals("")) {
            try {
                birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
            } catch (ParseException e) {
                return;
            }
        }
        User user = new User(username,fullName,email,password,phoneNumber, bio,birthday);
        userRepository.insert(user);
    }

    public boolean isUsernameAvailable(String username) {
        User user = userRepository.getByUsername(username);
        return (user == null);
    }

    public boolean isEmailAvailable(String email) {
        User user = userRepository.getByEmail(email);
        return user == null;
    }

    public boolean isPhoneNumberAvailable(String phoneNumber) {
        User user = userRepository.getByPhoneNumber(phoneNumber);
        return user == null;
    }
}
