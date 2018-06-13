/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.service;

import mto.billing.fe.main.Configuration;

/**
 *
 * @author huuloc.tran89
 */
public class LogService {

    public static void logRequest(String logStr) {
        ScribeServiceClient.getInstance().writeLog2(Configuration.SCRIBE_REQ_CATE, logStr);
    }

    public static void logState(String logStr) {
        ScribeServiceClient.getInstance().writeLog2(Configuration.SCRIBE_DB_CATE, logStr);
    }

    private static final String LOG_FORMAT = "%s\t%s\t%s\t%s\t%s\t%s";

    public static String getLogFormat(String method, String state, String resultcode, String resultMessage, String params) {

        int time = (int) (System.currentTimeMillis() / 1000);
        String message = String.format(LOG_FORMAT, method, time, state, resultcode, resultMessage, params);
        return message;
    }
}
