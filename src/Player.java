import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Player implements Runnable {
    private final int playerID;
    private final List<Card> hand;
    private final CardGame cardGame;
    private final int preferredDenomination;
    private PrintWriter writer;
    private final CountDownLatch latch;
//    private volatile boolean done = false; //The volatile keyword in Java is used to indicate that a variable's value will be modified by different threads.
    private boolean firstTime;
    public Player(int playerID, CardGame cardGame,CountDownLatch latch) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.cardGame = cardGame;
        this.hand = new LinkedList<>();
        this.firstTime = true;
        this.latch = latch;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("player" + playerID + ".txt", false)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            if (firstTime){
                writer.println("Player " + playerID + " initial hand is " + this.printHand());
                if (checkInitialWin()) {
                    // Check for win condition when the game starts,maybe the player already has 4 cards of the same face value
                    System.out.println("Player " + playerID + " wins");
                    System.out.println("player " + playerID + " final hand: " + this.printHand());
                    synchronized(cardGame){
                        cardGame.setGameWon(this.playerID);// Set the gameWon flag to true
                    }
                }
                firstTime = false;
            }

            while (!Thread.currentThread().isInterrupted()) {
                synchronized (cardGame) {
//                    System.out.println("isGameWon:"+cardGame.isGameWon());
                    if (cardGame.isGameWon()) {
//                        System.out.println("Winner " + cardGame.getWinnerID());
                        if (cardGame.getWinnerID() == this.playerID) {// if statement should not run keep it here in case of future changes
                            writer.println("Player " + playerID + " wins");
                            writer.println("Player " + playerID + " exits");
                            writer.println("player " + playerID + " final hand: " + this.printHand());
                        } else {
                            writer.println("player " + cardGame.getWinnerID() + " has informed player " + this.playerID + " that player " + cardGame.getWinnerID() + " has won\n" +
                                    "player " + this.playerID + " exits\n" +
                                    "player " + this.playerID + " hand: " + this.printHand());
                        }
                        writer.flush();
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                CardDeck previousDeck = cardGame.getDeck(playerID - 1);
                CardDeck nextDeck = cardGame.getDeck(playerID);

                // Draw a card from the previous deck
                Card drawnCard = previousDeck.drawCard();
                if (drawnCard != null) {
                    addHand(drawnCard);
                    writer.println("Player " + playerID + " draws a " + drawnCard.getFaceValue() + " from deck " + previousDeck.getDeckID());
                }

                // Discard a non-preferred card to the next deck
                Card cardToDiscard = null;
                for (Card card : hand) {
                    if (card.getFaceValue() != preferredDenomination) {
                        cardToDiscard = card;
                        break;
                    }
                }
                if (cardToDiscard != null) {
                    hand.remove(cardToDiscard);
                    nextDeck.discardCard(cardToDiscard);
                    writer.println("Player " + playerID + " discards a " + cardToDiscard.getFaceValue() + " to deck " + nextDeck.getDeckID());
                } else {
                    cardToDiscard = hand.remove(0);
                    nextDeck.discardCard(cardToDiscard);
                    writer.println("Player " + playerID + " discards a " + cardToDiscard.getFaceValue() + " to deck " + nextDeck.getDeckID());
                }
                writer.println("Player " + playerID + " current hand is " + this.printHand());
                if (checkWin()) {
                    System.out.println("Player " + playerID + " wins");
                    System.out.println("player " + playerID + " final hand: " + this.printHand());
                    writer.println("Player " + playerID + " wins");
                    writer.println("Player " + playerID + " exits");
                    writer.println("player " + playerID + " final hand: " + this.printHand());
                    writer.flush();
                    synchronized (cardGame) {
                        cardGame.setGameWon(this.playerID);
                    }
                    break;
                }

                writer.flush();
                Thread.sleep(1000); // Simulate time taken to play
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writer.close();
            latch.countDown(); // Decrement the latch count
        }

    }

    private boolean checkInitialWin() {
        if (hand.size() < 4) return false; // Ensure there are enough cards to check
        int previousValue = hand.get(0).getFaceValue();
        for (Card card : hand) {
            if (card.getFaceValue() != previousValue) {
                return false;
            }
        }
        return true;
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
    public String printHand(){
        StringBuilder handString = new StringBuilder();
        for (Card card : hand) {
            handString.append(card.getFaceValue()).append(" ");
        }
        return handString.toString().trim(); // remove the last space
    }

    public int getPlayerID() {
        return playerID;
    }
}
