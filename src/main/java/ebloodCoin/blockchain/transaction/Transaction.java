package ebloodCoin.blockchain.transaction;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ebloodCoin.blockchain.crypto.hashing.Hashable;
import ebloodCoin.blockchain.crypto.hashing.HashingFunction;
import ebloodCoin.blockchain.crypto.hashing.SHA256HashingFunction;
import ebloodCoin.utils.SecurityUtils;
import ebloodCoin.utils.StringUtils;

public class Transaction implements Serializable, Hashable {

	private static final long serialVersionUID = 1L;
	public static final double TRANSACTION_FEE = 1.0; 
	private String hashId;
	private long timestamp;
	private long mySequentialNumber;
	
	private PublicKey sender;
	private PublicKey receivers[];
	private double fundToTransfers[];
	
	private List<UTXo> inputs;
	private List<UTXo> outputs;
	
	private byte[] signature = null;
	private boolean signed = false;
	
	private HashingFunction hashing = new SHA256HashingFunction();
	
	
	
	public Transaction(PublicKey sender, PublicKey receiver, double fundToTransfer, List<UTXo> inputs) {
		setUp(sender, new PublicKey[] {receiver}, new double[] {fundToTransfer}, inputs);
	}
	
	public Transaction(PublicKey sender, PublicKey receiver[], double fundToTransfer[], List<UTXo> inputs) {
		setUp(sender,receiver, fundToTransfer, inputs);
	}
	
	
	private void setUp(PublicKey sender, PublicKey receivers[], double fundToTransfers[], List<UTXo> inputs) {
		this.fundToTransfers = fundToTransfers;
		this.receivers = receivers;
		this.sender = sender;
		this.inputs = inputs;
		
		this.timestamp = new Date().getTime();
		this.mySequentialNumber = StringUtils.getUniqueId();
		this.outputs = new ArrayList<UTXo>();
		
		try {
			computeHashId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void signTransaction(PrivateKey key) {
		try {
			if(signature == null && !signed) { // be sure it safe
				signature = SecurityUtils.signTransaction(key, getMessageData());
				signed = true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Boolean verifyTransaction(){
		try {
			return SecurityUtils.verifySignature(sender, signature, getMessageData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	public String computeHashId() throws Exception {
		String message = getMessageData();
		hashId =  hashing.hash(message);
		
		return hashId;
	}
	
	private String getMessageData() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(Long.toHexString(timestamp));
		sb.append(Long.toHexString(mySequentialNumber));
		
		sb.append(StringUtils.getKeyString(sender));
		
		for (int i = 0; i < receivers.length; i++) {
			sb.append(StringUtils.getKeyString(receivers[i]));
		}
		
		for (int i = 0; i < fundToTransfers.length; i++) {
			Double fund = fundToTransfers[i];
			sb.append(Double.toHexString(fund));
		}
		
		for (UTXo utxo : inputs) {
			sb.append(utxo.getHashId());
		}
		
		return sb.toString();
		
	}
	
	public Double getTotalFundToTransfer() {
		
		Double total = 0.;
		
		for (int i = 0; i < fundToTransfers.length; i++) {
			total += fundToTransfers[i];
		}
		
		return total;
	}
	
	public void addOutputUTXO(UTXo utxo) {
		if(! signed) {
			outputs.add(utxo);
		}
	}
	
	public UTXo getOutputUTXO(int id) {
		return outputs.get(id);
	}
	
	public int getNumberOfOutputUTXo() {
		return outputs.size();
	}
	
	public int getNumberOfInputUTXo() {
		return inputs == null ? 0 : inputs.size();
	}
	
	public UTXo getInputUTXO(int id) {
		return inputs.get(id);
	}
	
	public List<UTXo> getOutputs() {
		return outputs;
	}

	@Override
	public boolean equals(Object obj) {
		return ((Transaction)obj).getHashId().equals(getHashId());
	}

	public String getHashId() {
		return hashId;
	}
	
	public void print() {
		System.out.println("Transaction{");
		System.out.println("\tID:"+getHashId());
		System.out.println("\tSender:"+StringUtils.getKeyString(sender));
		System.out.println("\ttotal fund to be transferred:"+ getTotalFundToTransfer());
		System.out.println("\tReceivers:");
		
		
		for (int i = 0; i < outputs.size() -1; i++) {
			System.out.println("\t\t"+StringUtils.getKeyString(outputs.get(i).getReceiver()) + " " + outputs.get(i).getAmount());
	
		}
		
		System.out.println("\tFees"+TRANSACTION_FEE);
		System.out.println("\tChange"+getOutputs().get(getOutputs().size() - 1).getAmount());
		
		System.out.println("\tSignature"+ verifyTransaction());
		System.out.println("}");
	}
	
	public static void main(String args[]) {
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
			for (UTXo in : inputs) {
				availableFund += in.getAmount();
			}
			
			
			double totalCost = trans.getTotalFundToTransfer() + TRANSACTION_FEE;
			
			for (int i = 0; i < receivers.length; i++) {
				UTXo outputForReceiver = new UTXo(trans.getHashId(), receivers[i], senderKey.getPublic(), fundToTransfer[i]);
				trans.addOutputUTXO(outputForReceiver);
			}
			
			UTXo change = new UTXo(trans.getHashId(), senderKey.getPublic(), senderKey.getPublic(), totalCost - availableFund);
			trans.addOutputUTXO(change);
			
			trans.signTransaction(senderKey.getPrivate());
			trans.print();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
}
	
	
