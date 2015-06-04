package com.imooc.po;

import java.util.List;

/**
 * 回复图文消息，（xml的外层）
 *
 * @author 姜子凯
 *
 * @version 
 *
 * @since 2015年6月2日
 */
public class NewsMessage extends BaseMessage{
	private int ArticleCount;
	//消息体，是集合类型
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
