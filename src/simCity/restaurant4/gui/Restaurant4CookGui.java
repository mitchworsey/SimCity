package simCity.restaurant4.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import simCity.gui.Gui;
import simCity.gui.Node;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;
import simCity.interfaces.Person;
import simCity.restaurant4.interfaces.Restaurant4Cook;

public class Restaurant4CookGui implements Gui {

	SimCityGui gui;
	private Restaurant4Cook cook = null;

    private int xPos = 661, yPos = 494;//default cook position
    private int xDestination = 661, yDestination = 494;//default start position
    private int xStart = 661, yStart = 494;
    private int wheelX, wheelY; //shared data wheel position
    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    
    private String order1 = "", order2 = "", order3 = "";
    public static int order_1X = 661;
    public static int order_1Y = 459;
    public static int order_2X = 661;
    public static int order_2Y = 484;
    public static int order_3X = 720;
    public static int order_3Y = 484;
    
    public static int plate_1X = 590;
    public static int plate_1Y = 459;
    public static int plate_2X = 590;
    public static int plate_2Y = 479;
    public static int plate_3X = 590;
    public static int plate_3Y = 499;

    private enum Command {noCommand, CookingFood, GettingOrdersFromWheel, ReturningToGrill};
	private Command command=Command.noCommand;
	private String displayName = "";
	private ImageIcon wheelIcon = new ImageIcon("src/simCity/gui/images/wheel.gif");
    private Image wheelImg = wheelIcon.getImage();

    
    public Restaurant4CookGui(Restaurant4Cook cook, SimCityGui gui, int xStart, int yStart) {
    	this.cook = cook;
        this.gui = gui;
        
        newAStar = null;
        xPos = xStart;
        yPos = yStart;
        xDestination = xStart;
        yDestination = yStart;
        wheelX = 700;
        wheelY = 250;
        //newAStar = null;
        
        /*if(agent.getName().length() == 0) {
        	displayName = "";
        }
        else if (agent.getName().length() == 1 || agent.getName().length() == 2) {
        	displayName = agent.getName();
        }
        else { // agent name >= 2 
        	displayName = agent.getName().substring(0,2);
        }*/
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        
        if (finalList != null) {
    		if (!finalList.isEmpty()){
    			if (finalList.size()>=1){
    				xPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getX();
    				yPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getY();
    				finalList.remove(finalList.get(finalList.size()-1));
    			}
    		}
    	}
        
        if (xPos == xDestination && yPos == yDestination && command == Command.CookingFood) {
        	//cook.msgAtTable();
        	command=Command.noCommand;
        }
        else if (xPos == xDestination && yPos == yDestination && command == Command.GettingOrdersFromWheel) {
        	cook.msgAtLocation();
        	command=Command.noCommand;
        }
        else if (xPos == xDestination && yPos == yDestination && command == Command.ReturningToGrill) {
        	cook.msgAtLocation();
        	command = Command.noCommand;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
    	g.drawString(order1, order_1X, order_1Y);
    	g.drawString(order2, order_2X, order_2Y);
    	g.drawString(order3, order_3X, order_3Y);
    	g.drawImage(wheelImg, wheelX, wheelY, wheelIcon.getImageObserver());
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

	public void DoCookFood(String choice) {
		if(order1.equalsIgnoreCase("")) {
			order1 = choice;
		}
		else if(order2.equalsIgnoreCase("")) {
			order2 = choice;
		}
		else if(order3.equalsIgnoreCase("")) {
			order3 = choice;
		}
		command = Command.CookingFood;
	}

	public void DoPlating(String choice) {
		if(order1.equalsIgnoreCase(choice) && order_1X == 661) {
			order_1X = plate_1X;
			order_1Y = plate_1Y;
		}
		else if(order2.equalsIgnoreCase(choice) && order_2X == 661) {
			order_2X = plate_2X;
			order_2Y = plate_2Y;
		}
		else if(order3.equalsIgnoreCase(choice) && order_3X == 720) {
			order_3X = plate_3X;
			order_3Y = plate_3Y;
		}
	}
	
	public void DoClearPlatingArea(String choice) {
		if(order1.equalsIgnoreCase(choice) && order_1Y == plate_1Y) {
			order1 = "";
			order_1X = 661;
			order_1Y = 459;
		}
		else if(order2.equalsIgnoreCase(choice) && order_2Y == plate_2Y) {
			order2 = "";
			order_2X = 661;
			order_2Y = 484;
		}
		else if(order3.equalsIgnoreCase(choice) && order_3Y == plate_3Y) {
			order3 = "";
			order_3X = 720;
			order_3Y = 484;
		}
	}
	
	public void DoGetOrdersFromWheel() {
		setDestination(wheelX, wheelY);
		command = Command.GettingOrdersFromWheel;
	}
	
	public void DoReturnToGrill() {
		setDestination(xStart, yStart);
		command = Command.ReturningToGrill;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	public void setDestination(int x, int y) {
		xDestination = x;
		yDestination = y;
	    newAStar = new aStar(xPos, yPos, xDestination, yDestination, gui.restaurant4Panel.grid);
	    finalList = newAStar.getBestPath();
	}
}
