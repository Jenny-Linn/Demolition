package demolition;

/**
 * Represents the four directions.
 */
public enum Direction {
    LEFT (-1,0),
    UP(0,-1),
    RIGHT(1, 0),
    DOWN(0,1);

    private int x;
    private int y;

    /**
     * Creates a Direction object.
     * @param x Change in X value associated with the direction.
     * @param y Change in Y value associated with the direction.
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Return the change in X value associated with the direction.
     * @return the change in X value.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Return the change in Y value associated with the direction.
     * @return the change in Y value.
     */
    public int getY() {
        return this.y;
    }

}
