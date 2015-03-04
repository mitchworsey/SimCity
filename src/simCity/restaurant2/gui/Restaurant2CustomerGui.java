package simCity.restaurant2.gui;

import simCity.gui.Gui;
import simCity.gui.Restaurant2Panel;
import simCity.gui.SimCityGui;
import simCity.restaurant2.Restaurant2CustomerRole;
import simCity.restaurant2.interfaces.Restaurant2Customer;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Restaurant2CustomerGui implements Gui{

	public Restaurant2CustomerRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	
	Timer timer = new Timer();
	
	public Restaurant2Panel restPanel;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	public int xWaiting = 0;
	public int yWaiting = 80;
	
	final int agentWidth = 20;
	final int agentHeight = 20;
	final int xHome = 0;
	final int yHome = -40;
	final int xCashier = 220;
    final int yCashier = 725;
	
	final int messageWidth = 25;
	final int messageHeight = 17;
	final int tableFoodWidth = 25;
	final int tableFoodHeight = 25;

	private enum Command {NoCommand, FollowWaiter, GoToSeat, LeaveRestaurant, GoToCashier, GoToRestaurant};
	private Command command = Command.NoCommand;
	
	private enum State {NoState, ReviewMenu, MenuReviewed, PlaceOrder, OrderPlaced, EatFood, FoodEaten, NoMoney, NoFood};
	private State state = State.NoState;
	
	String foodChoice = "";
	String displayName = "";

	
	public Restaurant2CustomerGui(Restaurant2CustomerRole c){ //Restaurant2HostRole m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;	
		if(agent.getName().length() == 0) {
        	displayName = "";
        }
        else if (agent.getName().length() == 1 || agent.getName().length() == 2) {
        	displayName = agent.getName();
        }
        else { // agent name >= 2 
        	displayName = agent.getName().substring(0,2);
        }
	}
	
	
	public Restaurant2CustomerGui(Restaurant2CustomerRole c, Restaurant2Panel restPanel){ //Restaurant2HostRole m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		this.restPanel = restPanel;
		if(agent.getName().length() == 0) {
        	displayName = "";
        }
        else if (agent.getName().length() == 1 || agent.getName().length() == 2) {
        	displayName = agent.getName();
        }
        else { // agent name >= 2 
        	displayName = agent.getName().substring(0,2);
        }
	}
	
	public void setRestaurantPanel(Restaurant2Panel restPanel) {
		this.restPanel = restPanel;
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
			if (command == Command.GoToSeat) {
				agent.MsgAtSeat();
				command = Command.NoCommand;
			}
			else if (command == Command.GoToCashier || command == Command.GoToRestaurant) {
				agent.MsgActionComplete();
				command = Command.NoCommand;
			}
			else if (command == Command.LeaveRestaurant) {
				agent.MsgLeftRestaurant();
				isHungry = false;
				//gui.setAgentEnabled(agent);
				command = Command.NoCommand;
				state = State.NoState;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, agentWidth, agentHeight);
		g.setColor(Color.BLACK);
        g.drawString(displayName, xPos, yPos + 2*agentHeight/3);
        
        if (state == State.ReviewMenu) {
        	g.setColor(Color.BLACK);
        	g.drawOval(xPos, yPos - agentHeight, agentWidth, agentHeight);
        	g.setColor(Color.WHITE);
        	g.fillOval(xPos, yPos - agentHeight, agentWidth, agentHeight);
        	g.setColor(Color.BLACK);
        	g.drawString("...", xPos + agentWidth/5, yPos - agentHeight/3);
        }
        
        if (state == State.MenuReviewed) {
        	g.setColor(Color.BLACK);
        	g.drawOval(xPos, yPos - agentHeight, agentWidth, agentHeight);
        	g.setColor(Color.WHITE);
        	g.fillOval(xPos, yPos - agentHeight, agentWidth, agentHeight);
        	g.setColor(Color.BLACK);
        	g.drawString("  !", xPos + agentWidth/4, yPos - agentHeight/3);
        }
        
        if (state == State.PlaceOrder) {
        	g.setColor(Color.BLACK);
        	g.drawOval(xPos, yPos - messageHeight, messageWidth, messageHeight);
        	g.setColor(Color.WHITE);
        	g.fillOval(xPos, yPos - messageHeight, messageWidth, messageHeight);
        	g.setColor(Color.BLACK);
        	g.drawString(foodChoice.substring(0,2) + "!", xPos + messageWidth/5, yPos - messageHeight/3);
        }
        
        if (state == State.OrderPlaced) {
        	g.setColor(Color.BLACK);
        	g.drawRect(xPos + agentWidth, yPos, tableFoodWidth, tableFoodHeight);
        	g.drawString(foodChoice.substring(0,2) + "?", xPos + agentWidth + tableFoodWidth/5, yPos + 2*tableFoodHeight/3);
        }
        
        if (state == State.EatFood ) {
        	g.setColor(Color.CYAN);
        	g.fillRect(xPos + agentWidth, yPos, tableFoodWidth, tableFoodHeight);
        	g.setColor(Color.BLACK);
        	g.drawString(foodChoice.substring(0,2), xPos + agentWidth + tableFoodWidth/5, yPos + 2*tableFoodHeight/3);
        }
        
        if (state == State.NoMoney ) {
        	g.setColor(Color.BLACK);
        	g.drawOval(xPos, yPos - agentHeight, 2*agentWidth, agentHeight);
        	g.setColor(Color.WHITE);
        	g.fillOval(xPos, yPos - agentHeight, 2*agentWidth, agentHeight);
        	g.setColor(Color.BLACK);
        	g.drawString("$ :( $", xPos + agentWidth/6, yPos - agentHeight/3);
        }
        
        if ( state == State.NoFood ) {
        	g.setColor(Color.BLACK);
        	g.drawOval(xPos, yPos - agentHeight, 2*agentWidth, agentHeight);
        	g.setColor(Color.WHITE);
        	g.fillOval(xPos, yPos - agentHeight, 2*agentWidth, agentHeight);
        	g.setColor(Color.BLACK);
        	g.drawString("-__-*;", xPos + agentWidth/6, yPos - agentHeight/3);
        }
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.GotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat() { // Changes the command so the gui knows the Restaurant2CustomerRole has received the FollowMe msg from waiter
		command = Command.FollowWaiter;
	}
	
	public boolean BeginMoveToXY(int xDestination, int yDestination) { 
		// This is the function that is called directly by the Restaurant2WaiterGui. It is called repeatedly while the Restaurant2Waiter is trying to seat the customer till the customer
		// confirms it has gotten the waiter's "FollowMe" message, at which point this function returns true and ends the while loop in the Restaurant2WaiterGui
		if (command == Command.FollowWaiter) {
			this.xDestination = xDestination;
			this.yDestination = yDestination;
			command = Command.GoToSeat;
			restPanel.customerSeatOpen[(yWaiting - 50)/30] = true;
			return true;
		}
		else {
			return false;
		}
	}

	public void DoExitRestaurant() {
		xDestination = xHome;
		yDestination = yHome;
		command = Command.LeaveRestaurant;
	}
	
	public void DoGoToCashier() {
		xDestination = xCashier;
		yDestination = yCashier;
		command = Command.GoToCashier;
	}
	

	public void DoReviewMenu() {
		state = State.ReviewMenu;
		timer.schedule(new TimerTask() {
			public void run() {
				state = State.MenuReviewed;
				agent.MsgReadyToOrder();
				agent.MsgActionComplete();
			}
		},
		12000);
	}
	
	public void DoPlaceOrder(String foodChoice) {
		this.foodChoice = foodChoice;
		state = State.PlaceOrder;
		timer.schedule(new TimerTask() {
			public void run() {
				state = State.OrderPlaced;
				agent.MsgActionComplete();
			}
		},
		5000);
	}
	
	public void DoEatFood() {
		state = State.EatFood;
		timer.schedule(new TimerTask() {
			public void run() {
				state = State.FoodEaten;
				agent.MsgFinishedEating();
				agent.MsgActionComplete();
			}
		},
		8000);
	}
	
	public void DoNoMoneyLeave() {
		state = State.NoMoney;
		DoExitRestaurant();
	}
	
	public void DoNoFoodLeave() {
		state = State.NoFood;
		DoExitRestaurant();
	}
	
	public void DoNoTimeLeave() {
		restPanel.customerSeatOpen[(yWaiting - 50)/30] = true;
		DoExitRestaurant();
	}
	
	public void DoGoToRestaurant() {
		isHungry = true;
		xDestination = xWaiting;
		yDestination = yWaiting;
		command = Command.GoToRestaurant;
	}
	
	
	public void SendWaiterCoordinates(Restaurant2WaiterGui restaurant2WaiterGui) {
		restaurant2WaiterGui.xCustomer = xWaiting;
		restaurant2WaiterGui.yCustomer = yWaiting;
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
