package com.dten.healthcare.room.handler;

import cn.hutool.core.util.ObjectUtil;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.dten.healthcare.room.common.constant.VersionEnum;
import com.dten.healthcare.room.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/8/30
 * @Description:
 */
@Component
@Order(1)
public class SocketIOServerRunner implements CommandLineRunner{
	private static final Logger logger = LoggerFactory.getLogger(SocketIOServerRunner.class);

	@Autowired
	private final SocketIOServer socketIOServer;

	@Autowired
	public SocketIOServerRunner(SocketIOServer socketServer) {
		this.socketIOServer = socketServer;
	}

	@Override
	public void run(String... args) throws Exception {
		socketIOServer.start();
		logger.info("---------- NettySocketIO started ----------");
	}
}
