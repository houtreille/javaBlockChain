package ebloodCoin.blockchain.crypto.security.encryption;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ECDSAsymetricEncryptionTest {

	@Test
	public void testEncrypt() {
		String s = "XXX";
		
		
		
		try {
			ECDSAsymetricEncryption encryption = new ECDSAsymetricEncryption();
			byte[] encodedText = encryption.encrypt(s);
			
			String encryptedObject = new String(encodedText);
			
			String decryptedObject = new String(encryption.decrypt(encodedText));
			
			System.out.println(decryptedObject);
			
			
			byte[] sig = encryption.getSignature(null, s);
			boolean verfied = encryption.verifySignature(null, s, sig);
			
			assertTrue(verfied);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
//	public static void main(String args[]) {
//		TestAsymetricEncryptionRSA test = new TestAsymetricEncryptionRSA();
//		test.testEncryption();
//	}
	
}
