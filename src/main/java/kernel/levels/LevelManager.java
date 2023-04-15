package kernel.levels;

import kernel.main.Game;
import kernel.main.cheatingGame;
import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private Game game;
    private cheatingGame cheatingGame;
    private BufferedImage[] levelSprite;
    private BufferedImage[] waterSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0, aniTick, aniIndex;

    public LevelManager(Game game) {
        this.game = game;
        imSprites(); //load images for the level bg
        createWater();
        levels = new ArrayList<>();
        BufferedImage[] allLevels = LoadSave.GetLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }
    public LevelManager (cheatingGame cheatingGame){
        this.cheatingGame = cheatingGame;
        imSprites(); //load images for the level bg
        createWater();
        levels = new ArrayList<>();
        BufferedImage[] allLevels = LoadSave.GetLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    private void imSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 12; j++) {
                int index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j * 32, i * 32, 32, 32);
            }
    }

    private void createWater() {
        waterSprite = new BufferedImage[5];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
        for (int i = 0; i < 4; i++) {
            waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
        }
        waterSprite[4] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
    }

    public void loadNextLevel() {
        Level newLevel = levels.get(lvlIndex);
        if(game != null){
            game.getPlaying().getEnemyManager().loadEnemies(newLevel);
            game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
            game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
            game.getPlaying().getObjectManager().loadObjects(newLevel);
        }
        else{
            cheatingGame.getCheatPlay().getEnemyManager().loadEnemies(newLevel);
            cheatingGame.getCheatPlay().getPlayer().loadLvlData(newLevel.getLevelData());
            cheatingGame.getCheatPlay().setMaxLvlOffset(newLevel.getLvlOffset());
            cheatingGame.getCheatPlay().getObjectManager().loadObjects(newLevel);
        }
    }


    public void draw(Graphics g, int lvlOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                int x = Game.TILES_SIZE * i - lvlOffset;
                int y = Game.TILES_SIZE * j;
                if (index == 48)
                    g.drawImage(waterSprite[aniIndex], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                else if (index == 49)
                    g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                else
                    g.drawImage(levelSprite[index], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    public void update() {
        aniTick++;
        if (aniTick >= 40) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4)
                aniIndex = 0;
        }
    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public int getLevelIndex() {
        return lvlIndex;
    }

    public void setLevelIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }
}
