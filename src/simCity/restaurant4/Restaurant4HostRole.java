package simCity.restaurant4;

import simCity.Role;
import simCity.restaurant4.gui.Restaurant4WaiterGui;
import simCity.restaurant4.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class Restaurant4HostRole extends Role implements Restaurant4Host {
	static final int NTABLES = 3;//a global for the number of tables.
	static final int NSEATS = 12;//a global for the number of waiting area seats.
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Restaurant4Customer> waitingCustomers
	= new ArrayList<Restaurant4Customer>();
	/*public List<WaiterAgent> waiters
	= new ArrayList<WaiterAgent>();*/
	private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public enum hostState {none, askCookToCheckInventory};
	hostState state = hostState.askCookToCheckInventory;
	
	public enum myWaiterState
	{pending, working, requestedBreak, leavingForBreak, onBreak, returningToWork, requestSettled};
	
	public Collection<WaitingSeat> waitingAreaSeats;
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private Restaurant4Cook cook;
	
	private String name;
	private Semaphore allow = new Semaphore(0,true);

	public Restaurant4HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
		waitingAreaSeats = Collections.synchronizedList(new ArrayList<WaitingSeat>());
		for (int ix = 1; ix <= NSEATS; ix++) {
			waitingAreaSeats.add(new WaitingSeat(ix));//how you add to a collections
		}
	}
	
	public void setCook(Restaurant4Cook cook) {
		this.cook = cook;
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

	public Collection getTables() {
		return tables;
	}
	// Messages
	public void newWaiterAdded(Restaurant4Waiter w) { //from Gui
		waiters.add(new MyWaiter(w, 0, myWaiterState.pending));
		stateChanged();
	}

	public void msgIWantFood(Restaurant4Customer cust) {
		print("the customer wants food");
		//allow.release();
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgLeavingBeforeEating(Restaurant4Customer cust, int loc) {
		synchronized (waitingAreaSeats) {
			for (WaitingSeat seat : waitingAreaSeats) {
				if(seat.seat == loc) {
					seat.setUnoccupied();
					waitingCustomers.remove(cust);
					return;
				}
			}
		}
	}

	public void msgWaitingSeatIsFree(int loc) {
		synchronized (waitingAreaSeats) {
			for (WaitingSeat seat : waitingAreaSeats) {
				if(seat.seat == loc) {
					seat.setUnoccupied();
					return;
				}
			}
		}
	}

	public void msgWantToGoOnBreak(Restaurant4Waiter w) {
		synchronized (waiters) {
			for (MyWaiter waiter : waiters) {
				if(waiter.waiter == w) {
					waiter.state = myWaiterState.requestedBreak;
					stateChanged();
					break;
				}
			}
		}
	}
	
	public void msgGoingOnBreak(Restaurant4Waiter w) {
		synchronized (waiters) {
			for(MyWaiter waiter : waiters) {
				if(waiter.waiter == w) {
					waiter.state = myWaiterState.leavingForBreak;
					stateChanged();
					break;
				}
			}
		}
	}
	
	public void msgGoingBackToWork(Restaurant4Waiter w) {
		synchronized (waiters) {
			for(MyWaiter waiter : waiters) {
				if(waiter.waiter == w) {
					waiter.state = myWaiterState.returningToWork;
					stateChanged();
					break;
				}
			}
		}
	}

	public void msgTableIsFree(Restaurant4Waiter w, int tableNum) {
		synchronized (tables) {
			for (Table table : tables) {
				if(table.getTableNum() == tableNum) {
					print(table + " is free.");
					table.setUnoccupied();
					stateChanged();
					break;
				}
			}
		}
		synchronized (waiters) {
			for (MyWaiter waiter : waiters) {
				if(waiter.waiter == w) {
					waiter.customers--; //decrease count of customers being served by this WaiterAgent instance
					break;
				}
			}
		}
	}

	/*public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}*/

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if (waitingCustomers.isEmpty() && state == hostState.askCookToCheckInventory) {
			tellCookToCheckInventory();
			return true;
		}
		for (MyWaiter waiter : waiters) {
			if(waiter.state == myWaiterState.requestedBreak) {
				negotiateWaiterBreakRequest(waiter);
				return true;
			}
		}
		for (MyWaiter waiter : waiters) {
			if(waiter.state == myWaiterState.leavingForBreak) {
				allowBreak(waiter);
				return true;
			}
		}
		for (MyWaiter waiter : waiters) {
			if(waiter.state == myWaiterState.returningToWork) {
				welcomeBack(waiter);
				return true;
			}
		}
		if (!waitingCustomers.isEmpty() && !waiters.isEmpty()) {
			print("waiters is not empty");
			synchronized (tables) {
				for (Table table : tables) {
					if (!table.isOccupied()) {
						MyWaiter optWaiter = waiters.get(0);
						for(MyWaiter waiter : waiters) { //assign the next customer to the waiter with the least customers in his list
							if(waiter.customers < optWaiter.customers) { 
								optWaiter = waiter;
							}
						}
						if(!optWaiter.onBreak) {
							tellWaiterToSeatCustomer(waitingCustomers.get(0), optWaiter, table);//the action
							return true;
						}
						else { //if the waiter with the least customers is on break, assign customer to random waiter
							while(true) {
								Random waiter = new Random();
								int i = waiter.nextInt(waiters.size());
								if(!waiters.get(i).onBreak) { 
									print("WAITER: "+waiters.get(i));
									tellWaiterToSeatCustomer(waitingCustomers.get(0), waiters.get(i), table);//the action
									return true;
								}
							}
						}
					}
				}
			}
			tellCustomerRestaurantIsFull(waitingCustomers.get(waitingCustomers.size()-1)); // the last customer in the list
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void tellCookToCheckInventory() {
		cook.msgCheckInventory();
		state = hostState.none;
	}
	
	private void negotiateWaiterBreakRequest(MyWaiter w) {
		boolean waitersAvailable = false;
		synchronized (waiters) {
			for(MyWaiter waiter : waiters) { 
				if(!waiter.onBreak && waiter != w) { //if at least one other waiter is working
					waitersAvailable = true;
				}
			}
		}
		w.state = myWaiterState.requestSettled;
		if(waiters.size() > 1 && waitersAvailable) { 
			w.waiter.msgBreakPermitted();
		}
		else {
			w.waiter.msgKeepWorking();
			((Restaurant4WaiterGui) w.waiter.getGui()).resetOnBreak();
		}
		((Restaurant4WaiterGui) w.waiter.getGui()).enableBreak();
	}
	
	private void allowBreak(MyWaiter w) {
		print("ON BREAK");
		w.onBreak = true;
		w.state = myWaiterState.onBreak;
	}
	
	private void welcomeBack(MyWaiter w) {
		w.onBreak = false;
		w.waiter.msgKeepWorking();
		w.state = myWaiterState.working;
	}

	private void tellWaiterToSeatCustomer(Restaurant4Customer customer, MyWaiter waiter, Table table) {
		int seatNum = 0;
		customer.setWaiter(waiter.waiter);
		
		synchronized (waitingAreaSeats) {
			for (WaitingSeat seat : waitingAreaSeats) {
				if(seat.getOccupant() == customer) {
					seatNum = seat.seat;
					break;
				}
			}
		}
		
		waiter.waiter.msgSeatCustomer(customer, table.getTableNum(), seatNum);

		table.setOccupant(customer);
		waiter.customers++; //increase count of customers being served by this WaiterAgent instance

		waitingCustomers.remove(customer);
	}
	
	private void tellCustomerRestaurantIsFull(Restaurant4Customer customer) {
		synchronized (waitingAreaSeats) {
			for (WaitingSeat seat : waitingAreaSeats) {
				if (seat.getOccupant() == customer) { //if the customer is already waiting in the restaurant
					return;
				}		
			}
		}
		synchronized (waitingAreaSeats) {
			for (WaitingSeat seat : waitingAreaSeats) {
				if (!seat.isOccupied()) {
					print("Go to seat "+seat.seat);
					// message the last customer in the list 
					waitingCustomers.get(waitingCustomers.size()-1).msgRestaurantIsFull(seat.seat);
					seat.setOccupant(customer);
					return;
				}
			}
		}
	}

	//utilities

	/*public void setGui(Restaurant4/ gui) {
		hostGui = gui;
	}*/
	
	private static class MyWaiter {
		Restaurant4Waiter waiter;
		int customers = 0; //number of customers being served by this waiter
		boolean onBreak = false;
		myWaiterState state;

		MyWaiter(Restaurant4Waiter waiter, int customers, myWaiterState state) {
			this.waiter = waiter;
			this.customers = customers;
			this.state = state;
		}
	}

	/*public Restaurant4HostGui getGui() {
		return hostGui;
	}*/
	
	private class WaitingSeat {
		Restaurant4Customer occupiedBy;
		int seat;
		
		WaitingSeat(int seat) {
			this.seat = seat;
		}
		
		void setOccupant(Restaurant4Customer cust) {
			occupiedBy = cust;
		}
		
		Restaurant4Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}
		
		void setUnoccupied() {
			occupiedBy = null;
		}
	}

	private class Table {
		Restaurant4Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Restaurant4Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Restaurant4Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		public int getTableNum() {
			return tableNumber;
		}
	}
}

