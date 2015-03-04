package simCity.restaurant5.gui;

import java.awt.*;

import java.util.concurrent.Semaphore;

import simCity.interfaces.*;
import simCity.restaurant5.*;
import simCity.restaurant5.interfaces.Restaurant5Customer;
import simCity.gui.*;

public class Restaurant5CustomerGui implements Gui{
	public Restaurant5Customer agent = null;
	public Restaurant5Panel restPanel;
	private boolean isPresent = true;
	private boolean isHungry = false;
	
	private int xPos, yPos;
	private int xDestination, yDestination;

	private enum Command {
		noCommand, GoToSeat, GoToCashier,LeaveRestaurant
	};

	private Command command = Command.noCommand;

	public static final int xTable = 185;
	public static final int yTable = 375;
	public static final int xHost = 630;
	public static final int yHost = 485;
	public static final int xDoor = 650;
	public static final int yDoor = 710;
	
	private String FoodString = " ";

	public Restaurant5CustomerGui(Restaurant5Customer c, Restaurant5Panel rpanel) { // HostAgent m) {
		agent = c;
		xPos = 665;
		yPos = 710;
		xDestination = xHost;
		yDestination = yHost;
		// maitreD = m;
		this.restPanel = rpanel;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToSeat)
				agent.msgAnimationFinishedGoToSeat();
			else if (command == Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out
						.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
		//		gui.setCustomerEnabled(agent);
			}
			command = Command.noCommand;
		}
	}

	public void setFoodString(String food) {
		switch (food) {
		case "Steak":
			FoodString = "ST";
			break;
		case "Salad":
			FoodString = "SA";
			break;
		case "Chicken":
			FoodString = "CH";
			break;
		case "Pizza":
			FoodString ="PI";
			break;
		default:
			FoodString = " ";
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		g.drawString( FoodString, xPos, yPos);
	}
	

	public boolean isPresent() {
		return isPresent;
	}

	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}

	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToCashier() {
		xDestination = 545;
		yDestination = 565;
		command = Command.GoToCashier;
	}
	
	
	public void DoGoToSeat(int seatnumber) {// later you will map seatnumber to
											// table coordinates.
		switch (seatnumber) {
		case 1:
			xDestination = xTable;
			yDestination = yTable;
			break;
		case 2:
			xDestination = xTable + 150;
			yDestination = yTable;
			break;
		case 3:
			xDestination = xTable;
			yDestination = yTable - 150;
			break;
		case 4:
			xDestination = xTable + 150;
			yDestination = yTable - 150;
			break;
		default:
			xDestination = xTable;
			yDestination = yTable;
		}

		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = xDoor;
		yDestination = yDoor;
		command = Command.LeaveRestaurant;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return xPos;
	}


	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return yPos;
	}

	public Restaurant5Panel getPanel() {
		return restPanel;
	}

	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		xDestination = x;
		yDestination = y;
	}
}
