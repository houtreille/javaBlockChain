package ebloodCoin.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import ebloodCoin.blockchain.crypto.hashing.HashingFunction;
import ebloodCoin.blockchain.crypto.hashing.SHA256HashingFunction;
import ebloodCoin.blockchain.crypto.security.encryption.AsymentricEncryption;
import ebloodCoin.blockchain.crypto.security.encryption.AsymetricEncryptionRSA;

public class SecurityUtils {

	private static AsymentricEncryption encryption = new AsymetricEncryptionRSA();
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
	
	
}
