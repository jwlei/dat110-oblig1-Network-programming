package no.hvl.dat110.rpc;

// client-side stubs must extend this class

public abstract class RPCStub {

	protected RPCClient rmiclient;
	
	public void register(RPCClient rmiclient) {
		this.rmiclient = rmiclient;
	}
	
}
