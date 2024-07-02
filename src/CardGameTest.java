import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class CardGameTest {
    @Test
    public void testInputNumberOfPlayers() {
        // Simulate user input
        String input = "4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        int numberOfPlayers = CardGame.scanNumberOfPlayers();
        assertTrue(numberOfPlayers > 0, "The number of players should be greater than 0.");
    }
    private CardGame cardGame;
    private int numberOfPlayers;
    @Before
    public void setUp() {
        int numberOfPlayers = 4;
        cardGame = new CardGame(numberOfPlayers);
    }
    @Test
    public void testDistributeCards() {
        ArrayList<Integer> faceValues = new ArrayList<>();

        for (int i = 1; i <= cardGame.getNumberOfPlayers(); i++) {
            for (int j = 0; j < 8; j++) { // Assuming 8 cards per player and deck in total
                faceValues.add(i);
            }
        }
        System.out.println("Face values: " + faceValues);

        cardGame.distributeCards(faceValues);

        // Check if players have 4 cards each
        for (Player player : cardGame.getPlayerArrayList()) {
            assertEquals(4, player.getHand().size(), "Each player should have 4 cards in hand");
        }

        // Check if decks have the remaining 4 cards each
        for (CardDeck deck : cardGame.getCardDeckArrayList()) {
            assertEquals(4, deck.getCardDeck().size(), "Each deck should have 4 cards");
        }
    }
}