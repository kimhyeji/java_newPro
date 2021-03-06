package com.sbs.example.demo.controller;

import java.util.List;

import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.dto.Member;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.service.ArticleService;

public class ArticleController extends Controller {
	private ArticleService articleService;

	public ArticleController() {
		articleService = Factory.getArticleService();
	}

	public void doAction(Request reqeust) {
		if (reqeust.getActionName().equals("list")) {
			actionList(reqeust);
		} else if (reqeust.getActionName().equals("write")) {
			actionWrite(reqeust);
		} else if (reqeust.getActionName().equals("modify")) {
			actionModify(reqeust);
		} else if (reqeust.getActionName().equals("delete")) {
			actionDelete(reqeust);
		} else if (reqeust.getActionName().equals("detail")) {
			actionDetail(reqeust);
		} else if (reqeust.getActionName().equals("changeBoard")) {
			actionChangeBoard(reqeust);
		} else if (reqeust.getActionName().equals("currentBoard")) {
			actionCurrentBoard(reqeust);
		} else {
			System.out.println("올바른 명령어를 입력해주세요.");
		}
	}

	// 현재 게시판 출력
	private void actionCurrentBoard(Request reqeust) {
		Board board = Factory.getSession().getCurrentBoard();
		System.out.printf("현재 게시판 : %s\n", board.getName());
	}

	// 게시판 변경
	private void actionChangeBoard(Request reqeust) {
		System.out.println("변경하실 게시판 code를 입력해주세요.");
		System.out.println(articleService.getBoards());
		System.out.print("> ");
		String boardCode = Factory.getScanner().nextLine().trim();

		Board board = articleService.getBoardByCode(boardCode);

		if (board == null) {
			System.out.println("해당 게시판이 존재하지 않습니다.");
		} else {
			System.out.printf("%s 게시판으로 변경되었습니다.\n", board.getName());
			Factory.getSession().setCurrentBoard(board);
		}
	}

	// 게시물 상세 - 미구현 / 여기 하다 말았음
	private void actionDetail(Request reqeust) {
		System.out.println("조회하실 게시물 번호를 입력해주세요.");
		System.out.print("> ");
		
		int id = Factory.getScanner().nextInt();
		Factory.getScanner().nextLine();

		Article article = articleService.getForPrintArticle(id);

		if (article == null) {
			System.out.println("해당 게시물은 존재하지 않습니다.");
			return;
		}
		
		String writerName = (String)article.getExtra().get("writerName");
		
		List<ArticleReply> articleReplies = articleService.getForPrintArticleRepliesByArticleId(article.getId());
		int repliesCount = articleReplies.size();
		
		System.out.printf("== %d번 게시물 상세 시작 ==\n", article.getId());
		System.out.printf("번호 : %d\n", article.getId());
		System.out.printf("작성날짜 : %s\n", article.getRegDate());
		System.out.printf("제목 : %s\n", article.getTitle());
		System.out.printf("내용 : %s\n", article.getBody());
		System.out.printf("작성자 : %s\n", writerName);
		System.out.printf("댓글개수 : %d\n", repliesCount);
		System.out.printf("== %d번 게시물 상세 끝 ==\n", article.getId());
		
		System.out.println("댓글 <작성/수정/삭제>하시겠습니까? <yes or no>");
		System.out.print("> ");
		String yn = Factory.getScanner().nextLine().trim();
		
		if (yn.equals("yes")) {
			Member member = Factory.getSession().getLoginedMember();
			int writerId = article.getMemberId();
			
			if (member == null) {
				System.out.println("로그인 후 이용 가능합니다.");
				return;
			}
			
			for ( ArticleReply articleReply : articleReplies ) {
				String replyWriterName = (String)articleReply.getExtra().get("writerName");
				
				System.out.printf("%d번 댓글 : %s by %s\n", articleReply.getId(), articleReply.getBody(), replyWriterName);
			}

			System.out.println("< write/modify/delete/no >");
			System.out.print("> ");
			String MorD = Factory.getScanner().nextLine().trim();
			
			if (MorD.equals("write")) {
				replyWrite(id, writerId);
			} else if (MorD.equals("modify")) {
				replyModify(id, writerId);
			} else if (MorD.equals("delete")) {
				replyDelete(id);
			} else if (MorD.equals("no")) {
				System.out.println("명령어로 돌아갑니다.");
				return;
			} else {
				System.out.println("올바른 명령어를 입력해주세요.");
			}
		}
		
		else if (yn.equals("no")) {
			System.out.println("명령어로 돌아갑니다.");
			return;
		} else {
			System.out.println("올바른 명령어를 입력해주세요.");
		}
	}
	
