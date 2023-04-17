package kernel.utilz;

import kernel.main.Game;
//define and set some constants for the game
public class Constants {
    public static final float GRAVITY = 0.04f * Game.SCALE;
    public static final int ANI_SPEED = 25;

    public static class Dialogue {
        public static int GetSpriteAmount(int type) {
            return 0;
        }
    }

    public static class Projectiles {
        public static final int ball_width = (int) (Game.SCALE * 15);
        public static final int ball_height = (int) (Game.SCALE * 15);
        public static final float speed = 1.0f * Game.SCALE;
    }

    public static class ObjectConstants {
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int RED_POTION_VALUE = 15;
        public static final int RED_POTION_VALUE2 = 200;
        public static final int BLUE_POTION_VALUE = 10;
        public static final int BLUE_POTION_VALUE2 = 600;
        public static final int CONTAINER_WIDTH = (int) (Game.SCALE * 40);
        public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * 30);

        public static final int POTION_WIDTH = (int) (Game.SCALE * 12);
        public static final int POTION_HEIGHT = (int) (Game.SCALE * 16);
        public static final int SPIKE_WIDTH = (int) (Game.SCALE * 32);
        public static final int SPIKE_HEIGHT = (int) (Game.SCALE * 32);
        public static final int CANNON_WIDTH = (int) (40 * Game.SCALE);
        public static final int CANNON_HEIGHT = (int) (26 * Game.SCALE);
    }

    public static class EnemyConstants {
        public static final int CRABBY = 0;
        public static final int PINKSTAR = 1;
        public static final int SHARK = 2;
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;
        public static final int CRABBY_WIDTH = (int) (72 * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (32 * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_X = (int) (26 * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int) (9 * Game.SCALE);
        public static final int PINKSTAR_WIDTH = (int) (34 * Game.SCALE);
        public static final int PINKSTAR_HEIGHT = (int) (30 * Game.SCALE);
        public static final int PINKSTAR_DRAWOFFSET_X = (int) (9 * Game.SCALE);
        public static final int PINKSTAR_DRAWOFFSET_Y = (int) (7 * Game.SCALE);
        public static final int SHARK_WIDTH = (int) (34 * Game.SCALE);
        public static final int SHARK_HEIGHT = (int) (30 * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_X = (int) (8 * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_Y = (int) (6 * Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_state) {
                case IDLE: {
                    if (enemy_type == CRABBY)
                        return 9;
                    else if (enemy_type == PINKSTAR || enemy_type == SHARK)
                        return 8;
                }
                case RUNNING:
                    return 6;
                case ATTACK:
                    if (enemy_type == SHARK)
                        return 8;
                    return 7;
                case HIT:
                    return 4;
                case DEAD:
                    return 5;
            }

            return 0;

        }

        public static float GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case CRABBY:   return 50;
                case PINKSTAR, SHARK:  return 30;
                default:  return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case CRABBY:  return 15;
                case PINKSTAR: return 20;
                case SHARK: return 20;
                default: return 0;
            }
        }
    }

    public static class UI {
        public static class Buttons {
            //每一个小按钮的分辨率为140*56
            public static final int B_WIDTH = (int) (140 * Game.SCALE);
            public static final int B_HEIGHT = (int) (56 * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int ATTACK = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;
//set player's actions and return their corresponding values
        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case DEAD: return 8;
                case RUNNING: return 6;
                case IDLE: return 5;
                case HIT: return 4;
                case JUMP:
                case ATTACK: return 3;
                case FALLING:
                default: return 1;
            }
        }
    }
}
