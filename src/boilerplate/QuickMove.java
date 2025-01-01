package boilerplate;

public class QuickMove {

    int[] getQuickMove(Board board) {
        Move fallback = null;
        for (Piece p : board.getBotPieces()) {
            int x = p.getXCoordinate();
            int y = p.getYCoordinate();

            int[][] directions = p.isKing() ? new int[][] { { 2, 2 }, { 2, -2 }, { -2, 2 }, { -2, -2 } }
                    : new int[][] { { 2, 2 }, { 2, -2 } };

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (!board.validMove(p, newX, newY)) {
                    continue;
                } else {
                    return new int[]{
                        x, y, newX, newY
                    };
                }
            }
           
        }
        
        for (Piece p : board.getBotPieces()) {
            int x = p.getXCoordinate();
            int y = p.getYCoordinate();
           

            int[][] directions = p.isKing() ? new int[][] { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } }
                    : new int[][] { { 1, 1 }, { 1, -1 } };

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (!board.validMove(p, newX, newY)) {
                    continue;
                } else {
                    return new int[]{
                            x, y, newX, newY
                        };                 
                }
            }
           
        }
        return  null;
    }

}
