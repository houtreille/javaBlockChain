package ebloodCoin.blockchain.crypto.security.encryption;

public class TestXOREncryption {

	public void testEncryption() {
		
		String message = "XXX";
		
		XOREncryption encry = new XOREncryption();
		encry.setPassword("password+");
		
		try {
			byte[]  encryptedMessage = encry.encrypt(message);
			
			System.out.println(new String(encryptedMessage));
			
			byte[]  decryptedMessage = encry.decrypt(encryptedMessage);
			
			System.out.println(new String(decryptedMessage));
			
			assert(message.equals(new String(decryptedMessage)));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
