import java.util.LinkedList;
import java.util.List;

public class CardDeck {
    private final int deckID;
    private final List<Card> cardDeck;
    public CardDeck(int deckID){
        this.deckID = deckID;
        this.cardDeck = new LinkedList<>();
    }
    public synchronized Card drawCard() { // synchronized prevents the Race conditions, this method is thread-safe, only one thread can access it at a time.
        // Draw a card from the deck, remove it from the deck and return it
        if (!cardDeck.isEmpty()) {
            return cardDeck.remove(0); // Draw from the top of the deck
        }
        return null; // or throw an exception if preferred
    }
    public synchronized void discardCard(Card card) {
        // Discard a card to the deck
        cardDeck.add(card); // Discard to the top of the deck
    }
    public void addDeck(Card card){
        cardDeck.add(card);
    }
    public synchronized List<Card> getCardDeck(){
        return cardDeck;
    }

    public int getDeckID() {
        return deckID;
    }

}
