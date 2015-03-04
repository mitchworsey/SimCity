package simCity.test.mock;

import java.util.ArrayList;

import simCity.market.*;
import simCity.interfaces.*;

public class MockMarketCustomerRole extends Mock implements MarketCustomer{

	public EventLog log = new EventLog();
	
	public MockMarketCustomerRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setGrocer(MarketGrocer grocer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(MarketCashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void IAmInNeed() { // animation
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received IAmInNeed() from the MarketCustomerGui."));
	}

	@Override
	public void msgIRetrieved(String type) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgIRetrieved from the MarketGrocer. Product retrieved: " + type));
	}

	@Override
	public void msgHereIsBill(int total) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsBill from the MarketGrocer. Bill is " + total));
	}

	@Override
	public void msgHereIsChange(int CashierChange) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsChange from the MarketCashier. Change is " + CashierChange));
	}

	@Override
	public void msgWeHaveNoMore(String product) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgWeHaveNoMore from the MarketGrocer. Out of Product: " + product));
	}

	@Override
	public void msgAtMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtGrocer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtPickUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtWaitingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeftMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}
	
}
