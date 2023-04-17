package kernel.objects;

import kernel.audio.AudioPlayer;
import kernel.main.cheatingGame;
import kernel.state.Playing;
import kernel.main.Game;
import kernel.state.cheatPlay;
import kernel.utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static kernel.utilz.Constants.ANI_SPEED;
import static kernel.utilz.Constants.Directions.*;
import static kernel.utilz.Constants.GRAVITY;
import static kernel.utilz.Constants.PlayerConstants.*;
import static kernel.utilz.HelpMethods.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    // Jumping / Gravity
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float jumpSpeed_cg = -3.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    // StatusBarUI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int healthWidth = healthBarWidth;

    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;
    private cheatPlay cheatPlay;

    private int tileY = 0;

    private boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed = 15;
    private int powerGrowTick;

    public Player(float x, float y, int width, int height, Playing playing, float maxBlood) {
        super(x, y, width, height);
        this.playing = playing; //pass the game state 
        this.state = IDLE;      //initiate the state to original state
        this.maxBlood = maxBlood;   //set maximized blood
        this.currentBlood = maxBlood;//set the current health to the maximum
        this.walkSpeed = Game.SCALE * 1.0f;
        loadAnimations();
        initHitbox(20, 27);
        attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (20 * Game.SCALE));
        resetAttackBox();
    }
    public Player(float x, float y, int width, int height, cheatPlay cheatPlay, float maxBlood) {
        super(x, y, width, height);
        this.cheatPlay = cheatPlay;
        this.state = IDLE;
        this.maxBlood = maxBlood;   //set maximized blood to infinitely many
        this.currentBlood = maxBlood;   //set the current health to the maximum
        this.walkSpeed = Game.SCALE * 1.0f;
        loadAnimations();
        initHitbox(20, 27);
        attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (20 * Game.SCALE));
        resetAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;  this.y = spawn.y;
        hitbox.x = x;   hitbox.y = y;
    }

    public void update() {
        updateHealthBar();
        updatePowerBar();
        if (currentBlood <= 0) {
            if(playing != null)   {  //for the normal game mode updates
                  if (state != DEAD) {
                      state = DEAD; //currentblood<0__set the state to DEAD and play audio for death
                      aniTick = 0;   aniIndex = 0;
                      playing.setDying(true);
                      Game g = playing.getGame();
//                      playing.getGame().getAudioPlayer().playEffect(AudioPlayer.dead);
                    // Check if player died in air
                      if (!IsEntityOnFloor(hitbox, lvlData)) {
                          inAir = true;   airSpeed = 0;
                      }
                  } else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
                       playing.setGameOver(true);
                       playing.getGame().getAudioPlayer().stopSong();
//                       playing.getGame().getAudioPlayer().playEffect(AudioPlayer.ko);
                  } else {
                       updateAnimationTick();
                // Fall if in air_gravity effect
                  if (inAir)
                    if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                        hitbox.y += airSpeed;
                        airSpeed += GRAVITY;
                    } else
                        inAir = false;
                  }
            return;
        } //else_for the cheating game mode
            else {
                cheatingGame cg = cheatPlay.getCheatingGame();
                if (state != DEAD) {
                    state = DEAD;
                    aniTick = 0;   aniIndex = 0;
                    cheatPlay.setPlayerDying(true);
//                    cg.getAudioPlayer().playEffect(AudioPlayer.dead);
                    if (!IsEntityOnFloor(hitbox, lvlData)) {
                        inAir = true;   airSpeed = 0;
                    }
                } else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
                    cheatPlay.setGameOver(true);
                    cg.getAudioPlayer().stopSong();
//                    cg.getAudioPlayer().playEffect(AudioPlayer.ko);
                } else {
                    updateAnimationTick();
                    if (inAir)
                        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                            hitbox.y += airSpeed;
                            airSpeed += GRAVITY;
                        } else
                            inAir = false;
                }

                return;

            }
        }
        updateAttackBox();

        if (state == HIT) {
            if (aniIndex <= GetSpriteAmount(state) - 3)
                pushBack(pushBackDir, lvlData, 1.25f);
            updatePushBackDrawOffset();
        } else
            updatePos();

        if (moving) {
            checkPotionTouched();
            checkSpikesTouched();

            tileY = (int) (hitbox.y / Game.TILES_SIZE);
            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }

        if (attacking || powerAttackActive)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }


    private void checkSpikesTouched() {
        if(playing!=null) playing.checkSpikesTouched(this);
        else cheatPlay.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        if(playing != null)  playing.checkPotionTouched(hitbox);
        else cheatPlay.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;

        if (powerAttackActive)
            attackChecked = false;
        if(playing != null) {
            playing.checkEnemyHit(attackBox);
            playing.checkObjectHit(attackBox);
//            playing.getGame().getAudioPlayer().attacking();
//            playing.getGame().getAudioPlayer().attacking();
        }
        if(playing == null){
            cheatPlay.checkEnemyHit(attackBox);
            cheatPlay.checkObjectHit(attackBox);
//            cheatPlay.getCheatingGame().getAudioPlayer().attacking();
        }
    }

    private void setAttackBoxOnRightSide() {
        attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE * 5);
    }

    private void setAttackBoxOnLeftSide() {
        attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
    }

    private void updateAttackBox() {
        if (right && left) {
            if (flipW == 1) {
                setAttackBoxOnRightSide();
            } else {
                setAttackBoxOnLeftSide();
            }

        } else if (right || (powerAttackActive && flipW == 1))
            setAttackBoxOnRightSide();
        else if (left || (powerAttackActive && flipW == -1))
            setAttackBoxOnLeftSide();

        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentBlood / (float) maxBlood) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);
        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    public void render(Graphics graphics, int lvlOffset) {
        graphics.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset + (int) (pushDrawOffset)), width * flipW, height, null);
        //draw Game UI__Background ui
        graphics.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        //draw Health bar
        graphics.setColor(Color.red);
        graphics.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        // Power Bar
        graphics.setColor(Color.yellow);
        graphics.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
                if (state == HIT) {
                    newState(IDLE);
                    airSpeed = 0f;
                    if (!IsFloor(hitbox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }

    private void setAnimation() {
        int startAni = state;
        if (moving) state = RUNNING;
        if (state == HIT)   return;
        else  state = IDLE;
        if (inAir) {
            if (airSpeed < 0)  state = JUMP;
            else  state = FALLING;
        }
        if (powerAttackActive) {//if attack action is invoked
            state = ATTACK;  
            aniIndex = 1;  aniTick = 0;
            return;
        }
        if (attacking) {
            state = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;  aniTick = 0;
                return;
            }
        }
        if (startAni != state){
            aniTick = 0;
            aniIndex = 0;
        }
    }
    private void updatePos() {
        moving = false;
        if (jump) {
            if(playing != null)   jump(playing);
            else  jump(cheatPlay);
        }
        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (right && left))
                    return;

        float xSpeed = 0;

        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (powerAttackActive) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
            }

            xSpeed *= 3;
        }

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir && !powerAttackActive) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump(Playing playing) {
        if (inAir)  return;
//        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.jump);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void jump (cheatPlay cheatPlay){
        if(inAir)  return;
//        cheatPlay.getCheatingGame().getAudioPlayer().playEffect(AudioPlayer.jump);
        inAir = true;
        airSpeed = jumpSpeed_cg;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            if (powerAttackActive) {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    public void changeHealth(int value) {
        if (value < 0) {
            if (state == HIT)
                return;
            else
                newState(HIT);
        }

        currentBlood += value;
        currentBlood = Math.max(Math.min(currentBlood, maxBlood), 0);
    }

    public void changeHealth(int value, Enemy e) {
        if (state == HIT)
            return;
        changeHealth(value);
        pushBackOffsetDir = UP;
        pushDrawOffset = 0;

        if (e.getHitbox().x < hitbox.x)
            pushBackDir = RIGHT;
        else
            pushBackDir = LEFT;
    }

    public void dead() {
        currentBlood = 0;
    }

    public void changePower(int value) {
        powerValue += value;
        powerValue = Math.max(Math.min(powerValue, powerMaxValue), 0);
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.player);
        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
    //reset all the setting booleans to false
    public void resetAll() {
        resetDirBooleans();
        inAir = false;  attacking = false;   moving = false; //set all the motions to false
        airSpeed = 0f; //set pause
        state = IDLE;  //go back to original state
        currentBlood = maxBlood; //reset_max blood state
        powerAttackActive = false;  powerAttackTick = 0;  powerValue = powerMaxValue;
        hitbox.x = x;   hitbox.y = y;
        resetAttackBox();
        if (!IsEntityOnFloor(hitbox, lvlData))    inAir = true;
    }

    private void resetAttackBox() {
        if (flipW == 1)   setAttackBoxOnRightSide();
        else
            setAttackBoxOnLeftSide();
    }

    public int getTileY() {
        return tileY;
    }

    public void powerAttack() {
        if (powerAttackActive)
            return;
        if (powerValue >= 60) {
            powerAttackActive = true;
            changePower(-60);
        }

    }

}