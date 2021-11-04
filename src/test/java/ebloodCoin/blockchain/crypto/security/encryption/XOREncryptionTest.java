package ebloodCoin.blockchain.crypto.security.encryption;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
 

public class XOREncryptionTest {

	@Test
	public void testEncrypt() {
		//fail("Not yet implemented");
		
		String message = "XXX";
		
		XOREncryption encry = new XOREncryption();
		encry.setPassword("password+");
		
		try {
			byte[]  encryptedMessage = encry.encrypt(message);
			
			System.out.println(new String(encryptedMessage));
			
			byte[]  decryptedMessage = encry.decrypt(encryptedMessage);
			
			System.out.println(new String(decryptedMessage));
			
			assertTrue(message.equals(new String(decryptedMessage)));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
