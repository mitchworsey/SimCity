package simCity.interfaces;

import java.awt.Graphics2D;

public interface MarketCustomerGuiInterface {

	public static final int xEntrance = 160;
	public static final int yEntrance = 0;
	public static final int xCashier = 637;
	public static final int yCashier = 460;
	public static final int xWait = 100;
	public static final int yWait = 500;
	public static final int xExit = 520;
	public static final int yExit = 800;
	public static final int xPickUpArea = 340;
	public static final int yPickUpArea = 440;
	/* GROCERS */
	public static final int xGrocer1 = 195;
	public static final int yGrocer1 = 200;
	public static final int xGrocer2 = 150;
	public static final int yGrocer2 = 145;
	public static final int xGrocer3 = 150;
	public static final int yGrocer3 = 90;
	public static final int xGrocer4 = 150;
	public static final int yGrocer4 = 35;

	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract void setNeedy();

	public abstract boolean isNeedy();

	public abstract void setPresent(boolean p);

	public abstract void DoEnterMarket();

	public abstract void DoGoToGrocer();

	public abstract void DoGoWait();

	public abstract void DoPickUpProducts();

	public abstract void DoGoToCashier();

	public abstract void DoExitMarket();

	public abstract int getX();

	public abstract int getY();

	public abstract void setDestination(int x, int y);

	public abstract void NewGrocerPosition();

}