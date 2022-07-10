import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class testProgram extends PID_Code {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);                        //Get variable from user
        System.out.println("1. getPID()");
        System.out.println("2. getPIDWait()");
        try{
            num = sc.nextInt();
        }catch (Exception e){
            System.out.println(e);
        }
        if(num != 1 && num != 2){
            System.out.println("Enter 1 or 2");
            System.exit(0);
        }System.out.print("The number of threads created : ");
        try {
            numOfThread = sc.nextInt();
        }catch (Exception e){
            System.out.println("Enter an integer");
            System.exit(0);
        }
        System.out.print("The Life time of the program (sec) : ");
        try {
            programLifeTime = sc.nextInt();
        }catch (Exception e){
            System.out.println("Enter an integer");
            System.exit(0);
        }
        System.out.print("The Life time of each thread (sec) : ");
        try {
            threadLifeTime = sc.nextInt();
        }catch (Exception e){
            System.out.println("Enter an integer");
            System.exit(0);
        }
        System.out.print("The frequency of each thread creation : (Math.random() * 100) * ");
        try {
            randomCreation = sc.nextInt();
        }catch (Exception e){
            System.out.println("Enter an integer");
            System.exit(0);
        }
        System.out.println("Test program is initialized with " + numOfThread + " thread and " + programLifeTime
                + " seconds, with the life time " + threadLifeTime + " seconds of each thread");

        startTime = System.currentTimeMillis();                                     //Check start time
        ArrayList<Thread> threadArray = new ArrayList();                            //To interrupt thread make arraylist

        for (int i = 1; i <= numOfThread; i++) {
            long endTime = System.currentTimeMillis();
            long diffTime = (long) ((endTime - startTime) / (1000.0));
            if(diffTime > programLifeTime) break;
            try {
            Thread.sleep((int) (Math.random() * 100) * randomCreation);     //Sleep random time to create randomly
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(new PID_Code(i));
            threadArray.add(thread);
            thread.start();
        }

        while (true) {
            long afterTime = System.currentTimeMillis();                        //Time check
            long totalTime = (long) ((afterTime - startTime) / (1000.0));       //Program Ends
            for(int i = 0; i < threadArray.size(); i++) {
                threadArray.get(i).interrupt();
            }
            if (totalTime >= programLifeTime) {
                System.out.println(programLifeTime + " seconds has passed... Program ends");
                break;
            }
        }
        System.exit(0);
    }
}


