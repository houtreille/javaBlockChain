package ebloodCoin.blockchain.crypto.security.encryption;

public class TestAsymetricEncryptionRSA {

	public void testEncryption() {
		String s = "XXX";
		
		try {
			AsymentricEncryption encryption = new AsymetricEncryptionRSA();
			byte[] encodedText = encryption.encrypt(s);
			
			String encryptedObject = new String(encodedText);
			
			String decryptedObject = new String(encryption.decrypt(encodedText));
			
			System.out.println(decryptedObject);
			
			
			byte[] sig = encryption.getSignature(null, s);
			boolean verfied = encryption.verifySignature(null, s, sig);
			
			assert(verfied);
			
			
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
