package com.kevin.netty.protocol.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author ZGJ
 * create on 2017/12/30 21:52
 **/
public class WebSocketServer {

    private void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            //HTTP请求应答解码器
                            channelPipeline.addLast("http-codec", new HttpServerCodec());
                            //将HTTP消息的多个部分组合成一条完整的HTTP消息
                            channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            //WebSocket处理器
                            channelPipeline.addLast("handler", new WebSocketServerHandler());
                        }
                    });

            Channel ch = serverBootstrap.bind(port).sync().channel();
            System.out.println("WebSocket server start at port: " + port);
            System.out.println("Open your browser and navigate to http://localhost:" + port + "/");
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new WebSocketServer().run(port);
    }
}
