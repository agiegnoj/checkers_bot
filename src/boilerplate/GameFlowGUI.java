package boilerplate;

import javax.swing.*;

import ai.AlphaBetaSearch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameFlowGUI {
    private Board board;
    private boolean playersTurn;
    private char playerColor;
    private char botColor;
    private AlphaBetaSearch alphaBeta;
    private JFrame frame;
    private JPanel panel;
    private JButton[][] buttons;
    private Piece selectedPiece;
    private JButton restartButton; 

    public static void main(String[] args) {
        new GameFlowGUI();
    }

    public GameFlowGUI() {
        alphaBeta = new AlphaBetaSearch(6); // default depth of 6 for solid casual games
        Random r = new Random();
        int num = r.nextInt(2);
        playerColor = (num == 1) ? 'b' : 'w';
        if (playerColor == 'b') {
            botColor = 'w';
            playersTurn = true;
        } else {
            botColor = 'b';
            playersTurn = false;
        }

        board = new Board(playerColor, botColor);
        
        frame = new JFrame("Checkers Bot");
        frame.setSize(800, 800);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9)); // Extra row for the restart button
        
        buttons = new JButton[8][8];
        

        initializeBoard();
        
        restartButton = new JButton("Restart");
        restartButton.setPreferredSize(new Dimension(170, 50));
        restartButton.setLocation(700, 350);
        
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(); 
            }
        });
        panel.add(restartButton); 
        
        frame.add(panel);
        frame.setVisible(true);

        if (!playersTurn) {
            botPlay();
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(80, 80));
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 30));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setEnabled(true);

                int x = i;
                int y = j;

                // Action listeners for player input
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handlePlayerMove(x, y);
                    }
                });

                panel.add(buttons[i][j]);
            }
        }
        updateBoardDisplay();
    }

    private void handlePlayerMove(int x, int y) {
        if (selectedPiece == null) {
            Piece piece = board.getPiece(x, y);
            if (piece != null && piece.getColor() == playerColor 
                    && (board.captureSequencePiece == null ^ piece.equals(board.captureSequencePiece))) {
                selectedPiece = piece;
                buttons[x][y].setBackground(Color.GREEN);
            }
        } else {
            Piece currentPiece = board.getPiece(selectedPiece.getXCoordinate(), selectedPiece.getYCoordinate());
            if (board.validMove(currentPiece, x, y)) {
                board.move(currentPiece, x, y);
                selectedPiece = null;
                updateBoardDisplay();

                if (board.captureSequencePiece == null) {
                    playersTurn = !playersTurn;
                }
                if (!playersTurn) {
                    botPlay();
                }
            } else {
                // Invalid move, deselect the piece
                selectedPiece = null;
                updateBoardDisplay();
            }
        }
    }

    private void updateBoardDisplay() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j].setBackground(Color.GRAY);
                buttons[i][j].setText(""); // Reset button text
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));

                Piece piece = board.getPiece(i, j);
                if (piece != null) {
                    if (piece.isKing()) {
                        buttons[i][j].setText("*");
                        buttons[i][j].setForeground(Color.GRAY);
                    }
                    if (piece.getColor() == 'w') {
                        buttons[i][j].setBackground(Color.WHITE);
                    } else {
                        buttons[i][j].setBackground(Color.BLACK);
                    }
                }
            }
        }
    }

    private void botPlay() {
        while (true) {
            int[] move = alphaBeta.getBestMove(board.getBoardDeepCopy());

            if (move != null) {
                Piece currentPiece = board.getPiece(move[0], move[1]);
                if (board.validMove(currentPiece, move[2], move[3])) {
                    board.move(currentPiece, move[2], move[3]);
                }else {
                    quickMove();
                }
            } else {
                
                  quickMove();
                
            }

            updateBoardDisplay();

            // Check if bot is in capture sequence
            if (board.getCaptureSequencePiece() == null) {
                break;
            }
        }
        playersTurn = true;
    }

    private void quickMove() {
        int[] move = alphaBeta.quickMove(board.getBoardDeepCopy());
        if (move != null) {
            Piece currentPiece = board.getPiece(move[0], move[1]);
            board.move(currentPiece, move[2], move[3]);
        }
    }

    private void restartGame() {
        // Reset the board state
        board = new Board(playerColor, botColor);
        selectedPiece = null;
        playersTurn = (playerColor == 'b'); 
        updateBoardDisplay();
        if (!playersTurn) {
            botPlay();
        }
    }
}

