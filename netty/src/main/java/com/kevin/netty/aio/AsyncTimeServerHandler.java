package com.kevin.netty.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author ZGJ
 * create on 2017/10/29 17:38
 **/
public class AsyncTimeServerHandler implements Runnable {

    private int port = 8080;

    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    CountDownLatch countDownLatch;

    public AsyncTimeServerHandler(int port) {
        this.port = port;

        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The async time server is start in port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        doAccept();
        try {
            /*它的作用：在完成一组正在执行的操作之前，允许当前的现场一直阻塞
            此处，让现场在此阻塞，防止服务端执行完成后退出
            也可以使用while(true)+sleep
            生成环境就不需要担心这个问题，因为服务端是不会退出的
            */
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doAccept() {
        asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}
