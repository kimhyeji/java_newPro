package com.sbs.example.demo.dto;

import java.util.Map;

public class ArticleReply extends Dto {
	private int articleId;
	private int memberId;
	private String body;

	public ArticleReply() {

	}

	public ArticleReply(Map<String, Object> row) {
		super(row);
		this.body = (String) row.get("body");
		this.memberId = (int) row.get("memberId");
		this.articleId = (int) row.get("articleId");
	}
	
	public ArticleReply(int articleId, int memberId, String body) {
		this.articleId = articleId;
		this.memberId = memberId;
		this.body = body;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}