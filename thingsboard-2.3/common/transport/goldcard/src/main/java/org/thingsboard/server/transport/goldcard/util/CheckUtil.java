package org.thingsboard.server.transport.goldcard.util;

import io.netty.buffer.ByteBufUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author shanmh
 */
public class CheckUtil {
	
	public static boolean check(byte[] bytes, String checkType){
		String checkName = checkType.split("_")[0];
		switch (checkName) {
		case "crc16":
			return checkCRC16(bytes);
		case "cs":
			return checkCS(bytes);
		case "crc16-ccitt":
			return checkCrc16ccitt(bytes, checkType.split("_")[2]);
			
		default:
			return false;
		}
	}
	
	public static String getCheck(byte[] bytes, String checkType){
		String checkName = checkType.split("_")[0];
		switch (checkName) {
		case "crc16":
			return getCRC16(bytes);
		case "cs":
			return getCS(bytes);
		case "crc16-ccitt":
			return getCrc16ccitt(bytes, checkType.split("_")[2]);
		case "mac":
			return getMAC(bytes,checkType.split("_")[2],checkType.split("_")[1]);
		default:
			return null;
		}
	}
	
	
	/**
	 * CRC16 CCITT标准[ x16+x15+x2+1   8005] 
	 */
	private static String getCRC_8005(byte[] data) {
		int wCRC =  0xffff;
		for (int k = 0; k <data.length; k++)
		{
			wCRC = (wCRC ^ (int)(0xFF & data[k]));
			for (int i = 0; i < 8; i++)
			{
				if ((wCRC & 0x0001) > 0)
					wCRC = (int)(wCRC >> 1 ^ 0xA001);
				else
					wCRC = (int)(wCRC >> 1);
			}
		}
		wCRC = (int)((wCRC << 8) | ((wCRC >> 8) & 0xFF));
		
		String LO = Integer.toHexString(wCRC &0xFF);
		if(LO.length()==1){
			LO = "0"+LO;
		}
		
		String HI = Integer.toHexString((wCRC >>8)&0xFF);
		if(HI.length()==1){
			HI = "0" + HI ;
		}
		
		return HI+LO; 
	}

	
	private static boolean checkCrc16ccitt(byte[] bytes, String key){
		byte[] temp = Arrays.copyOf(bytes, bytes.length - 2);
		String check = getCrc16ccitt(temp, key);
		return ByteBufUtil.hexDump(bytes, bytes.length - 2, 2).equals(check);
	}
	
	private static String getCrc16ccitt(byte[] bytes, String key){
		if("8005".equals(key)){
			return getCRC_8005(bytes);
		}
		int crc = 0x00; // initial value
		for (int index = 0; index < bytes.length; index++) {
			byte b = bytes[index];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= Integer.parseInt(key, 16);
			}
		}
		crc &= 0xffff;
		
		return StringUtils.leftPad(Integer.toHexString(crc), 4, "0");
	}
	
	private static boolean checkCS(byte[] bytes){
		byte[] temp = Arrays.copyOf(bytes, bytes.length - 1);
		String cs = getCS(temp);
		return ByteBufUtil.hexDump(bytes, bytes.length - 1, 1).equals(cs);
	}
	
	private static String getCS(byte[] bytes){
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		int sum = 0;
		for (byte b : bytes) {
			// 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
			tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
			{
				tmp = "0" + tmp;
			}
			sum = sum + Integer.parseInt(tmp.toString(), 16);
			sb.append(tmp);
		}
		sum = sum % 256;
		String result = Integer.toHexString(sum);
		if(result.length() == 1){
			result = "0" + result;
		}
		return result;
	}
	
	public static String getCRC16(byte[] bytes){
		return getCRC16(bytes, 0, bytes.length);
	}
	
	public static String getCRC16(byte[] bytes, int offset, int length){
		int crc = calculateCRC16(bytes, offset, length);
		byte[] res = new byte[]{(byte) (crc & 0x00ff), (byte) (crc >> 8)};
		return ByteUtil.bytesToHex(res);
	}
	
	public static boolean checkCRC16(byte[] bytes){
		int len = bytes.length;
		int res = calculateCRC16(bytes, 0, len - 2);
		return res == ByteUtil.l2ByteToInt(bytes[len - 2], bytes[len - 1]);
	}
	
	public static int calculateCRC16(byte[] bytes, int offset, int length){
		return CRC16Util.calcCrc16(bytes, offset, length);
	}
	
	public static int calculateCRC16(byte[] bytes){
		return calculateCRC16(bytes, 0, bytes.length);
	}
	public static String getMAC(byte[] data,String start, String resultLength){
		byte[] needMac = new byte[data.length-Integer.valueOf(start)];
		System.arraycopy(data, Integer.valueOf(start), needMac, 0, data.length-Integer.valueOf(start));
		String result = ByteUtil.bytesToHex(MACUtil.getMac(needMac, Integer.valueOf(resultLength)));
		return result;
	}
	public static void main(String[] args) {
		
	}
}
