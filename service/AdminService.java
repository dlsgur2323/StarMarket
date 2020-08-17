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
	private boolean reportCheck = false;
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월dd일");
	SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
	SimpleDateFormat sdf4 = new SimpleDateFormat("yy/MM/dd HH:mm");
	
	public int adminMenu() { // ==================  관리자 메뉴 ====================
		System.out.println();
		System.out.println("┌───────────────────────────────────────┐");
		System.out.println("│\t★\t★\t★\t★\t│");
		System.out.println("│\t별\t별\t마\t켓\t│");
		System.out.println("│\t\t\t\t\t│");
		System.out.println("└───────────────────────────────────────┘");
		System.out.println("\t\t\t\t★ADMIN★");
		System.out.println();
		System.out.println("\t 1.  블   랙   리   스   트      관   리");
		System.out.println();
		System.out.println("\t 2.  신   고      내   역");
		System.out.println();
		System.out.println("\t 3.  게   시   물      관   리");
		System.out.println();
		System.out.println("\t 4.  공   지   사   항      관   리");
		System.out.println();
		System.out.println("\t\t　　   ┌─────────────────┐");
		System.out.println("\t\t　　   │  0. 로 그  아 웃　    │");
		System.out.println("\t\t　　   └─────────────────┘");
		System.out.println(" ───────────────────────────────────────");
		System.out.print(" 번호 입력>");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "1" :
				return View.BLACKLIST_MENU;
			case "2" :
				return View.REPORT_MENU;
			case "3" : 
				return View.BOARD_MANAGER;
			case "4" :
				return View.NOTICE_LIST;
			case "0" :
				Controller.loginUser = null;
				return View.HOME;
		}
		return View.ADMIN_MENU;
		
	}
	public int blackListMenu() { // ====================== 블랙리스트 관리 메뉴 
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌───────────────────────────────┐");
		System.out.println(" │\t  \t  \t \t │");
		System.out.println(" │\t블   랙   리   스   트      관   리\t │");
		System.out.println(" │\t  \t  \t \t │");
		System.out.println(" └───────────────────────────────┘");
		System.out.println("\n\n");
		System.out.println("　　　　 1.블 랙 리 스 트  회 원  ");
		System.out.println("\n\n\n");
		System.out.println("　　　　2.블 랙 리 스 트  게 시 물");
		System.out.println("\n\n");
		System.out.println(" ────────────────────────────────");
		System.out.println(" \t\t\t◀ 뒤로 가기");
		System.out.println(" ────────────────────────────────");
		System.out.print("메뉴 선택 >");
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
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌──────────────────────────────────────────────────────────────────┐");
		System.out.println(" │　　\t\t블　　랙　　리　　스　　트　　　관　　리\t\t　　│");
		System.out.println(" └──────────────────────────────────────────────────────────────────┘");
		System.out.println("　　　아이디　　│　　닉네임　　│　　　블랙리스트 시작　　　│　　　 블랙리스트 종료　　　");
		System.out.println("　───────────────────────────────────────────────────────────────────");
		for(Map<String, Object> user : userList){
			System.out.println("   "+ user.get("USER_ID") + "\t"
										+ "  "+user.get("NICKNAME") + "　　"
										+ user.get("BLACKLIST_START") + "　　"
										+ user.get("BLACKLIST_END"));
		}
		System.out.println("　──────────────────────────────────────────────────────────────────");
		System.out.println("\t\t\t\t\t\t◀뒤로가기   0.메인메뉴");
		System.out.println("　1.처분변경");
		System.out.println("　──────────────────────────────────────────────────────────────────");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.BLACKLIST_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.print("　처분변경 할 아이디 입력>");
				String userid = ScanUtil.nextLine();
				System.out.println("\n\n\n\n\n\n\n\n");
				System.out.println(" ┌─────────────────────┐");
				System.out.println(" │　처　분　변　경　사　항   │");
				System.out.println(" └─────────────────────┘");
				System.out.println("      1. 미 처 분");
				System.out.println();
				System.out.println("      2. 일 시 정 지");
				System.out.println();
				System.out.println("      3. 영 구 정 지");
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				System.out.print(" 입력 >");
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
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌──────────────────────────────────────────────────────────────────┐");
		System.out.println(" │　　\t\t블　　랙　　리　　스　　트　　　관　　리\t\t　　│");
		System.out.println(" └──────────────────────────────────────────────────────────────────┘");
		System.out.println("　　글번호　│　　　　　제목　　　　　│　　작성자　　│　　블랙리스트 상태　");
		System.out.println("　───────────────────────────────────────────────────────────────────");
		for(Map<String, Object> board : boardList){
			System.out.print("　　　"+board.get("BOARD_NO") + "\t　　　　"
										+ board.get("TITLE") + "\t　　"
										+ board.get("USER_ID") + "\t");
			if(Integer.parseInt(board.get("BLACKLIST").toString()) == 2) {
				System.out.println("블라인드");
			} else if(Integer.parseInt(board.get("BLACKLIST").toString()) == 3) {
				System.out.println("삭제");
			} else if(Integer.parseInt(board.get("BLACKLIST").toString()) == 4) {
				System.out.println("복구");
			}System.out.println();
		}
		System.out.println("　───────────────────────────────────────────────────────────────────");
		System.out.println("\t\t\t\t\t\t◀뒤로가기   0.메인메뉴");
		System.out.println("　1.처분변경");
		System.out.println("　───────────────────────────────────────────────────────────────────");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.BLACKLIST_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("처분 할 글번호 입력>");
				int boardno = ScanUtil.nextInt();
				System.out.println("\n\n\n\n\n\n\n\n");
				System.out.println(" ┌─────────────────────┐");
				System.out.println(" │　처　분　변　경　사　항   │");
				System.out.println(" └─────────────────────┘");
				System.out.println("      1. 미 처 분");
				System.out.println();
				System.out.println("      2. 블 라 인 드");
				System.out.println();
				System.out.println("      3. 삭 제");
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				System.out.print(" 입력 >");
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
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌───────────────────────┐");
		System.out.println(" │\t         \t │");
		System.out.println(" │\t신   고   내   역\t │");
		System.out.println(" │\t         \t │");
		System.out.println(" └───────────────────────┘");
		System.out.println("\n\n");
		System.out.println("　　　　1.회 원   신 고  ");
		System.out.println("\n\n\n");
		System.out.println("　　　　2.게 시 물  신고");
		System.out.println("\n\n");
		System.out.println(" ────────────────────────");
		System.out.println(" \t\t◀ 뒤로 가기");
		System.out.println(" ────────────────────────");
		System.out.print(" 메뉴 선택 >");

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
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println(" ┌────────────────────────────────────────┐");
		System.out.println(" │　　\t회　　원　　신　　고　　내　　역　　\t  │");
		System.out.println(" └────────────────────────────────────────┘");
		for(Map<String, Object> board : boardList){
			System.out.println();
			System.out.println("　─────────────────────────────────────────");
			System.out.print("   신고번호 : " + board.get("REPORT_NO") + "\t\t처분여부 : ");
			if(Integer.parseInt(board.get("DISPOSITION").toString()) == 1) {
				System.out.println("미처분");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 2) {
				System.out.println("일시정지");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 3) {
				System.out.println("영구정지");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 4) {
				System.out.println("복구");
			}
			System.out.println("   피신고자 : " + board.get("REPORTED_ID") + "\t"
								+ "신고자 : " + board.get("USER_ID"));
			System.out.println("   신고내용 : ");
			System.out.println("         " + board.get("REPORT_CONTENT"));
			System.out.println("   신고일자 : "+ sdf1.format(board.get("REG_DT")));
			System.out.println("　─────────────────────────────────────────");
		}
		System.out.println("\t\t\t◀뒤로가기   0.메인메뉴");
		System.out.println("　1.처분변경");
		System.out.println("　─────────────────────────────────────────");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.REPORT_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				
				System.out.println("처분 할 신고번호 입력>");
				int reportno = ScanUtil.nextInt();
				System.out.println("\n\n\n\n\n\n\n\n");
				System.out.println(" ┌─────────────────────┐");
				System.out.println(" │　처　분　변　경　사　항   │");
				System.out.println(" └─────────────────────┘");
				System.out.println("      1. 미 처 분");
				System.out.println();
				System.out.println("      2. 일 시 정 지");
				System.out.println();
				System.out.println("      3. 영 구 정 지");
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				System.out.print(" 입력 >");
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
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println(" ┌──────────────────────────────────────────────┐");
		System.out.println(" │　　\t게　　시　　물　　신　　고　　내　　역　　\t│");
		System.out.println(" └──────────────────────────────────────────────┘");
		List<Map<String, Object>> boardList = adminDao.selectReportBoard();
		for(Map<String, Object> board : boardList){
			System.out.println("　───────────────────────────────────────────────");
			System.out.print("　　신고번호 : " + board.get("REPORT_NO") + "\t\t\t처분여부 : ");
			if(Integer.parseInt(board.get("DISPOSITION").toString()) == 1) {
				System.out.println("미처분");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 2) {
				System.out.println("블라인드");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 3) {
				System.out.println("삭제");
			} else if(Integer.parseInt(board.get("DISPOSITION").toString()) == 4) {
				System.out.println("복구");
			}
			System.out.println("　　게시물번호 : " + board.get("BOARD_NO") + "\t\t\t"
								+ "신고자 : " + board.get("USER_ID"));
			System.out.println("　　신고내용 : ");
			System.out.println("　　　　　　"+board.get("REPORT_CONTENT"));
			System.out.println("　　신고일자 : "+ sdf1.format(board.get("REG_DT")));
			System.out.println("　───────────────────────────────────────────────");
		}
		System.out.println("\t\t\t\t◀뒤로가기   0.메인메뉴");
		System.out.println("　1.처분변경  2.게시글 조회");
		System.out.println("　───────────────────────────────────────────────");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "<" :
				return View.REPORT_MENU;
			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("처분 할 신고번호 입력>");
				int reportno = ScanUtil.nextInt();
				System.out.println("\n\n\n\n\n\n\n\n");
				System.out.println(" ┌─────────────────────┐");
				System.out.println(" │　처　분　변　경　사　항   │");
				System.out.println(" └─────────────────────┘");
				System.out.println("      1. 미 처 분");
				System.out.println();
				System.out.println("      2. 블 라 인 드");
				System.out.println();
				System.out.println("      3. 삭 제");
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				input = ScanUtil.nextLine();
				switch(input) {
				case "0" :
					return View.REPORT_BOARD;
				case "1" : case "2" : case "3" :
					int result = adminDao.boardDisposition(reportno, input);
					return View.REPORT_BOARD;
				}
				case "2" :
				System.out.println("조회할 게시글 번호 입력>");
				boardno = ScanUtil.nextInt();
				reportCheck = true;
				return View.ADMIN_BOARD_VIEW;
		}
		return View.REPORT_BOARD;
	}
	
	
	
	
	
	
	private int boardno;
	String format = "%-30s";
	public int boardManager() { // ================================ 게시물 리스트 조회 
		List<Map<String, Object>> boardList = adminDao.selectAdminBoardList();
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌───────────────────────────────────────────────────────────────────────────────────────────────────────────┐");
		System.out.println(" │　　\t\t\t\t\t게　　　시　　　물　　　　관　　　리\t\t\t\t\t　　　│");
		System.out.println(" └───────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
		System.out.println("　　번호　│　상태　│　　　　　　제목　　　　　　　　│　희망가격　│　상품이름　│　　작성자　　│　작성일　│　삭제여부　│　블랙리스트　");
		System.out.println("　────────────────────────────────────────────────────────────────────────────────────────────────────────────");
		for(Map<String, Object> board : boardList){
			System.out.print("　　　"+board.get("BOARD_NO") + "\t    ");
			if(board.get("TAG").equals("Y")) {
				System.out.print(" 판매중" + "\t ");
			} else {
				System.out.print(" 판매완료" + "\t");
			}
			System.out.printf(format,board.get("TITLE")); 
			System.out.print( 	board.get("PRICE") + "\t"
								+ board.get("GOODS_NAME") + "\t"
								+ board.get("USER_ID") + "\t"
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
		System.out.println("　────────────────────────────────────────────────────────────────────────────────────────────────────────────");
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t◀뒤로가기  ");
		System.out.println("　1.조 회　　　2.수 정");
		System.out.println("　────────────────────────────────────────────────────────────────────────────────────────────────────────────");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			System.out.println("게시글 번호 입력>");
			boardno = ScanUtil.nextInt();
			return View.ADMIN_BOARD_VIEW;
		case "2":
			System.out.print("수정 할 게시글 번호 입력>");
			boardno = ScanUtil.nextInt();
			System.out.println("\n\n\n\n\n\n\n\n");
			System.out.println(" ┌─────────────────────┐");
			System.out.println(" │　수　정　할　대　상　     │");
			System.out.println(" └─────────────────────┘");
			System.out.println("      1. 상 태");
			System.out.println();
			System.out.println("      2. 제 목");
			System.out.println();
			System.out.println("      3. 희 망 가 격");
			System.out.println();
			System.out.println("      4. 상 품 이 름");
			System.out.println();
			System.out.println("      5. 삭 제 여 부");
			System.out.println();
			System.out.println("      6. 블 랙 리 스 트");
			System.out.println(" ─────────────────────");
			System.out.println("\t\t ◀ 취 소");
			System.out.println(" ─────────────────────");
			input = ScanUtil.nextLine();
			switch(input) {
			case "<" :
				return View.BOARD_MANAGER;
			case "1" :
				System.out.println("\n\n\n\n\n\n\n");
				System.out.println(" ─────────────────────");
				System.out.println("     수정할 상태 입력");
				System.out.println();
				System.out.println("  N: 판매완료      Y : 판매중 ");
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				String tag = ScanUtil.nextLine();
				if(tag.equals("<")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardTag(boardno, tag);
				}
				return View.BOARD_MANAGER;
			case "2" :
				System.out.println("\n\n\n\n\n\n\n");
				System.out.println(" ─────────────────────");
				System.out.println("     수정할 제목 입력");
				System.out.println();
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				String title = ScanUtil.nextLine();
				if(title.equals("<")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardTitle(boardno, title);
				}
				return View.BOARD_MANAGER;
			case "3" :
				System.out.println("\n\n\n\n\n\n\n");
				System.out.println(" ─────────────────────");
				System.out.println("     수정할 가격 입력");
				System.out.println();
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				String price = ScanUtil.nextLine();
				if(price.equals("<")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardPrice(boardno, Integer.parseInt(price));
				}
				return View.BOARD_MANAGER;
			case "4" :
				System.out.println("\n\n\n\n\n\n\n");
				System.out.println(" ─────────────────────");
				System.out.println("     수정할 상품이름 입력");
				System.out.println();
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				String name = ScanUtil.nextLine();
				if(name.equals("<")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardName(boardno, name);
				}
				return View.BOARD_MANAGER;
			case "5" :
				System.out.println("\n\n\n\n\n\n\n");
				System.out.println(" ─────────────────────");
				System.out.println("     수정할 삭제여부 입력");
				System.out.println();
				System.out.println("  N: 미삭제      Y : 삭제");
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				String delCheck = ScanUtil.nextLine();
				if(delCheck.equals("<")) {
					return View.BOARD_MANAGER;
				} else {
					int result = adminDao.updateBoardDelCheck(boardno, delCheck);
				}
				return View.BOARD_MANAGER;
			case "6" :
				System.out.println("\n\n\n\n\n\n\n");
				System.out.println(" ───────────────────────");
				System.out.println("     수정할 블랙리스트 입력");
				System.out.println();
				System.out.println("  1:미처분   2:블라인드   3:삭제 ");
				System.out.println(" ───────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ───────────────────────");
				String black = ScanUtil.nextLine();
				if(black.equals("<")) {
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
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌──────────────────────────────────────┐");
		System.out.println(" │　　\t게　　시　　물　　　조　　회　　\t│");
		System.out.println(" └──────────────────────────────────────┘");
		System.out.println("    번호 : " + boardView.get("BOARD_NO"));
		if(boardView.get("TAG").equals("Y")) {
			System.out.print("판매중" + "\n");
		} else {
			System.out.print("판매완료" + "\n");
		}
		System.out.println("    제목 : " + boardView.get("TITLE"));
		System.out.println("    작성자 : " + boardView.get("NICKNAME"));
		System.out.println("    작성일자 : " + sdf1.format(boardView.get("REG_DT")));
		System.out.println(" ----------------------------------------");
		System.out.println("    상품이름 : " + boardView.get("GOODS_NAME"));
		System.out.println("    희망가격 : " + boardView.get("PRICE"));
		System.out.println("    내용 : ");
		Controller.lineEnter(boardView.get("CONTENT").toString(),20);
		System.out.println("　───────────────────────────────────────────");
		System.out.println("\t\t\t◀뒤로가기   0.메인메뉴");
		System.out.println("　1.수정");
		System.out.println("　───────────────────────────────────────────");
		String input = ScanUtil.nextLine();
		switch (input) {
			case "<" :
				if(reportCheck == true){
					reportCheck = false;
					return View.REPORT_BOARD;
				} else {
					return View.BOARD_MANAGER;
				}			case "0" :
				return View.ADMIN_MENU;
			case "1" :
				System.out.println("\n\n\n\n\n\n\n\n");
				System.out.println(" ┌─────────────────────┐");
				System.out.println(" │　수　정　할　대　상　     │");
				System.out.println(" └─────────────────────┘");
				System.out.println("      1. 상 태");
				System.out.println();
				System.out.println("      2. 제 목");
				System.out.println();
				System.out.println("      3. 희 망 가 격");
				System.out.println();
				System.out.println("      4. 상 품 이 름");
				System.out.println();
				System.out.println("      5. 삭 제 여 부");
				System.out.println();
				System.out.println("      6. 블 랙 리 스 트");
				System.out.println(" ─────────────────────");
				System.out.println("\t\t ◀ 취 소");
				System.out.println(" ─────────────────────");
				input = ScanUtil.nextLine();
				switch(input) {
				case "<" :
					return View.BOARD_MANAGER;
				case "1" :
					System.out.println("\n\n\n\n\n\n\n");
					System.out.println(" ─────────────────────");
					System.out.println("     수정할 상태 입력");
					System.out.println();
					System.out.println("  N: 판매완료      Y : 판매중 ");
					System.out.println(" ─────────────────────");
					System.out.println("\t\t ◀ 취 소");
					System.out.println(" ─────────────────────");
					String tag = ScanUtil.nextLine();
					if(tag.equals("<")) {
						return View.ADMIN_BOARD_VIEW;
					} else {
						int result = adminDao.updateBoardTag(boardno, tag);
					}
					return View.ADMIN_BOARD_VIEW;
				case "2" :
					System.out.println("\n\n\n\n\n\n\n");
					System.out.println(" ─────────────────────");
					System.out.println("     수정할 제목 입력");
					System.out.println();
					System.out.println(" ─────────────────────");
					System.out.println("\t\t ◀ 취 소");
					System.out.println(" ─────────────────────");
					String title = ScanUtil.nextLine();
					if(title.equals("<")) {
						return View.ADMIN_BOARD_VIEW;
					} else {
						int result = adminDao.updateBoardTitle(boardno, title);
					}
					return View.ADMIN_BOARD_VIEW;
				case "3" :
					System.out.println("\n\n\n\n\n\n\n");
					System.out.println(" ─────────────────────");
					System.out.println("     수정할 가격 입력");
					System.out.println();
					System.out.println(" ─────────────────────");
					System.out.println("\t\t ◀ 취 소");
					System.out.println(" ─────────────────────");
					String price = ScanUtil.nextLine();
					if(price.equals("<")) {
						return View.ADMIN_BOARD_VIEW;
					} else {
						int result = adminDao.updateBoardPrice(boardno, Integer.parseInt(price));
					}
					return View.ADMIN_BOARD_VIEW;
				case "4" :
					System.out.println("\n\n\n\n\n\n\n");
					System.out.println(" ─────────────────────");
					System.out.println("     수정할 상품이름 입력");
					System.out.println();
					System.out.println(" ─────────────────────");
					System.out.println("\t\t ◀ 취 소");
					System.out.println(" ─────────────────────");
					String name = ScanUtil.nextLine();
					if(name.equals("<")) {
						return View.ADMIN_BOARD_VIEW;
					} else {
						int result = adminDao.updateBoardName(boardno, name);
					}
					return View.ADMIN_BOARD_VIEW;
				case "5" :
					System.out.println("\n\n\n\n\n\n\n");
					System.out.println(" ─────────────────────");
					System.out.println("     수정할 삭제여부 입력");
					System.out.println();
					System.out.println("  N: 미삭제      Y : 삭제");
					System.out.println(" ─────────────────────");
					System.out.println("\t\t ◀ 취 소");
					System.out.println(" ─────────────────────");
					String delCheck = ScanUtil.nextLine();
					if(delCheck.equals("<")) {
						return View.ADMIN_BOARD_VIEW;
					} else {
						int result = adminDao.updateBoardDelCheck(boardno, delCheck);
					}
					return View.ADMIN_BOARD_VIEW;
				case "6" :
					System.out.println("\n\n\n\n\n\n\n");
					System.out.println(" ───────────────────────");
					System.out.println("     수정할 블랙리스트 입력");
					System.out.println();
					System.out.println("  1:미처분   2:블라인드   3:삭제 ");
					System.out.println(" ───────────────────────");
					System.out.println("\t\t ◀ 취 소");
					System.out.println(" ───────────────────────");
					String black = ScanUtil.nextLine();
					if(black.equals("<")) {
						return View.ADMIN_BOARD_VIEW;
					} else {
						int result = adminDao.updateBoardBlack(boardno, black);
					}
					return View.ADMIN_BOARD_VIEW;
				}
				return View.BOARD_MANAGER;
		}
		return View.ADMIN_BOARD_VIEW;
	}

//==============================================================
	private void stop(int interval){ //private을 붙여줘서 사용자 입장에서 불필요한 정보를 안볼 수 있게 할 수 있다. 
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			e.printStackTrace(); //밀리second 단위 1000이 1초
		}
	}
	
	
	
}
