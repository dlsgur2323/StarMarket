package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.BoardDao;
import dao.UserDao;

public class BoardService {
	
	private static BoardService instance;
	private BoardService(){}
	public static BoardService getInstance(){
		if (instance == null){
			instance = new BoardService();
		}
		return instance;
	}
	private BoardDao boardDao = BoardDao.getInstance();
	
	private int room_no;
	public static int boardno;
	private Object price;
	private String goods_name;
	private String seller_id;
	private String buyer_id;
	public static int tradeno;
	public static boolean check = false;
	SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월dd일");
	SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
	SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMMdd HH:mm");
	SimpleDateFormat sdf6 = new SimpleDateFormat("yyyyMMdd");
	
	//채팅방	
	public int ChatRoomList(){ //채팅방 목록
		
		List<Map<String, Object>> ChatRoomList = boardDao.selectChatRoomList();
		System.out.println("=======================================");
		System.out.println("   채팅방 번호    |   상품 이름      |   마지막 채팅일시");
		System.out.println("---------------------------------------");
		for(Map<String, Object> ChatRoom : ChatRoomList){
			System.out.println(ChatRoom.get("ROOM_NO") + "\t"
					+ChatRoom.get("GOODS_NAME") + "\t"
					+sdf2.format(ChatRoom.get("M_REG_DT")));
		}
		System.out.println("=======================================");
		System.out.println("\t\t\t\t<뒤로가기");
		System.out.println("1.채팅방조회\t2.채팅방나가기");
		System.out.println("---------------------------------------");
		System.out.print("입력>");
		String input = ScanUtil.nextLine();
		if(input.equals("<")){
			return View.MAIN_MENU;
		}else if(input.equals("1")){
			System.out.println("조회할 채팅방 번호를 입력해주세요.");
			room_no = Integer.parseInt(ScanUtil.nextLine());
			check = false;
			return View.CHATROOM_CHAT;
		}
		else if(input.equals("2")){
			System.out.println("나가기(삭제)할 채팅방 번호를 입력해주세요.");
			room_no = Integer.parseInt(ScanUtil.nextLine());
			int deleteChatRoom = boardDao.DeleteChatRoom(room_no);
			String input3 = Controller.loginUser.get("NICKNAME") + "님이 채팅방을 나갔습니다.";
			boardDao.ChatUpdate(input3, room_no);
			if(deleteChatRoom == 1){
				System.out.println("채팅방 나가는 중..(완료)");
				return View.CHATROOM_LIST;
			}
			return View.CHATROOM_LIST;
		}
		return View.CHATROOM_CHAT;
	}
	public int Chat() { //채팅내용
		boardDao.updatechat(room_no); //읽음표시
		
		String Before = null;
		List<Map<String, Object>> ChatList = boardDao.selectChat(room_no); //채팅방에 해당하는 채팅리스트 출력하기
		Map<String, Object> selectChatRoom = boardDao.selectChatRoom(room_no); //채팅방에 해당하는 게시물에가서 상품등 찾아오기
		boardno = Integer.parseInt(selectChatRoom.get("BOARD_NO").toString());
		goods_name = selectChatRoom.get("GOODS_NAME").toString();
		price = selectChatRoom.get("PRICE");
		seller_id = selectChatRoom.get("SELLER_ID").toString();
		buyer_id = selectChatRoom.get("BUYER_ID").toString();
		System.out.println("=======================================");
		for(Map<String, Object> Chat : ChatList){
			if(sdf2.format(Chat.get("REG_DT")).equals(Before)){
			}else{
				System.out.println("\t "+"------------------");
				System.out.println("\t   "+ sdf2.format(Chat.get("REG_DT")));
				System.out.println("\t "+"------------------");
				System.out.println();
			}
			if(Chat.get("NICKNAME").equals(Controller.loginUser.get("NICKNAME"))){
				System.out.println("\t\t" + Chat.get("NICKNAME"));
				System.out.print("\t\t" + Chat.get("CONTENT").toString().replace("\n", "\n\t\t"));
				System.out.println("\t\t" + sdf3.format(Chat.get("REG_DT")));
				Before = sdf2.format(Chat.get("REG_DT"));
				if(Chat.get("READ_CHECK").equals("N")){ 
					System.out.println("\t\t안읽음");
				}else{
					System.out.println();
				}
			}else{
				System.out.println(Chat.get("NICKNAME"));
				System.out.print(Chat.get("CONTENT"));
				System.out.println(sdf3.format(Chat.get("REG_DT")));
				Before = sdf2.format(Chat.get("REG_DT"));
				if(Chat.get("READ_CHECK").equals("N")){ 
					System.out.println("\t\t안읽음");
				}else{
					System.out.println();
				}
			}
		}
		System.out.println("--------------------------------------");
		if(seller_id.equals(Controller.loginUser.get("USER_ID"))){ //판매자만 구매확정요청 뜨게하기
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴 ");
		System.out.println("1.구매확인요청");
		System.out.println("--------------------------------------");
		}else{
			System.out.println("\t\t\t<뒤로가기   0.메인메뉴"); //구매자는 뒤로가기만 나옴.
			System.out.println("--------------------------------------");
		}
		System.out.println("=======================================");
		System.out.print("채팅내용입력 >");
		String input = ScanUtil.nextLine();
		
		
		if(input.equals("<") && check == true){
			return View.BOARD_VIEW;
		}else if(input.equals("<") && check == false){
			return View.CHATROOM_LIST;
		}else if(input.equals("0")){
			return View.MAIN_MENU;
		}else if(input.equals("1") && seller_id.equals(Controller.loginUser.get("USER_ID"))){
			input = "======================================="+"\n"
					+Controller.loginUser.get("NICKNAME")+"님이 구매확정 요청을 하였습니다."+"\n"
					+"-----------------------------------"+"\n"
					+"상품 : " + goods_name+"\n"
					+"희망가격 : " + price+"\n"
					+"-----------------------------------"+"\n"
					+"구매확정을 수락하시려면 YES를 입력해주세요."+"\n"
					+"=======================================";
			int result = boardDao.ChatUpdate(input, room_no);//구매확정요청 내용 채팅창에 자동 입력
		}else if(input.equals("YES") && buyer_id.equals(Controller.loginUser.get("USER_ID"))){
			int history_result = boardDao.TradeHistoryInsert(seller_id, buyer_id, boardno);//거래내역에 인설트
			input = "======================================="+"\n"
					+Controller.loginUser.get("NICKNAME")+"님이 구매확정을 완료했습니다."+"\n"
					+"-----------------------------------"+"\n"
					+"별의 별게 다 있는 별! 별! 마! 켓!을"+"\n"
					+"이용해주셔서 감사합니다.^_^"+"\n"
					+"오늘도 행복한 하루 되세요~"+"\n"
					+"-----------------------------------"+"\n"
					+"=======================================";
			int result = boardDao.ChatUpdate(input, room_no); //구매확정완료 내용 채팅창에 자동 입력
			return View.REVIEW_UPLOAD;
		}
		else{
			int result = boardDao.ChatUpdate(input, room_no);
			boardDao.ExitUpdate(room_no);
			return View.CHATROOM_CHAT;
			
		}

		return View.CHATROOM_CHAT;
	}

	
//===============================================================================
	//게시판

