package simCity.Restaurant3;

import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Market;

public class Restaurant3MarketBill {
	public enum MarketBillState
	{produced, distributed, paymentFinished};
	
	public Restaurant3Market m;
	public Restaurant3Cashier c;
	public double bill;
	public double change = 0;
	public MarketBillState mbs;
	
	public Restaurant3MarketBill(Restaurant3Market m, Restaurant3Cashier c, double bill, MarketBillState mbs){
		this.m = m;
		this.c = c;
		this.bill = bill;
		this.mbs = mbs;
	}
}
