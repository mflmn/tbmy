package org.thingsboard.server.transport.goldcard.util;

/**
 * @author shanmh
 */
public class CryptUtil {
	public static byte[] encrypt(byte[] bytes, String name, String password){
		byte[] key = ByteUtil.hexToByte(password);
		switch (name) {
		case "des":
			return DesUtil.Encrypt(bytes, key);

		default:
			return null;
		}
	}
	public static byte[] decrypt(byte[] bytes, String name, String password){
		byte[] key = ByteUtil.hexToByte(password);
		switch (name) {
		case "des":
			return DesUtil.Decrypt(bytes, key);

		default:
			return null;
		}
	}
}
