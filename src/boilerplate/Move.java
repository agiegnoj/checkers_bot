package boilerplate;

import java.util.Objects;

public class Move {
    private int currX, currY, newX, newY;

    public Move(int currX, int currY, int newX, int newY) {
        this.currX = currX;
        this.currY = currY;
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move)) return false;
        Move other = (Move) obj;
        return currX == other.currX && currY == other.currY &&
               newX == other.newX && newY == other.newY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currX, currY, newX, newY);
    }

    public int[] getMoveArray() {
        
        return new int []{
            currX, currY, newX, newY
        };
    }
}
