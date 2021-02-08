package no.hvl.dat110.system.controller;

import no.hvl.dat110.TODO;
import no.hvl.dat110.rpc.*;

public class Sensor extends RPCStub {

	private byte RPCID = 1;
	
	public int read() {
		// DONE
		// implement marshalling, call and unmarshalling for read RPC method
		int temp;
		
        byte[] request = RPCUtils.marshallVoid(RPCID);
        byte[] response = rpcclient.call(request);

        temp = RPCUtils.unmarshallInteger(response);

        return temp;
	}
	
}
