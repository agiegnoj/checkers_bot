package ai;


import boilerplate.Board;
import boilerplate.Move;
import boilerplate.Piece;

public class AlphaBetaSearch {
    private BoardEvaluation be;
    private final int depth;
   
   

    public AlphaBetaSearch(int depth) {
        be = new BoardEvaluation();
        this.depth = depth;
        
        
    }
    
    public int[] getBestMove(Board board) {
        
        
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;
        
           
        for (Piece p : board.getBotPieces()) {
            
            int x = p.getXCoordinate();
            int y = p.getYCoordinate();

           int[][] newCoordinates = p.isKing() == true ?
                        new int [][]{ { x + 2, y + 2 }, { x + 2, y - 2 }, { x - 2, y + 2 }, { x - 2, y - 2 },
                        { x + 1, y + 1 }, { x + 1, y - 1 }, { x - 1, y + 1 }, { x - 1, y - 1 },}:
                        new int[][]{ { x + 1, y + 1 }, { x + 1, y - 1 }, { x + 2, y + 2 }, { x + 2, y - 2 } };   ;
                
                for (int[] coord : newCoordinates) {
                           
                    int newX = coord[0];
                    int newY = coord[1];
                    
                    if (newX < 0 || newX >= 8 || newY < 0 || newY >= 8) {
                        continue;
                    }
                    
                    Board boardCopy = board.getBoardDeepCopy();
                    
                   
                    if (boardCopy.validMove(p, newX, newY)) {
                        boardCopy.move(p, newX, newY);
                        Move move = new Move(x, y, newX, newY);
                        
                        int value = boardCopy.getCaptureSequencePiece() == null ?
                             alphaBeta(boardCopy, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, false)
                             : alphaBeta(boardCopy, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                            
                        if (value >= bestValue) {                   
                            bestValue = value;
                            bestMove = move;
                        }                       
                    }
                }       
        }
        
        
        return bestMove != null ? bestMove.getMoveArray() : null;
    }


    private int alphaBeta(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
       // System.out.println("alphaBeta called with depth: " + depth + ", maximizingPlayer: " + maximizingPlayer);

        if (depth == 0)
            return be.evaluate(board);
           
        
        if (maximizingPlayer) { 
            int value = Integer.MIN_VALUE;
            for (Piece p : board.getBotPieces()) {

                int x = p.getXCoordinate();
                int y = p.getYCoordinate();
                
                
                int[][] newCoordinates = p.isKing()?
                        new int [][]{ { x + 2, y + 2 }, { x + 2, y - 2 }, { x - 2, y + 2 }, { x - 2, y - 2 },
                    { x + 1, y + 1 }, { x + 1, y - 1 }, { x - 1, y + 1 }, { x - 1, y - 1 },}:
                    new int[][]{ { x + 1, y + 1 }, { x + 1, y - 1 }, { x + 2, y + 2 }, { x + 2, y - 2 }};  ;

                    for (int[] coord : newCoordinates) {
                        
                        int newX = coord[0];
                        int newY = coord[1];
                        
                        if (newX < 0 || newX >= 8 || newY < 0 || newY >= 8) {
                            continue;
                        }
                        
                        Board boardCopy = board.getBoardDeepCopy();
                                    
                        if (boardCopy.validMove(p, newX, newY)) {
                            boardCopy.move(p, newX, newY);
                            if (board.getCaptureSequencePiece() == null) {
                                value = Math.max(value, alphaBeta(boardCopy, depth - 1, alpha, beta, false));
                            }else {
                                value = Math.max(value, alphaBeta(boardCopy, depth -1 , alpha, beta, true));
                            }
                            
                            if (value > beta)
                                break; // cutoff
                            alpha = Math.max(value, alpha);

                        }
                    }
                
            }
            return value;
        } else { 
            int value = Integer.MAX_VALUE;
            
            for (Piece p : board.getPlayerPieces()) {

                int x = p.getXCoordinate();
                int y = p.getYCoordinate();

                    int[][] newCoordinates = p.isKing() ?
                            new int [][]{ { x + 2, y + 2 }, { x + 2, y - 2 }, { x - 2, y + 2 }, { x - 2, y - 2 },
                        { x + 1, y + 1 }, { x + 1, y - 1 }, { x - 1, y + 1 }, { x - 1, y - 1 },}:
                        new int[][]{ { x - 1, y + 1 }, { x - 1, y - 1 }, { x - 2, y + 2 }, { x - 2, y - 2 }, };;
                            
                    for (int[] coord : newCoordinates) {
                        
                        int newX = coord[0];
                        int newY = coord[1];
                        
                        if (newX < 0 || newX >= 8 || newY < 0 || newY >= 8) {
                            continue;
                        }
                        Board boardCopy = board.getBoardDeepCopy();
                        
                        if (boardCopy.validMove(p, newX, newY)) {
                            boardCopy.move(p, newX, newY);
                            
                            if (boardCopy.getCaptureSequencePiece() == null) {
                                value = Math.min(value, alphaBeta(boardCopy, depth - 1, alpha, beta, true));
                            }else {
                                value = Math.min(value, alphaBeta(boardCopy, depth -1 , alpha, beta, false));
                            } 
                            
    
                            if (value < alpha)
                                break; // cutoff
                            beta = Math.min(value, beta);

                          
                        }
                    }            

            }
            return value;

        }
    }

}
