import java.sql.*;
import java.util.List;

public class PassLoader {
    /** Loads all passes in one transaction, batches of 1,000. */
    public static int loadAll(Connection con, List<String[]> passes) throws SQLException {
        String sql="INSERT INTO bus_pass(pass_id, reg_no, name, route_no, amount) VALUES (?, ?, ?, ?, ?)";
        boolean oldAutoCommit=con.getAutoCommit();
        con.setAutoCommit(false);
        try(PreparedStatement ps=con.prepareStatement(sql)){
            int n=0;
            for(String[] p:passes){
                // TODO 1 completed: bind fields and execute batches of 1,000.
                ps.setString(1,p[0]); ps.setString(2,p[1]); ps.setString(3,p[2]);
                ps.setInt(4,Integer.parseInt(p[3])); ps.setDouble(5,Double.parseDouble(p[4]));
                ps.addBatch(); n++;
                if(n%1000==0) ps.executeBatch();
            }
            ps.executeBatch();
            // TODO 2 completed: commit and return count.
            con.commit();
            return n;
        } catch(SQLException | RuntimeException e){
            // TODO 3 completed: roll back entire transaction and rethrow.
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }
}
