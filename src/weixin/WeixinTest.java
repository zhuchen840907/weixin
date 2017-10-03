package weixin;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * 
 * 参考
 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1481187827_i0l21
 * 微信api
 * 
 * @author zhuchen
 *
 */
public class WeixinTest 
{

	private String clientCredential = "client_credential";
	private String appid="wx7264ed29f42b83f0";
	private String secret="add784029077bbe7d85868b98ad43c5c";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String URL = "url";
	private static final String MEDIA_ID= "media_id";
	private String token = "5jd-RmkjvyWskKQYRJ2TPesADwm3VfJrpy3e9IIpu_LBe2CD0NXS4SDlLOgyEfiXibdFT7AIJfWGikxvOz5UIYsN1ljDhdsLIuvdEdqJYLQORYcCAAEHO";
	
	/**
	 * 获取token
	 * weixin result {"access_token":"ACCESS_TOKEN","expires_in":7200}
	 * @return token
	 */
	public String getToken()
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";
		url = String.format(url, clientCredential,appid,secret);
		System.out.println(url);
		result = HttpTools.doGet(url, 0, 0);
		System.out.println(result);
		JSONObject jsonObject = JSONObject.fromObject(result);
		if (jsonObject!=null && jsonObject.get(ACCESS_TOKEN)!=null)
		{
			return jsonObject.getString(ACCESS_TOKEN);
		}
		return result ;
	}
	
	/**
	 * 上传图片,图文里的图片
	 * {
    "url":  "http://mmbiz.qpic.cn/mmbiz/gLO17UPS6FS2xsypf378iaNhWacZ1G1UplZYWEYfwvuU6Ont96b1roYs CNFwaRrSaKTPCUdBK9DgEHicsKwWCBRQ/0"
      }
	 * @param filePath 图片本机位置
	 * @return  图片上传到微信服务器位置
	 * @throws IOException
	 */
	public String upFile(String filePath) throws IOException
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=%s";
		url = String.format(url, token);
		System.out.println(url);
		result = HttpTools.upFile(url, filePath);
		JSONObject jsonObject = JSONObject.fromObject(result);
		if (jsonObject!=null && jsonObject.get(URL)!=null)
		{
			return jsonObject.getString(URL);
		} 
		return result ;
		
	}
	
	/**
	 * 上传封面图
	 * @param filePath 图片本机位置
	 * @return
	 * @throws IOException 
	 */
	public String upMedia(String filePath) throws IOException
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=%s&type=%s";
		url = String.format(url, token, "thumb");
		System.out.println(url);
		result = HttpTools.upFile(url, filePath);
		
		System.out.println(result);
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		if (jsonObject!=null && jsonObject.get(MEDIA_ID)!=null)
		{
			return jsonObject.getString(MEDIA_ID);
		} 
		return result ;
	}
	
	
	/**
	 * param
	 * 
	 * 接口有变？？
	 * 
	 * {
         "media_id":MEDIA_ID
       }
	 * 
	 * @return
	 */
	public String upArticles()
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=%s";
		url = String.format(url, token);
		
		String content = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>第六条可以吗</title></head><body> 这是一个测试<img alt='' src='http://mmbiz.qpic.cn/mmbiz_jpg/uobAPsV6YEB1PuAH0CKSn0sF2P1OHP9OPLc8suyDLibwuAjMLDpicAoIBhJiaqIiaaRiajDY4l37Bqn3rZhQibkoWDYQ/0'></body></html>";
		
		
		JSONObject jsonObject = new JSONObject();
		
		JSONObject articlesJson = new JSONObject();
		articlesJson.put("thumb_media_id", "RCgaUiDla0yFurjlSjTNgMwZObK5RQEvWrDq5kk9IOI");		
		articlesJson.put("author", "zhuchen");
		articlesJson.put("title", "第六条可以吗");
		articlesJson.put("content_source_url", "https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433747234");
		articlesJson.put("content", content);
		articlesJson.put("digest", "test");
		articlesJson.put("show_cover_pic", 1);	
		
		ArrayList<JSONObject> articles = new ArrayList<>();
		articles.add(articlesJson);
		
		jsonObject.put("articles", articles);
				
		System.out.println(jsonObject.toString());
		result = HttpTools.doPost(url, jsonObject, 0, 0);
		
		
		
		return result ;
	}
	
	
	public String editArticles()
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=%s";
		url = String.format(url, token);
		return result;
	}
	
	/**
	 * 
	 * {
         "errcode":0,
         "errmsg":"send job submission success",
         "msg_id":34182, 
         "msg_data_id": 206227730
       }
	 * 
	 * 
	 * @return
	 */
	public String sendMsg()
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=%s";
		url = String.format(url, token);
		JSONObject jsonObject = new JSONObject();
		
		
		JSONObject filter = new JSONObject();
		filter.put("is_to_all", false);

		jsonObject.put("filter", filter);
		
		
		JSONObject mpnews = new JSONObject();
		mpnews.put("media_id", "RCgaUiDla0yFurjlSjTNgLi4BYlPNpS5-kww6rSLtqw");
		
		jsonObject.put("mpnews", mpnews);
		jsonObject.put("msgtype", "mpnews");
		jsonObject.put("send_ignore_reprint", 1);
		
		System.out.println(jsonObject.toString());
		
		result = HttpTools.doPost(url, jsonObject, 0, 0);
		
		
		
		return result ;
	}
	
	
	/**
	 * 
	 * {
         "errcode":0,
         "errmsg":"send job submission success",
         "msg_id":34182, 
         "msg_data_id": 206227730
       }
	 * 
	 * 
	 * @return
	 */
	public String sendMsgToUsers()
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=%s";
		url = String.format(url, token);
		JSONObject jsonObject = new JSONObject();
		
		
		JSONObject filter = new JSONObject();
		
		Object usersOpenId = getUsers();
		//System.out.println(usersOpenId);
		
		//filter.put("touser", usersOpenId);
		

		jsonObject.put("touser", usersOpenId);
		
		
		JSONObject mpnews = new JSONObject();
		mpnews.put("media_id", "RCgaUiDla0yFurjlSjTNgLi4BYlPNpS5-kww6rSLtqw");
		
		jsonObject.put("mpnews", mpnews);
		jsonObject.put("msgtype", "mpnews");
		jsonObject.put("send_ignore_reprint", 1);
		
		System.out.println(jsonObject.toString());
		
		result = HttpTools.doPost(url, jsonObject, 0, 0);
		
		
		
		return result ;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public Object getUsers()
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s";
		url = String.format(url, token,"");
		
		System.out.println(url);
		
		result = HttpTools.doGet(url, 0, 0);
			
		JSONObject jsonObject = JSONObject.fromObject(result);
		Object usersOpenId  = ((JSONObject)jsonObject.get("data")).get("openid");
		
		return usersOpenId ;
	}
	
	public String clearAPI()
	{
		String result = null;
		String url = "https://api.weixin.qq.com/cgi-bin/clear_quota?access_token=%s";
		url = String.format(url, token);
		
		System.out.println(url);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", appid);
		
		result = HttpTools.doPost(url, jsonObject, 0, 0);
				
		return result ;
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		WeixinTest weixinTest = new WeixinTest();
		//System.out.println(weixinTest.getToken());;
		//String filePath = "C:\\Desert.jpg";
		//System.out.println(weixinTest.upMedia(filePath));;
		//System.out.println(weixinTest.upArticles());;
		
		//System.out.println(weixinTest.sendMsg());
		//System.out.println(weixinTest.getUsers());
		//System.out.println(weixinTest.clearAPI());
		System.out.println(weixinTest.sendMsgToUsers());
	}
}
