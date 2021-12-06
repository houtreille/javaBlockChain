package ebloodCoin.messaging;

import java.security.PrivateKey;
import java.security.PublicKey;

public class MessageAskForBlokchainBroadcast extends MessageTextBroadcast {

	

	public MessageAskForBlokchainBroadcast(String info, PrivateKey key, PublicKey pubKey, String name,
			PublicKey receverKey) {
		super(info, key, pubKey, name, receverKey);
	}


	public static final long serialVersionUID = 1L;
	
	
	@Override
	public int getMessageType() {
		return Message.BLOCKCHAIN_ASK_BROADCAST;
	}


}
