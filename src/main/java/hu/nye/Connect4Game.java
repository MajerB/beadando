package hu.nye;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Connect4Game {
    private final GameBoard gameBoard;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;

    public Connect4Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.gameBoard = new GameBoard();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String filePath = "game_board.txt";

        while (true) {
            displayBoard();
            System.out.println("Press 0 to save the game, or choose a column (a,b,c,d,e,f,g): ");

            if (currentPlayer.equals(player1)) {
                // Player's turn
                String input = scanner.nextLine();
                if (input.equals("0")) {
                    try {
                        saveGameBoard(filePath);
                        System.out.println("Game saved successfully.");
                    } catch (IOException e) {
                        System.out.println("Failed to save the game.");
                    }
                    continue;
                }

                int col = input.charAt(0) - 'a';
                if (!gameBoard.playMove(col, currentPlayer.getToken())) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
            } else {
                // Computer's turn
                int col = generateComputerMove();
                if (gameBoard.playMove(col, currentPlayer.getToken())) {
                    System.out.println("Computer plays in column: " + (char) ('a' + col));
                } else {
                    System.out.println("Computer tried an invalid move. Re-trying...");
                    continue; // Retry move if computer chose an invalid column
                }
            }

            if (checkWin()) {
                displayBoard();
                System.out.println(currentPlayer.getName() + " wins!");

                // Clear the game board file
                try {
                    clearGameBoardFile(filePath);
                    System.out.println("Game board file cleared.");
                } catch (IOException e) {
                    System.out.println("Failed to clear the game board file.");
                }
                break;
            }

            if (checkDraw()) {
                displayBoard();
                System.out.println("It's a draw!");
                break;
            }

            switchPlayer();
        }
    }

    private void clearGameBoardFile(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (int i = 0; i < GameBoard.ROWS; i++) {
            for (int j = 0; j < GameBoard.COLS; j++) {
                writer.write('.');  // Write empty cell
            }
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer.equals(player1)) ? player2 : player1;
    }

    private boolean checkWin() {
        char[][] board = gameBoard.getBoard();
        char token = currentPlayer.getToken();

        // Check horizontal
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                if (board[row][col] == token &&
                        board[row][col + 1] == token &&
                        board[row][col + 2] == token &&
                        board[row][col + 3] == token) {
                    return true;
                }
            }
        }

        // Check vertical
        for (int col = 0; col < GameBoard.COLS; col++) {
            for (int row = 0; row < GameBoard.ROWS - 3; row++) {
                if (board[row][col] == token &&
                        board[row + 1][col] == token &&
                        board[row + 2][col] == token &&
                        board[row + 3][col] == token) {
                    return true;
                }
            }
        }

        // Check diagonal (bottom-left to top-right)
        for (int row = 3; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                if (board[row][col] == token &&
                        board[row - 1][col + 1] == token &&
                        board[row - 2][col + 2] == token &&
                        board[row - 3][col + 3] == token) {
                    return true;
                }
            }
        }

        // Check diagonal (top-left to bottom-right)
        for (int row = 0; row < GameBoard.ROWS - 3; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                if (board[row][col] == token &&
                        board[row + 1][col + 1] == token &&
                        board[row + 2][col + 2] == token &&
                        board[row + 3][col + 3] == token) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkDraw() {
        char[][] board = gameBoard.getBoard();
        // A draw occurs if there are no empty cells ('.') in the top row
        for (int col = 0; col < GameBoard.COLS; col++) {
            if (board[0][col] == '.') {
                return false;
            }
        }
        return true;
    }

    private int generateComputerMove() {
        Random rand = new Random();
        return rand.nextInt(GameBoard.COLS);
    }

    private void displayBoard() {
        char[][] board = gameBoard.getBoard();
        for (int i = 0; i < GameBoard.ROWS; i++) {
            for (int j = 0; j < GameBoard.COLS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void loadGameBoard(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        for (int i = 0; i < gameBoard.getBoard().length; i++) {
            String line = reader.readLine();
            for (int j = 0; j < gameBoard.getBoard()[i].length; j++) {
                gameBoard.getBoard()[i][j] = line.charAt(j);
            }
        }
        reader.close();
    }

    private void saveGameBoard(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (char[] row : gameBoard.getBoard()) {
            for (char cell : row) {
                writer.write(cell);
            }
            writer.newLine();
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter player 1 name: ");
        String player1Name = scanner.nextLine();
        Player player1 = new Player(player1Name, 'Y');
        Player player2 = new Player("Computer", 'R');

        Connect4Game game = new Connect4Game(player1, player2);
        String filePath = "game_board.txt";
        File file = new File(filePath);
        if (file.exists()) {
            game.loadGameBoard(filePath);
        }
        game.start();
        game.saveGameBoard(filePath);
    }
}
