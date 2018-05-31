1.什么是JDBC？
JDBC是用java编写一套用于不同关系型数据库的一个统一的api。
2.常用接口?
forName("com.mysql.jdbc.Driver")
forName("oracle.jdbc.driver.OracleDriver")
DriverManager.getConnection("jdbc:mysql://localhost:3306/test","username", "password")
DriverManager.getConnection("jdbc:oracle:thin:@localhost:port:database/","username","password")
DriverManager.getConnection("jdbc:microsoft:sqlserver://host:port;DatabaseName=database","username","password")
createStatement()用户给用户创建想数据库发送sql的statement对象,用于发送简单的SQL语句,不带参数
prepareStatement(sql)创建向数据库发送预编译sql的PrepareSatement对象,继承自Statement接口，由preparedStatement创建，用于发送含有一
个或多个参数的SQL语句。PreparedStatement对象比Statement对象的效率更高，并且可以防止SQL注入，所以我们一般都使用PreparedStatement。
prepareCall(sql)创建执行存储过程的callableStatement对象
setAutoCommit(boolean autoCommit)设置事务是否自动提交
commit()在链接上提交事务
rollback()在此链接上回滚事务
execute(sql)运行语句，返回是否有结果集
executeQuery(sql)执行查询
executeUpdate(sql)执行删除更新添加
ps.addBatch()把多条sql语句放到一个批处理中
executeBatch()向数据库发送一批sql语句执行

3.资源关闭的顺序?
关闭资源resultset->statement->connection

4.事物基本概念？
由一条或多条sql语句共同组成的一个数据库执行单元,在一个事物中,所有操作要么全部执行成功,要么全部失败。
5.事物的4大特性?
原子性,一致性,隔离性,持久性
6.日期的转换可以用SimpleDateFormat和Calendar？
前者可以非常方便的将String和Date之间转换,后者则将Date和Calendar非常方便的联系起来,Calendar.getInstance(),c.get(Calendar.YEAR)
7.TEXT文本存储,用于存储大量的文本数据？
TINYTEXT 255个字符2^8-1的TEXT列
TEXT 2^16-1的TEXT列
MEDIUMTEXT 2^24-1的TEXT列
LONGTEXT 2^32-1的TEXT列 4GB
8.BLOB,用于存储大量的二进制数据？
TINYBLOB
BLOB
MEDIUMBLOB
LONGBLOB
9.简单封装、资源文件properties处理连接信息？
Properties properties = new Properties();
properties.load(new FileReader("src/main/resources/log4j.properties"));

