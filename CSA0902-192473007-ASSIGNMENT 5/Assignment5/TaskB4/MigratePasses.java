import java.io.*;import java.nio.file.*;import java.sql.*;import java.util.*;
public class MigratePasses {
 public static void main(String[]a)throws Exception{
  if(a.length!=1){System.out.println("Usage: java MigratePasses <passes.csv>");return;}
  String url=System.getenv("CO5_DB_URL"),u=System.getenv("CO5_DB_USER"),p=System.getenv("CO5_DB_PASSWORD");
  List<String[]> valid=new ArrayList<>();List<String> rejected=new ArrayList<>();int lineNo=0;
  try(BufferedReader r=Files.newBufferedReader(Path.of(a[0]))){String line;while((line=r.readLine())!=null){lineNo++;String[]f=line.split(",",-1);try{
   if(f.length!=7)throw new IllegalArgumentException("expected 7 fields");
   Integer.parseInt(f[3]);Double.parseDouble(f[6]);java.time.LocalDate.parse(f[5]);
   valid.add(new String[]{f[0],f[1],f[2],f[3],f[6]});
  }catch(Exception e){rejected.add("line "+lineNo+": "+e.getMessage());}}}
  try(Connection c=DriverManager.getConnection(url,u,p)){int inserted=PassLoader.loadAll(c,valid);System.out.println("Inserted: "+inserted);}
  System.out.println("Rejected records: "+rejected.size());for(String x:rejected)System.out.println(x);
 }
}
