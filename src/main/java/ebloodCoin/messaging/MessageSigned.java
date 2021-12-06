package ebloodCoin.messaging;

import java.security.PublicKey;

import ebloodCoin.utils.SecurityUtils;

public abstract class MessageSigned extends Message {
	private final static long serialVersionUID = 1L;
	
	public abstract byte[] getSignature();
	
	public abstract PublicKey getSenderKey();
	
	public boolean isValid() {
		try {
			return SecurityUtils.verifySignature(getSenderKey(), getSignature(), (String)getMessageBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
