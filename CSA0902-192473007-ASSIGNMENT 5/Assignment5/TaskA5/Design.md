# A5 – Design for Correctness and Safety (4 marks)

## Transfer transaction pseudocode
```
ALGORITHM TransferWallet(sourceId, destinationId, amount)
BEGIN
 OPEN JDBC CONNECTION
 SET autoCommit TO FALSE
 TRY
  UPDATE source wallet SET balance = balance - amount
   WHERE id = sourceId AND balance >= amount
  IF affected rows is not 1 THEN RAISE insufficient funds
  UPDATE destination wallet SET balance = balance + amount
   WHERE id = destinationId
  IF affected rows is not 1 THEN RAISE destination missing
  COMMIT
 CATCH any error
  ROLLBACK
  RAISE error
 FINALLY
  RESTORE autoCommit and CLOSE connection
 END TRY
END
```
Validate positive amount and use row-count checks. Use appropriate isolation/locking to prevent concurrent overdrafts.

## Injection
String concatenation allows input to alter SQL syntax. A crafted ID such as `' OR '1'='1' -- ` may make the WHERE predicate true if inserted directly into the quoted SQL. Use `SELECT ... WHERE id=? AND pwd=?`, bind both values with `PreparedStatement`, and store password hashes rather than plaintext passwords. Escaping/regex filtering is weaker because SQL syntax/encoding edge cases are difficult to cover; PreparedStatement separates data from SQL structure. A transaction adds coordination/logging overhead but protects atomicity; separate auto-commit updates can debit one wallet and fail before crediting the other, losing money.
