package com.kevin.netty.bio;

import java.io.*;
import java.net.Socket;

/**
 * @author ZGJ
 * create on 2017/10/28 22:26
 **/
public class TImeClient {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 8080;

        try(Socket socket = new Socket(address, port);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true)) {
            printWriter.println("QUERY TIME ORDER");
            System.out.println("send order to server succeed");
            String resp = bufferedReader.readLine();
            System.out.println("Now is : " + resp);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
