package zcore.config;

/**
 *
 * @author lybn
 */
public class DDoSConfig {

    public static int CACHE_CAPACITY = Integer.parseInt(Config.getParam("ddos", "capacity"));
    public static long DOS_EXPIRE = Long.valueOf(Config.getParam("ddos", "expire")) * 1000l;
    public static long CACHE_EXPIRE = Long.valueOf(Config.getParam("ddos", "blockedtime")) * 1000l;
    public static int DOS_MAX_BY_IP = Integer.valueOf(Config.getParam("ddos", "maxbyip"));
    public static int DOS_MAX_BY_JT = Integer.valueOf(Config.getParam("ddos", "maxbyjt"));
    public static String IP_WHITE_LIST = Config.getParam("ddos", "ipwhitelist");
    public static String IS_CHECK_DOS = Config.getParam("ddos", "ischeckdos");
}
