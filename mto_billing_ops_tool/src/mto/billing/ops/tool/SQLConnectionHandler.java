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
import org.apache.commons.lang.StringUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
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
    public static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+", DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER", DEFAULT_DELIMITER = ";";
    private String delimiter = DEFAULT_DELIMITER;
    
    
    private SQLConnectionHandler(){
        try {
            currentConntector = DBConnector.getInstance("127.0.0.1", "3306", "testdb", "root", "1020");
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
            long time = SnowflakeIdGenerator.extractTimestamp64(orderNumber);
            java.util.Date date = new java.util.Date(time);
            DateFormat dbTableFormat = new SimpleDateFormat("yyyyMM");
            String tableId = "Orders_" + dbTableFormat.format(date);
            String sql = "SELECT * FROM " + tableId + " WHERE OrderNumber = " + orderNumber + " LIMIT 1";
            Result<Record> result = sqlFetch(sql);
            logger.info("selectDataByOrderNumber: " + orderNumber + "\n" + result);
            return result;
        }catch (SQLException ex){
            logger.error("Error When selectDataByOrderNumber:\n" + ex.getMessage(), ex);
        }
        return null;
    }
    
}