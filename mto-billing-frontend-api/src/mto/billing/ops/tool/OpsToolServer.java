/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.ops.tool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mto.billing.fe.entity.MbApiOutput;
import mto.billing.fe.main.Configuration;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.jooq.Record;
import zcore.controller.ApiOutput;
import zcore.controller.ApiServlet;
import zcore.utilities.CommonUtil;

/**
 *
 * @author rots
 */
public class OpsToolServer implements Runnable {
    private static final Lock LOCK = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(OpsToolServer.class);
    private Server server;
    private static OpsToolServer _instance = null;

    public static OpsToolServer getInstance() {
        if (_instance == null) {
            LOCK.lock();
            try {
                if (_instance == null) {
                    _instance = new OpsToolServer();
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
            connector.setHost(Configuration.OPS_TOOL_SERVER_HOST);
            connector.setPort(Configuration.OPS_TOOL_SERVER_PORT);
            connector.setIdleTimeout(12000);
            server.setConnectors(new Connector[]{connector});

            ServletHandler servletHandler = new ServletHandler();
            servletHandler.addServletWithMapping(OpsToolApiController.class, "/ops/tool/*");
            servletHandler.addServletWithMapping(DefaultApiController.class, "/*");

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{servletHandler, new DefaultHandler()});

            server.setHandler(handlers);

            logger.info("Jetty server starting...");
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Cannot start tool server: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static class OpsToolApiController extends ApiServlet {

        @Override
        protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
            try {
                String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
                switch (pathInfo) {
                    case "/getinfo/":
                    {
                        if (!checkValidParam(req, new String[]{"orderNumber"})
                                || !CommonUtil.isLong(req.getParameter("orderNumber"))) {
                            logger.warn("checkValidParam - OpsToolApiController: " + req.getParameterMap());
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.INVALID_DATA_INPUT, req);
                        }
                        long orderNumber = Long.parseLong(req.getParameter("orderNumber"));
                        Map<String,Object> result = OpsToolService.getInstance().getDataByOrderNumber(orderNumber);
                        if(null == result){
                            // Need define new error code
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.SERVER_ERROR, req);
                        }else{
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.SUCCESS, req, result);
                        }
                    }
                    case "/getaction/":
                    {
                         if (!checkValidParam(req, new String[]{"orderNumber"})
                                || !CommonUtil.isLong(req.getParameter("orderNumber"))) {
                            logger.warn("checkValidParam - OpsToolApiController: " + req.getParameterMap());
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.INVALID_DATA_INPUT, req);
                        }
                        long orderNumber = Long.parseLong(req.getParameter("orderNumber"));
                        List<Map<String,Object>> result = OpsToolService.getInstance().getActionDataByOrderNumber(orderNumber);
                        if(null == result){
                            // Need define new error code
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.SERVER_ERROR, req);
                        }else{
                            return new MbApiOutput(MbApiOutput.ERROR_CODE_API.SUCCESS, req, result);
                        }
                    }
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
