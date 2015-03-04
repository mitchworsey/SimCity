package simCity.interfaces;

import simCity.house.House;
import simCity.interfaces.HouseCustomer;

public interface HouseOwner {

	public abstract void msgIWantToLiveHere(HouseCustomer r);

	public abstract void msgHereIsSecurityDeposit(HouseCustomer r,
			double payment);
	
	public abstract void msgResidentNeedsToPayRent(HouseCustomer r);

	public abstract void msgHereIsRent(HouseCustomer r, double payment);

	public abstract void msgHouseNeedsMaintenance(HouseCustomer hc);

	public abstract void msgPayMaintenanceFee(House house);

	public abstract void msgAtMaintenance();

	public abstract void msgAtRealEstate();

	public abstract void msgLeftOffice();
	
	public abstract void setMaintenanceManager(HouseMaintenanceManager hmm);
	
	
	public abstract void createHouses();

}
