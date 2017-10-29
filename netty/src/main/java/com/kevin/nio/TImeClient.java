package com.kevin.nio;

/**
 * @author ZGJ
 * create on 2017/10/29 16:02
 **/
public class TImeClient {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 8080;

        new Thread(new TimeClientHandle(ip, port), "TimeClient-001").start();
    }
}
