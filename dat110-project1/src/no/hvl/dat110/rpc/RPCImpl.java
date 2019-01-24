package no.hvl.dat110.rpc;

// server-side stubs must implement this interface

public interface RPCImpl {
	
	public byte[] invoke(byte[] request);
	
}
