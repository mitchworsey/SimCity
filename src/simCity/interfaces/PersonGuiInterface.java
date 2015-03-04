package simCity.interfaces;

import java.awt.Graphics2D;

import simCity.Location;
import simCity.OrdinaryPerson;
import simCity.OrdinaryPerson.TransportationType;
import simCity.Role;

public interface PersonGuiInterface {

	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract void setPresent(boolean p);

	public abstract int getX();

	public abstract int getY();

	public abstract void setDestination(int x, int y);

	public abstract boolean DoRideBus(Location location);

	public abstract void DoEnterComponent();

	public abstract void DoExitComponent();

	public abstract void DoGoToBusStop(Location location);

	public abstract void DoGoToHousing(Location location);

	public abstract void DoGoToMarket(Location location);

	public abstract void DoGoToMarketGrocer(Location location);

	public abstract void DoGoToBank(Location location);

	public abstract void DoGoToBankTeller(Location location);
	
	public abstract void DoGoToBankGuard(Location location);

	public abstract void DoGoToRestaurant5(Location location);

	public abstract void DoGoToRestaurant4(Location location);

	public abstract void DoGoToRestaurant3(Location location);

	public abstract void DoGoToRestaurant2(Location location);

	public abstract void DoGoToRestaurant1(Location location);

	public abstract void DoGoToRestaurant1Waiter(Location location);

	public abstract void DoGoToRestaurant1Host(Location location);

	public abstract void DoGoToRestaurant1Cook(Location location);

	public abstract void DoGoToRestaurant1Cashier(Location location);

	public abstract void DoEnterHousing(Role r);

	public abstract void DoEnterHousingOfficeOwner(Role r);

	public abstract void DoEnterHousingOfficeManager(Role r);

	public abstract void DoEnterMarket(Role r);

	public abstract void DoEnterMarketGrocer(Role r);

	public abstract void DoEnterBank(Role r);

	public abstract void DoEnterBankTeller(Role r);
	
	public abstract void DoEnterBankGuard(Role r);

	public abstract void DoEnterRestaurant1(Role r);

	public abstract void DoEnterRestaurant1Waiter(Role r);
	
	public abstract void DoEnterRestaurant1Host(Role r);

	public abstract void DoEnterRestaurant1Cook(Role r);

	public abstract void DoEnterRestaurant1Cashier(Role r);

	public abstract void DoEnterRestaurant2(Role r);

	public abstract void DoEnterRestaurant3(Role r);

	public abstract void DoEnterRestaurant4(Role r);

	public abstract void DoEnterRestaurant5(Role r);

	public abstract void DoBoardBus();

	public abstract void DoExitBus(Location location);

	public abstract void DoEnterBusStop();

	public abstract void DoExitBusStop();

	public abstract void DoGoToHousingOffice(Location location);

	public abstract void DoEnterHousingOffice(Role r);
	
	public abstract void DoGoToHousingOffice(Location location, TransportationType type);
	
	public abstract void DoGoToHousingOfficeOwner(Location location,
			TransportationType transportation);

	public abstract void DoGoToHousingOfficeOwner(Location location);

	public abstract void DoGoToHousingOfficeManager(Location location);

	public abstract void DoGoToHousingOfficeManager(Location location,
			TransportationType transportation);
	
	public abstract void DoGoToHousing(Location location, TransportationType type);

	public abstract void DoGoToMarket(Location location, TransportationType type);

	public abstract void DoGoToBank(Location location, TransportationType type);

	public abstract void DoGoToRestaurant5(Location location, TransportationType type);

	public abstract void DoGoToRestaurant4(Location location, TransportationType type);

	public abstract void DoGoToRestaurant3(Location location, TransportationType type);

	public abstract void DoGoToRestaurant2(Location location, TransportationType type);

	public abstract void DoGoToRestaurant1(Location location, TransportationType type);

	public abstract void DoGoToRestaurant1Waiter(Location location, TransportationType type);

	public abstract void DoGoToRestaurant1Host(Location location, TransportationType type);

	public abstract void DoGoToRestaurant1Cook(Location location, TransportationType type);

	public abstract void DoGoToRestaurant1Cashier(Location location, TransportationType type);

	public abstract void DoGoToBankTeller(Location location, TransportationType type);

	public abstract void didAcquire();

	public abstract void setAgent(OrdinaryPerson ordinaryPerson);

	public abstract void DoGoToMarketGrocer(Location location, TransportationType transportation);


}