package rps;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 *
 * Draws the picture of the computer in the GUI.
 * 
 * @author Chris Von Hoene
 */
public class DrawPanel extends JPanel {
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image computerImage = toolkit.getImage("computerImage.png");
        g.drawImage(computerImage, 0, 0, this);
        System.out.println(this.getWidth());
        System.out.println(this.getHeight());
    }
    
}
