package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

/**
 * Represents a game level.
 */
public class GameLevel{
    private BombGuy player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bomb> bomb;
    private int time;
    private int secondsCounter;
    private Map map;
    private PImage[][] playerSprite;
    private PImage[][] redSprite;
    private PImage[][] yellowSprite;
    private String levelPath;

    /**
     * Creates a new Game Level object.
     *
     * @param levelPath File path of the map for the level.
     * @param time Allowed time for the level.
     * @param map Map for the level.
     * @param playerSprite Sprites for the Bomb Guy.
     * @param redSprite Sprites for the Red Enemy.
     * @param yellowSprite Sprites for the Yellow Enemy.
     */
    public GameLevel(String levelPath, int time, Map map, PImage[][] playerSprite,  PImage[][] redSprite, PImage[][] yellowSprite ) {
        this.enemies = new ArrayList<>();
        this.bomb = new ArrayList<>();
        this.time = time;
        this.secondsCounter = 0;
        this.map = map;
        this.playerSprite = playerSprite;
        this.redSprite = redSprite;
        this.yellowSprite = yellowSprite;
        this.levelPath = levelPath;

    }
    /**
     * Gets the file path to the level map.
     * @return the file path.
     */
    public String getLevelPath() {
        return this.levelPath;
    }

    /**
     * Gets the player's bomb guy.
     * @return The bomb guy.
     */
    public BombGuy getPlayer() {
        return this.player;
    }

    /**
     * Gets the list of red and yellow enemies.
     * @return The enemies.
     */
    public ArrayList<Enemy> getEnemies() {
        return this.enemies;
    }

    /**
     * Gets the list of bombs.
     * @return The bombs.
     */
    public ArrayList<Bomb> getBomb() {
        return this.bomb;
    }

    /**
     * Gets the map associated with the level.
     * @return The map.
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * Gets the remaining time for the timer.
     * @return The time.
     */
    public int getTime() {
        return this.time;
    }

    /**
     * Adjusts the timer.
     * @param time - the amount of time for the timer.
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Set a bomb
     * @param bomb - the bomb that the player has deployed.
     */
    public void setBomb(Bomb bomb) {
        this.bomb.add(bomb);
    }

    /**
     * let the timer automatically run the count-down.
     */
    public void changeTime() {
        this.secondsCounter ++;
        if (this.secondsCounter >= 60) {
            this.time --;
            this.secondsCounter = 0;
        }
    }

    /**
     * load in Bomb Guy and enemies according to the configuration file.
     */
    public void setCharacters() {
        if (enemies != null) {
            enemies.clear();
        }
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[i].length; j++) {
                if (map.getMap()[i][j] == 'P') {
                    this.player = new BombGuy(j, i, playerSprite, map);
                } else if (map.getMap()[i][j] == 'R') {
                    RedEnemy redEnemy = new RedEnemy(j, i, redSprite, map);
                    enemies.add(redEnemy);
                } else if (map.getMap()[i][j] == 'Y') {
                    YellowEnemy yellowEnemy = new YellowEnemy(j, i, yellowSprite, map);
                    enemies.add(yellowEnemy);
                }
            }
        }
    }

    /**
     * Remove the enemy from the level if it's on the same coordinate as given in the parameter.
     * @param x - the x-coordinate.
     * @param y - the y-coordinate.
     */
    public void removeEnemy(int x, int y) {
        Enemy enemy = null;
        boolean whetherExist = false;
        for (Enemy i : enemies) {
            if (i.getX() == x && i.getY() == y) {
                whetherExist = true;
                enemy = i;
            }
        }
        if (whetherExist) {
            enemies.remove(enemy);
        }
    }

    /**
     * Check whether the player collide with an enemy.
     * Bomb Guy loses a life if he hits an enemy and the level reset.
     */
    public void checkClash(Game game) {
        for (Enemy i : enemies) {
            if (player.getX() == i.getX() && player.getY() == i.getY()) {
                game.reduceLife();
                game.loadLevel(game.getCurrentGameLevel());
                return;
            }
        }
    }

    /**
     * Set the player to the parameter given.
     * @param player the Bomb Guy that the player can control.
     */
    public void setPlayer(BombGuy player) {
        this.player = player;
    }

    /**
     * Draws all the game objects and characters associated with this game level to the screen.
     * @param app The window to draw onto.
     * @param game The game that this game level is a part of
     */
    public void draw(PApplet app, Game game) {
        changeTime();
        getPlayer().tick();
        checkClash(game);
        getPlayer().draw(app);

        for(Enemy i : getEnemies()) {
            i.move();
            i.tick();
            i.draw(app);
        }

        for (Bomb j : getBomb()) {
            j.bombTick(map);
            j.draw(app);
        }
    }
}
