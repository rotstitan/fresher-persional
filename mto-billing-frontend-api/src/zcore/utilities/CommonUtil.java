/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.utilities;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author locth2
 */
public class CommonUtil {

    private static Logger logger = Logger.getLogger(CommonUtil.class);

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static JSONObject parseJSONObject(String value) {
        JSONObject ret = null;
        try {
            JSONParser parser = new JSONParser();
            ret = (JSONObject) parser.parse(value);
        } catch (ParseException e) {
            logger.warn("Error at parseJSONObject. " + e.getMessage() + " : " + value);
        }
        return ret;
    }

    public static String getMontlyStringOfOrder(long orderNumber) {
        try {
            long time = SnowflakeIdGenerator.extractTimestamp64(orderNumber);
            java.util.Date date = new java.util.Date(time);
            DateFormat dbTableFormat = new SimpleDateFormat("yyyyMM");
            return dbTableFormat.format(date);
        } catch (Exception e) {
            logger.error("Exception at getMontlyStringOfOrder method. " + e.getMessage(), e);
            return null;
        }
    }
}
