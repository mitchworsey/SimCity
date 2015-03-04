package simCity.restaurant4;

import agent.Agent;
import simCity.restaurant4.gui.Restaurant4WaiterGui;
import simCity.restaurant4.interfaces.*;
import simCity.test.mock.EventLog;
import simCity.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */

public abstract class Restaurant4WaiterRole extends Role implements Restaurant4Waiter {
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	
	public enum myCustomerState
	{waiting, seated, readyToOrder, asked, ordered, waitingForFoodToBeCooked, reordering, waitingToBeServed, waitingForCheck, receivedCheck, eating, done};
	
	public Menu menu = new Menu(); //make menu public so that it is visible to all agents
	
	private String name;
	private Restaurant4Host host;
	protected Restaurant4Cook cook;
	private Restaurant4Cashier cashier;
	private boolean breakAllowed = false;
	
	protected Semaphore allow = new Semaphore(0,true);

	public Restaurant4WaiterGui waiterGui = null;

	public Restaurant4WaiterRole(String name) {
		super();

		this.name = name;
	}

	public void setHost(Restaurant4Host host) {
		this.host = host;
	}
	
	public void setCook(Restaurant4Cook cook) {
		this.cook = cook;
	}
	
	public void setCashier(Restaurant4Cashier cashier) {
		this.cashier = cashier;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public boolean hasLeftTable() {
		return waiterGui.hasLeftTable();
	}

	public List getMyCustomers() {
		return customers;
	}

	// Messages
	public void msgBreakPermitted() {
		print("BREAK ALLOWED");
		breakAllowed = true;
		stateChanged();
	}
	
	public void msgKeepWorking() {
		print("CONTINUING TO WORK");
		breakAllowed = false;
		stateChanged();
	}
	
	public void msgAtDestination() { //from animation
		allow.release();
		stateChanged();
	}
	
	public void msgLeftTable() { //from animation
		stateChanged();
	}
	
	public void msgSeatCustomer(Restaurant4Customer c, int table, int seat) {
		customers.add(new MyCustomer(c, table, seat, myCustomerState.waiting));
		stateChanged();
	}
	
	public void msgReadyToOrder(Restaurant4Customer c) {
		for (MyCustomer customer : customers) {
			if(customer.customer == c) {
				print("Received msgReadyToOrder");
				customer.state = myCustomerState.readyToOrder;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgHereIsMyOrder(Restaurant4Customer c, String choice) {
		for (MyCustomer customer : customers) {
			if(customer.customer == c) {
				print("Received msgHereIsMyOrder");
				customer.choice = choice;
				customer.state = myCustomerState.ordered;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgOrderReady(int table) {
		for (MyCustomer customer : customers) {
			if(customer.table == table) {
				print("Received msgOrderReady");
				customer.state = myCustomerState.waitingToBeServed;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgCheckComputed(double amount, int table) {
		for (MyCustomer customer : customers) {
			if(customer.table == table) {
				customer.checkAmt = amount;
				customer.state = myCustomerState.waitingForCheck;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgDoneEatingAndLeaving(Restaurant4Customer c) {
		for (MyCustomer customer : customers) {
			if(customer.customer == c) {
				print("Received msgDoneEatingAndLeaving");
				customer.state = myCustomerState.done;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgOutOfFood(String choice) {
		for(MyCustomer customer : customers) {
			if(customer.choice == choice && customer.state == myCustomerState.waitingForFoodToBeCooked) {
				print("Telling customer to reorder");
				customer.state = myCustomerState.reordering;
				stateChanged();
				break;
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try {
			if(breakAllowed) {
				goOnBreak();
				return true;
			}
			for (MyCustomer customer : customers) {
				if (customer.state == myCustomerState.waiting) {
					if (hasLeftTable()){
						seatCustomer(customer);
						return true;
					}
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == myCustomerState.readyToOrder) {
					if (hasLeftTable()){
						takeOrder(customer);
						return true;
					}
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == myCustomerState.ordered) {
					giveCookOrder(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == myCustomerState.reordering) {
					if (hasLeftTable()){
						tellCustomerToReOrder(customer);
						return true;
					}
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == myCustomerState.waitingToBeServed) {
					if (hasLeftTable()){
						serveCustomer(customer);
						return true;
					}
				}
			}
			for(MyCustomer customer : customers) {
				if(customer.state == myCustomerState.waitingForCheck) {
					if (hasLeftTable()){
						giveCustomerCheck(customer);
						return true;
					}
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == myCustomerState.done) {
					tellHostTableIsFree(customer, customer.table);
					return true;
				}
			}
		}
		catch (ConcurrentModificationException cce) {
			return true;
		}
		return false;
	}

	// Actions
	private void goOnBreak() {
		host.msgGoingOnBreak(this);
		breakAllowed = false;
	}
	
	private void seatCustomer(MyCustomer c) {
		if(c.seat != 0) {
			waiterGui.DoGoToWaitingArea(c.seat);
			try {
				allow.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			waiterGui.DoGoGetCustomer();
			try {
				allow.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DoGoToCustomer(c.customer, c.table);
		c.customer.msgFollowMe(c.table);
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
		c.state = myCustomerState.seated;
	}
	
	private void takeOrder(MyCustomer c) {
		DoGoToCustomer(c.customer, c.table);
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.customer.msgWhatDoYouWant();
		c.state = myCustomerState.asked;
	}
	
	protected abstract void giveCookOrder(MyCustomer c);
	
	private void tellCustomerToReOrder(MyCustomer c) {
		DoGoToCustomer(c.customer, c.table);
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.customer.msgChooseNewOrder();
		c.state = myCustomerState.asked;
	}
	
	private void serveCustomer(MyCustomer c) {
		waiterGui.DoGoPickUpFood(c.choice);
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DoGoToCustomer(c.customer, c.table);
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.customer.msgHereIsYourFood();
		cashier.msgCreateCheck(this, c.customer, c.choice, c.table);
		c.state = myCustomerState.eating;
		waiterGui.DoLeaveCustomer();	
	}
	
	private void giveCustomerCheck(MyCustomer c) {
		DoGoToCustomer(c.customer, c.table);
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.customer.msgHereIsYourCheck(c.checkAmt);
		c.state = myCustomerState.receivedCheck;
		waiterGui.DoLeaveCustomer();
	}
	
	private void tellHostTableIsFree(MyCustomer c, int table) {
		host.msgTableIsFree(this, table);
		customers.remove(c);
		waiterGui.DoLeaveCustomer();
	}
	
	// The animation DoXYZ() routines
	private void DoGoToCustomer(Restaurant4Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Going to " + customer + " at " + table);
		waiterGui.DoBringToTable(table); 
	}

	//utilities

	public void setGui(Restaurant4WaiterGui gui) {
		waiterGui = gui;
	}

	public Restaurant4WaiterGui getGui() {
		return waiterGui;
	}
	
	public String toString() {
		return "waiter " + getName();
	}

	public class MyCustomer {
		Restaurant4Customer customer;
		int table;
		int seat;
		double checkAmt;
		String choice;
		myCustomerState state = myCustomerState.waiting;//The start state

		MyCustomer(Restaurant4Customer c, int table, int seat, myCustomerState state) {
			this.customer = c;
			this.table = table;
			this.seat = seat;
			this.checkAmt = 0;
			this.choice = null;
			this.state = state;
		}
		
		public myCustomerState getState() {
			return state;
		}
		
		public int getTableNum() {
			return table;
		}
		
		public String getOrder() {
			return choice;
		}
	}
	
	public static class Menu {
		public static Map<Integer, String> menuMap = new HashMap<Integer, String>(); //public for now -- accessible by all agents
		
		Menu() {
			menuMap.put(1, "steak");
			menuMap.put(2, "chicken");
			menuMap.put(3, "salad");
			menuMap.put(4, "pizza");
		}
	}
}

