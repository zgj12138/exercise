package com.kevin.aio;

/**
 * aio客户端
 * @author ZGJ
 * create on 2017/10/29 21:44
 **/
public class TimeClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;

        new Thread(new AsyncTimeClientHandler(host, port), "AIO-AsyncTimeClientHandler-001")
                .start();
    }
}
