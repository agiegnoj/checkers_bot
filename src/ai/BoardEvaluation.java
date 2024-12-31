package ai;

import boilerplate.Board;
import boilerplate.Piece;

public class BoardEvaluation {
    
    public int evaluate(Board board) {
        int eval = 0;
        
        int[] heuristicBalances = getHeuristicBalances(board); 
        // piece balance, king balance, center control balance, unvulnerability, piece mobility (bot perspective)
        int [] heuristicWeights = getDynamicWeights(board);
        
        for (int i = 0; i< 5; i++) {
            eval += heuristicBalances[i] * heuristicWeights[i];
        }
          
        return eval;
    }
    

    private int[] getHeuristicBalances(Board board) {
               
        int pieceBalance = 0;
        int kingBalance = 0;
        int centerBalance = 0;
        int unvulnerability = 0;
        int pieceMobilityBalance = 0;
        
        for (Piece p : board.getBotPieces()) {
            pieceBalance++;
            if (p.isKing()) kingBalance++;
            if (p.getXCoordinate ()>= 2 && p.getXCoordinate() <= 5 
                && p.getYCoordinate() >= 2 && p.getYCoordinate() <= 5 )
                centerBalance++;
            pieceMobilityBalance += countLegalMoves(p, board);
                    
        }
        
        for (Piece p : board.getPlayerPieces()) {
            pieceBalance--;
            if (p.isKing()) kingBalance--;
            if (p.getXCoordinate ()>= 2 && p.getXCoordinate() <= 5 
                && p.getYCoordinate() >= 2 && p.getYCoordinate() <= 5 )
                centerBalance--;  
            if (board.capturePossible(p, false)) {
                unvulnerability--;}
            else {
                unvulnerability++;
        }  
            pieceMobilityBalance -= countLegalMoves(p, board);
            
    }
        int[] heurBal = {
                pieceBalance, kingBalance, centerBalance, unvulnerability, 
                pieceMobilityBalance
            };
            
            return heurBal;
    
   
    }
    
    private int[] getDynamicWeights(Board board) {
        int phase = board.getGamePhase();
        if (phase == 0) { // opening
            return new int[] {2, 3, 5, 2, 3};
        } else if (phase == 1) { // midgame
            return new int[] {3, 4, 4, 3, 2};
        } else { // endgame
            return new int[] {4, 5, 2, 3, 1};
        }
    }


    private int countLegalMoves(Piece p, Board board) {
        int x = p.getXCoordinate();
        int y = p.getYCoordinate();
        
        int count = 0;

        int[][] directions = p.isKing()?
                new int [][]{ { x + 2, y + 2 }, { x + 2, y - 2 }, { x - 2, y + 2 }, { x - 2, y - 2 },
            { x + 1, y + 1 }, { x + 1, y - 1 }, { x - 1, y + 1 }, { x - 1, y - 1 },}:
            p.isBotPiece() ? new int[][]{ { x + 1, y + 1 }, { x + 1, y - 1 }, { x + 2, y + 2 }, { x + 2, y - 2 }}
            :  new int[][]{ { x - 1, y + 1 }, { x - 1, y - 1 }, { x - 2, y + 2 }, { x - 2, y - 2 }};;

        for (int[] dir : directions) {
            int newX = dir[0];
            int newY = dir[1];
            
            if (!board.inBounds(newX, newY) )
                continue;

            if (board.validMove(p, newX, newY)) {
                count++;
            }
        }
        
        return count;

    }
    
    
       

}
