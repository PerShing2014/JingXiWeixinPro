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
	//��װһ��get����ķ���       json ����jar��    
	//�ѽ��չ����Ľ��תΪjson��ʽ
	//�ӿڵ�ַ�Ĳ��� ������
	//---���� url��װ��֮�󣬴����ӿڣ��ӿڷ���һ��JSON����
	public static JSONObject doGetStr(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();//��Ϣ�����ý��
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
	
	//post����
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
	 * ������������ȡaccess_token
	 *
	 * @return
	 * 
	 * @author ���ӿ�
	 *
	 * @since 2015��6��2��
	 *
	 * @update:[�������YYYY-MM-DD][����������][�������]
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
	 * �����������ļ��ϴ�JAVA�е��ļ� �ϴ����أ��γ̡�
	 *
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * 
	 * @author ���ӿ�
	 *
	 * @since 2015��6��2��
	 *
	 * @update:[�������YYYY-MM-DD][����������][�������]
	 */
	public static String upload(String filePath,String accessToken,String type) throws IOException {
		File file = new File(filePath);
		if(!file.exists()||!file.isFile()){
			throw new IOException("�ļ�������");
		}
		
		String url = UPLOAD_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE", type);
		
		URL urlObj = new URL(url);
		//����
		HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		//��������ͷ��Ϣ
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//���ñ߽�
		String BOUNDARY = "---------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("UTF-8");
		
		//��������
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//�����ͷ
		out.write(head);
		
		//�ļ����Ĳ���
		//���ļ������ļ��ķ�ʽ ���뵽url��
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut))!=-1){
			out.write(bufferOut,0,bytes);
		}
		in.close();
		
		//��β����
		byte[] foot = ("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");//����������ݷָ���
		
		out.write(foot);
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try{
			//����BufferedReader����������ȡURL����Ӧ
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
	 * ������������װ�˵� 
	 * ���ڶ������ӿ�ʵ�ֲ˵�������
	 *
	 * @return
	 * 
	 * @author ���ӿ�
	 *
	 * @since 2015��6��2��
	 *
	 * @update:[�������YYYY-MM-DD][����������][�������]
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ClickButton button11 = new ClickButton();
		button11.setName("click�˵�");
		button11.setType("click");
		button11.setKey("11");
		
		ViewButton button21 = new ViewButton();
		button21.setName("view�˵�");
		button21.setType("view");
		button21.setUrl("http://www.baidu.com");
		
		//�����˵�
		ClickButton button31 = new ClickButton();
		button31.setName("ɨ���¼�");
		button31.setType("scancode_push");
		button31.setKey("31");
		//�����˵�
		ClickButton button32 = new ClickButton();
		button32.setName("����λ��");
		button32.setType("location_select");
		button32.setKey("32");
		
		Button button = new Button();
		button.setName("�˵�");
		button.setSub_button(new Button[]{button31,button32});
		//button11�����˵���button21���˵���button�����������Ӳ˵���
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
	//json�ĸ�ʽ���ء���ѯ�˵�
	public static JSONObject queryMenu(String token){
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN",token );
		JSONObject jsonObject = doGetStr(url);
		
		return jsonObject;
	}
	//ɾ���˵�
	//json�ĸ�ʽ����,��json�л�ȡint ִ��֮���ȡ�Ĵ��롣
	public static int deleteMenu(String token){
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN",token );
		JSONObject jsonObject = doGetStr(url);
		int result=0;
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	//���ʷ���,���ط���õ��ַ���
	public static String translate(String source) throws UnsupportedEncodingException{
		String url = "http://openapi.baidu.com/public/2.0/translate/dict/simple?client_id=0T5GGRIL3YwQgiHyaP5yFSwz&q=KEYWORD&from=auto&to=auto";
		url = url.replace("KEYWORD", URLEncoder.encode(source,"UTF-8"));
		JSONObject jsonObject = doGetStr(url);
		String errno = jsonObject.getString("errno");
		
		Object obj = jsonObject.get("data");//debug   ���֣�jsonObject�����÷��뵥�ʵĽӿڷ�����飬��ʱ  dataΪ ���� 
		
		StringBuffer dst = new StringBuffer();
		if("0".equals(errno)&&!"[]".equals(obj.toString())){//�ɹ�
			//json����תΪʵ�����ķ���
			TransResult transResult = (TransResult) JSONObject.toBean(jsonObject, TransResult.class);
			Data data = transResult.getData();
			Symbols symbols = data.getSymbols()[0];//����һ����������
			
			String phzh = symbols.getPh_zh()==null?"":"����ƴ����"+symbols.getPh_zh()+"\n";
			String phen = symbols.getPh_en()==null?"":"Ӣʽ���꣺"+symbols.getPh_en()+"\n";
			String pham = symbols.getPh_am()==null?"":"��ʽ���꣺"+symbols.getPh_am()+"\n";
			dst.append(phzh+phen+pham);
			
			//����
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
	//���飬���䷭��
	public static String translateFull(String source) throws UnsupportedEncodingException{
		String url = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=0T5GGRIL3YwQgiHyaP5yFSwz&q=KEYWORD&from=auto&to=auto";
		url = url.replace("KEYWORD", URLEncoder.encode(source,"UTF-8"));
		JSONObject jsonObject = doGetStr(url);
		
		StringBuffer dst = new StringBuffer();
		List<Map> list = (List<Map>) jsonObject.get("trans_result");
		
		for(Map map:list){
			dst.append(map.get("dst"));//{��src��:����, dst��:����}
		}
		return dst.toString();
	}
	
}