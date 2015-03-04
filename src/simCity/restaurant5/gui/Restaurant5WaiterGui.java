package simCity.restaurant5.gui;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.interfaces.*;
import simCity.restaurant5.*;
import simCity.restaurant5.interfaces.*;
import simCity.gui.*;

public class Restaurant5WaiterGui implements Gui {
	private Restaurant5Waiter agent = null;
	private boolean isOnBreak = false;
	private boolean isTired = false;
	
	private Timer timer = new Timer();
	
//	Restaurant5RestaurantGui gui;
	Restaurant5Panel restPanel;

	public int xHome = 100;
	public int yHome = 570;
	private int xCust = 630;
	private int yCust = 485;
	public int xPos = 250, yPos = 610;// default waiter position
	//private int xDestination = -20, yDestination = -20;// default start position
	private int xDestination = xHome, yDestination = yHome;
	
	public static final int xTable1 = 208;
	public static final int yTable1 = 375;

	public static final int xTable2 = 300;
	public static final int yTable2 = 250;

	public static final int xTable3 = 200;
	public static final int yTable3 = 100;

	public static final int xTable4 = 300;
	public static final int yTable4 = 100;

	public static final int xCook = 340;
	public static final int yCook = 260;
	
	public static final int xCashier = 605;
	public static final int yCashier = 535;
	 
	private int xCons = 20;
	private int yCons = 20;

	public String CookOrder;
	
	private String FoodString = " ";
	
	private enum wCommand {
		noCommand, GoToWaitingArea, GoToTable, GoToKitchen, GoOnBreak, 
		GoBackFromBreak, GoToHome, GoToCashier, Waiting
	};

	private wCommand wcommand = wCommand.noCommand;

	public Restaurant5WaiterGui(Restaurant5Waiter agent) {
		this.agent = agent;
	}
	public Restaurant5WaiterGui(Restaurant5Waiter w, Restaurant5Panel gui, int newHome) { // HostAgent m) {

		agent = w;
		xPos = 30;
		yPos = 500;
		//xDestination = 30;//-30;
		//yDestination = 30;//-30;
		xHome += newHome;
		xDestination = xHome;//30;//-30;
		yDestination = yHome;//30;//-30;
		NewHomePosition();
		// maitreD = m;
		this.restPanel = gui;
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
			if (wcommand == wCommand.GoToWaitingArea){
				System.out.println("GOTOWAITINGAREA");
				agent.msgAtWaitingArea();
			}	
			if (wcommand == wCommand.GoToTable) {
				agent.msgAtTable();
			}
			if (wcommand == wCommand.GoToHome) {
				agent.msgAtHome();  
			}
			if (wcommand == wCommand.GoOnBreak) {
				isOnBreak = true;
			}
			if (wcommand == wCommand.GoToCashier) {
				agent.msgAtCashier();
			}
			if (wcommand == wCommand.GoBackFromBreak) {
				isOnBreak = false;
				//gui.setWaiterEnabled(agent);
			}

			else if (wcommand == wCommand.GoToKitchen) {
				agent.msgAtKitchen();
			}
			
			wcommand = wCommand.noCommand;

		}

		if (xPos == 20 && yPos == 20) {
			agent.callStateChange();
		}
		if (xPos == 30 && yPos == 500) {
			agent.callStateChange();
		}
	}
	
	public void setFoodString(String food) {
		switch (food) {
		case "Steak":
			FoodString = "ST?";
			break;
		case "Salad":
			FoodString = "SA?";
			break;
		case "Chicken":
			FoodString = "CH?";
			break;
		case "Pizza":
			FoodString ="PI?";
			break;
		default:
			FoodString = " ";
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, xCons, yCons);
		g.drawString( FoodString, xPos, yPos);
	}

	public boolean isPresent() {
		return true;
	}
	
	public void setTired() {
		isTired = true;
		agent.gotTired();
		
	}
	

	public boolean onBreak() {
		return isOnBreak;
	}
	
	public void DoGoOnBreak() {
		isOnBreak = true;
		
		xDestination = (-xCons);
		yDestination = (-yCons);
		wcommand = wCommand.GoOnBreak;
	}


	public void DoStayWorking() { // changes checkbox to unchecked
		isOnBreak = false;
		isTired = false;
		//gui.setWaiterEnabled(agent);
		wcommand = wCommand.GoBackFromBreak;
	}


	
	public void DoBringToTable(Restaurant5Customer customer, int table) {
		switch (table) {
		case 1: 
			System.out.println("At table 1");
			xDestination = xTable1 + xCons;
			yDestination = yTable1 - yCons;
			break;
		case 2:
			System.out.println("At table 2");
			xDestination = xTable2 + xCons;
			yDestination = yTable2 - yCons;
			break;
		case 3:
			xDestination = xTable3 + xCons;
			yDestination = yTable3 - yCons;
			break;
		case 4:
			xDestination = xTable4 + xCons;
			yDestination = yTable4 - yCons;
			break;
		default:
			xDestination = xTable1 + xCons;
			yDestination = yTable1 - yCons;
			break;
		}
		wcommand = wCommand.GoToTable;

	}

	public void TakeOrder(Restaurant5Customer customer, int table) {
		switch (table) {
		case 1:
			xDestination = xTable1 + xCons;
			yDestination = yTable1 - yCons;
			break;
		case 2:
			xDestination = xTable2 + xCons;
			yDestination = yTable2 - yCons;
			break;
		case 3:
			xDestination = xTable3 + xCons;
			yDestination = yTable3 - yCons;
			break;
		case 4:
			xDestination = xTable4 + xCons;
			yDestination = yTable4 - yCons;
			break;
		default:
			xDestination = xTable1 + xCons;
			yDestination = yTable1 - yCons;
		}
		wcommand = wCommand.GoToTable;
	}

	public void DoGiveOrderToCook(String order) {
		xDestination = xCook;
		yDestination = yCook;
		CookOrder = order;
		wcommand = wCommand.GoToKitchen;
	}

	public void DoPickUpOrder(String order) {
		xDestination = xCook;
		yDestination = yCook;
		wcommand = wCommand.GoToKitchen;

	}

	public void DoGoToCashier() {
		xDestination = xCashier;
		yDestination = yCashier;
		wcommand = wCommand.GoToCashier;
	}

	public void DoGoToWaitArea() {
		xDestination = xCust;
		yDestination = yCust;
		wcommand = wCommand.GoToWaitingArea;
	}
	
	public void DoGoHome() {
		xDestination = (xHome);
		yDestination = (yHome);
		wcommand = wCommand.GoToHome;
	}

	public void waiting() {
		timer.schedule(new TimerTask() {
			public void run() {
				agent.msgWaitComplete();
			}
		}, 2000);
		
		wcommand = wCommand.Waiting;
	}
	
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void NewHomePosition() {
		xHome += 50;
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
	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		xDestination = x;
		yDestination = y;
	}
	public void DoDeliverOrderToWheel() {
		
		xDestination = xCook;
		yDestination = yCook;
		wcommand = wCommand.GoToKitchen;
		// TODO Auto-generated method stub
		
	}
}
