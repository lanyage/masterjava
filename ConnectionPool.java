package connectionpool;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lanyage on 2018/5/29.
 */
class Connection {
    private int index;

    public Connection(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public void preparedStatement() {

    }

    public void commit() {
        System.out.println("Connection" + getIndex() + " commit!");
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ConnectionPool {
    /**
     * connectionPool是线程共享的
     */
    private LinkedList<Connection> connectionPool = new LinkedList<Connection>();

    /**
     * 初始化数据库连接池
     */
    public ConnectionPool(int size) {
        for (int i = 0; i < size; i++) {
            connectionPool.add(new Connection(i + 1));
        }
    }

    public void release(Connection connection) {
        if (connection != null) {
            synchronized (connectionPool) {
                connectionPool.addLast(connection);
                connectionPool.notifyAll();
            }
        }
    }

    public Connection fetch(long mills) {
        synchronized (connectionPool) {
            long future = System.currentTimeMillis() + mills;
            long remain = mills;
            /** 如果当前连接池是空的并且自己还有时间,就等待 */
            while (connectionPool.isEmpty() && remain > 0) {
                try {
                    connectionPool.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Connection connection = null;
            if (!connectionPool.isEmpty()) {
                connection = connectionPool.pollFirst();
            }
            return connection;
        }
    }

    public int size() {
        synchronized (connectionPool) {
            return connectionPool.size();
        }
    }
}

class UserSession implements Runnable {
    private ConnectionPool connectionPool;
    private AtomicInteger got;
    private AtomicInteger notGot;
    private CountDownLatch countDownLatch;
    private CountDownLatch beforeEnd;
    public UserSession(ConnectionPool connectionPool, AtomicInteger got, AtomicInteger notGot, CountDownLatch countDownLatch,CountDownLatch beforeEnd) {
        this.connectionPool = connectionPool;
        this.got = got;
        this.notGot = notGot;
        this.countDownLatch = countDownLatch;
        this.beforeEnd = beforeEnd;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Connection connection = connectionPool.fetch(10);
        if (connection != null) {
            got.incrementAndGet();
            connection.preparedStatement();
            connection.commit();
            connectionPool.release(connection);
        } else {
            notGot.decrementAndGet();
        }
        beforeEnd.countDown();
    }
}

class Main {
    public static void main(String[] args) {
        int threadSize = 17;
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        CountDownLatch beforeEnd = new CountDownLatch(threadSize);
        ExecutorService executorService = Executors.newCachedThreadPool();

        ConnectionPool connectionPool = new ConnectionPool(3);
        AtomicInteger got = new AtomicInteger(0);
        AtomicInteger notGot = new AtomicInteger(0);
        for (int i = 0; i < threadSize; i++) {
            executorService.execute(new UserSession(connectionPool, got, notGot, countDownLatch, beforeEnd));
            countDownLatch.countDown();
        }
        executorService.shutdown();

        try {
            beforeEnd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("got :" + got.get());
        System.out.println("not got :" + notGot.get());
        System.out.println(connectionPool.size());
    }
}