	// 댓글 작성
	private void replyWrite(int id, int writerId) {
		System.out.println("댓글을 입력해주세요.");
		System.out.print("> ");
		String replyText = Factory.getScanner().nextLine().trim();
		
		articleService.reply(id, writerId, replyText);
		System.out.println("댓글 작성이 완료되었습니다.");
	}

	// 댓글 삭제
	private void replyDelete(int id) {
		System.out.println("몇번 댓글을 삭제하시겠습니까?");
		System.out.print("> ");
		int delNum = Factory.getScanner().nextInt();
		Factory.getScanner().nextLine();
		
		ArticleReply articleReply = articleService.getArticleReply(delNum);
		
		if(articleReply == null) {
			System.out.println("해당 번호의 댓글이 존재하지 않습니다.");
			return;
		}
		
		int memberId = Factory.getSession().getLoginedMember().getId();

		if (memberId != articleReply.getMemberId()) {
			System.out.println("댓글 삭제는 본인만 가능합니다.");
			return;
		}
		
		
		articleService.replyDelete(id);
		System.out.println("댓글 삭제가 완료되었습니다.");
	}

	// 댓글 수정
	private void replyModify(int id, int writerId) {
		System.out.println("몇번 댓글을 수정하시겠습니까?");
		System.out.print("> ");
		int modiNum = Factory.getScanner().nextInt();
		Factory.getScanner().nextLine();
		
		ArticleReply articleReply = articleService.getArticleReply(modiNum);
		
		if(articleReply == null) {
			System.out.println("해당 번호의 댓글이 존재하지 않습니다.");
			return;
		}
		
		int memberId = Factory.getSession().getLoginedMember().getId();

		if (memberId != articleReply.getMemberId()) {
			System.out.println("댓글 수정은 본인만 가능합니다.");
			return;
		}
		
		System.out.println("수정하실 댓글을 입력해주세요.");
		System.out.print("> ");
		String replyText = Factory.getScanner().nextLine().trim();
		
		articleService.replyModify(modiNum, replyText);
		System.out.println("댓글 수정이 완료되었습니다.");
	}

	// 게시물 리스트
	private void actionList(Request reqeust) {
		Board currentBoard = Factory.getSession().getCurrentBoard();
		List<Article> articles = articleService.getForPrintArticlesByBoardCode(currentBoard.getCode());

		System.out.printf("== %s 게시물 리스트 시작 ==\n", currentBoard.getName());
		System.out.println("번호 |                날짜 |        작성자 | 제목");
		for (Article article : articles) {
			System.out.printf("%4d | %s | %10s | %s\n", article.getId(), article.getRegDate(),
					article.getExtra().get("writerName"), article.getTitle());
		}
		System.out.printf("== %s 게시물 리스트 끝 ==\n", currentBoard.getName());
	}

	// 게시물 추가
	private void actionWrite(Request reqeust) {
		Member member = Factory.getSession().getLoginedMember();

		if (member == null) {
			System.out.println("로그인 후 이용 가능합니다.");
			return;
		}

		System.out.printf("제목 : ");
		String title = Factory.getScanner().nextLine();
		System.out.printf("내용 : ");
		String body = Factory.getScanner().nextLine();

		// 현재 게시판 id 가져오기
		int boardId = Factory.getSession().getCurrentBoard().getId();

		// 현재 로그인한 회원의 id 가져오기
		int memberId = Factory.getSession().getLoginedMember().getId();
		int newId = articleService.write(boardId, memberId, title, body);

		System.out.printf("%d번 글이 생성되었습니다.\n", newId);
	}

	// 게시물 수정
	private void actionModify(Request reqeust) {
		System.out.println("== 게시물 수정 ==");
		System.out.println("수정하실 게시물 번호를 입력해주세요.");
		System.out.print("> ");

		int modiNum = Factory.getScanner().nextInt();
		Factory.getScanner().nextLine();

		Article article = articleService.getArticle(modiNum);

		if (article == null) {
			System.out.println("해당 게시물은 존재하지 않습니다.");
			return;
		}

		System.out.println("수정하실 제목과 내용을 입력해주세요.");
		System.out.printf("제목 : ");
		String newTitle = Factory.getScanner().nextLine().trim();
		System.out.printf("내용 : ");
		String newBody = Factory.getScanner().nextLine().trim();

		articleService.modify(modiNum, newTitle, newBody);

	}

	// 게시물 삭제
	private void actionDelete(Request reqeust) {
		System.out.println("== 게시물 삭제 ==");
		System.out.println("삭제하실 게시물 번호를 입력해주세요.");
		System.out.print("> ");

		int delNum = Factory.getScanner().nextInt();
		Factory.getScanner().nextLine();

		Article article = articleService.getArticle(delNum);

		if (article == null) {
			System.out.println("해당 게시물은 존재하지 않습니다.");
			return;
		}

		articleService.delete(delNum);
	}
}
