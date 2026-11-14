# A3 – Discover the Data-Access Design Pattern (5 marks)
Common steps: obtain connection, prepare SQL, bind parameters, execute, iterate ResultSet, map rows to objects, close ResultSet/statement/connection, handle SQL errors. Differences: table/columns, entity mapping, key type and operation-specific SQL.

Generic contract:
```java
public interface Dao<T,K> {
 int insert(T value) throws SQLException;
 Optional<T> findById(K id) throws SQLException;
 List<T> findAll() throws SQLException;
 boolean update(T value) throws SQLException;
 boolean delete(K id) throws SQLException;
}
```
`StudentDao implements Dao<Student,Integer>` and owns student-table SQL and mapping. `Student(id, registerNo, name, department)` maps to `student(id PRIMARY KEY, register_no, name, department)`. Keep SQL in DAO, business rules in service, and UI out of persistence.

If SQL is copied into GUI/service classes, a column rename must be changed in many locations and tests become harder. DAO improves maintainability; per-operation database time/space complexity is essentially unchanged because the same SQL and rows are processed.
