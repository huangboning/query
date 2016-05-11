package com.studio.zqquery.encrypt;

import java.util.Random;

/**
 * 这个算法简单,而且效率高,每次可以操作8个字节的数据,加密解密的KEY为16字节,即包含4个int数据的int型数组,加密轮数应为8的倍数,
 * 一般比较常用的轮数为64,32,16,推荐用64轮.
 * 
 * @author hbn
 * 
 */
public class JAVATEAEncrypt {

	public static int[] TEA_KEY = null;
	public static String DYNAMIC_PASSWORD = null;

	/**
	 * 加密
	 * 
	 * @param content
	 * @param offset
	 * @param key
	 * @param times
	 *            密轮数
	 * @return
	 */
	public static byte[] encrypt(byte[] content, int offset, int[] key,
			int times) {
		int[] tempInt = byteToInt(content, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for (i = 0; i < times; i++) {
			sum += delta;
			y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
		}
		tempInt[0] = y;
		tempInt[1] = z;
		return intToByte(tempInt, 0);
	}

	/**
	 * 解密
	 * 
	 * @param encryptContent
	 * @param offset
	 * @param key
	 * @param times
	 * @return
	 */
	public static byte[] decrypt(byte[] encryptContent, int offset, int[] key,
			int times) {
		int[] tempInt = byteToInt(encryptContent, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0xC6EF3720, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for (i = 0; i < times; i++) {
			z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
			y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			sum -= delta;
		}
		tempInt[0] = y;
		tempInt[1] = z;

		return intToByte(tempInt, 0);
	}

	// byte[]型数据转成int[]型数据
	private static int[] byteToInt(byte[] content, int offset) {

		int[] result = new int[content.length >> 2];
		// 除以2的n次方 == 右移n位 即content.length / 4 ==content.length >> 2
		for (int i = 0, j = offset; j < content.length; i++, j += 4) {
			result[i] = transform(content[j + 3])
					| transform(content[j + 2]) << 8
					| transform(content[j + 1]) << 16 | (int) content[j] << 24;
		}
		return result;

	}

	// int[]型数据转成byte[]型数据
	private static byte[] intToByte(int[] content, int offset) {
		byte[] result = new byte[content.length << 2];
		// 乘以2的n次方 == 左移n位 即content.length * 4 ==content.length<< 2
		for (int i = 0, j = offset; j < result.length; i++, j += 4) {
			result[j + 3] = (byte) (content[i] & 0xff);
			result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
			result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
			result[j] = (byte) ((content[i] >> 24) & 0xff);
		}
		return result;
	}

	// 若某字节被解释成负的则需将其转成无符号正数
	private static int transform(byte temp) {
		int tempInt = (int) temp;
		if (tempInt < 0) {
			tempInt += 256;
		}
		return tempInt;
	}

	/**
	 * 如果你想一次处理大于8个字节的数据,需要再封装一下在加密
	 * 
	 * @param info
	 * @return
	 */
	public static byte[] encryptByTea(byte[] content, int[] key, int times) {
		int n = 8 - content.length % 8; // 若content的位数不足8的倍数,需要填充的位数
		byte[] encryptData = new byte[content.length + n];
		encryptData[0] = (byte) n;
		System.arraycopy(content, 0, encryptData, n, content.length);
		byte[] packEncryptData = new byte[encryptData.length];
		for (int offset = 0; offset < packEncryptData.length; offset += 8) {
			byte[] tempEncrpt = encrypt(encryptData, offset, key, times);
			System.arraycopy(tempEncrpt, 0, packEncryptData, offset, 8);
		}
		return packEncryptData;
	}

	/**
	 * 封装解密
	 * 
	 * @param content
	 * @param key
	 * @param times
	 * @return
	 */
	public static byte[] decryptByTea(byte[] content, int[] key, int times) {
		byte[] decryptData = new byte[content.length];
		for (int offset = 0; offset < content.length; offset += 8) {
			byte[] tempDecrpt = decrypt(content, offset, key, times);
			System.arraycopy(tempDecrpt, 0, decryptData, offset, 8);
		}
		int n = decryptData[0];
		byte[] packDecryptData = new byte[decryptData.length - n];
		System.arraycopy(decryptData, n, packDecryptData, 0, decryptData.length
				- n);
		return packDecryptData;
	}

	public static int[] getTeaKey() {
		if (TEA_KEY == null) {
			Random random = new Random();
			int key1 = random.nextInt();
			int key2 = random.nextInt();
			int key3 = random.nextInt();
			int key4 = random.nextInt();
			TEA_KEY = new int[4];
			TEA_KEY[0] = key1;
			TEA_KEY[1] = key2;
			TEA_KEY[2] = key3;
			TEA_KEY[3] = key4;
		}
		return TEA_KEY;
	}

	public static String getDynamicPassword() {
		String dynamicPassword = "";
		String[] pwdstr = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
				"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
				"y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		Random random = new Random();
		for (int k = 0; k < 6; k++) {
			int i = random.nextInt(62);
			String s = pwdstr[i];
			dynamicPassword = dynamicPassword + s;
		}
		return dynamicPassword;
	}

	public static void main(String[] args) {
		// // 加密解密所用的KEY
		// int[] KEY = new int[] { 0x789f5645, 0xf68bd5a4, 0x81963ffa,
		// 0x458fac58 };
		// JAVATEAEncrypt tea = new JAVATEAEncrypt();
		// byte[] info = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		// System.out.print("原数据：");
		// for (byte i : info) {
		// System.out.print(i + " ");
		// }
		// System.out.println();
		// byte[] secretInfo = tea.encrypt(info, 0, KEY, 32);
		// System.out.print("加密后的数据：");
		// for (byte i : secretInfo) {
		// System.out.print(i + " ");
		// }
		// System.out.println();
		// byte[] decryptInfo = tea.decrypt(secretInfo, 0, KEY, 32);
		// System.out.print("解密后的数据：");
		// for (byte i : decryptInfo) {
		// System.out.print(i + " ");
		// }
		// int[] KEY = new int[] { 0x789f5645, 0xf68bd5a4, 0x81963ffa,
		// 0x458fac58 };
		int[] KEY = getTeaKey();
		String info = "www.blogjava.net/orangehf/abc";
		System.out.println("原数据：" + info);

		byte[] encryptInfo = JAVATEAEncrypt.encryptByTea(info.getBytes(), KEY,
				32);
		System.out.print("加密后的数据：");
		for (byte i : encryptInfo) {
			System.out.print(i + " ");
		}
		System.out.println();

		byte[] decryptInfo = JAVATEAEncrypt.decryptByTea(encryptInfo, KEY, 32);
		System.out.print("解密后的数据：");
		System.out.println(new String(decryptInfo));
	}
}
