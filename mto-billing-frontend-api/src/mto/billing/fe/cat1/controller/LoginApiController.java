/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.cat1.controller;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import mto.billing.fe.main.Configuration;
import mto.ext.passport.PassportService;
import mto.billing.fe.service.JwtService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mto.billing.config.MbConfigDeclaredEntity;
import mto.billing.config.MbConfigEntity;
import mto.billing.config.MbConfigService;
import org.apache.log4j.Logger;
import mto.billing.fe.entity.MbApiOutput;
import mto.billing.fe.entity.MbApiOutput.*;
import mto.billing.fe.entity.MbDeclaredEntity.JTOKEN_LOGIN_METHOD;
import mto.billing.fe.entity.MbEntity;
import mto.billing.fe.entity.MbEntity.MbLoginInfo;
import mto.billing.fe.entity.MbStoreEntity;
import mto.billing.fe.entity.MbUtils;
import mto.ext.passport.PassportConstant;
import mto.ext.passport.PassportOutput;
import zcore.controller.ApiServlet;
import zcore.controller.ApiOutput;
import zcore.utilities.CommonUtil;

/**
 *
 * @author huuloc.tran89
 */
public class LoginApiController extends ApiServlet {

    private final Logger logger = Logger.getLogger(LoginApiController.class);

    @Override
    protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (!checkValidParam(req, new String[]{"clientKey", "lang"})) {
                logger.warn("checkValidParam - LoginApiController: " + req.getParameterMap());
                return new MbApiOutput(ERROR_CODE_API.INVALID_DATA_INPUT, req);
            }

            // check DDOS 
            String clientIP = CommonUtil.getClientIp(req);
            if (MbUtils.isDdosIp(clientIP)) {
                return new MbApiOutput(ERROR_CODE_API.DDOS_DETECTION_ERROR, req);
            }

            // check lang
            String lang = req.getParameter("lang");

            String clientKey = req.getParameter("clientKey");
            MbConfigService ss = MbConfigService.getInstance(Configuration.CONF_DOMAIN, Configuration.CONF_SECRET_KEY, Configuration.CONF_ENCRYPT_KEY);
            MbConfigEntity.MbConfig config = ss.getConfigCacheByKey(clientKey);

            if (config == null || config.appConfig == null || config.clientConfig == null) {
                return new MbApiOutput(ERROR_CODE_API.CONFIG_ERROR, req);
            }
            MbConfigEntity.MbAppConfig appConfig = config.appConfig;
            MbConfigEntity.MbClientConfig clientConfig = config.clientConfig;

            String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
            switch (pathInfo) {
                case "/login":
                    return login(appConfig, clientConfig, lang, req, resp);
                case "/direct":
                    return direct(appConfig, clientConfig, lang, req, resp);
                case "/logout":
                    return logout(appConfig, clientConfig, lang, req, resp);
                default:
                    return new MbApiOutput(ERROR_CODE_API.UNSUPPORTED_ERROR, req);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new MbApiOutput(ERROR_CODE_API.SERVER_ERROR, req);
    }

