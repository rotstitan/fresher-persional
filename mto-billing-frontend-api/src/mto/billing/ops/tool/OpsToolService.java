/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.ops.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import mto.billing.fe.main.AdminServer;
import static mto.billing.ops.tool.SQLConnectionHandler.currentConntector;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.eclipse.jetty.server.Server;
import org.jooq.Record;
import org.jooq.Result;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zcore.utilities.CommonUtil;

/**
 *
 * @author rots
 */
public class OpsToolService {
    private static final Lock LOCK = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(OpsToolService.class);
    private static OpsToolService _instance = null;

    public static OpsToolService getInstance() {
        if (_instance == null) {
            LOCK.lock();
            try {
                if (_instance == null) {
                    _instance = new OpsToolService();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return _instance;
    }
    
    private OpsToolService(){
    }
    
    public Map<String, Object> getDataByOrderNumber(long orderNumber){
        Result<Record> results = SQLConnectionHandler.getInstance().selectDataByOrderNumber(orderNumber);
        if(results == null || results.get(0) == null) return null;
        List<Map<String, Object>> rs = results.intoMaps();
        return rs.get(0);
    }
    public List<Map<String, Object>> getActionDataByOrderNumber(long orderNumber){
        Result<Record> results = SQLConnectionHandler.getInstance().selectActionDataByOrderNumber(orderNumber);
        if(results == null || results.get(0) == null) return null;
        List<Map<String, Object>> rs = results.intoMaps();
        return rs;
    }
    public static int count = 0;
    public static void ParseLogData(String filePath) {
        try {
            FileReader reader = new FileReader(new File(filePath));
            final LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            Connection cnn = SQLConnectionHandler.getInstance().getCurrentConnector().getMySqlConnection();
            while (cnn != null && (line = lineReader.readLine()) != null) {
                JSONObject data = CommonUtil.parseJSONObject(line);
                int result = SQLConnectionHandler.getInstance().insertActionOrder(cnn, data);
                if(result < 0) break;
            }
        } catch (FileNotFoundException ex) {
            logger.error(null, ex);
        } catch (IOException | SQLException ex) {
            logger.error(null, ex);
        }
    }
}
