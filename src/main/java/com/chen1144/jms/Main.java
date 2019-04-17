package com.chen1144.jms;

public class Main {
    public static void main(String[] args) throws Exception{
        MessageQueue messageQueue = new MessageQueue(8081);
        Client client = new Client(8081);
        Client client1 = new Client(8081);
        client1.consumers.add((key, value)->{
            System.out.println(key);
            System.out.println(value);
            System.out.flush();
        });
        messageQueue.start();
        client.start();
        client1.start();
        client1.subscribe("KE");
        client.send("KE2", "SOMEVALUE");
        client.send("KE3", "SOMEVALUE");
        client.send("KE4", "SOMEVALUE");
        client.send("KE5", "SOMEVALUE");
        client.send("KE", "SOMEVALUE");
//        client.send("KE", "SOMEVALUE");
    }
}
