import java.io.Serializable;

public class Turn implements Serializable {

    private Player player       = null;
    private Action minAction    = null;
    private Action playerAction = null;

    public Turn(Player player, Action minAction) {
        this.player    = player;
        this.minAction = minAction;
    }

    public void getAction() {
        while(playerAction == null) {
            //loop
        }
    }

    public Action end() {
        return this.minAction;
    }
}