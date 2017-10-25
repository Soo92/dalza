package mainframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;
import java.util.Vector;
import javax.naming.spi.DirStateFactory.Result;
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;
import mainframe.DBConnectionMgr;
import mainframe.MemberBean;

//DB와 연동 모든 기능 클래스
public class MemberMgr {

	DBConnectionMgr pool;


	public MemberMgr() {
		
		pool = DBConnectionMgr.getInstance();
	}

	public Vector<MemberBean> getMember() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from gameusers2";
		Vector<MemberBean> vlist = new Vector<MemberBean>();

		try {

			con = pool.getConnection();
			sql = "select  *  from GAMEUSERS2 ";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberBean bean = new MemberBean();
				bean.setId(rs.getString("ID"));
				bean.setPass(rs.getString("PASS"));
				
								vlist.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	
	public MemberBean getMember(String id) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		String sql = "select * from gameusers2";
		boolean flag = false;
		MemberBean bean = new MemberBean();

		try {

			con = pool.getConnection();
			sql = "select * from gameusers2 where idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {

				bean.setId(rs.getString("ID"));
				bean.setPass(rs.getString("PASS"));
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	//중복체크 
	
	
	public boolean checkID(MemberBean bean ) {
		Connection con = null;
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
		
		
		boolean flag = false;
		try {
			// pool에 빌려온다.
			con = pool.getConnection();
			String sql ="select count(*) from gameusers2 where id in(?)";
		
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getId());
			
			rs = pstmt.executeQuery();
			rs.next();
			if(rs.getInt(1) == 1) {
				flag = false;
			}else {
				flag = true;
			}
			} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}

	public boolean checkPass(MemberBean bean ) {
		Connection con = null;
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
		
		
		boolean flag = false;
		try {
			// pool에 빌려온다.
			con = pool.getConnection();
			String sql ="select count(*) from gameusers2 where id in(?) and pass in(?)";
		
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getId());
			pstmt.setString(2, bean.getPass());
			
			rs = pstmt.executeQuery();
			rs.next();
			if(rs.getInt(1) == 1) {
				flag = false;
			}else {
				flag = true;
			}
			} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	// 저장
	public boolean insertMember(MemberBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "select * from gameusers2";
		boolean flag = false;

		try {
			// pool에 빌려온다.
			con = pool.getConnection();
			sql = "insert into GAMEUSERS2(ID,PASS)values(?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getId());// 첫번째 ? 세팅 '홍길동'
			pstmt.setString(2, bean.getPass());
			

			// 실행결과값 : 적용된 레코드 갯수
			int cnt = pstmt.executeUpdate();
			
			if (cnt == 1)
			flag = true;
			System.out.println(cnt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// con은 반납, pstmt는 close
			pool.freeConnection(con, pstmt);
			
		}
		return flag;
	}

}