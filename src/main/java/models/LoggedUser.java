package models;

import controllers.UserController;

public class LoggedUser {
    private static User loggedUser;
    private static final UserController USER_CONTROLLER = new UserController();

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        LoggedUser.loggedUser = loggedUser;
    }

    public static void update() {
        loggedUser = USER_CONTROLLER.getById(loggedUser.getId());
    }
}
