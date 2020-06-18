package com.sbs.example.demo.service;

import java.util.List;

import com.sbs.example.demo.dao.ArticleDao;
import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = Factory.getArticleDao();
	}

	// num에 해당하는 게시물 가져오기
	public Article getArticleByNum(int num) {
		return articleDao.getArticleByNum(num);
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

	public int write(int boardId, int memberId, String title, String body) {
		Article article = new Article(boardId, memberId, title, body);
		return articleDao.save(article);
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	// 게시물 수정
	public void modify(int modiNum, String newTitle, String newBody) {
		articleDao.modify(modiNum, newTitle, newBody);
	}

	public void makeBoardIfNotExists(String name, String code) {
		Board board = articleDao.getBoardByCode(code);

		if (board == null) {
			makeBoard(name, code);
		}
	}

	public Board getBoardByCode(String boardCode) {
		return articleDao.getBoardByCode(boardCode);
	}

}