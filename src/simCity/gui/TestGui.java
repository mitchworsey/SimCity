package simCity.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class TestGui implements Gui{

        SimCityGui gui;

        private boolean isPresent = true;
        private int xPos, yPos;
        private int xDestination, yDestination;
        private aStar newAStar;
        private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
        private Node tempNode;

        public TestGui(SimCityGui gui){ 
                xPos = 50;
                yPos = 50;
                xDestination = 50;
                yDestination = 50;
                newAStar = null;
                this.gui = gui;
        }

        public void updatePosition() {
        	if (!finalList.isEmpty()){
        		if (finalList.size()>=1){
        			//simCity.gui.CityPanel.grid.release(xPos/10, yPos/10);
        			xPos = finalList.get(finalList.size()-1).getX();
        			yPos = finalList.get(finalList.size()-1).getY();
        			//simCity.gui.CityPanel.grid.set(xPos/10, yPos/10);
        			finalList.remove(finalList.get(finalList.size()-1));
        		}
        	}
        }

        public void draw(Graphics2D g) {
        Graphics2D g4 = (Graphics2D)g;
        g4.setColor(Color.BLUE);
        g4.fillRect(xPos, yPos, 20, 20);
        }

        public boolean isPresent() {
                return isPresent;
        }
        
        public void setPresent(boolean p) {
                isPresent = p;
        }
        
        public int getX() {
        	return xPos;
        }
        
        public int getY() {
        	return yPos;
        }
        
        public void setDestination(int x, int y){
        	xDestination = x;
        	yDestination = y;
            newAStar = new aStar(xPos, yPos, xDestination, yDestination, CityPanel.grid);
            finalList = newAStar.getBestPath();
        }
}