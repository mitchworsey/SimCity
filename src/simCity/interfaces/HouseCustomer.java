package simCity.interfaces;

import simCity.gui.HouseCustomerGui;
import simCity.house.House;
import simCity.house.HouseOwnerRole;

public interface HouseCustomer {

	public abstract void setGui(HouseCustomerGui g);

	public abstract void setHouse(House h);

	///Messages
	public abstract void msgAskToBuyProperty();

	public abstract void msgHouseUnavailable();

	public abstract void msgPaySecurityDeposit(House h);

	public abstract void msgMoveIn(House h);

	public abstract void msgPayRent();

	public abstract void msgLeftOffice();

	public abstract void msgAtRealEstate();

	public abstract void setOwner(HouseOwner owner);

}