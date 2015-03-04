package simCity.interfaces;

import java.util.concurrent.Semaphore;

import javax.swing.Icon;

import simCity.Location;
import simCity.Role;

public interface Person {

	public abstract void setBusDestination(String BusStop);
	
	public abstract void setBusLocation(String BusStop);

	public abstract Role findRole(String s);

	public abstract void ToHousing();

	public abstract void ToBank();

	public abstract void ToRestaurant1();

	public abstract void ToRestaurant2();
	
	public abstract void ToRestaurant3();
	
	public abstract void ToRestaurant4();
	
	public abstract void ToRestaurant5();

	public abstract void ToMarket();

	public abstract void ToBusStop(String busStop);

	public abstract void ArrivedAtHousing();
	
	public abstract void ArrivedAtHousingOffice();

	public abstract void ArrivedAtHousingOfficeOwner();

	public abstract void ArrivedAtHousingOfficeManager();

	public abstract void ArrivedAtBank();

	public abstract void ArrivedAtBankTeller();
	
	public abstract void ArrivedAtBankGuard();

	public abstract void ArrivedAtMarket();

	public abstract void ArrivedAtMarketGrocer();

	public abstract void ArrivedAtBusStop();
	
	public abstract void ArrivedAtRestaurant1();

	public abstract void ArrivedAtRestaurant1Waiter();

	public abstract void ArrivedAtRestaurant1Host();

	public abstract void ArrivedAtRestaurant1Cook();

	public abstract void ArrivedAtRestaurant1Cashier();
	
	public abstract void ArrivedAtRestaurant2();
	
	public abstract void ArrivedAtRestaurant3();
	
	public abstract void ArrivedAtRestaurant4();
	
	public abstract void ArrivedAtRestaurant5();
	
	public abstract void BusIsHere(Bus b);
	
	public abstract void BusAtDestination();

	public abstract void OutOfComponent(Role r);

	public abstract boolean pickAndExecuteAnAction();

	public abstract void stateChanged();

	public abstract void print(String s);
	
	public abstract void Do(String s);

	public abstract String getName();

	public abstract void msgAtDestination();

	public abstract void ToHousingOffice();
	
	public abstract double getMoney();
	
	public abstract void setMoney(double amount);
	
	public abstract void addMoney(double amount);
	
	public abstract void subtractMoney(double amount);
	
	public abstract void setHousingLocation(String s);
	
	public abstract String getHousingLocation();
	
	public abstract void setTimeHouseBought(double time);
	
	public abstract Double getTimeHouseBought();
	
	public abstract void setAIEnabled(boolean b);
	
	public abstract boolean getAIEnabled();

	public abstract void ToBank(Role r);

	public abstract void ToMarket1();

	public abstract void ToMarket2();
}
