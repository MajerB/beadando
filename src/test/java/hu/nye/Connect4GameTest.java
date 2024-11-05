package hu.nye;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Connect4GameTest {
    private Connect4Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player1 = new Player("Laking", 'Y');
        player2 = new Player("Computer", 'R');
        game = new Connect4Game(player1, player2);
    }

    @Test
    public void testSwitchPlayer() {
        game.switchPlayer();
        assertEquals(player2, game.getCurrentPlayer());
        game.switchPlayer();
        assertEquals(player1, game.getCurrentPlayer());
    }

    @Test
    public void testCheckWinHorizontal() {
        GameBoard board = game.getGameBoard();
        board.playMove(0, 'Y');
        board.playMove(1, 'Y');
        board.playMove(2, 'Y');
        board.playMove(3, 'Y');
        assertTrue(game.checkWin());
    }

    @Test
    public void testCheckWinVertical() {
        GameBoard board = game.getGameBoard();
        board.playMove(0, 'Y');
        board.playMove(0, 'Y');
        board.playMove(0, 'Y');
        board.playMove(0, 'Y');
        assertTrue(game.checkWin());
    }

    @Test
    public void testCheckWinDiagonal() {
        GameBoard board = game.getGameBoard();
        board.playMove(0, 'Y');
        board.playMove(1, '.');
        board.playMove(1, 'Y');
        board.playMove(2, '.');
        board.playMove(2, '.');
        board.playMove(2, 'Y');
        board.playMove(3, '.');
        board.playMove(3, '.');
        board.playMove(3, '.');
        board.playMove(3, 'Y');
        assertTrue(game.checkWin());
    }

    @Test
    public void testCheckDraw() {
        GameBoard board = game.getGameBoard();
        for (int i = 0; i < GameBoard.COLS; i++) {
            for (int j = 0; j < GameBoard.ROWS; j++) {
                board.playMove(i, (i + j) % 2 == 0 ? 'Y' : 'R');
            }
        }
        assertTrue(game.checkDraw());
    }

    @Test
    public void testGenerateComputerMove() {
        int col = game.generateComputerMove();
        assertTrue(col >= 0 && col < GameBoard.COLS);
    }

    @Test
    public void testUpdateHighScores() throws IOException {
        Connect4Game gameSpy = Mockito.spy(game);
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Laking", 2);
        doReturn(scores).when(gameSpy).loadHighScores();

        gameSpy.updateHighScores("Laking");
        verify(gameSpy).saveHighScores(anyMap());
        assertEquals(3, scores.get("Laking")); // Updated count
    }

    @Test
    public void testSaveGameBoard() throws IOException {
        File file = new File("test_game_board.txt");
        file.deleteOnExit(); // Clean up after test

        // Make a move in the first column (column index 0)
        game.getGameBoard().playMove(0, 'Y');
        game.saveGameBoard(file.getPath());

        // Read the saved file and check the last row for the 'Y' token
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            for (int i = 0; i < GameBoard.ROWS - 1; i++) {
                reader.readLine(); // Skip to the last row
            }
            line = reader.readLine(); // Read the last row where the token should be
            assertEquals("Y......", line); // Check the last row after move
        }
    }

    @Test
    public void testLoadGameBoard() throws IOException {
        File file = new File("test_game_board.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Y......\n");
            for (int i = 1; i < GameBoard.ROWS; i++) {
                writer.write(".......\n");
            }
        }
        game.loadGameBoard(file.getPath());
        assertEquals('Y', game.getGameBoard().getBoard()[0][0]);
    }
}