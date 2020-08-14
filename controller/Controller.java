package controller;

import java.util.Map;

import service.AdminService;
import service.BoardService;
import service.UserService;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		
		new Controller().start();
		

	}
	
	public static Map<String, Object> loginUser; //로그인한 유저의 정보를 기억하고 있어야함, 게시판에 글을 작성하거나 이럴 때 사용하기 위해
	
	
	private UserService userService = UserService.getInstance();
	private BoardService boardService = BoardService.getInstance();
	private AdminService adminService = AdminService.getInstance();

	private void start() {
		int view = View.HOME;
		
		while(true){
			switch (view) { //화면을 리턴 받아서 이동 
			case View.NOTICE_POPUP : view = boardService.noticePopup(); break;
			case View.HOME : view = home(); break;
			case View.MAIN_MENU: view = MainMenu(); break;
			case View.LOGIN : view = userService.login(); break;
			case View.JOIN : view = userService.join(); break;
			case View.BOARD_LIST : view = boardService.boardList(); break;
			case View.BOARD_VIEW : view = boardService.boardView(); break;
			case View.BOARD_INSERT_FORM : view = boardService.boardInsertForm(); break;
			case View.CHATROOM_LIST: view = boardService.ChatRoomList(); break;
			case View.CHATROOM_CHAT: view = boardService.Chat(); break;
			case View.NOTICE_LIST: view = boardService.noticeList(); break;
			case View.NOTICE_VIEW: view = boardService.noticeView(); break;
			case View.NOTICE_INSERT_FORM : view = boardService.noticeInsertForm(); break;
			case View.REVIEW_UPLOAD: view = boardService.reviewUpload(); break;	
			case View.REVIEW_VIEW: view = boardService.reviewView(); break;	
			case View.ADMIN_MENU : view = adminService.adminMenu(); break;
			case View.BLACKLIST_MENU : view = adminService.blackListMenu(); break;
			case View.BLACKLIST_USER : view = adminService.blackListUser(); break;
			case View.BLACKLIST_BOARD : view = adminService.blackListBoard(); break;
			case View.REPORT_MENU : view = adminService.reportMenu(); break;
			case View.REPORT_USER : view = adminService.reportUser(); break;
			case View.REPORT_BOARD : view = adminService.reportBoard(); break;
			case View.BOARD_MANAGER : view = adminService.boardManager(); break;
			case View.MY_PAGE: view = userService.myPage(); break;
			case View.PROFILE_OPTION: view = userService.profileOption(); break;
			case View.WRITING_LIST: view = userService.writingList(); break;
			case View.INTEREST_LIST: view = userService.interestList(); break;
			case View.TRADE_HISTORY: view = userService.tradeHistory(); break;
			case View.ADMIN_BOARD_VIEW : view = adminService.adminBoardView(); break;
			case View.SELLER_INFO : view = boardService.sellerInfo(); break;
			case View.BUY_TRADE_HISTORY: view = userService.buyHistory(); break;
			case View.SELL_TRADE_HISTORY: view = userService.sellHistory(); break;
			}
		}

}
	
	private int home() {
		System.out.println("----------------------------------------");
		System.out.println("1.로그인\t\t2.회원가입\t\t0.프로그램 종료");
		System.out.println("----------------------------------------");
		System.out.println("번호 입력>");
		
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: return View.LOGIN;
		case 2: return View.JOIN;
		case 0:
			System.out.println("프로그램이 종료되었습니다.");
			System.exit(0);
			break;
				
		}
		return View.HOME; //메소드가 끝나고 다음에 어떤 화면으로 갈지 리턴하는 것
	}
	
	public int MainMenu(){ //메인메뉴화면
		System.out.println("=========================================");
		System.out.println("\t별의 별게 다 있는 별! 별! 마! 켓!");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t1.마이페이지"); 
		System.out.println("\t\t2.공지사항");
		System.out.println("\t\t3.게시판");
		System.out.println("\t\t4.채팅");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t\t\t\t(0)로그아웃");
		System.out.println("=========================================");
		System.out.println("원하는 메뉴 선택 >");
		int input = ScanUtil.nextInt();
		
		switch(input){
		case 1 :
			return View.MY_PAGE;
		case 2 :
			return View.NOTICE_LIST;
		case 3 :
			return View.BOARD_LIST;
		case 4 :
			return View.CHATROOM_LIST;
		}
		System.out.println("로그아웃되었습니다.");
		loginUser = null;
		return View.HOME;
	}
	
	public static void lineEnter (String str ,int l) {
		int i = 1;
		while(true) {
			if(str.length() > l*i) {
				if(i > 1) {
					System.out.println(str.substring(l*(i-1), l*i));
				} else {
					System.out.println(str.substring(l*(i-1), l*i));
				}
			} else {
				if(i > 1) {
					System.out.println(str.substring(l*(i-1), str.length()));
					break;
				} else {
					System.out.println(str);
					break;
				}
			}
			i++;
		}
	}

}


