/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.main;

import mto.billing.ops.tool.OpsToolServer;
import org.apache.log4j.Logger;
import zcore.config.LogUtil;

/**
 *
 * @author rots
 */
public class ServiceDaemon {

    private static final Logger logger = Logger.getLogger(ServiceDaemon.class);

    public static void main(String[] args) {
        try {
            // init config
            LogUtil.init();
            Configuration.init();

            OpsToolServer opsToolServer = OpsToolServer.getInstance();
            new Thread(opsToolServer).start();
            //OpsToolService.ParseLogData("data/test/billing_ff_action_log_example");

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("Shutdown thread before webserver getinstance");
                        if (opsToolServer != null) {
                            opsToolServer.stop();
                        }

                    } catch (Exception e) {
                        logger.info("Shutdown exception: " + e.getMessage(), e);
                    }
                }
            }, "Stop Jetty Hook"));

        } catch (Throwable e) {
            String msg = "Exception encountered during startup.";
            logger.error(msg, e);
            logger.error("Uncaught exception: " + e.getMessage());
            System.exit(3);
        }
    }
}
