package com.studio.zqquery.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.studio.zqquery.common.Configure;

/**
 * CC��ʾC++ RSA�ӽ���,C++ RSA ����
 * 
 * @author hbn
 * 
 */
public class CCRSAEncrypt {
	private static byte[] privateKeyData = null;

	/**
	 * ���ص���⣨windwosΪdll�ļ���unixΪso�ļ���
	 * 
	 * @param path
	 */
	public static void loadLib(String path) {
		System.load(path);
	}

	public static byte[] getPrivateKey() {
		if (privateKeyData == null) {
			try {
				File file = new File(Configure.rootPath + "commonkey"
						+ File.separator + "rsa_private_key.dat");
				InputStream in = new FileInputStream(file);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] data = new byte[1024];
				int index = in.read(data);
				while (index > -1) {
					out.write(data, 0, index);
					index = in.read(data);
				}
				in.close();
				privateKeyData = out.toByteArray();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return privateKeyData;
	}

	/**
	 * RSA����
	 * 
	 * @param publicKey
	 *            ��Կ
	 * @param inByte
	 *            �����ܵ��ֽ�
	 * @param outByte
	 *            ���ܺ���ֽ�?
	 */
	public native static void RsaEncrypt(byte[] publicKey, byte[] inByte,
			byte[] outByte);

	/**
	 * RSA����
	 * 
	 * @param privateKey
	 *            ˽Կ
	 * @param inByte
	 *            ����ܵ��ֽ�?
	 * @param outByte
	 *            ���ܺ���ֽ�?
	 */
	public native static void RsaDecrypt(byte[] privateKey, byte[] inByte,
			byte[] outByte);

}
