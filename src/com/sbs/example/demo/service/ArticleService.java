package com.sbs.example.demo.service;

import java.util.List;

import com.sbs.example.demo.dao.ArticleDao;
import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = Factory.getArticleDao();
	}

	// 번호에 해당하는 게시물 가져오기
	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	public List<Article> getArticlesByBoardCode(String code) {
		return articleDao.getArticlesByBoardCode(code);
	}

	public List<Board> getBoards() {
		return articleDao.getBoards();
	}

	public int makeBoard(String name, String code) {
		Board oldBoard = articleDao.getBoardByCode(code);

		if (oldBoard != null) {
			return -1;
		}

		Board board = new Board(name, code);
		return articleDao.saveBoard(board);
	}

	public Board getBoard(int id) {
		return articleDao.getBoard(id);
	}
	
	public void makeBoardIfNotExists(String name, String code) {
		Board board = articleDao.getBoardByCode(code);

		if (board == null) {
			makeBoard(name, code);
		}
	}

	// 게시물 추가
	public int write(int boardId, int memberId, String title, String body) {
		Article article = new Article(boardId, memberId, title, body);
		return articleDao.save(article);
	}

	// 게시물 리스트
	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	// 게시물 수정
	public void modify(int modiNum, String newTitle, String newBody) {
		articleDao.modify(modiNum, newTitle, newBody);
	}

	// 게시물 삭제
	public void delete(int delNum) {
		articleDao.delete(delNum);
	}

	// 게시판 변경
	public Board getBoardByCode(String boardCode) {
		return articleDao.getBoardByCode(boardCode);
	}

	// 댓글
	public void reply(int articleId, int memberId, String replyText) {
		articleDao.reply(articleId, memberId, replyText);
	}
	
	// 댓글
	public List<ArticleReply> getArticleRepliesByArticleId(int id) {
		return articleDao.getArticleRepliesByArticleId(id);
	}

}