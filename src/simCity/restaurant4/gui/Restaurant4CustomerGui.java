package simCity.restaurant4.gui;

import simCity.gui.Gui;
import simCity.gui.Node;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;
import simCity.interfaces.Person;
import simCity.restaurant4.interfaces.Restaurant4Customer;
import simCity.restaurant4.interfaces.Restaurant4Host;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant4CustomerGui implements Gui{

	public Restaurant4Customer customer = null;
	private boolean isPresent = true;
	private boolean isHungry = false;

	//private HostAgent host;
	SimCityGui gui;

	private int xPos, yPos;
	private Map<Integer, Integer> tableXMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> tableYMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> seatXMap = new HashMap<Integer, Integer>();
	private String order = "";
	private int xDestination, yDestination;
	private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
	private enum Command {noCommand, GoToWaitingArea, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	private String displayName = "";

	//public static final int xTable = 200;
	//public static final int yTable = 250;
	public static int xTable = 200;
	public static int yTable = 250;

	public Restaurant4CustomerGui(Restaurant4Customer customer, SimCityGui gui, int xStart, int yStart){ //HostAgent m) {
		
		this.customer = customer;
    	this.gui = gui;
    	
    	xPos = xStart;
        yPos = yStart;
        xDestination = xStart;
        yDestination = yStart;
        newAStar = null;
		
		//maitreD = m;
		this.gui = gui;
		this.order = "";
        
        /*if(agent.pgetName().length() == 0) {
        	displayName = "";
        }
        else if (agent.getName().length() == 1 || agent.getName().length() == 2) {
        	displayName = agent.getName();
        }
        else { // agent name >= 2 
        	displayName = agent.getName().substring(0,2);
        }*/
		
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
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) ((Restaurant4Customer) customer).msgAnimationFinishedGoToSeat();
			else if (command==Command.GoToWaitingArea) {
				customer.msgAnimationFinishedGoToWaitingArea();
			}
			else if (command==Command.LeaveRestaurant) {
				customer.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.restaurant4Panel.setCustomerEnabled(customer);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 20, 20);
		//g.drawString(agent.getName(), xPos, yPos+20);
		g.drawString(order, xPos, yPos+35);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		customer.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoDisplayOrder(String order) {
		this.order = order;
	}

	public void DoGoToSeat(int tableNum) {//later you will map seatnumber to table coordinates.
		xTable = tableXMap.get(tableNum);
    	yTable = tableYMap.get(tableNum);
		setDestination(xTable, yTable);
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		setDestination(75, 170);
		command = Command.LeaveRestaurant;
	}
	
	public void DoGoToWaitingArea(int location) {
		setDestination(seatXMap.get(location), 100);
		command = Command.GoToWaitingArea;
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

	public void removeGui(Restaurant4CustomerGui customerGui) {
		gui.restaurant4Panel.removeGui((Gui) customerGui);
	}
}
