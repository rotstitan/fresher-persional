/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.entity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import mto.billing.fe.main.Configuration;
import mto.billing.fe.service.ScribeServiceClient;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpMethod;
import zcore.dos.DDosChecker;
import zcore.tokenpool.TokenPoolManager;
import zcore.utilities.CommonUtil;

/**
 *
 * @author anonymous
 */
public class MbUtils {

    private static final Logger logger = Logger.getLogger(MbUtils.class);
    private static final Pattern pattern = Pattern.compile("^([^?]+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);

    public static Comparator<NameValuePair> nameValuePairComp = new Comparator<NameValuePair>() {
        @Override
        public int compare(NameValuePair p1, NameValuePair p2) {
            return p1.getName().compareTo(p2.getName());
        }
    };

    public static boolean isDdos(String clientIp, String jtoken) {
        if (isDdosIp(clientIp) || isDdosJtoken(jtoken)) {
            return true;
        }
        return false;
    }

    public static boolean isDdosIp(String clientIp) {
        if (StringUtils.isNotBlank(DDosChecker.isDDos(clientIp))) {
            logger.warn("isDdosIp: " + clientIp);
            return true;
        }
        return false;
    }

    public static boolean isDdosJtoken(String jtoken) {
        if (StringUtils.isNotBlank(DDosChecker.isDDosJtoken(jtoken))) {
            logger.warn("isDdosJtoken: " + jtoken);
            return true;
        }
        return false;
    }

    public static void logCurlRequest(String logStr) {
        ScribeServiceClient.getInstance().writeLog2(Configuration.SCRIBE_EXT_REQ_CATE, logStr);
    }

    public static boolean checkSig(String secretKey, String sig, String method, HttpServletRequest req, String[] excludingParam) {
        try {
            Enumeration<String> params = req.getParameterNames();
            List<NameValuePair> p = new ArrayList<>();
            
            ArrayList<String> exclude = new ArrayList<>(Arrays.asList(excludingParam));
            while (params.hasMoreElements()) {
                String paramName = params.nextElement();
                if (!exclude.contains(paramName)) {
                    p.add(new BasicNameValuePair(paramName, req.getParameter(paramName)));
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(secretKey);

            Collections.sort(p, nameValuePairComp);
            for (NameValuePair nameValuePair : p) {
                sb.append(nameValuePair.getValue());
            }
            logger.info("checkSig: md5(" + sb.toString() + ") : " + CommonUtil.md5(sb.toString()) + " - sig: " + sig);

            if (sig.equals(CommonUtil.md5(sb.toString()))) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    public static boolean checkSig(String secretKey, String sig, String method, HttpServletRequest req) {
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
            for (NameValuePair nameValuePair : p) {
                sb.append(nameValuePair.getValue());
            }
            logger.info("checkSig: md5(" + sb.toString() + ") : " + CommonUtil.md5(sb.toString()) + " - sig: " + sig);

            if (sig.equals(CommonUtil.md5(sb.toString()))) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    public static String genSig(String secretKey, List<NameValuePair> params) {
        try {
            params.sort(nameValuePairComp);

            StringBuilder sb = new StringBuilder();
            sb.append(secretKey);
            for (NameValuePair param : params) {
                sb.append(param.getValue());
            }
            String sig = CommonUtil.md5(sb.toString());
            logger.info("genSig: md5(" + sb.toString() + ") : " + sig);
            return sig;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static String curl(String urlStr, String appID, HttpMethod method, String param, ContentType contentType) {
        String urlAnalized = CommonUtil.getAnalyzeUrl(urlStr);
//        boolean token = TokenPoolManager.getToken(urlAnalized, appID);
        try {
//            if (!token) {
//                logger.error("Rejected: " + urlAnalized + " | method: " + method.name());
//                return null;
//            }

            // Start function
            try {
                long startTime = System.nanoTime();
                URL url = new URL(urlStr);
                String proxyHost = System.getProperty("http.proxyHost");
                String proxyPort = System.getProperty("http.proxyPort");

                HttpURLConnection conn;
                if (proxyHost != null && proxyPort != null) {
                    try {
                        int port = Integer.parseInt(proxyPort);
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        logger.error("Proxy " + proxyHost + " didn't works!. " + e.getMessage(), e);
                        conn = (HttpURLConnection) url.openConnection();
                    }
                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }

                conn.setRequestProperty("Content-Type", contentType.getMimeType());
                conn.setConnectTimeout(15000); // Milliseconds
                conn.setRequestMethod(method.name());
                conn.setDoOutput(true); // Triggers POST.

                if (HttpMethod.POST == method) {
                    try (OutputStream output = conn.getOutputStream()) {
                        output.write(param.getBytes());
                    }
                }

                int code = conn.getResponseCode();

                BufferedReader br = null;
                if (code / 100 == 2) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    InputStream errorStream = conn.getErrorStream();
                    if (errorStream != null) {
                        br = new BufferedReader(new InputStreamReader(errorStream));
                    }
                }

                StringBuilder sb = new StringBuilder();
                if (br != null) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.isEmpty()) {
                            sb.append(line);
                        }
                    }
                    br.close();
                }

                logger.info("Curl: " + url + " - Param: " + param + " - Code: " + code + " - Result: " + sb.toString());
                logCurlRequest("Curl: " + url + " - Param: " + param + " - Code: " + code + " - Result: " + sb.toString());
                return sb.toString();

            } catch (Exception e) {
                logger.error("Curl: " + urlStr + " - Param: " + param + " - " + e.getMessage(), e);
            }
            return null;
        } finally {
//            if (token) {
//                TokenPoolManager.returnToken(urlAnalized);
//            }
        }
    }

}
