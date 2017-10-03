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
			throw new IOException("�ļ�������");
		}

		/**
		 * ��һ����
		 */
		URL urlObj = new URL(url);
		// ����
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		/**
		 * ���ùؼ�ֵ
		 */
		con.setRequestMethod("POST"); // ��Post��ʽ�ύ����Ĭ��get��ʽ
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post��ʽ����ʹ�û���

		// ��������ͷ��Ϣ
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		// ���ñ߽�
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		// ����������Ϣ

		// ��һ���֣�
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // �����������
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:image/jpg\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");

		// ��������
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// �����ͷ
		out.write(head);
		// �ļ����Ĳ���
		// ���ļ������ļ��ķ�ʽ ���뵽url��
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) 
		{
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		// ��β����
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// ����������ݷָ���
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
				// ����BufferedReader����������ȡURL����Ӧ
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
				System.out.println("����POST��������쳣��" + e);
				e.printStackTrace();
				throw new IOException("���ݶ�ȡ�쳣");
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
