package ebloodCoin.blockchain.crypto.security.encryption;

import java.util.Date;

public class TestSymetricEncryptionAES {

	public void testEncryption() {
		String s = new Date().toString();
		
		try {
			Encryption encryption = new SymetricEncryptionAES();
			byte[] encodedText = encryption.encrypt(s);
			
			String encryptedObject = new String(encodedText);
			
			String decryptedObject = new String(encryption.decrypt(encodedText));
			
			assert(decryptedObject.equals(s));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
