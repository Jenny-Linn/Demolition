package demolition;

import processing.core.PImage;

/**
 * Represents a Yellow Enemy.
 */
public class YellowEnemy extends Enemy {

    /**
     * Creates a new Yellow Enemy object.
     * @param x X-coordinate of the Enemy.
     * @param y Y-coordinate of the Enemy.
     * @param directionSprite the sprites for each directional movements of the Enemy.
     * @param map the map of the game level.
     */
    public YellowEnemy(int x, int y, PImage[][] directionSprite, Map map) {
        super(x, y, directionSprite, map);
    }

    /**
     * Make a clockwise decision on collision.
     */
    public void turn() {
        this.directionNum = (this.directionNum + 1)%4;
    }

}
