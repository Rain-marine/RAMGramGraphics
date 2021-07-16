package gui.controllers;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.ConfigLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.naming.SizeLimitExceededException;

public class ImageController {

    public static byte[] pickImage() throws SizeLimitExceededException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile.length()>2000000){ //2 MB
            throw new SizeLimitExceededException();
        }
        byte[] byteArray = null;
        if (selectedFile != null) {
            try {
                byteArray = Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }


    public static Image byteArrayToImage(byte[] byteArray) {
        Image image;
        if (byteArray != null) {
            image = new Image(new ByteArrayInputStream(byteArray));
            return image;
        } else {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            image = new Image(classloader.getResourceAsStream(ConfigLoader.readProperty("defaultPicAdd")));
        }
        return image;
    }


}
