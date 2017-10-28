package com.kevin.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞式IO时间服务器
 * @author ZGJ
 * create on 2017/10/28 21:59
 **/
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        ServerSocket serverSocket = null;
        try{
            //创建ServerSocket
            serverSocket = new ServerSocket(port);
            System.out.println("The time server is start in port: " + port);
            Socket socket = null;
            while (true) {
                //调用accept方法，该方法将一直阻塞，直到有客户端连接
                socket = serverSocket.accept();
                //客户端连接
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket != null) {
                System.out.println("The time server close");
                serverSocket.close();
                serverSocket = null;
            }
        }

    }
}
