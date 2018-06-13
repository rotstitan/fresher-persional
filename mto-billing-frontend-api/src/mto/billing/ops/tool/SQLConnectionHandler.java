/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.ops.tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mto.billing.fe.main.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.constraint;
import org.jooq.impl.SQLDataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zcore.utilities.CommonUtil;
import zcore.utilities.DBConnector;

/**
 *
 * @author rots
 */
public class SQLConnectionHandler {

    static DBConnector currentConntector = null;
    private static SQLConnectionHandler instance;
    private static final Lock LOCK = new ReentrantLock();
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SQLConnectionHandler.class);

    public synchronized DBConnector getCurrentConnector() {
        return currentConntector;
    }

    private SQLConnectionHandler() {
        try {
            currentConntector = DBConnector.getInstance(
                    Configuration.OPS_TOOL_SQL_HOST,
                    String.valueOf(Configuration.OPS_TOOL_SQL_PORT),
                    Configuration.OPS_TOOL_SQL_DBNAME,
                    "root", "1020");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static SQLConnectionHandler getInstance() {
        if (instance == null) {
            LOCK.lock();
            try {
                instance = new SQLConnectionHandler();
            } finally {
                LOCK.unlock();
            }
        } else {

        }
        return instance;
    }

    public Result<Record> sqlFetch(String sql) throws SQLException {
        DSLContext create = DSL.using(currentConntector.getMySqlConnection(), SQLDialect.MYSQL);
        Result<Record> result = create.fetch(sql);
        return result;
    }

    public Result<Record> sqlExecute(String sql) throws SQLException {
        Connection cnn = currentConntector.getMySqlConnection();
        DSLContext create = DSL.using(cnn, SQLDialect.MYSQL);
        ResultSet rs = cnn.createStatement().executeQuery(sql);
        Result<Record> result = create.fetch(rs);
        return result;
    }

    public Result<Record> selectDataByOrderNumber(long orderNumber) {
        try {
            String tableId = CommonUtil.getMontlyStringOfOrder(orderNumber);
            if (null == tableId) {
                logger.error("Error When selectDataByOrderNumber: Cannot Generate TableID\n", null);
                return null;
            }
            tableId = "Orders_" + tableId;
            String sql = "SELECT * FROM " + tableId + " WHERE OrderNumber = " + orderNumber + " LIMIT 1";
            Result<Record> result = sqlFetch(sql);
            return result;
        } catch (SQLException ex) {
            logger.error("Error When selectDataByOrderNumber:\n" + ex.getMessage(), ex);
        }
        return null;
    }

    public Result<Record> selectActionDataByOrderNumber(long orderNumber) {
        try {
            String tableId = CommonUtil.getMontlyStringOfOrder(orderNumber);
            if (null == tableId) {
                logger.error("Error When selectDataByOrderNumber: Cannot Generate TableID\n", null);
                return null;
            }
            tableId = "ActionOrders_" + tableId;
            String sql = "SELECT * FROM " + tableId + " WHERE OrderNumber = " + orderNumber;
            Result<Record> result = sqlFetch(sql);
            return result;
        } catch (SQLException ex) {
            logger.error("Error When selectDataByOrderNumber:\n" + ex.getMessage(), ex);
        }
        return null;
    }

    public int insertActionOrder(Connection cnn, JSONObject orderData) {
        try {
            String startDate = String.valueOf(orderData.get("startDate"));
            long orderNumber = (long) orderData.get("orderNumber");
            String type = String.valueOf(orderData.get("type"));
            String action = String.valueOf(orderData.get("action"));
            String errorMessage = String.valueOf(orderData.get("errorMessage"));
            String endDate = String.valueOf(orderData.get("endDate"));
            String path = String.valueOf(orderData.get("path"));
            String input = String.valueOf(orderData.get("input"));
            String output = String.valueOf(orderData.get("output"));
            long exTime = (long) orderData.get("exTime");
            JSONArray steps = (JSONArray) orderData.get("steps");
            int index = 0;
            for (; index < steps.size(); index++) {
                ((JSONObject) steps.get(index)).remove("startDate");
            }

            String tableId = CommonUtil.getMontlyStringOfOrder(orderNumber);
            if (null == tableId) {
                logger.error("Error When selectDataByOrderNumber: Cannot Generate TableID\n", null);
                return -1;
            }
            tableId = "ActionOrders_" + tableId;
            //put table if not exist
            DSLContext create = DSL.using(cnn, SQLDialect.MYSQL);
            int createResult = create.createTableIfNotExists(tableId)
                    .column("ID", SQLDataType.INTEGER.length(11).nullable(false).identity(true))
                    .column("OrderNumber", SQLDataType.BIGINT.length(20).nullable(false))
                    .column("StartDate", SQLDataType.VARCHAR.length(30).nullable(true))
                    .column("Type", SQLDataType.VARCHAR.length(10).nullable(false))
                    .column("Action", SQLDataType.VARCHAR.length(20).nullable(false))
                    .column("Steps", SQLDataType.VARCHAR.length(400).nullable(false))
                    .column("ErrorMessage", SQLDataType.VARCHAR.length(50))
                    .column("EndDate", SQLDataType.VARCHAR.length(30).nullable(true))
                    .column("Path", SQLDataType.VARCHAR.length(255))
                    .column("ExTime", SQLDataType.INTEGER.length(6).nullable(false))
                    .constraints(
                            constraint("PK_Order").primaryKey("ID"))
                    .execute();
            String sql = "INSERT INTO " + tableId + " "
                    + "(OrderNumber, StartDate, Type, Action, Steps, ErrorMessage, EndDate, Path, ExTime) "
                    + "VALUES (" + orderNumber + ",'" + startDate + "','" + type + "','" + action + "','" + steps + "','" + errorMessage + "','" + endDate + "','" + path + "'," + exTime + ")";

            Result<Record> result = create.fetch(sql);
            return 1;
        } catch (DataAccessException ex) {
            logger.error("Error When selectDataByOrderNumber:\n" + ex.getMessage(), ex);
        }
        return -1;
    }
}
