package simCity.test.mock;

import simCity.gui.HouseResidentGui;
import simCity.house.House;
import simCity.interfaces.BankTeller;
import simCity.interfaces.HouseResident;

public class MockHouseResidentRole extends Mock implements HouseResident{
	
	public EventLog log = new EventLog();
	public MockHouseResidentRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgGotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMakeFood(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodDone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeftHouse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtFridge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtStove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(HouseResidentGui residentGui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGotSleepy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtSink() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDishWasher() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtWasher() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtShower() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCleanDishes() {
		// TODO Auto-generated method stub
		
	}

}
