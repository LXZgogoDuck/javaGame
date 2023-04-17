package kernel.levels;

import kernel.components.*;
import kernel.main.Game;
import kernel.objects.Crabby;
import kernel.objects.Pinkstar;
import kernel.objects.Shark;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static kernel.utilz.Constants.EnemyConstants.*;
import static kernel.utilz.Constants.ObjectConstants.*;


public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs = new ArrayList<>();
    private ArrayList<Pinkstar> pinkstars = new ArrayList<>();
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<Shark> sharks = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;
    private Random random;

    public Level(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        //load all images and set the position
        for (int y = 0; y < img.getHeight(); y++){
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                loadLevelData(red, x, y);
                loadEnemies(green, x, y);
                loadObjects(blue, x, y);
            }
        }
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.tiles_width;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50)  lvlData[y][x] = 0;
        else    lvlData[y][x] = redValue;
    }
    //set the position of the enemies
    //cool features: the occurance of different enemy charcters is at random
    private void loadEnemies(int greenValue, int x, int y) {
        switch (greenValue) {
            case CRABBY -> {
                crabs.add(new Crabby(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                random = new Random();
                if(random.nextFloat()> 0.6f) {
                    pinkstars.add(new Pinkstar(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                }
            }
            case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }
    //set the objects for the game
    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case BLUE_POTION -> {
                potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
                double r = Math.random();
                    if (r >= 0.7) sharks.add(new Shark(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            }
            case RED_POTION -> {
                potions.add(new Potion(x*Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
                double r = Math.random();
                if (r <= 0.4) cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            }
            case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case SPIKE -> {
                spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
                double r = Math.random();
                if (r >= 0.6)
                    sharks.add(new Shark(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            }
        }
    }
    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public ArrayList<Shark> getSharks() {
        return sharks;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public ArrayList<Pinkstar> getPinkstars() {
        return pinkstars;
    }

}
