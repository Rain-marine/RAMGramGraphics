package util;

import exceptions.FXMLLoadException;

import java.io.*;
import java.util.Properties;

public class ConfigLoader {
    static Properties prop = new Properties();

    private static String readConfigURL(String configFile) {
        try{
            InputStream inputStream = new FileInputStream("src/main/resources/configurations/configurations.properties");
            prop.load(inputStream);
            return (prop.getProperty(configFile));}
        catch (IOException e){
            System.err.println("config file for " + configFile + " is missing");
        }
        return null;
    }

    public static String loadFXML(String fxml){
        String fxmlConfigFilePath = readConfigURL("FXMLUrlsConfig");
        try {
            assert fxmlConfigFilePath != null;
            InputStream inputStream = new FileInputStream(fxmlConfigFilePath);
            prop.load(inputStream);
            return (prop.getProperty(fxml));
        }
        catch (IOException e){
            new FXMLLoadException(fxml);
        }
        return null;
    }

    public static int getPreviousMenuCode(String menuName){
        String fxmlConfigFilePath = readConfigURL("previousMenuConfig");
        try {
            assert fxmlConfigFilePath != null;
            InputStream inputStream = new FileInputStream(fxmlConfigFilePath);
            prop.load(inputStream);
            return (Integer.parseInt(prop.getProperty(menuName)));
        }
        catch (IOException e){
            System.err.println("previous menus codes are missing");
        }
        return 2;
    }

    public static String readProperty(String property){
        String graphicalConfigFilePath = readConfigURL("graphicConfig");
        try {
            assert graphicalConfigFilePath != null;
            InputStream inputStream = new FileInputStream(graphicalConfigFilePath);
            prop.load(inputStream);
            return (prop.getProperty(property));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "404 : property not found";
    }


    public static void writeProperty(String key , String value,String comment){
        try {
            OutputStream outputStream = new FileOutputStream("src/main/resources/configurations/configurations.properties");
            prop.setProperty(key, value);
            prop.store(outputStream,comment);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
