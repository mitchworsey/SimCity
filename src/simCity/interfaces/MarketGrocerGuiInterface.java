package simCity.interfaces;

import java.awt.Graphics2D;

public interface MarketGrocerGuiInterface {



	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract void setTired();

	public abstract boolean onBreak();

	public abstract void DoGoOnBreak();

	public abstract void setPresent(boolean p);

	public abstract void DoGoHome();

	public abstract void DoGetCar();

	public abstract void DoGetMilk();

	public abstract void DoGetEggs();

	public abstract void DoGetBread();

	public abstract void DoDropOffProducts();

	public abstract int getX();

	public abstract int getY();

	public abstract void setDestination(int x, int y);

	public abstract void NewHomePosition();

	public abstract void DoLoadProducts();

	public abstract void DoGetSteak();

	public abstract void DoGetPizza();

	public abstract void DoGetSalad();

	public abstract void DoGetChicken();

}