package com.testing.inter;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.testing.common.AutoLogger;

public class HttpClientKw {
	//用于传递cookie的cookie池。
	private CookieStore cookieBag;

	//加一个用于指示是否使用cookie的标志位
	private boolean useCookie=true;

	//加一个 用于存储头域的Map
	public Map<String,String> headerMap;

	//加一个用于指示是否使用存储的头域Map的标志位
	private boolean useHeader=true;

	// 匹配unicode编码格式的正则表达式。
	private static final Pattern UNIPATTERN = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

	//构造方法中，先完成cookieStore的实例化。
	public HttpClientKw(){
		cookieBag=new BasicCookieStore();
		headerMap=new HashMap<>();
	}

	/**
	 * 查找字符串中的unicode编码并转换为中文。
	 * 
	 * @param u
	 * @return
	 */
	public static String DeCode(String u) {
		try {
			Matcher m = UNIPATTERN.matcher(u);
			StringBuffer sb = new StringBuffer(u.length());
			while (m.find()) {
				m.appendReplacement(sb, Character.toString((char) Integer.parseInt(m.group(1), 16)));
			}
			m.appendTail(sb);
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return u;
		}
	}

	/**
	 * SSLcontext用于绕过ssl验证，使发包的方法能够对https的接口进行请求。
	 */
	private static SSLContext createIgnoreVerifySSL() {

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSLv3");
			sc.init(null, new TrustManager[] { trustManager }, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sc;
	}

	/**
	 * 用于完成httpclient的创建
	 * 
	 * @return 返回创建好的httpclient对象用于发包。
	 */
	private CloseableHttpClient createClient() {
		// 采用绕过验证的方式处理https请求
		SSLContext sslcontext = createIgnoreVerifySSL();
		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		//如果要使用cookiestore，就在创建时带上setdefaultcookieStore方法。
		CloseableHttpClient client;
		// 创建自定义的httpclient对象
		if(useCookie) {
			//如果标志位为真，使用cookie，则带上cookieStore
			client = HttpClients.custom().setConnectionManager(connManager).setDefaultCookieStore(cookieBag).build();
		}else{
			//否则不带cookieStore
			client = HttpClients.custom().setConnectionManager(connManager).build();
		}

		// 当需要进行代理抓包时，启用下面的代码。
		// 设置代理地址，适用于需要用fiddler抓包时使用，不用时切记注释掉这句！
//		HttpHost proxy = new HttpHost("localhost", 8888, "http");
//		CloseableHttpClient client = HttpClients.custom().setProxy(proxy).setConnectionManager(connManager).build();
		return client;
	}

	/**
	 * 通过httpclient实现get方法
	 * 
	 * @param url   接口的url地址
	 * @param param 接口的参数列表。
	 */
	public String doGet(String url, String param) {
		// 创建httpclient对象
		CloseableHttpClient client = createClient();
		// result为最终返回结果
		String result = "";
		try {
			// 拼接url和param，形成最终请求的url格式。
			String urlWithParam = "";
			if (param.length() > 0) {
				urlWithParam = url + "?" + param;
			} else {
				urlWithParam = url;
			}
			// 创建get方式请求对象
			HttpGet get = new HttpGet(urlWithParam);
			// 设置连接的超时时间
			// setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
			RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
			get.setConfig(config);
			// httpclient执行httpget方法。
			CloseableHttpResponse response = client.execute(get);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换返回实体为String类型
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 对结果中可能出现的unicode编码进行转换，转成中文
			result = DeCode(result);
			// 释放返回实体
			EntityUtils.consume(entity);
			// 关闭返回包
			response.close();
			client.close();
		} catch (Exception e) {
			// 当出现报错时，将报错信息记录作为结果。
			AutoLogger.log.error(e, e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		}
		return result;
	}

	/**
	 * 通过httpclient实现的以x-www-form-urlencoded格式传参的post方法
	 * 重载的doPostUrl方法，没有传递头域参数列表。
	 * @param url   接口的url地址
	 * @param param 接口的参数列表。
	 */
	public String doPostUrl(String url, String param) {
		// 创建httpclient对象
		CloseableHttpClient client = createClient();
		// result为最终返回结果
		String result = "";
		try {
			// 创建post方式请求对象
			HttpPost post = new HttpPost(url);
			// 设置连接的超时时间
			// setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
			RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
			post.setConfig(config);
			// 创建urlencoded格式的请求实体，设置编码为utf8
			StringEntity postParams = new StringEntity(param);
			postParams.setContentType("application/x-www-form-urlencoded");
			postParams.setContentEncoding("UTF-8");
			// 添加请求体到post请求中
			post.setEntity(postParams);
			
			// 执行请求操作，并获取返回包
			CloseableHttpResponse response = client.execute(post);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换返回实体为String类型
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 对结果中可能出现的unicode编码进行转换，转成中文
			result = DeCode(result);
			// 释放返回实体
			EntityUtils.consume(entity);
			// 关闭返回包
			response.close();
		} catch (Exception e) {
			// 当出现报错时，将报错信息记录作为结果。
			AutoLogger.log.error(e, e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		} finally {
			// 关闭httpclient对象，释放资源
			try {
				client.close();
			} catch (IOException e) {
				AutoLogger.log.error(e, e.fillInStackTrace());
			}
		}
		return result;
	}

	/**
	 *  使用json格式传参的post方法
	 * @param url
	 * @param param
	 * @return
	 */
	public String doPostJson(String url,String param){
		// 创建httpclient对象
		CloseableHttpClient client = createClient();
		// result为最终返回结果
		String result = "";
		try {
			// 创建post方式请求对象
			HttpPost post = new HttpPost(url);
			// 设置连接的超时时间
			// setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
			RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
			post.setConfig(config);
			// 创建urlencoded格式的请求实体，设置编码为utf8
			StringEntity postParams = new StringEntity(param);
			postParams.setContentType("application/json");
			postParams.setContentEncoding("UTF-8");
			// 添加请求体到post请求中
			post.setEntity(postParams);

			// 执行请求操作，并获取返回包
			CloseableHttpResponse response = client.execute(post);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换返回实体为String类型
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 对结果中可能出现的unicode编码进行转换，转成中文
			result = DeCode(result);
			// 释放返回实体
			EntityUtils.consume(entity);
			// 关闭返回包
			response.close();
		} catch (Exception e) {
			// 当出现报错时，将报错信息记录作为结果。
			AutoLogger.log.error(e, e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		} finally {
			// 关闭httpclient对象，释放资源
			try {
				client.close();
			} catch (IOException e) {
				AutoLogger.log.error(e, e.fillInStackTrace());
			}
		}
		return result;
	}

	/**
	 *
	 * @param url
	 * @param Fileparam
	 * @param textParam
	 * @return
	 */
	public String doPostFile(String url,String Fileparam,String... textParam){
		return "";
	}

	/**
	 *  指定格式的post方法请求。
	 * @param url
	 * @param param
	 * @param type
	 * @return
	 */
	public String doPost(String url,String param,String type){
		// 创建httpclient对象
		CloseableHttpClient client = createClient();
		// result为最终返回结果
		String result = "";
		try {
			// 创建post方式请求对象
			HttpPost post = new HttpPost(url);
			// 设置连接的超时时间
			// setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
			RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
			post.setConfig(config);
			//使用头域
			if(useHeader){
				//遍历用于存储头域的map，把每个键值对，作为头域的键和值进行添加
				for (String headerKey : headerMap.keySet()) {
					post.setHeader(headerKey,headerMap.get(headerKey));
				}
			}
			switch(type){
				case "text":
					StringEntity posttextParams = new StringEntity(param);
					posttextParams.setContentType("text/xml");
					posttextParams.setContentEncoding("UTF-8");
					post.setEntity(posttextParams);
					break;
				case "url":
					// 创建urlencoded格式的请求实体，设置编码为utf8
					StringEntity postParams = new StringEntity(param);
					postParams.setContentType("application/x-www-form-urlencoded");
					postParams.setContentEncoding("UTF-8");
					// 添加请求体到post请求中
					post.setEntity(postParams);
					break;
				case "json":
					// 创建urlencoded格式的请求实体，设置编码为utf8
					StringEntity postJsonPatams = new StringEntity(param);
					postJsonPatams.setContentType("application/json");
					postJsonPatams.setContentEncoding("UTF-8");
					// 添加请求体到post请求中
					post.setEntity(postJsonPatams);
					break;
				case "file":
					MultipartEntityBuilder meb = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
					JSONObject paramJson = JSON.parseObject(param);
					System.out.println(paramJson);
					for (String key : paramJson.keySet()) {
						meb.addBinaryBody(key,new File(paramJson.getString(key)));
					}
					//用字符串分割的方式进行参数拆分。
//					String[] params = param.split("(\\{\")|(\":\")|(\"\\})");
//					System.out.println(Arrays.toString(params));
//					System.out.println(params.length);
//					meb.addBinaryBody(params[1].trim(),new File(params[2].trim()));
					HttpEntity fileEntity = meb.build();
					post.setEntity(fileEntity);
					break;
			}
			// 执行请求操作，并获取返回包
			CloseableHttpResponse response = client.execute(post);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换返回实体为String类型
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 对结果中可能出现的unicode编码进行转换，转成中文
			result = DeCode(result);
			// 释放返回实体
			EntityUtils.consume(entity);
			// 关闭返回包
			response.close();
		} catch (Exception e) {
			// 当出现报错时，将报错信息记录作为结果。
			e.printStackTrace();
			AutoLogger.log.error(e, e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		} finally {
			// 关闭httpclient对象，释放资源
			try {
				client.close();
			} catch (IOException e) {
				AutoLogger.log.error(e, e.fillInStackTrace());
			}
		}
		return result;

	}

	/**
	 * 使用cookie
	 */
	public void useCookie(){
		useCookie=true;
	}

	/**
	 * 不使用cookie
	 */
	public void notUseCookie(){
		useCookie=false;
	}

	/**
	 * 清空cookieStore中存储的cookie
	 */
	public void clearCookie(){
		cookieBag=new BasicCookieStore();
	}

	/**
	 * 添加头域键值到headerMap中
	 * 使用存储的头信息
	 */
	public void useHeader(){
		useHeader=true;
	}

	/**
	 * 将头域键值对添加到管理头域的map中
	 * @param key
	 * @param Value
	 */
	public void addHeader(String key,String Value){
		headerMap.put(key,Value);
	}


	/**
	 * 不使用存储的头信息
	 */
	public void notUseHeader(){
		useHeader=false;
	}

	/**
	 * 清空保存头的map
	 */
	public void clearHeader(){
		headerMap=new HashMap<>();
	}


}
