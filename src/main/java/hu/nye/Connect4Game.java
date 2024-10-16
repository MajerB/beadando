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
        while (true) {
            displayBoard();
            if (currentPlayer.equals(player1)) {
                System.out.println(currentPlayer.getName() + ", enter a column (a,b,c,d,e,f,g): ");
                String colInput = scanner.nextLine();
                int col = colInput.charAt(0) - 'a';
                if (!gameBoard.playMove(col, currentPlayer.getToken())) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
            } else {
                int col = generateComputerMove();
                gameBoard.playMove(col, currentPlayer.getToken());
                System.out.println("Computer plays in column: " + (char) ('a' + col));
            }
            if (checkWin()) {
                displayBoard();
                System.out.println(currentPlayer.getName() + " wins!");
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

    private void switchPlayer() {
        currentPlayer = (currentPlayer.equals(player1)) ? player2 : player1;
    }

    private boolean checkWin() {
        // Implement winning logic here
        return false;
    }

    private boolean checkDraw() {
        // Implement draw logic here
        return false;
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
