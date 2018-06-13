/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import zcore.utilities.CommonUtil;

/**
 *
 * @author tungdq  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   *
 */
public class MbConfigUtils {

    protected static final Logger logger = Logger.getLogger(MbConfigUtils.class);

    protected static Comparator<NameValuePair> nameValuePairComp = new Comparator<NameValuePair>() {        // solution than making method synchronized
        @Override
        public int compare(NameValuePair p1, NameValuePair p2) {
            return p1.getName().compareTo(p2.getName());
        }
    };

    protected static boolean checkSig(String secretKey, String sig, HttpServletRequest req) {
        try {
            Enumeration<String> params = req.getParameterNames();
            List<NameValuePair> p = new ArrayList<>();

            while (params.hasMoreElements()) {
                String paramName = params.nextElement();
                if (!paramName.equals("sig")) {
                    p.add(new BasicNameValuePair(paramName, req.getParameter(paramName)));
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(secretKey);

            Collections.sort(p, nameValuePairComp);
            p.forEach((nameValuePair) -> {
                sb.append(nameValuePair.getValue());
            });
            logger.info("checkSig: md5(" + sb.toString() + ") : " + CommonUtil.md5(sb.toString()) + " - sig: " + sig);

            if (sig.equals(CommonUtil.md5(sb.toString()))) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    protected static String genSig(String secretKey, List<NameValuePair> params) {
        try {
            params.sort(nameValuePairComp);

            StringBuilder sb = new StringBuilder();
            sb.append(secretKey);
            params.forEach((param) -> {
                sb.append(param.getValue());
            });
            String sig = CommonUtil.md5(sb.toString());
            logger.info("genSig: md5(" + sb.toString() + ") : " + sig);
            return sig;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    protected static String curl(String urlStr, String method, String param) {
        try {
            URL url = new URL(urlStr);
            String proxyHost = System.getProperty("http.proxyHost");
            String proxyPort = System.getProperty("http.proxyPort");

            HttpURLConnection conn = null;
            if (proxyHost != null && proxyPort != null) {
                try {
                    int port = Integer.parseInt(proxyPort);
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    conn = (HttpURLConnection) url.openConnection();
                    logger.error("Proxy " + proxyHost + " didn't works!. " + e.getMessage(), e);
                }
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            conn.setConnectTimeout(15000); // Milliseconds
            conn.setRequestMethod(method);
            conn.setDoOutput(true); // Triggers POST.

            if (method.equals("POST")) {
                try (OutputStream output = conn.getOutputStream()) {
                    output.write(param.getBytes());
                }
            }

            int code = conn.getResponseCode();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getResponseCode() / 100 == 2
                            ? conn.getInputStream() : conn.getErrorStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                if (!line.isEmpty()) {
                    sb.append(line);
                }
            }
            rd.close();

            logger.info("Curl: " + url + " - Param: " + param + " - Code: " + code + " - Result: " + sb.toString());
            return sb.toString();

        } catch (Exception e) {
            logger.error("Error at sendPostRequest. " + urlStr + " - Param: " + param + " - " + e.getMessage(), e);
        }
        return null;
    }

    protected static boolean checkValidParam(String[] params) {
        if (params.length == 0) {
            return true;
        }

        for (String param : params) {
            if (param == null || param.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    protected static boolean isJsonObject(String s) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
