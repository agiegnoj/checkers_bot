package boilerplate;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Board {

    private Set<Piece> botPieces;
    private Set<Piece> playerPieces;
    private char playerColor;
    private char botColor;
    final private Piece[][] board;
    Piece captureSequencePiece;

    public Board(char playerColor, char botColor) {

        botPieces = new HashSet<Piece>();
        playerPieces = new HashSet<>();
        board = new Piece[8][8];
        this.playerColor = playerColor;
        this.botColor = botColor;
        setUpBoard(playerColor);
        captureSequencePiece = null;

    }

    public Board(Piece[][] board, Set<Piece> playerPieces, Set<Piece> botPieces, char playerColor, char botColor,
            Piece capSeqPieceCopy) {
        this.board = board;
        this.playerPieces = playerPieces;
        this.botPieces = botPieces;
        this.playerColor = playerColor;
        this.botColor = botColor;
        this.captureSequencePiece = capSeqPieceCopy;
    }

    public boolean validMove(Piece currentPiece, int newX, int newY) {
        if (!inBounds(newX, newY))
            return false;

        if (currentPiece == null)
            return false;

        if (!currentPiece.isBotPiece() && !currentPiece.isKing() && (newX > currentPiece.getXCoordinate()))
            return false;

        if (currentPiece.isBotPiece() && !currentPiece.isKing() && (newX < currentPiece.getXCoordinate()))
            return false;

        int currX = currentPiece.getXCoordinate();
        int currY = currentPiece.getYCoordinate();
        int color = currentPiece.getColor();

        if (((captureSequencePiece == null ^ currentPiece.equals(captureSequencePiece))
                && Math.abs(newX - currentPiece.getXCoordinate()) == 2 // capture
                && Math.abs(newY - currentPiece.getYCoordinate()) == 2 && board[newX][newY] == null)) {

            int captX = (currX + newX) / 2;
            int captY = (currY + newY) / 2;
            if (inBounds(captX, captY) && board[newX][newY] == null && board[captX][captY] != null
                    && board[captX][captY].getColor() != color) {
                return true;
            }
        }

        if (captureSequencePiece == null && Math.abs(newX - currentPiece.getXCoordinate()) == 1 // normal move
                && Math.abs(newY - currentPiece.getYCoordinate()) == 1 && board[newX][newY] == null)

            return true;

        return false;
    }

    public void move(Piece currentPiece, int newX, int newY) {
        if (currentPiece == null) return;
        int currX = currentPiece.getXCoordinate();
        int currY = currentPiece.getYCoordinate();
        int color = currentPiece.getColor();
        if (Math.abs(newX - currentPiece.getXCoordinate()) == 2 // capture
                && Math.abs(newY - currentPiece.getYCoordinate()) == 2) {

            int captX = (currX + newX) / 2;
            int captY = (currY + newY) / 2;

            if (inBounds(captX, captY) && board[newX][newY] == null && board[captX][captY] != null
                    && board[captX][captY].getColor() != color) {
                captureHelper(currentPiece, captX, captY, newX, newY);
            }
        }

        else if (Math.abs(newX - currentPiece.getXCoordinate()) == 1 // normal move
                && Math.abs(newY - currentPiece.getYCoordinate()) == 1) {
            
            makeMove(currentPiece, newX, newY);

        }

    }
    
      

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(board);
        result = prime * result + Objects.hash(botColor, botPieces, captureSequencePiece, playerColor, playerPieces);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        return Arrays.deepEquals(board, other.board) && botColor == other.botColor
                && Objects.equals(botPieces, other.botPieces)
                && Objects.equals(captureSequencePiece, other.captureSequencePiece) && playerColor == other.playerColor
                && Objects.equals(playerPieces, other.playerPieces);
    }

    public boolean capturePossible(Piece currentPiece, boolean isBot) {
        int x = currentPiece.getXCoordinate();
        int y = currentPiece.getYCoordinate();
        char color = currentPiece.getColor();

        int[][] directions = currentPiece.isKing() ? new int[][] { { 2, 2 }, { 2, -2 }, { -2, 2 }, { -2, -2 } }
                : (isBot ? new int[][] { { 2, 2 }, { 2, -2 } } : new int[][] { { -2, 2 }, { -2, -2 } });

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (!inBounds(newX, newY)) {
                continue;
            } else if (capturePossibleHelper(x, y, newX, newY, color)) {
                return true;
            }
        }

        return false;
    }

    private boolean capturePossibleHelper(int x, int y, int newX, int newY, char color) {

        int middleX = (newX + x) / 2;
        int middleY = (newY + y) / 2;

        if (inBounds(middleX, middleY) && board[newX][newY] == null && board[middleX][middleY] != null
                && board[middleX][middleY].getColor() != color)
            return true;

        return false;
    }

    private boolean captureHelper(Piece currentPiece, int captX, int captY, int newX, int newY) {

        Piece toRemove = board[captX][captY];

        removePiece(toRemove);
        makeMove(currentPiece, newX, newY);

        if (capturePossible(currentPiece, currentPiece.isBotPiece())) {
            captureSequencePiece = currentPiece;
        } else {
            captureSequencePiece = null;
        }

        return true;
    }

    private void setUpBoard(char playerColor) {

        char botColor = playerColor == 'b' ? 'w' : 'b';

        Piece[] pieces = { new Piece(7, 0, playerColor, false), new Piece(7, 2, playerColor, false),
                new Piece(7, 4, playerColor, false), new Piece(7, 6, playerColor, false),
                new Piece(6, 1, playerColor, false), new Piece(6, 3, playerColor, false),
                new Piece(6, 5, playerColor, false), new Piece(6, 7, playerColor, false),
                new Piece(5, 0, playerColor, false), new Piece(5, 2, playerColor, false),
                new Piece(5, 4, playerColor, false), new Piece(5, 6, playerColor, false),
                new Piece(0, 1, botColor, true), new Piece(0, 3, botColor, true), new Piece(0, 5, botColor, true),
                new Piece(0, 7, botColor, true), new Piece(1, 0, botColor, true), new Piece(1, 2, botColor, true),
                new Piece(1, 4, botColor, true), new Piece(1, 6, botColor, true), new Piece(2, 1, botColor, true),
                new Piece(2, 3, botColor, true), new Piece(2, 5, botColor, true), new Piece(2, 7, botColor, true) };

        for (Piece p : pieces) {
            int x = p.getXCoordinate();
            int y = p.getYCoordinate();
            board[x][y] = p;
            if (!p.isBotPiece()) {
                playerPieces.add(p);
            } else {
                botPieces.add(p);
            }
        }
    }

    private void removePiece(Piece piece) {
        if (!piece.isBotPiece()) {
            playerPieces.remove(piece);

        } else {
            botPieces.remove(piece);
        }
        board[piece.getXCoordinate()][piece.getYCoordinate()] = null;
    }

    private void makeMove(Piece piece, int newX, int newY) {
        if ((newX == 7 && botColor == piece.getColor()) || (newX == 0 && piece.getColor() == playerColor)) // promote
            piece.promoteToKing();

        board[piece.getXCoordinate()][piece.getYCoordinate()] = null;
        piece.setXCoordinate(newX);
        piece.setYCoordinate(newY);
        board[newX][newY] = piece;
    }

    public Set<Piece> getBotPieces() {
        return botPieces;
    }

    public Set<Piece> getPlayerPieces() {
        return playerPieces;
    }

    public Piece getCaptureSequencePiece() {
        return captureSequencePiece;
    }

    public Piece getPiece(int x, int y) {

        return board[x][y];
    }

    public Piece[][] getBoard() {
        return board;
    }
    

    public Board getBoardDeepCopy() {
        Piece[][] copy = new Piece[8][8];
        Set<Piece> playerPiecesCopy = new HashSet<>();
        Set<Piece> botPiecesCopy = new HashSet<>();
        Piece capSeqPieceCopy = captureSequencePiece != null ? captureSequencePiece.copyOf() : null;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    Piece toAdd = board[i][j].copyOf();
                    copy[i][j] = toAdd;
                    if (toAdd.isBotPiece()) {
                        botPiecesCopy.add(toAdd);
                    } else {
                        playerPiecesCopy.add(toAdd);
                    }
                }
            }
        }

        Board boardDeepCopy = new Board(copy, playerPiecesCopy, botPiecesCopy, playerColor, botColor, capSeqPieceCopy);

        return boardDeepCopy;

    }

    public boolean inBounds(int x, int y) {
        return x < 8 && x >= 0 && y < 8 && y >= 0;
    }

    public int getGamePhase() {
        int totalPieces = botPieces.size()+playerPieces.size();
        if (totalPieces > 16) return 0; 
        else if (totalPieces > 8) return 1; 
        else return 2; 
    }




}
