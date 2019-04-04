package org.thingsboard.server.transport.goldcard.util;

public class MACUtil {
	public static byte[] getMac(byte[] dt, int mlen) {
		int n, k, len, ilen;
		// original input String length
		len = dt.length;
		// 原始串和mac长度的余数
		k = len % mlen;
		n = mlen - k;
		ilen = len + n;
		byte[] bt = new byte[ilen];
		for (int i = 0; i < len; i++) {
			dt[i] = (byte) (dt[i] > 0x00 ? dt[i] : dt[i] + 256);
			bt[i] = dt[i];
		}
		bt[len] = 127;

		byte bo[] = new byte[mlen];
		for (int i = 0; i < ilen;) {
			for (int j = 0; j < mlen; j++) {
				bo[j] ^= bt[i];
				i++;
			}
		}

		for (int i = 0; i < mlen; i++) {
			// 如果遇到\r \n

			if ((bo[i] >= 0x00 ? bo[i] : bo[i] + 256) >= 0x80) { // "\r"
				bo[i] ^= 0x80;
			}

			if (bo[i] == 0x0d) { // "\r"
				bo[i] = 0x4d;
			}
			if (bo[i] == 0x0a) { // "\n"
				bo[i] = 0x4a;
			}
			if (bo[i] == 0x3a) { // ":"
				bo[i] = 0x7a;
			}
			if (bo[i] == 0x7c) { // "|"
				bo[i] = 0x3c;
			}
			if (bo[i] == 0x00) { // "0"
				bo[i] = 0x40;
			}

		}
		return bo;
	}
	public static void main(String[] args) {
		String a = "112233445544444444446666667777778807002220170726201707261100000000002200000000003300000000004400000000005500000000001100000022000000330000004400000011";
		System.out.println(ByteUtil.bytesToHex(MACUtil.getMac(ByteUtil.hexToByte(a), 8)).toUpperCase());
	}
}
