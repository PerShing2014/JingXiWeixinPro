package com.imooc.test;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.imooc.po.AccessToken;
import com.imooc.util.WeixinUtil;

public class WeixinTest {
	public static void main(String[] args) {
		try {
//			AccessToken token = WeixinUtil.getAccessToken();
//			System.out.println("Ʊ�ݣ�"+token.getToken());
//			System.out.println("��Чʱ�䣺"+token.getExpiresIn());
			
			String path = "D:/img000.jpg";
//			String mediaId = WeixinUtil.upload(path, token.getToken(), "image");
//			String mediaId = WeixinUtil.upload(path, token.getToken(), "thumb");
//			System.out.println(mediaId);

//�����˵��Ĳ���
//			String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();//javaתΪjson
//			int result = WeixinUtil.createMenu(token.getToken(), menu);
//			if(result==0){
//				System.out.println("�����˵��ɹ�");
//			}else{
//				System.out.println("�����룺"+result);
//			}
			
			//��ѯ�˵�
//			JSONObject jsonObject = WeixinUtil.queryMenu(token.getToken());
//			System.out.println(jsonObject);
			
			//ɾ���˵�
//			int result = WeixinUtil.deleteMenu(token.getToken());
//			if(result==0){
//				System.out.println("�˵�ɾ���ɹ�");
//			}else{
//				System.out.println("�������Ϊ��"+result);
//			}
			
			//�ٶȷ���ӿ�
//			String result = WeixinUtil.translate("football");
			String result = WeixinUtil.translate("My Name is Jiangzikai");
			System.out.println(result);
			
//			String resultfull = WeixinUtil.translateFull("�й�����");
//			System.out.println(resultfull);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}