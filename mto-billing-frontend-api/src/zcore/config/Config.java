package zcore.config;

import java.io.File;
import java.util.Iterator;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

public class Config {

    public static final String CONFIG_HOME = "conf";
    public static final String CONFIG_FILE = "config.ini";
    static CompositeConfiguration config;

    public static String getHomePath() {
        return System.getProperty("apppath");
    }

    public static Integer getIntParam(String section, String name) throws Exception {
        String sval = getParam(section, name);
        if (sval == null) {
            throw new Exception("params " + name + " not found in section " + section);
        }
        return Integer.valueOf(sval);
    }

    public static Long getLongParam(String section, String name) throws Exception {
        String sval = getParam(section, name);
        if (sval == null) {
            throw new Exception("params " + name + " not found in section " + section);
        }
        return Long.valueOf(sval);
    }

    public static String getParam(String section, String name) {
        String key = section + "." + name;
        String value = config.getString(key);
        return value;
    }

    static {
        String CONFIG_ITEMS = System.getProperty("cfg_items");
        String HOME_PATH = System.getProperty("apppath");
        String APP_ENV = System.getProperty("appenv");
        if ((CONFIG_ITEMS == null) || (CONFIG_ITEMS.equals(""))) {
            CONFIG_ITEMS = "500";
        }
        if (APP_ENV == null) {
            APP_ENV = "";
        }
        if (APP_ENV != "") {
            APP_ENV = APP_ENV + ".";
        }

        config = new CompositeConfiguration();

        File configFile = new File(HOME_PATH + File.separator + "conf" + File.separator + APP_ENV + "config.ini");
        try {
            config.addConfiguration(new HierarchicalINIConfiguration(configFile));

            Iterator ii = config.getKeys();

            while (ii.hasNext()) {
                String key = (String) ii.next();
            }
        } catch (ConfigurationException e) {
            System.out.println("Exception when Config");
            System.exit(1);
        }
    }
}
