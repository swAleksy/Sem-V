import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MyThread extends Thread{
    private final int i;

    MyThread(int i) {
        this.i = i;
    }
    public void run(){
        for (int j = 0; j < 10; j++)
            System.out.println(j);
        System.out.println("Wątek " + this.i);
        
    }
}

class MyThread2 extends Thread{
    private final int i;
    MyThread2(int i) {
        this.i = i;
    }

    public void run(){
        for (int j = 0; j < 1000000; j++)
            System.out.println(j);
        System.out.println("z4Thr: " + i);

    }
}

class Printer{
    public synchronized void printNumbers(int mod){
        for (int i = mod; i <= 10 * mod; i++){
            System.out.println(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }        
        }
    }
}

class MyPrinterThread implements Runnable{
    private final int mod;
    private final Printer printer;

    MyPrinterThread(Printer printer, int mod){
        this.printer = printer;
        this.mod = mod;
    }

    public void run(){
        printer.printNumbers(mod);
    }
}

class Counter {
    private int i = 0;
    private final Lock lock = new ReentrantLock();

    void increment() {
        lock.lock(); 
        try {
            i += 1;
        } finally {
            lock.unlock(); 
        }
    }

    void getValue(){
        lock.lock();
        try {
            System.out.println("Val: " + i);
        } finally {
            lock.unlock();
        }
    }
}

class IncrementThread extends Thread{
    private Counter counter;
    private final int iterations;

    IncrementThread(Counter counter,  int iterations){
        this.iterations = iterations;
        this.counter = counter;
    }

    public void run(){
        for (int i = 0; i < iterations; i++)
            counter.increment();
    }
}

class GetValThread extends Thread{
    private Counter counter;

    GetValThread(Counter counter){
        this.counter = counter;
    }

    public void run(){
        counter.getValue();
    }
}

public class lab1 {
    public static void main(String[] args) {
        //1


        System.out.println("start");
        // 2
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3
        List<Thread> thrArray = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            Thread thread = new MyThread(i);
            thrArray.add(thread);
        }
        
        for (Thread thread : thrArray) {
            thread.start();
        }

        for (Thread thread : thrArray) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 4
        Thread z4thr = new MyThread2(1);
        Thread z4thr2 = new MyThread2(2);

        z4thr2.setPriority(Thread.MAX_PRIORITY);

        z4thr.start();
        z4thr2.start();

        try {
            z4thr.join();
            z4thr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 5&7
        Printer prt = new Printer();
        Thread z5_1 = new Thread(new MyPrinterThread(prt,2));
        Thread z5_2 = new Thread(new MyPrinterThread(prt,3));

        z5_1.start();
        z5_2.start();

        try {
            z5_1.join();
            z5_2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 6 -- Po dodaniu slowa synchronized - watki nie wykonuja sie wsołbieznie, tylko jeden po drugim
        // 8&9
        Counter mc = new Counter();
        IncrementThread z8_1 = new IncrementThread(mc, 5);
        GetValThread z8_2 = new GetValThread(mc);

        z8_1.start();
        z8_2.start();

    }
}
