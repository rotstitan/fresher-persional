/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.entity;

import com.google.gson.Gson;
import java.util.HashMap;
import mto.billing.fe.entity.MbDeclaredEntity.MB_LANGUAGE;

/**
 *
 * @author anonymous
 */
public class MbEntity {

    public static class MbLoginInfo {

        //Login
        public MbLoginInfo(String userID, String userName, String loginType, String jtoken) {
            this.userID = userID;
            this.userName = userName;
            this.loginType = loginType;
            this.jtoken = jtoken;
        }

        // Direct 
        public MbLoginInfo(String userID, String userName, String loginType, String serverID, String roleID, String roleName, String jtoken) {
            this.userID = userID;
            this.userName = userName;
            this.loginType = loginType;
            this.serverID = serverID;
            this.roleID = roleID;
            this.roleName = roleName;
            this.jtoken = jtoken;
        }

        // Quick
        public MbLoginInfo(String userID, String loginType, String serverID, String roleID, String roleName, Object info, String jtoken) {
            this.userID = userID;
            this.loginType = loginType;
            this.serverID = serverID;
            this.roleID = roleID;
            this.roleName = roleName;
            this.jtoken = jtoken;
            this.info = info;
        }

        public String userID;
        public String userName;
        public String loginType;
        public String jtoken;
        public String serverID;
        public String roleID;
        public String roleName;
        public Object info;

    }

    public static class MbOrderNumberData {

        public MbOrderNumberData(long orderNumber) {
            this.orderNumber = orderNumber;
            this.orderNumberStr = String.valueOf(orderNumber);
        }

        public long orderNumber;
        public String orderNumberStr;
    }

    public static class MbJtoken {

        public int appID = -1;
        public int clientID = -1;
        public String loginMethod = "";
        public String userID = "";
        public String loginType = "";
        public String roleID = "";
        public String roleName = "";
        public String serverID = "";
        public String userIP = "";

        public MbJtoken(int appID, int clientID, String loginMethod, String roleID) {
            this.appID = appID;
            this.clientID = clientID;
            this.roleID = roleID;
            this.loginMethod = loginMethod;
        }

        public MbJtoken(int appID, int clientID, String loginMethod, String userID, String loginType) {
            this.appID = appID;
            this.clientID = clientID;
            this.userID = userID;
            this.loginType = loginType;
            this.loginMethod = loginMethod;
        }

        public MbJtoken(int appID, int clientID, String loginMethod, String userID, String loginType, String roleID, String roleName, String serverID) {
            this.appID = appID;
            this.clientID = clientID;
            this.userID = userID;
            this.loginType = loginType;
            this.loginMethod = loginMethod;
            this.roleID = roleID;
            this.roleName = roleName;
            this.serverID = serverID;
        }

        public MbJtoken(int appID, int clientID, String loginMethod, String userID, String loginType, String roleID, String roleName, String serverID, String userIP) {
            this.appID = appID;
            this.clientID = clientID;
            this.userID = userID;
            this.loginType = loginType;
            this.loginMethod = loginMethod;
            this.roleID = roleID;
            this.roleName = roleName;
            this.serverID = serverID;
            this.userIP = userIP;
        }

        public String toJString() {
            return new Gson().toJson(this);
        }
    }

    public static class MbErrorMessage {

        public int error;
        public HashMap<MB_LANGUAGE, String> message;

    }

    public static class MbOrderStatusMessage {

        public int error;
        public HashMap<MB_LANGUAGE, String> message;

    }

    public static void main(String[] args) {
        System.out.println(Long.MAX_VALUE);
        System.out.println(Long.MAX_VALUE - 920319355508580352L);
    }
}
