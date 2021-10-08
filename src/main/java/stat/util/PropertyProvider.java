package stat.util;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyProvider {
    Properties prop;

    public PropertyProvider() {
        try {
            String filePath = System.getProperty("user.dir") + "//src//main//resources//application.properties";
            this.prop = new Properties();
            FileInputStream fs = new FileInputStream(filePath);
            prop.load(fs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Properties getProp() {
        return this.prop;
    }
}
