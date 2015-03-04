package simCity.restaurant4.gui;


import simCity.gui.Gui;
import simCity.gui.Node;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;
import simCity.interfaces.Person;	
import simCity.restaurant4.interfaces.Restaurant4Host;
import simCity.restaurant4.interfaces.Restaurant4Waiter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant4WaiterGui implements Gui {

	SimCityGui gui;
	Restaurant4CookGui cookGui;
	private Restaurant4Host host = null;
    private Restaurant4Waiter waiter = null;
    private boolean leftTable = true;
    private boolean onBreak = false;
    private String food = "";

    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Map<Integer, Integer> tableXMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> tableYMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> seatXMap = new HashMap<Integer, Integer>();
    private int xPos, yPos;//default waiter position
    private int xDestination, yDestination;//default start position
    private int homePosX, homePosY;//home position
    private int wheelX, wheelY; //shared data wheel position
    private int counterPosX, counterPosY;
    private enum Command {noCommand, GoingToWaitingArea, GetCustomerFromOffscreen, SeatingCustomer, LeavingCustomer, PuttingOrderOnWheel, PickingUpFood};
	private Command command=Command.noCommand;

    //public static final int xTable = 200;
    //public static final int yTable = 250;
    public static int xTable = 200;
    public static int yTable = 250;
    
    public Restaurant4WaiterGui(Restaurant4Waiter waiter, SimCityGui gui, int pos) {
        this.waiter = waiter;
        this.gui = gui;
        newAStar = null;
        
        tableXMap.put(1, 195);
        tableXMap.put(2, 380);
        tableXMap.put(3, 557);
        
        tableYMap.put(1, 246);
        tableYMap.put(2, 246);
        tableYMap.put(3, 246);
        
        seatXMap.put(1, 400);
        seatXMap.put(2, 375);
        seatXMap.put(3, 350);
        seatXMap.put(4, 325);
        seatXMap.put(5, 300);
        seatXMap.put(6, 275);
        seatXMap.put(7, 250);
        seatXMap.put(8, 225);
        seatXMap.put(9, 200);
        seatXMap.put(10, 175);
        seatXMap.put(11, 150);
        seatXMap.put(12, 125);
        
        xPos = 100;
        yPos = pos*22 + 230;
        xDestination = 100;
        yDestination = pos*22 + 230;
        homePosX = 100;
        homePosY = pos*22 + 230;
        
        counterPosX = 563;
        counterPosY = 470;		
        wheelX = 700;
        wheelY = 250;
    }
    
    public void setHost(Restaurant4Host host) {
    	this.host = host;
    }
    
    public void setCookGui(Restaurant4CookGui cook) {
    	cookGui = cook;
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

       /* if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20) & (yDestination == yTable - 20)) {
           //agent.msgAtTable();
        }*/
        
        if (xPos == xDestination && yPos == yDestination) {
        	if(command == Command.SeatingCustomer) {
        		waiter.msgAtDestination();
        		command = Command.noCommand;
        	}
        	else if(command == Command.GoingToWaitingArea) {
        		waiter.msgAtDestination();
            	command = Command.noCommand;
        	}
        	else if(command == Command.LeavingCustomer) {
        		waiter.msgLeftTable();
            	command = Command.noCommand;
        	}
        	else if(command == Command.PuttingOrderOnWheel) {
        		waiter.msgAtDestination();
        		command = Command.noCommand;
        	}
        	else if(command == Command.PickingUpFood) {
        		waiter.msgAtDestination();
            	cookGui.DoClearPlatingArea(food);
            	command = Command.noCommand;
        	}
        	else if(command == Command.GetCustomerFromOffscreen) {
        		waiter.msgAtDestination();
            	command = Command.noCommand;
        	}
        }
        
        if(xPos == homePosX && yPos == homePosY) {
        	leftTable = true;
        }
        else {
        	leftTable = false;
        }
    }

    public void draw(Graphics2D g) {
    	g.setColor(new Color(11916554));
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }
    
    public void setOnBreak() {
		onBreak = true;
		host.msgWantToGoOnBreak(waiter);
	}
    
    public void resetOnBreak() {
    	onBreak = false;
    }
    
    public void returnToWork(Restaurant4Waiter w) {
    	host.msgGoingBackToWork(w);
    }
    
    public void enableBreak() {
    	gui.restaurant4Panel.setWaiterEnabled(waiter);
    }
    
	public boolean onBreak() {
		return onBreak;
	}
    
    public void addWaiter(Restaurant4Waiter w) {
		host.newWaiterAdded(w);
	}
    
    public boolean hasLeftTable() {
    	return this.leftTable;
    }
    
    public void DoGoToWaitingArea(int seat) {
    	setDestination(seatXMap.get(seat) + 20, 120);
    	command = Command.GoingToWaitingArea;
    }
    
    public void DoGoGetCustomer() {
    	setDestination(115, 190);
    	command = Command.GetCustomerFromOffscreen;
    }

    public void DoBringToTable(int tableNum) {
    	xTable = tableXMap.get(tableNum);
    	yTable = tableYMap.get(tableNum);
    	
    	setDestination(xTable+20, yTable-20);
        command = Command.SeatingCustomer;
    }

    public void DoLeaveCustomer() {
    	setDestination(homePosX, homePosY);
        command = Command.LeavingCustomer;
    }
    
    public void DoGoPickUpFood(String choice) {
    	setDestination(counterPosX, counterPosY);
    	food = choice;
		command = Command.PickingUpFood;
	}
    
    public void DoPlaceOrderOnWheel() {
		setDestination(wheelX, wheelY);
		command = Command.PuttingOrderOnWheel;
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
