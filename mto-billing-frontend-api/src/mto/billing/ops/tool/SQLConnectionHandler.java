/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.ops.tool;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mto.billing.fe.main.Configuration;
import org.apache.commons.lang.StringUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep11;
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
import zcore.utilities.SnowflakeIdGenerator;

/**
 *
 * @author rots
 */
public class SQLConnectionHandler {
    static DBConnector currentConntector = null;
    private static SQLConnectionHandler instance;
    private static final Lock LOCK = new ReentrantLock();
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SQLConnectionHandler.class);
    // import sql data config
    public static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+", DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER", DEFAULT_DELIMITER = ";";
    private String delimiter = DEFAULT_DELIMITER;
    
    public synchronized DBConnector getCurrentConnector(){
        return currentConntector;
    }
    
    private SQLConnectionHandler(){
        try {
            currentConntector = DBConnector.getInstance(
                    Configuration.OPS_TOOL_SQL_HOST
                    ,String.valueOf(Configuration.OPS_TOOL_SQL_PORT) 
                    , Configuration.OPS_TOOL_SQL_DBNAME
                    , "root", "1020");
            
            //fetch all db
            //runScript(currentConntector.getMySqlConnection(),"billing.sql");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    public static SQLConnectionHandler getInstance(){
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
    private void runScript(final Connection conn, String path) {
        StringBuffer command = null;
        try {
            FileReader reader = new FileReader(new File(path)); 
            final LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();

                if (trimmedLine.startsWith("--") || trimmedLine.startsWith("//") || trimmedLine.startsWith("#")) {

                } else if (trimmedLine.endsWith(this.delimiter)) {

                    // Line is end of statement

                    // Support new delimiter
                    final Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
                    final Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        delimiter = trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim();

                        // New delimiter is processed, continue on next
                        // statement
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }

                    // Append
                    command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
                    command.append(" ");

                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = conn.createStatement();
                        boolean hasResults = stmt.execute(command.toString());
                        if (!conn.getAutoCommit()) {
                            conn.commit();
                        }
                        rs = stmt.getResultSet();
                        if (hasResults && rs != null) {

                            // Print result column names
                            final ResultSetMetaData md = rs.getMetaData();
                            final int cols = md.getColumnCount();
                            for (int i = 0; i < cols; i++) {
                                final String name = md.getColumnLabel(i + 1);
                            }
                            // Print result rows
                        }
                        command = null;
                    } finally {
                        if (rs != null)
                            try {
                                rs.close();
                            } catch (final Exception e) {
                                logger.error("Failed to close statement: " + e.getMessage());
                            }
                        if (stmt != null)
                            try {
                                stmt.close();
                            } catch (final Exception e) {
                                logger.error("Failed to close statement: " + e.getMessage());
                            }
                    }
                } else {

                    // Line is middle of a statement

                    // Support new delimiter
                    final Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
                    final Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        delimiter = trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim();
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }
                    command.append(line);
                    command.append(" ");
                }
            }
            conn.commit();
        } catch (final SQLException e) {
            e.fillInStackTrace();
        } catch (final IOException e) {
            e.fillInStackTrace();
        }
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
    
    public Result<Record> selectDataByOrderNumber(long orderNumber){
        try {
            String tableId = CommonUtil.getMontlyStringOfOrder(orderNumber);
            if(null == tableId) {
                logger.error("Error When selectDataByOrderNumber: Cannot Generate TableID\n", null);
                return null;
            }
            tableId =  "Orders_" + tableId;
            String sql = "SELECT * FROM " + tableId + " WHERE OrderNumber = " + orderNumber + " LIMIT 1";
            Result<Record> result = sqlFetch(sql);
            return result;
        }catch (SQLException ex){
            logger.error("Error When selectDataByOrderNumber:\n" + ex.getMessage(), ex);
        }
        return null;
    }
    public Result<Record> selectActionDataByOrderNumber(long orderNumber){
        try {
            String tableId = CommonUtil.getMontlyStringOfOrder(orderNumber);
            if(null == tableId) {
                logger.error("Error When selectDataByOrderNumber: Cannot Generate TableID\n", null);
                return null;
            }
            tableId =  "ActionOrders_" + tableId;
            String sql = "SELECT * FROM " + tableId + " WHERE OrderNumber = " + orderNumber;
            Result<Record> result = sqlFetch(sql);
            return result;
        }catch (SQLException ex){
            logger.error("Error When selectDataByOrderNumber:\n" + ex.getMessage(), ex);
        }
        return null;
    }
    public int insertActionOrder(Connection cnn, JSONObject orderData){
        try {
            String startDate = String.valueOf(orderData.get("startDate"));            
            long orderNumber = (long)orderData.get("orderNumber");
            String type = String.valueOf(orderData.get("type"));
            String action = String.valueOf(orderData.get("action"));
            String errorMessage = String.valueOf(orderData.get("errorMessage"));
            String endDate = String.valueOf(orderData.get("endDate"));
            String path = String.valueOf(orderData.get("path"));
            String input = String.valueOf(orderData.get("input"));
            String output = String.valueOf(orderData.get("output"));
            long exTime = (long)orderData.get("exTime");
            JSONArray steps = (JSONArray)orderData.get("steps");
            int index = 0;
            for(;index < steps.size();index ++){
                ((JSONObject)steps.get(index)).remove("startDate");
            }
            
            String tableId =  CommonUtil.getMontlyStringOfOrder(orderNumber);
            if(null == tableId) {
                logger.error("Error When selectDataByOrderNumber: Cannot Generate TableID\n", null);
                return -1;
            }
            tableId =  "ActionOrders_" + tableId;
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
          
            //System.out.println("Create:\n" + createResult);
            
            String sql = "INSERT INTO "+ tableId +" " +
                    "(OrderNumber, StartDate, Type, Action, Steps, ErrorMessage, EndDate, Path, ExTime) " +
                    "VALUES ("+orderNumber+",'"+startDate+"','"+type+"','"+action+"','"+steps+"','"+errorMessage+"','"+endDate+"','"+path+"',"+exTime+")";
          
            Result<Record> result = create.fetch(sql);
            return 1;
        }catch (DataAccessException ex){
            logger.error("Error When selectDataByOrderNumber:\n" + ex.getMessage(), ex);
        }
        return -1;
    }
}