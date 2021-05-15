package controllers;

import exceptions.InvalidInputException;
import models.LoggedUser;
import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.UserRepository;

import java.util.Date;


public class AuthController {
    private final static Logger log = LogManager.getLogger(AuthController.class);
    private final UserRepository userRepository = new UserRepository();

    public User login(String username, String password) throws InvalidInputException {
        User user = userRepository.getByUsername(username);
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
                    log.info(username + " last seen updated");
                    userRepository.setLastSeen(user.getId(), new Date());
                }
                return user;
            }

        }

    }
}
