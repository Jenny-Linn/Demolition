package demolition;

import processing.core.PImage;

import java.util.Random;

/**
 * Represents a Red Enemy.
 */
public class RedEnemy extends Enemy{

    /**
     * Creates a new Red Enemy object.
     * @param x X-coordinate of the Enemy.
     * @param y Y-coordinate of the Enemy.
     * @param directionSprite the sprites for each directional movements of the Enemy.
     * @param map the map of the game level.
     */
    public RedEnemy(int x, int y, PImage[][] directionSprite, Map map) {
        super(x, y, directionSprite, map);
    }

    /**
     * The Red Enemy would make a random decision on collision.
     * It cannot turn into its original direction.
     * It would only make valid decision such that once it turns it would not collide with another wall.
     */
    public void turn() {
        Random random = new Random();
        int num = random.nextInt(4);
        Direction[] directions = Direction.values();

        while (num == this.directionNum || !checkCrash(directions[num].getX(), directions[num].getY())) {
            num = random.nextInt(4);
        }

        this.directionNum = num;
    }

}
