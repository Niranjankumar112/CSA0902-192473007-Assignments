import java.sql.*;
public class UnsafeLoginDemo {
    public static boolean unsafe(Connection c, String id, String pwd) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE id='" + id + "' AND pwd='" + pwd + "'";
        try (Statement st=c.createStatement(); ResultSet rs=st.executeQuery(sql)) { rs.next(); return rs.getInt(1)>0; }
    }
    public static boolean safe(Connection c, String id, String pwd) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE id=? AND pwd=?";
        try (PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,id); ps.setString(2,pwd);
            try(ResultSet rs=ps.executeQuery()){rs.next();return rs.getInt(1)>0;}
        }
    }
    public static void main(String[] args) throws Exception {
        String url=System.getenv("CO5_DB_URL"), user=System.getenv("CO5_DB_USER"), pass=System.getenv("CO5_DB_PASSWORD");
        if(url==null||user==null||pass==null){System.out.println("Set CO5_DB_URL, CO5_DB_USER and CO5_DB_PASSWORD.");return;}
        try(Connection c=DriverManager.getConnection(url,user,pass)){
            String id=args.length>0?args[0]:"' OR '1'='1' -- ";
            String pwd=args.length>1?args[1]:"wrong";
            System.out.println("Unsafe accepts: "+unsafe(c,id,pwd));
            System.out.println("PreparedStatement accepts: "+safe(c,id,pwd));
        }
    }
}
