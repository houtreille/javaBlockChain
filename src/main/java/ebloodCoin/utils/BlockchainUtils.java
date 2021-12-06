package ebloodCoin.utils;

import ebloodCoin.blockchain.Block;
import ebloodCoin.blockchain.Blockchain;
import ebloodCoin.blockchain.TransactionBlockImproved;

public class BlockchainUtils {

	public static boolean validateBlockchain(Blockchain ledger) throws Exception {
		int size = ledger.getSize();
		
		for (int i = size -1 ; i > 0; i--) {

			TransactionBlockImproved currentBlock = (TransactionBlockImproved)ledger.getBlock(i);
			
			boolean b = currentBlock.verifySignature(currentBlock.getCreator());
			
			if(!b) {
				System.out.println("Validate blockchain : " + i +" signature is invalid");
				return false;
			}
			
			b = StringUtils.hasMeetDifficultyLevel(currentBlock.getHash(), 3) && currentBlock.getHash().equals(currentBlock.calculateBlockHash());
			
			
			if(!b) {
				System.out.println("Validate blockchain : " + i +" bad hashing");
				return false;
			}
			
			Block previousBlock = ledger.getBlock(i-1);
			
			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				System.out.println("Validate blockchain : " + i +" Invalid previous Hash");
				return false;
			}
		}
		
		try {
			
			if(ledger.getGenesisBlock() instanceof TransactionBlockImproved) {
				TransactionBlockImproved genesisBlock =  ((TransactionBlockImproved)ledger.getGenesisBlock());
				boolean b = genesisBlock.verifySignature( ((TransactionBlockImproved)ledger.getGenesisBlock()).getCreator());
			
				if(!b) {
					System.out.println("Validate blockchain : genesis block is tampered");
					return false;
				}
				
				if(!genesisBlock.getHash().equals(genesisBlock.calculateBlockHash())) {
					if(!b) {
						System.out.println("Validate blockchain : genesis block bad hashing");
						return false;
					}
				}
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
}
