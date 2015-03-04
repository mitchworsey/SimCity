package simCity.restaurant2.interfaces;

public interface Restaurant2Market {

	public abstract String getMaitreDName();

	public abstract String getName();

	/**
	 * Messages
	 */

	// from Restaurant2CookRole
	public abstract void INeedFood(Restaurant2Cook c, String foodChoice, int amount);
	
	public abstract void HereIsPayment(double money);

	public abstract void depleteMarket();

	public abstract void replenishMarket();
	

}