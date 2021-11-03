package ebloodCoin.blockchain.crypto.security.encryption;

public interface Encryption {

	public byte[] encrypt(String objectToEncrypt) throws Exception;
	public byte[] decrypt(byte[] objectToDecrypt) throws Exception;
	
}
