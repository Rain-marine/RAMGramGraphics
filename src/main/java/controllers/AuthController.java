package controllers;

import exceptions.InvalidInputException;
import models.LoggedUser;
import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.Repository;

import java.util.Date;


public class AuthController implements Repository {
    private final static Logger log = LogManager.getLogger(AuthController.class);

    public User login(String username, String password) throws InvalidInputException {
        User user = USER_REPOSITORY.getByUsername(username);
        if (user == null) {
            throw new InvalidInputException("Username not found");
        } else {
            String rightPass = user.getPassword();
            if (!rightPass.equals(password)) {
                log.warn(username + " entered wrong password");
                throw new InvalidInputException("Wrong password");
            }
            else {
                LoggedUser.setLoggedUser(user);
                log.info(username + " logged in");
                if (user.isActive()) {
                    log.info(username + " last seen updated ");
                    USER_REPOSITORY.setLastSeen(user.getId(), new Date());
                }
                return user;
            }

        }

    }
}
