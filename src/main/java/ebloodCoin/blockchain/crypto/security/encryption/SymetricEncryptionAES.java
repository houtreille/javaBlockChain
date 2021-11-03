package ebloodCoin.blockchain.crypto.security.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SymetricEncryptionAES implements Encryption {

		
	KeyGenerator keyGen = null;
	SecretKey key = null;
	
	public SymetricEncryptionAES() {
		try {
			keyGen = KeyGenerator.getInstance("AES");
			key =keyGen.generateKey();
			
			 
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			keyGen.init(sr);
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public byte[] encrypt(String messageToEncrypt) throws Exception {
			
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);	
		byte[] cipherText = cipher.doFinal(messageToEncrypt.getBytes());
		
		return cipherText;
	}

	public byte[] decrypt(byte[] messageToDecrypt)  throws Exception {
		
		Cipher cipher2 = Cipher.getInstance("AES");
		cipher2.init(Cipher.DECRYPT_MODE, key);
		byte[] decoded = cipher2.doFinal(messageToDecrypt);
		
		return decoded;
	}
	
}
