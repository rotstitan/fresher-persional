/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.ext.passport;

/**
 *
 * @author anonymous
 */
public class PassportOutput {

    public static final int PP_LOGIN_RETURN_CODE_OK = 0;
    public static final int PP_LOGIN_RETURN_CODE_SERVER_ERROR = 500;
    public static final int PP_LOGIN_RETURN_CODE_ACTION_NOTFOUND = 404;
    public static final int PP_LOGIN_RETURN_CODE_WRONG_SIGNATURE = 1;
    public static final int PP_LOGIN_RETURN_CODE_WRONG_DATA = 2;
    public static final int PP_LOGIN_RETURN_CODE_BACK = 100;
    public static final int PP_LOGIN_RETURN_CODE_NO_PERMISSION = 101;
    public static final int PP_LOGIN_RETURN_CODE_EXISTED_EMAIL = 102;
    public static final int PP_LOGIN_RETURN_CODE_NONEXISTED_EMAIL = 103;
    public static final int PP_LOGIN_RETURN_CODE_USER_PROTECTED = 104;
    public static final int PP_LOGIN_RETURN_CODE_NONEXISTED_USER = 105;
    public static final int PP_LOGIN_RETURN_CODE_INVALID_AUTH = 106;
    public static final int PP_LOGIN_RETURN_CODE_ERROR_OAUTH_CODE = 107;
    public static final int PP_LOGIN_RETURN_CODE_WRONG_GAME_SESSION = 108;
    public static final int PP_LOGIN_RETURN_CODE_LOGIN_CHANEL_NOTFOUND = 109;
    public static final int PP_LOGIN_RETURN_CODE_ERROR = 199;

    public static enum ERROR_CODE_API {
        SUCCESS(0, "Thành công"),
        WRONG_SIGNATURE(1, "Signature không chính xác"),
        WRONG_DATA(1, "Dữ liệu không đầy đủ"),
        NO_PERMISSION(101, "Ứng dụng không được cấp quyền"),
        NON_EXISTED_USER(105, "Tài khoản chưa tham gia game"),
        SOCIAL_AUTHEN_ERROR(199, "Đăng nhập kênh xã hội lỗi"),
        SESSION_INVALID(198, "Session không chính xác hoặc đã hết hạn"),
        SERVER_ERROR(199, "Đăng nhập kênh xã hội lỗi");

        public int error;
        public String message;

        private ERROR_CODE_API(int error, String message) {
            this.error = error;
            this.message = message;
        }

    }

}
