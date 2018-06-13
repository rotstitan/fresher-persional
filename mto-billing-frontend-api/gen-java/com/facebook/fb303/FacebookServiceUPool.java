/**
 * Autogenerated by Thrift Compiler (0.9.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.facebook.fb303;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.astyanax.Execution;
import com.netflix.astyanax.connectionpool.Host;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.retry.RetryNTimes;
import com.netflix.astyanax.retry.RetryPolicy;
import org.apache.thrift.*;
import vn.zing.zastyanax.*;

public class FacebookServiceUPool {

  public ZAstyanaxContext context;
  public static final int NUM_RETRIES = 2;
  public int maxCons = 400;
  public int maxConsPerHost = 200;
  public int initConns = 5;
  public int timeout = 1000;
  private static final Object lock = new Object();
  private static Map<String, FacebookServiceUPool> instances = new HashMap<String, FacebookServiceUPool>();
  private static String mapCode(String serviceName, String masterHostPort, String slaveHostPort) {
    return serviceName + masterHostPort + slaveHostPort;
  }

  public static FacebookServiceUPool getInstance(String serviceName, String masterHostPort, String slaveHostPort) {
    return getInstance(serviceName, masterHostPort, slaveHostPort, 0, 0, 0, 0);
  }
  public static FacebookServiceUPool getInstance(String serviceName, String masterHostPort, String slaveHostPort,int maxCons, int maxConsPerHost, int initConns, int timeout) {
    String key = mapCode(serviceName, masterHostPort, slaveHostPort);
    if (!instances.containsKey(key)) {
      synchronized(lock) {
        if (!instances.containsKey(key)) {
          instances.put(key, new FacebookServiceUPool(serviceName, masterHostPort, slaveHostPort, maxCons, maxConsPerHost, initConns, timeout));
        }
      }
    }
    return instances.get(mapCode(serviceName, masterHostPort, slaveHostPort));
  }
  public FacebookServiceUPool() {}
  public FacebookServiceUPool(String serviceName, String masterHostPort, String slaveHostPort, int maxCons, int maxConsPerHost, int initConns, int timeout) {
    if (maxCons > 0) {
      this.maxCons = maxCons;
    }
    if (maxConsPerHost > 0) {
      this.maxConsPerHost = maxConsPerHost;
    }
    if (initConns > 0) {
      this.initConns = initConns;
    }
    if (timeout > 0) {
      this.timeout = timeout;
    }
    ConnectionPoolConfigurationImpl connectionPoolConfig = new ConnectionPoolConfigurationImpl(serviceName + "Pool")
      .setMaxConns(this.maxCons)
      .setMaxConnsPerHost(this.maxConsPerHost)
      .setInitConnsPerHost(this.initConns)
      .setConnectTimeout(this.timeout)
      .setMaxFailoverCount(NUM_RETRIES);
    RetryPolicy retryPolicy = new RetryNTimes(NUM_RETRIES);
    FacebookService.Client.Factory factory = new FacebookService.Client.Factory();
    Host master = new Host(masterHostPort, 88888);
    Host slave = new Host(slaveHostPort, 88888);
    context = new ZAstyanaxContext(factory, connectionPoolConfig, serviceName, master, slave, retryPolicy);
  }

  public synchronized  void changeConfig(String serviceName, String oldMasterHostPort,String oldSlaveHostPort, String masterHostPort, String slaveHostPort) {
    instances.put(mapCode(serviceName, masterHostPort, slaveHostPort),new FacebookServiceUPool(serviceName, masterHostPort, slaveHostPort,maxCons, maxConsPerHost, initConns, timeout));
    instances.remove(mapCode(serviceName, oldMasterHostPort, oldSlaveHostPort));
  }

  /**
   * Returns a descriptive name of the service
   */
  public String getName() throws TException{
    Execution<String> ex = new AbstractMasterSlaveSyncExecution<String>() {
      @Override
      public OperationResult<String> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected String internalExecute(TServiceClient client) throws Exception {
            return (String) ((FacebookService.Iface) client).getName();
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (String) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Returns the version of the service
   */
  public String getVersion() throws TException{
    Execution<String> ex = new AbstractMasterSlaveSyncExecution<String>() {
      @Override
      public OperationResult<String> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected String internalExecute(TServiceClient client) throws Exception {
            return (String) ((FacebookService.Iface) client).getVersion();
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (String) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Gets the status of this service
   */
  public fb_status getStatus() throws TException{
    Execution<fb_status> ex = new AbstractMasterSlaveSyncExecution<fb_status>() {
      @Override
      public OperationResult<fb_status> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected fb_status internalExecute(TServiceClient client) throws Exception {
            return (fb_status) ((FacebookService.Iface) client).getStatus();
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (fb_status) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * User friendly description of status, such as why the service is in
   * the dead or warning state, or what is being started or stopped.
   */
  public String getStatusDetails() throws TException{
    Execution<String> ex = new AbstractMasterSlaveSyncExecution<String>() {
      @Override
      public OperationResult<String> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected String internalExecute(TServiceClient client) throws Exception {
            return (String) ((FacebookService.Iface) client).getStatusDetails();
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (String) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Gets the counters for this service
   */
  public Map<String,Long> getCounters() throws TException{
    Execution<Map<String,Long>> ex = new AbstractMasterSlaveSyncExecution<Map<String,Long>>() {
      @Override
      public OperationResult<Map<String,Long>> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected Map<String,Long> internalExecute(TServiceClient client) throws Exception {
            return (Map<String,Long>) ((FacebookService.Iface) client).getCounters();
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (Map<String,Long>) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Gets the value of a single counter
   * 
   * @param key
   */
  public long getCounter(final String key) throws TException{
    Execution<Long> ex = new AbstractMasterSlaveSyncExecution<Long>() {
      @Override
      public OperationResult<Long> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected Long internalExecute(TServiceClient client) throws Exception {
            return (Long) ((FacebookService.Iface) client).getCounter(key);
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (Long) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Sets an option
   * 
   * @param key
   * @param value
   */
  public void setOption(final String key, final String value) throws TException{
    Execution<Void> ex = new AbstractMasterSlaveSyncExecution<Void>() {
      @Override
      public OperationResult<Void> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected Void internalExecute(TServiceClient client) throws Exception {
            ((FacebookService.Iface) client).setOption(key, value);
            return null;
          }
        }, context.getRetryPolicy());
      }
    };
    try {
       ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Gets an option
   * 
   * @param key
   */
  public String getOption(final String key) throws TException{
    Execution<String> ex = new AbstractMasterSlaveSyncExecution<String>() {
      @Override
      public OperationResult<String> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected String internalExecute(TServiceClient client) throws Exception {
            return (String) ((FacebookService.Iface) client).getOption(key);
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (String) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Gets all options
   */
  public Map<String,String> getOptions() throws TException{
    Execution<Map<String,String>> ex = new AbstractMasterSlaveSyncExecution<Map<String,String>>() {
      @Override
      public OperationResult<Map<String,String>> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected Map<String,String> internalExecute(TServiceClient client) throws Exception {
            return (Map<String,String>) ((FacebookService.Iface) client).getOptions();
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (Map<String,String>) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Returns a CPU profile over the given time interval (client and server
   * must agree on the profile format).
   * 
   * @param profileDurationInSec
   */
  public String getCpuProfile(final int profileDurationInSec) throws TException{
    Execution<String> ex = new AbstractMasterSlaveSyncExecution<String>() {
      @Override
      public OperationResult<String> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected String internalExecute(TServiceClient client) throws Exception {
            return (String) ((FacebookService.Iface) client).getCpuProfile(profileDurationInSec);
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (String) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Returns the unix time that the server has been running since
   */
  public long aliveSince() throws TException{
    Execution<Long> ex = new AbstractMasterSlaveSyncExecution<Long>() {
      @Override
      public OperationResult<Long> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected Long internalExecute(TServiceClient client) throws Exception {
            return (Long) ((FacebookService.Iface) client).aliveSince();
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (Long) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Tell the server to reload its configuration, reopen log files, etc
   */
  public void reinitialize() throws TException{
    Execution<Void> ex = new AbstractMasterSlaveSyncExecution<Void>() {
      @Override
      public OperationResult<Void> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected Void internalExecute(TServiceClient client) throws Exception {
            ((FacebookService.Iface) client).reinitialize();
            return null;
          }
        }, context.getRetryPolicy());
      }
    };
    try {
       ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  /**
   * Suggest a shutdown to the server
   */
  public void shutdown() throws TException{
    Execution<Void> ex = new AbstractMasterSlaveSyncExecution<Void>() {
      @Override
      public OperationResult<Void> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected Void internalExecute(TServiceClient client) throws Exception {
            ((FacebookService.Iface) client).shutdown();
            return null;
          }
        }, context.getRetryPolicy());
      }
    };
    try {
       ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

}
