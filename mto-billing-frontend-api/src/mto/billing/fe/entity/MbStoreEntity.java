/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.entity;

import com.google.gson.Gson;
import java.util.HashMap;

/**
 *
 * @author huuloc.tran89
 */
public class MbStoreEntity {

    public static final int MAX_CHAR_STORE_STRING_50 = 50;
    public static final int MAX_CHAR_STORE_STRING_255 = 255;
    public static final int MAX_CHAR_STORE_STRING_1024 = 1024;
    public static final int MAX_CHAR_STORE_STRING_65565 = 65565;

    public static class AppServer {

        public AppServer(String serverID, String serverName, int status, Object info) {
            this.serverID = serverID;
            this.serverName = serverName;
            this.info = info;
            this.status = status;
        }

        public String serverID = "";
        public String serverName = "";
        public Object info;
        public int status;
    }

    public static class AppRole {

        public String userID = "";
        public String loginType = "";
        public String roleID = "";
        public String roleName = "";
        public String serverID = "";
        public Object info;

        public AppRole(String roleID, String roleName, Object info) {
            this.roleID = roleID;
            this.roleName = roleName;
            this.info = info;
        }

        public AppRole(String roleID, String roleName, String serverID, Object info) {
            this.roleID = roleID;
            this.roleName = roleName;
            this.serverID = serverID;
            this.info = info;
        }

        public AppRole(String userID, String loginType, String roleID, String roleName, String serverID, Object info) {
            this.roleID = roleID;
            this.roleName = roleName;
            this.serverID = serverID;
            this.userID = userID;
            this.loginType = loginType;
            this.info = info;
        }

        public String toJString() {
            return new Gson().toJson(this);
        }
    }

    public static class AppProduct {

        public String productID = "";
        public String productName = "";
        public String productType = "";
        public String productValue = "";
        public String productBonus = "";
        public String productDescription = "";
        public Object info;
        public boolean enabled = false;
    }

    public static class AppTrans {

        public AppTrans(String appTransID, String appAddInfo) {
            this.appTransID = appTransID;
            this.appAddInfo = appAddInfo;
        }
        public String appTransID = "";
        public String appAddInfo = "";
    }

    public static class AppResult {

        public String appTrans = "";
        public String errorCode = "";
        public String message = "";
    }

    public static class AppBalance {

        public AppBalance(double balance, String currency, String country) {
            this.balance = balance;
            this.currency = (currency != null ? currency.toUpperCase() : currency);
            this.country = country;
        }

        double balance;
        String currency = "";
        String country = "";
    }

    public static class MbStoreApiOutput {

        public int returnCode;
        public String returnMessage;

        public static enum ERROR_CODE_API {
            SUCCESS(1, "Thành công"),
            INVALID_DATA_INPUT(-1, "Dữ liệu không hợp lệ"),
            INVALID_SIGNATURE(-2, "Dữ liệu không chính xác"),
            DATA_NOT_FOUND(-3, "Nhân vật không tìm thấy"),
            SERVER_ERROR(-4, "Hệ thống xảy ra lỗi");

            public int error;
            public String message;

            private ERROR_CODE_API(int error, String message) {
                this.error = error;
                this.message = message;
            }
        }
    }

    public static class MbGetRolesApiOutput extends MbStoreApiOutput {

        public HashMap<String, AppRole> data;

    }

    public static class MbGetServersApiOutput extends MbStoreApiOutput {

        public HashMap<String, AppServer> data;

    }

    public static class MbGetBalancesApiOutput extends MbStoreApiOutput {

        public AppBalance data;

    }

    public static class MbGetProductsApiOutput extends MbStoreApiOutput {

        public HashMap<String, AppProduct> data;

    }

    public static class MbGetTransApiOutput extends MbStoreApiOutput {

        public AppTrans data;

    }

    public static class MbGetResultApiOutput extends MbStoreApiOutput {

        public AppResult data;

    }

}
