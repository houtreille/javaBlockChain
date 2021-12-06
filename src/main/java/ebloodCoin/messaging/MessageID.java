package ebloodCoin.messaging;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

import ebloodCoin.utils.SecurityUtils;

public class MessageID extends MessageSigned implements Serializable {

	

	private final static long serialVersionUID = 1L;
	private String info = null;
	private byte[] signature = null;
	private PublicKey sender = null;
	private String name = null;
	
	



	public MessageID(PrivateKey pk, PublicKey sender, String name) {
		this.info = Message.JCOIN_MESSAGE;
		try {
			signature = SecurityUtils.signTransaction(pk, this.info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.sender = sender;
		this.name = name;
	}
	
	
	
//	@Override
//	public boolean isValid() {
//		try {
//			return SecurityUtils.verifySignature(sender, signature, this.info);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(this.getSenderKey(), this.name);
	}

	@Override
	public int getMessageType() {
		return Message.ID;
	}

	@Override
	public String getMessageBody() {
		return this.info;
	}

	@Override
	public boolean isForBroadcast() {
		return false;
	}

	
	public String getName() {
		return name;
	}
	
	@Override
	public byte[] getSignature() {
		// TODO Auto-generated method stub
		return signature;
	}

	@Override
	public PublicKey getSenderKey() {
		return sender;
	}
	
}
