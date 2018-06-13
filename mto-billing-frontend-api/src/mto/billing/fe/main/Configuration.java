/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import mto.billing.config.MbConfigEntity;
import mto.billing.fe.entity.MbApiOutput.ERROR_CODE_API;
import mto.billing.fe.entity.MbEntity;
import mto.billing.fe.entity.MbEntity.MbErrorMessage;
import static mto.billing.fe.main.Configuration.MB_ORDER_STATUS_MESSAGE;
import zcore.config.Config;
import zcore.config.LogUtil;

import org.apache.log4j.Logger;
import zcore.utilities.CommonUtil;

/**
 *
 * @author huuloc.tran89
 */
public class Configuration {

    public static String WEB_SERVER_HOST;
    public static Integer WEB_SERVER_PORT;

    public static String ADMIN_SERVER_HOST;
    public static Integer ADMIN_SERVER_PORT;
    public static String ADMIN_SECRET_KEY;

    // redis
    public static String REDIS_ADDRESSES;
    public static boolean REDIS_IS_CLUSTER;
    public static String REDIS_POOL_PASSWORD;

    // scribe
    public static String SCRIBE_HOST;
    public static int SCRIBE_PORT;
    public static String SCRIBE_REQ_CATE;
    public static String SCRIBE_DB_CATE;
    public static String SCRIBE_EXT_REQ_CATE;

    // passport
    public static String PASSPORT_DOMAIN;
    public static String PASSPORT_KEY;
    public static String PASSPORT_CALLBACK_KEY;

    public static String PASSPORT_SEA_DOMAIN;
    public static String PASSPORT_SEA_KEY;
    public static String PASSPORT_SEA_CALLBACK_KEY;

    // payment
    public static String PAYMENT_DOMAIN;

    // config
    public static String CONF_DOMAIN;
    public static String CONF_ENCRYPT_KEY;
    public static String CONF_SECRET_KEY;

    //jtoken
    public static String JTOKEN_SECRET_KEY;
    public static int JTOKEN_EXPIRE_TIME_IN_SECOND;

    // store 
    public static String WPSTORE_DOMAIN;
    public static String WPSTORE_CLIENTID;
    public static String WPSTORE_SECRET_KEY;
    public static boolean WPSTORE_PROXY_ENABLE;

    // fulfillment
    public static String FULFILLMENT_DOMAIN;
    public static String FULFILLMENT_SECRET_KEY;

    // order node generator
    public static int ORDERNUMBER_GENERATOR_NODE_ID;

    // message lang path
    public static String API_MESSAGE_LANG_CONFIG_PATH;
    public static String ORDER_MESSAGE_LANG_CONFIG_PATH;

    // message map
    public static HashMap<ERROR_CODE_API, MbErrorMessage> MB_API_ERROR_MESSAGE;
    public static HashMap<String, MbErrorMessage> MB_ORDER_STATUS_MESSAGE;

    private static final Logger logger = Logger.getLogger(Configuration.class);

    
    // ops tool server config
    public static String OPS_TOOL_SERVER_HOST;
    public static Integer OPS_TOOL_SERVER_PORT;
    public static String OPS_TOOL_SECRET_KEY;
    public static String OPS_TOOL_SQL_HOST;
    public static Integer OPS_TOOL_SQL_PORT;
    public static String OPS_TOOL_SQL_DBNAME;
    
