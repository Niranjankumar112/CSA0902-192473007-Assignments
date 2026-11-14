import java.sql.*;
public class DatabaseInspector {
 public static void main(String[]a)throws Exception{
  try(Connection c=DriverManager.getConnection(System.getenv("CO5_DB_URL"),System.getenv("CO5_DB_USER"),System.getenv("CO5_DB_PASSWORD"))){
   DatabaseMetaData m=c.getMetaData();System.out.println("Database: "+m.getDatabaseProductName()+" "+m.getDatabaseProductVersion());
   System.out.println("Driver: "+m.getDriverName()+" "+m.getDriverVersion());
   try(ResultSet rs=m.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})){
    while(rs.next())System.out.println("Table: "+rs.getString("TABLE_NAME"));
   }
  }
 }
}
