/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.config;

import com.google.gson.Gson;
import mto.billing.config.MbConfigEntity.MbAppConfig;
import mto.billing.config.MbConfigEntity.MbClientConfig;
import mto.billing.config.MbConfigEntity.MbConfig;

/**
 *
 * @author huuloc.tran89
 */
public class MbConfigApiOutput {

    public int returnCode;
    public String returnMessage;

    public static enum ERROR_CODE_API {
        SUCCESS(1, "Thành công"),
        INVALID_DATA_INPUT(-1, "Dữ liệu không hợp lệ"),
        UNSUPPORTED_ERROR(-404, "Tính năng chưa được hỗ trợ"),
        CONFIG_ERROR(-505, "Thông tin đăng kí ứng dụng không chính xác"),
        CLIENT_CONFIG_ERROR(-501, "Thông tin client không tìm thấy"),
        DISPLAY_CONFIG_ERROR(-502, "Thông tin hiển thị không tìm thấy"),
        API_CONFIG_ERROR(-503, "Thông tin api không tìm thấy"),
        ITEM_CONFIG_ERROR(-504, "Thông tin items không tìm thấy"),
        WARNUP_CONFIG_ERROR(-100, "Reload config fail"),
        SERVER_ERROR(-500, "Hệ thống xảy ra lỗi");
        ;

        public int error;
        public String message;

        private ERROR_CODE_API(int error, String message) {
            this.error = error;
            this.message = message;
        }
    }

    public static class GetMbConfigOutput extends MbConfigApiOutput {

        public MbConfig data;

        public String toJString() {
            return new Gson().toJson(this);
        }
    }

    public static class GetMbClientConfigOutput extends MbConfigApiOutput {

        public MbClientConfig data;

        public String toJString() {
            return new Gson().toJson(this);
        }
    }

    public static class GetMbAppConfigOutput extends MbConfigApiOutput {

        public MbAppConfig data;

        public String toJString() {
            return new Gson().toJson(this);
        }
    }

}
