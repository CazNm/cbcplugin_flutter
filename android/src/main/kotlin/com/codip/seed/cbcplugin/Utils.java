package com.codip.seed.cbcplugin;

public class Utils {
	public static String toHexString(byte[] input) {
		String hexString = "";
		int length = input.length;
		
		for(int i = 0; i < length; i++) {
			if(i != length - 1) {
				hexString += String.format("%02X:", input[i]);
			} else {
				hexString += String.format("%02X", input[i]);
			}
		}
		
		return hexString;
	}
	
	public static String toHexString(byte[] input, int length) {
		String hexString = "";		
		
		for(int i = 0; i < length; i++) {
			if(i != length - 1) {
				hexString += String.format("%02X ", input[i]);
			} else {
				hexString += String.format("%02X ", input[i]);
			}
		}
		
		return hexString;
	}
	
	public static byte[] subBytes(byte[] src, int begin, int end) {
		int length = end - begin + 1;
		byte[] result = new byte[length];
		
		System.arraycopy(src, begin, result, 0, length);
		
		return result;
	}
}
