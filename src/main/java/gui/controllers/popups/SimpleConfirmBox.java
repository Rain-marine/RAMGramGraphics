package gui.controllers.popups;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SimpleConfirmBox implements ConfirmBox {
    static boolean answer;

    public static boolean display(String title , String message){
        Stage window = new Stage();
        Label lable = new Label();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);
        window.setMinWidth(300);
        window.getIcons().add(icon);
        lable.setText(message);

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
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(yesButton,noButton);
        hBox.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(lable,hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }


}
