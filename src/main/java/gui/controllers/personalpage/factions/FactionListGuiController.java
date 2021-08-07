package gui.controllers.personalpage.factions;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import gui.controllers.popups.factions.CreatFactionBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Group;
import util.ConfigLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FactionListGuiController implements Initializable , Controllers {

    @FXML
    private ScrollPane factionArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       loadFactions();
    }

    private void loadFactions() {
        List<Group> factions = FACTIONS_CONTROLLER.getFactions();
        VBox list = new VBox(10);
        for (Group faction : factions) {
            Label factionName = new Label(faction.getName());
            Button show = new Button("show");
            show.setId(String.valueOf(faction.getId()));
            show.setOnAction(event -> {
                FactionUsersGuiController.setFactionID(Integer.parseInt(show.getId()));
                SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("factionUsers"), event);
            });

            HBox factionRow = new HBox(5);
            factionRow.getChildren().addAll(factionName,show);
            list.getChildren().add(factionRow);
        }
        factionArea.setContent(list);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().personalPage(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void newFactionButtonClicked(ActionEvent actionEvent) {
        boolean answer = CreatFactionBox.display();
        if (answer){
            loadFactions();
        }
    }

    public void blackListButtonClicked(ActionEvent actionEvent) {
        DefaultFactionsGuiController.setList(DefaultFactionsGuiController.LIST.BLACKLIST);
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("defaultFactions"), actionEvent);
    }

    public void followingButtonClicked(ActionEvent actionEvent) {
        DefaultFactionsGuiController.setList(DefaultFactionsGuiController.LIST.FOLLOWING);
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("defaultFactions"), actionEvent);
    }

    public void followerButtonClicked(ActionEvent actionEvent) {
        DefaultFactionsGuiController.setList(DefaultFactionsGuiController.LIST.FOLLOWER);
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("defaultFactions"), actionEvent);
    }
}
