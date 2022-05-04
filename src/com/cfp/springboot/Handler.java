package com.cfp.springboot;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

//import com.sun.xml.internal.ws.api.message.Headers;
//import javax.xml.ws.spi.http.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.cfp.springboot.Handler.EchoPostHandler.parseQuery;


public class Handler {
    public static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Server started successfully at</h1>" + "<h1>Port: " + SimpleHttpServer.port + "</h1>";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }

    public static class EchoHeaderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Header header = (Header) exchange.getRequestHeaders();
            Set<? extends Map.Entry<?, ?>> entries = ((Map<?, ?>) header).entrySet();
            String response = "ECHO HEADER";
            for (Map.Entry<?, ?> entry : entries)
                response += entry.toString() + "\n";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }
    public static class EchoGetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> Parameters = new HashMap<>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, Parameters);
            String response = "ECHO GET";
            for (String key : Parameters.keySet())
                response += key + " = " + Parameters.get(key) + "\n";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static class EchoPostHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> Parameters = new HashMap<>();
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, Parameters);
            String response = "ECHO POST";
            parseQuery(query, Parameters);
            for (String key : Parameters.keySet())
                response += key + "=" + Parameters.get(key) + "\n";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        static void parseQuery(String query, Map<String, Object> Parameters) throws UnsupportedEncodingException {
            if (query != null) {
                String pairs[] = query.split("[&]");
                for (String pair : pairs) {
                    String param[] = pair.split("[=]");
                    String key = null;
                    String value = null;
                    if (param.length > 0) {
                        key = URLDecoder.decode((param[0]), System.getProperty("file.encoding"));
                    }
                    if (param.length > 1) {
                        key = URLDecoder.decode((param[1]), System.getProperty("file.encoding"));
                    }
                    Parameters.put(key, value);
                }
            }
        }
    }
}


