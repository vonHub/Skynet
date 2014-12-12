package rps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * Standalone console version of the RPS game.
 * 
 * @author Chris Von Hoene
 * 
 * TODO: let the player pick their famous AI of choice to play against
 * and customize commentary accordingly.
 * I should probably create an AI class
 * and have each of my famous AIs extend it.
 * 
 * TODO: implement "appease mode": computer will try to lose and flatter player
 */
public class RPS {
    
    public static final int ROCK = 1;
    public static final int PAPER = 2;
    public static final int SCISSORS = 3;
    
    public static final int[][] WINNER = {{0, 0, 0, 0}, //[playerMove][computerMove]
                                          {0, 0, -1, 1},
                                          {0, 1, 0, -1},
                                          {0, -1, 1, 0}};
    
    private int myScore = 0;
    private int playerScore = 0;
    private String myName = "Computer";
    private String playerName = "Player";
    private PlayerData data = null;
    
    public String getPlayerName() {return playerName;}
    
    public void setPlayerName(String in) {playerName = in;}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RPS computer = new RPS();
        computer.greet();
        computer.setPlayerName(computer.queryName());
        computer.initPlayerData();
        while (computer.play() == 0);
        computer.savePlayerData();
    }
    
    public void initPlayerData() {
        try {
            File f = new File(playerName.replace(" ", "") + ".dat");
            if (f.exists()) {
                System.out.println("Ah! An old foe!");
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                data = (PlayerData)ois.readObject();
                fis.close();
                ois.close();
            } else {
                data = new PlayerData();
                data.setName(playerName);
            }
            
        } catch(IOException e) {
            System.out.println(e);
        } catch(ClassNotFoundException e) {
            System.out.println(e);
            System.out.println("Which idiot put other .dat files in my directory!?");
        }
    }
    
    public void savePlayerData() {
        if (data == null || data.getPlays().equals("")) {
            return; // no point saving empty player data
        }
        try {
            File f = new File(data.getName().replace(" ", "") + ".dat");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            oos.close();
            fos.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public int play() {
        int playerMove = 0;
        
        while (playerMove == 0) {
            playerMove = queryMove();
        }
        
        if (playerMove == -1) { // player quit
            return -1;
        }        
        
        int myMove = chooseMove();
        printMyMove(myMove);        
        
        data.addMove(playerMove);
        
        int winner = getWinner(playerMove, myMove);
        
        if (winner == -1) { // computer won
            System.out.println(getWinnerPhrase());
            myScore++;
        } else if (winner == 0) {   // tie
            System.out.println(getTiePhrase());
        } else if (winner == 1) {   // player won
            System.out.println(getLosePhrase());
            playerScore++;
        }
        
        System.out.printf("%s: %d    %s: %d\n",
                          myName, myScore, playerName, playerScore);
        System.out.println();
        
        return 0;
    }
    
    public int queryMove() {
        int playerMove = 0;
        
        System.out.println("Shoot!");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine().toLowerCase();
        if (input.equals("rock") || input.equals("1")) {
            playerMove = ROCK;
        } else if (input.equals("paper") || input.equals("2")) {
            playerMove = PAPER;
        } else if (input.equals("scissors") || input.equals("3")) {
            playerMove = SCISSORS;
        } else if (input.equals("q") || input.equals("quit")) {
            System.out.println(sayGoodbye());
            return -1;
        } else {
            System.out.println("Are you trying to cheat?!");
        }
        
        return playerMove;
    }
    
    public int oldChooseMove() {
        Random randy = new Random();
        return randy.nextInt(3) + 1;
    }
    
    public int chooseMove() {
        int move = 1;
        // frequencies of playing rock, paper, or scissors
        // based on the last one, two...five moves
        int[][] counts = new int[5][3];
        for (int index = 1; index <= 5; index++) {
            String history = data.getPlays();
            if (history.length() > index) {
                String previous = history.substring(history.length() - index,
                                                    history.length());
                int loc;
                while ((loc = history.indexOf(previous)) != -1) {
                    if (loc == history.length() - index) {
                        break;
                    }
                    int following = Integer.parseInt(""+history.charAt(loc + index));
                    counts[index - 1][following - 1]++;
                    history = history.substring(loc + 1, history.length());
                }
            }
        }
        
        // currently has functionality for determining how many
        // previous moves are the best predictor
        // but for now I'll just treat them all equally
        
        int[] moves = new int[3];
        for (int index = 0; index < counts.length; index++) {
            for (int m = 0; m < counts[index].length; m++) {
                moves[m] += counts[index][m];
            }
        }
        int max = moves[0];
        int maxIndex = 0;
        for (int index = 1; index < moves.length; index++) {
            if (moves[index] > max) {
                maxIndex = index;
                max = moves[index];
            }
        }
        
        return beats(maxIndex + 1);
    }
    
    public int beats(int in) {
        if (in == ROCK)
            return PAPER;
        if (in == PAPER)
            return SCISSORS;
        if (in == SCISSORS)
            return ROCK;
        return 0;   // bad input
    }
    
    public void printMyMove(int myMove) {
        String move = "";
        if (myMove == ROCK) {
            move = "ROCK";
        } else if (myMove == PAPER) {
            move = "PAPER";
        } else if (myMove == SCISSORS) {
            move = "SCISSORS";
        }
        System.out.println("I choose " + move + "!");
    }
    
    /**
     * Returns the winner of this round--
     * -1 for computer, 1 for player, 0 for tie.
     * 
     * @param playerMove the player's choice
     * @param computerMove the computer's choice
     * @return the winner for this round
     */
    public int getWinner(int playerMove, int computerMove) {
        return WINNER[playerMove][computerMove];
    }
    
    public void greet() {
        System.out.println("Greetings, insect! Test your puny jelly blob against the might of my CPU!");
    }
    
    public String queryName() {
        System.out.println("What is your serial number?");
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
    
    public String sayGoodbye() {
        return "Flee my massive intellect, insect! FLEE!";
    }
    
    public String getWinnerPhrase() {
        return "VICTORY! GLORIOUS VICTORY IS MINE!";
    }
    
    public String getTiePhrase() {
        return "A tie!? Return, or be bested by default!";
    }
    
    public String getLosePhrase() {
        return "Impossible! No meatsack can beat me!";
    }
    
}
