package service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.UserDao;

public class UserService {
	
	private static UserService instance;
	private UserService(){}
	public static UserService getInstance(){
		if (instance == null){
			instance = new UserService();
		}
		return instance;
	}
	private UserDao userDao = UserDao.getInstance();
	private String currentNickname;
	private int currentHp;
	public int boardno;
	private Map<String, Object> param = new HashMap<>();

	
	public int join(){
		System.out.println("========== 회원가입 ==========");
		System.out.print("아이디>");
		String userId = ScanUtil.nextLine();
		Map<String, Object> userIdCheck = userDao.userIdCheck(userId);
		if(userIdCheck == null){
			String regexid = "[a-z0-9_-]{5,20}"; //a-z,0-9,_,- 중  5개 이상 20개 이하
			Pattern pid = Pattern.compile(regexid);
			Matcher mid = pid.matcher(userId);
			if(mid.matches()){
				param.put("USER_ID", userId);
				passwordCheck(); //비밀번호 확인 메서드
				int result = userDao.insertUser(param);
				if(0 < result){
					System.out.println("회원가입 성공");
				} else {
					System.out.println("회원가입 실패");
				}
				return View.HOME;
			}else{
				System.out.println("5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
				return View.JOIN;
			}
		}else{
			System.out.println("해당 아이디는 이미 사용중 입니다. 다른 아이디를 입력해주세요.");
			return View.JOIN;
		}
	}
		public void passwordCheck(){ //비밀번호 유효성 검사 메서드
			while(true){
			System.out.print("비밀번호>");
			String password = ScanUtil.nextLine();
			String regexpassword = "(?=.*\\d{1,50})(?=.*[~`!@#$%\\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,20}$";
			Pattern pid = Pattern.compile(regexpassword);
			Matcher mid = pid.matcher(password);
			if(mid.matches()){
				param.put("PASSWORD", password);
				System.out.print("이름>");
				String userName = ScanUtil.nextLine();
				System.out.print("닉네임>");
				String NickName = ScanUtil.nextLine();
				System.out.print("핸드폰번호>");
				String hp = ScanUtil.nextLine();
				param.put("USERNM", userName);
				param.put("NICKNAME", NickName);
				param.put("HP", hp);
				break;
			}else{
				System.out.println(" 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용하여 8자리 이상 입력하세요.");
			}
		}
		}
	
	
	public int login(){
		System.out.println("============ 로그인 ============");
		System.out.print("아이디>");
		String userId = ScanUtil.nextLine();
		System.out.print("비밀번호>");
		String password = ScanUtil.nextLine();
		
		Map<String, Object> user = userDao.selectUser(userId, password);
		
		if(user == null){
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
		} else {
			System.out.println("로그인 성공");
			
			Controller.loginUser = user;
			if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){
				return View.ADMIN_MENU;
			}
			return View.NOTICE_POPUP;
		}
		return View.LOGIN;
	}
	
	//마이페이지
		public int myPage(){ 
			Map<String, Object> userMypageView = userDao.userMypageView(userid);
			System.out.println("――――――――――――――――――＊――――――――――――――――――");
			System.out.println("\t　　＊마　이　플　래　닛＊\t\t");
			System.out.println("――――――――――――――――――＊――――――――――――――――――\n");
			System.out.println("\t닉네임 : ["+ userMypageView.get("NICKNAME") +"] 평점 : " + userMypageView.get("GRADE") + "\n");
			System.out.println("\t　　　1.　프로필 설정");
			System.out.println("\t　　　2.　작성글 목록");
			System.out.println("\t　　　3.　관심 목록");
			System.out.println("\t　　　4.　거래 이력\n");
			System.out.println("――――――――――――――――――＊――――――――――――――――――");
			System.out.println("\t\t\t\t\t<뒤로가기");
			System.out.println("―――――――――――――――――――――――――――――――――――――");
			System.out.println("메뉴 선택 >");
			String input = ScanUtil.nextLine();
			
			switch(input){
			case "1" :
				return View.PROFILE_OPTION;
			case "2" :
				return View.WRITING_LIST;
			case "3" :
				return View.INTEREST_LIST;
			case "4" :
				return View.TRADE_HISTORY;
			case "<" :
				return View.MAIN_MENU;
			}
			return View.HOME;
		}
		//프로필 설정 조회
		private String userid;	
		private String currentnickname;
		private String currentpassword;
		
