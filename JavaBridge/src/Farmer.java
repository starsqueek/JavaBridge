import java.util.Random;
import java.util.concurrent.Semaphore;


class BridgeFarmer implements Runnable
{
    private static final int NB_FARMERS = 2;
    private static final Semaphore[] bridge = new Semaphore[1];
    private static Random rand = new Random();
    private int id;
    private String village;

    public BridgeFarmer(int id) {
        this.id = id;
        bridge[0] = new Semaphore(1);
        if(this.id == 1){
            this.village = "North";
        }
        else{
            this.village = "South";
        }
    }

    private void farmer_sleep() {
        try {
            Thread.sleep(rand.nextInt(10) * 1000);
        } catch (InterruptedException e) {
            System.err.println("One philosopher thread died :(");
            System.exit(1);
        };
    }

    private void crossbridge() {

        System.out.println(this + " Wants to cross the bridge.");

        boolean getbridge  = bridge[0].tryAcquire();
        if (getbridge == true){
            System.out.println(this + " Is currently using the bridge");

            this.farmer_sleep();

            bridge[0].release();

            System.out.println(this + " Has left the bridge.");
            if(this.village == "North"){
                this.village = "South";
            }
            else{
                this.village = "North";
            }
            System.out.println(this + " is currenntly in the "+ this.village +" village.");
        }
        else{
            System.out.println(this + " Bridge is currently in use");
            //
        }
    }

    public String toString() {
        return "[Farmer " + id + "]";
    }

    public void run() {
        System.out.println(this + " is currenntly in the "+ this.village +" village.");
        for (int i = 0;;) {
            farmer_sleep();
            crossbridge();
        }
    }

    public static void main(String [] args) {
        Thread farmn[] = new Thread[NB_FARMERS];
        System.out.println("Start");

        for (int i = 0 ; i < NB_FARMERS ; i++) {
            farmn[i] = new Thread(new BridgeFarmer(i));
        }

        for (int i = 0 ; i < NB_FARMERS ; i++) {
            farmn[i].start();
        }

        try {
            for (int i = 0 ; i < NB_FARMERS ; i++)
                farmn[i].join();
        }
        catch (InterruptedException e) {
            System.err.println("One Farmer thread died :(");
            System.exit(1);
        }
        System.out.println("End");
    }
}	
