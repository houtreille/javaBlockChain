package ebloodCoin.messaging;

import java.util.ArrayList;

public class MessageAddressPrivate extends MessageTextBroadcast {

	

	public static final long serialVersionUID = 1L;
	private ArrayList<KeyNamePair> addresses;
	
	public MessageAddressPrivate(ArrayList<KeyNamePair> addresses) {
		this.addresses = addresses;
	}
	
	
	@Override
	public int getMessageType() {
		return Message.ADDRESS_PRIVATE;
	}

	@Override
	public ArrayList<KeyNamePair> getMessageBody() {
		return addresses;
	}

	@Override
	public boolean isForBroadcast() {
		return false;
	}

}
