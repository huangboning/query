package com.studio.query.encrypt;

import java.util.Map;

public class JAVARSATester {
	static String publicKey;
	static String privateKey;

	static {
		try {
			Map<String, Object> keyMap = JAVARSAEncrypt.genKeyPair();
			publicKey = JAVARSAEncrypt.getPublicKey(keyMap);
			privateKey = JAVARSAEncrypt.getPrivateKey(keyMap);
			System.err.println("公钥: \n\r" + publicKey);
			System.err.println("私钥： \n\r" + privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		test();
		testSign();
	}

	static void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
		System.out.println("\r加密前文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = JAVARSAEncrypt.encryptByPublicKey(data, publicKey);
		System.out.println("加密后文字：\r\n" + new String(encodedData));
		byte[] decodedData = JAVARSAEncrypt.decryptByPrivateKey(encodedData, privateKey);
		String target = new String(decodedData);
		System.out.println("解密后文字: \r\n" + target);
	}

	// static void testSign() throws Exception {
	// System.err.println("私钥加密——公钥解密");
	// String source = "这是一行测试RSA数字签名的无意义文字";
	// System.out.println("原文字：\r\n" + source);
	// byte[] data = source.getBytes();
	// byte[] encodedData = JAVARSAEncrypt.encryptByPrivateKey(data,
	// privateKey);
	// System.out.println("加密后：\r\n" + new String(encodedData));
	// byte[] decodedData = JAVARSAEncrypt.decryptByPublicKey(encodedData,
	// publicKey);
	// String target = new String(decodedData);
	// System.out.println("解密后: \r\n" + target);
	// System.err.println("私钥签名——公钥验证签名");
	// String sign = JAVARSAEncrypt.sign(encodedData, privateKey);
	// System.err.println("签名:\r" + sign);
	// boolean status = JAVARSAEncrypt.verify(encodedData, publicKey, sign);
	// System.err.println("验证结果:\r" + status);
	// }
	static void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");
		String source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = JAVARSAEncrypt.encryptByPublicKey(data, publicKey);
		System.out.println("加密后：\r\n" + new String(encodedData));
		byte[] decodedData = JAVARSAEncrypt.decryptByPrivateKey(encodedData, privateKey);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);
		System.err.println("私钥签名——公钥验证签名");
		String sign = JAVARSAEncrypt.sign(encodedData, privateKey);
		System.err.println("签名:\r" + sign);
		boolean status = JAVARSAEncrypt.verify(encodedData, publicKey, sign);
		System.err.println("验证结果:\r" + status);
	}
}
