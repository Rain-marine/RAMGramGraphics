package gui.controllers.profiles;

import controllers.SettingController;
import gui.controllers.Controllers;
import gui.controllers.Toolbar;
import javafx.event.ActionEvent;
import models.LoggedUser;

public class DeactiveSelfProfileGuiController implements Controllers {

    public void activateButtonClicked(ActionEvent actionEvent) {
        SETTING_CONTROLLER.activateAccount(LoggedUser.getLoggedUser().getUsername());
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }
}
