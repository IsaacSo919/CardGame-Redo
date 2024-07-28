import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
        numberOfPlayers = 2;
        cardGame = new CardGame(numberOfPlayers);
    }
    @Test
    public void testCreateGame() {
        CardGame cardGame = new CardGame(numberOfPlayers);
        //Check the number of players
        Player[] players = cardGame.getPlayerArrayList();
        assertEquals(numberOfPlayers, players.length, "Number of players should be " + numberOfPlayers);
        CardDeck[] decks = cardGame.getCardDeckArrayList();
        assertEquals(numberOfPlayers, decks.length, "Number of decks should be " + numberOfPlayers);

        // Ensure each player is initialized correctly
        for (int i = 0; i < numberOfPlayers; i++) {
            assertNotNull(players[i], "Player " + (i + 1) + " should be initialized");
            assertEquals(i + 1, players[i].getPlayerID(), "Player ID should be " + (i + 1));
        }

        // Ensure each deck is initialized correctly
        for (int i = 0; i < numberOfPlayers; i++) {
            assertNotNull(decks[i], "Deck " + (i + 1) + " should be initialized");
            assertEquals(i + 1, decks[i].getDeckID(), "Deck ID should be " + (i + 1));
        }
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
    @Test
    public void testSetGameWon() {
        cardGame.setGameWon(1); // the number is the winner's ID
        assertTrue(cardGame.isGameWon(), "The game should be marked as won");//
        assertEquals(1, cardGame.getWinnerID(), "The winner ID should be 1");
    }
    @Test
    public void testCardDeckOperations() {
        CardDeck deck = new CardDeck(1);
        Card card = new Card(5);
        deck.discardCard(card);
        assertEquals(1, deck.getCardDeck().size(), "Deck should have 1 card after discarding");

        Card drawnCard = deck.drawCard();
        assertEquals(card, drawnCard, "The drawn card should be the same as the discarded card");
        assertTrue(deck.getCardDeck().isEmpty(), "Deck should be empty after drawing the card");
    }
    @Test
    public void testInitialWinCondition() {
        // Set up a player with an initial winning hand
        Player player = new Player(1, cardGame, new CountDownLatch(1));
        player.addHand(new Card(1));
        player.addHand(new Card(1));
        player.addHand(new Card(1));
        player.addHand(new Card(1));

        assertTrue(player.checkWin(), "Player should win with four cards of the same value");
    }
    @Test
    public void testPlayerRun() throws InterruptedException {
        CardGame cardGame = new CardGame(2);
        Player player1 = cardGame.getPlayerArrayList()[0];
        Player player2 = cardGame.getPlayerArrayList()[1];

        Thread playerThread1 = new Thread(player1);
        Thread playerThread2 = new Thread(player2);

        // Give player1 a winning hand immediately
        player1.addHand(new Card(1));
        player1.addHand(new Card(1));
        player1.addHand(new Card(1));
        player1.addHand(new Card(1));

        // Give player2 a non-winning hand immediately
        player2.addHand(new Card(3));
        player2.addHand(new Card(3));
        player2.addHand(new Card(3));
        player2.addHand(new Card(3));

        playerThread1.start();
        playerThread2.start();



        // Allow some time for threads to run
        Thread.sleep(2000);

        assertTrue(cardGame.isGameWon(), "Game should be marked as won");
        assertEquals(1, cardGame.getWinnerID(), "Player 1 should be the winner");

        // Ensure player2 thread is stopped
        assertFalse(playerThread2.isAlive(), "Player 2 thread should be stopped");
    }
    @Test
    public void testWriteDeckContentsToFile() throws InterruptedException {
        CardGame cardGame = new CardGame(2);
        CardDeck deck1 = cardGame.getCardDeckArrayList()[0];
        CardDeck deck2 = cardGame.getCardDeckArrayList()[1];


        deck1.discardCard(new Card(1));
        deck1.discardCard(new Card(1));
        deck1.discardCard(new Card(1));
        deck1.discardCard(new Card(1));

        deck2.discardCard(new Card(2));
        deck2.discardCard(new Card(2));
        deck2.discardCard(new Card(2));
        deck2.discardCard(new Card(2));


        cardGame.writeDeckContentsToFile();

        for (int i = 1; i <= numberOfPlayers; i++) {
            File file = new File("Deck" + i + ".txt");
            assertTrue(file.exists(), "Deck file should be created: Deck" + i + ".txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                assertNotNull(line, "Deck file should not be empty");
                assertTrue(line.startsWith("Deck " + i + " contents: "), "Deck file should contain the correct contents");
            } catch (IOException e) {
                e.printStackTrace();
                fail("Exception while reading deck file: " + e.getMessage());
            }
        }
    }

}