		public int profileOption() {
			Map<String, Object> profileView = userDao.userProfileView(userid);
			System.out.println("――――――――――――――――――――――＊―――――――――――――――――――");
			System.out.println("아이디　　: " + profileView.get("USER_ID"));
			System.out.println("비밀번호　: " + profileView.get("PASSWORD"));
			System.out.println("이름　　　: " + profileView.get("USERNM"));
			System.out.println("닉네임　　: " + profileView.get("NICKNAME"));
			System.out.println("핸드폰번호: " + profileView.get("HP"));
			System.out.println("――――――――――――――――――――――＊―――――――――――――――――――");
			System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
			System.out.println("1.수정\t2.탈퇴");
			String input = ScanUtil.nextLine();
			switch (input){
			case "1": 
				
				System.out.println("1.비밀번호 \t2.닉네임\t3.핸드폰번호\t\t0.취소");
				System.out.println("수정할 대상 입력>");
				int input2 = ScanUtil.nextInt();			
				switch (input2) {
				case 1:
					return updatePassword(currentpassword, userid);
				case 2:
					return updateNickname(currentnickname, userid);
				case 3:
					return updateHp(currentHp, userid);	
				case 0:
					return View.PROFILE_OPTION;
				}
			case "2": 
				System.out.println("별별 마켓을 탈퇴하시겠습니까?");
				System.out.println("1.확인\t< 취소");
				String input3 = ScanUtil.nextLine();			
				switch (input3) {
				case "1":
					return deleteUserid(userid);
				case "<":
					return View.PROFILE_OPTION;
				}		
			case "<": return View.MY_PAGE;
			case "0": return View.MAIN_MENU;
			}		
			return View.MY_PAGE;
		}
		//비밀번호 수정	
		private int updatePassword(String currentpassword, String userid) {  
			System.out.println("새 비밀번호 입력>");
			String password = ScanUtil.nextLine();
			int result = userDao.updateNickname(password, userid);
			if(0 < result){
				System.out.println("비밀번호 수정 완료");
				userDao.updatePassword(password, userid);			
			} else {
				System.out.println("비밀번호 수정 실패");
			}
			return View.MY_PAGE;
		}
		
		//닉네임 수정	
		private int updateNickname(String currentnickname, String userid) {  
			System.out.println("수정할 내용 입력>");
			String nickname = ScanUtil.nextLine();
			int result = userDao.updateNickname(nickname, userid);
			if(0 < result){
				System.out.println("닉네임 수정 완료");
			} else {
				System.out.println("닉네임 수정 실패");
			}
			return View.PROFILE_OPTION;
		}	
		//핸드폰번호 수정
		private int updateHp (int currenthp, String userid) {  
			System.out.println("수정할 내용 입력>");
			String hp = ScanUtil.nextLine();
			int result = userDao.updateHp(hp, userid);
			if(0 < result){
				System.out.println("핸드폰번호 수정 완료");
			} else {
				System.out.println("핸드폰번호 수정 실패");
			}
			return View.PROFILE_OPTION;
		}
		//유저 회원탈퇴
		private int deleteUserid(String userid) {
			int result = userDao.deleteUserid(userid);
			if(0 < result){
				System.out.println("회원탈퇴 완료");
			} else {
				System.out.println("탈퇴 실패");
			}
			return View.MY_PAGE;
		}	
		
