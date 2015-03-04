package simCity.house;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import simCity.Location;
import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.gui.HouseOwnerGui;
import simCity.gui.SimCityGui;
import simCity.house.House.HouseState;
import simCity.interfaces.HouseCustomer;
import simCity.interfaces.HouseMaintenanceManager;
import simCity.interfaces.HouseOwner;
import simCity.interfaces.Person;

public class HouseOwnerRole extends Role implements HouseOwner{
	String name;
	double money = 300.00;
	//public List<MyResident> residents;
	public List<MyCustomer> customers;
	public List<House> houses;
	HouseMaintenanceManager maintenance;
	//House house1 = new House(this, null, new MockHouseMaintenanceManagerRole("Mock Maintenance"), "Rented Home", 1111, 500.00, 100.00, 50.00, HouseState.maintained);

	private Semaphore atMaintenance = new Semaphore(0,true);
	private Semaphore atRealEstate = new Semaphore(0,true);
	private Semaphore leftOffice = new Semaphore(0,true);
	
	HouseOwnerGui gui;
	/*
	public class MyResident{
		HouseResident hr;
		public House house;
		public ResidentState rs;
		MyResident(HouseResident hr, House h, ResidentState rs){
			this.hr = hr;
			this.house = h;
			this.rs = rs;
		}
	}
	public enum ResidentState {needsToPayRent, askedToPayRent, paidRent};
		
		*/
	public class MyCustomer{
		HouseCustomer hc;
		public House house;
		public CustomerState cs;
		MyCustomer(HouseCustomer hc, House h, CustomerState cs){
			this.hc = hc;
			this.house = h;
			this.cs = cs;
		}
	}
	public enum CustomerState {wantsToBuyProperty, toldCantBuyProperty, askedToPayDeposit, paidDeposit, movedIn, askedToPayRent, needsToPayRent, paidRent};

	public HouseOwnerRole(String name) {
		super();
		this.name = name;
		//residents = new ArrayList<MyResident>();
		customers = new ArrayList<MyCustomer>();
		houses = new ArrayList<House>();
	}	
		/*
	public MyResident findResident(HouseResident r){
		for(MyResident myRes: residents){
			if(myRes.hr == r)
				return myRes;		
		}
		return null;
	}
	*/
	
	public void setMaintenanceManager(HouseMaintenanceManager hmm){
		maintenance = hmm;
	}
	
	public void createHouses(){
		House house;
		for(int a = 0; a < 16; a++){
			house = new House(this, null, maintenance, "Apartment1", 1111+a, 325, 520, 500.00, 100.00, 50.00, HouseState.maintained);
			houses.add(house);
		}
		for(int a = 0; a < 16; a++){
			house = new House(this, null, maintenance, "Apartment2", 2222+a, 415, 520, 250.00, 50.00, 25.00, HouseState.maintained);
			houses.add(house);
		}
		for(int a = 0; a < 16; a++){
			house = new House(this, null, maintenance, "Apartment3", 3333+a, 510, 520, 300.00, 60.00, 30.00, HouseState.maintained);
			houses.add(house);
		}
		for(int a = 0; a < 16; a++){
			house = new House(this, null, maintenance, "Apartment4", 4444+a, 610, 520, 200.00, 40.00, 20.00, HouseState.maintained);
			houses.add(house);
		}
	}
	
	public MyCustomer findCustomer(HouseCustomer c){
		for(MyCustomer myCus: customers){
			if(myCus.hc == c)
				return myCus;		
		}
		return null;
	}
	
	public House findHouse(House h){
		for(House house: houses){
			if(h == house)
				return house;		
		}
		return null;
	}
	
	public House findHouseWithCustomer(HouseCustomer hc){
		for(House house: houses){
			if(house.occupiedBy == hc){
				return house;
			}
		}
		return null;
	}
	
	public House findAvailableHouse(){
		for(House house: houses){
			if(house.occupiedBy == null)
				return house;		
		}
		return null;
	}
	
