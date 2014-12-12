package rps;

import java.io.Serializable;

/**
 *
 * Stores data for each player, such as play history.
 * 
 * @author Chris Von Hoene
 */
public class PlayerData implements Serializable{
    private String name = "";
    private String plays = "";
    private int victories = 0;
    private int losses = 0;
    
    public String getName() {return name;}
    public String getPlays() {return plays;}
    public int getVictories() {return victories;}
    public int getLosses() {return losses;}
    
    public void setName(String in) {name = in;}
    public void setPlays(String in) {plays = in;} 
    public void setVictories(int in) {victories = in;};
    public void setLosses(int in) {losses = in;}
    
    public void incVictories() {victories++;}
    public void incLosses() {losses++;}
    
    public void addMove(int in) {
        plays = plays + in;
    }
}
