/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.tokenpool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author tudhm
 */
public class TokenPoolManager {

    private static final Logger logger = Logger.getLogger(TokenPoolManager.class);

    private static final Map<String, TokenPool> pools;
    private static final int TOKENPOOL_DEFAULT_THREAD = 10;

    static {
        pools = new ConcurrentHashMap<>();

    }

    public static boolean getToken(String type, String appID) {
        if (type == null) {
            return true;
        }

        TokenPool tp = pools.get(type);
        if (tp == null) {
            tp = new TokenPool(type, appID, TOKENPOOL_DEFAULT_THREAD);
            pools.put(type, tp);
        }

        boolean token = tp.getToken();
        if (!token) {
            logger.error("TokenPoolManager | getToken: Can not get tokenpool");
        }
        logger.info("TokenPoolManager | getToken: get tokenpool successfully!");

        return token;
    }

    public static boolean getToken(String type, String appID, Integer numToken) {
        if (type == null) {
            return true;
        }

        TokenPool tp = pools.get(type);
        if (tp == null) {
            tp = new TokenPool(type, appID, numToken);
            pools.put(type, tp);
        }

        boolean token = tp.getToken();
        if (!token) {
            logger.error("TokenPoolManager | getToken: Can not get tokenpool");
        }
        logger.info("TokenPoolManager | getToken: get tokenpool successfully!");

        return token;
    }

    public static void returnToken(String type) {
        if (type == null) {
            return;
        }
        pools.get(type).returnToken();
    }

    public static TokenPool getTokenPool(String type) {
        if (type == null) {
            return null;
        }

        return pools.get(type);
    }
}
