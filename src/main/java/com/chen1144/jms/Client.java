package com.chen1144.jms;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.function.BiConsumer;

public class Client {
    public int port;
    public Socket socket;
    public Vector<BiConsumer<String, String>> consumers;

    public Client(int port){
        this.port = port;
        this.consumers = new Vector<>();
    }

    public void start() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", port));
        new Thread(()->{
            try {
                InputStream reader = socket.getInputStream();
                Scanner scanner = new Scanner(reader);
                while (true){
                    String key = scanner.nextLine();
                    String value = scanner.nextLine();
                    consumers.forEach(consumer -> consumer.accept(key, value));
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void subscribe(String key) throws IOException{
        Base64Writer writer = new Base64Writer(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(Protocol.SUBSCRIBE);
        writer.write(key);
        writer.flush();
    }

    public void unsubscribe(String key) throws IOException{
        Base64Writer writer = new Base64Writer(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(Protocol.UNSUBSCRIBE);
        writer.write(key);
        writer.flush();
    }

    public void send(String key, String value) throws IOException{
        Base64Writer writer = new Base64Writer(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(Protocol.SEND);
        writer.write(key);
        writer.write(value);
        writer.flush();
    }
}
