package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;

    public class ClientDispatch implements Runnable {


        private Socket clientSocket;
        private String text="";
        private String line = "";
        private PrintWriter out;
        private BufferedReader bReader;


        public ClientDispatch(Socket clientSocket, PrintWriter out) {
            this.clientSocket = clientSocket;
            this.out = out;
        }


        @Override
        public void run() {

            try {
                request();
                System.out.println("Thread: " + Thread.currentThread().getName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void request() throws IOException {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            text = in.readLine();

            if (text != null) {


                System.out.println("Input: " + text);

                if (text.equals("GET / HTTP/1.1")) {
                    response();
                } else if (text.equals("GET /image HTTP/1.1")) {
                    image();
                } else {
                    error();
                }
            }

        }

        public void response() throws IOException{

            File responseFile = new File("./www/index.html");
            bReader = new BufferedReader(new FileReader("./www/index.html"));

            String httpResponse = "HTTP/1.0 200 Document Follows\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Length:" + responseFile.length() + "\r\n\r\n";
            out.println(httpResponse);

            while ((line = bReader.readLine()) != null) {
                out.println(line);

            }

        }

        public void error() throws  IOException {

            File errorFile = new File("./www/404.html");
            bReader = new BufferedReader(new FileReader("./www/404.html"));

            String httpResponseError = "HTTP/1.0 404 Not Found\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Length:" + errorFile.length() +"\r\n\r\n";
            out.println(httpResponseError);

            while ((line = bReader.readLine()) != null) {
                out.println(line);
            }
        }

        public void image() throws IOException {


            File imageFile = new File("./www/goku.jpeg");
            bReader = new BufferedReader(new FileReader("./www/goku.jpeg"));

            String httpResponseError = "HTTP/1.0 200 Document Follows\r\nContent-Type: image/jpeg\r\nContent-Length:"+ imageFile.length() +"\r\n\r\n";
            out.println(httpResponseError);

            while ((line = String.valueOf(bReader.lines())) != null) {
                out.print(line);
            }
        }


    }




