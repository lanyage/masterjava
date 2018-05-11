package Register;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.logging.Logger;

class ConcurrentDemo {
   
    public static ConcurrentMap<Integer,Integer> concurrentMap = new ConcurrentHashMap<>();
    public LinkedList<Integer> list = new LinkedList<>();
    public static final int MAX_SIZE = 2;
    public static final int MIN_SIZE = 0;

    public LinkedList<Integer> getList() {
        return list;
    }

    public static void main(String[] args) {
        ConcurrentDemo concurrentDemo = new ConcurrentDemo();
        LinkedList<Integer> container = concurrentDemo.getList();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Producer(container,"p1"));
        exec.execute(new Producer(container,"p2"));
        exec.execute(new Producer(container,"p3"));
        exec.execute(new Producer(container,"p4"));
        exec.execute(new Producer(container,"p5"));
        exec.execute(new Consumer(container,"c1"));
        exec.execute(new Consumer(container,"c2"));
        exec.execute(new Consumer(container,"c3"));
        exec.execute(new Consumer(container,"c4"));
        exec.execute(new Consumer(container,"c5"));
    }
}
class Producer implements Runnable {
    private LinkedList<Integer> container;
    private String name;
    private static int count = 1;
    public Producer(LinkedList<Integer> container,String name) {
        this.container = container;
        this.name = name;
    }
    public void produce() {
        synchronized (Producer.class) {
            System.out.println(this.name + ":" + (count));
            container.addFirst(count++);
        }
    }
    @Override
    public void run() {
        while(true) {
            synchronized (container) {
                if(container.size() == ConcurrentDemo.MAX_SIZE) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    produce();
                    container.notifyAll();
                }
            }
        }
    }
}
class Consumer implements Runnable {

    private LinkedList<Integer> container;
    private String name;

    public Consumer(LinkedList<Integer> container,String name) {
        this.container = container;
        this.name = name;
    }
    public void consume() {
        int popInt = container.pop();
        System.out.println(this.name+ ":" + popInt);
    }

    @Override
    public void run() {
        while(true) {
            synchronized (container) {
                if(container.size() == ConcurrentDemo.MIN_SIZE) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    consume();
                    container.notifyAll();
                }
            }
        }
    }
}