	public void setGui(HouseOwnerGui g){
		gui = g;
	}
		
		
	
	
//Messages
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseOwner#msgIWantToLiveHere(simCity.interfaces.HouseCustomer)
	 */
	@Override
	public void msgIWantToLiveHere(HouseCustomer r){
		print("Received msgIWantToLiveHere");
		House h = findAvailableHouse();
		customers.add(new MyCustomer(r, h, CustomerState.wantsToBuyProperty));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseOwner#msgHereIsSecurityDeposit(simCity.interfaces.HouseCustomer, double)
	 */
	@Override
	public void msgHereIsSecurityDeposit(HouseCustomer r, double payment){
		print("Received msgHereIsSecurityDeposit");
		money += payment;
		MyCustomer mc = findCustomer(r);
		mc.cs = CustomerState.paidDeposit;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseOwner#msgResidentNeedsToPayRent(simCity.interfaces.HouseResident)
	 */
	@Override
	public void msgResidentNeedsToPayRent(HouseCustomer r){
		//Owner will receiver this after a timer is finished or on a certain day every month
		print("Received msgHouseNeedsRent");
		MyCustomer mc = findCustomer(r);
		mc.cs = CustomerState.needsToPayRent;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseOwner#msgHereIsRent(simCity.interfaces.HouseResident, double)
	 */
	@Override
	public void msgHereIsRent(HouseCustomer r, double payment){
		print("Received msgHereIsRent");
		money += payment;
		MyCustomer mc = findCustomer(r);
		mc.cs = CustomerState.paidRent;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.house.HouseOwner#msgHouseNeedsMaintenance(simCity.house.HouseCustomer)
	 */
	@Override
	public void msgHouseNeedsMaintenance(HouseCustomer r){
		print("Received msgHouseNeedsMaintenance");
		//Owner will receiver this after a timer is finished or on a certain day every month or if something in the house breaks
		House h = findHouseWithCustomer(r);	
		h.hs = HouseState.needsMaintenance;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.house.HouseOwner#msgPayMaintenanceFee(simCity.house.House)
	 */
	@Override
	public void msgPayMaintenanceFee(House house){
		print("Received msgPayMaintenanceFee");
		House h = findHouse(house);
		h.hs = HouseState.justMaintained;
		stateChanged();
	}
	
	public void msgAtMaintenance(){
		atMaintenance.release();
		stateChanged();
	}
	
	public void msgAtRealEstate(){
		atRealEstate.release();
		stateChanged();
	}
	
	public void msgLeftOffice(){
		leftOffice.release();
		stateChanged();
	}
	
	
	
	
///Scheduler
	public boolean pickAndExecuteAnAction() {
		for(MyCustomer c: customers){
			if(c.cs == CustomerState.wantsToBuyProperty){
				if(c.house != null){
					tellCustomerToPaySecurityDeposit(c);
					return true;
				}
				else{
					tellCustomerNoHousesAvailable(c);
					return true;
				}
			}
		}
		
		for(MyCustomer c: customers){
			if(c.cs == CustomerState.paidDeposit){
				tellCustomerToMoveIn(c);
				return true;
			}
		}
		
		for(MyCustomer c: customers){
			if(c.cs == CustomerState.needsToPayRent){
				tellResidentToPayRent(c);
				return true;
			}
		}
		
		for(House h: houses){
			if(h.hs == HouseState.needsMaintenance){
				askForMaintenance(h);
				return true;
			}
		}
		
		for(House h: houses){
			if(h.hs == HouseState.justMaintained){
				payMaintenanceFee(h);
				return true;
			}
		}
		
		return false;
		
	}

	
	
	
///Actions
	private void tellCustomerToPaySecurityDeposit(MyCustomer mc){
		mc.hc.msgPaySecurityDeposit(mc.house);
		mc.cs = CustomerState.askedToPayDeposit;
	}
	
	private void tellCustomerNoHousesAvailable(MyCustomer mc){
		mc.hc.msgHouseUnavailable();
		mc.cs = CustomerState.toldCantBuyProperty;
		customers.remove(mc);
	}
	
	private void tellCustomerToMoveIn(MyCustomer mc){
		mc.house.occupiedBy = mc.hc;////////////////////////
		mc.hc.msgMoveIn(mc.house);
		mc.cs = CustomerState.movedIn;
		//msgHouseNeedsMaintenance(houses.get(0));
	}
	
	private void tellResidentToPayRent(MyCustomer mc){
		mc.hc.msgPayRent();
		mc.cs = CustomerState.askedToPayRent;
	}
	
	private void askForMaintenance(House h){
		gui.DoGoToMaintenance();
		try {
			atMaintenance.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		h.maintenance.msgINeedMaintenance(h);
		gui.DoGoToRealEstate();
		h.hs = HouseState.waitingForMaintenance;
	}
	
	private void payMaintenanceFee(House h){
		gui.DoGoToMaintenance();
		try {
			atMaintenance.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		money -= h.maintenanceFee;
		h.maintenance.msgHereIsMaintenanceFee(h.maintenanceFee);
		//gui.DoGoToRealEstate();
		h.hs = HouseState.maintained;
		gui.DoGoToRealEstate();
		try {
			atRealEstate.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		/*gui.DoLeaveOffice();
		try {
			leftOffice.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimCityGui.housingOfficePanel.removeGui(gui);
		getPersonAgent().OutOfComponent(this);*/
	}
}
