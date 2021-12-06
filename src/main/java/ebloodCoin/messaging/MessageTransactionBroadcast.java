package ebloodCoin.messaging;

import ebloodCoin.blockchain.transaction.Transaction;

public class MessageTransactionBroadcast extends Message {

	private static final long serialVersionUID = 1L;
	
	private Transaction trans = null;
	
	public MessageTransactionBroadcast(Transaction trans) {
		super();
		this.trans = trans;
	}

	@Override
	public int getMessageType() {
		return Message.TRANSACTION_BROADCAST;
	}

	@Override
	public Transaction getMessageBody() {
		return trans;
	}

	@Override
	public boolean isForBroadcast() {
		return true;
	}

}
