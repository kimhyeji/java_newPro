package com.sbs.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.example.demo.db.DBConnection;
import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;

// Dao
public class ArticleDao {
	private DBConnection dbConnection;

	public ArticleDao() {
		dbConnection = Factory.getDBConnection();
	}

	public List<Article> getArticlesByBoardCode(String code) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("SELECT A.* "));
		sb.append(String.format("FROM `article` AS A "));
		sb.append(String.format("INNER JOIN `board` AS B "));
		sb.append(String.format("ON A.boardId = B.id "));
		sb.append(String.format("WHERE 1 "));
		sb.append(String.format("AND B.`code` = '%s' ", code));
		sb.append(String.format("ORDER BY A.id DESC "));

		List<Article> articles = new ArrayList<>();
		List<Map<String, Object>> rows = dbConnection.selectRows(sb.toString());

		for (Map<String, Object> row : rows) {
			articles.add(new Article(row));
		}

		return articles;
	}

	// num에 해당하는 게시물 가져오기
	public Article getArticleByNum(int num) {
		String sql = "";
		sql += "SELECT * FROM article WHERE ID = " + num + ";";
		Map<String, Object> row = dbConnection.selectRow(sql);

		Article article = new Article(row);

		return article;
	}

	public List<Board> getBoards() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("SELECT * "));
		sb.append(String.format("FROM `board` "));
		sb.append(String.format("WHERE 1 "));
		sb.append(String.format("ORDER BY id DESC "));

		List<Board> boards = new ArrayList<>();
		List<Map<String, Object>> rows = dbConnection.selectRows(sb.toString());

		for (Map<String, Object> row : rows) {
			boards.add(new Board(row));
		}

		return boards;
	}

	public Board getBoardByCode(String code) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("SELECT * "));
		sb.append(String.format("FROM `board` "));
		sb.append(String.format("WHERE 1 "));
		sb.append(String.format("AND `code` = '%s' ", code));

		Map<String, Object> row = dbConnection.selectRow(sb.toString());

		if (row.isEmpty()) {
			return null;
		}

		return new Board(row);
	}

	public int saveBoard(Board board) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("INSERT INTO board "));
		sb.append(String.format("SET regDate = '%s' ", board.getRegDate()));
		sb.append(String.format(", `code` = '%s' ", board.getCode()));
		sb.append(String.format(", `name` = '%s' ", board.getName()));

		return dbConnection.insert(sb.toString());
	}

	public int save(Article article) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("INSERT INTO article "));
		sb.append(String.format("SET regDate = '%s' ", article.getRegDate()));
		sb.append(String.format(", `title` = '%s' ", article.getTitle()));
		sb.append(String.format(", `body` = '%s' ", article.getBody()));
		sb.append(String.format(", `memberId` = '%d' ", article.getMemberId()));
		sb.append(String.format(", `boardId` = '%d' ", article.getBoardId()));

		return dbConnection.insert(sb.toString());
	}

	public Board getBoard(int id) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("SELECT * "));
		sb.append(String.format("FROM `board` "));
		sb.append(String.format("WHERE 1 "));
		sb.append(String.format("AND `id` = '%d' ", id));

		Map<String, Object> row = dbConnection.selectRow(sb.toString());

		if (row.isEmpty()) {
			return null;
		}

		return new Board(row);
	}

	public List<Article> getArticles() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("SELECT * "));
		sb.append(String.format("FROM `article` "));
		sb.append(String.format("WHERE 1 "));
		sb.append(String.format("ORDER BY id DESC "));

		List<Article> articles = new ArrayList<>();
		List<Map<String, Object>> rows = dbConnection.selectRows(sb.toString());

		for (Map<String, Object> row : rows) {
			articles.add(new Article(row));
		}

		return articles;
	}

	// 게시물 수정
	public void modify(int modiNum, String newTitle, String newBody) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("UPDATE `article` SET "));
		sb.append(String.format("title = '%s'", newTitle));
		sb.append(String.format(", `body` = '%s'", newBody));
		sb.append(String.format(" WHERE `id` = %d", modiNum));
		
		dbConnection.update(sb.toString());
	}

}