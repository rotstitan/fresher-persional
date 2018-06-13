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
import mto.billing.fe.entity.MbApiOutput.ERROR_CODE_API;
import mto.billing.fe.entity.MbEntity;
import mto.billing.fe.entity.MbEntity.MbErrorMessage;
import static mto.billing.fe.main.Configuration.MB_ORDER_STATUS_MESSAGE;
import zcore.config.Config;
import zcore.config.LogUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author huuloc.tran89
 */
public class Configuration {

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
        
        OPS_TOOL_SERVER_HOST = Config.getParam("opstool_server", "host");
        OPS_TOOL_SERVER_PORT = Config.getIntParam("opstool_server", "port");
        OPS_TOOL_SECRET_KEY = Config.getParam("opstool_server", "secret_key");        
        OPS_TOOL_SQL_DBNAME = Config.getParam("opstool_server", "sql_db");
        OPS_TOOL_SQL_HOST = Config.getParam("opstool_server", "sql_host");
        OPS_TOOL_SQL_PORT = Config.getIntParam("opstool_server", "sql_port");
        
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

}
