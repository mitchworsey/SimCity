package simCity.test.mock;

import java.util.List;

import simCity.Restaurant3.Restaurant3MarketBill;
import simCity.Restaurant3.Restaurant3MarketRole.Order;
import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.Restaurant3.interfaces.Restaurant3Market;

public class Restaurant3MockMarket extends Mock implements Restaurant3Market{
	public EventLog log = new EventLog();
	public Restaurant3Cashier cashier;
	public Restaurant3MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setLowInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List getOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsOrder(Restaurant3Cook c, String choice,
			int numberOrdered) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderProcessed(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(Restaurant3MarketBill mb, double cash) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsPayment from cashier. Total = " + cash));
	}

	@Override
	public void deliverOrder(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliverMarketBill(Restaurant3MarketBill mb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveChangeToCashier(Restaurant3MarketBill mb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processOrder(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outOfFood(Order o) {
		// TODO Auto-generated method stub
		
	}

}
