package ebloodCoin.blockchain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import ebloodCoin.blockchain.crypto.security.encryption.XOREncryption;
import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;
import ebloodCoin.utils.BlockchainUtils;
import ebloodCoin.utils.SecurityUtils;
import ebloodCoin.utils.StringUtils;


public class Wallet {

	private KeyPair keypair;
	private String walletName;
	private static String keyFolder = "./WalletKeys/";
	private XOREncryption passwordEncryption = new XOREncryption();
	private Blockchain localLedger = null; //LET EACH WALLET have a local blockchain
	public static HashMap<Key, String> walletNameMap = new HashMap<Key, String>();
	
	public Wallet() {
		
	}
	
	public Wallet(String walletName) {
		super();
		this.walletName = walletName;
	}
	
	
	
	
	public Wallet(String walletName, String password) {
		
		try {
			this.keypair = SecurityUtils.generateKeyPair();
			this.walletName = walletName;
			walletNameMap.put(keypair.getPublic(), walletName);
			walletNameMap.put(keypair.getPrivate(), walletName);
			retrieveWallet(walletName, password);
			System.out.println("Successful wallet retrieval");
			
		} catch(StreamCorruptedException sce) {
			System.out.println("Invalid password for wallet retrieval");
			System.exit(0);
	     }catch (Exception e) {
			try {
				prepareWallet(password);
				System.out.println("Successful wallet creation");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private void prepareWallet(String password) throws IOException, Exception {
		Wallet test = new Wallet();
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bo); //allowing serialize any Serializable object into a byte array
		
		out.writeObject(this.keypair);
		byte[] keyBytes = passwordEncryption.encrypt(bo.toByteArray(), password);
		
		
		if(!new File(keyFolder).exists()) {
			new File(keyFolder).mkdir();
		}
		
		
		FileOutputStream fout = new FileOutputStream(keyFolder + walletName + "_Keys");
		fout.write(keyBytes);
		fout.flush();
		fout.close();
		
		bo.close();
	}
	
	private void retrieveWallet(String walletName, String password) throws IOException, StreamCorruptedException, Exception{
		FileInputStream fi = new FileInputStream(keyFolder + walletName + "_Keys");
		byte bb[] = new byte[4096]; // 4096 large enough for key data
		
		int size = fi.read(bb);
		fi.close();
		
		byte data[] = new byte[size];
		
		for (int i = 0; i < data.length; i++) {
			data[i] = bb[i];
		}
		
		byte keyBytes[] = passwordEncryption.decrypt(data, password);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(keyBytes));
		this.keypair = (KeyPair)in.readObject();
		this.walletName = walletName;
		walletNameMap.put(keypair.getPublic(), walletName);
		walletNameMap.put(keypair.getPrivate(), walletName);
	}
	
	public Transaction transferFund(PublicKey[] receivers, double[] fundToTransfer) {
		
		ArrayList<UTXo> unspent = new ArrayList<UTXo>();
		
		double available = localLedger.findUnspentUTXos(keypair.getPublic(), unspent);
		
		double totalToTransfer = 0.;
		for (int i = 0; i < fundToTransfer.length; i++) {
			totalToTransfer += fundToTransfer[i];
		}
		
		if(available < totalToTransfer) {
			System.out.println("Insufficient funds");
			return null;
		}
		
		//Prepare Inputs
		ArrayList<UTXo> inputs = new ArrayList<UTXo>();
		available = 0;
		
		for(int i = 0; i < unspent.size() && available < totalToTransfer; i++) {
			UTXo ut = unspent.get(i);
			available += ut.getAmount();
			inputs.add(ut);
		}
		
		Transaction t = new Transaction(keypair.getPublic(), receivers, fundToTransfer, inputs);
		
		//Prepare Outputs
		boolean b = t.prepareOutputUTXOs();
		
		if(b) {
			t.signTransaction(getPrivateKey());
			return t;
		} else {
			return null;
		}
	}
	
	public Transaction transferFund(PublicKey receiver, double fundToTransfer) {
		return transferFund(new PublicKey[] {receiver}, new double[] {fundToTransfer});
	}
	
	
	public double getCurrentBalance() {
		return localLedger.getCheckBalance(keypair.getPublic());
	}
	
	public String getWalletName() {
		return walletName;
	}
	
	public synchronized Blockchain getLocalLedger() {
		return localLedger;
	}

	
	public synchronized boolean setLocalLedger(Blockchain blockchain) {
		
		boolean b = false;
		
		try {
			b = BlockchainUtils.validateBlockchain(blockchain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!b) {
			System.out.println(getWalletName() +"] the incoming blockchain failed validation" );
			return false;
		}
		
		if(this.localLedger == null) {
			this.localLedger = blockchain;
			return true;
		} else {
			if((blockchain.getSize() > localLedger.getSize()) &&(blockchain.getGenesisMiner().equals(localLedger.getGenesisMiner()))) {
				this.localLedger = blockchain;
				return true;
			} else if (blockchain.getSize() > localLedger.getSize()) {
				System.out.println(getWalletName() +"] BLOCKChain is smaller than current one" );
				return false;
			} else {
				System.out.println(getWalletName() +"] BLOCKChain has different genesis miner" );
				return false;
			}
		}
	}
	
	
	public boolean validateTransaction(Transaction ts) {
		if(ts == null) {
			return false;
		} else if(!ts.verifyTransaction()) {
			return false;
		}
		
		if(getLocalLedger() == null && !getLocalLedger().isTransactionExist(ts)) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean verifyGuestBlock(TransactionBlockImproved block) throws Exception {
		return verifyGuestBlock(block, getLocalLedger());
	}
	
	public boolean verifyGuestBlock(TransactionBlockImproved block, Blockchain ledger) throws Exception {
		//verify the signature
		if(!block.verifySignature(block.getCreator())) {
			return false;
		}
		
		if(!StringUtils.hasMeetDifficultyLevel(block.getHash(), 3) || !block.getHash().equals(block.calculateBlockHash())) {
			return false;
		}
		
		if(!block.getPreviousHash().equals(ledger.getLastBlock().getHash())) {
			return false;
		}
		
		for (int i = 0; i < block.getTotalTransactionSize(); i++) {
			Transaction trans = (Transaction)block.getData(i);
			if(!validateTransaction(trans)) {
				return false;
			}
		}
		
		//if(block.getRewardTransaction().getTotalFundToTransfer() > Blockchain.MINING_REWARD + block.gett)
		
		return true;
	}
	
	
	public PublicKey getPublicKey() {
		return keypair.getPublic();
	}
	
	public PrivateKey getPrivateKey() {
		return keypair.getPrivate();
	}

}
