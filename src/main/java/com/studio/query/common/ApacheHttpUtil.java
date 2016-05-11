package com.studio.query.common;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ApacheHttpUtil {
	public static String sendGet(String url) {
		String result = "";
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Content-type",
					"hboring-coding-application/client-stream");

			response = httpclient.execute(httpGet);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			System.out.println(result);
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPost(String url, byte[] bodyData) {
		String result = "";
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-type",
					"hboring-coding-application/client-stream");
			// 拼接参数
			// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// nvps.add(new BasicNameValuePair("username", "vip"));
			// nvps.add(new BasicNameValuePair("password", "secret"));
			// httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			// byte[] postData = {};
			// byte[] codeData = ConversionTools.intToBytesH(251314);
			// byte[] bodyData =
			// "{\"account_id\":\"1\",\"account_name\":\"hbn\"}"
			// .getBytes("utf-8");
			// bodyData=DES3.encode(bodyData,
			// Constants.SECRET_KEY).getBytes("utf-8");
			// List<byte[]> postArrayData = new ArrayList<>();
			// postArrayData.add(codeData);
			// postArrayData.add(bodyData);
			// postData = ConversionTools.arraysByteToBytes(postArrayData);
			// System.out.println(new String(postData, "utf-8"));
			// StringEntity postEntity=new StringEntity(new
			// String(postData,"utf-8"));
			ByteArrayEntity postEntity = new ByteArrayEntity(bodyData);
			httpPost.setEntity(postEntity);
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String sendPostBySession(String url, byte[] loginData,byte[] bodyData) {
		String result = "";
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-type",
					"hboring-coding-application/client-stream");
			
			ByteArrayEntity loginEntity = new ByteArrayEntity(loginData);
			httpPost.setEntity(loginEntity);
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity respEntity = response.getEntity();
			result = EntityUtils.toString(respEntity, "utf-8");
			System.out.println(result);
			
			//Thread.sleep(59*1000);//模拟会话过期
			ByteArrayEntity postEntity = new ByteArrayEntity(bodyData);
			httpPost.setEntity(postEntity);
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String testSceneBySession(String url, byte[] loginData,byte[] switchData,byte[] bodyData) {
		String result = "";
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-type",
					"hboring-coding-application/client-stream");
			
			ByteArrayEntity loginEntity = new ByteArrayEntity(loginData);
			httpPost.setEntity(loginEntity);
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity respEntity = response.getEntity();
			result = EntityUtils.toString(respEntity, "utf-8");
			System.out.println(result);
			
			//Thread.sleep(59*1000);//模拟会话过期
			ByteArrayEntity switchEntity = new ByteArrayEntity(switchData);
			httpPost.setEntity(switchEntity);
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity switchRepEntity = response.getEntity();
			result = EntityUtils.toString(switchRepEntity, "utf-8");
			System.out.println(result);
			
			ByteArrayEntity postEntity = new ByteArrayEntity(bodyData);
			httpPost.setEntity(postEntity);
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
