package no.hvl.dat110.messaging;

import no.hvl.dat110.TODO;

public class Message {

	private byte[] payload;

	public Message(byte[] payload) {
		this.payload = payload; // TODO: check for length within boundary
	}

	public Message() {
		super();
	}

	public byte[] getData() {
		return this.payload; 
	}

	public byte[] encapsulate() {
		
        byte[] encoded = new byte[128];

        // encapulate/encode the payload of this message in the
        // encoded byte array according to message format
        encoded[0] = (byte) payload.length;
        System.arraycopy(payload, 0, encoded, 1, payload.length);

        return encoded;
    }

	public void decapsulate(byte[] received) {

	        // decapsulate the data contained in the received byte array and store it
	        // in the payload of this message

	        int size = received[0];
	        payload = new byte[size];
	        System.arraycopy(received, 1, payload, 0, payload.length);
	}
}
