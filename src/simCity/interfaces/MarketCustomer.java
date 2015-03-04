package simCity.interfaces;

import java.util.ArrayList;

public interface MarketCustomer {

	public abstract void setGrocer(MarketGrocer grocer);

	public abstract void setCashier(MarketCashier cashier);

	public abstract String getCustomerName();

	/*~~~~~~~~~~~~~~~~~~~~~ MESSAGES ~~~~~~~~~~~~~~~~~~*/
	public abstract void IAmInNeed();

	public abstract void msgIRetrieved(String type);

	public abstract void msgHereIsBill(int total);

	public abstract void msgHereIsChange(int CashierChange);

	public abstract void msgWeHaveNoMore(String product);

	public abstract void msgAtMarket();

	public abstract void msgAtGrocer();

	public abstract void msgAtPickUp();

	public abstract void msgAtWaitingArea();
	
	public abstract void msgLeftMarket();

	public abstract void msgAtCashier();



}
