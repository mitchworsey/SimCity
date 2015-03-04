package simCity.interfaces;

import java.awt.Graphics2D;

public interface MarketCashierGuiInterface {

	public static final int xGrocer = 200;
	public static final int yGrocer = 100;
	public static final int xWait = 100;
	public static final int yWait = 500;

	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract void setPresent(boolean p);

	public abstract void DoGoToGrocer();

	public abstract void DoGoWait();

	public abstract void DoGoToCashier();

	public abstract void DoExitMarket();

	public abstract int getX();

	public abstract int getY();

	public abstract void setDestination(int x, int y);

	public abstract void NewHomePosition();

}