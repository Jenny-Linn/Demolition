package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

/**
 * Represents a Bomb that the player can deploy.
 */
public class Bomb {
    private int x;
    private int y;
    private PImage[] bombSprite;
    private PImage[] explosionSprite;
    private int bombTimer;
    private int bombSpriteNum;
    private boolean explode;
    private boolean endExplode;
    private ArrayList<Integer> horizontalPosition;
    private ArrayList<Integer> verticalPosition;
    private Game game;
    private boolean hasExplode;

    /**
     * Creates a new Bomb object.
     * @param x X-coordinate of the bomb.
     * @param y Y-coordinate of the bomb.
     * @param bombSprite the sprites for the bomb.
     * @param  explosionSprite the sprites for the explosion.
     * @param game the Game that this bomb is a part of.
     */
    public Bomb(int x, int y, PImage[] bombSprite, PImage[] explosionSprite, Game game) {
        this.x = x;
        this.y =y;
        this.bombSprite = bombSprite;
        this.explosionSprite = explosionSprite;
        this.horizontalPosition = new ArrayList<>();
        this.verticalPosition = new ArrayList<>();
        this.bombTimer = 0;
        this.bombSpriteNum = 0;
        this.explode = false;
        this.endExplode = false;
        this.game = game;
        this.hasExplode = false;

    }

    /**
     * Return true if bomb has exploded, false if not.
     * @return true if bomb has exploded, false if not.
     */
    public boolean getExplode() {
        return this.explode;
    }

    /**
     * Return true if explosion has ended, false if not.
     * @return true if explosion has ended, false if not.
     */
    public boolean getEndExplode() {
        return this.endExplode;
    }

    /**
     * Updates the bomb sprite every 0.25 seconds.
     * The Bomb detonate after 2 seconds and start explosion for 0.5 seconds.
     * @param map The map.
     */
    public void bombTick(Map map) {
        bombTimer ++;
        if (bombTimer >= 15 && !explode) {
            bombTimer = 0;
            bombSpriteNum ++;
            if (bombSpriteNum == 8) {
                explode = true;
            }
        } else if (explode && !endExplode ) {
            if (bombTimer >= 30) {
                endExplode = true;
            } else if (!hasExplode){
                explode(map);
            }
        }
    }

    /**
     * The explosion.
     * The explosion's shockwave and fire extend in the four cardinal directions to a maximum of 2 grid spaces away from the location the bomb was placed.
     * The explosion can be stopped earlier than 2 spaces if it comes into contact with a wall.
     * The broken tiles are turned into empty tiles if came in contact with explosion.
     * All Characters lose a life if caught in the explosion.
     * @param map The map.
     */
    public void explode(Map map) {
        hasExplode = true;
        char[][] newMap = map.getMap();

        int[] position = new int[] {-1, 1};
        int[] horizontal = new int[]{-2,2};
        int[] vertical = new int[]{-2,2};


        for (int i = 0 ; i < 2 ; i++) {
            int num = 0;
            int increase = 1;

            // check whether position is positive or negative
            if (position[i] > 0) {
                num = 1;
                increase = -1;
            }

            // vertical check
            if (y+1 < 13 && y-1 >= 0) {
                if (map.getMap()[y+position[i]][x] == 'W') {
                    vertical[num] = position[i] + increase;
                } else if (map.getMap()[y+position[i]][x] == 'B') {
                    vertical[num] = position[i];
                } else if (y+2 < 13 && y-2 >= 0) {
                    if (map.getMap()[y+position[i]-increase][x] == 'W') {
                        vertical[num] = position[i];
                    }
                }
            }

            // horizontal check
            if (x+1 < 15 && x-1 >= 0) {
                if (map.getMap()[y][x+position[i]] == 'W') {
                    horizontal[num] = position[i] + increase;
                } else if (map.getMap()[y][x+position[i]] == 'B') {
                    horizontal[num] = position[i];
                } else if (x+2 < 15 && x-2 >= 0) {
                    if (map.getMap()[y][x+position[i]-increase] == 'W') {
                        horizontal[num] = position[i];
                    }
                }
            }
        }

        for (int i = 0 ; i <= 1 ; i++) {
            horizontalPosition.add(horizontal[i]);
            verticalPosition.add(vertical[i]);
        }

        if (horizontal[0] == -2) {
            horizontalPosition.add(-1);
        }
        if (horizontal[1] == 2) {
            horizontalPosition.add(1);
        }
        if (vertical[0] == -2) {
            verticalPosition.add(-1);
        }
        if (vertical[1] == 2) {
            verticalPosition.add(1);
        }

        game.getCurrentGameLevel().removeEnemy(x, y);
        if (game.getCurrentGameLevel().getPlayer().getX() == x && game.getCurrentGameLevel().getPlayer().getY() == y) {
            game.reduceLife();
            game.loadLevel(game.getCurrentGameLevel());
            return;
        }

        // change map
        for(int i :  horizontalPosition) {
            if (map.getMap()[y][x+i] != 'G')
                newMap[y][x+i] = ' ';
            game.getCurrentGameLevel().removeEnemy(x+i, y);
            if (game.getCurrentGameLevel().getPlayer().getX() == x+i && game.getCurrentGameLevel().getPlayer().getY() == y) {
                game.reduceLife();
                game.loadLevel(game.getCurrentGameLevel());
                return;
            }
        }

        for(int i :  verticalPosition) {
            if (map.getMap()[y+i][x] != 'G')
                newMap[y+i][x] = ' ';
            game.getCurrentGameLevel().removeEnemy(x, y+i);
            if (game.getCurrentGameLevel().getPlayer().getX() == x && game.getCurrentGameLevel().getPlayer().getY() == y + i) {
                game.reduceLife();
                game.loadLevel(game.getCurrentGameLevel());
                return;
            }
        }
        map.setMap(newMap);
    }

    /**
     * Return the list of changes in horizontal directions.
     * @return the list of changes in horizontal directions.
     */
    public ArrayList<Integer> getHorizontalPosition() {
        return this.horizontalPosition;
    }

    /**
     * Return the list of changes in vertical directions
     * @return the list of changes in vertical directions.
     */
    public ArrayList<Integer> getVerticalPosition() {
        return this.verticalPosition;
    }

    /**
     * Draws the bomb and explosion to the screen.
     *
     * @param app The window to draw onto
     */
    public void draw(PApplet app) {
        if (!explode) {
            app.image(bombSprite[bombSpriteNum], x*32, (y+2)*32);
        } else {
            if (!endExplode) {
                app.image(explosionSprite[0], x*32,(y+2)*32);

                for (Integer integer : horizontalPosition) {
                    if (integer != 0)
                        app.image(explosionSprite[1], (x + integer) * 32, (y + 2) * 32);
                }
                for (Integer integer : verticalPosition) {
                    if (integer != 0)
                        app.image(explosionSprite[2], (x) * 32, (y + 2 + integer) * 32);
                }
            }
        }
    }
}
