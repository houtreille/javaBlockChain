package ebloodCoin.messaging;

import java.security.PrivateKey;
import java.security.PublicKey;

import ebloodCoin.utils.SecurityUtils;

public class MessageTextBroadcast extends MessageSigned {

	public static final long serialVersionUID = 1L;
	
	private String info = null;
	private byte[] signature = null;
	private PublicKey pubKey = null;
	private String name = null;
	
	public MessageTextBroadcast() {
		
	}
	
	public MessageTextBroadcast(String info, PrivateKey key, PublicKey pubKey, String name,
			PublicKey receverKey) {
		super();
		this.info = info;
		try {
			this.signature = SecurityUtils.signTransaction(key, info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pubKey = pubKey;
		this.name = name;
		
	}
	
	@Override
	public int getMessageType() {
		return Message.TEXT_BROADCAST;
	}

	@Override
	public Object getMessageBody() {
		return this.info;
	}

	@Override
	public boolean isForBroadcast() {
		return false;
	}

	@Override
	public byte[] getSignature() {
		return signature;
	}

	@Override
	public PublicKey getSenderKey() {
		return pubKey;
	}
	
	public String getSenderName() {
		return name;
	}
	
	public KeyNamePair getSenderKeyNamePair( ) {
		return new KeyNamePair(getSenderKey(), name);
	}

}
