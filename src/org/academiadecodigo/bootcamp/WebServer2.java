package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer2 {

    private static int portNumber = 8080;
    private Socket clientSocket;

    private PrintWriter out;


    public WebServer2() {

        startConnection();
    }

    public void startConnection() {
        try {

            ServerSocket serverSocket = new ServerSocket(portNumber);

            while (true) {
                System.out.println("Listening...");
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                Thread thread = new Thread((new ClientDispatch(clientSocket, out)));
                thread.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


