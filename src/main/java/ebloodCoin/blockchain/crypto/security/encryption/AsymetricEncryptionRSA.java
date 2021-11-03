package ebloodCoin.blockchain.crypto.security.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

public class AsymetricEncryptionRSA implements AsymentricEncryption {

	KeyPairGenerator keyGenerator = null;
	KeyPair keypair = null;
	Cipher cipher = null;
	Signature signature = null;
	
	public AsymetricEncryptionRSA( ) {
		try {
			keyGenerator = KeyPairGenerator.getInstance("RSA");
			keyGenerator.initialize(1024);
			keypair = keyGenerator.generateKeyPair();
			cipher = Cipher.getInstance("RSA");
			signature = signature.getInstance("SHA256withRSA");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] encrypt(String objectToEncrypt, PrivateKey key) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(objectToEncrypt.getBytes());
	}
	
	public byte[] encrypt(String objectToEncrypt) throws Exception {
		//cipher.init(Cipher.ENCRYPT_MODE, keypair.getPublic());
		cipher.init(Cipher.ENCRYPT_MODE, keypair.getPrivate());
		return cipher.doFinal(objectToEncrypt.getBytes());
	}

	public byte[] decrypt(byte[] objectToDecrypt) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, keypair.getPublic());
		
		return cipher.doFinal(objectToDecrypt);
	}
	
	public byte[] decrypt(byte[] objectToDecrypt, PublicKey key) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, key);
		
		return cipher.doFinal(objectToDecrypt);
	}
	
	public byte[] getSignature(PrivateKey key, String message) throws Exception {
		if(key == null) {
			return getSignature(message);
		} else {
			signature.initSign(key);
			signature.update(message.getBytes());
			return signature.sign();
		}
	}
	
	public byte[] getSignature(String message) throws Exception {
		signature.initSign(keypair.getPrivate());
		signature.update(message.getBytes());
		return signature.sign();
	}


	public boolean verifySignature(String message, byte[] signatureToVerify) throws Exception {
		signature.initVerify(keypair.getPublic());
		signature.update(message.getBytes());
		return signature.verify(signatureToVerify);
	}
	
	public boolean verifySignature(PublicKey key,String message, byte[] signatureToVerify) throws Exception {
		if(key == null) {
			return verifySignature(message, signatureToVerify);
		} else {
			signature.initVerify(key);
			signature.update(message.getBytes());
			return signature.verify(signatureToVerify);
		}
	}
	
}
