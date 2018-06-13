package zcore.dos;

import com.vng.jcore.cache.lruexpire.LruExpireCache;
import zcore.config.DDoSConfig;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zcore.utilities.CommonUtil;

/**
 *
 * @author lybn
 */
public class DDosChecker {

    public static final LruExpireCache<String, DOSInfo> storage;
    private static final Logger logger = LoggerFactory.getLogger(DDosChecker.class);

    static {
        storage = new LruExpireCache<>(DDoSConfig.CACHE_CAPACITY);
    }

    /*
     Config DOS_MAX_BY_IP phải lớn, vì phòng máy có nhiều máy con kết nối với 
     một máy chủ, đi ra internet chung một IP.     
     */
    public static String isDDos(String clientIP) {
        String isCheckDos = DDoSConfig.IS_CHECK_DOS;
        if ("true".equals(isCheckDos)) {
            if (DDoSConfig.IP_WHITE_LIST.contains(clientIP)) {
                return "";
            }
            String dosIP = isDos_str(clientIP, DDoSConfig.DOS_MAX_BY_IP, DDoSConfig.CACHE_EXPIRE);
            return dosIP;
        }
        return "";
    }

    public static String isDDosJtoken(String clientIP) {
        String isCheckDos = DDoSConfig.IS_CHECK_DOS;
        if ("true".equals(isCheckDos)) {
            if (DDoSConfig.IP_WHITE_LIST.contains(clientIP)) {
                return "";
            }
            String dosIP = isDos_str(clientIP, DDoSConfig.DOS_MAX_BY_JT, DDoSConfig.CACHE_EXPIRE);
            return dosIP;
        }
        return "";
    }

    public static String isDDos(HttpServletRequest req) {
        String clientIP = CommonUtil.getClientIp(req);
        return isDDos(clientIP);
    }

    private static String isDos_str(String key, int maxTime, long expire) {
        try {
            if (StringUtils.isBlank(key)) {
                return "";
            }
            synchronized (key) {
                long curTime = System.currentTimeMillis();

                // The first time
                DOSInfo dosInfo = (DOSInfo) storage.get(key);
                if (dosInfo == null) {
                    dosInfo = new DOSInfo(1, curTime);
                    storage.put(key, dosInfo, expire);
                    return "";
                }

                // Check dos
                if (dosInfo.getCounter() >= maxTime) {
                    return ("DOS:" + key + ":" + dosInfo.getCounter() + ":" + maxTime + ":" + DDoSConfig.DOS_EXPIRE);
                }

                // In the new block time
                if (curTime - dosInfo.getFirstTime() > DDoSConfig.DOS_EXPIRE) {
                    dosInfo.setCounter(1);
                    dosInfo.setFirstTime(curTime);
                    storage.put(key, dosInfo, expire);
                    return "";
                }

                // In the current block time
                dosInfo.increaseCounter();
                storage.put(key, dosInfo, expire);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
}
