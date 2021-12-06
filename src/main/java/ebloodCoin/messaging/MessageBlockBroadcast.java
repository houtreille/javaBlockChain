package ebloodCoin.messaging;

import ebloodCoin.blockchain.TransactionBlockImproved;
import ebloodCoin.blockchain.transaction.Transaction;

public class MessageBlockBroadcast extends Message {

	private static final long serialVersionUID = 1L;
	
	private TransactionBlockImproved block = null;
	
	public MessageBlockBroadcast(TransactionBlockImproved block) {
		super();
		this.block = block;
	}

	@Override
	public int getMessageType() {
		return Message.BLOCK_BROADCAST;
	}

	@Override
	public TransactionBlockImproved getMessageBody() {
		return block;
	}

	@Override
	public boolean isForBroadcast() {
		return true;
	}

}
