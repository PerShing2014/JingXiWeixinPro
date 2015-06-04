package com.imooc.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.imooc.menu.Button;
import com.imooc.menu.ClickButton;
import com.imooc.menu.Menu;
import com.imooc.menu.ViewButton;
import com.imooc.po.AccessToken;
import com.imooc.trans.Data;
import com.imooc.trans.Parts;
import com.imooc.trans.Symbols;
import com.imooc.trans.TransResult;

public class WeixinUtil {
	private static final String APPID = "wx294b974ac6590521";
	private static final String APPSECRET = "47e8c911128dcac7583376553babf460";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	//封装一个get请求的方法       json 导入jar包    
	//把接收过来的结果转为json格式
	//接口地址的参数 传进来
	//---就是 url封装好之后，传给接口，接口返回一个JSON对象
	public static JSONObject doGetStr(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();//消息体中拿结果
			if(entity!=null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	//post请求
	public static JSONObject doPostStr(String url,String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	/**
	 * 
	 * 功能描述：获取access_token
	 *
	 * @return
	 * 
	 * @author 姜子凯
	 *
	 * @since 2015年6月2日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject!=null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	/**
	 * 
	 * 功能描述：文件上传JAVA中的文件 上传下载，课程。
	 *
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * 
	 * @author 姜子凯
	 *
	 * @since 2015年6月2日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String upload(String filePath,String accessToken,String type) throws IOException {
		File file = new File(filePath);
		if(!file.exists()||!file.isFile()){
			throw new IOException("文件不存在");
		}
		
		String url = UPLOAD_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE", type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//设置边界
		String BOUNDARY = "---------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("UTF-8");
		
		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分
		//把文件以流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut))!=-1){
			out.write(bufferOut,0,bytes);
		}
		in.close();
		
		//结尾部分
		byte[] foot = ("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");//定义最后数据分割线
		
		out.write(foot);
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try{
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
			}
			if(result==null){
				result = buffer.toString();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				reader.close();
			}
		}
		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type+"_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}
	
	/**
	 * 
	 * 功能描述：组装菜单 
	 * （第二步：接口实现菜单创建）
	 *
	 * @return
	 * 
	 * @author 姜子凯
	 *
	 * @since 2015年6月2日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ClickButton button11 = new ClickButton();
		button11.setName("click菜单");
		button11.setType("click");
		button11.setKey("11");
		
		ViewButton button21 = new ViewButton();
		button21.setName("view菜单");
		button21.setType("view");
		button21.setUrl("http://www.baidu.com");
		
		//二级菜单
		ClickButton button31 = new ClickButton();
		button31.setName("扫码事件");
		button31.setType("scancode_push");
		button31.setKey("31");
		//二级菜单
		ClickButton button32 = new ClickButton();
		button32.setName("地理位置");
		button32.setType("location_select");
		button32.setKey("32");
		
		Button button = new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{button31,button32});
		//button11，主菜单，button21主菜单，button下面有两个子菜单。
		menu.setButton(new Button[]{button11,button21,button});
		return menu;
	}
	
	public static int createMenu(String token,String menu){
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN",token );
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	//json的格式返回。查询菜单
	public static JSONObject queryMenu(String token){
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN",token );
		JSONObject jsonObject = doGetStr(url);
		
		return jsonObject;
	}
	//删除菜单
	//json的格式返回,从json中获取int 执行之后获取的代码。
	public static int deleteMenu(String token){
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN",token );
		JSONObject jsonObject = doGetStr(url);
		int result=0;
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	//单词翻译,返回翻译好的字符串
	public static String translate(String source) throws UnsupportedEncodingException{
		String url = "http://openapi.baidu.com/public/2.0/translate/dict/simple?client_id=0T5GGRIL3YwQgiHyaP5yFSwz&q=KEYWORD&from=auto&to=auto";
		url = url.replace("KEYWORD", URLEncoder.encode(source,"UTF-8"));
		JSONObject jsonObject = doGetStr(url);
		String errno = jsonObject.getString("errno");
		
		Object obj = jsonObject.get("data");//debug   发现，jsonObject对象，用翻译单词的接口翻译词组，此时  data为 【】 
		
		StringBuffer dst = new StringBuffer();
		if("0".equals(errno)&&!"[]".equals(obj.toString())){//成功
			//json对象转为实体对象的方法
			TransResult transResult = (TransResult) JSONObject.toBean(jsonObject, TransResult.class);
			Data data = transResult.getData();
			Symbols symbols = data.getSymbols()[0];//就这一个，数组中
			
			String phzh = symbols.getPh_zh()==null?"":"中文拼音："+symbols.getPh_zh()+"\n";
			String phen = symbols.getPh_en()==null?"":"英式音标："+symbols.getPh_en()+"\n";
			String pham = symbols.getPh_am()==null?"":"美式音标："+symbols.getPh_am()+"\n";
			dst.append(phzh+phen+pham);
			
			//解释
			Parts[] parts = symbols.getParts();
			String pat = null;
			for(Parts part:parts){
				pat = (part.getPart()!=null&&!"".equals(part.getPart()))?"["+part.getPart()+"]":"";
				String[] means = part.getMeans();
				dst.append(pat);
				for(String mean:means){
					dst.append(mean+";");
				}
			}
		}else{
			dst.append(translateFull(source));
		}
		return dst.toString();
	}
	//词组，段落翻译
	public static String translateFull(String source) throws UnsupportedEncodingException{
		String url = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=0T5GGRIL3YwQgiHyaP5yFSwz&q=KEYWORD&from=auto&to=auto";
		url = url.replace("KEYWORD", URLEncoder.encode(source,"UTF-8"));
		JSONObject jsonObject = doGetStr(url);
		
		StringBuffer dst = new StringBuffer();
		List<Map> list = (List<Map>) jsonObject.get("trans_result");
		
		for(Map map:list){
			dst.append(map.get("dst"));//{“src”:””, dst”:””}
		}
		return dst.toString();
	}
	
}
