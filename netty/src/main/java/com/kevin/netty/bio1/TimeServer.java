package com.kevin.netty.bio1;

import com.kevin.netty.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 伪异步IO,采用线程池
 * @author ZGJ
 * create on 2017/10/28 22:39
 **/
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("The time server is start in port: " + port);
            Socket socket = null;
            //创建IO任务线程池
            TimeServerHandlerExecutePool pool = new TimeServerHandlerExecutePool(50, 10000);
            while (true) {
                socket = serverSocket.accept();
                pool.execute(new TimeServerHandler(socket));
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("The time server close");
        }
    }
}
