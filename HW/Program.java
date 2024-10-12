package HW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


public class Program {
    
    private volatile static ArrayList<Boolean> forks = new ArrayList<>(Arrays.asList(true, false, false, true,true));


    private static CountDownLatch eatSignal=new CountDownLatch(5);
    private static CountDownLatch thinkSignal=new CountDownLatch(5);

    private static ArrayList<Philosoph> philosophs = new ArrayList<>(Arrays.asList(
        new Philosoph(1, eatSignal, thinkSignal, forks),
        new Philosoph(2, eatSignal, thinkSignal, forks),
        new Philosoph(3, eatSignal, thinkSignal, forks),
        new Philosoph(4, eatSignal, thinkSignal, forks),
        new Philosoph(5, eatSignal, thinkSignal, forks)
        ));
    public static void main(String[] args) {
        
        for (Philosoph philosoph: philosophs){
            new Thread(philosoph).start();
        }
    }

}
