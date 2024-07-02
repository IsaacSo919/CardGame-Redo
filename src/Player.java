import java.util.LinkedList;
import java.util.List;

public class Player implements Runnable {
    private final int playerID;
    private final List<Card> hand;
    private final CardGame cardGame;
    private final int preferredDenomination;
//    private volatile boolean done = false; //The volatile keyword in Java is used to indicate that a variable's value will be modified by different threads.

    public Player(int playerID, CardGame cardGame) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.cardGame = cardGame;
        this.hand = new LinkedList<>();

    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (checkWin()) { // check before the player draws a card
                System.out.println("Player " + playerID + " wins!");
                cardGame.stopGame();
                break;
            }
            try {
                CardDeck previousDeck = cardGame.getDeck(playerID - 1);
                CardDeck nextDeck = cardGame.getDeck(playerID);

                // Draw a card from the previous deck
                Card drawnCard = previousDeck.drawCard();
                if (drawnCard != null) {
                    addHand(drawnCard);
                    System.out.println("Player " + playerID + " draws a " + drawnCard.getFaceValue() + " from deck " + previousDeck.getDeckID());
                }

                // Discard a non-preferred card to the next deck
                Card cardToDiscard = null;
                for (Card card : hand) { //
                    if (card.getFaceValue() != preferredDenomination) {
                        cardToDiscard = card;
                        break;
                    }
                }
                if (cardToDiscard != null) {
                    hand.remove(cardToDiscard);
                    nextDeck.discardCard(cardToDiscard);
                    System.out.println("Player " + playerID + "  discards a " + cardToDiscard.getFaceValue()+" to deck " + nextDeck.getDeckID());
                } else {
                    // If no non-preferred card is found, discard any card
                    cardToDiscard = hand.remove(0);// remove the first card
                    nextDeck.discardCard(cardToDiscard);
                    System.out.println("Player " + playerID + " discards a " + cardToDiscard.getFaceValue()+" to deck " + nextDeck.getDeckID());
                }

                // Check for win condition
                if (checkWin()) {
                    System.out.println("Player " + playerID + " wins!");
                    // Print the player's hand
                    System.out.println("player 1 current hand is "+ this.printHand());
                    cardGame.stopGame();
                    break;
                }

                Thread.sleep(1000); // Simulate time taken to play

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                break;
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
