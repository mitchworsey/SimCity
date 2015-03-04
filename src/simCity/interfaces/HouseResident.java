package simCity.interfaces;

import simCity.interfaces.BankTeller;
import simCity.gui.HouseResidentGui;
import simCity.house.House;

public interface HouseResident {

	///Messages
	
	//Inside House Scenarios
	
	public abstract void msgGotHungry();
	
	public abstract void msgMakeFood(String type);
	
	public abstract void msgFoodDone();
		
	public abstract void msgLeftHouse();
	
	//Semaphore Messages
	
	public abstract void msgAtFridge();
	
	public abstract void msgAtStove();
	
	public abstract void msgAtTable();
	
	public abstract void msgAtBed();

	public abstract void setGui(HouseResidentGui residentGui);

	public abstract void msgGotSleepy();

	public abstract void msgAtSink();

	public abstract void msgAtDishWasher();

	public abstract void msgAtWasher();

	public abstract void msgAtShower();

	public abstract void msgCleanDishes();

}
