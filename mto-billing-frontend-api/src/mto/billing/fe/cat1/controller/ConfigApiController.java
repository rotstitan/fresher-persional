package mto.billing.fe.cat1.controller;

import java.util.HashMap;
import mto.billing.config.MbConfigService;
import mto.billing.fe.entity.MbApiOutput.*;
import mto.billing.fe.main.Configuration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mto.billing.config.MbConfigDeclaredEntity;
import mto.billing.config.MbConfigEntity;
import mto.billing.config.MbConfigEntity.MbAppConfig;
import mto.billing.config.MbConfigEntity.MbClientConfig;
import mto.billing.fe.entity.MbApiOutput;
import mto.billing.fe.entity.MbUtils;
import mto.ext.passport.PassportService;
import org.apache.log4j.Logger;
import zcore.controller.ApiOutput;
import zcore.controller.ApiServlet;
import zcore.utilities.CommonUtil;

/**
 *
 * @author huuloc.tran89
 */
public class ConfigApiController extends ApiServlet {

    private static final Logger logger = Logger.getLogger(ConfigApiController.class);

    private class MbDisplayInfoConfig {

        public MbDisplayInfoConfig(MbConfigEntity.DisplayInfo displayInfo,
                MbConfigEntity.MbStoreInfo mbStoreInfo,
                HashMap<String, String> loginInfo,
                HashMap<String, MbConfigEntity.VngPaymentGwInfo> paymentGwInfo,
                String lastUpdated) {

            this.displayInfo = displayInfo;
            this.mbStoreInfo = mbStoreInfo;
            this.loginInfo = loginInfo;
            this.paymentGwInfo = paymentGwInfo;
            this.lastUpdated = lastUpdated;

        }

        MbConfigEntity.DisplayInfo displayInfo;
        MbConfigEntity.MbStoreInfo mbStoreInfo;
        HashMap<String, String> loginInfo;
        HashMap<String, MbConfigEntity.VngPaymentGwInfo> paymentGwInfo;
        public String lastUpdated;
    }

    @Override
    protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (!checkValidParam(req, new String[]{"clientKey"})) {
                logger.warn("checkValidParam - ConfigApiController: " + req.getParameterMap());
                return new MbApiOutput(ERROR_CODE_API.INVALID_DATA_INPUT, req);
            }

            String clientKey = req.getParameter("clientKey");
            String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();

            //DDOS Check
            if (MbUtils.isDdosIp(CommonUtil.getClientIp(req))) {
                return new MbApiOutput(ERROR_CODE_API.DDOS_DETECTION_ERROR, req);
            }

            MbConfigService ss = MbConfigService.getInstance(Configuration.CONF_DOMAIN, Configuration.CONF_SECRET_KEY, Configuration.CONF_ENCRYPT_KEY);
            MbConfigEntity.MbConfig config = ss.getConfigCacheByKey(clientKey);

            if (config == null || config.appConfig == null || config.clientConfig == null) {
                return new MbApiOutput(ERROR_CODE_API.CONFIG_ERROR, req);
            }

            MbConfigEntity.MbAppConfig appConfig = config.appConfig;
            MbConfigEntity.MbClientConfig clientConfig = config.clientConfig;

            switch (pathInfo) {
                case "/getDisplayInfo":
                    return getDisplayInfo(appConfig, clientConfig, req);
                case "/getProductInfo":
                    return getProductInfo(appConfig, clientConfig, req);
            
                default:
                    return new MbApiOutput(ERROR_CODE_API.UNSUPPORTED_ERROR, req);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new MbApiOutput(ERROR_CODE_API.SERVER_ERROR, req);
    }

   
    public ApiOutput getDisplayInfo(MbAppConfig appConfig, MbClientConfig clientConfig, HttpServletRequest req) {
        try {
            String lastUpdated = req.getParameter("lastUpdated");

            int isLogin;
            String ppAppID;
            String ppMapID;
            int loginGwType;
            HashMap<String, String> loginInfo;
            long modifiedDate;

            try {
                isLogin = clientConfig.storeInfo.loginType;
                loginInfo = clientConfig.storeInfo.loginChannels;
                ppAppID = appConfig.coreApiInfo.loginGwApi.loginAppID;
                ppMapID = appConfig.coreApiInfo.loginGwApi.loginMapID;
                loginGwType = appConfig.coreApiInfo.loginGwApi.loginGwType;
                modifiedDate = clientConfig.modifiedDate;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                return new MbApiOutput(ERROR_CODE_API.CONFIG_ERROR, req);
            }

            if (isLogin == MbConfigDeclaredEntity.MB_CONFIG_ENABLE_TYPE.YES.type) {
                if (loginGwType == MbConfigDeclaredEntity.MB_COREAPI_LOGINGW_TYPE.PPLOGINGW.type) {
                    PassportService ps = PassportService.getInstance(Configuration.PASSPORT_DOMAIN);
                    loginInfo = ps.genUrl(clientConfig.storeInfo.loginChannels.keySet(), ppAppID, ppMapID);
                } else if (loginGwType == MbConfigDeclaredEntity.MB_COREAPI_LOGINGW_TYPE.PPLOGINGWSEA.type) {
                    PassportService ps = PassportService.getInstance(Configuration.PASSPORT_SEA_DOMAIN);
                    loginInfo = ps.genUrl(clientConfig.storeInfo.loginChannels.keySet(), ppAppID, ppMapID);
                }
            }

            if (CommonUtil.isLong(lastUpdated) && modifiedDate == Long.parseLong(lastUpdated)) {
                return new MbApiOutput(ERROR_CODE_API.SUCCESS, req,
                        new MbDisplayInfoConfig(null, null, null, null, String.valueOf(modifiedDate)));
            }

            return new MbApiOutput(ERROR_CODE_API.SUCCESS, req,
                    new MbDisplayInfoConfig(clientConfig.displayInfo, clientConfig.storeInfo, loginInfo, clientConfig.paymentGwInfo, String.valueOf(clientConfig.modifiedDate)));
//            return new MbApiOutput(ERROR_CODE_API.SUCCESS, req,
//                    new MbDisplayInfoConfig(clientConfig.displayInfo, clientConfig.storeInfo, loginInfo, clientConfig.paymentGwInfo, String.valueOf(System.currentTimeMillis())));

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new MbApiOutput(ERROR_CODE_API.SERVER_ERROR, req);
    }

    public ApiOutput getProductInfo(MbAppConfig appConfig, MbClientConfig clientConfig, HttpServletRequest req) {
        try {
            return new MbApiOutput(ERROR_CODE_API.SUCCESS, req, clientConfig.sellingProducts);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new MbApiOutput(ERROR_CODE_API.SERVER_ERROR, req);
    }

}
