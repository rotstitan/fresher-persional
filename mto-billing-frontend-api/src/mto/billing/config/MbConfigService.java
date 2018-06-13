/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mto.billing.config.MbConfigApiOutput.GetMbConfigOutput;
import mto.billing.config.MbConfigEntity.MbClientKey;
import mto.billing.config.MbConfigEntity.MbConfig;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import zcore.utilities.CommonUtil;

/**
 *
 * @author anonymous
 */
public class MbConfigService {

    private static final Logger logger = Logger.getLogger(MbConfigService.class);
    private static final Lock LOCK = new ReentrantLock();
    private static Map<String, MbConfigService> _instances = new NonBlockingHashMap<String, MbConfigService>();

    private final String domain;
    private final String secretKey;
    private final String encryptKey;

    private final LoadingCache<String, MbConfig> configCache;

    private MbConfigService(String domain, String secretKey, String encryptKey) {
        this.domain = domain;
        this.secretKey = secretKey;
        this.encryptKey = encryptKey;

        this.configCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(365, TimeUnit.DAYS)
                .build(new CacheLoader<String, MbConfig>() {
                    @Override
                    public MbConfig load(String clientID) throws Exception {
                        logger.info("MbConfigService load cache - clientID: " + clientID);
                        MbConfig mbConfig = getConfigByID(clientID);
                        if (mbConfig == null) {
                            throw new NullPointerException();
                        };
                        return mbConfig;
                    }
                });
    }

    public static MbConfigService getInstance(String domain, String secretKey, String encryptKey) {

        String key = new StringBuilder().append(domain).toString();

        if (!_instances.containsKey(key)) {
            LOCK.lock();
            try {
                if (_instances.get(key) == null) {
                    _instances.put(key, new MbConfigService(domain, secretKey, encryptKey));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return _instances.get(key);

    }

    public MbConfigEntity.MbConfig getConfigCacheByID(int clientID) {
        try {
            return configCache.get(String.valueOf(clientID));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public boolean clearConfigCacheByID(int clientID) {
        try {
            configCache.refresh(String.valueOf(clientID));
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    public MbConfigEntity.MbConfig getConfigCacheByKey(String clientKey) {
        try {
            MbClientKey decodeClientKey = decodeClientKey(clientKey);

            if (decodeClientKey == null) {
                logger.error("Decode clientKey failed: " + clientKey);
                return null;
            }

            int clientID = decodeClientKey.c;
            return configCache.get(String.valueOf(clientID));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    private MbClientKey decodeClientKey(String clientKey) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(encryptKey).parseClaimsJws(clientKey);

            Claims body = claims.getBody();
            MbClientKey client = CommonUtil.jsonToObject(body.toString(), MbClientKey.class);
            return client;
        } catch (Exception ex) {
            logger.info("Excetion encrypt data: " + ex.getMessage());
        }
        return null;
    }

    private MbConfigEntity.MbConfig getConfigByID(String clientID) {
        try {

            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("clientID", String.valueOf(clientID)));
            params.add(new BasicNameValuePair("ts", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("sig", MbConfigUtils.genSig(secretKey, params)));

            String url = domain + "/cf/api/getConfig";
            String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);

            String response = MbConfigUtils.curl(url + "?" + query, "GET", "");

            GetMbConfigOutput rs = CommonUtil.jsonToObject(response, GetMbConfigOutput.class);

            if (rs != null && rs.returnCode == GetMbConfigOutput.ERROR_CODE_API.SUCCESS.error) {
                return rs.data;
            }
            logger.error("MbConfig getConfig return invalid data: " + (rs == null ? null : rs.toJString()));

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    private MbConfigEntity.MbConfig getConfigByKey(String clientKey) {
        try {

            MbClientKey decodeClientKey = decodeClientKey(clientKey);

            if (decodeClientKey == null) {
                logger.error("Decode clientKey failed: " + clientKey);
                return null;
            }

            int clientID = decodeClientKey.c;
            return getConfigByID(String.valueOf(clientID));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

}