	public int boardList(){ ///////// 게시판 리스트 출력 
		List<Map<String, Object>> boardList = boardDao.selectBoardList();
		System.out.println("=========================================");
		System.out.println("번호\t상태\t제목\t작성자\t작성일");
		System.out.println("-----------------------------------------");
		for(Map<String, Object> board : boardList){
			if(Integer.parseInt(board.get("BLACKLIST").toString()) == 1) {
				System.out.print(board.get("BOARD_NO") + "\t");
			} else if (Integer.parseInt(board.get("BLACKLIST").toString()) == 2) {
				System.out.print("블라인드" + "\t");
			}
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
		System.out.println("\t\t\t\t<뒤로가기");
		System.out.println("1.조회\t2.등록");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			check = false;
			System.out.println("게시글 번호 입력>");
			boardno = ScanUtil.nextInt();
			return View.BOARD_VIEW;
		case "2":
			return View.BOARD_INSERT_FORM;
		case "<": 
			return View.MAIN_MENU;
		}
		return View.BOARD_LIST;
	}
	
	private void insertChatRoom(int boardno, Object seller_id) {
		Map<String, Object> param = new HashMap<>();
		param.put("BOARD_NO", boardno);
		param.put("SELLER_ID", seller_id);
		param.put("BUYER_ID", Controller.loginUser.get("USER_ID"));
		boardDao.insertChatRoom(param);
		
	}
	
