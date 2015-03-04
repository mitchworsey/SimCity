package simCity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import agent.Agent;
import simCity.Restaurant1.Restaurant1CashierRole;
import simCity.Restaurant1.Restaurant1CookRole;
import simCity.Restaurant1.Restaurant1CustomerRole;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.Restaurant1.Restaurant1NormalWaiterRole;
import simCity.Restaurant1.Restaurant1PCWaiterRole;
import simCity.Restaurant1.Restaurant1WaiterRole;
import simCity.Restaurant3.Restaurant3CustomerRole;
import simCity.bank.BankCustomerRole;
import simCity.bank.BankGuardRole;
import simCity.bank.BankRobberRole;
import simCity.bank.BankTellerRole;
import simCity.interfaces.PersonGuiInterface;
import simCity.gui.SimCityGui;
import simCity.house.House;
import simCity.house.HouseCustomerRole;
import simCity.house.HouseMaintenanceManagerRole;
import simCity.house.HouseOwnerRole;
import simCity.house.HouseResidentRole;
import simCity.interfaces.*;
import simCity.market.MarketCashierRole;
import simCity.market.MarketCustomerRole;
import simCity.market.MarketGrocerRole;
import simCity.restaurant2.*;
import simCity.restaurant2.interfaces.*;
import simCity.Restaurant3.interfaces.*;
import simCity.Restaurant3.*;
import simCity.restaurant4.*;
import simCity.restaurant4.interfaces.*;
import simCity.restaurant5.*;
import simCity.restaurant5.interfaces.*;
import simCity.test.mock.EventLog;
import simCity.test.mock.LoggedEvent;
import simCity.Clock;

public class OrdinaryPerson extends Agent implements Person {

	/*
	 * Data:
	 */
	
	String name;
	public double money;
	int hungerLevel = 0;
	int tiredLevel = 0;
	int houseStock = 1;
	public int componentNumber = 5;
	public EventLog log = new EventLog();
	
	Semaphore currentSemaphore = null;
	
	int halfAnHour = Clock.halfAnHour;
	final int hungerTime = 6500;
	final int tiredTime = 7500;
	
	public PersonGuiInterface gui;  // TEMPORRARY COMMENT OUT
	boolean anyTrue;
	public boolean insideComponent = false;
	public boolean AIEnabled = true;
	BusStopComponent location;
	BusStopComponent destination;
	Bus currentBus;
	String housingBehavior = "eat";
	String housingLocation = null;
	
	Timer timer = new Timer();
	
	private Semaphore actionComplete = new Semaphore(0, true);
	private Semaphore atDestination = new Semaphore(0, true);
	
	public Double timeHouseBought = null;
	int payMaintenanceDayCount = 0;
	int payRentDayCount = 0;
	
	
	public List<Role> roles = new ArrayList<Role>();
	
	PersonState state = PersonState.normal;
	PersonState tempState = PersonState.none;
	
	enum PersonState {none, normal, traveling, toHousing, toBank, toMarket, toMarketGrocer, toBusStop, toRestaurant1, toRestaurant2,
		toRestaurant3, toRestaurant4, toRestaurant5, toHousingOffice, toHousingOfficeOwner, toHousingOfficeManager, atHousing, atBank, atBankTeller, atBankGuard, atMarket, atMarketGrocer, atBusStop, atRestaurant1, atRestaurant2,
		atRestaurant3, atRestaurant4, atRestaurant5, atHousingOffice, atHousingOfficeOwner, atHousingOfficeManager,
		waitingForBus, busArrived, onBus, atDestination, outOfComponent, leavingBuilding,
		toRestaurant1Waiter, toRestaurant1Cook, toRestaurant1Host, toRestaurant1Cashier, atRestaurant1Waiter, atRestaurant1Cook, atRestaurant1Host, atRestaurant1Cashier, 
		toBankTeller, toBankGuard};
	
	public TransportationType transportation = TransportationType.car;
	
	public enum TransportationType {walk, car, bus, retardDriver, retardWalker};
	

	public Map<String, CityComponent> cityMap = new HashMap<String, CityComponent>();
	
	//From gui
	public List<Semaphore> acquireSemaphores = new ArrayList<Semaphore>();
	
	
	/*
	 * Constructor and utility functions:
	 */
	
	public OrdinaryPerson(String name, double money) {
		super();
		this.name = name;
		this.money = money;
		
		if (name.equals("durr") || name.equals("durrr") ) {
			this.transportation = TransportationType.retardWalker;
			AIEnabled = false;
		}
		
		if (name.equals("derp") || name.equals("derpp") ) {
			this.transportation = TransportationType.retardDriver;
			AIEnabled = false;
		}
		
		if (name.equals("abc")) {
			AIEnabled = false;
		}
		if (name.equals("walk")) {
			AIEnabled = true;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car")) {
			AIEnabled = true;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus")) {
			AIEnabled = true;
			this.transportation = TransportationType.bus;
		}
		if (name.equals("walk0")) {
			AIEnabled = false;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car0")) {
			AIEnabled = false;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus0")) {
			AIEnabled = false;
			this.transportation = TransportationType.bus;
		}
		
		// START INTERNAL TIMERS
		hungerLevel = 5;
		tiredLevel = 7;
		getHungry();
		getTired();
		checkTime();
	}
	
	public OrdinaryPerson(String name, double money, String transportation) {
		super();
		this.name = name;
		this.money = money;
		
		if (transportation.equals("walk")) {
			this.transportation = TransportationType.walk;
		}
		else if (transportation.equals("bus")) {
			this.transportation = TransportationType.bus;
		}
		else if (transportation.equals("car")) {
			this.transportation = TransportationType.car;
		}
		else if (transportation.equals("retardDriver") || transportation.equals("retardCar")) {
			this.transportation = TransportationType.retardDriver;
			AIEnabled = false;
		}
		else if (transportation.equals("retardWalker") || transportation.equals("retardPerson")) {
			this.transportation = TransportationType.retardWalker;
			AIEnabled = false;
		}
		
		if (name.equals("durr") || name.equals("durrr") ) {
			this.transportation = TransportationType.retardWalker;
			AIEnabled = false;
		}
		if (name.equals("derp") || name.equals("derpp") ) {
			this.transportation = TransportationType.retardDriver;
			AIEnabled = false;
		}
		if (name.equals("abc")) {
			AIEnabled = false;
		}
		if (name.equals("walk")) {
			AIEnabled = true;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car")) {
			AIEnabled = true;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus")) {
			AIEnabled = true;
			this.transportation = TransportationType.bus;
		}
		if (name.equals("walk0")) {
			AIEnabled = false;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car0")) {
			AIEnabled = false;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus0")) {
			AIEnabled = false;
			this.transportation = TransportationType.bus;
		}
		
		// START INTERNAL TIMERS
		hungerLevel = 5;
		tiredLevel = 7;
		getHungry();
		getTired();
		checkTime();
	}
	
	
	public OrdinaryPerson(String name, double money, PersonGuiInterface gui) {
		super();
		this.name = name;
		this.money = money;
		this.gui = gui;
		
		if (name.equals("durr") || name.equals("durrr") ) {
			this.transportation = TransportationType.retardWalker;
			AIEnabled = false;
		}
		
		if (name.equals("derp") || name.equals("derpp") ) {
			this.transportation = TransportationType.retardDriver;
			AIEnabled = false;
		}
		
		if (name.equals("abc")) {
			AIEnabled = false;
		}
		if (name.equals("walk")) {
			AIEnabled = true;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car")) {
			AIEnabled = true;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus")) {
			AIEnabled = true;
			this.transportation = TransportationType.bus;
		}
		if (name.equals("walk0")) {
			AIEnabled = false;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car0")) {
			AIEnabled = false;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus0")) {
			AIEnabled = false;
			this.transportation = TransportationType.bus;
		}
		
		// START INTERNAL TIMERS
		hungerLevel = 5;
		tiredLevel = 7;
		getHungry();
		getTired();
		checkTime();
	}
	
	
	public OrdinaryPerson(String name, double money, PersonGuiInterface gui, String transportation) {
		super();
		if (transportation.equals("walk")) {
			this.transportation = TransportationType.walk;
		}
		else if (transportation.equals("bus")) {
			this.transportation = TransportationType.bus;
		}
		else if (transportation.equals("car")) {
			this.transportation = TransportationType.car;
		}
		else if (transportation.equals("retardDriver") || transportation.equals("retardCar")) {
			this.transportation = TransportationType.retardDriver;
			AIEnabled = false;
		}
		else if (transportation.equals("retardWalker") || transportation.equals("retardPerson")) {
			this.transportation = TransportationType.retardWalker;
			AIEnabled = false;
		}
		this.name = name;
		this.money = money;
		this.gui = gui;
		
		if (name.equals("durr") || name.equals("durrr") ) {
			this.transportation = TransportationType.retardWalker;
			AIEnabled = false;
		}
		
		if (name.equals("derp") || name.equals("derpp") ) {
			this.transportation = TransportationType.retardDriver;
			AIEnabled = false;
		}
		
		if (name.equals("abc")) {
			AIEnabled = false;
		}
		if (name.equals("walk")) {
			AIEnabled = true;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car")) {
			AIEnabled = true;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus")) {
			AIEnabled = true;
			this.transportation = TransportationType.bus;
		}
		if (name.equals("walk0")) {
			AIEnabled = false;
			this.transportation = TransportationType.walk;
		}
		if (name.equals("car0")) {
			AIEnabled = false;
			this.transportation = TransportationType.car;
		}
		if (name.equals("bus0")) {
			AIEnabled = false;
			this.transportation = TransportationType.bus;
		}
		
		// START INTERNAL TIMERS
		hungerLevel = 5;
		tiredLevel = 7;
		getHungry();
		getTired();
		
	}
	
