package util;

import java.io.*;
import java.util.Properties;

public class ConfigLoader {
    static Properties prop = new Properties();

    public static String readProperty(String property){
        try {
            InputStream inputStream = new FileInputStream("src/main/resources/configurations/config.properties");
            prop.load(inputStream);
            return (prop.getProperty(property));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "property not found";
    }

    public static void writeProperty(String key , String value,String comment){
        try {
            OutputStream outputStream = new FileOutputStream("src/main/resources/configurations/config.properties");
            prop.setProperty(key, value);
            prop.store(outputStream,comment);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
