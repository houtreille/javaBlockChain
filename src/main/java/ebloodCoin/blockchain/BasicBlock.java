package ebloodCoin.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import ebloodCoin.blockchain.crypto.hashing.HashingFunction;
import ebloodCoin.blockchain.crypto.hashing.SHA256HashingFunction;
import ebloodCoin.utils.SecurityUtils;
import ebloodCoin.utils.StringUtils;

public class BasicBlock implements Block, Serializable {

	private static final long serialVersionUID = 1L;
	private String hashId;
	private String previousHashId;
	private long timeStamp;
	private ArrayList<String> transactions = new ArrayList<String>();
	private int nonce = 0; // keep increasing till the required hash is found
	private static int difficultyLevel = 10;
	
	
	public BasicBlock(String previousHashId, int difficultyLevel) {
		this.previousHashId = previousHashId;
		this.timeStamp = new Date().getTime();
		this.difficultyLevel = difficultyLevel;
		
	}
	
	
	public String calculateBlockHash() throws Exception {

		StringBuilder sb = new StringBuilder();
		
		sb.append(previousHashId + Long.toHexString(timeStamp));
		
		for (String transaction : transactions) {
			sb.append(transaction);
		}
		
		sb.append(difficultyLevel);
		sb.append(nonce);
		
		//System.out.println(  sb.toString() + " -> " + hashing.hash(sb.toString()));
		
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

	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean addTransaction(Object transaction) throws Exception {
		transactions.add((String)transaction);
		return true;
	}

	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public static void main(String args[]) {
		
		Block testBlock = new BasicBlock("0", 20);
		
		for (int i = 0; i < 10; i++) {
			try {
				testBlock.addTransaction("Transaction"+i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			testBlock.mineBlock(difficultyLevel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}