import java.sql.*;import java.util.*;
public class BatchBenchmark {
 static Connection connect()throws SQLException{return DriverManager.getConnection(System.getenv("CO5_DB_URL"),System.getenv("CO5_DB_USER"),System.getenv("CO5_DB_PASSWORD"));}
 static List<String[]> data(int n){List<String[]> a=new ArrayList<>();for(int i=1;i<=n;i++)a.add(new String[]{"P"+i,"REG"+i,"Student"+i,String.valueOf(i%25+1),"4500.0"});return a;}
 static void clear(Connection c)throws SQLException{try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");}}
 static long statement(Connection c,List<String[]> rows)throws SQLException{clear(c);long t=System.nanoTime();try(Statement s=c.createStatement()){for(String[]p:rows){String q="INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES('"+p[0]+"','"+p[1]+"','"+p[2]+"',"+p[3]+","+p[4]+")";s.executeUpdate(q);}}return (System.nanoTime()-t)/1_000_000;}
 static long prepared(Connection c,List<String[]> rows)throws SQLException{clear(c);long t=System.nanoTime();try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)")){for(String[]r:rows){p.setString(1,r[0]);p.setString(2,r[1]);p.setString(3,r[2]);p.setInt(4,Integer.parseInt(r[3]));p.setDouble(5,Double.parseDouble(r[4]));p.executeUpdate();}}return (System.nanoTime()-t)/1_000_000;}
 static long batch(Connection c,List<String[]> rows)throws SQLException{clear(c);long t=System.nanoTime();PassLoader.loadAll(c,rows);return (System.nanoTime()-t)/1_000_000;}
 public static void main(String[]a)throws Exception{try(Connection c=connect()){for(int n:new int[]{10,1000,10000,100000}){List<String[]>r=data(n);System.out.println(n+","+statement(c,r)+","+prepared(c,r)+","+batch(c,r));}}}
}
