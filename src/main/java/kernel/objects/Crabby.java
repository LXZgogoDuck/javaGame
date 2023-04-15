package kernel.objects;

import kernel.state.Playing;
import kernel.state.cheatPlay;

import static kernel.utilz.Constants.EnemyConstants.*;
import static kernel.utilz.HelpMethods.IsFloor;
public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox(82, 19, 30);
    }

    public void update(int[][] lvlData, Playing playing) {
        updateMotion(lvlData, playing);
        updateAnimationTick();
        updateAttackBox();
    }
    public void update(int[][] lvlData, cheatPlay cheatPlay){
        updateMotion(lvlData, cheatPlay);
        updateAnimationTick();
        updateAttackBox();
    }
    private void updateMotion(int[][] lvlData, cheatPlay cheatPlay) {
        if (firstUpdate) firstUpdateCheck(lvlData);
        if (inAir) {
            inAirChecks(lvlData, cheatPlay);
        } else {
            switch (state) {
                case IDLE:
                    if (IsFloor(hitbox, lvlData))
                        newState(RUNNING);//if the enemy is on the floor_set the state to running
                    else inAir = true;
                    break;
                case RUNNING://can see player_attack towards the player
                    if (canSeePlayer(lvlData, cheatPlay.getPlayer())) {
                        turnTowardsPlayer(cheatPlay.getPlayer());
                        if (isPlayerCloseForAttack(cheatPlay.getPlayer())) newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) attackChecked = false;
                    if (aniIndex == 3 && !attackChecked) checkPlayerHit(attackBox,cheatPlay.getPlayer());
                    break;
                case HIT:
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2) pushBack(pushBackDir, lvlData, 2f);
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }

    private void updateMotion(int[][] lvlData, Playing playing) {
        if (firstUpdate)   firstUpdateCheck(lvlData);
        if (inAir) {
            inAirChecks(lvlData, playing);
        } else {
            switch (state) {
                case IDLE:
                    if (IsFloor(hitbox, lvlData))   newState(RUNNING);//if the enemy is on the floor_set the state to running
                    else   inAir = true;   break;
                case RUNNING://can see player_attack towards the player
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        turnTowardsPlayer(playing.getPlayer());
                        if (isPlayerCloseForAttack(playing.getPlayer()))   newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)   attackChecked = false;
                    if (aniIndex == 3 && !attackChecked)   checkPlayerHit(attackBox, playing.getPlayer());
                    break;
                case HIT:
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)  pushBack(pushBackDir, lvlData, 2f);
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }

}