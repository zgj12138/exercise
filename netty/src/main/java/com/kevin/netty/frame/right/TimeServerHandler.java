package com.kevin.netty.frame.right;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author ZGJ
 * create on 2017/11/2 0:02
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //这里直接获得的就是String了，因为增加了StringDecoder进行了解码
        String body = (String) msg;
        System.out.println("The time server receive order : " + body
                + " ; the counter is : " + ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
                System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
