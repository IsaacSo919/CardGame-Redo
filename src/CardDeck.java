import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CardDeck {
    private int deckID;
    private final List<Card> cardDeck;
    public CardDeck(int deckID){
        this.deckID = deckID;
        this.cardDeck = new LinkedList<>();
    }
    public void addDeck(Card card){
        cardDeck.add(card);
    }
    public List<Card> getCardDeck(){
        return cardDeck;
    }

    public int getDeckID() {
        return deckID;
    }
}
