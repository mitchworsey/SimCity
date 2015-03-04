package simCity.Restaurant3.interfaces;

import simCity.Restaurant3.Restaurant3Check;
import simCity.Restaurant3.Restaurant3MarketBill;

public interface Restaurant3Cashier {

	public abstract double getCashInRegister();

	public abstract void setLowCash();

	public abstract void msgProduceCheck(Restaurant3Waiter w,
			Restaurant3Customer c, String choice);

	public abstract void msgMarketBillDelivered(Restaurant3MarketBill mb);

	public abstract void msgHereIsChange(double change);

	public abstract void msgHereIsPayment(Restaurant3Check c, double cash);

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */

	public abstract boolean pickAndExecuteAnAction();

	public abstract void giveCheckToWaiter(Restaurant3Check c);

	public abstract void giveChangeToCustomer(Restaurant3Check c);

	public abstract void payMarketBill(Restaurant3MarketBill mb);

}