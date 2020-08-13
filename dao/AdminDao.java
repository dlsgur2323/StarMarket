package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AdminDao {
	private static AdminDao instance;
	private AdminDao(){}
	public static AdminDao getInstance(){
		if(instance == null){
			instance = new AdminDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public List<Map<String, Object>> selectAdminBoardList() {
		String sql = "SELECT A.BOARD_NO, A.TAG, A.TITLE, A.PRICE, A.GOODS_NAME, A.USER_ID, B.NICKNAME, A.REG_DT, A.DELETE_CHECK, A.BLACKLIST"
				+ " FROM S_BOARD A"
				+ " LEFT OUTER JOIN S_USER B"
				+ " ON A.USER_ID = B.USER_ID"
				+ " ORDER BY A.BOARD_NO DESC";
		return jdbc.selectList(sql);
	}
	public int updateBoardTag(int boardno, String tag) {
		String sql = "UPDATE S_BOARD SET TAG = ?"
				+ " WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(tag);
		param.add(boardno);
		
		return jdbc.update(sql, param);
	}
	public int updateBoardTitle(int boardno, String title) {
		String sql = "UPDATE S_BOARD SET TITLE = ?"
				+ " WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(title);
		param.add(boardno);
		
		return jdbc.update(sql, param);	}
	public int updateBoardPrice(int boardno, int price) {
		String sql = "UPDATE S_BOARD SET PRICE = ?"
				+ " WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(price);
		param.add(boardno);
		
		return jdbc.update(sql, param);	}
	public int updateBoardName(int boardno, String name) {
		String sql = "UPDATE S_BOARD SET GOODS_NAME = ?"
				+ " WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(name);
		param.add(boardno);
		
		return jdbc.update(sql, param);	}
	public int updateBoardDelCheck(int boardno, String delCheck) {
		String sql = "UPDATE S_BOARD SET DELETE_CHECK = ?"
				+ " WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(delCheck);
		param.add(boardno);
		
		return jdbc.update(sql, param);	}
	public int updateBoardBlack(int boardno, String black) {
		String sql = "UPDATE S_BOARD SET BLACKLIST = ?"
				+ " WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(black);
		param.add(boardno);
		
		return jdbc.update(sql, param);	}
	public Map<String, Object> selectAdminBoardView(int boardno) { // ============== 게시물 조회 
		String sql = "SELECT A.BOARD_NO, A.TAG, A.GOODS_NAME, A.PRICE, A.TITLE, A.CONTENT, B.USER_ID ,B.NICKNAME, A.REG_DT"
				+ " FROM S_BOARD A"
				+ " LEFT OUTER JOIN S_USER B"
				+ " ON A.USER_ID = B.USER_ID"
				+ " WHERE A.BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(boardno);
		
		return jdbc.selectOne(sql, param);
	}
	public int userDisposition(int reportno, String input, Map<String, Object> p) { // ======== 판매자 신고내역 처분 처리 
		String sql = "UPDATE S_USER_REPORT SET DISPOSITION = ?"
				+ " WHERE USER_ID = (SELECT USER_ID FROM S_USER_REPORT WHERE REPORT_NO = ?)";
		List<Object> param = new ArrayList<>();
		param.add(input);
		param.add(reportno);
		
		jdbc.update(sql, param);
		
		String sql2 = "UPDATE S_USER SET BLACKLIST_START = TO_DATE(?, 'YYYYMMDD HH24:MI') , BLACKLIST_END = TO_DATE(?, 'YYYYMMDD HH24:MI')"
				+ " WHERE USER_ID = (SELECT REPORTED_ID FROM S_USER_REPORT WHERE REPORT_NO = ?)";
		List<Object> param2 = new ArrayList<>();
		param2.add(p.get("BLACKLIST_START"));
		param2.add(p.get("BLACKLIST_END"));
		param2.add(reportno);
		
		return jdbc.update(sql2, param2);
	}
	public List<Map<String, Object>> selectReportUser() { // =============== 판매자 신고 내역 리스트 
		String sql = "SELECT REPORT_NO, REPORT_CONTENT, REPORTED_ID, USER_ID, REG_DT, DISPOSITION"
				+ " FROM S_USER_REPORT";
		return jdbc.selectList(sql);
	}
	public int boardDisposition(int reportno, String input) {// =============== 게시물 신고 처분 처리 
		String sql = "UPDATE S_BOARD_REPORT SET DISPOSITION = ?"
				+ " WHERE BOARD_NO = (SELECT BOARD_NO FROM S_BOARD_REPORT WHERE REPORT_NO= ?)";
		List<Object> param = new ArrayList<>();
		param.add(input);
		param.add(reportno);
		
		jdbc.update(sql, param);
		
		String sql2 = "UPDATE S_BOARD SET BLACKLIST = ?"
				+ " WHERE BOARD_NO = (SELECT BOARD_NO FROM S_BOARD_REPORT WHERE REPORT_NO = ?)";
		List<Object> param2 = new ArrayList<>();
		param2.add(input);
		param2.add(reportno);
		
		return jdbc.update(sql2, param2);
	}
	public List<Map<String, Object>> selectReportBoard() { // =============== 게시물 신고 내역 리스트 
		String sql = "SELECT REPORT_NO, REPORT_CONTENT, BOARD_NO, USER_ID, REG_DT, DISPOSITION"
				+ " FROM S_BOARD_REPORT";
		return jdbc.selectList(sql);
	}
	public List<Map<String, Object>> selectBlackListUser() { // ================ 블랙리스트 회원 리스트 
		String sql = "SELECT USER_ID, NICKNAME, BLACKLIST_START, BLACKLIST_END"
				+ " FROM S_USER"
				+ " WHERE BLACKLIST_START IS NOT NULL"
				+ " ORDER BY BLACKLIST_END";
		return jdbc.selectList(sql);
	}
	public List<Map<String, Object>> selectBlackListBoard() { // ================= 블랙리스트 게스물 리스트 
		String sql = "SELECT BOARD_NO, TITLE, USER_ID, BLACKLIST"
				+ " FROM S_BOARD"
				+ " WHERE BLACKLIST != 1"
				+ " ORDER BY BLACKLIST";
		return jdbc.selectList(sql);
	}
	public int updateUserDisposition(String userid, Map<String, Object> p) { // ================ 블랙리스트 회원 처분 변경 
		String sql = "UPDATE S_USER SET BLACKLIST_START = TO_DATE(?, 'YYYYMMDD HH24:MI'), BLACKLIST_END = TO_DATE(?, 'YYYYMMDD HH24:MI')"
				+ " WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(p.get("BLACKLIST_START"));
		param.add(p.get("BLACKLIST_END"));
		param.add(userid);
		
		return jdbc.update(sql, param);
	}
	
	
}
