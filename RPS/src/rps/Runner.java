package rps;

import javax.swing.JOptionPane;

/**
 *
 * Runs the GUI version of the RPS game.
 * 
 * @author Chris Von Hoene
 * 
 * TODO: implement a keyboard listener so the user can
 * just press 1, 2, or 3 to make a move.
 * Faster than hitting a button.
 */
public class Runner {
    
    public static final int ROCK = 1;
    public static final int PAPER = 2;
    public static final int SCISSORS = 3;
    
    private PlayerInterface pi;
    private RPSGUI brain;
    
    public RPSGUI getBrain() {return brain;}
    
    public PlayerInterface getPlayerInterface() {return pi;}
    
    public Runner() {
        
    }
    
    public void begin() {
        String name = JOptionPane.showInputDialog("Please input your name");
        if (name == null) {
            return; // user hit "cancel"
        }
        initPlayerInterface(name);
        brain = new RPSGUI(this, name);
        brain.init();
        brain.queryMove();
    }
    
    public void initPlayerInterface(String name) {
        pi = new PlayerInterface(this);
        pi.setComputerName("Computer");
        pi.setPlayerName(name);
        pi.activate();
        pi.update();
    }
    
    public static void main(String[] args) { 
        Runner r = new Runner();
        r.begin();
    }
    
    public void playRock() {
        brain.processMove(ROCK);
        updateScores();
        brain.queryMove();
    }
    
    public void playPaper() {
        brain.processMove(PAPER);
        updateScores();
        brain.queryMove();
    }
    
    public void playScissors() {
        brain.processMove(SCISSORS);
        updateScores();
        brain.queryMove();
    }
    
    public void updateScores() {
        pi.setPlayerScore(brain.getPlayerScore());
        pi.setComputerScore(brain.getComputerScore());
        pi.updateScores();
    }
    
    public void endGame() {
        brain.close();
    }
    
    public void print(String s) {
        pi.print(s);
    }
    
}
