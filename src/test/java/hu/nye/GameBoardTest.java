package hu.nye;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GameBoardTest {
    private GameBoard gameBoard;

    @BeforeEach
    public void setUp() {
        gameBoard = new GameBoard();
    }

    @Test
    public void testPlayMoveValid() {
        assertTrue(gameBoard.playMove(0, 'Y'));
        assertEquals('Y', gameBoard.getBoard()[GameBoard.ROWS - 1][0]);
    }

    @Test
    public void testPlayMoveInvalid() {
        for (int i = 0; i < GameBoard.ROWS; i++) {
            gameBoard.playMove(0, 'Y');
        }
        assertFalse(gameBoard.playMove(0, 'R'));
    }

    @Test
    public void testEquals() {
        GameBoard otherBoard = new GameBoard();
        assertEquals(gameBoard, otherBoard);
        gameBoard.playMove(0, 'Y');
        assertNotEquals(gameBoard, otherBoard);
    }

    @Test
    public void testHashCode() {
        GameBoard otherBoard = new GameBoard();
        assertEquals(gameBoard.hashCode(), otherBoard.hashCode());
    }

    @Test
    public void testToString() {
        gameBoard.playMove(0, 'Y');
        gameBoard.playMove(1, 'R');
        String expected = "GameBoard {\n" +
                "[., ., ., ., ., ., .]\n" +
                "[., ., ., ., ., ., .]\n" +
                "[., ., ., ., ., ., .]\n" +
                "[., ., ., ., ., ., .]\n" +
                "[., ., ., ., ., ., .]\n" +
                "[Y, R, ., ., ., ., .]\n" +
                "}";
        assertEquals(expected, gameBoard.toString());
    }
}