		//작성글 목록 조회 
		public int writingList() {
			List<Map<String, Object>> writingList = userDao.selectWritingList();
			System.out.println("=========================================");
			System.out.println("번호\t상태\t제목\t작성자\t작성일");
			System.out.println("-----------------------------------------");
			for(Map<String, Object> board : writingList){
				System.out.print(board.get("BOARD_NO") + "\t");
				if(board.get("TAG").equals("Y")) {
					System.out.print("판매중" + "\t");
				} else {
					System.out.print("판매완료" + "\t");
				}
				System.out.println(board.get("TITLE") + "\t"
						    		+ board.get("NICKNAME") + "\t"
						    		+ sdf1.format(board.get("REG_DT")));
			}
			System.out.println("=========================================");
			System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
			System.out.println("1.조회\t2.삭제");
			String input = ScanUtil.nextLine();
			switch(input){
			case "1":
				System.out.println("조회할 번호를 입력해주세요");
				BoardService.boardno = ScanUtil.nextInt();
				return View.BOARD_VIEW;
			case "<":
				return View.MY_PAGE;
			case "0":
				return View.MAIN_MENU;
			case "2":
				System.out.println("삭제를 원하시는 게시글 번호를 입력해주세요>");
				boardno = ScanUtil.nextInt();				
				System.out.println("삭제하시겠습니까?");
				System.out.println("1.확인\t2.취소");
				input = ScanUtil.nextLine();
				switch (input) {
				case "1":
					return updateWritingList(boardno); //작성글 삭제
//					userDao.updateTag(boardno); 	   //작성글 판매태그변경 판매완료가 아니고 그냥 삭제라면?
				case "2":
					return View.WRITING_LIST;
				}
			}
			return View.MY_PAGE;
		}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd");
		//작성글리스트에서 작성글 삭제 
			private int updateWritingList(int boardno) { 
				int result = userDao.updateWritingList(boardno);
				if(0 < result){
					System.out.println("\n게시글 삭제가 완료되었습니다.\n");
				} else {
					System.out.println("삭제 실패");
				}
				return View.WRITING_LIST;
			}
			
			
		//관심 목록 조회 
		public int interestList() {
			List<Map<String, Object>> interestList = userDao.interestList();
			System.out.println("=========================================");
			System.out.println("번호\t게시글 번호\t게시글 제목\t관심 상품");
			System.out.println("-----------------------------------------");
			for(Map<String, Object> interestrow : interestList){
				System.out.print(interestrow.get("INTEREST_NO")+"\t");
				System.out.print(interestrow.get("BOARD_NO")+"\t");
				System.out.print(interestrow.get("TITLE")+"\t");
				System.out.print(interestrow.get("GOODS_NAME"));
				System.out.println();
			}
			System.out.println("=========================================");
			System.out.println("1.조회\t2.삭제\t\t<뒤로가기   0.메인메뉴");
			String input = ScanUtil.nextLine();
			switch(input){
			case "1":
				BoardService.check = true;
				System.out.print("조회할 번호를 입력해주세요 >");
				int interest_no = ScanUtil.nextInt();
				Map<String, Object> selectInterest = userDao.selectInterest(interest_no);
				BoardService.boardno = Integer.parseInt(selectInterest.get("BOARD_NO").toString());
				return View.BOARD_VIEW;
			case "2":
				System.out.println("삭제할 번호를 입력해주세요.");
				int input2 =ScanUtil.nextInt();
				int deleteInterest = userDao.deleteInterest(input2);
				if(deleteInterest == 1){
					System.out.println("삭제가 완료되었습니다.");
					return View.INTEREST_LIST;
				}else{
					System.out.println("삭제가 실패하였습니다.");
					return View.INTEREST_LIST;
				}
			case "<":
				return View.MY_PAGE;
			case "0":
				return View.MAIN_MENU;
			}
			return View.MY_PAGE;
		}
		// 거래이력은 조회만 되게 
		//거래이력 조회리스트 판매자 구매자 아이디 해당상품  거래일자 (정렬은 거래일자) 
	public int tradeHistory() {
		System.out.println("――――――――――――――――――――――＊―――――――――――――――――――\n");
		System.out.println("1.구매이력 조회\n2.판매이력 조회\n<뒤로가기\n");
		System.out.println("――――――――――――――――――――――＊―――――――――――――――――――");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			return View.BUY_TRADE_HISTORY;
		case "2":
			return View.SELL_TRADE_HISTORY;
		case "<":
			return View.MY_PAGE;
		}
		return View.MY_PAGE;
	}

	// 구매이력 조회
	public int buyHistory() {
		System.out.println("―――――――――――――――――――――＊――――――――――――――――――――");
		System.out.println("거래번호\t\t구매한 물품\t\t판매자\t\t거래일자\t\t리뷰");
		for (Map<String, Object> tradehistory : userDao.buyHistory()) {
			Map<String, Object> reviewCheck = userDao.reviewCheck(tradehistory.get("TRADE_NO"));
			System.out.print(tradehistory.get("TRADE_NO") + "\t\t" + tradehistory.get("GOODS_NAME") + "\t\t");
			System.out.print(tradehistory.get("SELLER_ID") + "\t\t" + tradehistory.get("REG_DT"));
			if (reviewCheck == null) {
				System.out.println("리뷰 없음");
			} else {
				System.out.println("리뷰 있음");
			}
		}
		System.out.println("―――――――――――――――――――――＊――――――――――――――――――――");
		System.out.println("1.리뷰조회\t\t\t<뒤로가기");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			System.out.println("조회할 거래번호 입력>");
			int input2 = ScanUtil.nextInt();
			Map<String, Object> reviewCheck = userDao.reviewCheck(input2);
			if (reviewCheck == null) {
				System.out.println("작성된 리뷰가 없습니다.");
				return View.BUY_TRADE_HISTORY;
			} else {
				BoardService.tradeno = input2;
				return View.REVIEW_VIEW;
			}
		case "<":
			return View.TRADE_HISTORY;
		}
		return View.MY_PAGE;
	}

	// 판매이력 조회
	public int sellHistory() {
		System.out.println("―――――――――――――――――――――＊――――――――――――――――――――");
		System.out.println("거래번호\t\t판매한 물품\t\t구매자\t\t거래일자\t\t리뷰");
		for (Map<String, Object> tradehistory : userDao.sellHistory()) {
			Map<String, Object> reviewCheck = userDao.reviewCheck(tradehistory.get("TRADE_NO"));
			System.out.print(tradehistory.get("TRADE_NO") + "\t\t" + tradehistory.get("GOODS_NAME") + "\t\t");
			System.out.print(tradehistory.get("BUYER_ID") + "\t\t" + tradehistory.get("REG_DT"));
			if (reviewCheck == null) {
				System.out.println("리뷰 없음");
			} else {
				System.out.println("리뷰 있음");
			}
		}
		System.out.println("―――――――――――――――――――――＊――――――――――――――――――――");
		System.out.println("1.리뷰조회\t\t\t<뒤로가기");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			System.out.println("조회할 거래번호 입력>");
			int input2 = ScanUtil.nextInt();
			Map<String, Object> reviewCheck = userDao.reviewCheck(input2);
			if (reviewCheck == null) {
				System.out.println("작성된 리뷰가 없습니다.");
				return View.SELL_TRADE_HISTORY;
			} else {
				BoardService.tradeno = input2;
				return View.REVIEW_VIEW;
			}

		case "<":
			return View.TRADE_HISTORY;
		}
		return View.MY_PAGE;
	}	
	

	
	
	
}


