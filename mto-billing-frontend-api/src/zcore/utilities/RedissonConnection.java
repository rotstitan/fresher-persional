/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.utilities;

/**
 *
 * @author huuloc.tran89
 */
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

public class RedissonConnection {

    private static Logger logger = Logger.getLogger(RedissonConnection.class.getName());
    private static final Lock createLock = new ReentrantLock();
    private static Map<String, RedissonConnection> instances = new HashMap();
    private RedissonClient redissonClient;

    public static RedissonConnection getInstance(String redisClusterAddrs, boolean isCluster, String password) {
        String key = redisClusterAddrs;
        if (!instances.containsKey(key)) {
            createLock.lock();
            try {
                if (!instances.containsKey(key)) {
                    instances.put(key, new RedissonConnection(redisClusterAddrs, isCluster, password));
                }
            } finally {
                createLock.unlock();
            }
        }
        return (RedissonConnection) instances.get(key);
    }

    private RedissonConnection(String redisAddrs, boolean isCluster, String password) {
        if (redisAddrs == null || redisAddrs.isEmpty()) {
            logger.error("Redis cluster hosts are not set");
            return;
        }
        logger.info("Creating Redis connection with hosts: " + redisAddrs);

        Config config = new Config();
        if (isCluster) {
            String[] nodeAddress = redisAddrs.split("\\|");
            logger.info("Redis nodeAddress: " + new Gson().toJson(nodeAddress));
            config.useClusterServers()
                    .addNodeAddress(nodeAddress)
                    .setScanInterval(2000)
                    .setSlaveConnectionMinimumIdleSize(1)
                    .setSlaveConnectionPoolSize(100)
                    .setMasterConnectionMinimumIdleSize(5)
                    .setMasterConnectionPoolSize(100)
                    .setIdleConnectionTimeout(10000)
                    .setConnectTimeout(1000)
                    .setTimeout(1000)
                    .setRetryAttempts(3)
                    .setRetryInterval(1000)
                    .setReconnectionTimeout(3000)
                    .setFailedAttempts(3)
                    .setReadMode(ReadMode.MASTER);

        } else {
            config.useSingleServer()
                    .setPassword(password)
                    .setAddress(redisAddrs)
                    .setConnectionMinimumIdleSize(5)
                    .setConnectionPoolSize(100)
                    .setIdleConnectionTimeout(10000)
                    .setConnectTimeout(1000)
                    .setTimeout(1000)
                    .setRetryAttempts(3)
                    .setRetryInterval(1000)
                    .setReconnectionTimeout(3000)
                    .setFailedAttempts(3);
        }

        this.redissonClient = Redisson.create(config);
    }

    public RedissonClient getClient() {
        return redissonClient;
    }

    public String getString(String key) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            String result = (String) bucket.get();
            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public boolean setString(String key, String value, long time, TimeUnit timeUnit) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value, time, timeUnit);
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    public boolean trySetString(String key, String value, long time, TimeUnit timeUnit) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            return bucket.trySet(value, time, timeUnit);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    public boolean deleteString(String key) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            return bucket.delete();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    public void stop() throws Exception {
        try {
            redissonClient.shutdown();
            logger.info("Shutdown Redisson OK");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
