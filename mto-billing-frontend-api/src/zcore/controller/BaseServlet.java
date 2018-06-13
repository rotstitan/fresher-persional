/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.controller;

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import zcore.utilities.CommonUtil;

/**
 *
 * @author tungdq
 */
public class BaseServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(BaseServlet.class);

    public BaseServlet() {
        super();
    }

    protected void out(String content, HttpServletRequest req, HttpServletResponse resp, String type) {
        try {
            resp.setCharacterEncoding("utf-8");
            if ("json".equalsIgnoreCase(type)) {
                resp.addHeader("Content-Type", "application/json; charset=utf-8");
            } else if ("plain".equalsIgnoreCase(type)) {
                resp.addHeader("Content-Type", "text/plain; charset=utf-8");
            } else {
                resp.addHeader("Content-Type", "text/html; charset=utf-8");
            }

            try (PrintWriter os = resp.getWriter()) {
                os.write(content);
                logRequestWithResponse(req, resp, content);
            }
        } catch (Exception ex) {
            logger.error("BaseServlet.out", ex);
        }
    }

    private JSONObject requestParamsToJSON(HttpServletRequest req) {
        try {
            JSONObject jsonObj = new JSONObject();
            Map<String, String[]> params = req.getParameterMap();
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                String v[] = entry.getValue();
                Object o = (v.length == 1) ? v[0] : v;
                jsonObj.put(entry.getKey(), o);
            }
            return jsonObj;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    private void logRequestWithResponse(HttpServletRequest request, HttpServletResponse response, String content) {
        JSONObject log = new JSONObject();
        JSONObject req = new JSONObject();
        req.put("URI", request.getRequestURI());
        req.put("Query", request
                .getQueryString() != null ? "?" + request.getQueryString().replace("\\", "") : "");

        JSONObject jsonReqMap = requestParamsToJSON(request);
        if (jsonReqMap != null) {
            if (jsonReqMap.get("p") != null) {
                jsonReqMap.put("p", CommonUtil.md5(jsonReqMap.get("p").toString()));
            }
            req.put("Param", jsonReqMap);
        }
        req.put("IP", request.getRemoteAddr());
        req.put("ForwardedIP", request.getHeader("X-FORWARDED-FOR"));
        req.put("Agent", request.getHeader("User-Agent") != null ? request.getHeader("User-Agent").replace("\\", "") : "");

        log.put("ts", System.currentTimeMillis());
        log.put("request", req);
        log.put("response", response.toString().replace("\n", " ||| ") + content);
        logger.info("logRequest: " + log.toString());
    }

}
