package manbu.dev;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Wang Jiacheng.
 * Date: 8/2/17
 * Time: 09:52
 */
public class HistoryLoader {

    private static final Properties properties;

    private static final String filePath = System.getProperty("user.home") + "/.live_editing.properties";

    static {

        properties = new Properties();

        File file = new File(filePath);

        InputStream inputStream = null;

        try {

            if(! file.exists()) {
                if(file.createNewFile())  {
                    System.out.println("Could not create history file: " + filePath);
                }
            }
            inputStream = new FileInputStream(file);

            properties.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static String getSource() {

        return properties.getProperty("history.source", "");
    }

    public static String getDestination() {

        return properties.getProperty("history.destination", "");
    }

    public static void setSource(String value) {

        OutputStream outputStream = null;

        try {

            outputStream = new FileOutputStream(new File(filePath));

            properties.setProperty("history.source", value);

            properties.store(outputStream, "history");

        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            IOUtils.closeQuietly(outputStream);
        }

    }
    public static void setDestination(String value) {

        OutputStream outputStream = null;

        try {

            outputStream = new FileOutputStream(new File(filePath));

            properties.setProperty("history.destination", value);

            properties.store(outputStream, "history");

        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            IOUtils.closeQuietly(outputStream);
        }

    }

}
