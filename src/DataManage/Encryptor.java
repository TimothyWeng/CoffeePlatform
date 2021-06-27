package DataManage;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
	public static String encryption(String info) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		try {
			md5.update(info.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] resultBytes = md5.digest();
		StringBuilder sb = new StringBuilder();
		// Get complete hashed password in hex format
		for (int i = 0; i < resultBytes.length; i++) {
			sb.append(Integer.toString((resultBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
}
