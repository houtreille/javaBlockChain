package ebloodCoin;

import java.security.PublicKey;
import java.util.ArrayList;

import ebloodCoin.blockchain.Block;
import ebloodCoin.blockchain.Blockchain;
import ebloodCoin.blockchain.Miner;
import ebloodCoin.blockchain.TransactionBlockImproved;
import ebloodCoin.blockchain.Wallet;
import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;

public class TestBlockchainImproved {

	private static Blockchain blockchain;
	public static int DIFFICULTY_LEVEL  =3;
	
	
	public static void main(String[] args) {
		
		try {
			ArrayList<Wallet> users = new ArrayList<Wallet>();
			Miner genesisMiner = new Miner("genesis", "genesis");
			users.add(genesisMiner);
			
			
			//Genesisblock - previous hash id = 0
			TransactionBlockImproved genesisBlock = new TransactionBlockImproved("0", DIFFICULTY_LEVEL, genesisMiner.getPublicKey());
			
			//inputs for genesis blocks
			UTXo ut1 = new UTXo("0", genesisMiner.getPublicKey(), genesisMiner.getPublicKey(), 10001.0);
			UTXo ut2 = new UTXo("0", genesisMiner.getPublicKey(), genesisMiner.getPublicKey(), 12000.0);
			
			ArrayList<UTXo> inputs = new ArrayList<UTXo>();
			inputs.add(ut1);
			inputs.add(ut2);
			
			Transaction gt = new Transaction(genesisMiner.getPublicKey(), genesisMiner.getPublicKey(), 10000, inputs);
			
			boolean b = gt.prepareOutputUTXOs();
			
			if(!b) {
				System.out.println("Genesis transaction failed");
				System.exit(1);
			}
			
			//genesis miner signs the transaction
			gt.signTransaction(genesisMiner.getPrivateKey());
			
			genesisBlock.addTransaction(gt);
			//GENESIS MINER mines the block
			b = genesisMiner.mineBlock(genesisBlock);
			
			
			blockchain = new Blockchain(genesisBlock);
			System.out.println("Blockchain genesis successful");
			//Genesis miner copies the blockchain to its local ledger
			genesisMiner.setLocalLedger(blockchain);
			System.out.println("Genesis Miner balance : " + genesisMiner.getCurrentBalance());
			
			Miner A = new Miner("A","A");
			Wallet B = new Wallet("B","B");
			Miner C = new Miner("C","C");
			
			users.add(A);
			users.add(B);
			users.add(C);
			
			A.setLocalLedger(blockchain);
			B.setLocalLedger(blockchain);
			C.setLocalLedger(blockchain);
			
			///////////////////Block b2
			TransactionBlockImproved b2 = A.createNewBlock(blockchain, 3);
			PublicKey[] receivers = {B.getPublicKey(), B.getPublicKey(), C.getPublicKey(), C.getPublicKey()};
			double[] funds = {500.,200.,300.,100.};
			
			Transaction t1 = genesisMiner.transferFund(receivers, funds);
			
			
			Boolean addedBlock = A.addTransaction(t1, b2);
			
			if(addedBlock) {
				System.out.println("t1 added to block b2");
			} else {
				System.out.println("t1 not added to block b2 -> Exit");
				System.exit(1);
			}
			
			
			Boolean addedReward = A.generateRewardTransaction(b2);
			
			if(addedReward) {
				System.out.println("reward transaction successfully added to b2");
			} else {
				System.out.println("reward transaction successfully NOT added to b2");
				System.exit(1);
			}
			
			boolean blockIsMined = A.mineBlock(b2);
			
			if(blockIsMined) {
				System.out.println("b2 successfully mined");
			} else {
				System.out.println("b2 not successfully mined");
				System.exit(1);
			}
			
			boolean blockIsVerified = verifyBlock(A, b2, "b2"); 
			
			
			if(blockIsVerified) {
				System.out.println("b2 block Is Verified");
			} else {
				System.out.println("b2 block Is Not Verified");
				System.exit(1);
			}
			
			Double total = 0.;
			
			for (Wallet w : users) {
				total += w.getCurrentBalance();
			}
			
			System.out.println("Total = " + total);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public static  boolean verifyBlock(Wallet w, Block b, String blockName) throws Exception {
		return w.verifyGuestBlock((TransactionBlockImproved)b);
	}

}
