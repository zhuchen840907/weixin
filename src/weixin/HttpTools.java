package weixin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

public class HttpTools {

	/**
	 * 
	 * @param url
	 * @param connTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String doGet(String url, int connTimeout, int readTimeout) 
	{
		String result = "";
		if (url == null || "".equals(url)) {
			return null;
		}
		HttpGet httpGet = null;
		HttpClient httpClient = null;
		try {

			httpClient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), connTimeout);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), readTimeout);
			httpGet = new HttpGet(url);
			HttpResponse reponse = httpClient.execute(httpGet);
			result = EntityUtils.toString(reponse.getEntity(), "utf-8");

		} catch (ClientProtocolException e) {
			// to do
		} catch (IOException e) {
			// TODO Auto-generated catch block

		}

		return result;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param connTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String doPost(String url, Object params, int connTimeout, int readTimeout)
	{
		String result = "";
		if (url == null || "".equals(url)) {
			return null;
		}
		HttpPost httpPost = null;
		HttpClient httpClient = null;
		try {

			httpClient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), connTimeout);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), readTimeout);
			httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(params.toString(),"utf-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpResponse reponse = httpClient.execute(httpPost);
			result = EntityUtils.toString(reponse.getEntity(), "utf-8");

		} catch (ClientProtocolException e)
		{
			// to do
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block

		}

		return result;
	}
	
	/**
	 * up file
	 * 
	 * @param url
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String upFile(String url, String filePath) throws IOException 
	{

		String result = null;

		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) 
		{
			throw new IOException("文件不存在");
		}

		/**
		 * 第一部分
		 */
		URL urlObj = new URL(url);
		// 连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		/**
		 * 设置关键值
		 */
		con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post方式不能使用缓存

		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		// 请求正文信息

		// 第一部分：
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:image/jpg\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");

		// 获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// 输出表头
		out.write(head);
		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) 
		{
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
				// 定义BufferedReader输入流来读取URL的响应
				reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) 
				{
					// System.out.println(line);
					buffer.append(line);
				}
				if (result == null) 
				{
					result = buffer.toString();
				}
		   } catch (IOException e) 
		   {
				System.out.println("发送POST请求出现异常！" + e);
				e.printStackTrace();
				throw new IOException("数据读取异常");
		   } finally 
		   {
			if (reader != null) 
			{
				reader.close();
			}

		}

		return result;

	}

}
