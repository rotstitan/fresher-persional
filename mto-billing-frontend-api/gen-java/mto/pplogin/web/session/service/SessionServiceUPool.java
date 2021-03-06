/**
 * Autogenerated by Thrift Compiler (0.9.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package mto.pplogin.web.session.service;

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

public class SessionServiceUPool {

  public ZAstyanaxContext context;
  public static final int NUM_RETRIES = 2;
  public int maxCons = 400;
  public int maxConsPerHost = 200;
  public int initConns = 5;
  public int timeout = 1000;
  private static final Object lock = new Object();
  private static Map<String, SessionServiceUPool> instances = new HashMap<String, SessionServiceUPool>();
  private static String mapCode(String serviceName, String masterHostPort, String slaveHostPort) {
    return serviceName + masterHostPort + slaveHostPort;
  }

  public static SessionServiceUPool getInstance(String serviceName, String masterHostPort, String slaveHostPort) {
    return getInstance(serviceName, masterHostPort, slaveHostPort, 0, 0, 0, 0);
  }
  public static SessionServiceUPool getInstance(String serviceName, String masterHostPort, String slaveHostPort,int maxCons, int maxConsPerHost, int initConns, int timeout) {
    String key = mapCode(serviceName, masterHostPort, slaveHostPort);
    if (!instances.containsKey(key)) {
      synchronized(lock) {
        if (!instances.containsKey(key)) {
          instances.put(key, new SessionServiceUPool(serviceName, masterHostPort, slaveHostPort, maxCons, maxConsPerHost, initConns, timeout));
        }
      }
    }
    return instances.get(mapCode(serviceName, masterHostPort, slaveHostPort));
  }
  public SessionServiceUPool() {}
  public SessionServiceUPool(String serviceName, String masterHostPort, String slaveHostPort, int maxCons, int maxConsPerHost, int initConns, int timeout) {
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
    SessionService.Client.Factory factory = new SessionService.Client.Factory();
    Host master = new Host(masterHostPort, 88888);
    Host slave = new Host(slaveHostPort, 88888);
    context = new ZAstyanaxContext(factory, connectionPoolConfig, serviceName, master, slave, retryPolicy);
  }

  public synchronized  void changeConfig(String serviceName, String oldMasterHostPort,String oldSlaveHostPort, String masterHostPort, String slaveHostPort) {
    instances.put(mapCode(serviceName, masterHostPort, slaveHostPort),new SessionServiceUPool(serviceName, masterHostPort, slaveHostPort,maxCons, maxConsPerHost, initConns, timeout));
    instances.remove(mapCode(serviceName, oldMasterHostPort, oldSlaveHostPort));
  }

  public TCheckSessionRCode checkSession(final String gameID, final String sessionID, final String userID, final String userAgent) throws TException{
    Execution<TCheckSessionRCode> ex = new AbstractMasterSlaveSyncExecution<TCheckSessionRCode>() {
      @Override
      public OperationResult<TCheckSessionRCode> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected TCheckSessionRCode internalExecute(TServiceClient client) throws Exception {
            return (TCheckSessionRCode) ((SessionService.Iface) client).checkSession(gameID, sessionID, userID, userAgent);
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (TCheckSessionRCode) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

  public TCheckSessionRCode removeSession(final String gameID, final String sessionID, final String userID) throws TException{
    Execution<TCheckSessionRCode> ex = new AbstractMasterSlaveSyncExecution<TCheckSessionRCode>() {
      @Override
      public OperationResult<TCheckSessionRCode> execute() throws ConnectionException {
        return context.getConnectionPool().executeWithFailover(new AbstractOperationImpl() {
          @Override
          protected TCheckSessionRCode internalExecute(TServiceClient client) throws Exception {
            return (TCheckSessionRCode) ((SessionService.Iface) client).removeSession(gameID, sessionID, userID);
          }
        }, context.getRetryPolicy());
      }
    };
    try {
      return (TCheckSessionRCode) ex.execute().getResult();
    } catch (ConnectionException e) {
       throw new TApplicationException(e.getMessage());
    }
  }

}
