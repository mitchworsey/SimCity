package simCity.interfaces;

import java.awt.Graphics2D;

public interface BankCustomerGuiInterface {
	
	public abstract void updatePosition();
	
	public abstract void draw(Graphics2D g);
	
	public abstract boolean isPresent();
	
	public abstract void setPresent(boolean p);
	
	public abstract int getX();
	
	public abstract int getY();
	
	public abstract void setDestination(int x, int y);
	
	public abstract void DoExitBank();
	
	public abstract void DoGoToTeller();
	
	public abstract void AtLocation();

	public abstract void removeGui(BankCustomerGuiInterface gui);
}
