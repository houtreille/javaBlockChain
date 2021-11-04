package ebloodCoin.blockchain.crypto.security.encryption;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class SymetricEncryptionAESTest {

	@Test
	public void testEncrypt() {
		String s = new Date().toString();
		
		try {
			Encryption encryption = new SymetricEncryptionAES();
			byte[] encodedText = encryption.encrypt(s);
			
			String encryptedObject = new String(encodedText);
			
			String decryptedObject = new String(encryption.decrypt(encodedText));
			
			assertTrue(decryptedObject.equals(s));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
