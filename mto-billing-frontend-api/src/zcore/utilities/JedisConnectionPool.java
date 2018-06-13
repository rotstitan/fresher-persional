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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisConnectionPool {

    private static Logger logger = Logger.getLogger(JedisConnectionPool.class.getName());
    private static final Lock createLock = new ReentrantLock();
    private static Map<String, JedisConnectionPool> instances = new HashMap();
    static JedisPool jedisPool;

    public static JedisConnectionPool getInstance(String host, int port, int timeout, String password) {
        String key = host + String.valueOf(port) + password;
        if (!instances.containsKey(key)) {
            createLock.lock();
            try {
                if (!instances.containsKey(key)) {
                    instances.put(key, new JedisConnectionPool(host, port, timeout, password));
                }
            } finally {
                createLock.unlock();
            }
        }
        return (JedisConnectionPool) instances.get(key);
    }

    private JedisConnectionPool(String host, int port, int timeout, String password) {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(10);
            config.setMaxIdle(10);
            config.setMinIdle(1);
            config.setMaxWaitMillis(30000);

            jedisPool = new JedisPool(config, host, port, timeout, password);
        } catch (JedisConnectionException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }

    public void returnResource(Jedis resource) {
        if (jedisPool != null) {
            try {
                if (resource != null) {
                    jedisPool.returnResource(resource);
                    resource = null;
                }
            } catch (JedisConnectionException e) {
                jedisPool.returnBrokenResource(resource);
                resource = null;
            } finally {
                if (resource != null) {
                    jedisPool.returnResource(resource);
                }
                resource = null;
            }
        }
    }
}
