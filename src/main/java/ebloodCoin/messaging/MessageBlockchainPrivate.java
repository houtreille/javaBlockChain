package ebloodCoin.messaging;

import java.security.PublicKey;

import ebloodCoin.blockchain.Blockchain;
import ebloodCoin.blockchain.transaction.Transaction;

public class MessageBlockchainPrivate extends Message {

	private static final long serialVersionUID = 1L;
	
	private Blockchain ledger = null;
	

	private PublicKey sender = null;
	private PublicKey receiver = null;
	private int initialSize = 0;
	
	public MessageBlockchainPrivate(Blockchain ledger, PublicKey sender, PublicKey receiver) {
		super();
		this.ledger = ledger;
		this.sender = sender;
		this.receiver = receiver;
		this.initialSize = ledger.getSize();
	}

	@Override
	public int getMessageType() {
		return Message.BLOCKCHAIN_PRIVATE;
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

	public PublicKey getReceiver() {
		return receiver;
	}

	@Override
	public Object getMessageBody() {
		return ledger;
	}

}
