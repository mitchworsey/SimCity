package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import simCity.interfaces.MarketCustomer;
import simCity.interfaces.MarketCustomerGuiInterface;

public class MarketCustomerGui implements Gui, MarketCustomerGuiInterface{
	public MarketCustomer mcust = null;
	private boolean isPresent = true;
	private boolean isInNeed = false;
	
	public ArrayList<String> groceryList = new ArrayList<String>();
	
	public SimCityGui gui;
	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private enum Command {
		noCommand, enterMarket, GoToGrocer, GoWait, GoPickUpProducts, GoToCashier, LeaveMarket
	}
	//private Command command = Command.noCommand;
	private Command command = Command.GoToGrocer;
	
	public static int xMarket = 190;
	public static int yMarket = 100;
	public static int xGrocer = 250;
	public static int yGrocer = 100;//265;
	/* CASHIERS */
	public static int xCashier1 = 570;
	public static int yCashier1 = 320;	
	public static int xCashier2 = 531;
	public static int yCashier2 = 320;	
	public static int xCashier3 = 492;
	public static int yCashier3 = 320;	
	public static int xCashier4 = 453;
	public static int yCashier4 = 320;	
	
	public MarketCustomerGui(MarketCustomer mc, SimCityGui gui) {
		mcust = mc; 
		xPos = xEntrance;
		yPos = yEntrance;
		//if()
//		xDestination = xEntrance;
//		yDestination = yEntrance;
		xDestination = xMarket;
		yDestination = yMarket;
//		NewGrocerPosition();
		this.gui = gui;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#updatePosition()
	 */
	@Override
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
			
			// if command == Command.enterMarket -> mcust.msgAtMarket();
			// if command == Command.GoToGrocer -> mcust.msgAtMarket();
			// if command == Command.GoWait -> mcust.msgAtMarket();
			// if command == Command.GoPickUpProducts -> mcust.msgAtMarket();
			// if command == Command.GoToCashier -> mcust.msgAtMarket();
			// if command == Command.LeaveMarket -> mcust.msgAtMarket();
			
			if (command == Command.enterMarket) {
				mcust.msgAtMarket();
			}
			else if (command == Command.GoToGrocer) {
				mcust.msgAtGrocer();
			}
			else if (command == Command.GoWait){
				mcust.msgAtWaitingArea();
			}
			else if (command == Command.GoPickUpProducts) {
				mcust.msgAtPickUp();
			}
			else if (command == Command.GoToCashier) {
				mcust.msgAtCashier();
			}
			else if (command == Command.LeaveMarket) {
				//mcust.msgAnimationFinishedLeaveRestaurant();
				System.out
						.println("about to call gui.setCustomerEnabled(agent);");
				mcust.msgLeftMarket();
				isInNeed = false;
				
			}
			command = Command.noCommand;
		}
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 20, 20);
	}
	

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#isPresent()
	 */
	@Override
	public boolean isPresent() {
		return isPresent;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#setNeedy()
	 */
	@Override
	public void setNeedy() {
		isInNeed = true;
		mcust.IAmInNeed();
		setPresent(true);
	}
	
	public void setNeedsList(ArrayList<String> glist) {
		groceryList = glist;
		//go to grocer and order stuff
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#isNeedy()
	 */
	@Override
	public boolean isNeedy() {
		return isInNeed;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#setPresent(boolean)
	 */
	@Override
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#DoEnterMarket()
	 */
	@Override
	public void DoEnterMarket() {
		xDestination = xMarket;
		yDestination = yMarket;
		command = Command.enterMarket;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#DoGoToGrocer()
	 */
	@Override
	public void DoGoToGrocer() {
		xDestination = xGrocer;
		yDestination = yGrocer;
		command = Command.GoToGrocer;
	}
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#DoGoWait()
	 */
	@Override
	public void DoGoWait() {
		xDestination = -20;
		yDestination = -20;
		command = Command.GoWait;
	}
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#DoPickUpProducts()
	 */
	@Override
	public void DoPickUpProducts() {
		xDestination = xPickUpArea;
		yDestination = yPickUpArea;
		command = Command.GoPickUpProducts;
	}
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#DoGoToCashier()
	 */
	@Override
	public void DoGoToCashier() {
		xDestination = xCashier;
		yDestination = yCashier;
		command = Command.GoToCashier;
	}
	

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#DoExitMarket()
	 */
	@Override
	public void DoExitMarket() {
		xDestination = xExit;
		yDestination = yExit;
		command = Command.LeaveMarket;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#getX()
	 */
	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#getY()
	 */
	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return yPos;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#setDestination(int, int)
	 */
	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		xPos = x;
		yPos = y; 
		
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCustomerGuiInterface#NewGrocerPosition()
	 */
	@Override
	public void NewGrocerPosition() {
		yGrocer += 55;
	}
	
}
