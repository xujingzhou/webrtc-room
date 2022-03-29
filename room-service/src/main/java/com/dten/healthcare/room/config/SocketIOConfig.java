package com.dten.healthcare.room.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.dten.healthcare.room.listener.SocketIOExceptionListener;
import java.io.InputStream;
import com.dten.healthcare.room.common.constant.VersionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
@Configuration
public class SocketIOConfig {

    private static final Logger logger = LoggerFactory.getLogger(SocketIOConfig.class);

    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    @Value("${socketio.bossCount}")
    private int bossCount;

    @Value("${socketio.workCount}")
    private int workCount;

    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socketio.upgradeTimeout}")
    private int upgradeTimeout;

    @Value("${socketio.pingTimeout}")
    private int pingTimeout;

    @Value("${socketio.pingInterval}")
    private int pingInterval;

    @Value("${socketio.keyStore}")
    private String keyStore;

    @Value("${socketio.keyStorePassword}")
    private String keyStorePassword;

    @Value("${spring.profiles.active}")
    private String active;

    public SocketIOServer getServer() {
        return socketIOServer();
    }

    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        socketConfig.setReuseAddress(true);
        socketConfig.setTcpKeepAlive(true);

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);
        config.setOrigin("*");
//        config.setRandomSession(true);

        // certification
        if (!StrUtil.equals(active, "local")) {
            logger.info("spring.profiles.active = {}", active);
            config.setExceptionListener(new SocketIOExceptionListener());
            config.setSSLProtocol("TLSv1.2");
            InputStream resourceAsStream = this.getClass().getResourceAsStream(keyStore);
            config.setKeyStore(resourceAsStream);
            config.setKeyStorePassword(keyStorePassword);
        }

        config.setAuthorizationListener(data -> {
            return true;
        });

        logger.info("SocketIOConfig = {}",  JSON.toJSONString(config));

        // 根据namespace区分版本兼容
        SocketIOServer server = new SocketIOServer(config);
        for (VersionEnum version : VersionEnum.values()) {
            server.addNamespace(version.getName());
        }
        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer server) {
        return new SpringAnnotationScanner(server);
    }
}
