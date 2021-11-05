package ebloodCoin.blockchain.crypto.security.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ECDSAsymetricEncryption implements AsymentricEncryption {

	KeyPairGenerator keyGenerator = null;
	KeyPair keypair = null;
	Cipher cipher = null;
	Signature signature = null;
	
	public ECDSAsymetricEncryption( ) {
		
		try {
			//add in jre/lib/security bouncey castle provider
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
			keypair = keyGen.generateKeyPair(); 
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
	
	public byte[] encrypt(String objectToEncrypt) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, keypair.getPrivate());
		return cipher.doFinal(objectToEncrypt.getBytes());
	}

	public byte[] decrypt(byte[] objectToDecrypt) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, keypair.getPublic());
		return cipher.doFinal(objectToDecrypt);
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

	public byte[] getSignature(PrivateKey key, String message) throws Exception {
		if(key == null) {
			return getSignature(message);
		} else {
			signature.initSign(key);
			signature.update(message.getBytes());
			return signature.sign();
		}
	}
	
}
