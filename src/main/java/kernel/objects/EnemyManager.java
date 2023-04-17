package kernel.objects;

import kernel.levels.Level;
import kernel.state.Playing;
import kernel.state.cheatPlay;
import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static kernel.utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private cheatPlay cheatPlay;
    private BufferedImage[][] crabbyArr, pinkstarArr, sharkArr;
    private Level currentLevel;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        sharkArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8, 5, 34, 30);
        pinkstarArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ps), 8, 5, 34, 30);
        crabbyArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.crab), 9, 5, 72, 32);
    }
    public EnemyManager(cheatPlay cheatPlay){
        this.cheatPlay = cheatPlay;
        sharkArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8, 5, 34, 30);
        pinkstarArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ps), 8, 5, 34, 30);
        crabbyArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.crab), 9, 5, 72, 32);
    }

    private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
        for (int j = 0; j < tempArr.length; j++)
            for (int i = 0; i < tempArr[j].length; i++)
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
        return tempArr;
    }

    public void loadEnemies(Level level) {
        this.currentLevel = level;
    }

    public void update(int[][] lvlData) {
        boolean isAnyActive = false;
        if (playing != null) {
            for (Crabby c : currentLevel.getCrabs())
                if (c.isActive()) {
                    c.update(lvlData, playing);
                    isAnyActive = true;
                }

            for (Pinkstar p : currentLevel.getPinkstars())
                if (p.isActive()) {
                    p.update(lvlData, playing);
                    isAnyActive = true;
                }

            for (Shark s : currentLevel.getSharks())
                if (s.isActive()) {
                    s.update(lvlData, playing);
                    isAnyActive = true;
                }
//when all the enemies are dead, game is completed!
            if (!isAnyActive)
                playing.setLevelCompleted(true);
        }
        else {
            for (Crabby c : currentLevel.getCrabs())
                if (c.isActive()) {
                    c.update(lvlData,cheatPlay);
                    isAnyActive = true;
                }

            for (Pinkstar p : currentLevel.getPinkstars())
                if (p.isActive()) {
                    p.update(lvlData, cheatPlay);
                    isAnyActive = true;
                }

            for (Shark s : currentLevel.getSharks())
                if (s.isActive()) {
                    s.update(lvlData, cheatPlay);
                    isAnyActive = true;
                }
//when all the enemies are dead, game is completed!
            if (!isAnyActive)
                cheatPlay.setLevelCompleted(true);

        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawPinkstars(g, xLvlOffset);
        drawSharks(g, xLvlOffset);
    }

    private void drawSharks(Graphics g, int xLvlOffset) {
        for (Shark s : currentLevel.getSharks())
            if (s.isActive()) {
                g.drawImage(sharkArr[s.getState()][s.getAniIndex()], (int) s.getHitbox().x - xLvlOffset - SHARK_DRAWOFFSET_X + s.flipX(),
                        (int) s.getHitbox().y - SHARK_DRAWOFFSET_Y + (int) s.getPushDrawOffset(), SHARK_WIDTH * s.flipW(), SHARK_HEIGHT, null);
            }
    }

    private void drawPinkstars(Graphics graphics, int xLvlOffset) {
        for (Pinkstar p : currentLevel.getPinkstars())
            if (p.isActive()) {
                graphics.drawImage(pinkstarArr[p.getState()][p.getAniIndex()], (int) p.getHitbox().x - xLvlOffset - PINKSTAR_DRAWOFFSET_X + p.flipX(),
                        (int) p.getHitbox().y - PINKSTAR_DRAWOFFSET_Y + (int) p.getPushDrawOffset(), PINKSTAR_WIDTH * p.flipW(), PINKSTAR_HEIGHT, null);
            }
    }

    private void drawCrabs(Graphics graphics, int xLvlOffset) {
        for (Crabby c : currentLevel.getCrabs())
            if (c.isActive()) {
                graphics.drawImage(crabbyArr[c.getState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
                        (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y + (int) c.getPushDrawOffset(), CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : currentLevel.getCrabs())
            if (c.isActive())
                if (c.getState() != DEAD && c.getState() != HIT)
                    if (attackBox.intersects(c.getHitbox())) {
                        c.hurt(20);
                        return;
                    }

        for (Pinkstar p : currentLevel.getPinkstars())
            if (p.isActive()) {
                if (p.getState() == ATTACK && p.getAniIndex() >= 3)
                    return;
                else {
                    if (p.getState() != DEAD && p.getState() != HIT)
                        if (attackBox.intersects(p.getHitbox())) {
                            p.hurt(20);
                            return;
                        }
                }
            }

        for (Shark s : currentLevel.getSharks())
            if (s.isActive()) {
                if (s.getState() != DEAD && s.getState() != HIT)
                    if (attackBox.intersects(s.getHitbox())) {
                        s.hurt(20);
                        return;
                    }
            }
    }

    public void resetAllEnemies() {
        for (Crabby c : currentLevel.getCrabs())
            c.resetEnemy();
        for (Pinkstar p : currentLevel.getPinkstars())
            p.resetEnemy();
        for (Shark s : currentLevel.getSharks())
            s.resetEnemy();
    }

}
