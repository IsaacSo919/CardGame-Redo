import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.Collections;

public class CardGame {
    private final int numberOfPlayers;
    private ArrayList<Player> playerArrayList;

    public CardGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.playerArrayList = new ArrayList<>();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Card Game!\n");
        int numberOfPlayers = getNumberOfPlayers();


        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            for (int j = 1; j <= 4; j++) {// 1-4 are the face values
                cards.add(new Card(i));// i is the face value, j*i is the CardID
            }
        }
        System.out.println("Cards created!");

        System.out.println("Before shuffling: ");
        printAllCards(cards);

        Collections.shuffle(cards);

        // Print and write cards after shuffling
        printCards(cards,  "cards.txt");

        // Read cards from the file
        ArrayList<Integer> cardValues = readCardsFromFile("cards.txt");
        System.out.println("Read card values from file: " + cardValues);


    }

    public static int getNumberOfPlayers() {
        //Create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        //Prompt the user to enter the number of players
        System.out.println("Please enter the number of players: ");
        int numberOfPlayers = scanner.nextInt();
        return numberOfPlayers;
    }
    private static void printAllCards(ArrayList<Card> cards) {
        for (Card card : cards) {
            System.out.println( card.getFaceValue());
        }
    }
    private static void printCards(ArrayList<Card> cards, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,false));
            for (Card card : cards) {
                writer.write( card.getFaceValue() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static ArrayList<Integer> readCardsFromFile(String filename) {
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
}
