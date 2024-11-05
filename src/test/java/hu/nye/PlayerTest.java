package hu.nye;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void testPlayerCreation() {
        Player player = new Player("Laking", 'Y');
        assertEquals("Laking", player.getName());
        assertEquals('Y', player.getToken());
    }

    @Test
    public void testEquals() {
        Player player1 = new Player("Laking", 'Y');
        Player player2 = new Player("Laking", 'Y');
        Player player3 = new Player("Balazs", 'R');

        assertEquals(player1, player2);
        assertNotEquals(player1, player3);
        assertNotEquals(player1, null); // Test comparison with null
        assertNotEquals(player1, "Not a player object"); // Test comparison with different object type
    }

    @Test
    public void testHashCode() {
        Player player1 = new Player("Laking", 'Y');
        Player player2 = new Player("Laking", 'Y');
        Player player3 = new Player("Balazs", 'R');

        assertEquals(player1.hashCode(), player2.hashCode());
        assertNotEquals(player1.hashCode(), player3.hashCode());
    }

    @Test
    public void testToString() {
        Player player = new Player("Laking", 'Y');
        String expected = "Player{name='Laking', token=Y}";
        assertEquals(expected, player.toString());
    }
}