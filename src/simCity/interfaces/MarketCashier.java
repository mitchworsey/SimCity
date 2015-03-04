package simCity.interfaces;

import simCity.market.MarketCashierRole.MyCustomer;


public interface MarketCashier {
	
	public abstract void setGui(MarketCashierGuiInterface mp);

	public abstract void setGrocer(MarketGrocer grocer);

	public abstract void setCustomer(MarketCustomer customer);

	public abstract String getCashierName();

	/*~~~~~~~~~~~~~~~~~~~~~ MESSAGES ~~~~~~~~~~~~~~~~~~*/
	public abstract void msgMyTotalPayment(MarketCustomer c, int total,
			int pay);
	
	public abstract void CompareTotalPayment(MyCustomer c);

}
