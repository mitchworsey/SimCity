package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simCity.interfaces.*;

public class MarketCashierGui implements Gui, MarketCashierGuiInterface{

	private MarketCashier mcashier = null;
	private boolean isPresent = true;
	private boolean isTired = false;
	
	SimCityGui gui;
	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private enum Command {
		noCommand, enterMarket, GoToGrocer, GoWait, GoPickUpProducts, GoToCashier, LeaveMarket
	}
	private Command command = Command.noCommand;

	public static int xCashier = 637;
	public static int yCashier = 370;

	
	public MarketCashierGui(MarketCashier mcash, SimCityGui gui) {
		mcashier = mcash; 
		xPos = xCashier;
		yPos = yCashier;
		xDestination = xCashier;
		yDestination = yCashier;
		NewHomePosition();
		this.gui = gui;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#updatePosition()
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
			/*if (command == Command.enterMarket)
			else if (command == Command.LeaveMarket) {
				//mcust.msgAnimationFinishedLeaveRestaurant();
				System.out
						.println("about to call gui.setCustomerEnabled(agent);");
				isInNeed = false;
				//gui.setCustomerEnabled(mcust);*/
			//}
			command = Command.noCommand;
		}
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
	}
	

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#isPresent()
	 */
	@Override
	public boolean isPresent() {
		return isPresent;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#setPresent(boolean)
	 */
	@Override
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#DoGoToGrocer()
	 */
	@Override
	public void DoGoToGrocer() {
		xDestination = -20;
		yDestination = -20;
		command = Command.GoToGrocer;
	}
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#DoGoWait()
	 */
	@Override
	public void DoGoWait() {
		xDestination = -20;
		yDestination = -20;
		command = Command.GoWait;
	}
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#DoGoToCashier()
	 */
	@Override
	public void DoGoToCashier() {
		xDestination = -20;
		yDestination = -20;
		command = Command.GoToCashier;
	}
	

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#DoExitMarket()
	 */
	@Override
	public void DoExitMarket() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveMarket;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#getX()
	 */
	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#getY()
	 */
	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#setDestination(int, int)
	 */
	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		xPos = x;
		yPos = y; 
		
	}
	/* (non-Javadoc)
	 * @see simCity.gui.MarketCashierGuiInterface#NewHomePosition()
	 */
	@Override
	public void NewHomePosition() {
		xCashier -= 58;
	}

}
