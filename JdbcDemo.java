package Register;

import org.apache.ibatis.session.TransactionIsolationLevel;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanyage on 2018/5/10.
 */
public class JdbcDemo {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "920725";

    private static Connection conn;

    /**
     * 加载mysql jdbc的驱动,创建连接
     */
    static {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试获取所有
     */
    public void testGetAll() {
        try {
            conn.setAutoCommit(false);
            Statement selectStateMent = conn.createStatement();
            String sql = "SELECT * FROM person";
            ResultSet resultSet = selectStateMent.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                System.out.println(resultSet.getString(2));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取单个记录
     *
     * @param id 主键
     */
    public void testGetById(int id) {
        try {
            conn.setAutoCommit(false);
            String sql = "SELECT * FROM person WHERE id = ?";
            PreparedStatement selectStateMent = conn.prepareStatement(sql);
            selectStateMent.setInt(1, id);
            ResultSet resultSet = selectStateMent.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                System.out.println(resultSet.getString(2));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 插入单条记录
     *
     * @param id   主键
     * @param name 名字
     */
    public void testInsert(int id, String name) {
        try {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO person VALUES (?,?)";
            PreparedStatement insertStatement = conn.prepareStatement(sql);
            insertStatement.setInt(1, id);
            insertStatement.setString(2, name);
            insertStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public void testInsertBatch(Map<Integer,String> params) {
        try {
            boolean autoCommitBefore = conn.getAutoCommit();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO person VALUES (?,?)";
            PreparedStatement insertStatement = conn.prepareStatement(sql);
            for(Map.Entry<Integer,String> entry : params.entrySet()) {
                insertStatement.setInt(1, entry.getKey());
                insertStatement.setString(2, entry.getValue());
                insertStatement.addBatch();
            }
            int[] res = insertStatement.executeBatch();
            conn.commit();
            conn.setAutoCommit(autoCommitBefore);
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
     * 删除单个记录
     *
     * @param id 主键
     */
    public void testDeleteOne(int id) {
        try {
            conn.setAutoCommit(false);
            String sql = "DELETE FROM person WHERE id = ?";
            PreparedStatement deleteStatement = conn.prepareStatement(sql);
            deleteStatement.setInt(1, id);
            int res = deleteStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public void testDeleteBatch() {
        try {
            conn.setAutoCommit(false);
            String sql = "DELETE FROM person WHERE id = ?";
            PreparedStatement deleteStatement = conn.prepareStatement(sql);
            for(int i = 1; i < 100000; i++) {
                deleteStatement.setInt(1, i);
                deleteStatement.addBatch();
            }
            int[] res = deleteStatement.executeBatch();
            System.out.println(res.length);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
     * 更新单个记录
     * @param id
     * @param newName
     */
    public void testUpdateOne(int id, String newName) {
        try {
            conn.setAutoCommit(false);
            String sql = "UPDATE person SET name = ? WHERE id = ?";
            PreparedStatement updateStatement = conn.prepareStatement(sql);
            updateStatement.setString(1,newName);
            updateStatement.setInt(2, id);
            int res = updateStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
    *Jdbc的事物,jdbc的事物分为5种隔离级别,NONE不支持事物,READ_UNCOMMITED允许脏读不可重复读和幻读,READ_COMMITED不允许脏读但是允许不可重复读和幻读
    *REPEATABLE_READ禁止脏读和不可重复读,允许幻读，SERIALIZABLE禁止脏读、不可重复读和幻读
    */
    public void testJdbcTransaction() {
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(TransactionIsolationLevel.READ_UNCOMMITTED.getLevel());
            Statement insertStatement = conn.createStatement();
            insertStatement.execute("INSERT INTO person value(1,'兰亚戈')");
            insertStatement.execute("INSERT INTO person value(2,'舒放')");
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        JdbcDemo jdbcDemo = new JdbcDemo();
        jdbcDemo.testGetAll();
        jdbcDemo.testGetById(4);
        jdbcDemo.testInsert(5,"张三");
        jdbcDemo.testDeleteOne(5);
        jdbcDemo.testUpdateOne(4,"兰亚戈");

        Map<Integer,String> params = new HashMap<>();
        for(int i = 1; i <= 100000; i++) {
            params.put(i,"兰亚戈"+i);
        }
        long start = System.currentTimeMillis();
        jdbcDemo.testInsertBatch(params);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        jdbcDemo.testDeleteBatch();
        jdbcDemo.testJdbcTransaction();
    }
}
