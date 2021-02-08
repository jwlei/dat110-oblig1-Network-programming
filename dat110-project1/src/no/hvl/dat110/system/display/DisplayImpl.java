package no.hvl.dat110.system.display;

import no.hvl.dat110.TODO;
import no.hvl.dat110.rpc.RPCImpl;
import no.hvl.dat110.rpc.RPCUtils;

public class DisplayImpl implements RPCImpl {

    public void write(String message) {
        System.out.println("DISPLAY: " + message);
    }

    public byte[] invoke(byte[] request) {

        byte rpcid;

        // DONE
        // implement unmarshalling, call, and marshall for write RPC method
        // look at how this is done int he SensorImpl for the read method

        String message = RPCUtils.unmarshallString(request);

        write(message);

        rpcid = request[0];

        return RPCUtils.marshallString(rpcid, message);
    }
}    