    public ApiOutput login(MbConfigEntity.MbAppConfig appConfig, MbConfigEntity.MbClientConfig clientConfig, String lang, HttpServletRequest req, HttpServletResponse resp) {
        try {
            int loginType;
            int loginGwType;
            String secretKey;
            String callbackSecretKey;

            try {
                loginType = clientConfig.storeInfo.loginType;
                loginGwType = appConfig.coreApiInfo.loginGwApi.loginGwType;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                return new MbApiOutput(ERROR_CODE_API.CONFIG_ERROR, req);
            }

            if (loginType == MbConfigDeclaredEntity.MB_CONFIG_ENABLE_TYPE.NO.type) {
                return new MbApiOutput(ERROR_CODE_API.UNSUPPORTED_ERROR, req);
            }

            if (loginGwType == MbConfigDeclaredEntity.MB_COREAPI_LOGINGW_TYPE.PPLOGINGW.type) { //PPLOGINGW VN
                secretKey = appConfig.coreApiInfo.loginGwApi.secretKey;
                callbackSecretKey = appConfig.coreApiInfo.loginGwApi.callbackKey;
                PassportService ppts = PassportService.getInstance(Configuration.PASSPORT_DOMAIN);
                String jtoken = null;

                PassportConstant.CallbackData data = ppts.verifyCallbackData(req, secretKey, callbackSecretKey);
                if (data != null && data.error != null) {
                    if (data.error.equals(PassportOutput.ERROR_CODE_API.SUCCESS)) { // check pp error code
                        // create jtoken
                        jtoken = JwtService.createJwtSession(
                                new MbEntity.MbJtoken(appConfig.appID, clientConfig.clientID, JTOKEN_LOGIN_METHOD.LOGIN.name().toLowerCase(),
                                        data.userID, data.loginType));
                        if (jtoken != null) {
                            return new MbApiOutput(ERROR_CODE_API.SUCCESS, req, new MbLoginInfo(data.userID, data.userName, data.loginType, jtoken));
                        }
                    }
                }
                logger.error("Auth login - CallbackData: " + (data == null ? null : data.toJString()));
                return new MbApiOutput(ERROR_CODE_API.PASSPORT_AUTHEN_ERROR.getError(),
                        (data == null || Strings.isNullOrEmpty(jtoken)) ? ERROR_CODE_API.PASSPORT_AUTHEN_ERROR.getMessage(lang) : data.error.message);
            } else if (loginGwType == MbConfigDeclaredEntity.MB_COREAPI_LOGINGW_TYPE.PPLOGINGWSEA.type) { //PPLOGINGW SEA
                secretKey = appConfig.coreApiInfo.loginGwApi.secretKey;
                callbackSecretKey = appConfig.coreApiInfo.loginGwApi.callbackKey;
                PassportService ppts = PassportService.getInstance(Configuration.PASSPORT_SEA_DOMAIN);
                String jtoken = null;

                PassportConstant.CallbackData data = ppts.verifyCallbackData(req, secretKey, callbackSecretKey);
                if (data != null && data.error != null) {
                    if (data.error.equals(PassportOutput.ERROR_CODE_API.SUCCESS)) { // check pp error code
                        // create jtoken
                        jtoken = JwtService.createJwtSession(
                                new MbEntity.MbJtoken(appConfig.appID, clientConfig.clientID, JTOKEN_LOGIN_METHOD.LOGIN.name().toLowerCase(),
                                        data.userID, data.loginType));
                        if (jtoken != null) {
                            return new MbApiOutput(ERROR_CODE_API.SUCCESS, req, new MbLoginInfo(data.userID, data.userName, data.loginType, jtoken));
                        }
                    }
                }
                logger.error("Auth login - CallbackData: " + (data == null ? null : data.toJString()));
                return new MbApiOutput(ERROR_CODE_API.PASSPORT_AUTHEN_ERROR.getError(),
                        (data == null || Strings.isNullOrEmpty(jtoken)) ? ERROR_CODE_API.PASSPORT_AUTHEN_ERROR.getMessage(lang) : data.error.message);
            } else { // chưa hỗ trợ pplogin TYPE khác 
                return new MbApiOutput(ERROR_CODE_API.UNSUPPORTED_ERROR, req);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new MbApiOutput(ERROR_CODE_API.SERVER_ERROR, req);
    }

    

    public ApiOutput direct(MbConfigEntity.MbAppConfig appConfig, MbConfigEntity.MbClientConfig clientConfig, String lang, HttpServletRequest req, HttpServletResponse resp) {
        try {
            int directType = clientConfig.storeInfo.directType;
            if (directType == MbConfigDeclaredEntity.MB_CONFIG_ENABLE_TYPE.NO.type) {
                return new MbApiOutput(ERROR_CODE_API.UNSUPPORTED_ERROR, req);
            }

            if (!checkValidParam(req, new String[]{"sig", "userID"})) {
                logger.warn("checkValidParam - Auth direct: " + req.getParameterMap());
                return new MbApiOutput(ERROR_CODE_API.INVALID_DATA_INPUT, req);
            }

            String sig = req.getParameter("sig");

            if (!MbUtils.checkSig(clientConfig.secretKey, sig, clientConfig.signMethod, req, new String[]{"sig", "clientKey"})) {
                return new MbApiOutput(ERROR_CODE_API.INVALID_SIGNATURE, req);
            }

            String userID = req.getParameter("userID");
            String userName = req.getParameter("userName") == null ? "" : req.getParameter("userName");
            String loginType = req.getParameter("loginType") == null ? "" : req.getParameter("loginType");
            String serverID = req.getParameter("serverID") == null ? "" : req.getParameter("serverID");
            String roleID = req.getParameter("roleID") == null ? "" : req.getParameter("roleID");
            String roleName = req.getParameter("roleName") == null ? "" : req.getParameter("roleName");

            String jtoken = JwtService.createJwtSession(
                    new MbEntity.MbJtoken(appConfig.appID, clientConfig.clientID, JTOKEN_LOGIN_METHOD.DIRECT.name().toLowerCase(),
                            userID, loginType, roleID, roleName, serverID));
            if (jtoken != null) {
//                return new MbApiOutput(ERROR_CODE_API.SUCCESS, new MbLoginInfo(userID, userName, loginType, jtoken));
                return new MbApiOutput(ERROR_CODE_API.SUCCESS, req, new MbLoginInfo(userID, userName, loginType, serverID, roleID, roleName, jtoken));
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new MbApiOutput(ERROR_CODE_API.SERVER_ERROR, req);
    }

    public ApiOutput logout(MbConfigEntity.MbAppConfig appConfig, MbConfigEntity.MbClientConfig clientConfig, String lang, HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (!checkValidParam(req, new String[]{"jtoken"})) {
                logger.warn("checkValidParam - Auth logout: " + req.getParameterMap());
                return new MbApiOutput(ERROR_CODE_API.INVALID_DATA_INPUT, req);
            }

            String jtoken = req.getParameter("jtoken");

            MbEntity.MbJtoken mbJtoken = JwtService.getDataFromJwtSession(jtoken);
            if (mbJtoken == null) {
                return new MbApiOutput(ERROR_CODE_API.INVALID_TOKEN, req);
            }

            // Delete jwtoken in redis
            JwtService.deleteJwtSession(jtoken, appConfig.appID);

            return new MbApiOutput(ERROR_CODE_API.SUCCESS, req);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new MbApiOutput(ERROR_CODE_API.SERVER_ERROR, req);
    }

}
