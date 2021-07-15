package gui.controllers.profiles;

import gui.controllers.Toolbar;
import javafx.event.ActionEvent;

public class NotVisibleProfileGuiController {
    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().explorer(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }
}
