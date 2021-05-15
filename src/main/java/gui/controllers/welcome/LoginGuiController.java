package gui.controllers.welcome;

import controllers.AuthController;
import exceptions.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import views.profiles.DeActiveSelfProfile;

public class LoginGuiController {
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorMessage;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private final AuthController authController = new AuthController();
    private final static Logger log = LogManager.getLogger(LoginGuiController.class);



    public void loginButtonClicked(ActionEvent actionEvent) {
        System.out.println("logging in....");
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if(username.isEmpty())
            errorMessage.setText("Enter Your Username");
        else if(password.isEmpty())
            errorMessage.setText("Enter Your Password");
        else {
            try {
                User user = authController.login(username, password);
                if(user.isActive()) {
                    log.info(username + " logged in");
                    try {
                        root = FXMLLoader.load(getClass().getClassLoader().getResource("MainMenu.fxml"));
                        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //todo : deActive profile
                    new DeActiveSelfProfile(username).run();
                }
            }
            catch (InvalidInputException e) {
                errorMessage.setText(e.getMessage());
            }
        }
    }

    public void registerButtonClicked(ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("Welcome/Register.fxml"));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }


}
