package simCity.test.mock;

import simCity.house.House;
import simCity.interfaces.HouseCustomer;
import simCity.interfaces.HouseMaintenanceManager;
import simCity.interfaces.HouseOwner;
import simCity.interfaces.HouseResident;

public class MockHouseOwnerRole extends Mock implements HouseOwner{

	public MockHouseOwnerRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgPayMaintenanceFee(House house) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtMaintenance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtRealEstate() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsSecurityDeposit(HouseCustomer r, double payment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgResidentNeedsToPayRent(HouseCustomer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsRent(HouseCustomer r, double payment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeftOffice() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantToLiveHere(HouseCustomer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaintenanceManager(HouseMaintenanceManager hmm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createHouses() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHouseNeedsMaintenance(HouseCustomer hc) {
		// TODO Auto-generated method stub
		
	}

}

