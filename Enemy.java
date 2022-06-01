package demolition;

import processing.core.PImage;

/**
 * Represents a Bomb Guy that the player can control.
 */
public  abstract class Enemy extends Characters implements Turn {
    private int moveTimer;

    /**
     * Creates a new Enemy object.
     * @param x X-coordinate of the Enemy.
     * @param y Y-coordinate of the Enemy.
     * @param directionSprite the sprites for each directional movements of the Enemy.
     * @param map the map of the game level.
     */
    public Enemy(int x, int y, PImage[][] directionSprite, Map map) {
        super(x, y, directionSprite, map);
        this.moveTimer = 0;
    }

    /**
     * Represents a Bomb Guy that the player can control.
     */
    public abstract void turn();

    /**
     * Return the ordinal number for the direction.
     * @return the ordinal number for the direction.
     */
    public int getDirectionNum() {
        return this.directionNum;
    }

    /**
     * Set the ordinal number for the direction.
     * @param num The ordinal number for the direction
     */
    public void setDirectionNum(int num) {
        this.directionNum = num;

    }

    /**
     * Move the Enemy once every second.
     * Enemies cannot walk through broken or solid walls, and so it would turn.
     */
    public void move() {
        this.moveTimer ++;

        if (this.moveTimer <  App.FPS) {
            return;
        }

        this.moveTimer = 0;
        if (this.directionNum == Direction.UP.ordinal()) {
            if (checkCrash(Direction.UP.getX(), Direction.UP.getY())) {
                this.x += Direction.UP.getX();
                this.y += Direction.UP.getY();
            } else {
                turn();
            }
        } else if (this.directionNum == Direction.DOWN.ordinal()) {
            if (checkCrash(Direction.DOWN.getX(), Direction.DOWN.getY())) {
                this.x += Direction.DOWN.getX();
                this.y += Direction.DOWN.getY();
            } else {
                turn();
            }

        } else if (this.directionNum == Direction.RIGHT.ordinal()) {
            if (checkCrash(Direction.RIGHT.getX(), Direction.RIGHT.getY())) {
                this.x += Direction.RIGHT.getX();
                this.y += Direction.RIGHT.getY();
            } else {
                turn();
            }

        } else if (this.directionNum == Direction.LEFT.ordinal()) {
            if (checkCrash(Direction.LEFT.getX(), Direction.LEFT.getY())) {
                this.x += Direction.LEFT.getX();
                this.y += Direction.LEFT.getY();
            } else {
                turn();
            }
        }
    }
}



