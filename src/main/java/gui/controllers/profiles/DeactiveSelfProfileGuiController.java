package gui.controllers.profiles;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import models.LoggedUser;

public class DeactiveSelfProfileGuiController implements Controllers {

    public void activateButtonClicked(ActionEvent actionEvent) {
        SETTING_CONTROLLER.activateAccount(LoggedUser.getLoggedUser().getUsername());
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }
}
