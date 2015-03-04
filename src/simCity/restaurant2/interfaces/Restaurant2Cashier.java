package simCity.restaurant2.interfaces;

import simCity.restaurant2.Restaurant2CashierRole.Check;

public interface Restaurant2Cashier {

	public abstract String getMaitreDName();

	public abstract String getName();

	/**
	 * Messages
	 */

	public abstract void ProduceCheck(Restaurant2Waiter w, Restaurant2Customer c, String choice);

	public abstract void HereIsPayment(Check check, double cash);
	
	public abstract void HereIsCost(Restaurant2Market m, double price);

}