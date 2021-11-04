package ebloodCoin.utils;

import java.security.Key;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class StringUtils {

	private static long uniqueId = 0L;

	public static long getUniqueId() {
		uniqueId++;
		return uniqueId;
	}


	public static String getMiningDifficulatyString(int diff) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < diff; i++) {
			sb.append("0");
		}
		
		return sb.toString();
		
	}
	
	
	public static boolean hasMeetDifficultyLevel(String hash, int difficultyLevel) {
		char[] array = hash.toCharArray();
		
		for (int i = 0; i < difficultyLevel; i++) {
			if(array[i] != '0') {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Converts a hash in bytes into a bit String
	 */
	public static String toBinaryString(byte hash[]) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < hash.length; i++) {
			String s = Integer.toBinaryString((int)hash[i]);
			while(s.length() < 8) {
				s = "0"+s; // because Integer.toBinaryString TRUNCATE leading zero
			}
			sb.append(s);
		}
		
		return sb.toString();
	}
	
	public static String getKeyString(Key key) {
		return java.util.Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
}
