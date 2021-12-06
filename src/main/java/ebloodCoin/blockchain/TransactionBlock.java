package ebloodCoin.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import ebloodCoin.blockchain.crypto.hashing.HashingFunction;
import ebloodCoin.blockchain.crypto.hashing.SHA256HashingFunction;
import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.utils.SecurityUtils;
import ebloodCoin.utils.StringUtils;

public class TransactionBlock implements Block, Serializable {

	

	private static final int TRANSACTION_UPPER_LIMIT = 2;
	private static final long serialVersionUID = 1L;
	private String hashId;
	private String previousHashId;
	private long timeStamp;
	

	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private int nonce = 0; // keep increasing till the required hash is found
	private static int difficultyLevel = 3;
	
	
	public TransactionBlock(String previousHashId, int difficultyLevel) {
		this.previousHashId = previousHashId;
		this.timeStamp = new Date().getTime();
		this.difficultyLevel = difficultyLevel;
		
	}
	
	
	public String calculateBlockHash() throws Exception {

		StringBuilder sb = new StringBuilder();
		
		sb.append(previousHashId + Long.toHexString(timeStamp));
		
		for (Transaction transaction : transactions) {
			sb.append(transaction.getHashId());
		}
		
		sb.append(difficultyLevel);
		sb.append(nonce);
		
		return SecurityUtils.getDefaultHashingFunction().hash(sb.toString());
	}

	public Boolean mineBlock(int prefixDifficulty) throws Exception {
	
		this.hashId = calculateBlockHash();
		
		while(!StringUtils.hasMeetDifficultyLevel(hashId, prefixDifficulty)) {
			nonce++;
			this.hashId = calculateBlockHash();
		}
		
		return true;

	}

	public String getHash() {
		return hashId;
	}

	public String getPreviousHash() {
		return previousHashId;
	}

	public boolean addTransaction(Object transaction) throws Exception {
		if(transactions.size() <= TRANSACTION_UPPER_LIMIT) {
			transactions.add((Transaction)transaction);
			return true;
		} else {
			return false;
		}
	}

	public Object getData(Object i) {
		return transactions.get((Integer)i);
	}
	
	public int getTotalTransactionSize() {
		return transactions.size();
	}
	
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public int getNonce() {
		return nonce;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Block) {
			if(    (((Block)obj).getHash()).equals(getHash())) {
				return true;
			}
		}
		return false;
	}
	
	
}