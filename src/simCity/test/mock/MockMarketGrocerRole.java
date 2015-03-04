package simCity.test.mock;

import java.util.ArrayList;

import simCity.market.*;
import simCity.market.MarketGrocerRole.MyCustomer;
import simCity.interfaces.*;

public class MockMarketGrocerRole extends Mock implements MarketGrocer {
	public EventLog log = new EventLog();
	public MockMarketGrocerRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log.add(new LoggedEvent("MarketGrocer name is " + name));
	}

	@Override
	public void setCustomer(MarketCustomer customer) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("MarketGrocer customer is set as: " + customer));
	}

	@Override
	public void setCashier(MarketCashier cashier) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("MarketGrocer cashier is set as: " + cashier));
		
	}

	@Override
	public String getGrocerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findIt(MyCustomer c, String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantStuff(MarketCustomer c, ArrayList<String> needs) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgIWantStuff from MarketCustomer. Needs list is: " + needs + ". " + "Needs list size is: " + needs.size()));
	}

	@Override
	public void FindProduct(MyCustomer c, String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BringProductToCust(MyCustomer c, String product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ChargeCustomer(MyCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(MarketGrocerGuiInterface mg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDropOffArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtMilk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtEggs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDT() {
		// TODO Auto-generated method stub
		
	}
}
