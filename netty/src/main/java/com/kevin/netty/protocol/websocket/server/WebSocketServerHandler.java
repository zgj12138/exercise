package com.kevin.netty.protocol.websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ZGJ
 * create on 2017/12/30 22:03
 **/
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
