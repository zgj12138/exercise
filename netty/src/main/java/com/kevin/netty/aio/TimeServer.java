package com.kevin.netty.aio;

/**
 * AIO异步时间服务器
 * @author ZGJ
 * create on 2017/10/29 17:38
 **/
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;

        AsyncTimeServerHandler timeServerHandle = new AsyncTimeServerHandler(port);
        new Thread(timeServerHandle, "AIO-AsyncTimeServer-001").start();
    }
}
