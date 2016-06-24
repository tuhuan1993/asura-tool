package com.asura.tools.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import sun.net.ConnectionResetException;
import sun.net.ConnectionResetException;

public class HttpUtil {
	public static HashMap<String, String> getParameters(String url) {
		HashMap map = new HashMap();
		int index = url.indexOf("?");
		if (index > 0) {
			String subUrl = url.substring(index + 1);
			String[] ss = StringUtil.split(subUrl, "&");
			for (String s : ss) {
				int ei = s.indexOf("=");
				if (ei <= 0)
					continue;
				try {
					map.put(s.substring(0, ei), URLDecoder.decode(s.substring(ei + 1), "utf8"));
				} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
				}
			}
		}
		return map;
	}

	public static String getContent(String urlStr, String encoding) {
		try {
			URL url = new URL(urlStr);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), encoding));

			String s = "";

			StringBuffer sb = new StringBuffer("");

			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}

			br.close();
			return sb.toString();
		} catch (Exception e) {
			return "error open url:" + urlStr + "\n" + ExceptionUtil.getExceptionContent(e);
		}
	}

	public static byte[] fetchBytes(String url) {
		HttpClient client = getHttpClient();

		byte[] content = null;
		HttpGet method = new HttpGet(url);
		try {
			HttpResponse response = client.execute(method);
			content = EntityUtils.toByteArray(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

	public static String fetchContent(String urlStr, String encoding) throws ConnectionResetException {
		HttpClient client = getHttpClient();

		byte[] content = null;
		HttpGet method = new HttpGet(urlStr);
		try {
			HttpResponse response = client.execute(method);
			content = EntityUtils.toByteArray(response.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			if (e.getLocalizedMessage().equalsIgnoreCase("Read timed out")) {
				System.out.println("Read timed out ->" + urlStr);
			} else {
				if (e.getLocalizedMessage().equalsIgnoreCase("Connection reset")) {
					throw new ConnectionResetException();
				}
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.getConnectionManager().closeExpiredConnections();
				client.getConnectionManager().closeIdleConnections(30L, TimeUnit.SECONDS);
			}
			method.abort();
		}

		if (content != null) {
			try {
				return new String(content, encoding);
			} catch (UnsupportedEncodingException e) {
				System.out.println("error encoding = " + encoding + " use utf-8 replace it!");
				try {
					return new String(content, "utf-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					return "";
				}
			}
		}
		return null;
	}

	public static HttpParams getSimpleHttpParams() {
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		HttpConnectionParams.setConnectionTimeout(params, 30000);

		HttpConnectionParams.setSoTimeout(params, 60000);

		HttpClientParams.setRedirecting(params, true);
		ConnManagerParams.setMaxTotalConnections(params, 1000);

		return params;
	}

	public static HttpClient getHttpClient(HttpParams params) {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		HttpClient client = new DefaultHttpClient(cm, params);

		return client;
	}

	public static HttpClient getHttpClient() {
		return getHttpClient(getSimpleHttpParams());
	}

	public static HttpClient getProxyClient(String host, int port) {
		HttpClient client = getHttpClient();
		if (!(StringUtil.isNullOrEmpty(host))) {
			HttpHost proxy = new HttpHost(host, port);
			client.getParams().setParameter("http.route.default-proxy", proxy);
		}

		return client;
	}

	public static String post(String urlString, HashMap<String, String> params) {
		List result = new ArrayList();
		try {
			URL postUrl = new URL(urlString);

			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			List list = new ArrayList();
			for (String key : params.keySet()) {
				list.add(key + "=" + URLEncoder.encode((String) params.get(key), "utf-8"));
			}
			String content = StringUtil.getStringFromStrings(list, "&");
			out.writeBytes(content);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line = "";

			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
			reader.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return StringUtil.getStringFromStrings(result, "\n");
	}

	public static String getContent(String strUrl) {
		return getContent(strUrl, "utf8");
	}

	public static void main(String[] args) {
		try {
			System.out.println(fetchContent("http://www.163.com", "utf-8"));
		} catch (ConnectionResetException e) {
			e.printStackTrace();
		}
	}
}
