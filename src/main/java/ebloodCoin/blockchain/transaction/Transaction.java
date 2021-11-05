package ebloodCoin.blockchain.transaction;

import java.io.Serializable;
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
	
	
	public boolean prepareOutputUTXOs() {
		if(receivers.length != fundToTransfers.length) {
			return false;
		}
		
		double totalCost = getTotalFundToTransfer() + TRANSACTION_FEE;
		double available = 0.0;
		
		for (int i = 0; i < fundToTransfers.length; i++) {
			available += fundToTransfers[i];
		}
		
		if(available < totalCost) {
			return false;
		}
		
		outputs.clear();
		for (int i = 0; i < receivers.length; i++) {
			UTXo ut = new UTXo(getHashId(), receivers[i], sender, fundToTransfers[i]);
			outputs.add(ut);
		}
		
		UTXo change = new UTXo(getHashId(), sender, sender, available - totalCost);
		outputs.add(change);
		
		return true;
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
	
	public PublicKey getSender() {
		return sender;
	}

	public PublicKey[] getReceivers() {
		return receivers;
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
	
	
}
	
	
