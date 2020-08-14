package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class BoardDao {
		
		private static BoardDao instance;
		private BoardDao(){}
		public static BoardDao getInstance(){
			if (instance == null){
				instance = new BoardDao();
			}
			return instance;
		}
		
		private JDBCUtil jdbc = JDBCUtil.getInstance();
		
		//채팅방	
	public List<Map<String, Object>> selectChatRoomList() {//채팅방 목록
			String sql = "SELECT CR.ROOM_NO ROOM_NO, B.BOARD_NO, GOODS_NAME, M_REG_DT"
					+ " FROM S_BOARD B, S_CHATROOM CR, (SELECT MAX(REG_DT) M_REG_DT FROM S_CHAT)"
					+ " WHERE B.BOARD_NO = CR.BOARD_NO"
					+ " AND ((CR.SELLER_ID = ? AND CR.SELLER_EXIT = 'Y') OR (CR.BUYER_ID = ? AND CR.BUYER_EXIT = 'Y'))"
					+ " ORDER BY CR.ROOM_NO DESC";
			
			List<Object>param = new ArrayList<>();
			param.add(Controller.loginUser.get("USER_ID"));
			param.add(Controller.loginUser.get("USER_ID"));
			return jdbc.selectList(sql, param);
		}
	public List<Map<String, Object>> selectChat(int room_no) {//선택한 채팅방
		
			String sql = "SELECT U.NICKNAME, C.CONTENT, C.REG_DT, C.READ_CHECK, CR.SELLER_ID, CR.BUYER_ID, B.BOARD_NO"
					+ " FROM S_CHAT C, S_USER U, S_BOARD B, S_CHATROOM CR"
					+ " WHERE C.USER_ID = U.USER_ID AND "
					+ "		  CR.BOARD_NO = B.BOARD_NO AND "
					+ "	      C.ROOM_NO = CR.ROOM_NO AND"	
					+ "		  C.ROOM_NO = ?"
					+ " ORDER BY CHAT_NO";
			List<Object>param = new ArrayList<>();
			param.add(room_no);
			
			
			return jdbc.selectList(sql, param);
		}
	public int ChatUpdate(String input, int room_no) {//채팅내용 입력
		String sql = "INSERT INTO S_CHAT VALUES((SELECT NVL(MAX(CHAT_NO),0)+1 FROM S_CHAT),?,?,SYSDATE,?,?)";	 
		List<Object>param = new ArrayList<>();
		param.add(room_no);
		param.add(input);
		param.add(Controller.loginUser.get("USER_ID"));
		param.add("N");
		
		return jdbc.update(sql, param);
	}
	public void updatechat(int room_no) {//채팅내용을 보러 들어갈때 읽음표시여부 변경
		
		String sql = "UPDATE S_CHAT SET READ_CHECK = ? WHERE USER_ID != ? AND ROOM_NO = ?";
		List<Object>param = new ArrayList<>();
		param.add("Y");
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(room_no);
		jdbc.update(sql, param);
		
	}
	public Map<String, Object> selectChatRoom(int room_no) { // ============ 채팅방 번호와 맞는 게시물의 정보 가져오기
		String sql = "SELECT C.ROOM_NO, C.SELLER_ID, C.BUYER_ID, B.PRICE, B.GOODS_NAME, B.BOARD_NO "
				+ " FROM S_CHATROOM C, S_BOARD B"
				+ " WHERE C.BOARD_NO = B.BOARD_NO AND C.ROOM_NO = ? AND BUYER_ID = ? OR SELLER_ID = ?";
		
		List<Object>param = new ArrayList<>();
		param.add(room_no);
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(Controller.loginUser.get("USER_ID"));
		return jdbc.selectOne(sql, param);
	}
	
	public Map<String, Object> selectChatRoomNo(int boardno) { // ============ 채팅방 번호 확인 
		String sql = "SELECT ROOM_NO"
				+ " FROM S_CHATROOM"
				+ " WHERE BOARD_NO = ? AND BUYER_ID = ?";
		
		List<Object>param = new ArrayList<>();
		param.add(boardno);
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.selectOne(sql, param);
	}
	
	
	public int insertChatRoom(Map<String, Object> p) { // ===============  채팅방 생성
		String sql = "INSERT INTO S_CHATROOM (ROOM_NO, BOARD_NO, SELLER_ID, SELLER_EXIT, BUYER_ID, BUYER_EXIT)"
				+ " VALUES ((SELECT NVL(MAX(ROOM_NO), 0)+1 FROM S_CHATROOM), ?, ?, 'Y', ?, 'Y')";
		List<Object> param = new ArrayList<>();
		param.add(p.get("BOARD_NO"));
		param.add(p.get("SELLER_ID"));
		param.add(p.get("BUYER_ID"));
		
		return jdbc.update(sql, param);
	}
	
	public int DeleteChatRoom(int room_no) {//채팅방 나가기메서드
		String sql = "UPDATE S_CHATROOM SET SELLER_EXIT = DECODE(?, SELLER_ID,'N', SELLER_EXIT), BUYER_EXIT = DECODE(?, BUYER_ID,'N', BUYER_EXIT)"
				+ " WHERE room_no = ?";
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(room_no);
		
		return jdbc.update(sql, param);
		
	}
	
	public void ExitUpdate(int room_no) { //한사람이 나갔는데 다시 말을걸면 둘다 상태가 다시 Y로 업데이트 되어야함. 나갔는데 다시 말 걸었을 때 사용하는 메서드
		String sql = "UPDATE S_CHATROOM SET SELLER_EXIT = 'Y', BUYER_EXIT = 'Y'"
				+ " WHERE ROOM_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(room_no);
		
		jdbc.update(sql, param);
	}
	
//=======================================================
	
	public List<Map<String, Object>> selectBoardList(){ /////////////// 게시판 리스트 띄우는 메소드 
	String sql = "SELECT A.BOARD_NO, A.TAG, A.TITLE, B.NICKNAME, A.REG_DT, A.BLACKLIST"
				+ " FROM S_BOARD A, S_USER B"
				+ " WHERE A.USER_ID = B.USER_ID AND"
				+ " BLACKLIST IN (1,2) AND DELETE_CHECK = 'N'"
				+ " ORDER BY A.BOARD_NO DESC";
		return jdbc.selectList(sql);
	}
	public Map<String, Object> selectBoardView(int boardno) { ///////// 게시글 조회 메소드 
		String sql = "SELECT A.BOARD_NO, A.TAG, A.GOODS_NAME, A.PRICE, A.TITLE, A.CONTENT, B.USER_ID ,B.NICKNAME, A.REG_DT"
				+ " FROM S_BOARD A"
				+ " LEFT OUTER JOIN S_USER B"
				+ " ON A.USER_ID = B.USER_ID"
				+ " WHERE A.BOARD_NO = ? AND DELETE_CHECK = 'N' AND BLACKLIST = 1";
		List<Object> param = new ArrayList<>();
		param.add(boardno);
		
		return jdbc.selectOne(sql, param);
	}
	
	
	public int insertBoard(Map<String, Object> p) {		//////////  게시글 작성 메소드
		String sql = "INSERT INTO S_BOARD (BOARD_NO, TAG, TITLE, CONTENT, PRICE, GOODS_NAME, USER_ID, REG_DT, DELETE_CHECK, BLACKLIST)"
				+ " VALUES ((SELECT NVL(MAX(BOARD_NO), 0)+1 FROM S_BOARD), 'Y', ?, ?, ?, ?, ?, SYSDATE, 'N', 1)";
		List<Object> param = new ArrayList<>();
		param.add(p.get("TITLE"));
		param.add(p.get("CONTENT"));
		param.add(p.get("PRICE"));
		param.add(p.get("GOODS_NAME"));
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.update(sql, param);
	}
	public int deleteBoard(int boardno) { ////////////////// 게시글 삭제 
		String sql = "UPDATE S_BOARD SET DELETE_CHECK = 'Y' WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int updateTitle(int boardno, String title) { /////////////// 게시글 제목 수정 
		String sql = "UPDATE S_BOARD SET TITLE = ?"
				+ "WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(title);
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int updateContent(int boardno, String content) { /////////// 게시글 상세내용 수정 
		String sql = "UPDATE S_BOARD SET CONTENT = ?"
				+ "WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(content);
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int updatePrice(int boardno, int price) { ///////// 게시글 희망가격 수정 
		String sql = "UPDATE S_BOARD SET PRICE = ?"
				+ "WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(price);
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int updateGoodsName(int boardno, String name) { /////////// 게시글 상품이름 수정 
		String sql = "UPDATE S_BOARD SET GOODS_NAME = ?"
				+ "WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(name);
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int insertReportUser(String report, Object reported_id) { // ============= 판매자 신고 등록 
		String sql = "INSERT INTO S_USER_REPORT (REPORT_NO, REPORT_CONTENT, REPORTED_ID, USER_ID, REG_DT, DISPOSITION)"
				+ " VALUES ((SELECT NVL(MAX(REPORT_NO), 0)+1 FROM S_USER_REPORT), ?, ?, ?, SYSDATE, 1)";
		List<Object> param = new ArrayList<>();
		param.add(report);
		param.add(reported_id);
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.update(sql, param);
	}
	public int insertReportBoard(String report, int boardno) { // ============ 게시물 신고 등록 
		String sql = "INSERT INTO S_BOARD_REPORT (REPORT_NO, BOARD_NO, REPORT_CONTENT, USER_ID, REG_DT, DISPOSITION)"
				+ " VALUES ((SELECT NVL(MAX(REPORT_NO), 0)+1 FROM S_BOARD_REPORT), ?, ?, ?, SYSDATE, 1)";
		List<Object> param = new ArrayList<>();
		param.add(boardno);
		param.add(report);
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.update(sql, param);
	}
	
	public List<Map<String, Object>> selectNoticeList() { //////////// 공지사항 리스트 메소드
		String sql = "SELECT NOTICE_NO, TITLE, REG_DT, POPUP_START, POPUP_END"
				+ " FROM S_NOTICE"
				+ " WHERE DELETE_CHECK = 'N'"
				+ " ORDER BY NOTICE_NO DESC";
		return jdbc.selectList(sql);
	}
	public Map<String, Object> selectNoticeView(int boardno) { ///////////// 공지사항 조회 메소드 
		String sql = "SELECT NOTICE_NO, TITLE, CONTENT, REG_DT"
				+ " FROM S_NOTICE"
				+ " WHERE NOTICE_NO = ? AND DELETE_CHECK = 'N'";
		List<Object> param = new ArrayList<>();
		param.add(boardno);
		
		return jdbc.selectOne(sql, param);
	}
	public int insertNotice(Map<String, Object> p) { ////////////////// 공지사항 등록 메소
		String sql = "INSERT INTO S_NOTICE (NOTICE_NO, TITLE, CONTENT, ADMIN_ID, REG_DT, DELETE_CHECK, POPUP_START, POPUP_END)"
				+ " VALUES ((SELECT NVL(MAX(NOTICE_NO), 0)+1 FROM S_NOTICE), ?, ?, ?, SYSDATE, 'N', TO_DATE(?, 'YYYYMMDD HH24:MI'), TO_DATE(?, 'YYYYMMDD HH24:MI'))";
		List<Object> param = new ArrayList<>();
		param.add(p.get("TITLE"));
		param.add(p.get("CONTENT"));
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(p.get("POPUP_START"));
		param.add(p.get("POPUP_END"));
		
		return jdbc.update(sql, param);
	}
	
	public int updateNoticeContent(int boardno, String content) { ///=============== 공지사항 내용 수정 다오 
		String sql = "UPDATE S_NOTICE SET CONTENT = ?"
				+ "WHERE NOTICE_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(content);
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int updateNoticeTitle(int boardno, String title) { // ============== 공지사항 제목 수정 다오 
		String sql = "UPDATE S_NOTICE SET TITLE = ?"
				+ "WHERE NOTICE_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(title);
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int deleteNotice(int boardno) {
		String sql = "UPDATE S_NOTICE SET DELETE_CHECK = 'Y' WHERE NOTICE_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public List<Map<String, Object>> selectPopupList() { // ============== 팝업 띄우기 다오 
		String sql = "SELECT *"
				+ " FROM S_NOTICE"
				+ " WHERE POPUP_START < SYSDATE AND (POPUP_END > SYSDATE OR POPUP_END IS NULL)";
		
		return jdbc.selectList(sql);
	}
	public int updateNoticePopupStart(int boardno, Map<String, Object> p) { //공지사항 팝업 날짜 수정 메서드
		String sql = "UPDATE S_NOTICE SET POPUP_START = TO_DATE(?, 'YYYYMMDD HH24:MI')"
				+ "WHERE NOTICE_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(p.get("POPUP_START"));
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int updateNoticePopupEnd(int boardno, Map<String, Object> p) {//공지사항 팝업 날짜 수정 메서드
		String sql = "UPDATE S_NOTICE SET POPUP_END = TO_DATE(?, 'YYYYMMDD HH24:MI')"
				+ "WHERE NOTICE_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(p.get("POPUP_END"));
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
//===========================================================
	//리뷰
	// 리뷰등록 쿼리 상품이름 파라미터로 받는거 추가 
	public int insertReview(int boardno, int grade, String content) {// 파라미터 3개
			String sql = "INSERT INTO S_REVIEW (TRADE_NO, GRADE, REVIEW_CONTENT, REG_DT)"
					+ " VALUES ((SELECT TRADE_NO FROM S_TRADE_HISTORY WHERE BOARD_NO = ?),"
					+ " ?,?,SYSDATE)";

			List<Object> param = new ArrayList<>();
			param.add(boardno);
			param.add(grade);
			param.add(content);


			return jdbc.update(sql, param);
		}

		//구매이력 연결-내가 작성한 리뷰 리스트 조회  쿼리  --> 판매자 정보조회 뜨는 리뷰 리스트 
	public List<Map<String, Object>> selectReviewList(Object sellerid) {
			String sql = "SELECT B.GOODS_NAME, T.TRADE_NO,T.GRADE ,T.BUYER_ID, T.REG_DT "
					+ "	FROM S_BOARD B, "
					+ " (SELECT R.TRADE_NO, H.BOARD_NO, H.BUYER_ID, R.GRADE, R.REG_DT "
					+ " FROM S_REVIEW R, S_TRADE_HISTORY H"
					+ " WHERE R.TRADE_NO = H.TRADE_NO AND H.SELLER_ID = ?) T"
					+ " WHERE B.BOARD_NO = T.BOARD_NO";
			
			List<Object> param = new ArrayList<>();
			param.add(sellerid);
			
			return jdbc.selectList(sql, param);
		}
		//리뷰 조회 쿼리
	public Map<String, Object> selectReview(int tradeno) {
					String sql = " SELECT B.GOODS_NAME, T.TRADE_NO, T.GRADE ,T.SELLER_ID, T.REG_DT, T.REVIEW_CONTENT , (SELECT NICKNAME FROM S_USER WHERE USER_ID = T.SELLER_ID) NICKNAME"
							+ " FROM S_BOARD B, "
							+ " (SELECT R.TRADE_NO ,H.BOARD_NO , H.SELLER_ID , R.GRADE, R.REG_DT , R.REVIEW_CONTENT "
							+ " FROM S_REVIEW R, S_TRADE_HISTORY H"
							+ " WHERE R.TRADE_NO = ? AND R.TRADE_NO = H.TRADE_NO) T"
							+ " WHERE B.BOARD_NO = T.BOARD_NO";			
					
					List<Object> param = new ArrayList<>();
					param.add(tradeno);
					
					return jdbc.selectOne(sql, param);
				}
		
		//판매 구분 태그 변경 
		public void updateTag(int boardno) { 
					String sql = "UPDATE S_BOARD SET TAG = 'N'"
					+ "WHERE BOARD_NO = ?";
			List<Object> param = new ArrayList<>();
			param.add(boardno);
			
			 jdbc.update(sql, param);
		}

		public int TradeHistoryInsert(String seller_id, String buyer_id, Object boardno) {
			String sql = "INSERT INTO S_TRADE_HISTORY VALUES((SELECT NVL(MAX(TRADE_NO), 0)+1 FROM S_TRADE_HISTORY),?,?,?,SYSDATE)";
			List<Object> param = new ArrayList<>();
			param.add(seller_id);
			param.add(buyer_id);
			param.add(boardno);
			
			return jdbc.update(sql, param);
		}
		
	public Map<String, Object> selectSellerInfo(int boardno) { // ====================== 판매자 정보 조회 
			String sql = "SELECT U.USER_ID, U.NICKNAME, "
					+ " NVL((SELECT AVG(GRADE) FROM S_REVIEW WHERE TRADE_NO IN "
					+ " ((SELECT TRADE_NO FROM S_TRADE_HISTORY WHERE SELLER_ID = (SELECT USER_ID FROM S_BOARD WHERE BOARD_NO = ?)))) ,0) GRADE"
					+ " FROM S_USER U"
					+ " WHERE U.USER_ID = (SELECT USER_ID FROM S_BOARD WHERE BOARD_NO = ?)";
			List<Object> param = new ArrayList<>();
			param.add(boardno);
			param.add(boardno);
			
			return jdbc.selectOne(sql, param);
		}
		
		//==================================================================
		public int insertestInsertForm(int boardno) { //관심목록에 등록하는 쿼리
			String sql = "INSERT INTO S_INTEREST_LIST VALUES((SELECT NVL(MAX(INTEREST_NO), 0)+1 FROM S_INTEREST_LIST),?,?)";
			List<Object> param = new ArrayList<>();
			param.add(Controller.loginUser.get("USER_ID"));
			param.add(boardno);
			
			return jdbc.update(sql, param);
		}
	public Map<String, Object> selectBlackList(int boardno) {
			String sql = "SELECT BLACKLIST, DELETE_CHECK "
					+ " FROM S_BOARD"
					+ " WHERE BOARD_NO = ?";
			List<Object> param = new ArrayList<>();
			param.add(boardno);
			
			return jdbc.selectOne(sql, param);
		}

	
	}
