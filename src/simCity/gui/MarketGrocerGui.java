package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simCity.interfaces.MarketGrocer;
import simCity.interfaces.MarketGrocerGuiInterface;

public class MarketGrocerGui implements Gui, MarketGrocerGuiInterface{

	public static final int xGrocer = 400;
	public static final int yGrocer = 50;
	public static final int xBreak = 400;
	public static final int yBreak = 50;
	public static final int xDropOff = 340;
	public static final int yDropOff = 374;
	public static final int xWait = 100;
	public static final int yWait = 500;
	/* Different product locations (shelfs) */
	public static final int xCar = 550;
	public static final int yCar = 110;
	public static final int xBread = 440;
	public static final int yBread = 170;
	public static final int xMilk = 430;
	public static final int yMilk = 230;
	public static final int xEggs = 380;
	public static final int yEggs = 290;
	private MarketGrocer mgrocer = null;
	private boolean isPresent = true;
	private boolean isOnBreak = false;
	private boolean isTired = false;
	
	SimCityGui gui;
	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private enum Command {
		noCommand, GoHome, GoPickUpCar, GoPickUpMilk, GoPickUpBread, GoPickUpEggs,
		GoOnBreak, GoBackFromBreak, GoDropOffProducts, GoToDT
	}
	private Command command = Command.noCommand;
	
	public static int xHome = 333;
	public static int yHome = 100;
	public int xDT = 900;
	public int yDT = 150;
	public MarketGrocerGui(MarketGrocer mg, SimCityGui gui) {
		mgrocer = mg; 
		xPos = xHome;
		yPos = yHome;
		xDestination = xHome;
		yDestination = yHome;
	//	NewHomePosition();
		this.gui = gui;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#updatePosition()
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
			if (command == Command.GoHome) {
				mgrocer.msgAtHome();
				command = Command.noCommand;
			}
			else if (command == Command.GoPickUpCar) {
				mgrocer.msgAtCar();
				//command = Command.noCommand;
			}
			else if (command == Command.GoPickUpBread) {
				mgrocer.msgAtBread();
				//command = Command.noCommand;
			}
			else if (command == Command.GoPickUpMilk) {
				mgrocer.msgAtMilk();
				//command = Command.noCommand;
			}
			else if (command == Command.GoPickUpEggs) {
				mgrocer.msgAtEggs();
				//command = Command.noCommand;
			}
			else if (command == Command.GoToDT) {
				mgrocer.msgAtDT();
				//command = Command.noCommand;
			}
			else if (command == Command.GoDropOffProducts) {
				mgrocer.msgAtDropOffArea();
				//command = Command.noCommand;
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fillRect(xPos, yPos, 20, 20);
	}
	

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#isPresent()
	 */
	@Override
	public boolean isPresent() {
		return isPresent;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#setTired()
	 */
	@Override
	public void setTired() {
		isTired = true;
//		mgrocer.gotTired();
		
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#onBreak()
	 */
	@Override
	public boolean onBreak() {
		return isOnBreak;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#DoGoOnBreak()
	 */
	@Override
	public void DoGoOnBreak() {
		isOnBreak = true;
		
		xDestination = (xBreak);
		yDestination = (yBreak);
		command = Command.GoOnBreak;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#setPresent(boolean)
	 */
	@Override
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#DoGoHome()
	 */
	@Override
	public void DoGoHome() {
		System.out.println("Going back to Grocer home.");
		xDestination = xHome;
		yDestination = yHome;
		command = Command.GoHome;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#DoGetCar()
	 */
	@Override
	public void DoGetCar() {
		System.out.println("Picking up Car.");
		xDestination = xCar;
		yDestination = yCar;
		command = Command.GoPickUpCar;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#DoGetMilk()
	 */
	@Override
	public void DoGetMilk() {
		System.out.println("Picking up Milk.");
		xDestination = xMilk;
		yDestination = yMilk;
		command = Command.GoPickUpMilk;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#DoGetEggs()
	 */
	@Override
	public void DoGetEggs() {
		System.out.println("Picking up Eggs.");
		xDestination = xEggs;
		yDestination = yEggs;
		command = Command.GoPickUpEggs;
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#DoGetBread()
	 */
	@Override
	public void DoGetBread() {
		System.out.println("Picking up Bread.");
		xDestination = xBread;
		yDestination = yBread;
		command = Command.GoPickUpBread;
	}
	@Override
	public void DoGetSteak() {
		System.out.println("Picking up Steak.");
		xDestination = xBread;
		yDestination = yBread;
		command = Command.GoPickUpBread;
	}
	@Override
	public void DoGetPizza() {
		System.out.println("Picking up Pizza.");
		xDestination = xBread;
		yDestination = yBread;
		command = Command.GoPickUpBread;
	}
	@Override
	public void DoGetSalad() {
		System.out.println("Picking up Salad.");
		xDestination = xBread;
		yDestination = yBread;
		command = Command.GoPickUpBread;
	}
	@Override
	public void DoGetChicken() {
		System.out.println("Picking up Chicken.");
		xDestination = xBread;
		yDestination = yBread;
		command = Command.GoPickUpBread;
	}
	
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#DoDropOffProducts()
	 */
	@Override
	public void DoDropOffProducts() {
		System.out.println("Dropping off Products.");
		xDestination = xDropOff;
		yDestination = yDropOff;
		command = Command.GoDropOffProducts;
	}

	public void DoLoadProducts() {
		xDestination = xDT;
		yDestination = yDT;
		command = Command.GoToDT;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#getX()
	 */
	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#getY()
	 */
	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return yPos; 
		
	}

	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#setDestination(int, int)
	 */
	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		xPos = x;
		yPos = y; 
		
	}
	
	/* (non-Javadoc)
	 * @see simCity.gui.MarketGrocerGuiInterface#NewHomePosition()
	 */
	@Override
	public void NewHomePosition() {
		yHome += 55;
	}
}
