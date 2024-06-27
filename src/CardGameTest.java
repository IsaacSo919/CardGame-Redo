import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CardGameTest {
    @Test
    public void testInputNumberOfPlayers() {
        // Simulate user input
        String input = "4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        int numberOfPlayers = CardGame.getNumberOfPlayers();
        assertTrue(numberOfPlayers > 0, "The number of players should be greater than 0.");
    }
}