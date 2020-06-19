package com.sbs.example.demo.controller;

import com.sbs.example.demo.dto.Member;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.service.MemberService;

public class MemberController extends Controller {
	private MemberService memberService;

	public MemberController() {
		memberService = Factory.getMemberService();
	}

	public void doAction(Request reqeust) {
		if (reqeust.getActionName().equals("logout")) {
			actionLogout(reqeust);
		} else if (reqeust.getActionName().equals("login")) {
			actionLogin(reqeust);
		} else if (reqeust.getActionName().equals("whoami")) {
			actionWhoami(reqeust);
		} else if (reqeust.getActionName().equals("join")) {
			actionJoin(reqeust);
		} else {
			System.out.println("올바른 명령어를 입력해주세요.");
		}
	}

	// 회원가입
	private void actionJoin(Request reqeust) {
		System.out.println("지금부터 회원가입을 시작합니다.");
		System.out.println("사용하실 이름, 아이디, 비밀번호를 입력해주세요.");
		
		System.out.print("이름 : ");
		String name = Factory.getScanner().nextLine().trim();
		while(true) {
			System.out.print("아이디 : ");
			String loginId = Factory.getScanner().nextLine().trim();
			Member member = memberService.getMemberByLoginId(loginId);
			
			if(member != null) {
				System.out.println("이미 존재하는 아이디 입니다.");
				continue;
			} else {
				System.out.print("비밀번호 : ");
				String loginPw = Factory.getScanner().nextLine().trim();
				memberService.join(loginId, loginPw, name);
				System.out.printf("%s님 회원가입이 완료되었습니다.\n", name);
				break;
			}
		}
	}

	// 현재 접속자
	private void actionWhoami(Request reqeust) {
		Member loginedMember = Factory.getSession().getLoginedMember();

		if (loginedMember == null) {
			System.out.println("현재 로그인 상태가 아닙니다.");
		} else {
			System.out.println(loginedMember.getName());
		}

	}

	// 로그인
	private void actionLogin(Request reqeust) {
		Member loginedMember = Factory.getSession().getLoginedMember();
		if(loginedMember == null) {
			System.out.printf("로그인 아이디 : ");
			String loginId = Factory.getScanner().nextLine().trim();

			System.out.printf("로그인 비번 : ");
			String loginPw = Factory.getScanner().nextLine().trim();

			Member member = memberService.getMemberByLoginIdAndLoginPw(loginId, loginPw);

			if (member == null) {
				System.out.println("일치하는 회원이 없습니다.");
			} else {
				System.out.println(member.getName() + "님 환영합니다.");
				Factory.getSession().setLoginedMember(member);
			}	
		} else {
			System.out.println("이미 로그인 상태입니다.");
		}
		
	}

	// 로그아웃
	private void actionLogout(Request reqeust) {
		Member loginedMember = Factory.getSession().getLoginedMember();

		if (loginedMember != null) {
			Session session = Factory.getSession();
			System.out.println("로그아웃 되었습니다.");
			session.setLoginedMember(null);
		}

	}
}