package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.*;

public class RPCClient {

    private final MessagingClient msgclient;
    private Connection connection;

    public RPCClient(String server, int port) {

        msgclient = new MessagingClient(server, port);
    }

    public void register(RPCStub remote) {
        remote.register(this);
    }

    public void connect() {

        // connect using the underlying messaging layer connection
        connection = msgclient.connect();
    }

    public void disconnect() {

        // disconnect/close the underlying messaging connection
        connection.close();
    }

    public byte[] call(byte[] rpcrequest) {

        byte[] rpcreply = null;
		
		/*
		
		Make a remote call on the RPC server by sending the RPC request message
		and receive an RPC reply message
		
		rpcrequest is the marshalled rpcrequest from the client-stub
		rpctreply is the rpcreply to be unmarshalled by the client-stub
		
		*/

        Message message = new Message(rpcrequest);

        connection.send(message);

        return connection.receive().getData();
    }
}