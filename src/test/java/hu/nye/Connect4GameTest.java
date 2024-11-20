package hu.nye;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Connect4GameTest {

    private Connect4Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1", 'Y');
        player2 = new Player("Computer", 'R');
        game = new Connect4Game(player1, player2);
    }

    @Test
    void testCheckWinHorizontal() {
        GameBoard board = game.getGameBoard();
        char[][] grid = board.getBoard();

        grid[0][0] = 'Y';
        grid[0][1] = 'Y';
        grid[0][2] = 'Y';
        grid[0][3] = 'Y';

        assertTrue(game.checkWin());
    }

    @Test
    void testCheckWinVertical() {
        GameBoard board = game.getGameBoard();
        char[][] grid = board.getBoard();

        grid[0][0] = 'Y';
        grid[1][0] = 'Y';
        grid[2][0] = 'Y';
        grid[3][0] = 'Y';

        assertTrue(game.checkWin());
    }

    @Test
    void testCheckWinDiagonalBottomLeftToTopRight() {
        GameBoard board = game.getGameBoard();
        char[][] grid = board.getBoard();

        grid[3][0] = 'Y';
        grid[2][1] = 'Y';
        grid[1][2] = 'Y';
        grid[0][3] = 'Y';

        assertTrue(game.checkWin());
    }

    @Test
    void testCheckWinDiagonalTopLeftToBottomRight() {
        GameBoard board = game.getGameBoard();
        char[][] grid = board.getBoard();

        grid[0][0] = 'Y';
        grid[1][1] = 'Y';
        grid[2][2] = 'Y';
        grid[3][3] = 'Y';

        assertTrue(game.checkWin());
    }

    @Test
    void testCheckDraw() {
        GameBoard board = game.getGameBoard();
        char[][] grid = board.getBoard();

        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                grid[row][col] = (row + col) % 2 == 0 ? 'Y' : 'R';
            }
        }

        assertTrue(game.checkDraw());
    }

    @Test
    void testSaveAndLoadGameBoard() throws IOException {
        String filePath = "test_game_board.txt";
        GameBoard board = game.getGameBoard();
        char[][] grid = board.getBoard();

        grid[0][0] = 'Y';
        grid[1][1] = 'R';
        game.saveGameBoard(filePath);

        Connect4Game loadedGame = new Connect4Game(player1, player2);
        loadedGame.loadGameBoard(filePath);

        char[][] loadedGrid = loadedGame.getGameBoard().getBoard();
        assertEquals('Y', loadedGrid[0][0]);
        assertEquals('R', loadedGrid[1][1]);
        assertEquals('.', loadedGrid[2][2]);

        new File(filePath).delete();
    }

    @Test
    public void testUpdateHighScores() throws IOException {
        Connect4Game gameSpy = Mockito.spy(game);
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Laking", 2);
        doReturn(scores).when(gameSpy).loadHighScores();

        gameSpy.updateHighScores("Laking");
        verify(gameSpy).saveHighScores(anyMap());
        Assertions.assertEquals(3, scores.get("Laking")); // Updated count
    }

    @Test
    void testGenerateComputerMove() {
        int col = game.generateComputerMove();
        assertTrue(col >= 0 && col < GameBoard.COLS, "Computer move should be within valid column range.");
    }

    @Test
    public void testSwitchPlayer() {
        game.switchPlayer();
        Assertions.assertEquals(player2, game.getCurrentPlayer());
        game.switchPlayer();
        Assertions.assertEquals(player1, game.getCurrentPlayer());
    }


    @Test
    public void testShowHighScores() throws IOException {
        File file = new File("highscores.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Laking: 5\n");
            writer.write("Computer: 3\n");
        }
        game.showHighScores();
    }

    @Test
    public void testLoadHighScores() {
        Map<String, Integer> highScores = game.loadHighScores();
        Assertions.assertNotNull(highScores);
        assertTrue(highScores.isEmpty() || highScores.containsKey("Laking"));
    }

    @Test
    public void testLoadGameBoardFileNotFound() {
        File file = new File("nonexistent_game_board.txt");

        assertThrows(IOException.class, () -> game.loadGameBoard(file.getPath()), "IOException should be thrown when the file does not exist.");
    }

    @Test
    public void testShowHighScoresEmptyFile() throws IOException {
        File file = new File("empty_highscores.txt");
        file.deleteOnExit();


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

        }

        game.showHighScores();
    }

    @Test
    public void testCheckDrawWhenBoardIsFull() {
        GameBoard board = game.getGameBoard();


        for (int col = 0; col < GameBoard.COLS; col++) {
            for (int row = 0; row < GameBoard.ROWS; row++) {
                board.playMove(col, (col + row) % 2 == 0 ? 'Y' : 'R');
            }
        }

        assertTrue(game.checkDraw(), "The game should end in a draw when the board is full and no player has won.");
    }

}
