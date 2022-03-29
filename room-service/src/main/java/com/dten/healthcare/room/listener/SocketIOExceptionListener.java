package com.dten.healthcare.room.listener;

import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.NotSslRecordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.websocket.DecodeException;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/16
 * @Description:
 */
public class SocketIOExceptionListener extends ExceptionListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOExceptionListener.class);

    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        // Mask SSL related exceptions
        if ( e instanceof NotSslRecordException || e instanceof DecodeException
            || e.getCause() instanceof SSLException ) {
 //           logger.debug("SocketIOExceptionListener: Not secure connection attempt detected. IP = {}, cause = {}", ctx.channel().remoteAddress(), e.getMessage());
            ctx.channel().close();
            return true;
        }

        logger.info("SocketIOExceptionListener: exceptionCaught is called.");
        return super.exceptionCaught(ctx, e);
    }
}
