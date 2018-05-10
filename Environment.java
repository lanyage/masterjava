package Register;

/**
 * Created by lanyage on 2018/5/10.
 */

import java.lang.reflect.InvocationTargetException;

/**
 * 事物的抽象接口
 */
interface Transaction {
    String getConnection();

    String getDataSource();

    String getTransactionIsolationLevel();

    boolean isAutoCommit();
}

/**
 *jdbc事物,包括数据库连接,数据源,事物隔离级别和自动commit
 */
class JdbcTransaction implements Transaction {
    private String connection;
    private String dataSource;
    /**
     * NONE
     * READ_COMMITED
     * READ_UNCOMMITED
     * REPEATABLE_READ
     * SERIALIZABLE
     */
    private String transactionIsolationLevel;
    private boolean autoCommit;

    public JdbcTransaction(String connection) {
        this.connection = connection;
    }

    public JdbcTransaction(String dataSource, String transactionIsolationLevel, boolean autoCommit) {
        this.dataSource = dataSource;
        this.transactionIsolationLevel = transactionIsolationLevel;
        this.autoCommit = autoCommit;
    }

    public String getConnection() {
        return connection;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getTransactionIsolationLevel() {
        return transactionIsolationLevel;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }
}

/**
 * 事物工厂,用于创建事物对象
 */
class TransactionFactory {
    public Transaction newTransaction(String connection) {
        return new JdbcTransaction(connection);
    }

    public Transaction newTransaction(String dataSource, String transactionIsolationLevel, boolean autoCommit) {
        return new JdbcTransaction(dataSource, transactionIsolationLevel, autoCommit);
    }
}

/**
 * 该类一般是封装配置文件里面的<code>'<environment>'标签,里面会封装环境id('dev'/'prod')、数据源和事物工厂</code>
 */
class Environment {
    private String id;
    private String dataSource;
    private TransactionFactory transactionFactory;

    private Environment(String id, String dataSource, TransactionFactory transactionFactory) {
        if (id == null || id.equals("") || id.trim().equals("")) {
            throw new RuntimeException("Environment's id mustn't not be null", new Exception());
        } else if (dataSource == null || dataSource.equals("") || dataSource.trim().equals("")) {
            throw new RuntimeException("Environment's dataSource mustn't not be null", new Exception());
        } else {
            this.id = id;
            if (transactionFactory == null) {
                throw new RuntimeException("Environment's transactionFactory mustn't not be null", new Exception());
            } else {
                this.dataSource = dataSource;
                this.transactionFactory = transactionFactory;
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getDataSource() {
        return dataSource;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "id='" + id + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", transactionFactory='" + transactionFactory + '\'' +
                '}';
    }

    public static class Builder {
        private String id;
        private String dataSource;
        private TransactionFactory transactionFactory;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            this.transactionFactory = transactionFactory;
            return this;
        }
        public Environment build() {
            return new Environment(this.id, this.dataSource, this.transactionFactory);
        }
    }
}

/**
 * 舞台类
 */
class Stage {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        //首先获得事物工厂
        TransactionFactory transactionFactory = new TransactionFactory();
        //通过建造者模式来构建运行环境类对象
        Environment environment =  new Environment.Builder().id("environment&proxy_1").dataSource("外太空").transactionFactory(transactionFactory).build();
        System.out.println(environment.getId());
        System.out.println(environment.getDataSource());
        System.out.println(environment.getTransactionFactory());
    }
}
