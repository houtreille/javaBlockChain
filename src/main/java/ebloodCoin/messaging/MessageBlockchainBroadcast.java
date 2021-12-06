package ebloodCoin.messaging;

import java.security.PublicKey;

import ebloodCoin.blockchain.Blockchain;
import ebloodCoin.blockchain.transaction.Transaction;

public class MessageBlockchainBroadcast extends Message {

	private static final long serialVersionUID = 1L;
	
	private Blockchain ledger = null;
	

	private PublicKey sender = null;
	private int initialSize = 0;
	
	public MessageBlockchainBroadcast(Blockchain ledger, PublicKey sender) {
		super();
		this.ledger = ledger;
		this.sender = sender;
		this.initialSize = ledger.getSize();
	}

	@Override
	public int getMessageType() {
		return Message.BLOCKCHAIN_BROADCAST;
	}

	public int getInfoSize() {
		return initialSize;
	}

	@Override
	public boolean isForBroadcast() {
		return false;
	}
	
	public PublicKey getSender() {
		return sender;
	}

	@Override
	public Object getMessageBody() {
		return ledger;
	}

}
