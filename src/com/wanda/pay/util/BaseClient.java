package com.wanda.pay.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import android.content.Context;

import com.wanda.pay.R;

public class BaseClient {


	//	protected static String _httpsPost(String path, Map<String, String> params,Context context)
	//			throws Exception {
	//		try {
	//			HttpURLConnection conn = null;
	//			// 1> 组拼实体数据
	//			StringBuilder entityBuilder = new StringBuilder("");
	//			if (params != null && !params.isEmpty()) {
	//				for (Map.Entry<String, String> entry : params.entrySet()) {
	//					entityBuilder.append(entry.getKey()).append('=');
	//					entityBuilder.append(URLEncoder.encode(entry.getValue(),
	//							"utf-8"));
	//					entityBuilder.append('&');
	//				}
	//				entityBuilder.deleteCharAt(entityBuilder.length() - 1);
	//			}
	//			byte[] entity = entityBuilder.toString().getBytes();
	//			URL url = new URL(path);
	//			conn = (HttpURLConnection) url.openConnection();
	//			conn.setConnectTimeout(30 * 1000);
	//			conn.setReadTimeout(30 * 1000);
	//			conn.setRequestMethod("POST");
	//			conn.setDoOutput(true);// 允许输出数据
	//			conn.setRequestProperty("Content-Type",
	//					"application/x-www-form-urlencoded");
	//			conn.setRequestProperty("Content-Length",
	//					String.valueOf(entity.length));
	//			OutputStream outStream = conn.getOutputStream();
	//			outStream.write(entity);
	//			outStream.flush();
	//			outStream.close();
	//			InputStream stream = conn.getInputStream();
	//			int responseCode = conn.getResponseCode();
	//			if (conn.getResponseCode() == 200) {
	//				return InputStreamTOString(stream);
	//			}
	//			return InputStreamTOString(stream);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			throw e;
	//		}
	//
	//	}



	/**
	 * https 网络请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws Exception
	 */
	protected static String _httpsPost(String url, Map<String, String> map,Context mContext)
			throws Exception {
		String responseBody = "";
		try {
			responseBody = sendPOSTRequestForInputStream(url, map, "utf-8", 50,
					50,mContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return responseBody;
	}

	private static final X509TrustManager xtm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
	private static X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
	private static HttpsURLConnection conn = null;
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	public static String sendPOSTRequestForInputStream(String path,
			Map<String, String> params, String encoding, int connectTime,
			int readTime
			,Context mContext
			) {
		try {
			// 1> 组拼实体数据
			// method=save&name=liming&timelength=100
			StringBuilder entityBuilder = new StringBuilder("");
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					entityBuilder.append(entry.getKey()).append('=');
					entityBuilder.append(URLEncoder.encode(entry.getValue(),
							encoding));
					entityBuilder.append('&');
				}
				entityBuilder.deleteCharAt(entityBuilder.length() - 1);
			}
			LogUtil.i("path=" + path + "  entityBuilder::::"
					+ entityBuilder.toString());
			byte[] entity = entityBuilder.toString().getBytes();

			URL url = new URL(path);
			conn = (HttpsURLConnection) url.openConnection();
			if (conn instanceof HttpsURLConnection) {
				/**
				 * https请求启用代码 开始

				 try{
				 KeyStore ts = KeyStore.getInstance("BKS");
				 ts.load(mContext.getResources().openRawResource(R.raw.ca),
				 "wonders".toCharArray());

				 String tmfAlgorithm =
				 TrustManagerFactory.getDefaultAlgorithm();
				 TrustManagerFactory tmf =
				 TrustManagerFactory.getInstance(tmfAlgorithm);
				 tmf.init(ts);

				 // Create an SSLContext that uses our TrustManager
				 SSLContext context = SSLContext.getInstance("TLS");
				 context.init(null, tmf.getTrustManagers(), null);
				 conn.setSSLSocketFactory(context.getSocketFactory());
				 conn.setHostnameVerifier(DO_NOT_VERIFY);
				 }catch(Exception e){
				 e.printStackTrace();
				 }

				 * https请求启用代码 结束
				 */

				/**
				 * http请求启用代码 开始
				 */
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(new KeyManager[0], xtmArray, new SecureRandom());
				SSLSocketFactory socketFactory = context.getSocketFactory();
				((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
				((HttpsURLConnection) conn)
				.setHostnameVerifier(HOSTNAME_VERIFIER);
				/**
				 * http请求启用代码 结束
				 */

			}
			conn.setConnectTimeout(connectTime * 1000);
			conn.setReadTimeout(readTime * 1000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);// 允许输出数据
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",
					String.valueOf(entity.length));
			OutputStream outStream = conn.getOutputStream();
			outStream.write(entity);
			outStream.flush();
			outStream.close();
			InputStream stream = conn.getInputStream();
			int responseCode = conn.getResponseCode();
			if (conn.getResponseCode() == 200) {
				return InputStreamTOString(stream);
			}
			return InputStreamTOString(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	final static int BUFFER_SIZE = 2048;

	public static String InputStreamTOString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		data = null;
		return new String(outStream.toByteArray());
	}

}
