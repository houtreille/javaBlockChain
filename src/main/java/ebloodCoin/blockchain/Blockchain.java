package ebloodCoin.blockchain;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;

public class Blockchain implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final double MINING_REWARD = 100.;
	private LedgerList<Block> blockchain;
	
	public Blockchain(TransactionBlock genesisBlock) {
		blockchain = new LedgerList<Block>();
		blockchain.add(genesisBlock);
	}
	
	public Block getGenesisBlock() {
		return blockchain.getFirst();
	}
	
	public Block getLastBlock() {
		return blockchain.getLast();
	}
	
	public Block getBlock(int index) {
		return blockchain.getElementAt(index);
	}
	
	public synchronized boolean addBlock(Block block) {
		if(block.getPreviousHash().equals(blockchain.getLast().getHash())) {
			return blockchain.add(block);
		}
		return false;
	}
	
	public int getSize() {
		return blockchain.getSize();
	}
	
	public double findRelatedUTXos(PublicKey key,
			ArrayList<UTXo> all, ArrayList<UTXo> spent, ArrayList<UTXo> unspent) {
		return findRelatedUTXos(key, all, spent, unspent, new ArrayList<Transaction>());
	}
	
	
	/*
	 * Computationally expensive because we look for given in the entire blockchain 
	 */
	public double findRelatedUTXos(PublicKey key,
			ArrayList<UTXo> all, ArrayList<UTXo> spent, ArrayList<UTXo> unspent, ArrayList<Transaction> sentTransactions) {
		double gain = 0.;
		double spending = 0.;
		Map<String, UTXo> map = new HashMap<String, UTXo>(); //store UTXo found by the search
		
		for (int i = 0; i < getSize(); i++) {
			TransactionBlock block = (TransactionBlock)blockchain.getElementAt(i);
			int blockSize = block.getTotalTransactionSize();
			
			for (int j = 0; j < blockSize; j++) {
				Transaction t = (Transaction)block.getData(j);
				//each transaction can be either sent or receive by the public key
				if(i != 0 && t.getSender().equals(key)) { //not genesis block and sender of transation is key we look for
					// feed parameters sentTransactions & spent
					
					//INPUT
					//Sum all the spending spent by the public key
					for (int x=0; x < t.getNumberOfInputUTXo() ; x++) {
						UTXo ut = t.getInputUTXO(x);
						spent.add(ut);
						map.put(ut.getHashId(), ut);
						spending += ut.getAmount();
					}
					
					sentTransactions.add(t);
				}
				
				//Sum all the funds ever transferred to the public key
				for (int x=0; x < t.getNumberOfOutputUTXo() ; x++) {
					UTXo ux = t.getOutputUTXO(x);
					
					if(ux.getReceiver().equals(key)) {
						all.add(ux);
						gain += ux.getAmount();
					}
				}	
			}	
		}
		
		 for (int i = 0; i < all.size(); i++) {
			UTXo ut = all.get(i);
			if(!map.containsKey(ut.getHashId())) {
				unspent.add(ut);
			}
		}
		 
		return gain - spending;
	}
	
	
	 public double findUnspentUTXos(PublicKey key, ArrayList<UTXo> unspent) {
		 return findRelatedUTXos(key, new ArrayList<UTXo>(), new ArrayList<UTXo>(), unspent);
		  
	 }
	 
	 public List<UTXo> findUnspentUTXos(PublicKey key) {
		 
		  ArrayList<UTXo> unspent = new ArrayList<UTXo>();
		  findRelatedUTXos(key, new ArrayList<UTXo>(), new ArrayList<UTXo>(), unspent);
		  return unspent;
	 }
	
	
	public double getCheckBalance(PublicKey key) {
		return findRelatedUTXos(key, new ArrayList<UTXo>(), new ArrayList<UTXo>(), new ArrayList<UTXo>(), new ArrayList<Transaction>());
	}
	
}
