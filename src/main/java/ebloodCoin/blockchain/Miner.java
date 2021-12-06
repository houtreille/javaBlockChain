package ebloodCoin.blockchain;

import java.util.LinkedList;

import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;
import ebloodCoin.blockchain.transaction.UTXoAsMiningReward;
import ebloodCoin.utils.SecurityUtils;

public class Miner extends Wallet {

	public Miner(String minerName, String password) {
		super(minerName, password);
	}
	
	public boolean mineBlock(Block block) throws Exception  {
		
		if(block instanceof TransactionBlockImproved && ((TransactionBlockImproved)block).mineBlock(getPublicKey(), 3)) {
			byte[] signature = SecurityUtils.signTransaction(getPrivateKey(), block.getHash());
			return ((TransactionBlockImproved)block).signTheBlock(getPublicKey(), signature);	
		} else if (((TransactionBlock)block).mineBlock( 3)) {
			return (block.mineBlock(3));
		} else {
			return false;
		}
	}
	
	public boolean addTransaction(Transaction ts, TransactionBlockImproved block) throws Exception {
		
		if(validateTransaction(ts )) {
			return block.addTransaction(ts);
		} else {
			return false;
		}
	}
	
	public boolean deleteTransaction(Transaction ts, TransactionBlockImproved block) throws Exception {
		//return block.deleteTransaction(ts, block);
		return false;
	}
	
	public boolean generateRewardTransaction(TransactionBlockImproved block) {
		double amount = Blockchain.MINING_REWARD; // + block.getTransactionFeeAmount();
		Transaction t = new Transaction(getPublicKey(), getPublicKey(), amount, new LinkedList<UTXo>());
		
		UTXo ut = new UTXoAsMiningReward(t.getHashId(), t.getSender(), getPublicKey(), amount);
		t.addOutputUTXO(ut);
		t.signTransaction(getPrivateKey());
		
		return block.generateRewardTransaction(getPublicKey(), t);
	}
	
	
	// A block is supposed to be created by a miner
	public TransactionBlockImproved createNewBlock(Blockchain ledger, int difficultyLevel) {
		TransactionBlockImproved b = new TransactionBlockImproved(ledger.getLastBlock().getHash(), difficultyLevel, this.getPublicKey());
		
		return b;
	}
}
