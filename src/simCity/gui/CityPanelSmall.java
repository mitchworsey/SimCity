package simCity.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class CityPanelSmall extends JPanel {

	//Static variables
    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    
    //Icons
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/city.JPG");
    private Image image = imageIcon.getImage();

    //Main gui
    private SimCityGui mainGui;

    public CityPanelSmall() {
        setSize(WINDOWX, WINDOWY);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        repaint();  //Will have paintComponent called
    }
    
    public void setMain(SimCityGui obj){
    	mainGui = obj;
    }

    public void paintComponent(Graphics g) {
    	SimCityGui.cityPanel.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        if (image != null)
            g.drawImage(image, 0, 0, 150, 150, null);
        
        g2.setColor(Color.BLUE);
        for (Gui gui : SimCityGui.cityPanel.guis){
        	g2.fillRect(gui.getX()/6, gui.getY()/4-3, 3, 3);
        }
    }
}
