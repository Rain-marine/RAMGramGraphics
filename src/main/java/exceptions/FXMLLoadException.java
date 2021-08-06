package exceptions;

import controllers.FactionsController;
import gui.controllers.popups.AlertBox;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class FXMLLoadException extends IOException {
    private final static Logger log = LogManager.getLogger(FXMLLoadException.class);

    String message;
    public FXMLLoadException(String fxmlTitle) {
        super("404: " + fxmlTitle);
        log.error("fxml not found: " + fxmlTitle);
        this.message = "the FXML file for " + fxmlTitle + "is missing";
        AlertBox.display("fxml missing" , message);
    }
}
