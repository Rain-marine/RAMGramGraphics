package gui.controllers.popups;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean answer;

    public static boolean display(String title , String message){
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to log out?");
//        alert.show();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);
        window.setMinWidth(300);
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
