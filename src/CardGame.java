import java.io.*;
import java.util.*;

public class CardGame {
    private final int numberOfPlayers;
    private final List<Player> playerArrayList;
    private final List<CardDeck> cardDeckArrayList;
    private final List<Thread> playerThreads = new LinkedList<>();
    private boolean gameWon = false;
    private int winnerID;

    public CardGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.playerArrayList = new LinkedList<>();
        this.cardDeckArrayList = new LinkedList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            playerArrayList.add(new Player(i+1, this));
            cardDeckArrayList.add(new CardDeck(i+1));
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Card Game!\n");
        int numberOfPlayers = scanNumberOfPlayers();
        CardGame cardGame = new CardGame(numberOfPlayers);
        //Card Pack generator
        //createCardPack(numberOfPlayers);
        // Read userinput for cardPack file
        ArrayList<Integer> tempCards = handleCardPackFile(numberOfPlayers);
        // Set up the table
        cardGame.distributeCards(tempCards);
        cardGame.startGame();
    }



    private static void createCardPack(int numberOfPlayers) {
        numberOfPlayers = 5;
        ArrayList<Card> cardsValues = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            for (int j = 1; j <= 8; j++) {// 1-4 are the face values
                cardsValues.add(new Card(i));// i will be the face value, j*i is the CardID
            }
        }
        System.out.println("Cards created!");
        Collections.shuffle(cardsValues);
        writeCards(cardsValues, "five.txt");
    }

    public synchronized int getWinnerID() {
        return winnerID;
    }
    public synchronized boolean isGameWon() {
        return gameWon;
    }
    public synchronized void setGameWon(int playerID) { // being called in if(checkWin()) in Player.java
        this.gameWon = true;
        this.winnerID = playerID;
        System.out.println("---------------------\nPrint winner and other players message to txt file.\nStop the game.");

        notifyAll(); // Notify all waiting threads that the game has been won
    }
    private void deletePreviousFiles() {
        for (int i = 1; i <= 100; i++) {
            File file = new File("player" + i + ".txt");
            if (file.exists()) {
                file.delete();
            }
        }
    }
    private void startGame() {
        System.out.println("-------------------------------\nStart the game!");
        deletePreviousFiles();
        for(Player player : playerArrayList){
            Thread playerThread = new Thread(player);
            playerThreads.add(playerThread);
            playerThread.start();
        }
    }
    public void stopGame() {
        System.out.println("Stop the game, interrupt all threads");
        for (Thread playerThread : playerThreads) {
            playerThread.interrupt();
        }
    }
    public void distributeCards(ArrayList<Integer> faceValues) {
        System.out.println("Start creating player, discrete decks and give them hands");
        Collections.shuffle(faceValues);
        int faceValueCardCount = 0;
        for (int roundCount = 0; roundCount < 4; roundCount++) { // nested for loop 4 rounds
            for (int i = 0; i < numberOfPlayers; i++) {//nested for loop for each player(and decks)
                playerArrayList.get(i).addHand(new Card(faceValues.get(faceValueCardCount)));
                faceValueCardCount++;
                cardDeckArrayList.get(i).addDeck(new Card(faceValues.get(faceValueCardCount)));
                faceValueCardCount++;
            }
        }
        System.out.println("Finish distributing cards");


        //----------------------Print out the hands and decks (intialize))----------------------
        for (Player player : playerArrayList) {
            System.out.print("player " + player.getPlayerID() + " has hand: ");
            for (Card card : player.getHand()) {
                System.out.print(card.getFaceValue() + " ");
            }
            System.out.print("\n");
        }
        for (CardDeck cardDeck : cardDeckArrayList) {
            System.out.print("Deck " + cardDeck.getDeckID() + " has deck: ");
            for (Card card : cardDeck.getCardDeck()) {
                System.out.print(card.getFaceValue() + " ");
            }
            System.out.print("\n");
        }

    }
    public static ArrayList<Integer> readCardsFromFile(String filename) {
        ArrayList<Integer> cardValues = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int cardValue = Integer.parseInt(line); // Parse each line as an integer
                cardValues.add(cardValue);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return cardValues;
    }
    public static void writeCards(ArrayList<Card> cards, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
            for (Card card : cards) {
                writer.write(card.getFaceValue() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int scanNumberOfPlayers() {
        int numberOfPlayers = 0;
        //Create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        //Prompt the user to enter the number of players
        while (!validInput) {
            System.out.println("Please enter the number of players: ");
            if (scanner.hasNextInt()) {
                numberOfPlayers = scanner.nextInt();
                if (numberOfPlayers > 0) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a number greater than 0.");
                }
            } else {
                System.out.println("Invalid input. Please enter a integer.");
                scanner.next();
            }
        }
        return numberOfPlayers;
    }

    public static ArrayList<Integer> handleCardPackFile(int numberOfPlayers) {
        boolean validInput = false;
        //Create a scanner object to read user input
        //Prompt the user to enter the number of players
        while (!validInput) {
            System.out.println("Please enter location of the pack to load:");
            Scanner scanner = new Scanner(System.in);
            String fileName = scanner.nextLine();
            if (checkFileExist(fileName)) {
                if (readCardsFromFile(fileName).size() == numberOfPlayers * 8) {
                    System.out.println("Finish reading cards from file");
                    return (readCardsFromFile(fileName));
                } else {
                    System.out.println("Invalid input. Please enter a file with " + numberOfPlayers * 8 + " cards.");
                }
            }

        }
        return null;
    }
    public static boolean checkFileExist(String fileName){
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            System.out.println("File does not exist. Please enter a valid file name.");
            return false;
        }
    }





//-------------------------------testing-------------------------------
    public int getNumberOfPlayers() {
    return numberOfPlayers;
    }
    public static void printAllCards(ArrayList<Card> cards) {
        for (Card card : cards) {
            System.out.println(card.getFaceValue());
        }
    }

    public Player[] getPlayerArrayList() {
        return playerArrayList.toArray(new Player[0]);
    }

    public CardDeck[] getCardDeckArrayList() {
        return cardDeckArrayList.toArray(new CardDeck[0]);
    }
    public CardDeck getDeck(int deckID) {
        return cardDeckArrayList.get(deckID % numberOfPlayers); //this prevents the out of bound exception,the last deck will be the same as the first deck
    }
}
