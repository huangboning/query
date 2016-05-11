package com.studio.zqquery.encrypt;

public class CCTEAEncrypt {
	public static void loadLib(String path) {
		System.load(path);
	}

	/**
	 * TEA加密
	 * 
	 * @param key
	 * @param inByte
	 * @param outByte
	 */
	public native static void TeaEncrypt(byte[] key, byte[] inByte,
			byte[] outByte);

	/**
	 * TEA解密
	 * 
	 * @param key
	 * @param inByte
	 * @param outByte
	 * @return
	 */
	public native static boolean TeaDecrypt(byte[] key, byte[] inByte,
			byte[] outByte);

}