	public void noPersonState() {
		state = PersonState.none;
	}
	
	public void addMoney(double amount) {
		money = money + amount;
	}
	
	public void subtractMoney(double amount) {
		money = money - amount;
	}
	
	public void setMoney(double amount) {
		money = amount;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setAIEnabled(boolean b) {
		AIEnabled = b;
	}
	
	public boolean getAIEnabled() {
		return AIEnabled;
	}
	
	public void setHousingLocation(String s) {
		housingLocation = s;
	}
	
	public String getHousingLocation() {
		return housingLocation;
	}
	
	public void setTimeHouseBought(double time) {
		timeHouseBought = time;
	}
	
	public Double getTimeHouseBought() {
		return timeHouseBought;
	}
	
	public void addComponent(String key, CityComponent c) {
		cityMap.put(key, c);
	}
	
	public void addComponents(Map<String, CityComponent> map) {
		for( Map.Entry<String, CityComponent> entry : map.entrySet() ) {
			cityMap.put(entry.getKey(), entry.getValue());
		}
	}
	
	public String getName() {
		return name;
	}
	 
	/*
	 * Set gui
	 */
	public void setGui(PersonGuiInterface g){
		gui = g;
		gui.setAgent(this);
	}
	
	public void setBusDestination(String BusStop) {
		destination = ((BusStopComponent) cityMap.get(BusStop));
	}
	
	public void setBusLocation(String BusStop) {
		location = ((BusStopComponent) cityMap.get(BusStop));
	}
	
	public Role findRole(String s) {
		if (roles.size() == 0) {
			return null;
		}
		
		/////////////////////////////////				//////////////////////////////////
		///////////////////////////////// MARKET AGENTS ///////////////////////////////////
		
		else if (s.equals("MarketCustomer")) {
			for (Role r : roles) {
				if (r instanceof MarketCustomer) {
					return r;
				}
			}
		}
		
		else if (s.equals("Market3Customer")) {
			for (Role r : roles) {
				if (r instanceof MarketCustomer) {
					if (r.getComponentPanel() == SimCityGui.marketPanel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Market2Customer")) {
			for (Role r : roles) {
				if (r instanceof MarketCustomer) {
					if (r.getComponentPanel() == SimCityGui.market2Panel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Market1Customer")) {
			for (Role r : roles) {
				if (r instanceof MarketCustomer) {
					if (r.getComponentPanel() == SimCityGui.market1Panel) {
						return r;
					}
				}
			}
		}

		else if (s.equals("MarketGrocer")) {
			for (Role r : roles) {
				if (r instanceof MarketGrocer) {
					return r;
				}
			}
		}
		
		else if (s.equals("Market3Grocer")) {
			for (Role r : roles) {
				if (r instanceof MarketGrocer) {
					if (r.getComponentPanel() == SimCityGui.marketPanel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Market2Grocer")) {
			for (Role r : roles) {
				if (r instanceof MarketGrocer) {
					if (r.getComponentPanel() == SimCityGui.market2Panel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Market1Grocer")) {
			for (Role r : roles) {
				if (r instanceof MarketGrocer) {
					if (r.getComponentPanel() == SimCityGui.market1Panel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("MarketCashier")) {
			for (Role r : roles) {
				if (r instanceof MarketCashier) {
					return r;
				}
			}
		}
		
		else if (s.equals("Market3Cashier")) {
			for (Role r : roles) {
				if (r instanceof MarketCashier) {
					if (r.getComponentPanel() == SimCityGui.marketPanel){
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Market2Cashier")) {
			for (Role r : roles) {
				if (r instanceof MarketCashier) {
					if (r.getComponentPanel() == SimCityGui.market2Panel){
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Market1Cashier")) {
			for (Role r : roles) {
				if (r instanceof MarketCashier) {
					if (r.getComponentPanel() == SimCityGui.market1Panel){
						return r;
					}
				}
			}
		}
		
		
		
		
		/////////////////////////////////				  /////////////////////////////////////
		/////////////////////////////////	BANK AGENTS   /////////////////////////////////////
		
		
		else if (s.equals("BankCustomer")) {
			for (Role r : roles) {
				if (r instanceof BankCustomer) {
					return r;
				}
			}
		}
		
		else if (s.equals("Bank2Customer")) {
			for (Role r : roles) {
				if (r instanceof BankCustomer) {
					if (r.getComponentPanel() == SimCityGui.bankPanel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Bank1Customer")) {
			for (Role r : roles) {
				if (r instanceof BankCustomer) {
					if (r.getComponentPanel() == SimCityGui.bank1Panel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("BankTeller")) {
			for (Role r : roles) {
				if (r instanceof BankTeller) {
					return r;
				}
			}
		}
		
		else if (s.equals("Bank2Teller")) {
			for (Role r : roles) {
				if (r instanceof BankTeller) {
					if (r.getComponentPanel() == SimCityGui.bankPanel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Bank1Teller")) {
			for (Role r : roles) {
				if (r instanceof BankTeller) {
					if (r.getComponentPanel() == SimCityGui.bank1Panel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("BankGuard")) {
			for (Role r : roles) {
				if (r instanceof BankGuard) {
					return r;
				}
			}
		}
		
		else if (s.equals("Bank2Guard")) {
			for (Role r : roles) {
				if (r instanceof BankGuard) {
					if (r.getComponentPanel() == SimCityGui.bankPanel){
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Bank1Guard")) {
			for (Role r : roles) {
				if (r instanceof BankGuard) {
					if (r.getComponentPanel() == SimCityGui.bank1Panel){
						return r;
					}
				}
			}
		}
		
		else if (s.equals("BankRobber")) {
			for (Role r : roles) {
				if (r instanceof BankRobber) {
					return r;
				}
			}
		}
		
		else if (s.equals("Bank2Robber")) {
			for (Role r : roles) {
				if (r instanceof BankRobber) {
					if (r.getComponentPanel() == SimCityGui.bankPanel) {
						return r;
					}
				}
			}
		}
		
		else if (s.equals("Bank1Robber")) {
			for (Role r : roles) {
				if (r instanceof BankRobber) {
					if (r.getComponentPanel() == SimCityGui.bank1Panel) {
						return r;
					}
				}
			}
		}
		
		
		
		
		
		/////////////////////////////////					 /////////////////////////////////////
		/////////////////////////////////	HOUSING AGENTS   /////////////////////////////////////
		
		
		else if (s.equals("HousingCustomer")) {
			for (Role r : roles) {
				if (r instanceof HouseResident) {
					return r;
				}
			}
		}
		
		
		else if (s.equals("HousingOfficeCustomer")) {
			for (Role r : roles) {
				if (r instanceof HouseCustomer) {
					return r;
				}
			}
		}

		else if (s.equals("HousingOfficeOwner")) {
			for (Role r : roles) {
				if (r instanceof HouseOwner) {
					return r;
				}
			}
		}

		else if (s.equals("HousingOfficeManager")) {
			for (Role r : roles) {
				if (r instanceof HouseMaintenanceManager) {
					return r;
				}
			}
		}
		
		
		
		
		
		
		/////////////////////////////////					 	 /////////////////////////////////////
		/////////////////////////////////	RESTAURANT1 AGENTS   /////////////////////////////////////
		
		
		else if (s.equals("Restaurant1Customer")) {
			for (Role r : roles) {
				if (r instanceof Restaurant1Customer) {
					return r;
				}
			}
		}
		
		
		else if (s.equals("Restaurant1Waiter")) {
			for (Role r : roles) {
				if (r instanceof Restaurant1Waiter) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant1Host")) {
			for (Role r : roles) {
				if (r instanceof Restaurant1Host) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant1Cook")) {
			for (Role r : roles) {
				if (r instanceof Restaurant1Cook) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant1Cashier")) {
			for (Role r : roles) {
				if (r instanceof Restaurant1Cashier) {
					return r;
				}
			}
		}
		
		
		
		/////////////////////////////////					 	 /////////////////////////////////////
		/////////////////////////////////	RESTAURANT2 AGENTS   /////////////////////////////////////
		
		else if (s.equals("Restaurant2Customer")) {
			for (Role r : roles) {
				if (r instanceof Restaurant2Customer) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant2Waiter")) {
			for (Role r : roles) {
				if (r instanceof Restaurant2Waiter) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant2Host")) {
			for (Role r : roles) {
				if (r instanceof Restaurant2Host) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant2Cook")) {
			for (Role r : roles) {
				if (r instanceof Restaurant2Cook) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant2Cashier")) {
			for (Role r : roles) {
				if (r instanceof Restaurant2Cashier) {
					return r;
				}
			}
		}
		
		
		
		
		/////////////////////////////////					 	 /////////////////////////////////////
		/////////////////////////////////	RESTAURANT3 AGENTS   /////////////////////////////////////
		
		else if (s.equals("Restaurant3Customer")) {
			for (Role r : roles) {
				if (r instanceof Restaurant3Customer) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant3Waiter")) {
			for (Role r : roles) {
				if (r instanceof Restaurant3Waiter) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant3Host")) {
			for (Role r : roles) {
				if (r instanceof Restaurant3Host) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant3Cook")) {
			for (Role r : roles) {
				if (r instanceof Restaurant3Cook) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant3Cashier")) {
			for (Role r : roles) {
				if (r instanceof Restaurant3Cashier) {
					return r;
				}
			}
		}
		
		
		
		
		
		/////////////////////////////////					 	 /////////////////////////////////////
		/////////////////////////////////	RESTAURANT4 AGENTS   /////////////////////////////////////
		
		else if (s.equals("Restaurant4Customer")) {
			for (Role r : roles) {
				if (r instanceof Restaurant4Customer) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant4Waiter")) {
			for (Role r : roles) {
				if (r instanceof Restaurant4Waiter) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant4Host")) {
			for (Role r : roles) {
				if (r instanceof Restaurant4Host) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant4Cook")) {
			for (Role r : roles) {
				if (r instanceof Restaurant4Cook) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant4Cashier")) {
			for (Role r : roles) {
				if (r instanceof Restaurant4Cashier) {
					return r;
				}
			}
		}
		
		
		
		
		
		
		/////////////////////////////////					 	 /////////////////////////////////////
		/////////////////////////////////	RESTAURANT5 AGENTS   /////////////////////////////////////
		
		else if (s.equals("Restaurant5Customer")) {
			for (Role r : roles) {
				if (r instanceof Restaurant5Customer) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant5Waiter")) {
			for (Role r : roles) {
				if (r instanceof Restaurant5Waiter) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant5Host")) {
			for (Role r : roles) {
				if (r instanceof Restaurant5Host) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant5Cook")) {
			for (Role r : roles) {
				if (r instanceof Restaurant5Cook) {
					return r;
				}
			}
		}
		
		else if (s.equals("Restaurant5Cashier")) {
			for (Role r : roles) {
				if (r instanceof Restaurant5Cashier) {
					return r;
				}
			}
		}
		
		
		
		
		return null; // no matching roles found, return null
	 }
	
	 
	
	
	 public Role RoleFactory(String s) {
		 
		 
		///////////////////////////////					   ///////////////////////////////
		/////////////////////////////// RESTAURANT1 AGENTS ///////////////////////////////
		 
		if (s.equals("Restaurant1Customer")) {
			Role r = new Restaurant1CustomerRole(name);
			r.setPersonAgent(this);	
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.restaurant1Panel);
			return r;
			// create new Restaurant1Customer
		}
		else if (s.equals("Restaurant1Waiter")) {
			Role r = new Restaurant1NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.restaurant1Panel);
			return r;
			// create new Restaurant1Waiter 
		}
		else if (s.equals("Restaurant1NormalWaiter")) {
			Role r = new Restaurant1NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.restaurant1Panel);
			return r;
			// create new Restaurant1Waiter 
		}
		else if (s.equals("Restaurant1PCWaiter")) {
			Role r = new Restaurant1PCWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.restaurant1Panel);
			return r;
			// create new Restaurant1Waiter 
		}
		else if (s.equals("Restaurant1Host")) {
			Role r = new Restaurant1HostRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.restaurant1Panel);
			return r;
			// create new Restaurant1Host
		}
		else if (s.equals("Restaurant1Cook")) {
			Role r = new Restaurant1CookRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.restaurant1Panel);
			return r;
			// create new Restaurant1Cook
		}
		else if (s.equals("Restaurant1Cashier")) {
			Role r = new Restaurant1CashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.restaurant1Panel);
			return r;
			// create new Restaurant1Cashier 
		}
		
		
		
		
		
		///////////////////////////////					   ///////////////////////////////
		/////////////////////////////// RESTAURANT2 AGENTS ///////////////////////////////
		
		else if (s.equals("Restaurant2Customer")) {
			Role r = new Restaurant2CustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.restaurant2Panel);
			return r;
			// create new Restaurant2Customer
		}
		else if (s.equals("Restaurant2Waiter")) {
			Role r = new Restaurant2NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.restaurant2Panel);
			return r;
			// create new Restaurant2Waiter 
		}
		else if (s.equals("Restaurant2NormalWaiter")) {
			Role r = new Restaurant2NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.restaurant2Panel);
			return r;
			// create new Restaurant2Waiter 
		}
		else if (s.equals("Restaurant2PCWaiter")) {
			Role r = new Restaurant2PCWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.restaurant2Panel);
			return r;
			// create new Restaurant2Waiter 
		}
		else if (s.equals("Restaurant2Host")) {
			Role r = new Restaurant2HostRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.restaurant2Panel);
			return r;
			// create new Restaurant2Host
		}
		else if (s.equals("Restaurant2Cook")) {
			Role r = new Restaurant2CookRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.restaurant2Panel);
			return r;
			// create new Restaurant2Cook
		}
		else if (s.equals("Restaurant2Cashier")) {
			Role r = new Restaurant2CashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.restaurant2Panel);
			return r;
			// create new Restaurant2Cashier 
		}
		
		
		
		
		///////////////////////////////					   ///////////////////////////////
		/////////////////////////////// RESTAURANT3 AGENTS ///////////////////////////////
		
		else if (s.equals("Restaurant3Customer")) {
			Role r = new Restaurant3CustomerRole(name);
			r.setPersonAgent(this);	
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.restaurant3Panel);
			return r;
			// create new Restaurant3Customer
		}
		else if (s.equals("Restaurant3Waiter")) {
			Role r = new Restaurant3NormalWaiter(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.restaurant3Panel);
			return r;
			// create new Restaurant3Waiter 
		}
		else if (s.equals("Restaurant3NormalWaiter")) {
			Role r = new Restaurant3NormalWaiter(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.restaurant3Panel);
			return r;
			// create new Restaurant3Waiter 
		}
		else if (s.equals("Restaurant3PCWaiter")) {
			Role r = new Restaurant3PCWaiter(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.restaurant3Panel);
			return r;
			// create new Restaurant3Waiter 
		}
		else if (s.equals("Restaurant3Host")) {
			Role r = new Restaurant3HostRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.restaurant3Panel);
			return r;
			// create new Restaurant3Host
		}
		else if (s.equals("Restaurant3Cook")) {
			Role r = new Restaurant3CookRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.restaurant3Panel);
			return r;
			// create new Restaurant3Cook
		}
		else if (s.equals("Restaurant3Cashier")) {
			Role r = new Restaurant3CashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.restaurant3Panel);
			return r;
			// create new Restaurant3Cashier 
		}
		
		
		
		
		///////////////////////////////					   ///////////////////////////////
		/////////////////////////////// RESTAURANT4 AGENTS ///////////////////////////////
		
		else if (s.equals("Restaurant4Customer")) {
			Role r = new Restaurant4CustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(4);
			r.setComponentPanel(SimCityGui.restaurant4Panel);
			return r;
			// create new Restaurant4Customer
		}
		else if (s.equals("Restaurant4Waiter")) {
			Role r = new Restaurant4NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(4);
			r.setComponentPanel(SimCityGui.restaurant4Panel);
			return r;
			// create new Restaurant4Waiter 
		}
		else if (s.equals("Restaurant4NormalWaiter")) {
			Role r = new Restaurant4NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(4);
			r.setComponentPanel(SimCityGui.restaurant4Panel);
			return r;
			// create new Restaurant4Waiter 
		}
		else if (s.equals("Restaurant4PCWaiter")) {
			Role r = new Restaurant4PCWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(4);
			r.setComponentPanel(SimCityGui.restaurant4Panel);
			return r;
			// create new Restaurant4Waiter 
		}
		else if (s.equals("Restaurant4Host")) {
			Role r = new Restaurant4HostRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(4);
			r.setComponentPanel(SimCityGui.restaurant4Panel);
			return r;
			// create new Restaurant4Host
		}
		else if (s.equals("Restaurant4Cook")) {
			Role r = new Restaurant4CookRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(4);
			r.setComponentPanel(SimCityGui.restaurant4Panel);
			return r;
			// create new Restaurant4Cook
		}
		else if (s.equals("Restaurant4Cashier")) {
			Role r = new Restaurant4CashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(4);
			r.setComponentPanel(SimCityGui.restaurant4Panel);
			return r;
			// create new Restaurant4Cashier 
		}
		
		
		
		
		///////////////////////////////					   ///////////////////////////////
		/////////////////////////////// RESTAURANT5 AGENTS ///////////////////////////////
		
		else if (s.equals("Restaurant5Customer")) {
			Role r = new Restaurant5CustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(5);
			r.setComponentPanel(SimCityGui.restaurant5Panel);
			return r;
			// create new Restaurant5Customer
		}
		else if (s.equals("Restaurant5Waiter")) {
			Role r = new Restaurant5NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(5);
			r.setComponentPanel(SimCityGui.restaurant5Panel);
			return r;
			// create new Restaurant5Waiter 
		}
		else if (s.equals("Restaurant5NormalWaiter")) {
			Role r = new Restaurant5NormalWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(5);
			r.setComponentPanel(SimCityGui.restaurant5Panel);
			return r;
			// create new Restaurant5Waiter 
		}
		else if (s.equals("Restaurant5PCWaiter")) {
			Role r = new Restaurant5PCWaiterRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(5);
			r.setComponentPanel(SimCityGui.restaurant5Panel);
			return r;
			// create new Restaurant5Waiter 
		}
		else if (s.equals("Restaurant5Host")) {
			Role r = new Restaurant5HostRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(5);
			r.setComponentPanel(SimCityGui.restaurant5Panel);
			return r;
			// create new Restaurant5Host
		}
		else if (s.equals("Restaurant5Cook")) {
			Role r = new Restaurant5CookRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(5);
			r.setComponentPanel(SimCityGui.restaurant5Panel);
			return r;
			// create new Restaurant5Cook
		}
		else if (s.equals("Restaurant5Cashier")) {
			Role r = new Restaurant5CashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(5);
			r.setComponentPanel(SimCityGui.restaurant5Panel);
			return r;
			// create new Restaurant5Cashier 
		}
		
		
		
		
		///////////////////////////////				  ///////////////////////////////
		/////////////////////////////// MARKET AGENTS ///////////////////////////////
		
		else if (s.equals("MarketCustomer")) {
			// create new MarketCustomer
			Role r = new MarketCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.marketPanel);
			return r;
		}
		else if (s.equals("Market3Customer")) {
			// create new MarketCustomer
			Role r = new MarketCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.marketPanel);
			return r;
		}
		else if (s.equals("Market2Customer")) {
			// create new MarketCustomer
			Role r = new MarketCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.market2Panel);
			return r;
		}
		else if (s.equals("Market1Customer")) {
			// create new MarketCustomer
			Role r = new MarketCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.market1Panel);
			return r;
		}
		
		
		
		else if (s.equals("MarketGrocer")) {
			// create new MarketCustomer
			Role r = new MarketGrocerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.marketPanel);
			return r;
		}
		else if (s.equals("Market3Grocer")) {
			// create new MarketCustomer
			Role r = new MarketGrocerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.marketPanel);
			return r;
		}
		else if (s.equals("Market2Grocer")) {
			// create new MarketCustomer
			Role r = new MarketGrocerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.market2Panel);
			return r;
		}
		else if (s.equals("Market1Grocer")) {
			// create new MarketCustomer
			Role r = new MarketGrocerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.market1Panel);
			return r;
		}
		
		
		
		else if (s.equals("MarketCashier")) {
			// create new MarketCashier
			Role r = new MarketCashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.marketPanel);
			return r;
		}
		else if (s.equals("Market3Cashier")) {
			// create new MarketCashier
			Role r = new MarketCashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(3);
			r.setComponentPanel(SimCityGui.marketPanel);
			return r;
		}
		else if (s.equals("Market2Cashier")) {
			// create new MarketCashier
			Role r = new MarketCashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.market2Panel);
			return r;
		}
		else if (s.equals("Market1Cashier")) {
			// create new MarketCashier
			Role r = new MarketCashierRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.market1Panel);
			return r;
		}
		
		
		
		
		///////////////////////////////				 ////////////////////////////////
		/////////////////////////////// HOUSE AGENTS ///////////////////////////////
		
		else if (s.equals("HousingCustomer") || s.equals("HousingResident")) {
			// create new HousingCustomer
			Role r = new HouseResidentRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.housingOfficePanel);
			return r;
		}
		
		
		
		
		///////////////////////////////				 		 ////////////////////////////////
		/////////////////////////////// HOUSING OFFICE AGENTS ///////////////////////////////		
		
		
		else if (s.equals("HousingOfficeCustomer")) {
			// create new HousingOfficeCustomer
			Role r = new HouseCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.housingOfficePanel);
			return r;
		}
		else if (s.equals("HousingMaintenanceManager")) {
			// create new HousingOfficeCustomer
			Role r = new HouseMaintenanceManagerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.housingOfficePanel);
			return r;
		}
		else if (s.equals("HousingOwner")) {
			// create new HousingOfficeCustomer
			Role r = new HouseOwnerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.housingOfficePanel);
			return r;
		}
		
				
		
		
		///////////////////////////////				////////////////////////////////
		/////////////////////////////// BANK AGENTS ///////////////////////////////	

		else if (s.equals("BankCustomer")) {
			// create new BankCustomer
			Role r = new BankCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank2Customer")) {
			// create new BankCustomer
			Role r = new BankCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank1Customer")) {
			// create new BankCustomer
			Role r = new BankCustomerRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.bank1Panel);
			return r;
		}
		
		
		
		else if (s.equals("BankTeller")) {
			// create new BankTeller
			Role r = new BankTellerRole(name, 5000);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank2Teller")) {
			// create new BankTeller
			Role r = new BankTellerRole(name, 5000);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank1Teller")) {
			// create new BankTeller
			Role r = new BankTellerRole(name, 5000);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.bank1Panel);
			return r;
		}
		
		
		
		else if (s.equals("BankGuard")) {
			// create new BankGuard
			Role r = new BankGuardRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank2Guard")) {
			// create new BankGuard
			Role r = new BankGuardRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank1Guard")) {
			// create new BankGuard
			Role r = new BankGuardRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.bank1Panel);
			return r;
		}
		
		
		
		else if (s.equals("BankRobber")) {
			// create new BankRobber
			Role r = new BankRobberRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank2Robber")) {
			// create new BankRobber
			Role r = new BankRobberRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(2);
			r.setComponentPanel(SimCityGui.bankPanel);
			return r;
		}
		else if (s.equals("Bank1Robber")) {
			// create new BankRobber
			Role r = new BankRobberRole(name);
			r.setPersonAgent(this);
			r.setComponentNumber(1);
			r.setComponentPanel(SimCityGui.bank1Panel);
			return r;
		}
		
		
		
		// if it gets to this point, then there was no proper type asked for
		return null;
	 }
	
	
	 
	
	/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Messages:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Messages:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Messages:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
	 
	// from PersonGui
	public void MsgActionComplete() {
		actionComplete.release();
		stateChanged();
	}
	
	// from PersonGui
	public void msgAtDestination() {
		atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void tryToAcquire(Semaphore semaphore){
		acquireSemaphores.add(semaphore);
		stateChanged();
	}
	
	// from ControlPanel
	public void setTransportationMethod(String s) {
		if (s.equals("walk")) {
			transportation = TransportationType.walk;
		}
		else if (s.equals("bus")) {
			transportation = TransportationType.bus;
		}
		else if (s.equals("car")) {
			transportation = TransportationType.car;
		}
		else if (s.equals("retardWalker") || s.equals("retardPerson") ) {
			transportation = TransportationType.retardWalker;
			AIEnabled = false;
		}
		else if (s.equals("retardDriver") || s.equals("retardCar") ) {
			transportation = TransportationType.retardDriver;
			AIEnabled = false;
		}
	}
	 
	// from ControlPanel
	public void ToHousing() {
		log.add(new LoggedEvent("Received ToHousing msg"));
		print("Received ToHousing msg");
		state = PersonState.toHousing;
		stateChanged();
	}
	
	public void ToHousing(Role r) {
		log.add(new LoggedEvent("Received ToHousing msg"));
		print("Received ToHousing msg");
		state = PersonState.toHousing;
		stateChanged();
	}
	
	// from ConrolPanel
	public void ToBank() {
		log.add(new LoggedEvent("Received ToBank msg"));
		print("Received ToBank msg");
		state = PersonState.toBank;
		stateChanged();
	}
	
	public void ToBank(Role r) {
		componentNumber = r.getComponentNumber();
		log.add(new LoggedEvent("Received ToBank msg"));
		print("Received ToBank(Role) msg");
		state = PersonState.toBank;
		stateChanged();
	}
	
	public void ToBankTeller(){
		log.add(new LoggedEvent("Received ToBankTeller msg"));
		state = PersonState.toBankTeller;
		stateChanged();
	}
	
	public void ToBankGuard() {
		log.add(new LoggedEvent("Received ToBankGuard msg"));
		state = PersonState.toBankGuard;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToRestaurant1() {
		log.add(new LoggedEvent("Received ToRestaurant1 msg"));
		print("Received ToRestaurant1 msg");
		state = PersonState.toRestaurant1;
		stateChanged();
	}
	
	public void ToRestaurant1(Role r) {
		//componentNumber = r.getComponentNumber();
		log.add(new LoggedEvent("Received ToRestaurant1 msg"));
		print("Received ToRestaurant1Role msg");
		state = PersonState.toRestaurant1;
		stateChanged();
	}
	
	public void ToRestaurant1Waiter() {
		log.add(new LoggedEvent("Received ToRestaurant1Waiter msg"));
		print("Received ToRestaurant1Waiter msg");
		state = PersonState.toRestaurant1Waiter;
		stateChanged();
	}
	
	public void ToRestaurant1Host() {
		log.add(new LoggedEvent("Received ToRestaurant1Host msg"));
		print("Received ToRestaurant1Host msg");
		state = PersonState.toRestaurant1Host;
		stateChanged();
	}
	public void ToRestaurant1Cook() {
		log.add(new LoggedEvent("Received ToRestaurant1Cook msg"));
		state = PersonState.toRestaurant1Cook;
		stateChanged();
	}
	public void ToRestaurant1Cashier() {
		log.add(new LoggedEvent("Received ToRestaurant1Cashier msg"));
		state = PersonState.toRestaurant1Cashier;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToRestaurant2() {
		log.add(new LoggedEvent("Received ToRestaurant2 msg"));
		print("Received ToRestaurant2 msg");
		state = PersonState.toRestaurant2;
		stateChanged();
	}
	
	public void ToRestaurant2(Role r) {
		//componentNumber = r.getComponentNumber();
		log.add(new LoggedEvent("Received ToRestaurant2 msg"));
		print("Received ToRestaurant2Role msg");
		state = PersonState.toRestaurant2;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToRestaurant3() {
		log.add(new LoggedEvent("Received ToRestaurant3 msg"));
		print("Received ToRestaurant3 msg");
		state = PersonState.toRestaurant3;
		stateChanged();
	}
	
	public void ToRestaurant3(Role r) {
		log.add(new LoggedEvent("Received ToRestaurant3 msg"));
		print("Received ToRestaurant3Role msg");
		state = PersonState.toRestaurant3;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToRestaurant4() {
		log.add(new LoggedEvent("Received ToRestaurant4 msg"));
		print("Received ToRestaurant4 msg");
		state = PersonState.toRestaurant4;
		stateChanged();
	}
	
	public void ToRestaurant4(Role r) {
		log.add(new LoggedEvent("Received ToRestaurant4 msg"));
		print("Received ToRestaurant4Role msg");
		state = PersonState.toRestaurant4;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToRestaurant5() {
		log.add(new LoggedEvent("Received ToRestaurant5 msg"));
		print("Received ToRestaurant5 msg");
		state = PersonState.toRestaurant5;
		stateChanged();
	}
	
	public void ToRestaurant5(Role r) {
		log.add(new LoggedEvent("Received ToRestaurant5 msg"));
		print("Received ToRestaurant5Role msg");
		state = PersonState.toRestaurant5;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToMarket() {
		log.add(new LoggedEvent("Received ToMarket msg"));
		print("Received ToMarket msg");
		state = PersonState.toMarket;
		stateChanged();
	}
	
	public void ToMarket(Role r) {
		componentNumber = r.getComponentNumber();
		log.add(new LoggedEvent("Received ToMarket msg"));
		print("Received ToMarketRole msg");
		state = PersonState.toMarket;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToMarketGrocer() {
		log.add(new LoggedEvent("Received ToMarketGrocer msg"));
		state = PersonState.toMarketGrocer;
		stateChanged();
	}
	
	// from ControlPanel
	public void ToBusStop(String busStop) {
		log.add(new LoggedEvent("Received ToBusStop msg"));
		print("Received ToBusStop msg");
		state = PersonState.toBusStop;
		location = ((BusStopComponent) cityMap.get(busStop));
		stateChanged();
	}
	
	public void ToHousingOffice(){
		log.add(new LoggedEvent("Received ToHousingOffice msg"));
		print("Received ToHousingOffice msg");
		state = PersonState.toHousingOffice;
		stateChanged();
	}
	
	public void ToHousingOffice(Role r) {
		componentNumber = r.getComponentNumber();
		log.add(new LoggedEvent("Received ToHousingOffice msg"));
		print("Received ToHousingOfficeRole msg");
		state = PersonState.toHousingOffice;
		stateChanged();
	}
	
	public void ToHousingOfficeOwner(){
		log.add(new LoggedEvent("Received ToHousingOffice msg"));
		state = PersonState.toHousingOfficeOwner;
		stateChanged();
	}
	
	public void ToHousingOfficeManager(){
		log.add(new LoggedEvent("Received ToHousingOffice msg"));
		state = PersonState.toHousingOfficeManager;
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtHousing() {
		log.add(new LoggedEvent("Received ArrivedAtHousing msg"));
		state = PersonState.atHousing;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtHousingOffice() {
		log.add(new LoggedEvent("Received ArrivedAtHousingOffice msg"));
		state = PersonState.atHousingOffice;
		//atDestination.release();
		stateChanged();
	}

	// from PersonGui
	public void ArrivedAtHousingOfficeOwner() {
		log.add(new LoggedEvent("Received ArrivedAtHousingOfficeOwner msg"));
		state = PersonState.atHousingOfficeOwner;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtHousingOfficeManager() {
		log.add(new LoggedEvent("Received ArrivedAtHousingOfficeManager msg"));
		state = PersonState.atHousingOfficeManager;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtBank() {
		log.add(new LoggedEvent("Received ArrivedAtBank msg"));
		state = PersonState.atBank;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtBankTeller() {
		log.add(new LoggedEvent("Received ArrivedAtBankTeller msg"));
		state = PersonState.atBankTeller;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtBankGuard() {
		log.add(new LoggedEvent("Received ArrivedAtBankGuard msg"));
		state = PersonState.atBankGuard;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtMarket() {
		log.add(new LoggedEvent("Received ArrivedAtMarket msg"));
		state = PersonState.atMarket;
		//atDestination.release();
		stateChanged();
	}
	

	// from PersonGui
	public void ArrivedAtMarketGrocer() {
		log.add(new LoggedEvent("Received ArrivedAtMarketGrocer msg"));
		state = PersonState.atMarketGrocer;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtBusStop() {
		log.add(new LoggedEvent("Received ArrivedAtBusStop msg"));
		state = PersonState.atBusStop;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant1() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant1 msg"));
		state = PersonState.atRestaurant1;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant1Waiter() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant1Waiter msg"));
		state = PersonState.atRestaurant1Waiter;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant1Host() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant1Host msg"));
		state = PersonState.atRestaurant1Host;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant1Cook() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurantCook msg"));
		state = PersonState.atRestaurant1Cook;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant1Cashier() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant1Cashier msg"));
		state = PersonState.atRestaurant1Cashier;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant2() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant2 msg"));
		state = PersonState.atRestaurant2;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant3() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant3 msg"));
		state = PersonState.atRestaurant3;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant4() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant4 msg"));
		state = PersonState.atRestaurant4;
		//atDestination.release();
		stateChanged();
	}
	
	// from PersonGui
	public void ArrivedAtRestaurant5() {
		log.add(new LoggedEvent("Received ArrivedAtRestaurant5 msg"));
		state = PersonState.atRestaurant5;
		//atDestination.release();
		stateChanged();
	}
	
	// from
	public void BusIsHere(Bus b) {
		log.add(new LoggedEvent("Received BusIsHere msg"));
		currentBus = b;
		state = PersonState.busArrived;
		stateChanged();
	}
	
	// from Bus
	public void BusAtDestination() {
		log.add(new LoggedEvent("Received BusAtDestination msg"));
		state = PersonState.atDestination;
		stateChanged();
	}
	
	// from all Components
	public void OutOfComponent(Role r) {
		print("Exiting Component");
		if (r instanceof Restaurant1CustomerRole) {
			hungerLevel = 0;
			money = money - 10;
		}
		if (r instanceof Restaurant2CustomerRole) {
			hungerLevel = 0;
		}
		if (r instanceof Restaurant3CustomerRole) {
			hungerLevel = 0;
			money = money - 10;
		}
		if (r instanceof Restaurant4CustomerRole) {
			hungerLevel = 0;
			money = money - 10;
		}
		if (r instanceof Restaurant5CustomerRole) {
			hungerLevel = 0;
			money = money - 10;
		}
		if (r instanceof MarketCustomerRole) {
			houseStock = 2;
			money = money - 5;
		}
		if (r instanceof HouseResidentRole) {
			if (housingBehavior.equals("eat")) {
				hungerLevel = 0;
			}
			if (housingBehavior.equals("sleep")) {
				tiredLevel = 0;
			}
		}
		if (r instanceof BankCustomerRole) {
			money = money + 15;
		}
		r.active = false;
		insideComponent = false;
		state = PersonState.outOfComponent;
		stateChanged();
	}
	
	
	/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Scheduler:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Scheduler:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Scheduler:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
	

	public boolean pickAndExecuteAnAction() {
		
		/*
		if (!acquireSemaphores.isEmpty()){
			acquireSemaphores(acquireSemaphores.get(0));
			return true;
		}
		*/
		
		if (state == PersonState.outOfComponent) {
			exitComponent();
		}
		
		anyTrue = false;
		if ( roles.size() != 0 ) {
			for (Role r : roles) {
				if (r.active) {
					anyTrue = (anyTrue) || (r.pickAndExecuteAnAction());
				}
			}
		}
		
		if (state == PersonState.atBusStop) {
			enterBusStop();
			return true;
		}
		
		if (state == PersonState.atDestination) {
			exitBus();
			return true;
		}
		
		if (state == PersonState.busArrived) {
			boardBus();
			return true;
		}
		
		if (insideComponent) {
			return anyTrue; // don't check scheduler for other outside component actions if inside a component at the moment
		}
		
		// AI FUNCTIONALITY HANDLED HERE (automated actions based on internal variables)
		
		if (AIEnabled) {
			if (state == PersonState.normal){
				if (housingLocation == null) {
					if (cityMap.get("HousingOffice").isOpen()) {
						goToHousingOffice();
						return true;
					}
				}
				else if ( (Clock.timeOfDay >= 22.00) || (Clock.timeOfDay <= 6.00) ) {
					goToHousingAndSleep();
					return true;
				}
				else if (hungerLevel >= 20) {
					if( (Clock.timeOfDay >= 20.00) || (Clock.timeOfDay <= 8.00) ) {
						goToHousingAndEat();
						return true;
					}
					else {
						if (cityMap.get("Restaurant1").isOpen()) {
							chooseARestaurant();
							return true;
						}
					}
				}
				else if (tiredLevel >= 20) {
					goToHousingAndSleep();
					return true;
				}
				else if (hungerLevel >= 10) {
					if( (Clock.timeOfDay >= 20.00) || (Clock.timeOfDay <= 8.00) ) {
						goToHousingAndEat();
						return true;
					}
					else {
						if (cityMap.get("Restaurant1").isOpen()) {
							chooseARestaurant();
							return true;
						}
					}
				}
				else if (money <= 10) {
					if( (Clock.timeOfDay < 20.00) && (Clock.timeOfDay > 8.00) ) {
						if (cityMap.get("Bank").isOpen()) {
							goToBank();
							return true;
						}
					}
				}
				else if (houseStock <= 0) {
					if( (Clock.timeOfDay < 20.00) && (Clock.timeOfDay > 8.00) ) {
						if (cityMap.get("Market").isOpen()) {
							goToMarket();
							return true;
						}
					}
				}
				else if (tiredLevel >= 12) {
					goToHousingAndSleep();
					return true;
				}
				else { // nothing happening, go home and eat a snack/watch TV or something
					/*
					goToHousingAndEat();
					return true;
					*/
				}
			}
		}
		
		if (state == PersonState.toRestaurant1) {
			goToRestaurant1();
			return true;
		}
		
		if (state == PersonState.toRestaurant1Waiter) {
			goToRestaurant1Waiter();
			return true;
		}
		
		if (state == PersonState.toRestaurant1Host) {
			goToRestaurant1Host();
			return true;
		}
		
		if (state == PersonState.toRestaurant1Cook) {
			goToRestaurant1Cook();
			return true;
		}
		
		if (state == PersonState.toRestaurant1Cashier) {
			goToRestaurant1Cashier();
			return true;
		}
		
		if (state == PersonState.toRestaurant2) {
			goToRestaurant2();
			return true;
		}
		
		if (state == PersonState.toRestaurant3) {
			goToRestaurant3();
			return true;
		}
		
		if (state == PersonState.toRestaurant4) {
			goToRestaurant4();
			return true;
		}
		
		if (state == PersonState.toRestaurant5) {
			goToRestaurant5();
			return true;
		}
	
		if (state == PersonState.toBank) {
			goToBank();
			return true;
		}
		
		if (state == PersonState.toBankTeller) {
			goToBankTeller();
			return true;
		}
		
		if (state == PersonState.toBankGuard) {
			goToBankGuard();
			return true;
		}
		
		if (state == PersonState.toMarket) {
			goToMarket();
			return true;
		}
		
		if (state == PersonState.toMarketGrocer) {
			goToMarketGrocer();
			return true;
		}
	
		if (state == PersonState.toHousing) {
			goToHousingAndEat();
			return true;
		}
	
		if (state == PersonState.toBusStop) {
			goToBusStop(location);
			return true;
		}
		
		if (state == PersonState.toHousingOffice) {
			goToHousingOffice();
			return true;
		}

		if (state == PersonState.toHousingOfficeOwner) {
			goToHousingOfficeOwner();
			return true;
		}

		if (state == PersonState.toHousingOfficeManager) {
			goToHousingOfficeManager();
			return true;
		}
	
		if (state == PersonState.atHousing) {
			enterHousing();
			return true;
		}
		
		if (state == PersonState.atHousingOffice) {
			enterHousingOffice();
			return true;
		}
		
		if (state == PersonState.atHousingOfficeOwner) {
			enterHousingOfficeOwner();
			return true;
		}
		
		if (state == PersonState.atHousingOfficeManager) {
			enterHousingOfficeManager();
			return true;
		}
		
		if (state == PersonState.atMarket) {
			enterMarket();
			return true;
		}

		if (state == PersonState.atMarketGrocer) {
			enterMarketGrocer();
			return true;
		}
		
		if (state == PersonState.atRestaurant1) {
			enterRestaurant1();
			return true;
		}
		
		if (state == PersonState.atRestaurant1Waiter) {
			enterRestaurant1Waiter();
			return true;
		}
		
		if (state == PersonState.atRestaurant1Host) {
			enterRestaurant1Host();
			return true;
		}
		
		if (state == PersonState.atRestaurant1Cook) {
			enterRestaurant1Cook();
			return true;
		}
		
		if (state == PersonState.atRestaurant1Cashier) {
			enterRestaurant1Cashier();
			return true;
		}
		
		if (state == PersonState.atRestaurant2) {
			enterRestaurant2();
			return true;
		}
		
		if (state == PersonState.atRestaurant3) {
			enterRestaurant3();
			return true;
		}
		
		if (state == PersonState.atRestaurant4) {
			enterRestaurant4();
			return true;
		}
		
		if (state == PersonState.atRestaurant5) {
			enterRestaurant5();
			return true;
		}
		
		if (state == PersonState.atBank) {
			enterBank();
			return true;
		}

		if (state == PersonState.atBankTeller) {
			enterBankTeller();
			return true;
		}
		
		if (state == PersonState.atBankGuard) {
			enterBankGuard();
			return true;
		}
		
		return anyTrue; // nothing left to do, return anyTrue
	}
	
	
	
	/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Actions:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Actions:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Actions:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
	
	
	private void acquireSemaphores(Semaphore semaphore){
		semaphore.acquireUninterruptibly(); 
		gui.didAcquire();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println("Didn't finish sleeping");
		}
		semaphore.release();
	}
	
	private void goToRestaurant1() {
		log.add(new LoggedEvent("Going To Restaurant1"));
		print("Going to Restaurant1");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant1");
		gui.DoGoToRestaurant1(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant1Waiter() {
		log.add(new LoggedEvent("Going To Restaurant1"));
		print("Going to Restaurant1");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant1");
		gui.DoGoToRestaurant1Waiter(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant1Host() {
		log.add(new LoggedEvent("Going To Restaurant1"));
		print("Going to Restaurant1");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant1");
		gui.DoGoToRestaurant1Host(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant1Cook() {
		log.add(new LoggedEvent("Going To Restaurant1"));
		print("Going to Restaurant1");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant1");
		gui.DoGoToRestaurant1Cook(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant1Cashier() {
		log.add(new LoggedEvent("Going To Restaurant1"));
		print("Going to Restaurant1");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant1");
		gui.DoGoToRestaurant1Cashier(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant2() {
		log.add(new LoggedEvent("Going To Restaurant2"));
		print("Going to Restaurant2");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant2");
		gui.DoGoToRestaurant2(c.location, transportation);
	//	print (name + " made it through goToRestaurant2");
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant3() {
		log.add(new LoggedEvent("Going To Restaurant3"));
		print("Going to Restaurant3");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant3");
		gui.DoGoToRestaurant3(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant4() {
		log.add(new LoggedEvent("Going To Restaurant4"));
		print("Going to Restaurant4");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant4");
		gui.DoGoToRestaurant4(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToRestaurant5() {
		log.add(new LoggedEvent("Going To Restaurant5"));
		print("Going to Restaurant5");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Restaurant5");
		gui.DoGoToRestaurant5(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToBank() {
		log.add(new LoggedEvent("Going To Bank"));
		print("Going to Bank");
		tempState = state;
		state = PersonState.traveling;
		if (componentNumber == 1) {
			CityComponent c = cityMap.get("Bank1");
			gui.DoGoToBank(c.location, transportation);
		}
		else { // componentNumber == 2
			CityComponent c = cityMap.get("Bank");
			gui.DoGoToBank(c.location, transportation);
		}
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	

	
	private void goToBankTeller() {
		log.add(new LoggedEvent("Going To BankTeller"));
		print("Going to BankTeller");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Bank");
		gui.DoGoToBankTeller(c.location, transportation);
	}
	
	private void goToBankGuard() {
		log.add(new LoggedEvent("Going To BankTeller"));
		print("Going to BankTeller");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Bank");
		gui.DoGoToBankGuard(c.location);
	}
	
	private void goToMarket() {
		log.add(new LoggedEvent("Going To Market"));
		print("Going to Market");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Market");
		gui.DoGoToMarket(c.location, transportation);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}

	private void goToMarketGrocer() {
		log.add(new LoggedEvent("Going To MarketGrocer"));
		print("Going to MarketGrocer");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("Market");
		gui.DoGoToMarketGrocer(c.location, transportation);
	}
	
	private void goToHousing() {
		if (housingLocation == null) {
			state = PersonState.toHousingOffice;
			stateChanged();
		}
		else {
			log.add(new LoggedEvent("Going To Housing"));
			print("Going to Housing");
			tempState = state;
			state = PersonState.traveling;
			CityComponent c = cityMap.get(housingLocation);
			gui.DoGoToHousing(c.location, transportation);
		}
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void goToHousingOffice() {
		log.add(new LoggedEvent("Going To Housing Office"));
		print("Going to Housing Office");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("HousingOffice");
		gui.DoGoToHousingOffice(c.location, transportation);
	}

	private void goToHousingOfficeOwner() {
		log.add(new LoggedEvent("Going To Housing Office Owner"));
		print("Going to Housing Office Owner");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("HousingOffice");
		gui.DoGoToHousingOfficeOwner(c.location, transportation);
	}

	private void goToHousingOfficeManager() {
		log.add(new LoggedEvent("Going To Housing Office Manager"));
		print("Going to Housing Office Manager");
		tempState = state;
		state = PersonState.traveling;
		CityComponent c = cityMap.get("HousingOffice");
		gui.DoGoToHousingOfficeManager(c.location, transportation);
	}
	
	private void goToHousingAndSleep() {
		housingBehavior = "sleep";
		goToHousing();
	}
	
	private void goToHousingAndEat() {
		housingBehavior = "eat";
		goToHousing();
	}
	
	
	private void goToBusStop(BusStopComponent b) {
		log.add(new LoggedEvent("Going To BusStop"));
		print("Going to Bus Stop");
		tempState = state;
		state = PersonState.traveling;
		gui.DoGoToBusStop(b.location);
		
		/*
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void enterHousing() {
	/*
		roles.find(HousingCustomerRole);
		if (HousingCustomerRole not found) {
			Role r = new HousingCustomerRole();
			r.sendMessage(); //send initial housing message here
		}
		else {
			r.sendMessage(); //send initial housing message here
		}
	*/
		log.add(new LoggedEvent("Entering Housing"));
		print("Entering Housing");
		Role r = findRole("HousingCustomer");
		if (r == null) { // No HousingCustomer role exists in roles at the moment
			r = RoleFactory("HousingCustomer"); // RoleFactory creates a new instance of HousingCustomerRole
			roles.add(r);
			// possible r.sendMessage();
			//((HouseResidentRole) r).msgAskToBuyProperty(cityMap.get("House1").h);
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;
		//let apartment panel know about Resident
		if (housingLocation.equals("Apartment1")) {
			SimCityGui.apartmentPanel1.arrivedAtApartment((HouseResidentRole) r);
		}
		else if (housingLocation.equals("Apartment2")) {
			SimCityGui.apartmentPanel2.arrivedAtApartment((HouseResidentRole) r);
		}
		else if (housingLocation.equals("Apartment3")) {
			SimCityGui.apartmentPanel3.arrivedAtApartment((HouseResidentRole) r);
		}
		else if (housingLocation.equals("Apartment4")) {
			SimCityGui.apartmentPanel4.arrivedAtApartment((HouseResidentRole) r);
		}
		
		if (housingBehavior.equals("eat")) {
			((HouseResident) r).msgGotHungry();
			houseStock--;
		}
		if (housingBehavior.equals("sleep")) {
			((HouseResident) r).msgGotSleepy();
		}
		gui.DoEnterHousing(r);
	}
	
	private void enterHousingOffice() {
		
		
		log.add(new LoggedEvent("Entering Housing Office"));
		print("Entering Housing Office");
		
		//// CHECK ALL JOBS FIRST
		Role r;
		r = findRole("HousingOfficeManager");
		if (r == null) {
			r = findRole("HousingOfficeOwner");
		}
		if (r == null) {
			r = findRole("HousingOfficeCustomer");
		}
		if (r == null) { // No HousingCustomer role exists in roles at the moment
			print("Making new HousingOfficeCustomer");
			r = RoleFactory("HousingOfficeCustomer"); // RoleFactory creates a new instance of HousingCustomerRole
			roles.add(r);
			((HouseCustomerRole) r).msgAskToBuyProperty();
			// possible r.sendMessage();
			//((HouseCustomerRole) r).msgAskToBuyProperty(cityMap.get("House1").h);
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;
		
		gui.DoEnterHousingOffice(r);
	}

	private void enterHousingOfficeOwner() {
		log.add(new LoggedEvent("Entering Housing OfficeOwner"));
		print("Entering Housing Office Owner");
		Role r = findRole("HousingOfficeOwner");
		if (r == null) { // No HousingCustomer role exists in roles at the moment
			r = RoleFactory("HousingOfficeOwner"); // RoleFactory creates a new instance of HousingCustomerRole
			roles.add(r);
			//((HouseCustomerRole) r).msgAskToBuyProperty();
			// possible r.sendMessage();
			//((HouseCustomerRole) r).msgAskToBuyProperty(cityMap.get("House1").h);
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;
		
		gui.DoEnterHousingOfficeOwner(r);
	}

	private void enterHousingOfficeManager() {
		log.add(new LoggedEvent("Entering Housing OfficeManager"));
		print("Entering Housing Office Manager");
		Role r = findRole("HousingOfficeMaitenenceManager");
		if (r == null) { // No HousingCustomer role exists in roles at the moment
			r = RoleFactory("HousingOfficeMaintenanceManager"); // RoleFactory creates a new instance of HousingCustomerRole
			roles.add(r);
			//((HouseCustomerRole) r).msgAskToBuyProperty();
			// possible r.sendMessage();
			//((HouseCustomerRole) r).msgAskToBuyProperty(cityMap.get("House1").h);
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;
		
		gui.DoEnterHousingOfficeManager(r);
	}
	
	
	private void enterBank() {
	/*
		roles.find(BankingCustomerRole);
		if (BankingCustomerRole not found) {
			Role r = new BankingCustmerRole();
			r.sendMessage(); //send initial housing message here
		}
		else {
			r.sendMessage(); //send initial housing message here
		}
	*/
		log.add(new LoggedEvent("Entering Bank"));
		print("Entering Bank");
		
		Role r;
		if (componentNumber == 1) {
			r = findRole("Bank1Teller");
			if (r == null) {
				r = findRole("Bank1Guard");
			}
			if (r == null) {
				r = findRole("Bank1Robber");
			}
			if (r == null) {
				r = findRole("Bank1Customer");
			}
			if (r == null) {
				r = RoleFactory("Bank1Customer");
				roles.add(r);
			}
		}
		
		else {
			r = findRole("BankTeller");
			if (r == null) {
				r = findRole("BankGuard");
			}
			if (r == null) {
				r = findRole("BankRobber");
			}
			if (r == null) {
				r = findRole("BankCustomer");
			}
			if (r == null) { // No BankCustomer role exists in roles at the moment
				r = RoleFactory("BankCustomer");
				roles.add(r); 	// RoleFactory creates a new instance of BankCUstomerRole
				// possible r.sendMessage();
			}
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;
		
		gui.DoEnterBank(r);
	}
	
	private void enterBankTeller() {
		log.add(new LoggedEvent("Entering BankTeller"));
		print("Entering BankTeller");
		Role r = findRole("BankTeller");
		if (r == null) { // No BankCustomer role exists in roles at the moment
			r = RoleFactory("BankTeller");
			roles.add(r); 	// RoleFactory creates a new instance of BankCUstomerRole
			// possible r.sendMessage();
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;

		gui.DoEnterBankTeller(r);
	}
	
	private void enterBankGuard() {
		log.add(new LoggedEvent("Entering BankGuard"));
		print("Entering BankGuard");
		Role r = findRole("BankGuard");
		if (r == null) { // No BankCustomer role exists in roles at the moment
			r = RoleFactory("BankGuard");
			roles.add(r); 	// RoleFactory creates a new instance of BankCUstomerRole
			// possible r.sendMessage();
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;

		gui.DoEnterBankGuard(r);
	}
	
	private void enterMarket() {
	/*
		roles.find(MarketCustomerRole);
		if (MarketCustomerRole not found) {
			Role r = new MarketCustomerRole();
			r.sendMessage(); //send initial housing message here
		}
		else {
			r.sendMessage(); //send initial housing message here
		}
	*/
		log.add(new LoggedEvent("Entering Market"));
		print("Entering Market");
		Role r;
		
		if (componentNumber == 1) {
			r = findRole("Market1Cashier");
			if (r == null) {
				r = findRole("Market1Grocer");
			}
			if (r == null) {
				r = findRole("Market1Customer");
			}
			if (r == null) {
				r = RoleFactory("Market1Customer");
				roles.add(r);
				( (MarketCustomerRole) r).setWallet( (int) Math.floor(money) );
			}
		}
		else if (componentNumber == 2) {
			r = findRole("Market2Cashier");
			if (r == null) {
				r = findRole("Market2Grocer");
			}
			if (r == null) {
				r = findRole("Market2Customer");
			}
			if (r == null) {
				r = RoleFactory("Market2Customer");
				roles.add(r);
				( (MarketCustomerRole) r).setWallet( (int) Math.floor(money) );
			}
		}
		else { // componentNumber is not 1 or 2
			r = findRole("MarketCashier");
			if (r == null) {
				r = findRole("MarketGrocer");
			}
			if (r == null) {
				r = findRole("MarketCustomer");
			}
			if (r == null) {
				r = RoleFactory("MarketCustomer");
				roles.add(r);
				( (MarketCustomerRole) r).setWallet( (int) Math.floor(money) );
			}
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;
		gui.DoEnterMarket(r);
	}

	private void enterMarketGrocer() {
		log.add(new LoggedEvent("Entering MarketGrocer"));
		print("Entering MarketGrocer");
		Role r = findRole("MarketGrocer");
		if (r == null) { // No MarketCustomer role exists in roles at the moment
			r = RoleFactory("MarketGrocer");
			roles.add(r);
			// possible r.sendMessage();
			//((MarketCustomerRole) r).IAmInNeed();
		}
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;
		gui.DoEnterMarketGrocer(r);
	}
	
	
	private void enterRestaurant1() {
	/*
		roles.find(RestaurantCustomerRole);
		if (RestaurantCustomerRole not found) {
			Role r = new RestaurantCustomerRole();
			r.sendMessage(); //send initial housing message here
		}
		else {
			r.sendMessage(); //send initial housing message here
		}
	*/
		
		///// FIRST ITERATE THROUGH ALL ROLES TO LOOK FOR JOBS
		Role r;
		
		r = findRole("Restaurant1Waiter");
		if (r == null) {
			r = findRole("Restaurant1Cook");
		}
		if (r == null) {
			r = findRole("Restaurant1Cashier");
		}
		if (r == null) {
			r = findRole("Restaurant1Host");
		}
		if ( r == null ) {
			r = findRole("Restaurant1Customer");
		}
		if (r == null) { // No Restaurant1 roles exist in roles at the moment
			r = RoleFactory("Restaurant1Customer");
			roles.add(r);
			// possible r.sendMessage();
		}
		
		log.add(new LoggedEvent("Entering Restaurant1"));
		print("Entering Restaurant1");
		
		r.active = true;
		insideComponent = true;
		state = PersonState.normal;			
		hungerLevel = 0;
		
		gui.DoEnterRestaurant1(r);
	}
	
	
	
	
	
	
	private void enterRestaurant1Waiter() {
			log.add(new LoggedEvent("Entering Restaurant1 Waiter"));
			print("Entering Restaurant1 Waiter");
			Role r = findRole("Restaurant1Waiter");
			if (r == null) { // No Restaurant1Customer role exists in roles at the moment
				r = RoleFactory("Restaurant1Waiter");
				roles.add(r);
				// possible r.sendMessage();
			}
			r.active = true;
			
			insideComponent = true;
			state = PersonState.normal;			
			hungerLevel = 0;
			
			gui.DoEnterRestaurant1Waiter(r);
	}
	
	private void enterRestaurant1Host() {
		log.add(new LoggedEvent("Entering Restaurant1 Host"));
		print("Entering Restaurant1 Host");
		Role r = findRole("Restaurant1Host");
		if (r == null) { // No Restaurant1Customer role exists in roles at the moment
			r = RoleFactory("Restaurant1Host");
			roles.add(r);
			// possible r.sendMessage();
		}
		r.active = true;
		
		insideComponent = true;
		state = PersonState.normal;			
		hungerLevel = 0;
		
		gui.DoEnterRestaurant1Host(r);
}
	private void enterRestaurant1Cook() {
		log.add(new LoggedEvent("Entering Restaurant1 Cook"));
		print("Entering Restaurant1 Cook");
		Role r = findRole("Restaurant1Cook");
		if (r == null) { // No Restaurant1Customer role exists in roles at the moment
			r = RoleFactory("Restaurant1Cook");
			roles.add(r);
			// possible r.sendMessage();
		}
		r.active = true;
		
		insideComponent = true;
		state = PersonState.normal;			
		hungerLevel = 0;
		
		gui.DoEnterRestaurant1Cook(r);
	}
	private void enterRestaurant1Cashier() {
		log.add(new LoggedEvent("Entering Restaurant1 Cashier"));
		print("Entering Restaurant1 Cashier");
		Role r = findRole("Restaurant1Cashier");
		if (r == null) { // No Restaurant1Customer role exists in roles at the moment
			r = RoleFactory("Restaurant1Cashier");
			roles.add(r);
			// possible r.sendMessage();
		}
		r.active = true;
		
		insideComponent = true;
		state = PersonState.normal;			
		hungerLevel = 0;
		
		gui.DoEnterRestaurant1Cashier(r);
	}
	
	
	
	private void enterRestaurant2() {
		/*
			roles.find(RestaurantCustomerRole);
			if (RestaurantCustomerRole not found) {
				Role r = new RestaurantCustomerRole();
				r.sendMessage(); //send initial housing message here
			}
			else {
				r.sendMessage(); //send initial housing message here
			}
		*/
			log.add(new LoggedEvent("Entering Restaurant2"));
			print("Entering Restaurant2");
			
			///// FIRST ITERATE THROUGH ALL ROLES TO LOOK FOR JOBS
			Role r;
			
			r = findRole("Restaurant2Waiter");
			if (r == null) {
				r = findRole("Restaurant2Cook");
			}
			if (r == null) {
				r = findRole("Restaurant2Cashier");
			}
			if (r == null) {
				r = findRole("Restaurant2Host");
			}
			if ( r == null ) {
				r = findRole("Restaurant2Customer");
			}
			if (r == null) { // No Restaurant2Customer role exists in roles at the moment
				r = RoleFactory("Restaurant2Customer");
				roles.add(r);
				// possible r.sendMessage();
			}
			r.active = true;
			insideComponent = true;
			state = PersonState.normal;
			hungerLevel = 0;
			
			gui.DoEnterRestaurant2(r);
	}
	
	private void enterRestaurant3() {
		/*
			roles.find(RestaurantCustomerRole);
			if (RestaurantCustomerRole not found) {
				Role r = new RestaurantCustomerRole();
				r.sendMessage(); //send initial housing message here
			}
			else {
				r.sendMessage(); //send initial housing message here
			}
		*/
			log.add(new LoggedEvent("Entering Restaurant3"));
			print("Entering Restaurant3");

			///// FIRST ITERATE THROUGH ALL ROLES TO LOOK FOR JOBS
			Role r;
			
			r = findRole("Restaurant3Waiter");
			if (r == null) {
				r = findRole("Restaurant3Cook");
			}
			if (r == null) {
				r = findRole("Restaurant3Cashier");
			}
			if (r == null) {
				r = findRole("Restaurant3Host");
			}
			if ( r == null ) {
				r = findRole("Restaurant3Customer");
			}
			if (r == null) { // No Restaurant3Customer role exists in roles at the moment
				r = RoleFactory("Restaurant3Customer");
				roles.add(r);
				// possible r.sendMessage();
			}
			r.active = true;
			insideComponent = true;
			state = PersonState.normal;
			hungerLevel = 0;
			
			gui.DoEnterRestaurant3(r);
	}
	
	private void enterRestaurant4() {
		/*
			roles.find(RestaurantCustomerRole);
			if (RestaurantCustomerRole not found) {
				Role r = new RestaurantCustomerRole();
				r.sendMessage(); //send initial housing message here
			}
			else {
				r.sendMessage(); //send initial housing message here
			}
		*/
			log.add(new LoggedEvent("Entering Restaurant4"));
			print("Entering Restaurant4");
			
			
			///// FIRST ITERATE THROUGH ALL ROLES TO LOOK FOR JOBS
			Role r;
			
			r = findRole("Restaurant4Waiter");
			if (r == null) {
				r = findRole("Restaurant4Cook");
			}
			if (r == null) {
				r = findRole("Restaurant4Cashier");
			}
			if (r == null) {
				r = findRole("Restaurant4Host");
			}
			if ( r == null ) {
				r = findRole("Restaurant4Customer");
			}
			if (r == null) { // No Restaurant4Customer role exists in roles at the moment
				r = RoleFactory("Restaurant4Customer");
				roles.add(r);
				// possible r.sendMessage();
			}
			r.active = true;
			insideComponent = true;
			hungerLevel = 0;
			state = PersonState.normal;
			
			gui.DoEnterRestaurant4(r);
	}
	
	private void enterRestaurant5() {
		/*
			roles.find(RestaurantCustomerRole);
			if (RestaurantCustomerRole not found) {
				Role r = new RestaurantCustomerRole();
				r.sendMessage(); //send initial housing message here
			}
			else {
				r.sendMessage(); //send initial housing message here
			}
		*/
			log.add(new LoggedEvent("Entering Restaurant5"));
			print("Entering Restaurant5");
			
			///// FIRST ITERATE THROUGH ALL ROLES TO LOOK FOR JOBS
			Role r;
			
			r = findRole("Restaurant5Waiter");
			if (r == null) {
				r = findRole("Restaurant5Cook");
			}
			if (r == null) {
				r = findRole("Restaurant5Cashier");
			}
			if (r == null) {
				r = findRole("Restaurant5Host");
			}
			if ( r == null ) {
				r = findRole("Restaurant5Customer");
			}
			if (r == null) { // No Restaurant5Customer role exists in roles at the moment
				r = RoleFactory("Restaurant5Customer");
				roles.add(r);
				// possible r.sendMessage();
			}
			
			r.active = true;
			insideComponent = true;
			state = PersonState.normal;
			hungerLevel = 0;
			
			gui.DoEnterRestaurant5(r);	
			state = PersonState.normal;
	}
	
	private void exitComponent() {
		print("Exited Component");
		gui.DoExitComponent();
		insideComponent = false;
		state = PersonState.normal;
	}
	
	
	private void chooseARestaurant() {
		Random generator = new Random();
		int restaurantPick = generator.nextInt(5) + 1;
			switch (restaurantPick) {
				case 1: goToRestaurant1(); break;
				case 2: goToRestaurant2(); break;
				case 3: goToRestaurant3(); break;
				case 4: goToRestaurant4(); break;
				case 5: goToRestaurant5(); break;
			}
	}
	
	
	private void enterBusStop() {
		location.b.WantToRide(this, destination.b);
		state = PersonState.waitingForBus;
		insideComponent = true;
		
		gui.DoEnterBusStop();
	}
		
	private void boardBus() {
		gui.DoExitBusStop();
		
		insideComponent = true;
		state = PersonState.onBus;
		
		gui.DoBoardBus();
	}
	
	private void exitBus() {
		currentBus.LeavingBus(this);
		gui.DoExitBus(destination.location);
		insideComponent = false;
		state = tempState;
		stateChanged();
	}
	
	
	
	private void getHungry() {
		timer.schedule(new TimerTask() {
			public void run() {
				if (Clock.timeOfDay < 22.00 && Clock.timeOfDay > 6) {
					hungerLevel++;
				}
				if (hungerLevel >= 10) {
					if (state == PersonState.normal) {
						stateChanged();
					}
				}
				getHungry();
			}
		}, 
		hungerTime );
	}
	
	private void getTired() {
		timer.schedule(new TimerTask() {
			public void run() {
				if (Clock.timeOfDay < 22.00 && Clock.timeOfDay > 6) {
					tiredLevel++;
				}
				if (tiredLevel >= 12) {
					if (state == PersonState.normal) {
						stateChanged();	
					}
				}
				getTired();
			}
		}, 
		tiredTime );
	}	
	
	private void checkTime() {
		timer.schedule(new TimerTask() {
			public void run() {
				if ( Clock.timeOfDay == 22 ) {
					CityComponent c;
					for( Map.Entry<String, CityComponent> entry : cityMap.entrySet() ) {
						c = entry.getValue();
						c.setOpen(false);
					}
					// keep own house open always
					if (housingLocation != null) {
						c = cityMap.get(housingLocation);
						c.setOpen(true);
					}
				}
				
				if ( timeHouseBought != null ) {
					if ( Clock.timeOfDay == timeHouseBought ) {
						payMaintenanceDayCount++;
						payRentDayCount++;
						if (payRentDayCount >= 2) {
							payRentDayCount = 0;
							Role r = findRole("HousingOfficeCustomer");
							SimCityGui.housingOfficePanel.owner.msgResidentNeedsToPayRent((HouseCustomerRole) r);
							if (AIEnabled) {
								state = PersonState.toHousingOffice;
							}
						}
						else if (payMaintenanceDayCount >= 3) {
							payMaintenanceDayCount = 0;
							Role r = findRole("HousingOfficeCustomer");
							SimCityGui.housingOfficePanel.owner.msgHouseNeedsMaintenance((HouseCustomerRole) r);
							if (AIEnabled) {
								state = PersonState.toHousingOffice;
							}
						}
					}
				}
				
				if( Clock.timeOfDay == 6) {
					CityComponent c;
					for( Map.Entry<String, CityComponent> entry : cityMap.entrySet() ) {
						c = entry.getValue();
						c.setOpen(true);
					}
				}
				
				if (Clock.timeOfDay == 22 || Clock.timeOfDay == 6) {
					if (state == PersonState.normal) {
						stateChanged();	
					}
				}
				checkTime();
			}
		}, 
		halfAnHour );
	}

	@Override
	public void ToMarket1() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ToMarket2() {
		// TODO Auto-generated method stub
		
	}
}

