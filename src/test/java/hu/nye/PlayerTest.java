package hu.nye;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void testPlayerCreation() {
        Player player = new Player("Alice", 'Y');
        assertEquals("Alice", player.getName());
        assertEquals('Y', player.getToken());
    }

    @Test
    public void testEquals() {
        Player player1 = new Player("Alice", 'Y');
        Player player2 = new Player("Alice", 'Y');
        Player player3 = new Player("Bob", 'R');
        assertTrue(player1.equals(player2));
        assertFalse(player1.equals(player3));
    }

    @Test
    public void testHashCode() {
        Player player1 = new Player("Alice", 'Y');
        Player player2 = new Player("Alice", 'Y');
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    public void testToString() {
        Player player = new Player("Alice", 'Y');
        String expected = "Player{name='Alice', token=Y}";
        assertEquals(expected, player.toString());
    }
}
