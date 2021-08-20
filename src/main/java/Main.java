import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import view.StartUp;


public class Main extends Application {
    static Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
       log.info("Application Started");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new StartUp().onlineInitialize(primaryStage);
    }
}
