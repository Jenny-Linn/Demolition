package demolition;

import processing.core.*;
import java.util.HashMap;

/**
 * Handles the Window of the program.
 */
public class App extends PApplet {
    /**
    * The width of the window.
    */
    public static final int WIDTH = 480;
    /**
    * The height of the window.
    */
    public static final int HEIGHT = 480;
    /**
    * The number of frames per second.
    */
    public static final int FPS = 60;
    private boolean keyWork;
    private PImage[] icons = new PImage[2];
    private HashMap<String, PImage> gridSprite = new HashMap<>();
    private PImage[][] playerSprite = new PImage[4][4];
    private PImage[][] redEnemy = new PImage[4][4];
    private PImage[][] yellowEnemy = new PImage[4][4];
    private PImage[] bombSprite = new PImage[9];
    private PImage[] explosionSprite  = new PImage[3];
    private PFont font;
    private Game game;
    private String configName;

    public App() {
        this.keyWork = true;
    }

    /**
     * Sets up parameters for the project, namely its width and height.
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Sets up the program, setting the frame rate.
     * Loads in all images. Loads in the font for the text.
     */
    public void setup() {
        frameRate(FPS);

        icons[0] = this.loadImage("src/main/resources/icons/player.png");
        icons[1] = this.loadImage("src/main/resources/icons/clock.png");

        gridSprite.put("SolidWall", this.loadImage("src/main/resources/wall/solid.png"));
        gridSprite.put("BrokenWall", this.loadImage("src/main/resources/broken/broken.png"));
        gridSprite.put("EmptyTile", this.loadImage("src/main/resources/empty/empty.png"));
        gridSprite.put("GoalTile", this.loadImage("src/main/resources/goal/goal.png"));

        String[] directions = {"", "_left", "_up", "_right", "_down"};
        for(int i = 0; i < playerSprite.length; i++) {
            for (int j = 0; j < playerSprite[i].length; j++) {
                playerSprite[i][j] = this.loadImage(String.format("src/main/resources/player/player%s%d.png", directions[i], j+1));
                redEnemy[i][j] = this.loadImage(String.format("src/main/resources/red_enemy/red%s%d.png", directions[i+1], j+1));
                yellowEnemy[i][j] = this.loadImage(String.format("src/main/resources/yellow_enemy/yellow%s%d.png", directions[i+1], j+1));
            }
        }

        bombSprite[0] = this.loadImage("src/main/resources/bomb/bomb.png");
        for (int i = 1; i < 9; i++ ) {
            bombSprite[i] = this.loadImage(String.format("src/main/resources/bomb/bomb%d.png", i));
        }

        explosionSprite[0] = this.loadImage("src/main/resources/explosion/centre.png");
        explosionSprite[1] = this.loadImage("src/main/resources/explosion/horizontal.png");
        explosionSprite[2] = this.loadImage("src/main/resources/explosion/vertical.png");

        font = createFont("PressStart2P-Regular.ttf", 20);
        textFont(font);
        textAlign(CENTER, CENTER);

        setConfigName("config.json");

        this.game = new Game(getConfigName(), playerSprite, redEnemy, yellowEnemy);
        game.loadConfig(this);
        game.setUpGame();
        if (game.getGameLevels().size() != 0)
            game.loadLevel(game.getGameLevels().get(0));
    }

    /**
     * Move the Bomb Guy according to the input of the keys. Press SPACE key to deploy bombs.
     */
    public void keyPressed() {
        if (this.keyWork) {
            if (keyCode == UP) {
                game.getCurrentGameLevel().getPlayer().move(Direction.UP.getX(), Direction.UP.getY(), 2);
            } else if (keyCode == DOWN) {
                game.getCurrentGameLevel().getPlayer().move(Direction.DOWN.getX(), Direction.DOWN.getY(), 0);
            } else if (keyCode == LEFT) {
                game.getCurrentGameLevel().getPlayer().move( Direction.LEFT.getX(), Direction.LEFT.getY(), 1);
            } else if (keyCode == RIGHT) {
                game.getCurrentGameLevel().getPlayer().move(Direction.RIGHT.getX(), Direction.RIGHT.getY(), 3);
            } else if (keyCode == 32) {
                Bomb bomb = new Bomb(game.getCurrentGameLevel().getPlayer().getX(),game.getCurrentGameLevel().getPlayer().getY(), bombSprite, explosionSprite, game);
                game.getCurrentGameLevel().setBomb(bomb);
            }
        }
        this.keyWork = false;
    }

    /**
     * Called every frame if a key is released.
     */
    public void keyReleased() {
        this.keyWork = true;
    }

    /**
     * Draws and updates the program.
     */
    public void draw() {
        background(239, 129, 0);
        fill(0);

        if (game.getWin()) {
            text("YOU WIN", 240, 240);
        } else if (game.getDies() || game.getCurrentGameLevel().getTime() <= 0) {
            text("GAME OVER", 240, 240);
        } else {
            this.image(icons[0], 128, 16);
            this.image(icons[1], 256, 16);

            game.checkChangeStage();

            text(String.valueOf(game.getPlayerLife()), 180, 35);
            text(String.valueOf(game.getCurrentGameLevel().getTime()), 325, 35);

            game.getCurrentGameLevel().getMap().draw(this, this.gridSprite);
            game.getCurrentGameLevel().draw(this, game);
        }
    }

    public void setConfigName(String name) {
        configName = name;
    }

    public String getConfigName() {
        return this.configName;
    }

    public Game getGame() {
        return this.game;
    }

    public static void main(String[] args) {
        PApplet.main("demolition.App");
    }
}
