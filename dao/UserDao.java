package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import controller.Controller;

public class UserDao {
		
		//싱글톤 패턴 - 객체를 하나만 만들어서 사용하겠다
		private static UserDao instance;
		private UserDao(){}
		public static UserDao getInstance(){
			if (instance == null){
				instance = new UserDao();
			}
			return instance;
		}
		
		
		private JDBCUtil jdbc = JDBCUtil.getInstance();
		
		public int insertUser(Map<String, Object> p){ /////////////////// 회원가입
			String sql = "INSERT INTO S_USER VALUES (?,?,?,?,?,'N','N',null,null)";
			
			List<Object> param = new ArrayList<>();
			param.add(p.get("USER_ID"));
			param.add(p.get("PASSWORD"));
			param.add(p.get("USERNM"));
			param.add(p.get("NICKNAME"));
			param.add(p.get("HP"));
			
			return jdbc.update(sql, param);
			
		}
		public Map<String, Object> selectUser(String userId, String password) { /////////////// 로그인  ---수정됨(블랙리스트 회원, 탈퇴회원 로그인 안되도록)
			String sql = "SELECT USER_ID, PASSWORD, NICKNAME, ADMIN_CHECK"
					+ " FROM S_USER"
					+ " WHERE USER_ID = ? AND PASSWORD = ? AND DROP_CHECK = 'N' "
					+ " AND ((BLACKLIST_START IS null AND BLACKLIST_END IS null) OR (BLACKLIST_START IS NOT null AND BLACKLIST_END < SYSDATE ))";
			
			List<Object> param = new ArrayList<>();
			param.add(userId);
			param.add(password);
			
						
			return jdbc.selectOne(sql, param);
		}

//유저 프로필 설정 조회 쿼리 
	public Map<String, Object> userProfileView() {  
		String sql = "SELECT USER_ID, PASSWORD, USERNM, NICKNAME, HP"
				+ " FROM S_USER"
				+ " WHERE USER_ID = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));		
					
		return jdbc.selectOne(sql, param);
		
	}
	//유저 비밀번호 수정쿼리
	public int updatePassword(String password) { 
		String sql = "UPDATE S_USER SET PASSWORD = ?"
				+ " WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(password);
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.update(sql, param);
	}	
	//유저 닉네임 수정쿼리
	public int updateNickname(String nickname) { 
		String sql = "UPDATE S_USER SET NICKNAME = ?"
				+ " WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(nickname);
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.update(sql, param);
	}	
	//유저 핸드폰번호 수정 쿼리 
	public int updateHp(String hp) { 
		String sql = "UPDATE S_USER SET HP = ?"
				+ "WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(hp);
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.update(sql, param);
	}
	//유저 회원 탈퇴 쿼리
	public int deleteUserid() {
		String sql = "UPDATE S_USER SET DROP_CHECK = 'Y' WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		
		return jdbc.update(sql, param);
	}
	//유저 닉네임 & 평점 조회 
		public Map<String, Object> userMypageView() {  
			String sql = "SELECT NICKNAME, "
					+ " NVL((SELECT AVG(GRADE) FROM S_REVIEW WHERE TRADE_NO IN"
					+ " ((SELECT TRADE_NO FROM S_TRADE_HISTORY WHERE SELLER_ID = ?))) ,0) GRADE"
					+ " FROM S_USER U"			
					+ " WHERE USER_ID = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(Controller.loginUser.get("USER_ID"));		
			param.add(Controller.loginUser.get("USER_ID"));		
								
			return jdbc.selectOne(sql, param);
			
		}	
	
	
	
	
		
		
		
	
	//작성글 목록 조회 쿼리	
	public List<Map<String, Object>> selectWritingList() { 
		String sql = "SELECT A.BOARD_NO, A.TAG, A.TITLE, B.NICKNAME, A.REG_DT"
				+ " FROM S_BOARD A"
				+ " LEFT OUTER JOIN S_USER B"
				+ " ON A.USER_ID = B.USER_ID"				
				+ " WHERE B.USER_ID = ? AND DELETE_CHECK = 'N' AND BLACKLIST = 1"				
				+ " ORDER BY A.BOARD_NO DESC";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));	
		
		return jdbc.selectList(sql, param);
		
	}
	//작성글 목록조회에서 작성글 삭제하는 쿼리
		public int updateWritingList(int boardno) {
			String sql = "UPDATE S_BOARD SET DELETE_CHECK = 'Y' WHERE BOARD_NO = ?";
			List<Object> param = new ArrayList<>();
			param.add(boardno);
			
			return jdbc.update(sql, param);
		}	
		//작성글 조회 판매 구분 태그 변경 
		public void updateTag(int boardno) { 
			String sql = "UPDATE S_BOARD SET TAG = 'N'"
					+ "WHERE BOARD_NO = ?";
			List<Object> param = new ArrayList<>();
			param.add(boardno);
			
			 jdbc.update(sql, param);
		}	
	
	
	//관심목록 조회 --> 게시글조회에서 관심목록 추가된 글 목록을 조회하는 쿼리 
	public List<Map<String, Object>> interestList() { 
		String sql = "SELECT I.INTEREST_NO, B.BOARD_NO, B.TITLE, B.GOODS_NAME "
				+ " FROM S_INTEREST_LIST I, S_BOARD B"
				+ " WHERE I.BOARD_NO = B.BOARD_NO AND I.USER_ID = ? AND DELETE_CHECK = 'N' AND BLACKLIST = 1"
				+ " ORDER BY INTEREST_NO DESC";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));	
		
		return jdbc.selectList(sql, param);
		
	}
	public int deleteInterest(int input2) {//관심목록에서 삭제하는 쿼리
		String sql = "DELETE S_INTEREST_LIST WHERE INTEREST_NO = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(input2);
		
		return jdbc.update(sql, param);
		
	}
	public Map<String, Object> selectInterest(int interest_no) {//관심목록에서 조회해서 해당게시물 번호 가져오는 쿼리
		String sql = "SELECT BOARD_NO FROM S_INTEREST_LIST WHERE INTEREST_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(interest_no);
		
		return jdbc.selectOne(sql, param);
		
	}
	public Map<String, Object> userIdCheck(String userId) {////회원가입할 때 아이디 중복 확인하는 쿼리
		String sql = "SELECT USER_ID FROM S_USER WHERE USER_ID = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(userId);
		
		return jdbc.selectOne(sql, param);
	}
	
	
	
	
	//거래이력 조회리스트 판매자 구매자 아이디 해당상품  거래일자 (정렬은 거래일자) 거래이력 구매이력 나누기 	
		
	//구매 이력 리스트 조회
		public List<Map<String, Object>> buyHistory() { 
			String sql = "SELECT A.SELLER_ID, A.BUYER_ID, B.GOODS_NAME, A.REG_DT, A.TRADE_NO"
					+ " FROM S_TRADE_HISTORY A, S_BOARD B"
					+ " WHERE A.BOARD_NO = B.BOARD_NO AND A.BUYER_ID = ?"				
					+ " ORDER BY A.REG_DT DESC";
			
			List<Object> param = new ArrayList<>();
			param.add(Controller.loginUser.get("USER_ID"));	
			
			return jdbc.selectList(sql, param);
		}	
		//판매 이력 리스트 조회
	public List<Map<String, Object>> sellHistory() { 
			String sql = "SELECT A.SELLER_ID, A.BUYER_ID, B.GOODS_NAME, A.REG_DT, A.TRADE_NO"
					+ " FROM S_TRADE_HISTORY A"
					+ " LEFT OUTER JOIN S_BOARD B"
					+ " ON A.BOARD_NO = B.BOARD_NO"				
					+ " WHERE A.SELLER_ID = ?"						
					+ " ORDER BY A.REG_DT DESC";
			
			List<Object> param = new ArrayList<>();
			param.add(Controller.loginUser.get("USER_ID"));	
			
			return jdbc.selectList(sql, param);
		}
		public Map<String, Object> reviewCheck(Object tradeno) {
			String sql = " SELECT TRADE_NO"
					+ " FROM S_REVIEW"
					+ " WHERE TRADE_NO = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(tradeno);	
			
			return jdbc.selectOne(sql, param);
		}	
	
	

	
}
	
	//거래 이력 리스트 조회하는거
	
	//거래 이력 게시글 조회 
	


