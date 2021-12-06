package ebloodCoin.blockchain;

import java.security.PublicKey;
import java.util.ArrayList;

import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.utils.SecurityUtils;

public class TransactionBlockImproved extends TransactionBlock {

	

	/*
	 * A block can be started only after a certain number of valid transaction have been collected
	 */
	public final static int TRANSACTION_LOWER_LIMIT = 1;
	public final static int TRANSACTION_UPPER_LIMIT = 100;
	
	//To record miner of each block
	private PublicKey creator;

	//keep track of block has been mined [security] - once a block has been mined, no further modification is allowed
	private boolean isMined = false;
	
	//The miner must sign the block, so others miners can verify the signature
	private byte[] minerSignature;
	
	//The transaction to reward the miner
	private Transaction rewardTransaction;
	
	public TransactionBlockImproved(String previousHashId, int difficultyLevel) {
		super(previousHashId, difficultyLevel);
	}
	
	
	public TransactionBlockImproved(String previousHashId, int difficultyLevel, PublicKey creator) {
		super(previousHashId, difficultyLevel);
		this.creator = creator;
	}
	
	@Override
	public String calculateBlockHash() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getPreviousHash() + Long.toHexString(getTimeStamp()));
		sb.append(computeMerkleRoot());
		sb.append(""+getNonce());
		
		return SecurityUtils.getDefaultHashingFunction().hash(sb.toString());
		
	}
	
	
	
	
	public Boolean mineBlock(PublicKey key, int prefixDifficulty) throws Exception {
		
		if(creator.equals(key) && !isMined) {
			if(mineBlock(prefixDifficulty)) {
				isMined = true;
			}
		}

		return isMined;
	}

	public boolean addTransaction(Object transaction, PublicKey key) throws Exception {

		if(!isMined && minerSignature == null) {
			if(getTotalTransactionSize() < TRANSACTION_UPPER_LIMIT) {
				if(key.equals(creator)) {
					return super.addTransaction(transaction);
				}
			}
		}
		return false;
	}
	
	public boolean generateRewardTransaction(PublicKey key, Transaction trans) {
		if(rewardTransaction == null && key.equals(creator)) {
			this.rewardTransaction = trans;
			return true;
		}
		return false;
	}
	
	public Transaction getRewardTransaction() {
		return rewardTransaction;
	}
	

	private String computeMerkleRoot() throws Exception {
		
		ArrayList<String> hashes = new ArrayList<String>();
		
		
		for (int i = 0; i < getTotalTransactionSize(); i++) {
			hashes.add(((Transaction)getData(i)).getHashId().toString());
		}
		
		if(rewardTransaction != null) {
			hashes.add(rewardTransaction.getHashId());
		}
		
		return SecurityUtils.computeMerkleTreeRootHash(hashes.toArray());
	}
	
	/*
	 * When a miner to add the block to the blockchain it is necessary to verify the signature
	 */
	public boolean verifySignature(PublicKey key) throws Exception {
		return SecurityUtils.verifySignature(key, this.minerSignature, getHash());
	}
	
	/*A block must be signed based upon hash
	 *
	 */
	
	public boolean signTheBlock(PublicKey key, byte[] signature) throws Exception {
		if(!isSigned()) {
			if(key.equals(creator)) {
				if (SecurityUtils.verifySignature(key, signature, getHash())) {
					this.minerSignature = signature;
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	public PublicKey getCreator() {
		return creator;
	}

	public boolean isMined() {
		return isMined;
	}

	public boolean isSigned() {
		return minerSignature != null;
	}
	
}
