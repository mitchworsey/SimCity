package simCity.Restaurant1;

import simCity.Role;
//import simCity.Restaurant1.Restaurant1Waiter.Table;
//import simCity.gui.HostGui;

import simCity.interfaces.Restaurant1Customer;
import simCity.interfaces.Restaurant1Host;
import simCity.interfaces.Restaurant1Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */

public class Restaurant1HostRole extends Role implements Restaurant1Host {
	
	static final int NTABLES = 4;
	public List<Restaurant1Customer> allCustomers = Collections.synchronizedList(new ArrayList<Restaurant1Customer>());
	public List<Restaurant1Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Restaurant1Customer>());
	public List<Bills> customersWithDebt = Collections.synchronizedList(new ArrayList<Bills>());
	public List<Restaurant1Waiter> waitingWaiters = Collections.synchronizedList(new ArrayList<Restaurant1Waiter>());
	public List<Restaurant1Waiter> waiterWantsBreak = Collections.synchronizedList(new ArrayList<Restaurant1Waiter>()); 
	public List<Restaurant1Waiter> waiterOnBreak = Collections.synchronizedList(new ArrayList<Restaurant1Waiter>());
	public static List<Table> tables;
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	//public HostGui hostGui = null;
	
	boolean atStart = true;
	boolean pause = false;
	boolean action = false;

	public Restaurant1HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		synchronized(tables) {
			for (int ix = 1; ix <= NTABLES; ix++) {
				tables.add(new Table(ix));//how you add to a collections
			}
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}
	
	public List getWaitingWaiters() {
		return waitingWaiters;
	}

	public Collection getTables() {
		return tables;
	}
	
	public static int getNumTables() {
		return NTABLES;
	}
	
	public void addWaiters(Restaurant1Waiter wait) {
		wait.msgSetHost(this);
		waitingWaiters.add(wait);
	}
	
	// Messages
	
	public void msgIWantFood(Restaurant1Customer cust) {
		waitingCustomers.add(cust);
		allCustomers.add(cust);
		if (cust != null){
		}
		if (!waitingCustomers.isEmpty()){
			System.out.println("Its not empty" + name);
		}
		stateChanged();
	}
	

	public void msgLeavingTable(Restaurant1Customer cust) {
		synchronized(tables) {
			for (Table table : tables) {
				if (table.getOccupant() == cust) {
					table.setUnoccupied();
					stateChanged();
				}
			}
		}
	}

	public void msgAtTable() {//from animation
		atTable.release();// = true;
		atStart = false;
		stateChanged();
	}
	
	public void msgAtStart(Restaurant1Waiter wait) {
		//atStart = true;
		boolean notOnList = true;
		synchronized(waitingWaiters){
			for (Restaurant1Waiter waiter : waitingWaiters){
				if ( wait == waiter)
					notOnList = false;
			}
		}
		if (notOnList) {
    		waitingWaiters.add(wait);
		}
		stateChanged();
	}
	
	public void msgCustomerWithDebt(String customerName, String order) {
		Bills nextCustomer = new Bills();
		synchronized(allCustomers){
			for (Restaurant1Customer cust : allCustomers) {
				if (cust.getCustomerName() == customerName){
					nextCustomer.setCustomer(cust);
					nextCustomer.setOrder(order);
				}
			}
		}
		//Bills nextCustomer = new Bills(customer, order);
		customersWithDebt.add(nextCustomer);
		stateChanged();
	}
	
	public void msgHereIsMoney(Restaurant1Customer customer) {
		synchronized(customersWithDebt){
			for (int i = 0; i < customersWithDebt.size(); i++) {
				Bills bill = customersWithDebt.get(i);
				if (bill.getCustomer() == customer)
					customersWithDebt.remove(bill);
			}
		}
		waitingCustomers.add(customer);
		stateChanged();
	}
	
	public void msgCustomerLeft(String customerName) {
		synchronized(allCustomers){
			for (int i = 0; i < allCustomers.size(); i++) {
				Restaurant1Customer cust = allCustomers.get(i);
				if (cust.getCustomerName() == customerName)
					allCustomers.remove(cust);
			}
		}
		synchronized(tables){
			for (Table table: tables) {
				if (table.getOccupant().getCustomerName() == customerName)
					table.setUnoccupied();
			}
		}
		stateChanged();
	}
	
	public void msgAskForBreak(Restaurant1Waiter waiter) {
		waiterWantsBreak.add(waiter);
		stateChanged();
	}
	
	public void msgBackAtWork(Restaurant1Waiter waiter) {
		waiterOnBreak.remove(waiter);
		waitingWaiters.add(waiter);
		stateChanged();
	}
	
	public void msgPause() {
		pause = true;
		action = true;
		stateChanged();
	}

	public void msgUnpause() {
		pause = false;
		action = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
        if(!waiterWantsBreak.isEmpty()) {
        	checkForBreak(waiterWantsBreak.get(0));
        	return true;
        }
        if (!waitingCustomers.isEmpty()){
        	synchronized(waitingCustomers){
        		for (int i=0; i < waitingCustomers.size(); i++){
        			waitingCustomers.get(i).msgStandAt(i);
        		}
        	}
        }
        if (!waitingWaiters.isEmpty()){
        	synchronized(waitingWaiters){
        		for (Restaurant1Waiter waiter : waitingWaiters) {
        			synchronized(tables){
        				for (Table table : tables) {
        					if (table.getOccupant() == null)
        						waiter.msgUpdateTablesNull(table.getNumber());
        					else
        						waiter.msgUpdateTables(table.getOccupant(), table.getNumber());
        				}
        			}
        		}
        	}
        }
        synchronized(tables){
        	for (Table table : tables) {
        		if (!table.isOccupied()) {
        			if (!waitingCustomers.isEmpty()) {
        				synchronized(waitingCustomers){
        					for (Restaurant1Customer customer : waitingCustomers) {
        						if (!customersWithDebt.isEmpty()) {
        							synchronized(customersWithDebt){
        								for (Bills bill : customersWithDebt) {
        									if(customer == bill.getCustomer()){
        										requestMoney(bill);
        										return true;
        									}
        								}
        							}
        						}
        					}
        				}
        				if(!waitingWaiters.isEmpty()) {
        					seatCustomer(waitingCustomers.get(0), table, waitingWaiters.get(0));//the action
        					return true;
        				}
        			}
        		}
        	}
        }
		return false;
	}

	// Actions
	
	private void checkForBreak(Restaurant1Waiter waiter) {
		waiterWantsBreak.remove(waiter);
		waitingWaiters.remove(waiter);
		boolean allow = true;
		if (waitingWaiters.size() < 1)
			allow = false;
		synchronized(allCustomers){
			for (Restaurant1Customer customer : allCustomers) {
				if (customer.getWaiter() == waiter)
					allow = false;
			}
		}
		if (allow) {
			print("Waiter " + waiter.getName() + " may go on break");
			waiter.msgGoOnBreak();
			waiterOnBreak.add(waiter);
		}
		else {
			print("Waiter " + waiter.getName() + " may not go on break");
			waiter.msgNoBreak();
		}
	}

	private void seatCustomer(Restaurant1Customer customer, Table table, Restaurant1Waiter waiter) {
		System.out.println("Sent seat customer");
		table.setOccupant(customer);
		waitingWaiters.remove(waiter);
		customer.setWaiter(waiter);
		waiter.msgSeatCustomer(customer, table.getNumber());		
		print("Sent message to " + waiter.getName() + " to seat " + customer);
		waitingCustomers.remove(customer);	
	}
	
	private void requestMoney(Bills bill) {
		print("Customer " + bill.getCustomer() + " owes money");
		waitingCustomers.remove(bill.getCustomer());
		(bill.getCustomer()).msgRequestMoney(bill.getAmount());
	}

	//utilities

	/*public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}*/
	

	public class Table {
		Restaurant1Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		

		void setOccupant(Restaurant1Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Restaurant1Customer getOccupant() {
			if (occupiedBy == null)
				return null;
			else 
				return occupiedBy;
		}

		public boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		public int getNumber() {
			return tableNumber;
		}
	}
	
	public class Bills {
		Restaurant1Customer customer;
		double amount;
		String food = new String();
		
		Bills() {
			customer = null;
			amount = 0;
		}
		
		Bills(Restaurant1Customer cust, double amt) {
			customer = cust;
			amount = amt;
		}
		
		Bills(Restaurant1Customer cust, String order) {
			customer = cust;
			food = order;
			if(order.equals("pizza")) {
				amount = 8.99;
			}
			if(order.equals("steak")) {
				amount = 15.99;
			}
			if(order.equals("salad")) {
				amount = 5.99;
			}
			if(order.equals("chicken")) {
				amount = 10.99;
			}
		}
		
		void setOrder(String order) {
			food = order;
			if(order.equals("pizza")) {
				amount = 8.99;
			}
			if(order.equals("steak")) {
				amount = 15.99;
			}
			if(order.equals("salad")) {
				amount = 5.99;
			}
			if(order.equals("chicken")) {
				amount = 10.99;
			}
		}
		
		void setCustomer(Restaurant1Customer cust) {
			customer = cust;
		}
		
		void setAmount(double amt) {
			amount = amt;
		}
		
		Restaurant1Customer getCustomer() {
			return customer;
		}
		
		double getAmount() {
			return amount;
		}
	}

}
