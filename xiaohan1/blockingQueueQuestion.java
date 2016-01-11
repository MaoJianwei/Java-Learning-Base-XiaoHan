package xiaohan1;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by mao on 2016/1/10.
 */
public class blockingQueueQuestion {


    public static void main(String arg[]) {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(9);

        CyclicBarrier sync = new CyclicBarrier(7);

        Thread p1 = new producer(blockingQueue,sync);
        Thread p2 = new producer(blockingQueue,sync);

        Thread c1 = new customer(blockingQueue,sync);
        Thread c2 = new customer(blockingQueue,sync);
        Thread c3 = new customer(blockingQueue,sync);
        Thread c4 = new customer(blockingQueue,sync);
        Thread c5 = new customer(blockingQueue,sync);

        c1.start();
        c2.start();
        c3.start();
        c4.start();
        c5.start();
        p1.start();
        p2.start();


        int aliveCount = 2;
        do{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Sleep is interrupted");// TODO - check - should not go here, can delete
            }

            aliveCount = 0;

            if(p1.isAlive())
                aliveCount++;

            if(p2.isAlive())
                aliveCount++;

            System.out.println("------- " + aliveCount + " is alive -------");
        }while(aliveCount != 0);

        customer.isNeedExit = true;
        producer.isNeedExit = true;
        p1.interrupt(); // interrupt sleep to exit thread
        p2.interrupt();

    }

    public static class customer extends Thread{

        BlockingQueue<Integer> blockingQueue;
        CyclicBarrier sync;

        static boolean isNeedExit = false;


        customer(BlockingQueue<Integer> commonQueue, CyclicBarrier sync){
            this.blockingQueue = commonQueue;
            this.sync = sync;
        }

        @Override
        public void run(){

            try {
                System.out.println("wait in");
                sync.await();
            } catch (Exception e) {
                System.out.println("sync:somthing wrong");// TODO - check - should not go here, can delete
            }

            Integer food ;

            do{
                try {
                    food = blockingQueue.poll(50,TimeUnit.MILLISECONDS); // do not use take() - avoid blocked forever
                    if(food != null)
                        System.out.println("get: " + food);
                } catch (InterruptedException e) {
                    food = Integer.valueOf(99); // TODO - check - should not go here, can delete
                }
            }while(!isNeedExit && ( food == null || 0 != food % 100));
            System.out.println("===== c exit =====");
        }
    }

    public static class producer extends Thread{

        BlockingQueue<Integer> blockingQueue;
        CyclicBarrier sync;

        static boolean isNeedExit = false;

        producer(BlockingQueue<Integer> commonQueue, CyclicBarrier sync){
            this.blockingQueue = commonQueue;
            this.sync = sync;
        }

        @Override
        public void run(){

            try {
                System.out.println("wait in");
                sync.await();
            } catch (Exception e) {
                System.out.println("sync:somthing wrong");// TODO - check - should not go here, can delete
            }

            while(!isNeedExit){

                try {
                    Random randomGenerate = new Random();
                    int wait = randomGenerate.nextInt(5) % 5 + 1;

                    System.out.println("sleep: " + wait);
                    sleep(wait*1000);

                    int food = randomGenerate.nextInt(1000) & 1000 + 1;

                    blockingQueue.put(food);

                    System.out.println("put: " + food);

                } catch (InterruptedException e) {
                    System.out.println("Sleep is interrupted");
                }

            }
            System.out.println("===== p exit =====");
        }
    }
}
