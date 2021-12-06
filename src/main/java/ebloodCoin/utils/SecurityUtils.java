package ebloodCoin.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import ebloodCoin.blockchain.crypto.hashing.HashingFunction;
import ebloodCoin.blockchain.crypto.hashing.SHA256HashingFunction;
import ebloodCoin.blockchain.crypto.security.encryption.Signable;
import ebloodCoin.blockchain.crypto.security.encryption.AsymetricEncryptionRSA;

public class SecurityUtils {

	private static Signable encryption = new AsymetricEncryptionRSA();
	private static HashingFunction hashing = new SHA256HashingFunction();
	
	public static boolean verifySignature(PublicKey sender, byte[] signature, String message) throws Exception {
		return encryption.verifySignature(sender, message, signature);
	}
	
	public static byte[] signTransaction(PrivateKey key, String message)  throws Exception {
		return encryption.getSignature(key, message);
	}
	
	public static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		KeyPair pair = generator.generateKeyPair();
		return pair;
	}
	
	public static HashingFunction getDefaultHashingFunction() {
		return hashing;
	}
	
	//Merkle tree
	public static String computeMerkleTreeRootHash(Object[] hashes) throws Exception{
		return computeMerkleTreeRootHash(hashes, 0, hashes.length - 1);
	}
	
	public static String computeMerkleTreeRootHash(Object[] hashes, int from, int end) throws Exception{
		
		int deep = end - from + 1;
		
		//bottom case
		if(deep == 1) {
			return hashes[end].toString();
		} else if(deep == 2) {
			return hashing.hash(hashes[from].toString() + hashes[end].toString());
		} else {
			int c = (from + end) / 2;
			String msg = computeMerkleTreeRootHash(hashes, from, c) + computeMerkleTreeRootHash(hashes, c + 1, end);
			return hashing.hash(msg);
		}
	}
	
	
	
	
}