	public int boardView() { //////  게시글 조회 
		Map<String, Object> boardView = boardDao.selectBoardView(boardno);
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
		System.out.println("\t\t\t\t<뒤로가기   0.메인메뉴"); //수정, 삭제 권한설정 완료
		System.out.println("1.수정\t2.채팅연결\t3.판매자 정보");
		System.out.println("4.관심등록\t5.신고\t6.삭제");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			if(Controller.loginUser.get("USER_ID").equals(boardView.get("USER_ID"))){
				System.out.println("1.제목\t2.내용\t3.상품이름\t4.희망가격\t0.취소");
				System.out.println("수정할 대상 입력>");
				input = ScanUtil.nextLine();
				switch (input) {
					case "1":
						return updateTitle(boardno);
					case "2":
						return updateContent(boardno);
					case "3":
						return updateGoodsName(boardno);
					case "4":
						return updatePrice(boardno);
					case "0":
						return View.BOARD_VIEW;
				}
			}else{
				System.out.println("해당 게시물의 작성자가 아니라 수정이 불가합니다.");
				return View.BOARD_VIEW;
			}
		case "6":
			if(Controller.loginUser.get("USER_ID").equals(boardView.get("USER_ID"))){
			System.out.println("삭제하시겠습니까?");
			System.out.println("1.확인\t2.취소");
			input = ScanUtil.nextLine();
			switch (input) {
			case "1":
				return deleteBoard(boardno);
			case "2":
				return View.BOARD_VIEW;
				}
			}else{
				System.out.println("해당 게시물의 작성자가 아니라 삭제가 불가합니다.");
				return View.BOARD_VIEW;
			}
		case "<":
			if(check){
				return View.INTEREST_LIST;
			}else if(!check){
				return View.BOARD_LIST;
			}
		case "0":
			return View.MAIN_MENU;
		case "2":
			Map<String, Object> chatRoom = boardDao.selectChatRoomNo(boardno);
			if(chatRoom == null) {
				insertChatRoom(boardno, boardView.get("USER_ID"));
				
				Map<String, Object> chatRoom2 = boardDao.selectChatRoomNo(boardno);
				room_no = Integer.parseInt(chatRoom2.get("ROOM_NO").toString());
				check = true;
				return View.CHATROOM_CHAT;
			} else {
				room_no = Integer.parseInt(chatRoom.get("ROOM_NO").toString());
				check = true;
				return View.CHATROOM_CHAT;
			}
		case "4":
			System.out.println("관심상품으로 등록 하시겠습니까?");
			System.out.println("1.등록\t2.취소");
			input = ScanUtil.nextLine();
			switch (input) {
			case "1":
				return insertestInsert();
			case "2":
				return View.BOARD_VIEW;
		}
		case "5" :
			System.out.println("1.판매자 신고\t2.게시글 신고\t0.취소");
			System.out.println("신고 구분 입력>");
			input = ScanUtil.nextLine();
			switch(input) {
				case "0" :
					return View.BOARD_VIEW;
				case "1" :
					System.out.println("신고할 내용 입력>");
					String report = ScanUtil.nextLine();
					boardDao.insertReportUser(report, boardView.get("USER_ID"));
					return View.BOARD_VIEW;
				case "2" :
					System.out.println("신고할 내용 입력>");
					String report2 = ScanUtil.nextLine();
					boardDao.insertReportBoard(report2, boardno);
					return View.BOARD_VIEW;
			}
		case "3" :
			return View.SELLER_INFO; 
		}
		return View.BOARD_VIEW;
	}
	private int insertestInsert() { //게시글에서 관심목록 등록 메소드
		int insertestInsertForm = boardDao.insertestInsertForm(boardno);
		if(insertestInsertForm == 1){
			System.out.println("관심 등록이 완료되었습니다.");
			return View.BOARD_VIEW;
		}else{
			System.out.println("관심 등록이 실패하였습니다.");
		}
		return View.BOARD_VIEW;
	}
	private Date today = new Date();
	private int updateContent(int boardno) {  /////// 게시글 내용 수정 메소드 
		System.out.println("수정할 내용 입력> (0.취소)");
		String content = ScanUtil.nextLine();
		if(content.equals("0")) {
			return View.BOARD_VIEW;
		} else {
			int result = boardDao.updateContent(boardno, content);
			if(0 < result){
				System.out.println("내용 수정 성공");
			} else {
				System.out.println("내용 수정 실패");
			}
			return View.BOARD_VIEW;
		}
	}
	private int updateTitle(int boardno) { //////////// 게시글 제목 수정 메소드 
		System.out.println("수정할 제목 입력> (0.취소)");
		String title = ScanUtil.nextLine();
		if(title.equals("0")) {
			return View.BOARD_VIEW;
		} else {
			int result = boardDao.updateTitle(boardno, title);
			if(0 < result){
				System.out.println("제목 수정 성공");
			} else {
				System.out.println("제목 수정 실패");
			}
			return View.BOARD_VIEW;
		}
	}
	private int updateGoodsName(int boardno) { //////////// 게시글 제목 수정 메소드 
		System.out.println("수정할 상품이름 입력> (0.취소)");
		String name = ScanUtil.nextLine();
		if(name.equals("0")) {
			return View.BOARD_VIEW;
		} else {
			int result = boardDao.updateGoodsName(boardno, name);
			if(0 < result){
				System.out.println("상품이름 수정 성공");
			} else {
				System.out.println("상품이름 수정 실패");
			}
			return View.BOARD_VIEW;
		}
	}
	private int updatePrice(int boardno) { //////////// 게시글 제목 수정 메소드 
		System.out.println("수정할 희망가격 입력> (0.취소)");
		int price = ScanUtil.nextInt();
		if(price == 0) {
			return View.BOARD_VIEW;
		} else {
			int result = boardDao.updatePrice(boardno, price);
			if(0 < result){
				System.out.println("희망가격 수정 성공");
			} else {
				System.out.println("희망가격 수정 실패");
			}
			return View.BOARD_VIEW;
		}
	}
	private int deleteBoard(int boardno) {   ///////////// 게시글 삭제 메소드 
		int result = boardDao.deleteBoard(boardno);
		if(0 < result){
			System.out.println("삭제 성공");
		} else {
			System.out.println("삭제 실패");
		}
		return View.BOARD_LIST;
	}
	public int boardInsertForm() { /////////////////////////// 게시글 등록 메소드 
		System.out.println("============ 글쓰기 ============");
		System.out.print("제목>");
		String title = ScanUtil.nextLine();
		System.out.print("상품이름>");
		String goods = ScanUtil.nextLine();
		System.out.print("희망가격>");
		String price = ScanUtil.nextLine();
		System.out.print("상세내용>");
		String content = ScanUtil.nextLine();
		
		Map<String, Object> param = new HashMap<>();
		param.put("TITLE", title);
		param.put("GOODS_NAME", goods);
		param.put("PRICE", price);
		param.put("CONTENT", content);
		int result = boardDao.insertBoard(param);
		if(0 < result){
			System.out.println("새 글 등록 성공");
		} else {
			System.out.println("새 글 등록 실패");
		}
		return View.BOARD_LIST;
	}
	
	
	
	//=========================공지사항
	
	public int noticeList() { /////////////// 공지사항 리스트 출력 
		List<Map<String, Object>> noticeList = boardDao.selectNoticeList();
		System.out.println("=========================================");
		System.out.println("번호\t제목\t작성일");
		System.out.println("-----------------------------------------");
		for(Map<String, Object> board : noticeList){
			System.out.print(board.get("NOTICE_NO") + "\t");
			System.out.println(board.get("TITLE") + "\t"
					    		+ sdf1.format(board.get("REG_DT")));
		}
		System.out.println("=========================================");
		if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){//권한설정 완료
		System.out.println("\t\t\t\t<뒤로가기");
		System.out.println("1.조회\t2.등록");
		}else{
			System.out.println("\t\t\t\t<뒤로가기");
			System.out.println("1.조회");
		}
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			System.out.println("게시글 번호 입력>");
			boardno = ScanUtil.nextInt();
			return View.NOTICE_VIEW;
		case "2":
			if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){//권한설정 완료
				return View.NOTICE_INSERT_FORM;
			}else{
				return View.NOTICE_LIST;
			}
			
		case "<": 
			if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){//권한설정 완료
				return View.ADMIN_MENU;
			}else{
				return View.MAIN_MENU;
			}
		}
		return View.NOTICE_LIST;
	}
	public int noticeView() { ////////////////// 공지사항 조회 메소드 
		Map<String, Object> noticeView = boardDao.selectNoticeView(boardno);
		System.out.println("=========================================");
		System.out.println("번호 : " + noticeView.get("NOTICE_NO"));
		System.out.println("제목 : " + noticeView.get("TITLE"));
		System.out.println("작성일자 : " + sdf1.format(noticeView.get("REG_DT")));
		System.out.println("----------------------------------------");
		System.out.println("내용 : " + noticeView.get("CONTENT"));
		System.out.println("=========================================");
		if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){ //권한설정 완료
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		System.out.println("1.수정\t2.삭제");
		}else{
			System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		}
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){//권한설정 완료
				System.out.println("1.제목\t2.내용\t3.팝업설정\t0.취소");
				System.out.println("수정할 대상 입력>");
				input = ScanUtil.nextLine();
				switch (input) {
					case "1":
						return updateNoticeTitle(boardno);
					case "2":
						return updateNoticeContent(boardno);
					case "3":
						return updatePopUp(boardno);
					case "0":
						return View.NOTICE_VIEW;
				}
			}else{
				return View.NOTICE_VIEW;
			}
		case "2":
			if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){//권한설정 완료
			System.out.println("삭제하시겠습니까?");
			System.out.println("1.확인\t2.취소");
			input = ScanUtil.nextLine();
			switch (input) {
				case "1":
					return deleteNotice(boardno);
				case "2":
					return View.NOTICE_VIEW;
				}
			}else{
				return View.NOTICE_VIEW;
			}
		case "<":
			return View.NOTICE_LIST;
		case "0":
			if(Controller.loginUser.get("ADMIN_CHECK").equals("Y")){
				return View.ADMIN_MENU;
			}else{
				return View.MAIN_MENU;
		}
		}
		return View.NOTICE_VIEW;
	}
	private int deleteNotice(int boardno) {//공지사항 삭제 메서드
		int result = boardDao.deleteNotice(boardno);
		if(0 < result){
			System.out.println("삭제 성공");
		} else {
			System.out.println("삭제 실패");
		}
		return View.NOTICE_LIST;
	}
	private int updatePopUp(int boardno) { //===================== 공지사항 팝업날짜 수정 =============
		System.out.println("수정할 날짜 (1.팝업시작 일시\t2.팝업종료 일시");
		String input = ScanUtil.nextLine();
		switch(input) {
			case "1" :
				System.out.println("팝업 시작 날짜> 1.현재날짜\n (예시 20200811) 입력>");
				String startDate = ScanUtil.nextLine();
				System.out.println("팝업 시작 시각> 1.현재시각\n (예시 17:35) 입력>");
				String startTime = ScanUtil.nextLine();
				
				String start = startDate + " " + startTime;
				
				if(startDate.equals("1") && startTime.equals("1")){ // ======= 현재날짜 현재시각 
					start = sdf5.format(today);
				} else if (startDate.equals("1")) { // ============ 현재날짜 특정시간 
					start = sdf6.format(today) + " " + startTime;
				} else if (startTime.equals("1")) { // =========== 특정날짜 현재시각 
					start = startDate + " " + sdf3.format(today);
				} else {
					start = startDate + " " + startTime;
				}
				
				Map<String, Object> param = new HashMap<>();
				param.put("POPUP_START", start);
				int result = boardDao.updateNoticePopupStart(boardno, param);
				if(0 < result){
					System.out.println("수정 성공");
				} else {
					System.out.println("수정 실패");
				}
				
				break;
				
			case "2" :
				System.out.println("팝업 종료 날짜> 1.현재날짜\t2.미정\n (예시 20200811) 입력>");
				String endDate = ScanUtil.nextLine();
				String endTime = null;
				if(endDate.equals("2")) {
					
				} else {
					System.out.println("팝업 종료 시각> \n (예시 17:35) 입력>");
					endTime = ScanUtil.nextLine();
				}
				
				String end = endDate + " " + endTime;
				
				if(endDate.equals("1") && endTime.equals("1")){ // ======= 현재날짜 현재시각 
					end = sdf5.format(today);
				} else if (endDate.equals("1")) { // ============ 현재날짜 특정시간 
					end = sdf6.format(today) + " " + endTime;
				} else if(endDate.equals("2")) {
					end = null;
				} else {
					end = endDate + " " + endTime;
				}
				
				Map<String, Object> param2 = new HashMap<>();
				param2.put("POPUP_END", end);
				int result2 = boardDao.updateNoticePopupEnd(boardno, param2);
				if(0 < result2){
					System.out.println("수정 성공");
				} else {
					System.out.println("수정 실패");
				}
				
				break;
			 
		}
		return View.NOTICE_VIEW;
	}
	private int updateNoticeTitle(int boardno) { // ===================== 공지사항 제목 수정 ===============
		System.out.println("수정할 제목 입력>(0.취소)");
		String title = ScanUtil.nextLine();
		if(title.equals("0")) {
			return View.NOTICE_VIEW;
		} else {
			int result = boardDao.updateNoticeTitle(boardno, title);
			if(0 < result){
				System.out.println("제목 수정 성공");
			} else {
				System.out.println("제목 수정 실패");
			}
			return View.NOTICE_VIEW;
		}
	}
	private int updateNoticeContent(int boardno) { // ================== 공지사항 내용 수정 ==============
		System.out.println("수정할 내용 입력> (0.취소)");
		String content = ScanUtil.nextLine();
		if(content.equals("0")) {
			return View.NOTICE_VIEW;
		} else {
			int result = boardDao.updateNoticeContent(boardno, content);
			if(0 < result){
				System.out.println("내용 수정 성공");
			} else {
				System.out.println("내용 수정 실패");
			}
			return View.NOTICE_VIEW;
		}
	}
	public int noticeInsertForm(){ //////////////// 공지사항 등록 메소드 
		System.out.println("============ 글쓰기 ============");
		System.out.print("제목>");
		String title = ScanUtil.nextLine();
		System.out.print("상세내용>");
		String content = ScanUtil.nextLine();
		System.out.print("팝업으로 설정하시겠습니까?> y/n");
		String popup = ScanUtil.nextLine();
		if(popup.equals("y")) {
			System.out.println("팝업 시작 날짜> 1.현재날짜\n (예시 20200811) 입력>");
			String startDate = ScanUtil.nextLine();
			System.out.println("팝업 시작 시각> 1.현재시각\n (예시 17:35) 입력>");
			String startTime = ScanUtil.nextLine();
			System.out.println("팝업 종료 날짜> 1.현재날짜\n (예시 20200811) 입력>");
			String endDate = ScanUtil.nextLine();
			System.out.println("팝업 종료 시각> \n (예시 17:35) 입력>");
			String endTime = ScanUtil.nextLine();

			String start = startDate + " " + startTime;
			String end = startDate + " " + startTime;
			
			if(startDate.equals("1") && startTime.equals("1")){ // ======= 현재날짜 현재시각 
				start = sdf5.format(today);
			} else if (startDate.equals("1")) { // ============ 현재날짜 특정시간 
				start = sdf6.format(today) + " " + startTime;
			} else if (startTime.equals("1")) { // =========== 특정날짜 현재시각 
				start = startDate + " " + sdf3.format(today);
			} else {
				start = startDate + " " + startTime;
			}
			
			if(endDate.equals("1") && endTime.equals("1")){ // ======= 현재날짜 현재시각 
				end = sdf5.format(today);
			} else if (endDate.equals("1")) { // ============ 현재날짜 특정시간 
				end = sdf6.format(today) + " " + endTime;
			} else {
				end = endDate + " " + endTime;
			}
			
			Map<String, Object> param = new HashMap<>();
			param.put("TITLE", title);
			param.put("CONTENT", content);
			param.put("POPUP_START", start);
			param.put("POPUP_END", end);
			int result = boardDao.insertNotice(param);
			if(0 < result){
				System.out.println("새 글 등록 성공");
			} else {
				System.out.println("새 글 등록 실패");
			}
			
			
		} else if (popup.equals("n")){
			Map<String, Object> param = new HashMap<>();
			param.put("TITLE", title);
			param.put("CONTENT", content);
			int result = boardDao.insertNotice(param);
			if(0 < result){
				System.out.println("새 글 등록 성공");
			} else {
				System.out.println("새 글 등록 실패");
			}
		}
		return View.NOTICE_LIST;
	}
	public int noticePopup() { // ======================= 공지사항 팝업으로 띄우기 ================== //
		List<Map<String, Object>> popupList = boardDao.selectPopupList();
		if(popupList == null) {
			
		} else {
			for(Map<String, Object> popup : popupList) {
				System.out.println("=========================================");
				System.out.println("번호 : " + popup.get("NOTICE_NO"));
				System.out.println("제목 : " + popup.get("TITLE"));
				System.out.println("작성일자 : " + sdf1.format(popup.get("REG_DT")));
				System.out.println("----------------------------------------");
				System.out.println("내용 : " + popup.get("CONTENT"));
				System.out.println("=========================================");
				System.out.println("> 다음");
				String input = ScanUtil.nextLine();
			}
		}
		return View.MAIN_MENU;
	}
	
	
	
	public int sellerInfo() { // 판매자 정보 조회 
		Map<String, Object> sellerInfo = boardDao.selectSellerInfo(boardno);
		System.out.println("=========================================");
		System.out.println("\\t\\t판매자 정보");
		System.out.println("-----------------------------------------");
		System.out.println("\t판매자 : " + sellerInfo.get("NICKNAME") + "\t" + "평점 : " + sellerInfo.get("GRADE") );
		
		List<Map<String, Object>> reviewList = boardDao.selectReviewList(sellerInfo.get("USER_ID"));
		System.out.println("―――――――――――――――――＊REVIEW＊――――――――――――――――");
		System.out.println("리뷰번호\t상품이름\t별점\t작성자\t작성일");
		System.out.println("――――――――――――――――――――――――――――――――――――――――――");
		for(Map<String, Object> review : reviewList){
			System.out.println(review.get("TRADE_NO") + "\t"
					+ review.get("GOODS_NAME") + "\t" 
					+ review.get("GRADE") + "\t" 
					+ review.get("BUYER_ID") + "\t" 
					
					+ review.get("REG_DT"));
		}
		System.out.println("=========================================");
		System.out.println("\t\t\t<뒤로가기   0.메인메뉴");
		System.out.println("1.리뷰조회");
		System.out.println("입력>>");
		String input = ScanUtil.nextLine();
		switch (input) {
		case "1":
			System.out.println("리뷰 번호 입력>>");
			tradeno = ScanUtil.nextInt();
			return View.REVIEW_VIEW; //리뷰번호에 해당하는 리뷰로 이동
		case "<": 
			return View.BOARD_VIEW;
		case "0":
			return View.MAIN_MENU;
		}
		return View.SELLER_INFO;
		
	}
	

