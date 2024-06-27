import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.Collections;

public class CardGame {
    private final int numberOfPlayers;
    private final ArrayList<Player> playerArrayList;
    private final ArrayList<CardDeck> cardDeckArrayList;

    public CardGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.playerArrayList = new ArrayList<>();
        this.cardDeckArrayList = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            playerArrayList.add(new Player(i));
            cardDeckArrayList.add(new CardDeck(i));
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Card Game!\n");
        int numberOfPlayers = getNumberOfPlayers();
        CardGame cardGame = new CardGame(numberOfPlayers);

        ArrayList<Card> cardsValues = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            for (int j = 1; j <= 4; j++) {// 1-4 are the face values
                cardsValues.add(new Card(i));// i will be the face value, j*i is the CardID
            }
        }
        System.out.println("Cards created!");

        System.out.println("Before shuffling: ");
        printAllCards(cardsValues);

        Collections.shuffle(cardsValues);

        // Print and write cards after shuffling
        writeCards(cardsValues, "cards.txt");

        // Read cards from the file
        ArrayList<Integer> tempCards = readCardsFromFile("cards.txt");
        System.out.println("Finish reading cards from file");

        // Set up the table
        System.out.println("Start creating player, discrete decks and give them hands");
        cardGame.distributeCards(tempCards);
        System.out.println(cardGame.playerArrayList);


    }

    public static int getNumberOfPlayers() {
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

    public static void printAllCards(ArrayList<Card> cards) {
        for (Card card : cards) {
            System.out.println(card.getFaceValue());
        }
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

    public void distributeCards(ArrayList<Integer> faceValues) {
        boolean deckvsPlayerFlag = true;
        int i = 0;
        for (Integer facevalue : faceValues) {
            if (deckvsPlayerFlag) {
                Card tempCard1 = new Card(facevalue);
                this.playerArrayList.get(i % this.playerArrayList.size()).addHand(tempCard1);
                i++;
                deckvsPlayerFlag = false;
            } else {
                Card tempCard2 = new Card(facevalue);
                this.cardDeckArrayList.get(i % playerArrayList.size()).addCard(tempCard2);
                i++;
            }
        }
    }


}
