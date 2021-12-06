package ebloodCoin.messaging;

import java.io.Serializable;

public abstract class Message implements Serializable {
	private final static long serialVersionUID = 1L;
	public static final int ID = 0;
	
	//Message types
	public static final int TEXT_BROADCAST = 1;
	public static final int TEXT_PRIVATE = 2;
	
	public static final int TRANSACTION_BROADCAST = 10;
	public static final int BLOCK_BROADCAST = 20;
	public static final int BLOCK_PRIVATE = 21;
	public static final int BLOCK_ASK_PRIVATE = 22;
	public static final int BLOCK_ASK_BROADCAST = 23;
	
	public static final int BLOCKCHAIN_BROADCAST = 3;
	public static final int BLOCKCHAIN_PRIVATE = 31;
	public static final int BLOCKCHAIN_ASK_BROADCAST = 33;
	
	public static final int ADDRESS_BROADCAST = 4;
	public static final int ADDRESS_PRIVATE = 41;
	
	//A test string used to testify if the message transportation is tampered
	public static final String JCOIN_MESSAGE = "This package is from mdSky";
	public abstract int getMessageType();
	
	public static final String TEXT_CLOSE = "Close_me";
	public static final String TEXT_ASK_ADDRESSES  = "TEXT_QUERY_ADDRESSES";
	
	public abstract Object getMessageBody();
	public abstract boolean isForBroadcast();
}
