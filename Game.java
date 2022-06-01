package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents one game that consists of various different game levels.
 */
public class Game {
    private String configPath;
    private HashMap<String, Integer> levelInfo;
    protected int playerLife;
    private ArrayList<GameLevel> gameLevels;
    protected PImage[][] playerSprite;
    protected PImage[][] redSprite;
    protected PImage[][] yellowSprite;
    private GameLevel currentGameLevel;
    private ArrayList<String> levelName;
    private boolean win;
    private boolean dies;

    /**
     * Creates a new Game object.
     * @param configPath the path for the configuration file
     * @param playerSprite the sprite images for Bomb Guy
     * @param  redSprite the sprite images for Red Enemy
     * @param yellowSprite the sprite images for Yellow Enemy
     */
    public Game(String configPath, PImage[][] playerSprite, PImage[][] redSprite, PImage[][] yellowSprite) {
        this.configPath = configPath;
        this.levelInfo = new HashMap<>();
        this.playerLife = 0;
        this.gameLevels = new ArrayList<>();
        this.playerSprite = playerSprite;
        this.redSprite = redSprite;
        this.yellowSprite = yellowSprite;
        this.levelName = new ArrayList<>();
        this.win = false;
        this.dies = false;
    }

    /**
     * Gets the number of player's life.
     * @return The number of player's lives.
     */
    public int getPlayerLife(){
        return this.playerLife;
    }

    /**
     * Reduce the player's lives by 1.
     */
    public void reduceLife() {
        if (this.playerLife - 1 <= 0) {
            this.dies = true;
            return;
        }
        this.playerLife --;
    }

    /**
     * Returns whether the player has won the whole game.
     * @return true if the player wins the game, false if not.
     */
    public boolean getWin() {
        return this.win;
    }

    /**
     * Returns whether the game is over.
     * @return true if the game is over, false if not.
     */
    public boolean getDies() {
        return this.dies;
    }

    /**
     * Returns the current game level.
     * @return the current game level.
     */
    public GameLevel getCurrentGameLevel() {
        return this.currentGameLevel;
    }

    /**
     * Returns the list of game levels in temporal order.
     * @return the list of game levels.
     */
    public ArrayList<GameLevel> getGameLevels() {
        return this.gameLevels;
    }

    /**
     * Returns the list of level path names in temporal order.
     * @return the list of level path names.
     */
    public ArrayList<String> getLevelName() {
        return this.levelName;
    }

    /**
     * Returns the level information retrieved from the configuration file, including file name and time.
     * @return the level information.
     */
    public HashMap<String, Integer> getLevelInfo() {
        return this.levelInfo;
    }


    /**
     * Read in configuration file.
     * @param app The PApplet object.
     */
    public void loadConfig(PApplet app) {
        // save config info
        JSONObject jsonFile = app.loadJSONObject(this.configPath);
        JSONArray jsonArray = jsonFile.getJSONArray("levels");

        for(int i = 0; i < jsonArray.size(); i++) {
            levelInfo.put(jsonArray.getJSONObject(i).getString("path"), jsonArray.getJSONObject(i).getInt("time"));
            levelName.add(jsonArray.getJSONObject(i).getString("path"));
        }

        this.playerLife = jsonFile.getInt("lives");
    }

    /**
     * Sets up the game, including the map and the game level.
     */
    public void setUpGame() {
        for (String i : levelName) {
            Map map = new Map(i);
            GameLevel gameLevel = new GameLevel(i, levelInfo.get(i), map, playerSprite, redSprite, yellowSprite);
            gameLevels.add(gameLevel);
        }
    }

    /**
     * load the game level.
     * check whether the map associated with the level is valid.
     * If valid, continue to set up the characters for the level.
     * @param gameLevel The game level to load.
     */
    public void loadLevel(GameLevel gameLevel) {
        this.currentGameLevel = gameLevel;
        try{
            gameLevel.getMap().checkValidMap();
        } catch(InvalidMap e) {
            System.out.println(e.getMessage());
        }
        gameLevel.setCharacters();
        currentGameLevel.setTime(levelInfo.get(currentGameLevel.getLevelPath()));
    }

    /**
     * check whether player proceed to the next level. If yes, load the next level.
     */
    public void checkChangeStage() {
        if (this.currentGameLevel.getPlayer().isWin()) {
            if (gameLevels.indexOf(this.currentGameLevel) == gameLevels.size()-1) {
                this.win = true;
                return;
            }
            int index = gameLevels.indexOf(this.currentGameLevel) + 1;
            if (index < gameLevels.size())
                loadLevel(gameLevels.get(index));
        }
    }
}
