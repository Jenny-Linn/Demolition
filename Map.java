package demolition;

import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Represents a Map.
 */
public class Map {
    private char[][] map;
    private String filePath;

    /**
     * Creates a new Map object.
     *
     * @param filePath File path of the map.
     */
    public Map(String filePath) {
        this.map = new char[13][15];
        this.filePath = filePath;
    }

    /**
     * Returns the map.
     * @return the map.
     */
    public char[][] getMap() {
        return this.map;
    }

    /**
     * Update the map.
     *
     * @param map The updated map.
     */
    public void setMap(char[][] map) {
        this.map = map;
    }

    /**
     * Check whether the given map file is valid.
     * Set up the Map if the file is valid.
     * @throws InvalidMap when the given map file is not valid.
     */
    public void checkValidMap() throws InvalidMap{
        try {
            Scanner scan = new Scanner(new File(filePath));

            // save map into a multidimensional array
            int line_num = 0;
            boolean containGoalTile = false;
            boolean containStartPoint = false;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (line.length() != 15) {
                    throw new InvalidMap("width is not 15");
                } else if (!line.matches("[WBG PRY]+")) {
                    throw new InvalidMap("contains wrong letter");
                }

                if (line.contains("G")) {
                    containGoalTile = true;
                }
                if (line.contains("P")) {
                    containStartPoint = true;
                }

                this.map[line_num] = line.toCharArray();
                line_num ++;
            }
            if (line_num != 13) {
                throw new InvalidMap("height is not 13");
            } else if (!containGoalTile) {
                throw new InvalidMap("does not contain goal tile");
            } else if (!containStartPoint) {
                System.out.println("here");
                throw new InvalidMap("does not contain starting point");
            }

        } catch (FileNotFoundException e){
            throw new InvalidMap("file does not exist");
        }
    }

    /**
     * Draws all the grid of the map to the screen.
     * @param app The window to draw onto.
     * @param gridSprite The sprites for different types of grid.
     */
    public void draw(PApplet app,HashMap<String, PImage> gridSprite) {
        String matchEmpty = "PRY";
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'W') {
                    app.image(gridSprite.get("SolidWall"), j*32, (i+2)*32);
                } else if (map[i][j] == 'B') {
                    app.image(gridSprite.get("BrokenWall"), j*32, (i+2)*32);
                } else if (map[i][j] ==  ' ' || matchEmpty.indexOf(map[i][j]) != -1) {
                    app.image(gridSprite.get("EmptyTile"), j*32, (i+2)*32);
                } else if (map[i][j] == 'G') {
                    app.image(gridSprite.get("GoalTile"), j*32, (i+2)*32);
                }
            }
        }
    }
}

/**
 * Represent a InvalidMap Exception.
 */
class InvalidMap extends Exception{

    /**
     * Creates a new InvalidMap Exception.
     */
    public InvalidMap(String msg) {
        super(msg);
    }
}

