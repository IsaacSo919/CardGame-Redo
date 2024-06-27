import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CardDeck {
    private int deckID;
    private final List<Card> cards;
    public CardDeck(int deckID){
        this.deckID = deckID;
        this.cards = new LinkedList<>();
    }
    public void addCard(Card card){
        cards.add(card);
    }
}
