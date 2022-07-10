import java.util.ArrayList;

public class PID_Code implements PIDManager, Runnable{
    private final static Object sync = new Object();                    //Synchronization
    static int pidCheck[] = new int[MAX_PID - MIN_PID + 1];             //Pid usage check
    static int pidPtr = 0;                                              //Pid location check
    int seqNum;                                                         //Thread Number
    static long threadLifeTime;                                         //Thread working time
    static long programLifeTime;                                        //Program working time
    static long startTime;                                              //Program beginning time
    static int num;                                                     //getPID or getPIDWait
    static int numOfThread;                                             // # of Threads
    static int randomCreation;                                          //Setting threadLifeTime

    public PID_Code(int seqNum) {
        this.seqNum = seqNum;
    }
    public PID_Code(){}


    @Override
    public int getPID() {                                               //Allocate PID to the array
        synchronized (sync) {
            if(pidPtr >= 0 && pidPtr < 124){
                    pidCheck[pidPtr] = 1;
                    pidPtr++;
                    return pidPtr + 3;
                }
            else{
                return -1;
            }
        }
    }

    @Override
    public int getPIDWait() {
        synchronized (sync){                                            //Allocate PID to the array if there is a space
            int sum = 0;
            for(int i = 0; i < 124; i++){
                sum = sum + pidCheck[i];
            }
            if(sum < 124){
                if (pidPtr >= 0 && pidPtr <= 123) {
                    pidCheck[pidPtr] = 1;
                    pidPtr++;
                    return pidPtr + 3;
                }
            }else {                                                     //if not, execute wait.
                try{
                    sync.wait();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            for (int i = 0; i < 124; i++) {                             //Allocate PID to the array empty space
                if (pidCheck[i] == 0) {
                    pidCheck[i] = 1;
                    return i + 4;
                }
            }
            return 0;
        }
    }

    @Override
    public void releasePID(int pid) {                                   //Decrease Ptr and set pidCheck 0 and notify.
        synchronized (sync) {
            pidPtr--;
            pidCheck[pid - MIN_PID] = 0;
            sync.notify();
        }
    }


    public void run(){
        int pid;
        if(num == 1){
            pid = getPID();
            if(pid == -1) {
                System.out.println("No available pids");
                return;
            }
        }else{
            pid = getPIDWait();
        }
        long endTime = System.currentTimeMillis();                      // check time to calcurate time
        long diffTime = (long) ((endTime - startTime) / (1000.0));
        if (diffTime <= programLifeTime)                                // print thread is created
            System.out.println("Thread " + this.seqNum + " created at Second "
                    + diffTime + " (pid:" + pid + ")");
        try {                                                           // print is sleep during threadlife
            Thread.sleep(1000 * threadLifeTime);
        } catch (Exception e) {
        }
        if (diffTime + threadLifeTime <= programLifeTime)               // print thread is destroyed
            System.out.println("Thread " + this.seqNum + " destroyed at Second "
                    +(diffTime +threadLifeTime)+ " (pid:" + pid + ")");
        releasePID(pid);
    }
}
