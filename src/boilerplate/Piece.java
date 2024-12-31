package boilerplate;

import java.util.Objects;

public class Piece {
    private boolean isKing;
    private char color;
    private int xCoordinate;
    private int yCoordinate;
    private boolean isBotPiece;
    private Move recaptureMove;
    
    
    public Piece(int x, int y, char color, boolean isBotPiece){
        this.xCoordinate = x;
        this.yCoordinate = y;      
        this.color = color;
        this.isBotPiece = isBotPiece;
        
    }
    
    public Piece(int x, int y, char color, boolean isBotPiece, Move recapMove){
        this.xCoordinate = x;
        this.yCoordinate = y;      
        this.color = color;
        this.isBotPiece = isBotPiece;
        this.recaptureMove = recapMove;
        
    }
   
    
    public void promoteToKing() {
        isKing = true;
    }
    
    public boolean isKing() {
        return isKing;
    }
    
    public String toString() {
        if (isKing && color == 'b') {
            return "kb";
        }else if (isKing) {
            return "kw";
        }else if (color == 'b') {
            return "mb";
        }
        return "mw";
         
    }
    
    public int  getXCoordinate() {
        return xCoordinate;  
    }
    
    public int  getYCoordinate() {
        return yCoordinate;  
    }
    
    public void setXCoordinate(int x) {
        xCoordinate = x;
    }
    
    public void setYCoordinate(int y) {
        yCoordinate = y;
    }
    
    public char getColor() {
        return color;
    }
    
    public boolean isBotPiece() {
        return isBotPiece;
    }
    
    public void setRecaptureMove(int x, int y) {
        recaptureMove = new Move(xCoordinate, yCoordinate, x, y);
    }
    
    public int[] getRecapCoordinates() {
        return recaptureMove.getMoveArray();
    }
    
    
    public Piece copyOf() {
        Piece copiedPiece = new Piece(this.xCoordinate, this.yCoordinate, this.color, this.isBotPiece, recaptureMove);
        if (this.isKing) {
            copiedPiece.promoteToKing();
        }
        return copiedPiece;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate, color);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece piece = (Piece) obj;
        return xCoordinate == piece.xCoordinate && yCoordinate == piece.yCoordinate && color == piece.color;
    }
    
    

}
