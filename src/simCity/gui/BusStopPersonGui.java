package simCity.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import simCity.BusStopAgent;
import simCity.Location;
import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.interfaces.Person;
import simCity.interfaces.PersonGuiInterface;

public class BusStopPersonGui implements Gui{

        SimCityGui gui;
        
        PersonGuiInterface pg;
        
        final int agentWidth = 40;
        final int agentHeight = 40;
        public int xIndex, yIndex;
        public int xPos, yPos;
        
        public final int xLeft = 215;
        public final int yTop = 260;
        public final int xSpacing = 60;
        public final int ySpacing = 110;
        
        boolean isPresent = true;
        
        public BusStopPersonGui(PersonGuiInterface pg, SimCityGui gui, int xIndex, int yIndex) {
        	this.pg = pg;
            this.gui = gui;
            this.xIndex = xIndex;
            this.yIndex = yIndex;
            xPos = xLeft + xIndex*xSpacing;
            yPos = yTop + yIndex*ySpacing;
           
        }

         

        public void updatePosition() {
        	
        }
        	 
        	

        public void draw(Graphics2D g) {
        	Graphics2D g4 = (Graphics2D)g;
        	g4.setColor(Color.GREEN);
        	g4.fillRect(xPos, yPos, agentWidth, agentHeight);
        	//g4.setColor(Color.BLACK);
        	//g.drawString(displayName, xPos, yPos + 2*agentHeight/3);
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

		public void setDestination(int x, int y) {
		
		}
        
}