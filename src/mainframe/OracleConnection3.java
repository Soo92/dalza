package mainframe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OracleConnection3 {
    
    private String _driver = "oracle.jdbc.OracleDriver",
    _url = "jdbc:oracle:thin:@192.168.0.64:1521:orcl",
    _user = "scott",
    _password = "tiger";
   Connection con;
   
    public OracleConnection3() {
       try {
         Class.forName(_driver);
         con =
               DriverManager.getConnection(_url,_user,_password);
         System.out.println("연결성공");
      } catch (Exception e) {
         e.printStackTrace();
      }
    }
    
    public void select () {
    	
       try {
           String sql ="select  *  from gameusers2";
         PreparedStatement pstmt=
          con.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();
         System.out.println(" ID \t PASS\t score");
         
         while(rs.next()) {
            String id = rs.getString("id");
            String pass = rs.getString("pass");
            String score= rs.getString("score");
    
            System.out.println(id+"\t"+pass+"\t"+score);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
    }
    
   public static void main(String[] args) {
      OracleConnection3 orcl = 
            new OracleConnection3();
      orcl.select();
   }
}
