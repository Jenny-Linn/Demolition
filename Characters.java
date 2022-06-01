package demolition;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a character of the game. Characters include Bomb Guy, Red and Yellow Enemies.
 */
public abstract class Characters{
    protected int x;
    protected int y;
    protected PImage[][] directionSprite;
    protected int directionNum;
    protected int timer;
    protected int spriteNumber;
    protected Map map;

    /**
     * Creates a new Characters object.
     * @param x X-coordinate of the Enemy.
     * @param y Y-coordinate of the Enemy.
     * @param directionSprite the sprites for each directional movements of the Enemy.
     * @param map the map of the game level.
     */
    public Characters(int x, int y, PImage[][] directionSprite, Map map) {
        this.x = x;
        this.y = y;
        this.directionSprite = directionSprite;
        this.directionNum = 0;
        this.timer = 0;
        this.spriteNumber = 0;
        this.map = map;
    }

    /**
     * Returns the X-coordinate that the character is on.
     * @return the X-coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the Y-coordinate that the character is on.
     * @return the Y-coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Check whether moving the character will crash into walls.
     * @param changeX change in X-coordinate.
     * @param changeY change in Y-coordinate.
     * @return true if moving the character will crash into walls, false if not.
     */
    public boolean checkCrash(int changeX, int changeY) {
        if (this.y + changeY > 12 || this.x + changeX > 14) {
            return false;
        } else if (map.getMap()[this.y + changeY][this.x + changeX] == 'W'
                || map.getMap()[this.y + changeY][this.x + changeX] == 'B') {
            return false;
        }
        return true;
    }

    /**
     * Updates the sprite every 0.2 seconds.
     */
    public void tick() {
        this.timer ++;
        if (this.timer >= App.FPS/4) {
            this.spriteNumber ++;
            if (this.spriteNumber > 3)
                this.spriteNumber = 0;
            this.timer = 0;
        }
    }

    /**
     * Draws the character to the screen.
     *
     * @param app The window to draw onto
     */
    public void draw(PApplet app) {
        app.image(this.directionSprite[this.directionNum][this.spriteNumber], this.x * 32, (y+2) * 32 -16);
    }
}
