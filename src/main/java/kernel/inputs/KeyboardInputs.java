package kernel.inputs;

import kernel.main.Game;
import kernel.main.GamePanel;
import kernel.state.Gamestate;
import kernel.state.Playing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void keyReleased(KeyEvent e) {
        Game g = gamePanel.getGame();
        if(g != null) {
            switch (Gamestate.state) {
                case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
                case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
            }
        }
        else switch (Gamestate.state) {
            case MENU -> gamePanel.getCheatingGame().getMenu().keyReleased(e);
            case PLAYING -> gamePanel.getCheatingGame().getCheatPlay().keyReleased(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void keyPressed(KeyEvent e) {
        Game g = gamePanel.getGame();
        if(g != null) {
            switch (Gamestate.state) {
                case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
                case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
                case OPTIONS -> gamePanel.getGame().getGameOptions().keyPressed(e);
            }
        }
        else switch (Gamestate.state) {
            case MENU -> gamePanel.getCheatingGame().getMenu().keyPressed(e);
            case PLAYING -> gamePanel.getCheatingGame().getCheatPlay().keyPressed(e);
            case OPTIONS -> gamePanel.getCheatingGame().getGameOptions().keyPressed(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}