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
import org.jooq.impl.DSL.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jooq.Record;
import org.jooq.Result;
import org.json.simple.JSONObject;
import zcore.utilities.*;
import java.util.Date;
import zcore.config.LogUtil;


/**
 *
 * @author rots
 */
public class Converter {
    
    public static void ParseLogData(String path) {
        try {
            FileReader reader = new FileReader(new File(path));
            final LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                JSONObject data = CommonUtil.parseJSONObject(line);
                // Do some thing with json Log
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SQLConnectionHandler instance = SQLConnectionHandler.getInstance();
            long order = 1076719857552072704l;
            System.out.println("Test order in table: Orders_201803");
            Result<Record> records = instance.selectDataByOrderNumber(order);
        } catch (Exception ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
