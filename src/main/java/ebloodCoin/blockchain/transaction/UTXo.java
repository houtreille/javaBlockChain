package ebloodCoin.blockchain.transaction;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

import ebloodCoin.blockchain.crypto.hashing.Hashable;
import ebloodCoin.blockchain.crypto.hashing.HashingFunction;
import ebloodCoin.blockchain.crypto.hashing.SHA256HashingFunction;
import ebloodCoin.utils.StringUtils;

public class UTXo implements Serializable, Hashable {

	private static final long serialVersionUID = 1L;
	
	private String hashId;
	private String parentTransactionId;
	private PublicKey receiver;
	private PublicKey sender;
	private long timestamp;
	private double amount;
	private long sequentialNumber = 0L;
	
	private HashingFunction hashing = new SHA256HashingFunction();
	
	public UTXo(String parentTransactionId, PublicKey receiver, PublicKey sender,
			double amount) {
		super();
		
		this.parentTransactionId = parentTransactionId;
		this.receiver = receiver;
		this.sender = sender;
		this.timestamp = new Date().getTime();
		this.amount = amount;
		this.sequentialNumber = StringUtils.getUniqueId();
	}
	
	public String computeHashId() throws Exception {
		String message = parentTransactionId +
				StringUtils.getKeyString(sender)+
				StringUtils.getKeyString(receiver)+
				Long.toHexString(timestamp)+
				Double.toHexString(amount)+
				Long.toHexString(sequentialNumber);
		
		return hashing.hash(message);
				
	}

	public String getHashId() {
		return hashId;
	}

	public String getParentTransactionId() {
		return parentTransactionId;
	}

	public PublicKey getReceiver() {
		return receiver;
	}

	public PublicKey getSender() {
		return sender;
	}

	public double getAmount() {
		return amount;
	}

	public long getSequentialNumber() {
		return sequentialNumber;
	}

	@Override
	public boolean equals(Object arg0) {
		return ((UTXo)arg0).getHashId().equals(hashId);
	}
	
	public boolean isMiningReward() {
		return false;
	}
	
	
	
}
