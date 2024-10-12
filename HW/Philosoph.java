package HW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Philosoph implements Runnable {

    volatile ArrayList<Boolean> forks;
    private CountDownLatch eatSignal;
    private CountDownLatch thinkSignal;
    private int num;
    private int countEat = 1;

    // private boolean leftFork;
    // private boolean rightFork;

    public Philosoph(int num, CountDownLatch eatSignal, CountDownLatch thinkSignal, ArrayList<Boolean> forks) {
        this.num = num;
        this.eatSignal = eatSignal;
        this.thinkSignal = thinkSignal;
        // this.leftFork=leftFork;
        // this.rightFork=rightFork;
        this.forks = forks;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(2000,3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thinkSignal.countDown();
        think(); //??
        eat();
        think();
        eat();
        think();
        eat();
        System.out.println("Филосов "+ this.num + " наелся до отвала и будет размышлять");
        think();

    }


    // метод размышлений
    private void think (){
        try {
            // go();
            thinkSignal.await();
            System.out.println("Филосов "+ this.num + " приступил к размышлениям");
            if (checkForks()==true) {
                thinkSignal.countDown();//!
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //метод еды
    void eat (){
        try {
            go();
            getForks();
            // System.out.println(" Филосов " + this.num +" взял вилки");
            // eatSignal.await();
            eatSignal.countDown();
            System.out.println("Филосов " + this.num +" приступил к еде " + countEat +" раз");
            Thread.sleep(new Random().nextInt(3000,5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // System.out.println("Филосов " + this.num +" закончил есть");
        putForks();
        countEat++;
    }

    public void go (){
        synchronized(this){
            notify();
        }
    }

    // проверяем левую (-1) и правую вилку(-2)
    public boolean checkForks(){
        
        if (this.num == 1 && forks.get(0).equals(true) && forks.get(forks.size()-1).equals(true)) {
            return true;
        }
        if (this.num>=2 && forks.get(num-2).equals(true) && forks.get(num-1).equals(true)){
            return true;
        }
        else{
            return false;
        }        
    }

    // public boolean checkForks(){
        
    //     if(leftFork==true&&rightFork==true){
    //         return true;
    //     }        
    //     else return false;
    // }

    // берем вилку
    public void getForks(){
        // leftFork = false;
        // rightFork = false;
        // System.out.println("Вилка " + (num-1) +" и "+"вилка " +(num) +" заняты Философом "+num);
        
        if (this.num == 1) {
            forks.set(num-1, false);
            forks.set(forks.size()-1, false);
            System.out.println("Заняты Вилка " + (num) +" и "+"вилка " +(forks.size()) +" Философом "+num);
        }
        if(this.num >=2) {
            forks.set(num-2, false);
            forks.set(num-1, false);
            System.out.println("Заняты Вилка " + (num-1) +" и "+"вилка " +(num) +" Философом "+num);
        }          
    }

    // кладем вилку
    public void putForks(){
        // rightFork=true;
        // leftFork=false;
        // System.out.println("Вилка " + (num) +" и "+"вилка " +(num-1) +" освободились Философом "+num);
        if (this.num == 1) {
            forks.set(num-1, true);
            forks.set(forks.size()-1, true);
            System.out.println("Освободилась Вилка " + (num) +" и "+"вилка " +(5) +" Философом "+num);
        }
        if(this.num >=2) {
            forks.set(num-2, true);
            forks.set(num-1, true);
            System.out.println("Освободилась Вилка " + (num-1) +" и "+"вилка " +(num) +" Философом "+num);
        }          
    }

}
