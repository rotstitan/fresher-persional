/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.main;

import mto.billing.fe.cat1.controller.ConfigApiController;
import mto.billing.fe.cat1.controller.LoginApiController;
import mto.billing.fe.entity.MbApiOutput;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.RequestLogHandler;

import org.eclipse.jetty.servlet.ServletContextHandler;
import zcore.controller.ApiOutput;
import zcore.controller.ApiServlet;

/**
 *
 * @author tungdq
 */
public class WebServer implements Runnable {

    private static final Lock LOCK = new ReentrantLock();
    private static final Logger LOGGER = Logger.getLogger(WebServer.class);
    private Server server;
    private static WebServer _instance = null;

    public static WebServer getInstance() {
        if (_instance == null) {
            LOCK.lock();
            try {
                if (_instance == null) {
                    _instance = new WebServer();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return _instance;
    }

    @Override
    public void run() {
        try {
            server = new Server();

            // Connector
            final ServerConnector connector = new ServerConnector(server);
            connector.setHost(Configuration.WEB_SERVER_HOST);
            connector.setPort(Configuration.WEB_SERVER_PORT);
            connector.setIdleTimeout(12000);
            server.setConnectors(new Connector[]{connector});

            // Context Handler
            final ServletContextHandler context = new ServletContextHandler();
            server.setHandler(context);
            
          
            context.addServlet(LoginApiController.class, "/fe/api/auth/*");
            context.addServlet(ConfigApiController.class, "/fe/api/config/*");
            context.addServlet(DefaultApiController.class, "/*");
            LOGGER.info("Jetty server starting...");
            server.start();
            server.join();
        } catch (Exception e) {
            LOGGER.error("Cannot start web server: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static class DefaultApiController extends ApiServlet {

        @Override
        protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.UNSUPPORTED_ERROR, req);
        }

    }

}
