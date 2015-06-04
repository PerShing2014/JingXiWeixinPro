package com.imooc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.imooc.po.Image;
import com.imooc.po.ImageMessage;
import com.imooc.po.Music;
import com.imooc.po.MusicMessage;
import com.imooc.po.News;
import com.imooc.po.NewsMessage;
import com.imooc.po.TextMessage;
import com.thoughtworks.xstream.XStream;

public class MessageUtil {
	//������Ϣ����....��Ϣ��װ��
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_SCANCODE = "scancode_push";
	/**
	 * xmlתΪMap����
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		//��request�л�ȡ������
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		//��Ԫ��
		Element root =doc.getRootElement();
		//��Ԫ�ص��ӽڵ�
		List<Element> list = root.elements();
		
		for(Element e:list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	
	
	/**
	 * ���ı���Ϣ����תΪxml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXML(TextMessage textMessage){
		XStream xstream = new XStream();
		//���ã������л��е���ȫ�����ƣ��ñ����滻��
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	/**
	 * ƴ���ı���Ϣ
	 */
	public static String initText(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);//����
		text.setToUserName(fromUserName);//����
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		//��װ��һ�� text����
		//��text���� ʹ�ù���תΪxml
		return textMessageToXML(text);
	}
	
	
	/**
	 * ���˵�
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("��ӭ����ע��Ϧ�����⣬�밴�ղ˵� ��ʾ���в�����\n\n");
		sb.append("1�����ںż򵥽���\n");
		sb.append("2�����ں���ϸ����\n");
		sb.append("3�����鷭��\n\n");
		sb.append("�ظ��������˰����˵�");
		return sb.toString();
	}
	/**
	 * �ظ��ؼ��� ��1��ʱ�����ع��ںŵļ򵥽���
	 * @return
	 */
	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("java������ĸ�ϰ���͸��������ר����ܡ���������ռ��������뷢����");
		return sb.toString();
	}
	/**
	 * �ظ��ؼ��� ��2��ʱ�����ع��ںŵ���ϸ����
	 * @return
	 */
	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("�����ںţ���Ҫ������ӻ�����ʼϵͳ�ĸ�ϰ�����⡣\nͬʱ���ڸ���֪ʶ�㣬����ר�������н��ܡ�\n"
				+ "������Ѱ������ʵĽ̳�ʹ�����⡣\nͬʱ�ռ�������ԽӴ����������������⣬�������Ϸ��͸���Ҳο���");
		return sb.toString();
	}
	//����
	public static String threeMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("���鷭��ʹ��ָ��\n\n");
		sb.append("ʹ��ʾ����\n");
		sb.append("��������\n");
		sb.append("�����й�����\n");
		sb.append("����football��\n\n");
		sb.append("�ظ�����ʾ���˵�");
		
		return sb.toString();
	}
	
	/**
	 * 
	 * ����������ͼ����ϢתΪxml
	 *
	 * @param newsMessage
	 * @return
	 * 
	 * @author ���ӿ�
	 *
	 * @since 2015��6��2��
	 *
	 * @update:[�������YYYY-MM-DD][����������][�������]
	 */
	public static String newsMessageToXML(NewsMessage newsMessage){
		XStream xstream = new XStream();
		//���ã������л��е���ȫ�����ƣ��ñ����滻��
		xstream.alias("xml", newsMessage.getClass());
		//ÿ����Ϣ�� �� ��Ҫitem��ǩ��Χ.����Ϣ�壬תΪitem
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	//ͼƬ��ϢתΪxml
	public static String imageMessageToXML(ImageMessage imageMessage){
		XStream xstream = new XStream();
		//���ã������л��е���ȫ�����ƣ��ñ����滻��
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	//������Ϣ��ʵ�����תΪxml
	public static String musicMessageToXML(MusicMessage musicMessage){
		XStream xstream = new XStream();
		//���ã������л��е���ȫ�����ƣ��ñ����滻��
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
	
	
	/**
	 * ��װһ��ͼ����Ϣ��
	 * ����������ƴ�ӣ�����һ����Ϣ�塣
	 *
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 * 
	 * @author ���ӿ�
	 *
	 * @since 2015��6��2��
	 *
	 * @update:[�������YYYY-MM-DD][����������][�������]
	 */
	public static String initNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		//ע�⣺��ͼ�ģ�����������ഴ������news���󣬼��뵽List��
		News news = new News();
		news.setTitle("����");
		news.setDescription("gjrgjrpgkgprgpgpksg");
		news.setPicUrl("http://examplejzk.tunnel.mobi/Weixin/image/img01.jpg");
		news.setUrl("www.baidu.com");
		 	
		newsList.add(news);		
		//����xml������
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXML(newsMessage);
		
		return message;
	}
	
	/**
	 * 
	 * ������������װͼƬ��Ϣ
	 *
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 * 
	 * @author ���ӿ�
	 *
	 * @since 2015��6��2��
	 *
	 * @update:[�������YYYY-MM-DD][����������][�������]
	 */
	public static String initImageMessage(String toUserName,String fromUserName){
		String message = null;
		Image image = new Image();
		image.setMediaId("9uoku-NZ3I6SQ6S1dNl_PyXkgT6rqvPZ4Es-4QqH3yu4GXlpbyNKyP8_C8K701Kw");
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message = imageMessageToXML(imageMessage);
		return message;
	}
	
	/**
	 * 
	 * ������������װ������Ϣ
	 *
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 * 
	 * @author ���ӿ�
	 *
	 * @since 2015��6��2��
	 *
	 * @update:[�������YYYY-MM-DD][����������][�������]
	 */
	public static String initMusicMessage(String toUserName,String fromUserName){
		String message = null;
		Music music = new Music();
		music.setThumbMediaId("r4T_aWRe3p62k_n7yDdG97pXmXwvFVnNq54veyzJPsoWjpKyfqaUFYXmWPtRxMnp");
		music.setTitle("see you again");
		music.setDescription("��7");
		music.setMusicUrl("http://examplejzk.tunnel.mobi/Weixin/resource/SeeYouAgain.mp3");
		music.setHQMusicUrl("http://examplejzk.tunnel.mobi/Weixin/resource/SeeYouAgain.mp3");
		
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);
		
		message = musicMessageToXML(musicMessage);
		return message;
	}
}