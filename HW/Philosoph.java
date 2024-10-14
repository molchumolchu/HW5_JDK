package HW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Philosoph implements Runnable {

    // volatile static Boolean [] forks;
    private volatile static Boolean[] forks = {true, true, true, true, true};
    private static CountDownLatch eatSignal=new CountDownLatch(5);
    private static CountDownLatch thinkSignal=new CountDownLatch(5);
    private int num;
    private int countEat = 1;


    public Philosoph(int num, CountDownLatch eatSignal, CountDownLatch thinkSignal) {
        this.num = num;
        this.eatSignal = eatSignal;
        this.thinkSignal = thinkSignal;
        
        this.forks = forks;
    }

    public static void main(String[] args) {
        ArrayList<Philosoph> philosophs = new ArrayList<>(Arrays.asList(
            new Philosoph(1, eatSignal, thinkSignal),
            new Philosoph(2, eatSignal, thinkSignal),
            new Philosoph(3, eatSignal, thinkSignal),
            new Philosoph(4, eatSignal, thinkSignal),
            new Philosoph(5, eatSignal, thinkSignal)
        ));

        for (Philosoph philosoph: philosophs){
            new Thread(philosoph).start();
        }
    }


    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(2000,3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        while (countEat<=3) {
            eat();
            think();
        }
        System.out.println("Филосов "+ this.num + " наелся до отвала и будет размышлять");
        think();

    }


    // метод размышлений
    private void think (){
            System.out.println("Филосов "+ this.num + " приступил к размышлениям");
            while(checkForks()==false){
                go();
            }
            if (checkForks()==true) {
                thinkSignal.countDown();
            }
    }

    //метод еды
    void eat (){
        try {
            if (checkForks()==true) {
                getForks();
                System.out.println("Филосов " + this.num +" приступил к еде " + countEat +" раз");
                go();
                Thread.sleep(new Random().nextInt(2000,5000));
                eatSignal.countDown();
                countEat++;
                putForks();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void go (){
        synchronized(this){
            notify();
        }
    }

    // проверяем левую (-1) и правую вилку(-2)
    public synchronized boolean checkForks(){
        if (num==1&&forks[0]==true && forks[4]==true){
            return true;
        }
        if (num!=1&&forks[num-1]==true && forks[num-2]==true){
            return true;
        }
        else{
            return false;
        }        
    }


    // берем вилку
    public void getForks(){
        if(num==1){
            forks[num-1]=false;
            forks[4]=false;
            System.out.println("Заняты Вилка " + (num-1) +" и "+"вилка " +(4) +" Философом "+num);
        }
        if(num!=1){
            forks[num-1]=false;
            forks[num-2]=false;
            System.out.println("Заняты Вилка " + (num-1) +" и "+"вилка " +(num-2) +" Философом "+num);
        }

                
    }

    // кладем вилку
    public void putForks(){
        if(num==1){
            forks[num-1]=true;
            forks[4]=true;
            System.out.println("Освободились Вилка " + (num-1) +" и "+"вилка " +(4) +" Философом "+num);
        }
        if(num!=1){
            forks[num-1]=true;
            forks[num-2]=true;
            System.out.println("Освободились Вилка " + (num-1) +" и "+"вилка " +(num-2) +" Философом "+num);
        }
    }

}