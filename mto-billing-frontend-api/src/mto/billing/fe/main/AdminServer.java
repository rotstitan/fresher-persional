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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mto.billing.config.MbConfigService;
import mto.billing.fe.entity.MbApiOutput;
import mto.billing.fe.entity.MbDeclaredEntity;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import zcore.controller.ApiOutput;
import zcore.controller.ApiServlet;
import zcore.utilities.CommonUtil;

/**
 *
 * @author huuloc.tran89
 */
public class AdminServer implements Runnable {

    private static final Lock LOCK = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(AdminServer.class);
    private Server server;
    private static AdminServer _instance = null;

    public static AdminServer getInstance() {
        if (_instance == null) {
            LOCK.lock();
            try {
                if (_instance == null) {
                    _instance = new AdminServer();
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
            // Thread 
            QueuedThreadPool threadPool = new QueuedThreadPool();
            threadPool.setMinThreads(100);
            threadPool.setMaxThreads(100);
            server = new Server(threadPool);

            // Connector
            ServerConnector connector = new ServerConnector(server);
            connector.setHost(Configuration.ADMIN_SERVER_HOST);
            connector.setPort(Configuration.ADMIN_SERVER_PORT);
            connector.setIdleTimeout(12000);
            server.setConnectors(new Connector[]{connector});

            ServletHandler servletHandler = new ServletHandler();
            servletHandler.addServletWithMapping(AdminApiController.class, "/fe/nimda/*");
            servletHandler.addServletWithMapping(DefaultApiController.class, "/*");

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{servletHandler, new DefaultHandler()});

            server.setHandler(handlers);

            logger.info("Jetty server starting...");
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Cannot start admin server: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static class AdminApiController extends ApiServlet {

        @Override
        protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
            try {
                String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
                switch (pathInfo) {
                    case "/reloadConfig":
                        if (!checkValidParam(req, new String[]{"clientID"})
                                || !CommonUtil.isInteger(req.getParameter("clientID"))) {
                            logger.warn("checkValidParam - AdminApiController: " + req.getParameterMap());
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.INVALID_DATA_INPUT, req);
                        }

                        int clientID = Integer.parseInt(req.getParameter("clientID"));
                        MbConfigService cs = MbConfigService.getInstance(Configuration.CONF_DOMAIN, Configuration.CONF_SECRET_KEY, Configuration.CONF_ENCRYPT_KEY);
                        if (cs.clearConfigCacheByID(clientID)) {
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.SUCCESS, req, cs.getConfigCacheByID(clientID));
                        }
                        return new MbApiOutput(MbApiOutput.ERROR_CODE_API.APP_CONFIG_ERROR, null);

                    default:
                        return new MbApiOutput(MbApiOutput.ERROR_CODE_API.UNSUPPORTED_ERROR, req);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.SERVER_ERROR, req);
        }

    }

    public static class DefaultApiController extends ApiServlet {

        @Override
        protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.UNSUPPORTED_ERROR, req);
        }

    }
}
