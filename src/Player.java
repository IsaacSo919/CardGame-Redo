import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player extends Thread {
    private final int playerID;
    private List<Card> hand;
    private volatile boolean done = false; //The volatile keyword in Java is used to indicate that a variable's value will be modified by different threads.

    public Player(int playerID) {
        this.playerID = playerID;
        this.hand = new LinkedList<>();

    }
    public void run() {
        while(!done){
            if(checkWin()){
                System.out.println("Player " + playerID + " wins!");
                done = true;
            }

        }
    }

    public boolean checkWin() {
//        this.setHandtoSame();
        for (Card card : hand) {
            if (card.getFaceValue() != playerID) {
                return false; // like a filter, if the card is not the same as the playerID, return false, this will break the loop
            }
        }
        return true; // until all cards are the same as the playerID, return true
    }
    public void addHand(Card card){
        hand.add(card);
    }

    public void setHandtoSame() {
        this.hand.clear();
        for(int i = 0; i < 4; i++){
            Card tempCard = new Card(1);
            this.hand.add(tempCard);
        }
    }

    public List<Card> getHand(){
        return hand;
    }
    public void printHand(){
        System.out.println("Player " + playerID + " has the following cards: ");
        for (Card card : hand) {
            System.out.println(card.getFaceValue());
        }
    }

    public int getPlayerID() {
        return playerID;
    }
}
