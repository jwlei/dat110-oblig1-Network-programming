package no.hvl.dat110.rpc;

import java.util.HashMap;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Connection;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessagingServer;

public class RPCServer {



    private final MessagingServer msgserver;
    private Connection connection;

    // hashmap to register RPC methods which are required to implement RPCImpl

    private final HashMap<Integer, RPCImpl> services;

    public RPCServer(int port) {

        this.msgserver = new MessagingServer(port);
        this.services = new HashMap<>();

        // the stop RPC methods is built into the server
        services.put((int) RPCCommon.RPIDSTOP, new RPCServerStopImpl());
    }

    public void run() {

        System.out.println("RPC SERVER RUN - Services: " + services.size());

        connection = msgserver.accept();

        System.out.println("RPC SERVER ACCEPTED");

        boolean stop = false;

        while (!stop) {

            // - receive message containing RPC request
            Message message = connection.receive();
            // - find the identifier for the RPC methods to invoke
            byte[] byteIn = message.getData();
            int rpcid = byteIn[0];
            // - lookup the method to be invoked
            RPCImpl method = services.get(rpcid);
            // - invoke the method
            byte[] reply = method.invoke(message.getData());
            // - send back message containing RPC reply
            connection.send(new Message(reply));

            if (rpcid == RPCCommon.RPIDSTOP) {
                stop = true;
            }
        }

    }

    public void register(int rpcid, RPCImpl impl) {
        services.put(rpcid, impl);
    }

    public void stop() {
        connection.close();
        msgserver.stop();

    }
}