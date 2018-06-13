/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.ext.passport;

import com.google.common.base.Strings;
import mto.ext.passport.PassportConstant.CallbackData;
import mto.ext.passport.PassportOutput.ERROR_CODE_API;
import static mto.ext.passport.PassportUtils.checkValidParam;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

/**
 *
 * @author anonymous
 */
public class PassportService {

    private final Logger logger = Logger.getLogger(PassportService.class);
    private static final Lock LOCK = new ReentrantLock();
    private static final Map<String, PassportService> _instances = new NonBlockingHashMap<String, PassportService>();

    private final String domain;
    private final String webLoginUrl = "/oauth/%s?gameID=%s&mapID=%s&timestamp=1527844562196&sig=9ZH1JxXQSlDplzBuRMuDmVZJl66nKB";

    private PassportService(String domain) {
        this.domain = domain;
    }

    public static PassportService getInstance(String domain) {

        String key = new StringBuilder().append(domain).toString();

        if (!_instances.containsKey(key)) {
            LOCK.lock();
            try {
                if (_instances.get(key) == null) {
                    _instances.put(key, new PassportService(domain));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return _instances.get(key);

    }

    public CallbackData verifyCallbackData(HttpServletRequest req, String secretKey, String callbackSecretKey) {
        try {
            if (!checkValidParam(req, new String[]{"success", "gameID", "session", "userID", "loginType", "ts", "sig", "userName"})) {
                return new CallbackData(ERROR_CODE_API.WRONG_DATA);
            }

            Integer success = (req.getParameter("success") == null) ? -1 : Integer.parseInt(req.getParameter("success"));
            String gameID = (req.getParameter("gameID") == null) ? "" : req.getParameter("gameID");
            String session = (req.getParameter("session") == null) ? "" : req.getParameter("session");
            String userID = (req.getParameter("userID") == null) ? "" : req.getParameter("userID");
            String loginType = (req.getParameter("loginType") == null) ? "" : req.getParameter("loginType");
            String ts = (req.getParameter("ts") == null) ? "" : req.getParameter("ts");
            String sig = (req.getParameter("sig") == null) ? "" : req.getParameter("sig");
            String userName = (req.getParameter("userName") == null) ? "" : req.getParameter("userName");
            String userAgent = req.getHeader("User-Agent");

            if (!PassportUtils.checkSigKey(callbackSecretKey, sig, new Object[]{success, gameID, session, userID, loginType, ts})) {
                return new CallbackData(ERROR_CODE_API.WRONG_SIGNATURE);
            }
            for (ERROR_CODE_API value : ERROR_CODE_API.values()) {
                if (value.error == success) {
                    return new CallbackData(value, gameID, session, userID, loginType, userName, userAgent);
//                    if (checkSession(gameID, secretKey, session, userID, userAgent)) {
//                        return new CallbackData(value, gameID, session, userID, loginType, userName, userAgent);
//                    } else {
//                        return new CallbackData(ERROR_CODE_API.SESSION_INVALID);
//                    }
                } else {
                    return new CallbackData(value);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;

    }

    private boolean checkSession(String appID, String secretKey, String session, String userID, String userAgent) {
        boolean isLogin = false;

        logger.info(String.format("SessionModelApi.isLogin: sessionID=%s, userID=%s, userAgent=%s", session, userID, userAgent));

        if (!Strings.isNullOrEmpty(userID)
                && !Strings.isNullOrEmpty(session)
                && !Strings.isNullOrEmpty(userAgent)) {
            String url = domain + "/login/session/check";
            String timestamp = System.currentTimeMillis() + "";
            String sig = PassportUtils.md5(secretKey + appID + userID + session + userAgent + timestamp);

            List<NameValuePair> params = new ArrayList();

            params.add(new BasicNameValuePair("gameID", appID));
            params.add(new BasicNameValuePair("timestamp", timestamp));
            params.add(new BasicNameValuePair("sig", sig));
            params.add(new BasicNameValuePair("sessionID", session));
            params.add(new BasicNameValuePair("userAgent", userAgent));
            params.add(new BasicNameValuePair("userID", userID));

            String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);

            String response;
            JSONObject rp;
            int returnCode = -1;
            String message = "";

            try {
                response = PassportUtils.curl(url + "?" + query, "GET", "");

                rp = new JSONObject(response);
                returnCode = rp.getInt("returnCode");
                message = rp.getString("message");

                if (returnCode == 0) {
                    isLogin = true;
                }

            } catch (Exception ex) {
                logger.error("Exception at SessionModelApi.logout: " + ex.getMessage(), ex);
            }
        }
        return isLogin;
    }

    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        try {

        } catch (Exception ex) {
            logger.error("Exception at SessionModelApi.logout: " + ex.getMessage(), ex);
        }
    }

    public HashMap<String, String> genUrl(Set<String> channels, String appID, String mapID) {
        HashMap<String, String> rs = new HashMap<>();
        try {
            channels.forEach((channel) -> {
                rs.put(channel, String.format(domain + webLoginUrl, channel, appID, mapID));
            });
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return rs;
    }

    public static void main(String[] args) {
        ERROR_CODE_API[] values = PassportOutput.ERROR_CODE_API.values();
        for (ERROR_CODE_API value : values) {
            System.out.println(value);
        }
        System.out.println(PassportOutput.ERROR_CODE_API.SUCCESS.name());
    }

}
