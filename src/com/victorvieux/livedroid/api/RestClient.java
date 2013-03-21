package com.victorvieux.livedroid.api;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.victorvieux.livedroid.tools.CachedAsyncHttpResponseHandler;

public class RestClient {
	public static final String BASE_URL = "https://xboxapi.com/json/";
	private static AsyncHttpClient client;

	static {
		client = new AsyncHttpClient();	
		client.setTimeout(20 * 1000);
		try{
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			client.setSSLSocketFactory(sf);
		} catch (Exception e) {}
	}

	static public void getGames(Context context, CachedAsyncHttpResponseHandler responseHandler, String gamertag) {
		responseHandler.setContext(context);
		responseHandler.setUrl(BASE_URL + "games/" + gamertag.replace(" ", "%20"));
		client.get(context, BASE_URL + "games/" + gamertag.replace(" ", "%20"), null, responseHandler);
	}

	static public void get(Context context, CachedAsyncHttpResponseHandler responseHandler, String url) {
		responseHandler.setContext(context);
		responseHandler.setUrl(url);
		client.get(context, url, null, responseHandler);
	}






	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
