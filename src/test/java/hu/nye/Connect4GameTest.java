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
        Assertions.assertEquals(player2, game.getCurrentPlayer());
        game.switchPlayer();
        Assertions.assertEquals(player1, game.getCurrentPlayer());
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
        // Fill the board with alternating tokens, no winner
        for (int col = 0; col < GameBoard.COLS; col++) {
            for (int row = 0; row < GameBoard.ROWS; row++) {
                board.playMove(col, (col + row) % 2 == 0 ? 'Y' : 'R');
            }
        }
        assertTrue(game.checkDraw()); // Board is full, no winner, should return true for draw
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
        Assertions.assertEquals(3, scores.get("Laking")); // Updated count
    }

    @Test
    public void testSaveGameBoard() throws IOException {
        File file = new File("test_game_board.txt");
        file.deleteOnExit();


        game.getGameBoard().playMove(0, 'Y');
        game.saveGameBoard(file.getPath());


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            for (int i = 0; i < GameBoard.ROWS - 1; i++) {
                reader.readLine();
            }
            line = reader.readLine();
            Assertions.assertEquals("Y......", line);
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
        Assertions.assertEquals('Y', game.getGameBoard().getBoard()[0][0]);
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