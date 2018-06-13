/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.ext.passport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author anonymous
 */
public class PassportUtils {

    protected static final Logger logger = Logger.getLogger(PassportUtils.class);

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

    protected static boolean checkSigKey(String secretKey, String sigKey, Object[] params) {
        StringBuilder s = new StringBuilder();
        for (Object param : params) {
            s = s.append(param);
        }
        return sigKey.equals(md5(secretKey + s.toString()));
    }

    protected static String md5(String input) {
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

    protected static boolean checkValidParam(HttpServletRequest request, String[] params) {
        if (request == null || params == null) {
            return false;
        }

        if (params.length == 0) {
            return true;
        }

        List<String> listParam = new LinkedList<String>();
        Enumeration<String> enumParams = request.getParameterNames();
        while (enumParams.hasMoreElements()) {
            String paramName = enumParams.nextElement();
            if (request.getParameter(paramName) != null && !request.getParameter(paramName).isEmpty()) {
                listParam.add(paramName);
            }
        }

        for (int i = 0; i < params.length; i++) {
            if (!listParam.contains(params[i])) {
                return false;
            }
        }
        return true;
    }
}
