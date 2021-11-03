package ebloodCoin.blockchain.crypto.security.encryption;

public class XOREncryption implements Encryption {

	private String password;
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public byte[] encrypt(byte[] key,String password) throws Exception {
		
		int more = 100;
		
		//byte[] p = hashing.messageDigestSHA256ToBytes(password); // extra layer of security
		byte[] p = password.getBytes();
		
		byte[] pwds = new byte[p.length * more];
		
		//repeat password to concatenate it 'more' times
		for (int i = 0, z = 0; i <more; i++) {
			for(int j = 0; j < p.length; j++, z++) {
				pwds[z]=p[j];
			}
		}
		
		byte[] result = new byte[key.length];
		
		int i = 0;
		
		//perform xor operation on every byte
		for (i = 0; i < key.length && i < pwds.length; i++) {
			result[i] = (byte) (key[i] ^ pwds[i]); //97^98=3  3^97=98 3^98=97 66^67
			//result[i] = (byte)((key[i] ^ pwds[i]) & 0xFF);
		}
		
		//copy the remaining bytes without any operation
		while(i < key.length) {
			result[i] = key[i];
			i++;
		}
		
		return result;
	}

	public byte[] decrypt(byte[] key, String password) throws Exception {
		return encrypt(key, password);
	}
	
	
	public byte[] encrypt(String objectToEncrypt) throws Exception {
		return encrypt(objectToEncrypt.getBytes(), password);
	}

	public byte[] decrypt(byte[] objectToDecrypt) throws Exception {
		return decrypt(objectToDecrypt, password);
	}
	
	public static void main(String args[]) {
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
