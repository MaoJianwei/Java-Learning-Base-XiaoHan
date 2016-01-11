package xiaohan2;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by mao on 2016/1/10.
 */
public class SportMeet {


    public static void main(String arg[]) {

        CyclicBarrier sync = new CyclicBarrier(8);


        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (int i = 1; i <= 8; i++)
            executorService.execute(new athlete(sync, i));

        executorService.shutdown();
    }

    public static class athlete implements Runnable {

        CyclicBarrier sync;
        int num;

        athlete(CyclicBarrier sync, int num) {
            this.sync = sync;
            this.num = num;
        }

        @Override
        public void run() {

            try {
                System.out.println( num + " is ready!");
                sync.await();
            } catch (Exception e) {
                System.out.println("sync:somthing wrong");// TODO - check - should not go here
            }
            System.out.println( num + " run run run!");

        }
    }
}
