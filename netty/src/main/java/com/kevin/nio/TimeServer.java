package com.kevin.nio;

/**
 * nio time server
 * @author ZGJ
 * create on 2017/10/29 13:48
 **/
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
