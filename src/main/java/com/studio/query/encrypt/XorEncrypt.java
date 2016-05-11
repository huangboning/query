package com.studio.zqquery.encrypt;

public class XorEncrypt {
	// /**
	// * �����ܽ���
	// *
	// * @param buf
	// * @return
	// */
	// public static Byte[] XorEncrypt(byte[] buf) {
	// Byte[] data = new Byte[buf.length];
	// byte[] key = { (byte) 0x86 };
	// int keyIndex = 0;
	// for (int x = 0; x < buf.length; x++) {
	// data[x] = (byte) (buf[x] ^ key[keyIndex]);
	// if (++keyIndex == key.length) {
	// keyIndex = 0;
	// }
	// }
	// return data;
	// }
	/**
	 * �����ܽ��� ����һ���ص㣺 a^b = c c^b = a ���Լ򵥵㣬����ļӽ��ܶ���һ�����������
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] xorEncryptAndDecrypt(byte[] data) {
		byte[] xorData = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			xorData[i] = (byte) (data[i] ^ 0x86);
		}
		return xorData;
	}

	public static byte xorEncryptAndDecrypt(byte data) {
		byte xorData = (byte) (data ^ 0x86);
		return xorData;
	}
}
