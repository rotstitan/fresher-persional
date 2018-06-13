/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.utilities;

import zcore.config.Config;
import java.util.Calendar;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import zcore.utilities.CommonUtil;
import org.apache.log4j.Logger;

public class CookieUtil {

    private static Logger logger = Logger.getLogger(CookieUtil.class);

    public static void clearCookie(String name, String path, String cookieDomain, HttpServletRequest req, HttpServletResponse resp) {
        try {
            CookieUtil.setCookie(name, "deleted", 0, false, path, cookieDomain, req, resp);
        } catch (Exception e) {
            logger.error("Exception at clearCookie " + e.getMessage(), e);
        }
    }

    public static Cookie getCookie(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static void setCookie(String cookieName, String value, int expire, boolean httponly, String path, String domain, HttpServletRequest req, HttpServletResponse resp) {
        try {
            String strExpire = "";
            if (expire == 0) { // expire now
                String ex = "Thu, 01 Jan 1970 00:00:01 GMT";
                strExpire = ";Expires=" + ex;
            } else if (expire > 0) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MILLISECOND, expire * 1000);
                String ex = CommonUtil.formatDate(cal.getTime(), "EEE, dd-MMM-yyyy HH:mm:ss zzz");
                strExpire = ";Expires=" + ex;
            }
            // else expire < -1: expires after browser is closed.

            String strHttponly = "";
            if (httponly == true) {
                strHttponly = ";HttpOnly";
            }

            String headerValue = cookieName + "=" + value + ";Path=" + path + ";Domain=" + domain + strExpire + strHttponly;
            resp.setHeader("P3P", "CP=\"NOI ADM DEV PSAi COM NAV OUR OTRo STP IND DEM\"");
            resp.setHeader("Vary", "Accept-Encoding");
            resp.addHeader("Set-Cookie", headerValue);
        } catch (Exception e) {
            logger.error("Exception at setCookie " + e.getMessage(), e);
        }
    }

    public static void setCookie(String cookieName, String value, int expire, boolean httponly, String cookieDomain, HttpServletRequest req, HttpServletResponse resp) {
        setCookie(cookieName, value, expire, httponly, "/", cookieDomain, req, resp);
    }

    public static void setCookie(String cookieName, String value, int expire, String cookieDomain, HttpServletRequest req, HttpServletResponse resp) {
        setCookie(cookieName, value, expire, false, "/", cookieDomain, req, resp);
    }

    public static void setCookie(String cookieName, String value, String cookieDomain, HttpServletRequest req, HttpServletResponse resp) {
        setCookie(cookieName, value, -1, false, "/", cookieDomain, req, resp); // default is end of session
    }

    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        if (cookies == null) {
            return (defaultValue);
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) {
                return (cookie.getValue());
            }
        }
        return (defaultValue);
    }

    public static String getCookieValue(HttpServletRequest req, String cookieName, String defaultValue) {
        Cookie[] cookies = req.getCookies();
        return getCookieValue(cookies, cookieName, defaultValue);
    }
}
