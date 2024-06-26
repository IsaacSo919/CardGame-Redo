public class Player extends Thread {
    private final int playerID;

    public Player(int playerID) {
        playerID++;
        this.playerID = playerID;
    }


}
