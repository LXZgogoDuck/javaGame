package kernel.main;

import kernel.inputs.KeyboardInputs;
import kernel.inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import static kernel.main.Game.GAME_HEIGHT;
import static kernel.main.Game.GAME_WIDTH;


public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;
    private cheatingGame cheatingGame;

    public GamePanel(Game game) {
        mouseInputs = new MouseInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    public GamePanel(cheatingGame cheatingGame){
        mouseInputs = new MouseInputs(this);
        this.cheatingGame = cheatingGame;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(game != null) game.render(g);
        else cheatingGame.render(g);
    }

    public Game getGame() {
        return game;
    }
    public cheatingGame getCheatingGame(){
        return cheatingGame;
    }

}