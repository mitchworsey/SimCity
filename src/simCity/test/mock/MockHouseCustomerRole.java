package simCity.test.mock;

import simCity.gui.HouseCustomerGui;
import simCity.house.House;
import simCity.house.HouseOwnerRole;
import simCity.interfaces.HouseCustomer;
import simCity.interfaces.HouseOwner;

public class MockHouseCustomerRole extends Mock implements HouseCustomer{
	public EventLog log = new EventLog();

	public MockHouseCustomerRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setGui(HouseCustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHouse(House h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHouseUnavailable() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMoveIn(House h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayRent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeftOffice() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtRealEstate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAskToBuyProperty() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setOwner(HouseOwner owner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPaySecurityDeposit(House h) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received PaySecurityDeposit from Owner"));
	}

}
