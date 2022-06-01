package demolition;

import processing.core.PImage;

/**
 * Represents a Bomb Guy that the player can control.
 */
public class BombGuy extends Characters{
    private boolean win;

    /**
     * Creates a new Bomb Guy object.
     * @param x X-coordinate of the Bomb Guy.
     * @param y Y-coordinate of the Bomb Guy.
     * @param directionSprite the sprites for each directional movements of the Bomb Guy.
     * @param map the map of the game level.
     */
    public BombGuy(int x, int y, PImage[][] directionSprite, Map map) {
        super(x, y, directionSprite, map);
        this.win = false;
    }

    /**
     * Return true if the Bomb Guy reached the goal tile, false if not.
     * @return true if the Bomb Guy reached the goal tile, false if not.
     */
    public boolean isWin() {
        return this.win;
    }

    /**
     * Update whether the Bomb Guy has reached the goal tile.
     * @param win value for whether the Bomb Guy has win the game.
     */
    public void setWin(boolean win) {
        this.win = win;
    }

    /**
     * Move the Bomb Guy.
     * The Bomb Guy cannot walk through broken or solid walls.
     * If the Bomb Guy reaches the goal tile, it wins the current level.
     * @param changeX change in X-coordinate.
     * @param changeY change in Y-coordinate.
     * @param spriteNum the sprite number associated with the direction Bomb Guy is facing.
     */
    public void move(int changeX, int changeY, int spriteNum) {
        if (checkCrash(changeX, changeY)) {
            char[][] newMap = map.getMap();
            newMap[this.y][this.x] = ' ';
            this.x += changeX;
            this.y += changeY;

            if (map.getMap()[this.y][this.x] == 'G')
                this.win = true;

            this.directionNum = spriteNum;
            map.setMap(newMap);
        }
    }
}
