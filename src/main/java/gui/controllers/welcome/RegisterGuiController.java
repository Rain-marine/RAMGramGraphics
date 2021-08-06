package gui.controllers.welcome;

import controllers.RegisterManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import util.ConfigLoader;

import java.util.Objects;

public class RegisterGuiController {
    static Logger log = LogManager.getLogger(RegisterGuiController.class);

    @FXML
    private Label fullNameError;
    @FXML
    private Label registerSuccessful;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;
    @FXML
    private Label rePasswordError;
    @FXML
    private Label emailError;
    @FXML
    private Label phoneNumberError;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField rePasswordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField bioTextField;
    @FXML
    private DatePicker birthdayTextField;

    private final RegisterManager registerManager = new RegisterManager();

    public void registerButtonClicked(ActionEvent actionEvent) {
        String fullName = fullNameTextField.getText();
        if (fullName.isEmpty()) {
            fullNameError.setText("you must enter your fullName");
            return;
        }
        fullNameError.setText("");
        String username = usernameTextField.getText();
        if(username.isEmpty()){
            usernameError.setText("you must enter your username");
            return;
        }
        else{
            if(!registerManager.isUsernameAvailable(username)) {
                usernameError.setText("username already exists");
                return;
            }
            usernameError.setText("");
        }
        String password = passwordTextField.getText();
        if(password.isEmpty()){
            passwordError.setText("you must enter your password");
            return;
        }
        passwordError.setText("");
        String rePassword = rePasswordTextField.getText();
        if(rePassword.isEmpty()){
            rePasswordError.setText("you must re-enter your password");
            return;
        }
        else{
            if(!password.equals(rePassword)){
                rePasswordError.setText("passwords don't match");
                return;
            }
            rePasswordError.setText("");
        }
        String email = emailTextField.getText();
        if(email.isEmpty()){
            System.out.println("you must enter your email");
            return;
        }
        else if(!email.contains("@") || !email.contains(".")){
            emailError.setText("invalid email address");
            return;
        }
        else{
            if(!registerManager.isEmailAvailable(email)){
                emailError.setText("email already exists");
                return;
            }
            emailError.setText("");
        }
        String phoneNumber = phoneNumberTextField.getText();
        if(!phoneNumber.isEmpty()) {
            try {
                Integer.parseInt(phoneNumber);
                if (!registerManager.isPhoneNumberAvailable(phoneNumber)){
                    phoneNumberError.setText("phone number already exists");
                    return;
                }
                phoneNumberError.setText("");
            } catch (NumberFormatException e) {
                phoneNumberError.setText("phone number should be only numbers");
                return;
            }
        }
        String bio = bioTextField.getText();
        String birthday = birthdayTextField.getValue() == null ? "" : birthdayTextField.getValue().toString() ;
        registerManager.makeNewUser(fullName,username,password,email,phoneNumber,bio,birthday);
        registerSuccessful.setText("register successful! go back to login");
    }


    public void backButtonClicked(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(ConfigLoader.loadFXML("loginFXMLAddress"))));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            log.error(e.getStackTrace());
            System.out.println(e.getMessage());
        }
    }


}
