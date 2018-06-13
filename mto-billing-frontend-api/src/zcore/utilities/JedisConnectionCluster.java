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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import redis.clients.jedis.HostAndPort;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisConnectionCluster {

    private static Logger logger = Logger.getLogger(JedisConnectionCluster.class.getName());
    private static final Lock createLock = new ReentrantLock();
    private static Map<String, JedisConnectionCluster> instances = new HashMap();
    private JedisCluster jedisCluster = null;

    public static JedisConnectionCluster getInstance(String redisClusterAddrs) {
        String key = redisClusterAddrs;
        if (!instances.containsKey(key)) {
            createLock.lock();
            try {
                if (!instances.containsKey(key)) {
                    instances.put(key, new JedisConnectionCluster(redisClusterAddrs));
                }
            } finally {
                createLock.unlock();
            }
        }
        return (JedisConnectionCluster) instances.get(key);
    }

    private JedisConnectionCluster(String redisClusterAddrs) {
        if (redisClusterAddrs == null || redisClusterAddrs.isEmpty()) {
            logger.error("Redis cluster hosts are not set");
            return;
        }

        logger.info("Creating Redis cluster connection with hosts: " + redisClusterAddrs);

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(30);
        config.setMaxWaitMillis(2000);
        Set<HostAndPort> jedisClusterNodes = new LinkedHashSet<HostAndPort>();

        for (String hostName : redisClusterAddrs.split(",[ ]*")) {
            String[] readHostParts = hostName.split(":");
            if ((readHostParts.length != 2) || !(readHostParts[1].matches("\\d+"))) {
                logger.error("Invalid host name set for redis cluster: " + hostName);
                continue;
            }

            logger.info(String.format("Adding host %s : %s", readHostParts[0], readHostParts[1]));
            jedisClusterNodes.add(new HostAndPort(readHostParts[0], Integer.parseInt(readHostParts[1])));
        }

        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes, 2000, 2000, 5, config);
        Map<String, JedisPool> cNodes = jedisCluster.getClusterNodes();

        logger.info("Jedis cluster connection created:");
        for (String nodeName : cNodes.keySet()) {
            logger.info("node: " + nodeName);
            Jedis jedis = cNodes.get(nodeName).getResource();
            logger.debug("Server Info: " + jedis.info());
        }
        this.jedisCluster = jedisCluster;
    }

    public JedisCluster getJedisCluster() {
        return this.jedisCluster;
    }

}
