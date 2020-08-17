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
	public static boolean paramcheck = false;
	private Map<String, Object> param = new HashMap<>();
	public static boolean buyHistoryPage= false; //리뷰 뒤로가기 수정
	public static boolean sellHistoryPage= false; //리뷰 뒤로가기 수정
	public static boolean writingCheck= false;
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월dd일");
	
	public int join(){
		System.out.println("\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌──────────────────────────────────────┐");
		System.out.println(" │\t    \t  \t \t \t│");
		System.out.println(" │\t   회\t 원\t가\t입\t│");
		System.out.println(" │\t    \t  \t \t \t│");
		System.out.println(" └──────────────────────────────────────┘");	
		System.out.println("\n");
		System.out.println("　　　아 이 디 : ");
		System.out.println();
		System.out.println("　　　비 밀 번 호 : ");
		System.out.println();
		System.out.println("　　　이 름 :");
		System.out.println();
		System.out.println("　　　닉 네 임 :");
		System.out.println();
		System.out.println("　　　핸 드 폰 번 호 :");
		System.out.println();
		System.out.println(" ───────────────────────────────────────");

		String userId = ScanUtil.nextLine();
		Map<String, Object> userIdCheck = userDao.userIdCheck(userId);
			if(userIdCheck == null){
				String regexid = "[a-z0-9_-]{5,20}"; //a-z,0-9,_,- 중  5개 이상 20개 이하
				Pattern pid = Pattern.compile(regexid);
				Matcher mid = pid.matcher(userId);
				if(mid.matches()){
					param.put("USER_ID", userId);
					passwordCheck(userId); //비밀번호 확인 메서드
					if(paramcheck){
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.println("\t  ─────────────────────────");
						System.out.println("\t　　　❌ 회 원 가 입  실 패 ❌");
						System.out.println("\t  ─────────────────────────");
						System.out.println("\n\n\n\n\n\n\n\n");
						stop(2000);
						return View.HOME;
					}
					else{
					int result = userDao.insertUser(param);
					if(0 < result){
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.println("\t  ─────────────────────────");
						System.out.println("\t　　　✨ 회 원 가 입  성 공✨");
						System.out.println("\t 　   가입한 정보로 로그인 해주세요.");
						System.out.println("\t  ─────────────────────────");
						System.out.println("\n\n\n\n\n\n\n\n");
						stop(2000);
					} else {
						System.out.println("\n\n\n\n\n\n\n\n");
						System.out.println("\t  ─────────────────────────");
						System.out.println("\t　　　❌ 회 원 가 입  실 패 ❌");
						System.out.println("\t  ─────────────────────────");
						System.out.println("\n\n\n\n\n\n\n\n");
						stop(2000);
					}
					return View.HOME;
					}
				}else{
					System.out.println("\n\n\n\n\n");
					System.out.println(" ──────────────────────────────────────────────");
					System.out.println("\t\t   ❌ 주  의❌");
					System.out.println(" ──────────────────────────────────────────────");
					System.out.println();
					System.out.println(" 5~20자의 영문 소문자,숫자와 특수기호(_),(-)만 사용 가능합니다.");
					System.out.println();
					System.out.println("\t\t\t\t   ◀ 확인");
					System.out.println(" ──────────────────────────────────────────────");
					System.out.println("\n\n\n\n\n");
					String check = ScanUtil.nextLine();
					return View.JOIN;
				}
			}else{
				System.out.println("\n\n\n\n\n\n\n\n");
				System.out.println("\t  ─────────────────────────");
				System.out.println("\t  ❌ 해당 아이디는 이미 사용중 입니다!❌");
				System.out.println("\t 　       다른 아이디를 입력해주세요.");
				System.out.println("\t  ─────────────────────────");
				System.out.println("\n\n\n\n\n\n\n\n");
				stop(2000);
				return View.JOIN;
		}
	}
		public void passwordCheck(String userId){ //비밀번호 유효성 검사 메서드
			while(true){
				System.out.println("\n\n\n\n\n\n\n\n\n\n");
				System.out.println(" ┌──────────────────────────────────────┐");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" │\t   회\t 원\t가\t입\t│");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" └──────────────────────────────────────┘");	
				System.out.println("\n");
				System.out.println("　　　아 이 디 : " + userId);
				System.out.println();
				System.out.println("　　　비 밀 번 호 : ");
				System.out.println();
				System.out.println("　　　이 름 :");
				System.out.println();
				System.out.println("　　　닉 네 임 :");
				System.out.println();
				System.out.println("　　　핸 드 폰 번 호 :");
				System.out.println();
				System.out.println(" ───────────────────────────────────────");
				String password = ScanUtil.nextLine();
			String regexpassword = "(?=.*\\d{1,20})(?=.*[~`!@#$%\\^&*()-+=]{1,20})(?=.*[a-zA-Z]{2,20}).{8,20}$";
			Pattern pid = Pattern.compile(regexpassword);
			Matcher mid = pid.matcher(password);
			if(mid.matches()){
				param.put("PASSWORD", password);
				String passwordprivite = "*";
				for(int i = 0; i < password.length()-1; i++){
					passwordprivite += "*";
				}
				System.out.println("\n\n\n\n\n\n\n\n\n\n");
				System.out.println(" ┌──────────────────────────────────────┐");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" │\t   회\t 원\t가\t입\t│");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" └──────────────────────────────────────┘");	
				System.out.println("\n");
				System.out.println("　　　아 이 디 : " + userId);
				System.out.println();
				System.out.println("　　　비 밀 번 호 : " + passwordprivite);
				System.out.println();
				System.out.println("　　　이 름 :");
				System.out.println();
				System.out.println("　　　닉 네 임 :");
				System.out.println();
				System.out.println("　　　핸 드 폰 번 호 :");
				System.out.println();
				System.out.println(" ───────────────────────────────────────");
				String userName = ScanUtil.nextLine();
				param.put("PASSWORD", password);
				System.out.println("\n\n\n\n\n\n\n\n\n\n");
				System.out.println(" ┌──────────────────────────────────────┐");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" │\t   회\t 원\t가\t입\t│");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" └──────────────────────────────────────┘");	
				System.out.println("\n");
				System.out.println("　　　아 이 디 : " + userId);
				System.out.println();
				System.out.println("　　　비 밀 번 호 : " + passwordprivite);
				System.out.println();
				System.out.println("　　　이 름 :" + userName);
				System.out.println();
				System.out.println("　　　닉 네 임 :");
				System.out.println();
				System.out.println("　　　핸 드 폰 번 호 :");
				System.out.println();
				System.out.println(" ───────────────────────────────────────");
				String NickName = ScanUtil.nextLine();
				System.out.println("\n\n\n\n\n\n\n\n\n\n");
				System.out.println(" ┌──────────────────────────────────────┐");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" │\t   회\t 원\t가\t입\t│");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" └──────────────────────────────────────┘");	
				System.out.println("\n");
				System.out.println("　　　아 이 디 : " + userId);
				System.out.println();
				System.out.println("　　　비 밀 번 호 : " + passwordprivite);
				System.out.println();
				System.out.println("　　　이 름 :" + userName);
				System.out.println();
				System.out.println("　　　닉 네 임 :" + NickName);
				System.out.println();
				System.out.println("　　　핸 드 폰 번 호 :");
				System.out.println();
				System.out.println(" ───────────────────────────────────────");
				String hp = ScanUtil.nextLine();
				System.out.println("\n\n\n\n\n\n\n\n\n\n");
				System.out.println(" ┌──────────────────────────────────────┐");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" │\t   회\t 원\t가\t입\t│");
				System.out.println(" │\t    \t  \t \t \t│");
				System.out.println(" └──────────────────────────────────────┘");	
				System.out.println("\n");
				System.out.println("　　　아 이 디 : " + userId);
				System.out.println();
				System.out.println("　　　비 밀 번 호 : " + passwordprivite);
				System.out.println();
				System.out.println("　　　이 름 :" + userName);
				System.out.println();
				System.out.println("　　　닉 네 임 :" + NickName);
				System.out.println();
				System.out.println("　　　핸 드 폰 번 호 :" + hp);
				System.out.println();
				System.out.println(" ───────────────────────────────────────");
				System.out.println(" \t\t\t1.등 록　◀ 취 소");
				System.out.println(" ───────────────────────────────────────");
				String input =  ScanUtil.nextLine();
				if(input.equals("<")){
					paramcheck = true;
					break;
				}else{
				param.put("USERNM", userName);
				param.put("NICKNAME", NickName);
				param.put("HP", hp);
					break;
				}
			}else{
				System.out.println("\n\n\n\n\n\n");
				System.out.println(" ──────────────────────────────────────────");
				System.out.println("\t\t❌ 주  의❌");
				System.out.println(" ──────────────────────────────────────────");
				System.out.println();
				System.out.println(" 영문자는2자이상, 숫자와 특수문자는 각각1개이상 사용하여");
				System.out.println(" 8~20자가 되도록 입력하세요.");
				System.out.println();
				System.out.println("\t\t\t\t◀ 확인");
				System.out.println(" ──────────────────────────────────────────");
				System.out.println("\n\n\n\n\n\n");
				String check = ScanUtil.nextLine(); //수정됨
			}
		}
		}
	
	
	public int login(){ //--수정됨
		System.out.println("\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ┌──────────────────────────────┐");
		System.out.println(" │\t    \t  \t \t│");
		System.out.println(" │\t   로\t 그\t인\t│");
		System.out.println(" │\t    \t  \t \t│");
		System.out.println(" └──────────────────────────────┘");
		System.out.println("\n\n");
		System.out.println("　　　아이디 : ");
		System.out.println("\n\n\n");
		System.out.println("　　　비밀번호: ");
		System.out.println("\n\n");
		System.out.println(" ───────────────────────────────");
		System.out.println(" \t\t\t◀ 취소");
		System.out.println(" ───────────────────────────────");
		String userId = ScanUtil.nextLine();
		if(userId.equals("<")){
			return View.HOME;
		}else{
			System.out.println("\n\n\n\n\n\n\n\n\n\n");
			System.out.println(" ┌──────────────────────────────┐");
			System.out.println(" │\t    \t  \t \t│");
			System.out.println(" │\t   로\t 그\t인\t│");
			System.out.println(" │\t    \t  \t \t│");
			System.out.println(" └──────────────────────────────┘");
			System.out.println("\n\n");
			System.out.println("　　　아이디 : " + userId);
			System.out.println("\n\n\n");
			System.out.println("　　　비밀번호: ");
			System.out.println("\n\n");
			System.out.println(" ───────────────────────────────");
			System.out.println(" \t\t\t◀ 취소");
			System.out.println(" ───────────────────────────────");
			String password = ScanUtil.nextLine();
			if(password.equals("<")){
				return View.HOME;
			} else {
			Map<String, Object> user = userDao.selectUser(userId, password);
			
			if(user == null){
				System.out.println("\n\n\n\n\n\n\n\n");
				System.out.println("\t ─────────────────────────────────");
				System.out.println("\t　　　　  ❌ 로 그 인 실 패!❌");
				System.out.println("\t ─────────────────────────────────");
				System.out.println("\t   　  정지된 회원 또는 탈퇴한 회원이거나");
				System.out.println("\t　아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
				System.out.println("\t ─────────────────────────────────");
				System.out.println("\n\n\n\n\n\n\n\n");
				stop(2000);
				//시간초 걸기
			} else {
				String passwordwatched = "*";
				for(int i = 0; i < password.length()-1; i++){
					passwordwatched += "*";
				}
				System.out.println("\n\n\n\n\n\n\n\n\n\n");
				System.out.println(" ┌──────────────────────────────┐");
				System.out.println(" │\t    \t  \t \t│");
				System.out.println(" │\t   로\t 그\t인\t│");
				System.out.println(" │\t    \t  \t \t│");
				System.out.println(" └──────────────────────────────┘");
				System.out.println("\n\n");
				System.out.println("　　　아이디 : " + userId);
				System.out.println("\n\n\n");
				System.out.println("　　　비밀번호: " + passwordwatched);
				System.out.println("\n\n");
				System.out.println(" ───────────────────────────────");
				System.out.println(" \t\t\t◀ 취소");
				System.out.println(" ───────────────────────────────");
				stop(500);
				System.out.println("\n\n\n\n\n\n\n\n\n");
				System.out.println("\t 　 로　　그　　인　　중...");
				System.out.println("\n\n\n\n\n\n\n\n\n");
				stop(500);
				System.out.println("\n\n\n\n\n\n\n\n\n");
				//시간초 걸기
				Controller.loginUser = user;
				if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){
					return View.ADMIN_MENU;
				}
				return View.NOTICE_POPUP;
				}
			}
		}
		return View.LOGIN;
	}
	
	//마이페이지
		public int myPage(){ //(2.폼수정)
			Map<String, Object> userMypageView = userDao.userMypageView();
			System.out.println("\n\n\n\n\n\n\n\n\n\n");
			System.out.println("┌───────────────────────────────────────┐");
			System.out.println("│\t\t\t\t\t│");
			System.out.println("│　　　　✧⁺마　　이　　플　　래　　닛⁺✧\t│");
			System.out.println("│\t\t\t\t\t│");
			System.out.println("└───────────────────────────────────────┘");
			System.out.printf("\t　　닉네임 : ["+ userMypageView.get("NICKNAME") +"] 평점 : "+ userMypageView.get("GRADE") + "\n");
			System.out.println(" ───────────────────────────────────────");
			System.out.println();
			System.out.println();
			System.out.printf("\t1.  프      로      필      설       정  ");
			System.out.println();
			System.out.println();
			System.out.printf("\t2.  작      성      글      목       록   ", "");
			System.out.println();
			System.out.println();
			System.out.printf("\t3.  관      심      글      목      록  ", "");
			System.out.println();
			System.out.println();
			System.out.printf("\t4.  거      래      이      력  ", "");
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println(" ───────────────────────────────────────");
			System.out.println("\t\t\t\t◀뒤로가기");
			System.out.println(" ───────────────────────────────────────");
			System.out.print(" 번호 입력>");
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
		
		public int profileOption() {//3.폼수정
			Map<String, Object> profileView = userDao.userProfileView();
			System.out.println("┌───────────────────────────────────────┐");
			System.out.println("│\t\t\t\t\t│");
			System.out.println("│　　　　✧⁺프　　로　　필　　조　　회⁺✧\t│");
			System.out.println("│\t\t\t\t\t│");
			System.out.println("└───────────────────────────────────────┘");
			System.out.println();
			System.out.println();
			System.out.println("아이디　　: " + profileView.get("USER_ID"));
			System.out.println();
			System.out.println("이름　　　: " + profileView.get("USERNM"));
			System.out.println();
			System.out.println("닉네임　　: " + profileView.get("NICKNAME"));
			System.out.println();
			System.out.println("핸드폰번호: " + profileView.get("HP"));
			System.out.println();
			System.out.println();
			System.out.println(" ───────────────────────────────────────");
			System.out.println("\t\t\t◀뒤로가기　0.취소");
			System.out.println(" ───────────────────────────────────────");
			System.out.println("1.수정\t2.탈퇴");
			String input = ScanUtil.nextLine();
			switch (input){
			case "1": 
				System.out.println("1.비밀번호 \t2.닉네임\t3.핸드폰번호" );
				System.out.println("수정할 대상 입력>");
				String input2 = ScanUtil.nextLine();
				System.out.println("\n\t\t\t\t<뒤로가기");
				switch (input2) {
				case "1":
					return updatePassword(currentpassword);
				case "2":
					return updateNickname(currentnickname);
				case "3":
					return updateHp(currentHp);	
				case "<":
					return View.PROFILE_OPTION;
				}
				return View.PROFILE_OPTION;
				
			case "2": 
				System.out.println("별별 마켓을 탈퇴하시겠습니까?");
				System.out.println("1.확인\t< 취소");
				String input3 = ScanUtil.nextLine();			
				switch (input3) {
				case "1":
					return deleteUserid();
				case "<":
					return View.PROFILE_OPTION;
				}		
			case "<": return View.MY_PAGE;
			case "0": return View.MAIN_MENU;
			}		
			return View.MY_PAGE;
		}
		//비밀번호 수정	--수정됨(중간에 뒤로가기 필요 )
		private int updatePassword(String currentpassword) {
			System.out.print("현재 비밀번호를 입력>");
			String passwordcheck = ScanUtil.nextLine();
			System.out.println();
			if(passwordcheck.equals(Controller.loginUser.get("PASSWORD"))){
				System.out.print("새 비밀번호 입력>");
				String password = ScanUtil.nextLine();
				int result = userDao.updatePassword(password);
				if(0 < result){
					userDao.updatePassword(password);
					System.out.println("비밀번호 수정 완료");
					Map<String, Object> user = userDao.selectUser(Controller.loginUser.get("USER_ID").toString(), password);
					Controller.loginUser = user;
					return View.PROFILE_OPTION;
				} else {
					System.out.println("비밀번호 수정 실패");
			}
			}
			System.out.println("비밀번호가 일치하지 않아 수정이 불가합니다.");
			return View.PROFILE_OPTION;
		}
		
		//닉네임 수정	
		private int updateNickname(String currentnickname) {  
			System.out.println("수정할 내용 입력>");
			String nickname = ScanUtil.nextLine();
			int result = userDao.updateNickname(nickname);
			if(0 < result){
				System.out.println("닉네임 수정 완료");
				String password = Controller.loginUser.get("PASSWORD").toString();
				Map<String, Object> user = userDao.selectUser(Controller.loginUser.get("USER_ID").toString(), password);
				Controller.loginUser = user;
			} else {
				System.out.println("닉네임 수정 실패");
			}
			return View.PROFILE_OPTION;
		}	
		//핸드폰번호 수정
		private int updateHp (int currenthp) {  
			System.out.println("수정할 내용 입력>");
			String hp = ScanUtil.nextLine();
			int result = userDao.updateHp(hp);
			if(0 < result){
				System.out.println("핸드폰번호 수정 완료");
			} else {
				System.out.println("핸드폰번호 수정 실패");
			}
			return View.PROFILE_OPTION;
		}
		//유저 회원탈퇴
		private int deleteUserid() {
			int result = userDao.deleteUserid();
			if(0 < result){
				System.out.println("회원탈퇴 완료");
				Controller.loginUser = null;
				return View.HOME;
			} else {
				System.out.println("탈퇴 실패");
			}
			return View.MY_PAGE;
		}	
		
	//작성글 목록 조회 
		public int writingList() {//4.폼수정
			List<Map<String, Object>> writingList = userDao.selectWritingList();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println(" ┌──────────────────────────────────────────────────────────────────┐");
			System.out.println(" │　　\t\t　   　✧⁺작　　 　성　　 　글　　 　목　　　 록⁺✧\t\t　   　│");
			System.out.println(" └──────────────────────────────────────────────────────────────────┘");
			System.out.println("　　　번호　　│　　상태　　│　　　　\t제목\t　　　　│　　작성자　　│　　작성일　　　");
			System.out.println("　───────────────────────────────────────────────────────────────────");
			System.out.println();
			for(Map<String, Object> board : writingList){
				System.out.print("　　"+board.get("BOARD_NO") + "\t");
				if(board.get("TAG").equals("Y")) {
					System.out.print("판매중" + "\t");
				} else {
					System.out.print("판매완료" + "\t");
				}
				System.out.println(board.get("TITLE") + "\t"
						    		+ board.get("NICKNAME") + "\t"
						    		+ sdf1.format(board.get("REG_DT")));
			}
			System.out.println();
			System.out.println("　───────────────────────────────────────────────────────────────────");
			System.out.println("\t\t\t\t\t\t◀뒤로가기　　　0.메인메뉴");
			System.out.println("　───────────────────────────────────────────────────────────────────");
			System.out.println("1.조회\t2.삭제");
			String input = ScanUtil.nextLine();
			switch(input){
			case "1":
				System.out.println("조회할 번호를 입력해주세요");
				BoardService.boardno = ScanUtil.nextInt();
				writingCheck = true;
				return View.BOARD_VIEW;
			case "<":
				return View.MY_PAGE;
			case "0":
				return View.MAIN_MENU;
			case "2":
				System.out.println("삭제를 원하시는 게시글 번호를 입력해주세요>");
				boardno = ScanUtil.nextInt();				
				System.out.println("삭제하시겠습니까?");
				System.out.println("1.확인\t<취소");
				input = ScanUtil.nextLine();
				switch (input) {
				case "1":
					return updateWritingList(boardno); //작성글 삭제
//					userDao.updateTag(boardno); 	   //작성글 판매태그변경 판매완료가 아니고 그냥 삭제라면?
				case "<":
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
			System.out.println("\n\n\n\n\n\n\n\n");
			System.out.println(" ┌───────────────────────────────────────────────────────────────────┐");
			System.out.println(" │　　\t\t　   　✧⁺관　　 　심　　 　글　　 　목　　　 록⁺✧\t\t　   　│");
			System.out.println(" └───────────────────────────────────────────────────────────────────┘");
			System.out.println("　　　번호　　│　　게시글 번호　　│　　　　　　　게시글 제목\t　　　　│　　　관심 상품　　　");
			System.out.println("　────────────────────────────────────────────────────────────────────");
			System.out.println();
			for(Map<String, Object> interestrow : interestList){
				System.out.print("　　"+interestrow.get("INTEREST_NO")+"\t");
				System.out.print(interestrow.get("BOARD_NO")+"\t");
				System.out.print(interestrow.get("TITLE")+"\t");
				System.out.print(interestrow.get("GOODS_NAME"));
				System.out.println();
			}
			System.out.println("　────────────────────────────────────────────────────────────────────");
			System.out.println("\t\t\t\t\t\t◀뒤로가기　　　0.메인메뉴");
			System.out.println("　────────────────────────────────────────────────────────────────────");
			System.out.println("1.조회\t2.삭제");
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
					System.out.println("\n\n\n\n\n");
					System.out.println("┌───────────────────────────────────────┐");
					System.out.println("│\t\t\t\t\t│");
					System.out.println("│　　　　✧⁺거　　　래　　　이　　　력⁺✧\t│");
					System.out.println("│\t\t\t\t\t│");
					System.out.println("└───────────────────────────────────────┘");
					System.out.println();
					System.out.println();
					System.out.println();
					System.out.printf("\t1.  구   매   이   력    조    회       ");
					System.out.println();
					System.out.println();
					System.out.printf("\t2.  판   매   이   력    조    회       ");
					System.out.println();
					System.out.println();
					System.out.println();
					System.out.println();
					System.out.println(" ───────────────────────────────────────");
					System.out.println("\t\t\t◀뒤로가기　0.메인메뉴");
					System.out.println(" ───────────────────────────────────────");
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
					//구매이력 조회
				public int buyHistory() {
					System.out.println("\n\n\n\n\n");
					System.out.println(" ┌──────────────────────────────────────────────────────────────────────────────┐");
					System.out.println(" │　　\t\t　   　✧⁺구　　 　 　 매　　 　  　이　　 　 　 력⁺✧\t\t　   　     │");
					System.out.println(" └──────────────────────────────────────────────────────────────────────────────┘");
					System.out.println("　　거래번호　│　　　　구매한물품　　　　│　　구매자　　│　　　　거래일자　　　　│　　리뷰　　　");
					System.out.println("　───────────────────────────────────────────────────────────────────────────────");
					for(Map<String, Object> tradehistory : userDao.buyHistory()){
						Map<String, Object> reviewCheck = userDao.reviewCheck(tradehistory.get("TRADE_NO"));
						System.out.print("　　"+tradehistory.get("TRADE_NO") + "\t\t" + tradehistory.get("GOODS_NAME") + "\t\t");
						System.out.print(tradehistory.get("SELLER_ID") + "\t\t"
								    		+ sdf2.format(tradehistory.get("REG_DT"))+ "　　");
						if (reviewCheck == null){
							System.out.println("리뷰 없음");
						}else {
							System.out.println("리뷰 있음");
						}
					}
					System.out.println("　───────────────────────────────────────────────────────────────────────────────");
					System.out.println("\t\t\t\t\t\t\t\t　　　◀뒤로가기　　　");
					System.out.println("　───────────────────────────────────────────────────────────────────────────────");
					System.out.println("　1.리뷰조회\t\t\t");
					String input = ScanUtil.nextLine();
					switch (input) {
					case "1" :
						System.out.println("조회할 거래번호 입력>");
						int input2 = ScanUtil.nextInt();
						Map<String, Object> reviewCheck = userDao.reviewCheck(input2);
						if (reviewCheck == null){
							System.out.println("작성된 리뷰가 없습니다.");
							return View.BUY_TRADE_HISTORY;
						}else {
							BoardService.tradeno = input2;
							buyHistoryPage = true;
							return View.REVIEW_VIEW;
						}
					case "<": 
						return View.TRADE_HISTORY;
					}
					return View.MY_PAGE;
				}
				//판매이력 조회 
				public int sellHistory() {
					System.out.println("\n\n\n\n\n");
					System.out.println(" ┌──────────────────────────────────────────────────────────────────────────────┐");
					System.out.println(" │　　\t\t　   　✧⁺판　　 　 　 매　　 　  　이　　 　 　 력⁺✧\t\t　   　     │");
					System.out.println(" └──────────────────────────────────────────────────────────────────────────────┘");
					System.out.println("　　거래번호　│　　　　판매한물품　　　　│　　구매자　　│　　　　거래일자　　　　│　　리뷰　　　");
					System.out.println("　───────────────────────────────────────────────────────────────────────────────");
					for(Map<String, Object> tradehistory : userDao.sellHistory()){
						Map<String, Object> reviewCheck = userDao.reviewCheck(tradehistory.get("TRADE_NO"));				
						System.out.print("　　"+tradehistory.get("TRADE_NO") + "\t\t" + tradehistory.get("GOODS_NAME") + "\t\t");
						System.out.print(tradehistory.get("BUYER_ID") + "\t\t"
								    		+ sdf2.format(tradehistory.get("REG_DT")) + "　　");
						if (reviewCheck == null){
							System.out.println("리뷰 없음");
						}else {
							System.out.println("리뷰 있음");
						}
					}
					System.out.println("　───────────────────────────────────────────────────────────────────────────────");
					System.out.println("\t\t\t\t\t\t\t\t　　　◀뒤로가기　　　");
					System.out.println("　───────────────────────────────────────────────────────────────────────────────");
					System.out.println("　1.리뷰조회\t\t\t");
					String input = ScanUtil.nextLine();
					switch (input) {
					case "1" :
						System.out.println("조회할 거래번호 입력>");
						int input2 = ScanUtil.nextInt();
						Map<String, Object> reviewCheck = userDao.reviewCheck(input2);
						if (reviewCheck == null){
							System.out.println("작성된 리뷰가 없습니다.");
							return View.SELL_TRADE_HISTORY;
						}else {
							BoardService.tradeno = input2;
							sellHistoryPage = true;
							return View.REVIEW_VIEW;
						}
						
					case "<": 
						return View.TRADE_HISTORY;
					}
					return View.MY_PAGE;
				}		
	
//=============================================================
				private void stop(int interval){ //private을 붙여줘서 사용자 입장에서 불필요한 정보를 안볼 수 있게 할 수 있다. 
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace(); //밀리second 단위 1000이 1초
					}
				}
	

	
	
	
}


