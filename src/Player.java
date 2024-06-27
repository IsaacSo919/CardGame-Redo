import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player extends Thread {
    private final int playerID;
    private List<Card> hand;

    public Player(int playerID) {
        this.playerID = playerID;
        this.hand = new LinkedList<>();

    }
    public void addHand(Card card){
        hand.add(card);
    }


}
