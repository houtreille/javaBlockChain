package ebloodCoin;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.GsonBuilder;

import ebloodCoin.blockchain.BasicBlock;
import ebloodCoin.blockchain.Wallet;
import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;
import ebloodCoin.utils.SecurityUtils;

public class Main {

	public static void main(String[] args) {
		
		/*
		try {
			BasicBlock a = new BasicBlock("0",0);
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bo); //allowing serialize any Serializable object into a byte array
			
			
			out.writeObject(a);
			
			FileOutputStream fout = new FileOutputStream("walletSerialization");
			fout.write(bo.toByteArray());
			
			fout.flush();
			fout.close();
			
			out.close();
			bo.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		*/
		
		//Wallet
//		
//		Scanner in = new Scanner(System.in);
//		System.out.println("Wallet name");
//		String name = in.nextLine();
//		
//		System.out.println("password");
//		String password = in.nextLine();
//		in.close();
//		
//		Wallet w  = new Wallet(name, password);
//		System.out.println("wallet created : " + w.getWalletName());
//		
//		Wallet w2  = new Wallet(name, password);
		/////////////////////////////////////////////////////////////////
		/*
		 * Transaction
		 */
		////////////////////////////////////////////////////////////////
		try {
			KeyPair senderKey = SecurityUtils.generateKeyPair();
			
			PublicKey[] receivers = new PublicKey[2];
			double[] fundToTransfer = new double[receivers.length];
			
			for (int i = 0; i < fundToTransfer.length; i++) {
				receivers[i] = SecurityUtils.generateKeyPair().getPublic();
				fundToTransfer[i] = (i + 1) * 100;
			}
			
			UTXo input = new UTXo("0", senderKey.getPublic(), senderKey.getPublic(), 1000);
			ArrayList<UTXo> inputs = new ArrayList<UTXo>();
			inputs.add(input);
			
			Transaction trans = new Transaction(senderKey.getPublic(), receivers, fundToTransfer, inputs);
			
			//Make sure sender total balance >= total transaction cost

			double availableFund = 0.;
			for (UTXo inpute : inputs) {
				availableFund += inpute.getAmount();
			}
			
			
			double totalCost = trans.getTotalFundToTransfer() + Transaction.TRANSACTION_FEE;
			
			for (int i = 0; i < receivers.length; i++) {
				UTXo outputForReceiver = new UTXo(trans.getHashId(), receivers[i], senderKey.getPublic(), fundToTransfer[i]);
				trans.addOutputUTXO(outputForReceiver);
			}
			
			UTXo change = new UTXo(trans.getHashId(), senderKey.getPublic(), senderKey.getPublic(), totalCost - availableFund);
			trans.addOutputUTXO(change);
			
			trans.signTransaction(senderKey.getPrivate());
			trans.print();
			
//			String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(trans);		
//			System.out.println(blockchainJson);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