    public Configuration() throws Exception {

        WEB_SERVER_HOST = Config.getParam("web_server", "host");
        WEB_SERVER_PORT = Config.getIntParam("web_server", "port");

        ADMIN_SERVER_HOST = Config.getParam("admin_server", "host");
        ADMIN_SERVER_PORT = Config.getIntParam("admin_server", "port");
        ADMIN_SECRET_KEY = Config.getParam("admin_server", "secret_key");
        
        OPS_TOOL_SERVER_HOST = Config.getParam("opstool_server", "host");
        OPS_TOOL_SERVER_PORT = Config.getIntParam("opstool_server", "port");
        OPS_TOOL_SECRET_KEY = Config.getParam("opstool_server", "secret_key");        
        OPS_TOOL_SQL_DBNAME = Config.getParam("opstool_server", "sql_db");
        OPS_TOOL_SQL_HOST = Config.getParam("opstool_server", "sql_host");
        OPS_TOOL_SQL_PORT = Config.getIntParam("opstool_server", "sql_port");

        SCRIBE_HOST = Config.getParam("scribe", "host");
        SCRIBE_PORT = Config.getIntParam("scribe", "port");
        SCRIBE_REQ_CATE = Config.getParam("scribe", "cate_req");
        SCRIBE_EXT_REQ_CATE = Config.getParam("scribe", "cate_store_req");
        SCRIBE_DB_CATE = Config.getParam("scribe", "cate_db");

        PASSPORT_DOMAIN = Config.getParam("passport", "domain");
        PASSPORT_KEY = Config.getParam("passport", "serectkey");
        PASSPORT_CALLBACK_KEY = Config.getParam("passport", "keycallback");

        PASSPORT_SEA_DOMAIN = Config.getParam("passport_sea", "domain");
        PASSPORT_SEA_KEY = Config.getParam("passport_sea", "serectkey");
        PASSPORT_SEA_CALLBACK_KEY = Config.getParam("passport_sea", "keycallback");

        PAYMENT_DOMAIN = Config.getParam("payment", "domain");

        WPSTORE_DOMAIN = Config.getParam("wpStore", "domain");
        WPSTORE_CLIENTID = Config.getParam("wpStore", "client_id");
        WPSTORE_SECRET_KEY = Config.getParam("wpStore", "secret_key");
        WPSTORE_PROXY_ENABLE = Config.getIntParam("wpStore", "proxy_enable") == 1;

        REDIS_ADDRESSES = Config.getParam("redis", "addresses");
        REDIS_IS_CLUSTER = Config.getIntParam("redis", "isCluster") == 1;
        REDIS_POOL_PASSWORD = Config.getParam("redis", "pool_password");

        CONF_DOMAIN = Config.getParam("config", "domain");
        CONF_SECRET_KEY = Config.getParam("config", "secret_key");
        CONF_ENCRYPT_KEY = Config.getParam("config", "encrypt_key");

        FULFILLMENT_DOMAIN = Config.getParam("fulfillment", "domain");
        FULFILLMENT_SECRET_KEY = Config.getParam("fulfillment", "secret_key");

        JTOKEN_SECRET_KEY = Config.getParam("jtoken", "secret_key");
        JTOKEN_EXPIRE_TIME_IN_SECOND = Config.getIntParam("jtoken", "expire_in_second");

        ORDERNUMBER_GENERATOR_NODE_ID = Integer.parseInt(System.getProperty("order_node_id"));

        API_MESSAGE_LANG_CONFIG_PATH = Config.getParam("message_lang", "api_message_config");
        ORDER_MESSAGE_LANG_CONFIG_PATH = Config.getParam("message_lang", "order_message_config");

        FileInputStream file;
        Reader reader;

        // api message
        file = new FileInputStream(API_MESSAGE_LANG_CONFIG_PATH);
        reader = new InputStreamReader(file);
        MB_API_ERROR_MESSAGE = new Gson().fromJson(reader, new TypeToken<HashMap<ERROR_CODE_API, MbEntity.MbErrorMessage>>() {
        }.getType());

        // order message
        file = new FileInputStream(ORDER_MESSAGE_LANG_CONFIG_PATH);
        reader = new InputStreamReader(file);
        MB_ORDER_STATUS_MESSAGE = new Gson().fromJson(reader, new TypeToken<HashMap<String, MbEntity.MbErrorMessage>>() {
        }.getType());
    }

    public static void init() throws Exception {
        LogUtil.init();
        new Configuration();
    }

    public static void main(String[] args) throws Exception {
        Configuration.init();
        String a = "{\n"
                + "    \"SUCCESS\": {\n"
                + "        \"error\": 1,\n"
                + "        \"message\": {\n"
                + "            \"VN\": \"Thành công\",\n"
                + "            \"EN\": \"Success\",\n"
                + "            \"IN\": \" Sukses\",\n"
                + "            \"TH\": \"สำเร็จ\"\n"
                + "        }\n"
                + "    }\n"
                + "}";
        String b = "{\n"
                + "    \"[DE]DELIVER_SUCCESS\": {\n"
                + "        \"message\": {\n"
                + "            \"EN\": \"[DE -211] DELIVER_SUCCESS\",\n"
                + "            \"TH\": \"[DE -211] DELIVER_SUCCESS\",\n"
                + "            \"IN\": \"[DE -211] DELIVER_SUCCESS\",\n"
                + "            \"VI\": \"[DE -211] Giao dịch thành công\"\n"
                + "        },\n"
                + "        \"error\": 211\n"
                + "    }\n"
                + "}";

//      
        HashMap<ERROR_CODE_API, MbErrorMessage> fromJson = new Gson().fromJson(a, new TypeToken<HashMap<ERROR_CODE_API, MbEntity.MbErrorMessage>>() {
        }.getType());
        System.out.println(CommonUtil.objectToString(fromJson));
//
//        HashMap<ERROR_CODE_API, MbErrorMessage> fromJson = new Gson().fromJson(
//                b, new TypeToken<HashMap<String, MbEntity.MbErrorMessage>>() {
//                }.getType());
//        System.out.println(CommonUtil.objectToString(fromJson));

    }

}