//============================================================================
	//리뷰
		//리뷰 작성 폼 	
	private int currentReviewNo;
	
	public int reviewUpload(){ //상품이름 파라미터로
		System.out.println("―――――――――――――――――＊REVIEW＊――――――――――――――――");
		System.out.println("판매자 아이디 :" + seller_id);
		System.out.println("――――――――――――――――――――――――――――――――――――――――――");
		System.out.printf("[%s] 구매후기", goods_name/*파라미터로 받은 상품이름*/ );
		System.out.print("★별점을 입력해주세요★ >>");
		int grade = ScanUtil.nextInt();
		if (grade == 1){
			System.out.println("★");
		}else if (grade ==2){
			System.out.println("★★");
		}else if (grade ==3){
			System.out.println("★★★");
		}else if (grade ==4){
			System.out.println("★★★★");
		}else{
			System.out.println("★★★★★");
		}
		System.out.println("리뷰내용을 입력해주세요. >>");
		System.out.println("―――――――――――――――――――――＊――――――――――――――――――――");
		System.out.println();
		String content = ScanUtil.nextLine();
		System.out.println();
		System.out.println("―――――――――――――――――――――＊――――――――――――――――――――");
		System.out.println("1.등록\t2.취소");
		int input = ScanUtil.nextInt();
		if (input == 1){
			int result = boardDao.insertReview(boardno, grade, content);
			if (0 < result){
				System.out.println("리뷰가 등록되었습니다.");
				boardDao.updateTag(boardno);
			}else{
				System.out.println("리뷰 등록 실패.");
			}
		}else{
			System.out.println("리뷰 등록 취소");
		}
		return View.CHATROOM_CHAT;
	}
	
	//리뷰 리스트 조회 

	
	
	//리뷰 조회(수정/삭제 가능하게) 나중에 페이지구분 처리 
	public int reviewView() {
		Map<String, Object> review = boardDao.selectReview(tradeno);
		System.out.println("거래번호\t:" + review.get("TRADE_NO"));
		System.out.println("―――――――――――――――――＊REVIEW＊――――――――――――――――");
		System.out.println("판매자 닉네임 : " + "[" + review.get("NICKNAME") + "]");
		System.out.println("작성일\t : " + review.get("REG_DT"));
		System.out.println("――――――――――――――――――――――――――――――――――――――――――");
		System.out.println("상품이름\t : " + "[" + review.get("GOODS_NAME") + "]" );
		System.out.println("별점\t : " + review.get("GRADE")); //review.get("GRADE") 을 별로 바꾸고싶음 	
		System.out.println("내용\t : " + review.get("REVIEW_CONTENT")); 
		System.out.println("――――――――――――――――――――――――――――――――――――――――――");
		System.out.println("\t\t\t\t\t<뒤로가기");
		
		String input = ScanUtil.nextLine();
		
		switch (input){//
		case "<": 
			//return View.REVIEW_LIST;
		}
		return View.REVIEW_VIEW;
	}
	
	
	

//======================================================	
	
	
	
		
}


	
