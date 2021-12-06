package ebloodCoin;

import java.util.ArrayList;

import ebloodCoin.blockchain.Blockchain;
import ebloodCoin.blockchain.Miner;
import ebloodCoin.blockchain.TransactionBlock;
import ebloodCoin.blockchain.Wallet;
import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;
import ebloodCoin.utils.StringUtils;

public class TestBlockchain {

	private static Blockchain blockchain;
	public static int DIFFICULTY_LEVEL  =3;
	
	
	public static void main(String[] args) {
		System.out.println("Blockchain platform starts");
		System.out.println("Creating genesis miner, genesis transaction and genesis block");
		
		//Creating gensis miner
		Miner genesisMiner = new Miner("genesis", "genesis");
		Wallet.walletNameMap.get(genesisMiner.getPublicKey());
		
		//Genesisblock - previous hash id = 0
		TransactionBlock genesisBlock = new TransactionBlock("0", DIFFICULTY_LEVEL);
		
		//inputs for genesis blocks
		UTXo ut1 = new UTXo("0", genesisMiner.getPublicKey(), genesisMiner.getPublicKey(), 10001.0);
		UTXo ut2 = new UTXo("0", genesisMiner.getPublicKey(), genesisMiner.getPublicKey(), 12000.0);
		
		ArrayList<UTXo> inputs = new ArrayList<UTXo>();
		inputs.add(ut1);
		inputs.add(ut2);
		/* transaction inputs are 2 dummy UTXO to genesis*/
		Transaction gt = new Transaction(genesisMiner.getPublicKey(), genesisMiner.getPublicKey(), 10000, inputs);
		/* Transaction outputs are fund to receivers + change */
		boolean b = gt.prepareOutputUTXOs();
		
		if(!b) {
			System.out.println("Genesis transaction failed");
			System.exit(1);
		}
		
		//genesis miner signs the transaction
		gt.signTransaction(genesisMiner.getPrivateKey());
		StringUtils.displayTransaction(gt, System.out, 0);
		
		//add transaction to genesis block
		try {
			genesisBlock.addTransaction(gt);
			//GENESIS MINER mines the block
			b = genesisBlock.mineBlock(DIFFICULTY_LEVEL);
			
			if(b) {
				System.out.println("Successful mining for block : " + genesisBlock.getHash());
			} else {
				System.out.println("UNSuccessful mining for block : " + genesisBlock.getHash());
				System.exit(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		blockchain = new Blockchain(genesisBlock);
		System.out.println("Blockchain genesis successful");
		//Genesis miner copies the blockchain to its local ledger
		genesisMiner.setLocalLedger(blockchain);
		System.out.println("Genesis Miner balance : " + genesisMiner.getCurrentBalance());
		
		Miner A = new Miner("A","A");
		Wallet B = new Wallet("B","B");
		Miner C = new Miner("C","C");
		
		A.setLocalLedger(blockchain);
		B.setLocalLedger(blockchain);
		C.setLocalLedger(blockchain);
		
		//Create Second block
		TransactionBlock b2 = new TransactionBlock(genesisBlock.getHash(), DIFFICULTY_LEVEL);
		
		//genesis miner transfer 100 to A and 200 to B
		Transaction b2_t1 = genesisMiner.transferFund(A.getPublicKey(), 200.);
		if(b2_t1 != null) {
			try {
				if(b2_t1.verifyTransaction() && b2.addTransaction(b2_t1)) {
					//At this moment before block is mined and added to the chain
					// ABC shoulf have zero balance
					System.out.println("Miner " + genesisMiner.getCurrentBalance());
					System.out.println("A " + A.getCurrentBalance());
					System.out.println("B " + B.getCurrentBalance());
					System.out.println("C " + C.getCurrentBalance());
					StringUtils.displayTransaction(b2_t1, System.out, 0);
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		Transaction b2_t2 = genesisMiner.transferFund(B.getPublicKey(), 100.);
		if(b2_t2 != null) {
			try {
				if(b2_t2.verifyTransaction() && b2.addTransaction(b2_t2)) {
					//At this moment before block is mined and added to the chain
					// ABC shoulf have zero balance
					System.out.println("Miner " + genesisMiner.getCurrentBalance());
					System.out.println("A " + A.getCurrentBalance());
					System.out.println("B " + B.getCurrentBalance());
					System.out.println("C " + C.getCurrentBalance());
					StringUtils.displayTransaction(b2_t2, System.out, 0);
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			if(A.mineBlock(b2)) {
				System.out.println("b2 has been mined " + b2.getHash());
				blockchain.addBlock(b2);
				
				System.out.println("Miner " + genesisMiner.getCurrentBalance());
				System.out.println("A " + A.getCurrentBalance());
				System.out.println("B " + B.getCurrentBalance());
				System.out.println("C " + C.getCurrentBalance());
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		//Create Second block
			TransactionBlock b3 = new TransactionBlock(b2.getHash(), DIFFICULTY_LEVEL);
			
			//Should fail
			Transaction b3_t1 = A.transferFund(B.getPublicKey(), 200.);
			
			try {
				if(b3_t1 != null && b3_t1.verifyTransaction() && b3.addTransaction(b3_t1)) {
					System.out.println("t3 added to b3");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//should fail
			Transaction b3_t2 = A.transferFund(C.getPublicKey(), 300.);
			
			try {
				if(b3_t2 != null && b3_t2.verifyTransaction() && b3.addTransaction(b3_t2)) {
					System.out.println("t3 added to b3");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			Transaction b3_t3 = A.transferFund(C.getPublicKey(), 20.);
			
			try {
				if(b3_t3.verifyTransaction() && b3.addTransaction(b3_t3)) {
					System.out.println("b3_t3 added to b3");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Transaction b3_t4 = B.transferFund(C.getPublicKey(), 80.);
			
			try {
				if(b3_t4.verifyTransaction() && b3.addTransaction(b3_t4)) {
					System.out.println("b3_t4 added to b3");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			try {
				if(C.mineBlock(b3)) {
					System.out.println("b3 has been mined " + b3.getHash());
					blockchain.addBlock(b3);
					
					System.out.println("Miner " + genesisMiner.getCurrentBalance());
					System.out.println("A " + A.getCurrentBalance());
					System.out.println("B " + B.getCurrentBalance());
					System.out.println("C " + C.getCurrentBalance());
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
