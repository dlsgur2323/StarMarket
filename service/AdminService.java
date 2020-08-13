package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.AdminDao;

public class AdminService {
	private static AdminService instance;
	private AdminService(){}
	public static AdminService getInstance(){
		if(instance == null){
			instance = new AdminService();
		}
		return instance;
	}
	
	private AdminDao adminDao = AdminDao.getInstance();
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월dd일");
	SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
	SimpleDateFormat sdf4 = new SimpleDateFormat("yy/MM/dd HH:mm");
	
	public int adminMenu() { // ==================  관리자 메뉴 ====================
		System.out.println("=========================================");
		System.out.println("\t관리자 메뉴");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t1.블랙리스트 관리");
		System.out.println("\t\t2.신고내역");
		System.out.println("\t\t3.게시물 관리");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t\t\t\t  0.로그아웃");
		System.out.println("=========================================");
		System.out.println("원하는 메뉴 선택 >");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "1" :
				return View.BLACKLIST_MENU;
			case "2" :
				return View.REPORT_MENU;
			case "3" : 
				return View.BOARD_MANAGER;
			case "0" :
				Controller.loginUser = null;
				return View.HOME;
		}
		return View.ADMIN_MENU;
		
	}
	public int blackListMenu() { // ====================== 블랙리스트 관리 메뉴 
		System.out.println("=========================================");
		System.out.println("\t블랙리스트 관리");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t1.블랙리스트 회원");
		System.out.println("\t\t2.블랙리스트 게시물");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t\t\t\t < 뒤로가기");
		System.out.println("=========================================");
		System.out.println("원하는 메뉴 선택 >");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "1" :
				return View.BLACKLIST_USER;
			case "2" :
				return View.BLACKLIST_BOARD;
			case "<" : 
				return View.ADMIN_MENU;
		}
		return View.BLACKLIST_MENU;
	}
	
	public int blackListUser() { // ================= 블랙리스트 회원 목록
		List<Map<String, Object>> userList = adminDao.selectBlackListUser();
		System.out.println("=========================================");
		System.out.println("아이디\t닉네임\t블랙리스트 시작\t블랙리스트 종료");
		System.out.println("-----------------------------------------");
		for(Map<String, Object> user : userList){
			System.out.println(user.get("USER_ID") + "\t"
										+ user.get("NICKNAME") + "\t"
										+ user.get("BLACKLIST_START") + "\t"
										+ user.get("BLACKLIST_END"));
		}
		System.out.println("=========================================");
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		System.out.println("1.처분변경");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.BLACKLIST_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("처분변경 할 아이디 입력>");
				String userid = ScanUtil.nextLine();
				System.out.println("처분변경 사항 입력");
				System.out.println("1.미처분\t2.일시정지\t3.영구정지\t0.취소");
				input = ScanUtil.nextLine();
				Date today = new Date();
				String start = null;
				String end = null;
				switch(input) {
					case "0" :
						return View.BLACKLIST_USER;
					case "1" :
						start = null;
						end = null;
						break;
					case "2" :
						System.out.println("일시정지 종료 날짜> 1.현재날짜\n (예시 20200811) 입력>");
						String endDate = ScanUtil.nextLine();
						System.out.println("일시정지 종료 시각> \n (예시 17:35) 입력>");
						String endTime = ScanUtil.nextLine();
						
						start = sdf5.format(today);
						end = endDate + " " + endTime;
						
						if (endDate.equals("1")) { // ============ 현재날짜 특정시간 
							end = sdf6.format(today) + " " + endTime;
						} else { // ============= 특정날짜 특정시간 
							end = endDate + " " + endTime;
						}
						break;
					case "3" :
						start = sdf5.format(today);
						end = null;
						break;
				}
				Map<String, Object> param = new HashMap<>();
				param.put("BLACKLIST_START", start);
				param.put("BLACKLIST_END", end);
				int result = adminDao.updateUserDisposition(userid, param);
				return View.BLACKLIST_USER;
		}
		return View.BLACKLIST_USER;
		
	}
	
	public int blackListBoard() { // ================ 블랙리스트 게시물 목록 
		List<Map<String, Object>> boardList = adminDao.selectBlackListBoard();
		System.out.println("=========================================");
		System.out.println("글번호\t제목\t작성자\t블랙리스트 상태");
		System.out.println("-----------------------------------------");
		for(Map<String, Object> board : boardList){
			System.out.print(board.get("BOARD_NO") + "\t"
										+ board.get("TITLE") + "\t"
										+ board.get("USER_ID") + "\t");
			if(Integer.parseInt(board.get("BLACKLIST").toString()) == 2) {
				System.out.println("블라인드");
			} else if(Integer.parseInt(board.get("BLACKLIST").toString()) == 3) {
				System.out.println("삭제");
			} else if(Integer.parseInt(board.get("BLACKLIST").toString()) == 4) {
				System.out.println("복구");
			}System.out.println();
		}
		System.out.println("=========================================");
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		System.out.println("1.처분변경");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.BLACKLIST_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("처분 할 글번호 입력>");
				int boardno = ScanUtil.nextInt();
				System.out.println("처분변경 사항 입력");
				System.out.println("1.미처분\t2.블라인드\t3.삭제\t0.취소");
				input = ScanUtil.nextLine();
				switch(input) {
					case "0" :
						return View.REPORT_BOARD;
					case "1" : case "2" : case "3" :
					int result = adminDao.updateBoardBlack(boardno, input);
					return View.BLACKLIST_BOARD;
				}
		}
		return View.BLACKLIST_BOARD;
	}
	
	public int reportMenu() { //================ 신고내역 메뉴
		System.out.println("=========================================");
		System.out.println("\t신고내역");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t1.회원 신고");
		System.out.println("\t\t2.게시물 신고");
		System.out.println("-----------------------------------------");
		System.out.println("\t\t\t\t\t < 뒤로가기");
		System.out.println("=========================================");
		System.out.println("원하는 메뉴 선택 >");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "1" :
				return View.REPORT_USER;
			case "2" :
				return View.REPORT_BOARD;
			case "<" : 
				return View.ADMIN_MENU;
		}
		return View.BLACKLIST_MENU;	
	}
	
	public int reportUser() { // =================== 회원 대상 신고 내역 리스트 
		List<Map<String, Object>> boardList = adminDao.selectReportUser();
		System.out.println("=========================================");
		for(Map<String, Object> board : boardList){
			System.out.println("-----------------------------------------");
			System.out.print("신고번호 : " + board.get("REPORT_NO") + "\t\t처분여부 : ");
			if(Integer.parseInt(board.get("DISPOSITION").toString()) == 1) {
				System.out.println("미처분");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 2) {
				System.out.println("일시정지");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 3) {
				System.out.println("영구정지");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 4) {
				System.out.println("복구");
			}
			System.out.println("피신고자 : " + board.get("REPORTED_ID") + "\t"
								+ "신고자 : " + board.get("USER_ID"));
			System.out.println("신고내용 : ");
			System.out.println(board.get("REPORT_CONTENT"));
			System.out.println("신고일자 : "+ sdf1.format(board.get("REG_DT")));
			System.out.println("-----------------------------------------");
		}
		System.out.println("=========================================");
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		System.out.println("1.처분변경");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.REPORT_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("처분 할 신고번호 입력>");
				int reportno = ScanUtil.nextInt();
				System.out.println("처분 내용 입력");
				System.out.println("1.미처분\t2.일시정지\t3.영구정지\t0.취소");
				input = ScanUtil.nextLine();
				Date today = new Date();
				String start = null;
				String end = null;
				switch(input) {
					case "0" :
						return View.REPORT_USER;
					case "1" :
						start = null;
						end = null;
						break;
					case "2" :
						System.out.println("일시정지 종료 날짜> 1.현재날짜\n (예시 20200811) 입력>");
						String endDate = ScanUtil.nextLine();
						System.out.println("일시정지 종료 시각> \n (예시 17:35) 입력>");
						String endTime = ScanUtil.nextLine();
						
						start = sdf5.format(today);
						end = endDate +  " " + endTime;
						
						if (endDate.equals("1")) { // ============ 현재날짜 특정시간 
							end = sdf6.format(today) + " " + endTime;
						} else { // ============= 특정날짜 특정시간 
							end = endDate + " " + endTime;
						}
						break;
					case "3" :
						start = sdf5.format(today);
						end = null;
						break;
				}
				Map<String, Object> param = new HashMap<>();
				param.put("BLACKLIST_START", start);
				param.put("BLACKLIST_END", end);
				int result = adminDao.userDisposition(reportno, input, param);
				return View.REPORT_USER;
		}
		
		return View.REPORT_USER;
	}
	
	SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMMdd HH:mm");
	SimpleDateFormat sdf6 = new SimpleDateFormat("yyyyMMdd");

	
	
	public int reportBoard() { // =================== 게시물 대상 신고 내역 리스트
		List<Map<String, Object>> boardList = adminDao.selectReportBoard();
		System.out.println("=========================================");
		for(Map<String, Object> board : boardList){
			System.out.println("-----------------------------------------");
			System.out.print("신고번호 : " + board.get("REPORT_NO") + "\t\t처분여부 : ");
			if(Integer.parseInt(board.get("DISPOSITION").toString()) == 1) {
				System.out.println("미처분");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 2) {
				System.out.println("블라인드");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 3) {
				System.out.println("삭제");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 4) {
				System.out.println("복구");
			}
			System.out.println("게시물번호 : " + board.get("BOARD_NO") + "\t"
								+ "신고자 : " + board.get("USER_ID"));
			System.out.println("신고내용 : ");
			System.out.println(board.get("REPORT_CONTENT"));
			System.out.println("신고일자 : "+ sdf1.format(board.get("REG_DT")));
			System.out.println("-----------------------------------------");
		}
		System.out.println("=========================================");
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		System.out.println("1.처분변경");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.REPORT_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("처분 할 신고번호 입력>");
				int reportno = ScanUtil.nextInt();
				System.out.println("처분 내용 입력");
				System.out.println("1.미처분\t2.블라인드\t3.삭제\t0.취소");
				input = ScanUtil.nextLine();
				switch(input) {
				case "0" :
					return View.REPORT_BOARD;
				case "1" : case "2" : case "3" :
					int result = adminDao.boardDisposition(reportno, input);
					return View.REPORT_BOARD;
				}
		}
		return View.REPORT_BOARD;
	}
	
	
	
	
	
	
	private int boardno;
	
	public int boardManager() { // ================================ 게시물 리스트 조회 
		List<Map<String, Object>> boardList = adminDao.selectAdminBoardList();
		System.out.println("=========================================");
		System.out.println("번호\t상태\t제목\t희망가격\t상품이름\t작성자 아이디\t작성자 닉네임\t작성일\t삭제여부\t블랙리스트\t");
		System.out.println("-----------------------------------------");
		for(Map<String, Object> board : boardList){
			System.out.print(board.get("BOARD_NO") + "\t");
			if(board.get("TAG").equals("Y")) {
				System.out.print("판매중" + "\t");
			} else {
				System.out.print("판매완료" + "\t");
			}
			System.out.print(board.get("TITLE") + "\t"
								+ board.get("PRICE") + "\t"
								+ board.get("GOODS_NAME") + "\t"
								+ board.get("USER_ID") + "\t"
					    		+ board.get("NICKNAME") + "\t"
					    		+ sdf1.format(board.get("REG_DT")) + "\t"
								+ board.get("DELETE_CHECK") + "\t");
			if(Integer.parseInt(board.get("BLACKLIST").toString()) == 1) {
				System.out.println("미처분");
			} else if(Integer.parseInt(board.get("BLACKLIST").toString()) == 2) {
				System.out.println("블라인드");
			} else if(Integer.parseInt(board.get("BLACKLIST").toString()) == 3) {
				System.out.println("삭제");
			}
		}
		System.out.println("=========================================");
		System.out.println("1.조회\t2.수정\t< 뒤로가기");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			System.out.println("게시글 번호 입력>");
			boardno = ScanUtil.nextInt();
			return View.ADMIN_BOARD_VIEW;
		case "2":
			System.out.println("게시글 번호 입력>");
			boardno = ScanUtil.nextInt();
			System.out.println("수정할 대상 입력");
			System.out.println("1.상태\t2.제목\t3.희망가격\t4.상품이름\t5.삭제여부\t6.블랙리스트\t0.취소");
			System.out.println(">");
			input = ScanUtil.nextLine();
			switch(input) {
			case "0" :
				return View.BOARD_MANAGER;
			case "1" :
				System.out.println("수정할 상태 입력");
				System.out.println("N: 판매완료 Y : 판매중 0 : 취소");
				System.out.println(">");
				String tag = ScanUtil.nextLine();
				if(tag.equals("0")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardTag(boardno, tag);
				}
				return View.BOARD_MANAGER;
			case "2" :
				System.out.println("수정할 제목 입력");
				System.out.println("(0.취소) >");
				String title = ScanUtil.nextLine();
				if(title.equals("0")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardTitle(boardno, title);
				}
				return View.BOARD_MANAGER;
			case "3" :
				System.out.println("수정할 희망가격 입력");
				System.out.println("(0.취소) >");
				int price = ScanUtil.nextInt();
				if(price == 0) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardPrice(boardno, price);
				}
				return View.BOARD_MANAGER;
			case "4" :
				System.out.println("수정할 상품이름 입력");
				System.out.println("(0.취소) >");
				String name = ScanUtil.nextLine();
				if(name.equals("0")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardName(boardno, name);
				}
				return View.BOARD_MANAGER;
			case "5" :
				System.out.println("수정할 삭제여부 입력");
				System.out.println("n : 미삭제 y : 삭제 0 : 취소");
				System.out.println(">");
				String delCheck = ScanUtil.nextLine();
				if(delCheck.equals("0")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardDelCheck(boardno, delCheck);
				}
				return View.BOARD_MANAGER;
			case "6" :
				System.out.println("수정할 블랙리스트 입력");
				System.out.println("1 : 미처분 2 : 블라인드 3. 삭제 0 : 취소");
				System.out.println(">");
				String black = ScanUtil.nextLine();
				if(black.equals("0")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardBlack(boardno, black);
				}
				return View.BOARD_MANAGER;
			}
			return View.BOARD_MANAGER;
		case "<": 
			return View.ADMIN_MENU;
		}
		return View.BOARD_MANAGER;
	}
	public int adminBoardView() { // ======================== 게시물 조회
		Map<String, Object> boardView = adminDao.selectAdminBoardView(boardno);
		System.out.println("=========================================");
		System.out.println("번호 : " + boardView.get("BOARD_NO"));
		System.out.println("제목 : " + boardView.get("TITLE"));
		System.out.println("작성자 : " + boardView.get("NICKNAME"));
		System.out.println("작성일자 : " + sdf1.format(boardView.get("REG_DT")));
		System.out.println("----------------------------------------");
		System.out.println("상품이름 : " + boardView.get("GOODS_NAME"));
		System.out.println("희망가격 : " + boardView.get("PRICE"));
		System.out.println("내용 : " + boardView.get("CONTENT"));
		System.out.println("=========================================");
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		System.out.println("1.수정");
		String input = ScanUtil.nextLine();
		switch (input) {
			case "<" :
				return View.BOARD_MANAGER;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("수정할 대상 입력");
				System.out.println("1.상태\t2.제목\t3.희망가격\t4.상품이름\t5.삭제여부\t6.블랙리스트\t0.취소"); //고민
				System.out.println(">");
				input = ScanUtil.nextLine();
				switch(input) {
				case "0" :
					return View.BOARD_MANAGER;
				case "1" :
					System.out.println("수정할 상태 입력");
					System.out.println("n : 판매완료 y : 판매중 0 : 취소");
					System.out.println(">");
					String tag = ScanUtil.nextLine();
					if(tag.equals("0")) {
						return View.BOARD_MANAGER;
					} else {
						int result = adminDao.updateBoardTag(boardno, tag);
					}
					return View.BOARD_MANAGER;
				case "2" :
					System.out.println("수정할 제목 입력");
					System.out.println("(0.취소) >");
					String title = ScanUtil.nextLine();
					if(title.equals("0")) {
						return View.BOARD_MANAGER;
					} else {
						int result = adminDao.updateBoardTitle(boardno, title);
					}
					return View.BOARD_MANAGER;
				case "3" :
					System.out.println("수정할 희망가격 입력");
					System.out.println("(0.취소) >");
					int price = ScanUtil.nextInt();
					if(price == 0) {
						return View.BOARD_MANAGER;
					} else {
						int result = adminDao.updateBoardPrice(boardno, price);
					}
					return View.BOARD_MANAGER;
				case "4" :
					System.out.println("수정할 상품이름 입력");
					System.out.println("(0.취소) >");
					String name = ScanUtil.nextLine();
					if(name.equals("0")) {
						return View.BOARD_MANAGER;
					} else {
						int result = adminDao.updateBoardName(boardno, name);
					}
					return View.BOARD_MANAGER;
				case "5" :
					System.out.println("수정할 삭제여부 입력");
					System.out.println("n : 미삭제 y : 삭제 0 : 취소");
					System.out.println(">");
					String delCheck = ScanUtil.nextLine();
					if(delCheck.equals("0")) {
						return View.BOARD_MANAGER;
					} else {
						int result = adminDao.updateBoardDelCheck(boardno, delCheck);
					}
					return View.BOARD_MANAGER;
				case "6" :
					System.out.println("수정할 블랙리스트 입력");
					System.out.println("1 : 미처분 2 : 블라인드 3. 삭제 0 : 취소");
					System.out.println(">");
					String black = ScanUtil.nextLine();
					if(black.equals("0")) {
						return View.BOARD_MANAGER;
					} else {
						int result = adminDao.updateBoardBlack(boardno, black);
					}
					return View.BOARD_MANAGER;
				}
				return View.BOARD_MANAGER;
		}
		return View.ADMIN_BOARD_VIEW;
	}
	
	
	
	
}
