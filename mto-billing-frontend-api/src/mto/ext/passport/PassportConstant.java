/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.ext.passport;

import com.google.gson.Gson;

/**
 *
 * @author anonymous
 */
public class PassportConstant {

    public static final String PP_LOGIN_GUEST_LOGIN_TYPE = "10";
    public static final String PP_LOGIN_ZING_LOGIN_TYPE = "11";
    public static final String PP_LOGIN_ZALO_LOGIN_TYPE = "12";
    public static final String PP_LOGIN_GOOGLE_LOGIN_TYPE = "13";
    public static final String PP_LOGIN_FACEBOOK_LOGIN_TYPE = "14";
    public static final String PP_LOGIN_ZINGME_LOGIN_TYPE = "15";

    public static class CallbackData {

        public PassportOutput.ERROR_CODE_API error;
        public String gameID;
        public String session;
        public String userID;
        public String loginType;
        public String userName;
        public String userAgent;

        public CallbackData(PassportOutput.ERROR_CODE_API error, String gameID, String session, String userID, String loginType, String userName, String userAgent) {
            this.error = error;
            this.gameID = gameID;
            this.session = session;
            this.userID = userID;
            this.loginType = loginType;
            this.userName = userName;
            this.userAgent = userAgent;
        }

        public CallbackData(PassportOutput.ERROR_CODE_API error) {
            this.error = error;
        }

        public String toJString() {
            return new Gson().toJson(this);
        }
    }
}
