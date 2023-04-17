package kernel.state;

import kernel.audio.AudioPlayer;
import kernel.gameUI.MenuButton;
import kernel.main.Game;
import kernel.main.cheatingGame;

import java.awt.event.MouseEvent;
//already checked
// the general states class__each subclass(menu/playing/credit/quit/options) is one kind of state
// pass to the Game object

public class State {

    protected Game game;
    protected cheatingGame cheatingGame;

    public State(Game game) {
        this.game = game;
    }
    public State(cheatingGame cheatingGame){
        this.cheatingGame = cheatingGame;
    }
    public boolean isIn(MouseEvent event, MenuButton menuButton) {
        return menuButton.getBounds().contains(event.getX(), event.getY());
    }

    public Game getGame() {
        return game;
    }
    public cheatingGame getCheatingGame(){
        return cheatingGame;
    }

    @SuppressWarnings("incomplete-switch")
    public void setGamestate(Gamestate state) {
        switch (state) {
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
            case MENU -> game.getAudioPlayer().getSong(AudioPlayer.menu);
        }
        Gamestate.gamestate = state;
    }

}