package ebloodCoin.messaging;

import java.security.PrivateKey;
import java.security.PublicKey;

import ebloodCoin.utils.SecurityUtils;

public class MessageTextPrivate extends MessageSigned {

	public static final long serialVersionUID = 1L;
	
	private String info = null;

	private byte[] signature = null;
	private PublicKey senderKey = null;
	

	private String senderName = null;
	private PublicKey receverKey = null;

	
	public MessageTextPrivate(String info, PrivateKey privKey, PublicKey senderKey, String senderName,
			PublicKey receverKey) {
		super();
		this.info = info;
		try {
			this.signature = SecurityUtils.signTransaction(privKey, info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.senderKey = senderKey;
		this.senderName = senderName;
		this.receverKey = receverKey;
	}

	
	@Override
	public int getMessageType() {
		return Message.TEXT_PRIVATE;
	}

	@Override
	public String getMessageBody() {
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
		return senderKey;
	}
	
	public String getSenderName() {
		return senderName;
	}

	public PublicKey getReceverKey() {
		return receverKey;
	}
	
	public KeyNamePair getSenderKeyNamePair( ) {
		return new KeyNamePair(getSenderKey(), senderName);
	}

}
