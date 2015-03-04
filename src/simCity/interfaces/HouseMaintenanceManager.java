package simCity.interfaces;

import simCity.house.House;

public interface HouseMaintenanceManager {

	///Messages
	public abstract void msgINeedMaintenance(House h);

	public abstract void msgHereIsMaintenanceFee(double payment);

	public abstract void msgAtFridge();

	public abstract void msgAtStove();

	public abstract void msgAtTable();

	public abstract void msgAtBed();

	public abstract void msgAtShower();
	
	public abstract void msgAtSink();
	
	public abstract void msgAtDishWasher();
	
	public abstract void msgAtWasher();

	public abstract void msgLeftHouse();

	public abstract void msgLeftOffice();

	public abstract void msgAtMaintenance();

	public abstract void msgPlacedWorkOrder();

}
