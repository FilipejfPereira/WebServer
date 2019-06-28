package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
    private ServerSocket server;
    private String [] aS;
    private PrintWriter out;
    private DataOutputStream datOut;
    private Socket clientSocket;
    private int numberOfThreads = 1;


    public WebServer() {
        try {
            server = new ServerSocket(4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accept(){
        try {
            clientSocket = server.accept();
            while(clientSocket.isConnected()) {
                if (clientSocket.getInputStream().read() != -1) {
                    numberOfThreads++;
                    System.out.println("threads count: " + numberOfThreads);

                    new Thread(new ThreadServer(clientSocket)).run();
                    clientSocket = server.accept();
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void start() {

        try {


            while (true) {


                InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());

                BufferedReader reader = new BufferedReader(isr);
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                datOut = new DataOutputStream(clientSocket.getOutputStream());
                String line = reader.readLine();
                if( line == null){
                    start();
                }
                aS = line.split(" ");


                while (!line.isEmpty()) {

                    System.out.println(line);

                    line = reader.readLine();
                }


                File filepath = new File("www/" +aS[1]);

                if (filepath.exists()) {

                    if(aS[1].equals("/")){
                        filepath = new File("www/index.html");
                    }

                    webPage(filepath);


                }else{

                    error();

                }
            }
    } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void error(){
        try {
            File file = new File("www/404.html");
            FileInputStream inputStream = new FileInputStream(file);

            BufferedReader htmlReader = new BufferedReader(new InputStreamReader(inputStream));

            String html;
            String htmlSended = "";
            while ((html = htmlReader.readLine()) != null) {
                htmlSended += (html);
            }


            long length = file.length();


            out.println("HTTP/1.0 404 Not Found");
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("Content-Length: " + length);
            out.println();
            out.print(htmlSended);
            datOut.writeBytes(htmlSended);

            out.flush();
            datOut.flush();
            datOut.close();
            htmlReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void webPage(File file){
        try {

            FileInputStream inputStream = new FileInputStream(file);

            BufferedReader htmlReader = new BufferedReader(new InputStreamReader(inputStream));
            String html;
            String htmlSended = "";
            while ((html = htmlReader.readLine()) != null) {
                htmlSended += (html);
            }



            long length = file.length();




            out.println("HTTP/1.0 200 Document Follows");
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("Content-Length: " + length);
            out.println();
            out.print(htmlSended);
            datOut.writeBytes(htmlSended);

            out.flush();
            datOut.flush();
            datOut.close();
            htmlReader.close();
            System.out.println("it reached response");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



        private class ThreadServer implements Runnable{
            private Socket clientSocket;

            public ThreadServer(Socket clientSocket){
                this.clientSocket = clientSocket;
            }
            @Override
            public void run() {

                start();

            }
        }

}





