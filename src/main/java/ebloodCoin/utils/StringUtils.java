package ebloodCoin.utils;

import java.io.PrintStream;
import java.security.Key;

import ebloodCoin.blockchain.Blockchain;
import ebloodCoin.blockchain.TransactionBlock;
import ebloodCoin.blockchain.Wallet;
import ebloodCoin.blockchain.transaction.Transaction;
import ebloodCoin.blockchain.transaction.UTXo;

public class StringUtils {

	private static long uniqueId = 0L;

	public static long getUniqueId() {
		uniqueId++;
		return uniqueId;
	}


	public static String getMiningDifficulatyString(int diff) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < diff; i++) {
			sb.append("0");
		}
		
		return sb.toString();
		
	}
	
	
	public static boolean hasMeetDifficultyLevel(String hash, int difficultyLevel) {
		char[] array = hash.toCharArray();
		
		for (int i = 0; i < difficultyLevel; i++) {
			if(array[i] != '0') {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Converts a hash in bytes into a bit String
	 */
	public static String toBinaryString(byte hash[]) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < hash.length; i++) {
			String s = Integer.toBinaryString((int)hash[i]);
			while(s.length() < 8) {
				s = "0"+s; // because Integer.toBinaryString TRUNCATE leading zero
			}
			sb.append(s);
		}
		
		return sb.toString();
	}
	
	public static String getKeyString(Key key) {
		return java.util.Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static void displayTab(PrintStream out, int level, String s) {
		for (int i = 0; i < level; i++) {
			out.print("\t");
		}
		out.println(s);
	}
	
	public static void displayUTXo(UTXo utxo, PrintStream out, int level) {
		displayTab(out, level, "fund : " + utxo.getAmount() + ", sender : " + Wallet.walletNameMap.get(utxo.getSender()) +", receiver :" + Wallet.walletNameMap.get(utxo.getReceiver()));
	}
	
	public static void displayTransaction(Transaction trans, PrintStream out, int level) {
		displayTab(out, level, "Transaction{");
		displayTab(out, level+1, "Hash:" +	trans.getHashId());
		displayTab(out, level+1, "sender:" + Wallet.walletNameMap.get(trans.getSender())); //StringUtils.getKeyString(trans.getSender())
		displayTab(out, level+1, "receiver:");
		
		for (int i = 0; i < trans.getReceivers().length; i++) {
			displayTab(out, level+2, Wallet.walletNameMap.get(trans.getReceivers()[i]));
		}
		
				
				
		displayTab(out, level+1, "total:" + trans.getTotalFundToTransfer());
		displayTab(out, level+1, "Inputs:");
		
		for (int i = 0; i < trans.getNumberOfInputUTXo(); i++) {
			UTXo ut= trans.getInputUTXO(i);
			displayUTXo(ut, out, level+2);
		}
		
		displayTab(out, level+1, "Outputs:");
		
		for (int i = 0; i < trans.getNumberOfOutputUTXo() - 1; i++) {
			UTXo ut= trans.getOutputUTXO(i);
			displayUTXo(ut, out, level+2);
		}
		
		UTXo change = trans.getOutputUTXO(trans.getNumberOfOutputUTXo() - 1);
		displayTab(out, level + 2, "Change : " + change.getAmount());
		displayTab(out, level + 1, "Transaction Fee : " + Transaction.TRANSACTION_FEE);
		
		try {
			boolean b = trans.verifyTransaction();
			displayTab(out, level + 1, "Transaction verified: " + b);
		} catch (Exception e) {
			displayTab(out, level + 1, "Transaction not verified");
		}
		displayTab(out, level, "}");
		
	}
	
	public static void displayBlock(TransactionBlock block, PrintStream out, int level) {
		displayTab(out, level, "Block{");
		displayTab(out, level, "\tID: " + block.getHash());
		
		for (int i = 0; i < block.getTotalTransactionSize(); i++) {
			displayTransaction((Transaction)block.getData(i), out, level + 1);
		}
		
		//if(block.getRewardTransaction() != null) {
			displayTab(out, level + 1, "Rewaard transaction : 0");// + block.getRewardTransaction());
		//}
		
		displayTab(out, level, "}");
	}
	
	public static void displayBlockchain(Blockchain chain, PrintStream out, int level) {
		displayTab(out, level, "Blockchain { number of blocks : " + chain.getSize());
		
		for (int i = 0; i < chain.getSize(); i++) {
			TransactionBlock block = (TransactionBlock)chain.getBlock(i);
			displayBlock(block, out, level+1);
		}
		
		displayTab(out, level, "}");
	}
	
}
