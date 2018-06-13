/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.utilities;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import scribe.thrift.LogEntry;
import scribe.thrift.ResultCode;
import scribe.thrift.zscribeUPool;

import vn.zing.zastyanax.ZAstyanaxContext;

/**
 *
 * @author tunm
 */
public class ScribeServiceClient {

    private static Logger logger = Logger.getLogger(ScribeServiceClient.class);
    private static final Lock createLock_ = new ReentrantLock();
    private static Map<String, ScribeServiceClient> _instances = new NonBlockingHashMap<String, ScribeServiceClient>();
    private zscribeUPool poolInst;

    public ZAstyanaxContext getContext() {
        return poolInst.context;
    }

    public static ScribeServiceClient getInstance(String master_host, int master_port,
            String slave_host, int slave_port, int maxCons, int maxConsPerHost,
            int initCons, int timeout) {
        String key = master_host + master_port + slave_host + slave_port;
        if (!_instances.containsKey(key)) {
            createLock_.lock();
            try {
                if (_instances.get(key) == null) {
                    _instances.put(key, new ScribeServiceClient(master_host, master_port,
                            slave_host, slave_port, maxCons, maxConsPerHost, initCons, timeout));
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instances.get(key);
    }

    private ScribeServiceClient(String master_host, int master_port, String slave_host, int slave_port, int maxCons, int maxConsPerHost, int initCons, int timeout) {
        try {
            poolInst = zscribeUPool.getInstance(master_host + ":" + master_port
                    + "::" + zscribeUPool.class.toString(), master_host + ":"
                    + master_port, slave_host + ":" + slave_port, maxCons, maxConsPerHost, initCons, timeout);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        logger.info(ScribeServiceClient.class.toString() + ": finished init pool.......");
    }

    public boolean writeLog(String category, String message) {
        String[] messages = new String[1];
        messages[0] = message;
        return this.writeLog(category, messages);
    }

    public boolean writeLog(String category, String[] messages) {
        boolean ret = false;
        if (messages == null || messages.length == 0) {
            return ret;
        }
        try {
            List<LogEntry> logs = new LinkedList<LogEntry>();

            for (int i = 0; i < messages.length; i++) {
                LogEntry le = new LogEntry();
                le.category = category;
                le.message = messages[i];
                logs.add(le);
            }
            ResultCode Log = poolInst.Log(logs);
            if (Log.OK.equals(Log)) {
                ret = true;
            }
        } catch (Exception ex) {
        }
        return ret;
    }

    public boolean writeLog2(String category, String message) {
        String[] messages = new String[1];
        messages[0] = message;
        return this.writeLog2(category, messages);
    }

    public boolean writeLog2(String category, String[] messages) {

        boolean ret = false;
        if (messages == null || messages.length == 0) {
            return ret;
        }
        try {
            List<LogEntry> logs = new LinkedList<LogEntry>();

            for (int i = 0; i < messages.length; i++) {
                LogEntry le = new LogEntry();
                le.category = category;
                le.message = messages[i];
                logs.add(le);
            }
            poolInst.Log2(logs);
            ret = true;
        } catch (Exception ex) {
        }
        return ret;

    }

}
