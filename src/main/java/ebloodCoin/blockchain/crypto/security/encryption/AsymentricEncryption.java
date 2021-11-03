package ebloodCoin.blockchain.crypto.security.encryption;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface AsymentricEncryption extends Encryption {

	public byte[] getSignature(PrivateKey key, String message) throws Exception;
	public boolean verifySignature(PublicKey key,String message, byte[] signatureToVerify) throws Exception;
}
