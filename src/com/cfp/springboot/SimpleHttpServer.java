package com.cfp.springboot;


import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;


public class SimpleHttpServer {
    public static int DEFAULT_PORT = 1100;
    public static int port;
    private HttpServer httpServer;

    public static void main(String[] args) {
        SimpleHttpServer httpServer = new SimpleHttpServer();
        httpServer.start(DEFAULT_PORT);
    }

    private void start(int port) {
        this.port = port;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("Server running at " + port);

            httpServer.createContext("/", (HttpHandler) new Handler.RootHandler());
            httpServer.createContext("/echoHeader", (HttpHandler) new Handler.EchoHeaderHandler());
            httpServer.createContext("/echoGet", (HttpHandler) new Handler.EchoGetHandler());
            httpServer.createContext("/echoPost", (HttpHandler) new Handler.EchoPostHandler());

            httpServer.setExecutor(null);
            httpServer.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}