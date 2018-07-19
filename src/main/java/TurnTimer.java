import java.io.Serializable;

public class TurnTimer extends Thread implements Serializable {
    private int timer = 0;

    public TurnTimer(int count) {
        timer = count;
    }

    public void run() {
        while(timer > 0) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            timer--;
        }

    }
}