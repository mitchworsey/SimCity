package simCity.test.mock;

import java.awt.Graphics2D;

import simCity.Location;
import simCity.OrdinaryPerson;
import simCity.OrdinaryPerson.TransportationType;
import simCity.Role;
import simCity.interfaces.PersonGuiInterface;

public class MockPersonGui extends Mock implements PersonGuiInterface {

	public MockPersonGui(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean DoRideBus(Location location) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void DoEnterComponent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoExitComponent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToBusStop(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToHousing(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToMarket(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToBank(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant5(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant4(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant3(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant2(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterHousing(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterMarket(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterBank(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant1(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant2(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant3(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant4(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant5(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoBoardBus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoExitBus(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterBusStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoExitBusStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant1Waiter(Role r) {
		
	}
	
	public void DoGoToHousingOffice(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant1Host(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant1Cook(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterRestaurant1Cashier(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Waiter(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Host(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Cook(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Cashier(Location location) {
		
	}
	
	public void DoEnterHousingOffice(Role r) {
		// TODO Auto-generated method stub
		
	}

	public void DoGoToBankTeller(Location location) {
		
	}

	public void DoGoToHousingOffice(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	
	public void DoEnterBankTeller(Role r) {

	}
	
	public void DoGoToHousing(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToMarket(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToBank(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant5(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant4(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant3(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant2(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Waiter(Location location,
			TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Host(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Cook(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToRestaurant1Cashier(Location location,
			TransportationType type) {

	}

	@Override
	public void DoGoToBankGuard(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterBankGuard(Role r) {
		
	}

	public void DoGoToBankTeller(Location location, TransportationType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didAcquire() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAgent(OrdinaryPerson ordinaryPerson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToMarketGrocer(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterMarketGrocer(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToMarketGrocer(Location location,
			TransportationType transportation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterHousingOfficeOwner(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEnterHousingOfficeManager(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToHousingOfficeOwner(Location location,
			TransportationType transportation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToHousingOfficeOwner(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToHousingOfficeManager(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToHousingOfficeManager(Location location,
			TransportationType transportation) {
		// TODO Auto-generated method stub
		
	}

}
