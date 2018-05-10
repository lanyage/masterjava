package Register;

/**
 * Created by lanyage on 2018/5/10.
 * 建造者模式,类的可变性几乎为0,然后内部类Builder来事先构造属性,然后最后通过builder的build方法来构建环境对象
 */
public class Environment {
    private String id;
    private String dataSource;
    private String transactionFactory;

    private Environment(String id, String dataSource, String transactionFactory) {
        if (id == null || id.equals("") || id.trim().equals("")) {
            throw new RuntimeException("Environment's id mustn't not be null", new Exception());
        } else if (dataSource == null || dataSource.equals("") || dataSource.trim().equals("")) {
            throw new RuntimeException("Environment's dataSource mustn't not be null", new Exception());
        } else {
            this.id = id;
            if (transactionFactory == null || transactionFactory.equals("") || transactionFactory.trim().equals("")) {
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

    public String getTransactionFactory() {
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
        private String transactionFactory;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder transactionFactory(String transactionFactory) {
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Environment build() {
            return new Environment(this.id, this.dataSource, this.transactionFactory);
        }
    }
}

class Main {
    public static void main(String[] args) {
        Environment.Builder environmentBuilder = new Environment.Builder();
        Environment environment = environmentBuilder.id("environmentProxy&1")
                .dataSource("comboPooledDataSource")
                .transactionFactory("transactionFactoryBean")
                .build();
        System.out.println(environment);
        System.out.println(environment.getId());
        System.out.println(environment.getDataSource());
        System.out.println(environment.getTransactionFactory());
    }
}
