package gui.controllers.popups;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class ConfirmBox {

    static boolean answer;

    public static boolean display(String title , String message){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Image icon = new Image(classloader.getResourceAsStream("Images/warning.png"));

//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,message);
//        alert.show();


        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);
        window.setMinWidth(300);
        window.getIcons().add(icon);
        Label label = new Label();
        label.setText(message);

        //create two buttons
        Button yesButton  = new Button("Yes");
        Button noButton  = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,yesButton,noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

}
