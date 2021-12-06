package ebloodCoin.messaging;

import java.io.Serializable;
import java.security.PublicKey;

public class KeyNamePair implements Serializable {

	public final static long serialVersionUID = 1L;
	private PublicKey key;
	

	private String name;
	
	public KeyNamePair(PublicKey key, String name) {
		this.key = key;
		this.name = name;
	}
	
	public PublicKey getKey() {
		return key;
	}

	public String getName() {
		return name;
	}
}
