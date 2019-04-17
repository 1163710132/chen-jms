package com.chen1144.jms;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MessageQueue {
    public final int port;
    public ConcurrentHashMap<String, Vector<Socket>> map;

    public MessageQueue(int port){
        this.port = port;
    }

    public void start() throws IOException{
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost", port));
        map = new ConcurrentHashMap<>();
        new Thread(()->{
            try {
                while (true){
                    Socket socket = serverSocket.accept();
                    new Thread(()->{
                        try {
                            InputStream inputStream = socket.getInputStream();
                            Base64Reader reader = new Base64Reader(new InputStreamReader(socket.getInputStream()));
                            while (true){
                                String line = reader.read();
                                switch (line){
                                    case Protocol.SUBSCRIBE:
                                    {
                                        String key = reader.read();
                                        map.computeIfAbsent(key, __->new Vector<>())
                                                .add(socket);
                                        break;
                                    }
                                    case Protocol.UNSUBSCRIBE:
                                    {
                                        String key = reader.read();
                                        map.computeIfAbsent(key, __->new Vector<>())
                                                .remove(socket);
                                        break;
                                    }
                                    case Protocol.SEND:
                                    {
                                        String key = reader.read();
                                        String value = reader.read();
                                        map.computeIfAbsent(key, __->new Vector<>())
                                                .forEach(consumer -> {
                                                    try {
                                                        consumer.getOutputStream().write(key.getBytes());
                                                        consumer.getOutputStream().write('\n');
                                                        consumer.getOutputStream().write(value.getBytes());
                                                        consumer.getOutputStream().write('\n');
                                                        consumer.getOutputStream().flush();
                                                    }catch (IOException e){
                                                        throw new RuntimeException(e);
                                                    }
                                                });
                                        break;
                                    }
                                }
                            }
                        }catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }).start();
    }
}
