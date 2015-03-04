package simCity.restaurant2.gui;

import simCity.gui.Gui;
import simCity.restaurant2.Restaurant2HostRole;
import simCity.restaurant2.interfaces.Restaurant2Host;

import java.awt.*;


/** HOSTGUI WILL NOT BE USED FOR V2!!!
 * 	ALL MOVEMENTS AND FUNCTIONALITY BESIDES ACCESSORS DISABLED
 */
public class Restaurant2HostGui implements Gui {

    private Restaurant2Host agent = null;

    private int xPos = -20, yPos = -20;//default host position
    private int xDestination = -20, yDestination = -20;//default start position
    

    public Restaurant2HostGui(Restaurant2Host agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    	// No animation in v2, do nothing
    }

    public void draw(Graphics2D g) {
    	// No animation in v2, do nothing
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		
	}
}
