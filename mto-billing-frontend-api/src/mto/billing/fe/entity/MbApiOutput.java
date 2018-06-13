/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.entity;

import javax.servlet.http.HttpServletRequest;
import zcore.controller.ApiOutput;
import static mto.billing.fe.main.Configuration.MB_API_ERROR_MESSAGE;
import org.apache.log4j.Logger;

/**
 *
 * @author anonymous
 */
public class MbApiOutput extends ApiOutput {

    private static final Logger logger = Logger.getLogger(MbApiOutput.class);

    public static enum ERROR_CODE_API {
        SUCCESS,
        PAYMENT_IN_PROGRESS,
        INVALID_DATA_INPUT,
        INVALID_SIGNATURE,
        INVALID_TOKEN,
        INVALID_COUNTRY_CURRENCY,
        INVALID_LANGUAGE,
        // PASSPORT 
        PASSPORT_AUTHEN_ERROR,
        // STORE 
        APP_CONFIG_ERROR,
        STORE_GET_ROLE_ERROR,
        STORE_GET_ROLE_NOT_SUPPORT_ERROR,
        STORE_GET_SERVER_ERROR,
        STORE_GET_SERVER_NOT_SUPPORT_ERROR,
        STORE_GET_BALANCE_ERROR,
        STORE_GET_BALANCE_NOT_SUPPORT_ERROR,
        STORE_GET_PRODUCTS_ERROR,
        STORE_GET_PRODUCTS_NOT_SUPPORT_ERROR,
        STORE_GET_TRANS_ERROR,
        STORE_GET_PAY_WALLET_ERROR,
        STORE_GET_PAY_WALLET_NOT_SUPPORT_ERROR,
        // PAYMENT
        PAYMENT_GET_APPINFO_ERROR,
        PAYMENT_PAY_CARD_ERROR,
        PAYMENT_PAY_ZALOPAY_ERROR,
        PAYMENT_PAY_SMS_ERROR,
        PAYMENT_PAY_BANK_ERROR,
        PAYMENT_PAYWALLET_ERROR,
        PAYMENT_VERIFY_SMSOTP_ERROR,
        // Fulfillment 
        FULFILLMENT_GET_RESULT_ERROR,
        FULFILLMENT_ORDER_NOT_FOUND_ERROR,
        FULFILLMENT_CREATE_ORDER_FAILED,
        // Frontend 
        GET_ORDER_STATUS_ERROR,
        // DEFAULT ERROR  
        DDOS_DETECTION_ERROR,
        UNSUPPORTED_ERROR,
        CONFIG_ERROR,
        SERVER_ERROR;

        private int error;
        private String message;

        public int getError() {
            MbEntity.MbErrorMessage errorMessage = MB_API_ERROR_MESSAGE.get(this);
            if (errorMessage == null) {
                logger.error("MbApiOutput cannot get defined error code: " + this);
                return MB_API_ERROR_MESSAGE.get(SERVER_ERROR).error;
            }
            return errorMessage.error;
        }

        public String getMessage(String lang) {
            MbEntity.MbErrorMessage errorMessage = MB_API_ERROR_MESSAGE.get(this);
            if (errorMessage == null || errorMessage.message.get(MbDeclaredEntity.MB_LANGUAGE.getByID(lang)) == null)  {
                logger.error("MbApiOutput cannot get defined error message: " + this);
                return MB_API_ERROR_MESSAGE.get(SERVER_ERROR).message.get(MbDeclaredEntity.MB_LANGUAGE.getByID(lang));
            }
            return errorMessage.message.get(MbDeclaredEntity.MB_LANGUAGE.getByID(lang));
        }
    }

    public MbApiOutput(int error, String message) {
        super(error, message, null);
    }

    public MbApiOutput(ERROR_CODE_API returnCode, HttpServletRequest req, Object data) {
        super(returnCode.getError(), returnCode.getMessage(req.getParameter("lang")), data);
        this.returnMessage_ = returnCode.name();
    }

    public MbApiOutput(ERROR_CODE_API returnCode, HttpServletRequest req) {
        super(returnCode.getError(), returnCode.getMessage(req.getParameter("lang")), null);
        this.returnMessage_ = returnCode.name();
    }

//    public MbApiOutput(ERROR_CODE_API returnCode) {
//        super(returnCode.getError(), returnCode.getMessage(MbDeclaredEntity.MB_LANGUAGE.EN.name()), null);
//    }
}
