package util;

import java.io.BufferedReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	//싱글톤 패턴 : 인스턴스의 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴
	 String url = "jdbc:oracle:thin:@localhost:1521:xe";
	 String user = "STARPC";
	 String password = "java";
	 Connection con = null;
	 PreparedStatement ps =null;
	 ResultSet rs = null;
	 
			

	 
	private JDBCUtil(){
		
		 /*
		  * Map<String, Object> selectOne(String sql) //한행만 딱 지정해서 출력할 때, 결과 : row하나
		  * Map<String, Object> selectOne(String sql, List<Object> param) //한행인데 물음표로 값을 받을 때  결과 : row하나
		  * List<Map<String, Object>> selectList(String sql) //WHERE절이 없는 쿼리, 결과 : row 여러개 
		  * List<Map<String, Object>> selectList(String sql, List<Object> param)///WHERE절이 있는 경우  결과 : row 여러개
		  * int update(String sql) //update row하나
		  * int update(String sql, List<Object> param) //update row 여러개
		  */
		 
		 
		 
	}
	//인스턴스를 보관할 변수
	private static JDBCUtil instance;
	
	//인스턴스를 빌려주는 메서드
	public static JDBCUtil getInstance(){
		if(instance == null){ //인스턴스 변수가 null일때만  객체생성, 객체는 무조건 1번만 생성되도록
			instance = new JDBCUtil();
		}
		return instance;
	}
	
	
	
	//1
	public Map<String, Object> selectOne(String sql) {// row하나, ?가 없음, 컬럼명과 컬럼값(해쉬맵)을 리턴, 물음표가 없는 SQL
		
		Map<String, Object> row = new HashMap<>();		
		
		try {			
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSetMetaData md = rs.getMetaData(); // 메타 데이터 : 데이터에 대한 데이터( z

			int columnCount = md.getColumnCount(); // 컬럼의 갯수를 가져옴

			while (rs.next()) {

				for (int i = 1; i <= columnCount; i++) {// 컬럼의 갯수만큼 for문을 돔
					if(md.getColumnTypeName(i) == "CLOB"){
						String clobstring = clobToString(rs.getClob(i));
						String key = md.getColumnLabel(i);
						Object value = clobstring;
						row.put(key, value);
					}else{
					String key = md.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
/*					String key = md.getColumnLabel(i);//컬럼명을 키로 
					Object value = rs.getObject(i);//값을 값으로
					row.put(key, value); //row 해쉬맵에 키와 값을 설정
										//한키에 여러값이 올수 없음.
*/				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 닫을 때 순서는 사용했던 순서의 역순으로
			if(rs != null) try { rs.close(); } catch(Exception e) {};
			if(ps != null) try { ps.close(); } catch(Exception e) {};
			if(con != null) try { con.close(); } catch(Exception e) {};
		}
		return row;
	}
	
	
	
	
	//2
	public Map<String, Object> selectOne(String sql, List<Object> param) {//
		
		Map<String, Object> row = null;
		try {
			
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			for(int i = 0; i < param.size(); i++){//물음표 갯수
				ps.setObject(i+1, param.get(i)); //param list에 있는 값을 컬럼에 저장. ps.setObject(물음표 인덱스, 물음표에 들어갈 값)
			}

			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData(); // 메타 데이터 : 데이터에 대한 데이터

			int columnCount = md.getColumnCount(); // 컬럼의 갯수를 가져옴

			while (rs.next()) {
				row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {// 컬럼의 갯수만큼 for문을 돔
					if(md.getColumnTypeName(i) == "CLOB"){
						String clobstring = clobToString(rs.getClob(i));
						String key = md.getColumnLabel(i);
						Object value = clobstring;
						row.put(key, value);
					}else{
					String key = md.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 닫을 때 순서는 사용했던 순서의 역순으로
			if(rs != null) try { rs.close(); } catch(Exception e) {};
			if(ps != null) try { ps.close(); } catch(Exception e) {};
			if(con != null) try { con.close(); } catch(Exception e) {};
		}
		return row;
		
	}
	
	
	
	
	
	//3
	public List<Map<String, Object>> selectList(String sql) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSetMetaData md = rs.getMetaData(); 
			
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				int columnCount = md.getColumnCount(); 
				for (int i = 1; i <= columnCount; i++) {// 컬럼의 갯수만큼 for문을 돔
					if(md.getColumnTypeName(i) == "CLOB"){
						String clobstring = clobToString(rs.getClob(i));
						String key = md.getColumnLabel(i);
						Object value = clobstring;
						row.put(key, value);
					}else{
					String key = md.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				}
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 닫을 때 순서는 사용했던 순서의 역순으로
			if(rs != null) try { rs.close(); } catch(Exception e) {};
			if(ps != null) try { ps.close(); } catch(Exception e) {};
			if(con != null) try { con.close(); } catch(Exception e) {};
		}
		return list;
	}
	
	
	
	
	
	//4
	public List<Map<String, Object>> selectList(String sql, List<Object> param) {//해당 사람이 뭘 넘겨줄지 몰라서 List타입으로 선언 
		
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			for(int i = 0; i < param.size(); i++){ 
				ps.setObject(i+1, param.get(i)); //param list에 있는 값을 컬럼에 저장. ps.setObject(컬럼, 값); 
			}

			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData(); // 메타 데이터 : 데이터에 대한 데이터( z
			int columnCount = md.getColumnCount(); // 컬럼의 갯수를 가져옴

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {// 컬럼의 갯수만큼 for문을 돔
					if(md.getColumnTypeName(i) == "CLOB"){
						String clobstring = clobToString(rs.getClob(i));
						String key = md.getColumnLabel(i);
						Object value = clobstring;
						row.put(key, value);
					}else{
						String key = md.getColumnLabel(i);
						Object value = rs.getObject(i);
						row.put(key, value);
					}
				}list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 닫을 때 순서는 사용했던 순서의 역순으로
			if(rs != null) try { rs.close(); } catch(Exception e) {};
			if(ps != null) try { ps.close(); } catch(Exception e) {};
			if(con != null) try { con.close(); } catch(Exception e) {};
		}
		return list;
	
		
	}
	
	
	
	
	
	//5
	public int update(String sql){
		int update = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			update = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {// 닫을 때 순서는 사용했던 순서의 역순으로
			if(rs != null) try { rs.close(); } catch(Exception e) {};
			if(ps != null) try { ps.close(); } catch(Exception e) {};
			if(con != null) try { con.close(); } catch(Exception e) {};
		}
		return update;

	}
	
	
	
	
	
	
	//6
	public int update(String sql, List<Object> param){
		int update = 0;
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			for(int i = 0; i < param.size(); i++){
				ps.setObject(i+1, param.get(i)); //param list에 있는 값을 컬럼에 저장.
			}
			update = ps.executeUpdate();
		
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 닫을 때 순서는 사용했던 순서의 역순으로
			if(rs != null) try { rs.close(); } catch(Exception e) {};
			if(ps != null) try { ps.close(); } catch(Exception e) {};
			if(con != null) try { con.close(); } catch(Exception e) {};
		}
		
		return update;
	
		
	}
	
	
		
	public static String clobToString(Clob clob) throws Exception {

		StringBuffer s = new StringBuffer();
		BufferedReader br = new BufferedReader(clob.getCharacterStream());
		String ts = "";
		while((ts = br.readLine()) != null) {
			   s.append(ts + "\n");
		}
		br.close();
		return s.toString();
	}
	
	
	
}

	

	


