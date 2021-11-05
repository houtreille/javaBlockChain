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
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;

import ebloodCoin.blockchain.crypto.security.encryption.XOREncryption;
import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;
import ebloodCoin.utils.SecurityUtils;


public class Wallet {

	private KeyPair keypair;
	private String walletName;
	private static String keyFolder = "./WalletKeys/";
	private XOREncryption passwordEncryption = new XOREncryption();
	private Blockchain localLedger = null; //LET EACH WALLET have a local blockchain
	
	
	public Wallet() {
		
	}
	
	public Wallet(String walletName) {
		super();
		this.walletName = walletName;
	}
	
	
	
	
	public Wallet(String walletName, String password) {
		
		super();
		
		try {
			this.keypair = SecurityUtils.generateKeyPair();
			this.walletName = walletName;
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
		
		//Create Input for the transaction
		ArrayList<UTXo> inputs = new ArrayList<UTXo>();
		available = 0;
		
		for(int i = 0; i < unspent.size() && available < totalToTransfer; i++) {
			UTXo ut = unspent.get(i);
			available += ut.getAmount();
			inputs.add(ut);
		}
		
		Transaction t = new Transaction(keypair.getPublic(), receivers, fundToTransfer, inputs);
		
		boolean b = t.prepareOutputUTXOs();
		
		if(b) {
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

	
	public synchronized void setLocalLedger(Blockchain blockchain) {
		this.localLedger = blockchain;
	}
	
	

}
