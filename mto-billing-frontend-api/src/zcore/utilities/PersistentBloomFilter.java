/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.utilities;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.io.Files;
import it.sauronsoftware.cron4j.Scheduler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author huuloc.tran89
 */
public class PersistentBloomFilter {

    private static final Logger logger = Logger.getLogger(PersistentBloomFilter.class);
    private static final Lock createLock = new ReentrantLock();
    private static Map<String, PersistentBloomFilter> instances = new HashMap();
    private BloomFilter bf;

    private String dumpPath;
    private String dumpCron;

    public static PersistentBloomFilter getInstance(int item, double fpp, String dumpPath, String dumpCron) throws Exception {
        String key = dumpPath + dumpCron;
        if (!instances.containsKey(key)) {
            createLock.lock();
            try {
                if (!instances.containsKey(key)) {
                    instances.put(key, new PersistentBloomFilter(item, fpp, dumpPath, dumpCron));
                }
            } finally {
                createLock.unlock();
            }
        }
        return (PersistentBloomFilter) instances.get(key);
    }

    private PersistentBloomFilter(int item, double fpp, String dumpPath, String dumpCron) throws Exception {
        bf = BloomFilter.create(
                Funnels.byteArrayFunnel(),
                item,
                fpp);
        this.dumpPath = dumpPath;
        this.dumpCron = dumpCron;

        if (new File(dumpPath).exists()) {
            loadBF();
        }

        // Backup 
        Scheduler bfDumpScheduler = new Scheduler();
        logger.info("Backup Bloomfilter Scheduler: " + dumpCron);
        bfDumpScheduler.schedule(dumpCron, new Runnable() {
            @Override
            public void run() {
                dumpBF();
            }
        });

        bfDumpScheduler.start();

        //Hook event
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                dumpBF();
            }
        }));
    }

    public boolean mightContain(byte[] key) {
        return bf.mightContain(key);
    }

    public void set(byte[] key) {
        bf.put(key);
    }

    private void dumpBF() {
        try {
            logger.info("BloomFilter dump - file: " + dumpPath);
            File file = new File(dumpPath);
            if (file.exists()) {
                Files.copy(file, new File(dumpPath + ".bk"));
            }
            DataOutputStream output = new DataOutputStream(new FileOutputStream(dumpPath));
            bf.writeTo(output);
            logger.info("BloomFilter sucessfully dump - file: " + dumpPath);
        } catch (Exception ex) {
            logger.error("BloomFilter fail dump - file: " + dumpPath + " " + ex.getMessage(), ex);
        }
    }

    private void loadBF() throws FileNotFoundException, IOException {
        try {
            this.logger.info("BloomFilter load - file: " + dumpPath);
            DataInputStream input = new DataInputStream(new FileInputStream(new File(dumpPath)));
            bf = BloomFilter.readFrom(input, Funnels.byteArrayFunnel());
            logger.info("BloomFilter sucessfully load - file: " + dumpPath);
        } catch (Exception ex) {
            try {
                logger.error("BloonFilter load - file: " + dumpPath + " failed. Load backup " + dumpPath + ".bk");
                DataInputStream input = new DataInputStream(new FileInputStream(new File(dumpPath + ".bk")));
                bf = BloomFilter.readFrom(input, Funnels.byteArrayFunnel());
                logger.info("BloomFilter sucessfully load - file: " + dumpPath + ".bk");
            } catch (Exception ex1) {
                throw ex1;
            }
        }
    }
}
