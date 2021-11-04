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
import java.util.Scanner;

import ebloodCoin.blockchain.crypto.security.encryption.XOREncryption;
import ebloodCoin.utils.SecurityUtils;


public class Wallet {

	private KeyPair keypair;
	private String walletName;
	private static String keyFolder = "./WalletKeys/";
	private XOREncryption passwordEncryption = new XOREncryption();
	
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
	
	public String getWalletName() {
		return walletName;
	}

}
