package com.imooc.po;

import java.util.List;

/**
 * �ظ�ͼ����Ϣ����xml����㣩
 *
 * @author ���ӿ�
 *
 * @version 
 *
 * @since 2015��6��2��
 */
public class NewsMessage extends BaseMessage{
	private int ArticleCount;
	//��Ϣ�壬�Ǽ�������
	private List<News> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
	
}
