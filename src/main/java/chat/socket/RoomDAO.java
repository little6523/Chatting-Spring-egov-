//package chat;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class RoomDAO {
//	Connection conn = null;
//	PreparedStatement psmt = null;
//	ResultSet rs = null;
//	int cnt = 0;
//
//	public void conn() {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			String db_url = "jdbc:mysql://localhost:3305:";
//			String db_id = "hr";
//			String db_pw = "hr";
//			conn = DriverManager.getConnection(db_url, db_id, db_pw);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void close() {
//		try {
//			if (rs != null) {
//				rs.close();
//			}
//			if (psmt != null) {
//				psmt.close();
//			}
//			if (conn != null) {
//				conn.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//    
//	public int insert(id) {
//		conn();
//		String sql = "insert into model1 values(?,?,?)";
//		try{			
//			psmt = conn.prepareStatement(sql);
//			psmt.setString(1,id);
//			psmt.setString(2,pw);
//			psmt.setString(3,name);
//			cnt = psmt.executeUpdate();
//            
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//        	close();
//        }
//   		return cnt;
//    }
//}
package chat.socket;


