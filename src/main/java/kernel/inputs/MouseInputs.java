package kernel.inputs;

import kernel.main.Game;
import kernel.main.GamePanel;
import kernel.state.Gamestate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static kernel.state.Gamestate.MENU;
import static kernel.state.Gamestate.PLAYING;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseDragged(MouseEvent e) {
        Game g = gamePanel.getGame();
        if (g != null) {
            switch (Gamestate.state) {
                case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(e);
                case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(e);
            }
        }
        if (g == null){
            switch (Gamestate.state) {
                case PLAYING -> gamePanel.getCheatingGame().getCheatPlay().mouseDragged(e);
                case OPTIONS -> gamePanel.getCheatingGame().getGameOptions().mouseDragged(e);
            }
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseMoved(MouseEvent e) {
        Game g = gamePanel.getGame();
        if (g != null) {
            switch (Gamestate.state) {
                case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
                case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
                case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(e);
            }
        if (g == null){   switch (Gamestate.state) {
                    case MENU -> gamePanel.getCheatingGame().getMenu().mouseMoved(e);
                    case PLAYING -> gamePanel.getCheatingGame().getCheatPlay().mouseMoved(e);
                    case OPTIONS -> gamePanel.getCheatingGame().getGameOptions().mouseMoved(e);
                }
            }
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseClicked(MouseEvent e) {
        Game g = gamePanel.getGame();
        if(g != null) {
            switch (Gamestate.state) {
                case PLAYING -> gamePanel.getGame().getPlaying().mouseClicked(e);
            }
        }
        else switch (Gamestate.state) {
            case PLAYING -> gamePanel.getCheatingGame().getCheatPlay().mouseClicked(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mousePressed(MouseEvent e) {
        Game g = gamePanel.getGame();
        if(g != null) {
            switch (Gamestate.state) {
                case MENU -> gamePanel.getGame().getMenu().mousePressed(e);
                case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(e);
                case OPTIONS -> gamePanel.getGame().getGameOptions().mousePressed(e);
            }
        }
        else switch (Gamestate.state) {
            case MENU -> gamePanel.getCheatingGame().getMenu().mousePressed(e);
            case PLAYING -> gamePanel.getCheatingGame().getCheatPlay().mousePressed(e);
            case OPTIONS -> gamePanel.getCheatingGame().getGameOptions().mousePressed(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseReleased(MouseEvent e) {
        Game g = gamePanel.getGame();
        if(g != null) {
            switch (Gamestate.state) {
                case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);
                case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);
                case OPTIONS -> gamePanel.getGame().getGameOptions().mouseReleased(e);
            }
        }
        else  switch (Gamestate.state) {
            case MENU -> gamePanel.getCheatingGame().getMenu().mouseReleased(e);
            case PLAYING -> gamePanel.getCheatingGame().getCheatPlay().mouseReleased(e);
            case OPTIONS -> gamePanel.getCheatingGame().getGameOptions().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not In use
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not In use
    }

}