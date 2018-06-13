/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zcore.tokenpool;

/**
 *
 * @author tudhm
 */
public class TokenPool {

    private final String poolName;
    private final String appID;
    private final int MAX_TOKEN;
    private volatile int CURRENT_TOKEN;

    public TokenPool(String poolName, String appID, int numToken) {
        this.poolName = poolName;
        if (numToken < 0) {
            numToken = 0;
        }
        this.CURRENT_TOKEN = numToken;
        this.MAX_TOKEN = numToken;
        this.appID = appID;
    }

    public String getPoolName() {
        return poolName;
    }

    public String getAppID() {
        return appID;
    }

    public synchronized boolean getToken() {
        if (CURRENT_TOKEN > 0) {
            CURRENT_TOKEN--;
            return true;
        }
        return false;
    }

    public synchronized void returnToken() {
        if (CURRENT_TOKEN < MAX_TOKEN) {
            CURRENT_TOKEN++;
        }
    }

    public int getTotalToken() {
        return MAX_TOKEN;
    }

    public int getUsedToken() {
        return MAX_TOKEN - CURRENT_TOKEN;
    }
}
