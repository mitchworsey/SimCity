package simCity.test.mock;

import simCity.market.*;
import simCity.market.MarketCashierRole.MyCustomer;
import simCity.interfaces.*;

public class MockMarketCashierRole extends Mock implements MarketCashier{

	public EventLog log = new EventLog();
	
	public MockMarketCashierRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log.add(new LoggedEvent("MarketCashierRole name is: " + name));
		
	}

	@Override
	public void setGrocer(MarketGrocer grocer) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("MarketGrocer is being set as: " + grocer));
	}

	@Override
	public void setCustomer(MarketCustomer customer) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("MarketCustomer is set as: " + customer));
	}

	@Override
	public String getCashierName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgMyTotalPayment(MarketCustomer c, int total, int pay) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("msgMyTotalPayment total is: " + total + " and pay is: " + pay));
	}

	@Override
	public void CompareTotalPayment(MyCustomer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("CompareTotalPayment compares." ));
	}

	@Override
	public void setGui(MarketCashierGuiInterface mp) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("setGui set MarketCashierGui: " + mp));
	}
	
}
