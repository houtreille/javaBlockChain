package ebloodCoin.blockchain.crypto.hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class SHA256HashingFunction implements HashingFunction {

	public String hash(String object) throws Exception {
		return messageDigestSHA256ToString(object);
	}
	
	public String messageDigestSHA256ToString(String msgToHash) throws Exception {
		return Base64.getEncoder().encodeToString((messageDigestSHA256ToBytes(msgToHash)));
	}

	public byte[] messageDigestSHA256ToBytes(String msgToHash) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		final byte[] encodedhash = digest.digest(msgToHash.getBytes(StandardCharsets.UTF_8));
		
		return encodedhash;
	}

}
