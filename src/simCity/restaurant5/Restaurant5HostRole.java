package simCity.restaurant5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import simCity.Role;
import simCity.gui.*;
import simCity.interfaces.*;
import simCity.restaurant5.*;
import simCity.restaurant5.gui.Restaurant5HostGui;
import simCity.restaurant5.interfaces.Restaurant5Customer;
import simCity.restaurant5.interfaces.Restaurant5Host;
import simCity.restaurant5.interfaces.Restaurant5Waiter;



/**
 * RESTAURANT HOST AGENT
 */
// We only have 2 types of agents in this prototype. A customer and an agent
// that
// does all the rest. Rather than calling the other agent a waiter, we called
// him
// the HostAgent. A Host is the manager of a restaurant who sees that all
// is proceeded as he wishes.
public class Restaurant5HostRole extends Role implements Restaurant5Host{ //implements Restaurant5Host {

	static final int NTABLES = 4;// a global for the number of tables.
	// Notice that we implement waitingCustomers using ArrayList, but type it
	// with List semantics.
	private List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private List<MyWaiter> myWaiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private Collection<Table> tables;
	// note that tables is typed with Collection semantics.
	// Later we will see how it is implemented

	private String name;
	private int minWaiternum = 0;
	

	public enum HostState {
		DoingNothing, ServingCustomer
	};

	//private HostState hstate = HostState.DoingNothing;
	public Restaurant5HostGui hostGui = null;

	public enum CustomerState {
		Waiting, Seated, Done
	};

	private CustomerState cstate = CustomerState.Waiting;

	public enum WaiterState {
		Working, RequestBreak, OnBreak, OffBreak
	};


	public Restaurant5HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));// how you add to a collections
		}
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#getName()
	 */
	public String getMaitreDName() {
		return name;
	}


	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#getCustomers()
	 */
	@Override
	public List getCustomers() {
		return myCustomers;
	}

	//List of waiting customers?


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#getTables()
	 */
	@Override
	public Collection getTables() {
		return tables;
	}


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#getMyWaiters()
	 */
	@Override
	public List getMyWaiters() {
		return myWaiters;
	}

	// ////////////MESSAGES ///////////////


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#msgIWantBreak(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void msgIWantBreak(Restaurant5Waiter waiter) { // check if that specific waiter can go on break -> how many waiters? how many
		print(waiter + " wants to go on break.");
		findWaiter(waiter).ws = WaiterState.RequestBreak;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#msgBackFromBreak(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void msgBackFromBreak(Restaurant5Waiter waiter) {
		print(waiter + " is back.");
		findWaiter(waiter).ws = WaiterState.OffBreak;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#msgIWantFood(simCity.restaurant5.interfaces.Restaurant5Customer)
	 */
	@Override
	public void msgIWantFood(Restaurant5Customer cust) {
		myCustomers.add(new MyCustomer(cust, cstate.Waiting));
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#msgIAmHereToWork(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void msgIAmHereToWork(Restaurant5Waiter wait) {
		AddWaiter(wait);
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#findWaiter(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public MyWaiter findWaiter(Restaurant5Waiter waiter) {
		for  (int i=0; i< myWaiters.size() ; i++) {
			if (myWaiters.get(i).w == waiter) {
				return myWaiters.get(i);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#msgLeavingTable(simCity.restaurant5.interfaces.Restaurant5Customer, simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void msgLeavingTable(Restaurant5Customer cust, Restaurant5Waiter waiter) {
		//make scheduler remove the cust
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				findWaiter(waiter).numberOfCustomers --;
				table.setUnoccupied();
				stateChanged();
			}
		}
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#howManyCustomers()
	 */
	@Override
	public int howManyCustomers() {
		return myCustomers.size();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#numWaitersWorking()
	 */
	@Override
	public int numWaitersWorking() {
		int waitersWorking = 0;
		for (int i=0; i<myWaiters.size(); i++) {
			if ((myWaiters.get(i).ws == WaiterState.Working)) {
				waitersWorking = waitersWorking + 1;
			}
		}
		return waitersWorking;
	}


	/**
	 *********** SCHEDULER: Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/*
		 * Think of this next rule as: Does there exist a table and customer, so
		 * that table is unoccupied and customer is waiting. If so seat him at
		 * the table.
		 */

		/*
		 * if WaitingCustomers is !empty & there exists t in tables such that
		 * t.isOccupied!= true, then SeatCustomer(WaitingCustomer[0].t)
		 */

		if(!myWaiters.isEmpty()){

			int minCust = myWaiters.get(0).numberOfCustomers;
			MyWaiter lstBusy = myWaiters.get(0);
			for (int i=0; i<myWaiters.size(); i++) {
				if (myWaiters.get(i).numberOfCustomers < minCust && (myWaiters.get(i).ws == WaiterState.Working)) { // not on break, working
					minCust = myWaiters.get(i).numberOfCustomers;
					lstBusy = myWaiters.get(i);
				}
			}


			for (int j = 0; j < myCustomers.size(); j++) { // for each customer
				if (myCustomers.get(j).s == CustomerState.Waiting) { // are there any waiting customers?
					for (Table table : tables) {
						if (!table.isOccupied()) { // are there any unoccupied tables?
							seatCustomer(myCustomers.get(j), lstBusy, table);// the action
							return true;
						}
					}
					return true;
				}
			}

			for (int i=0; i<myWaiters.size(); i++) { // response to break request for waiter
				if (myWaiters.get(i).ws == WaiterState.RequestBreak) {
					if (numWaitersWorking() >= 1) {
						myWaiters.get(i).ws = WaiterState.OnBreak;
						AcceptWaiterBreak(myWaiters.get(i).w);
						return true;
					}
					if (numWaitersWorking() < 1){
						myWaiters.get(i).ws = WaiterState.Working;
						DenyWaiterBreak(myWaiters.get(i).w);
						return true;
					}

				}
				if (myWaiters.get(i).ws == WaiterState.OffBreak) { // setting onBreak to false -> avail to seatCust
					WaitBackFromBreak(myWaiters.get(i).w);
					return true;
				}
			} 

			return true;

		}

		return false;
	}

	/*
	 * if (hstate == HostState.DoingNothing) { for (Table table : tables) {
	 * if (!table.isOccupied()) { if (!myWaiters.isEmpty()) { if
	 * (!waitingCustomers.isEmpty()) { seatCustomer(waitingCustomers.get(0),
	 * myWaiters.get(0), table);//the action return true;//return true to
	 * the abstract agent to reinvoke the scheduler. } } } } }
	 */// Customers are seating themselves rather than waiting until Host
	// goes to table with them

	// we have tried all our rules and found
	// nothing to do. So return false to main loop of abstract agent
	// and wait.


	// ///////////// ACTIONS ////////////////

	private void seatCustomer(MyCustomer customer, MyWaiter wait, Table table) {
		print("Assigning " + wait.w.getName() + " to seat " + customer.c.getName() + " at table " + table.tableNumber);
		wait.w.msgSeatCustomer(customer.c, wait.w, table.tableNumber);// Tell waiter to seat customer
		wait.numberOfCustomers ++;
		customer.c.setWaiter(wait.w);
		customer.table = table.tableNumber;
		customer.s = CustomerState.Seated;
		table.setOccupant(customer.c);

	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#AddWaiter(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void AddWaiter(Restaurant5Waiter waiter) {
		myWaiters.add(new MyWaiter(waiter, WaiterState.Working, 0));
	//	myWaiters.add(waiter);
	}


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#AcceptWaiterBreak(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void AcceptWaiterBreak(Restaurant5Waiter waiter) {
		print("Waiter break request accepted.");
		findWaiter(waiter).ws = WaiterState.OnBreak;
		waiter.msgAcceptBreak();
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#DenyWaiterBreak(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void DenyWaiterBreak(Restaurant5Waiter waiter) {
		print("Waiter break request denied.");
		findWaiter(waiter).ws = WaiterState.Working;
		waiter.msgDenyBreak();
		stateChanged();

	}


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#WaitBackFromBreak(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void WaitBackFromBreak(Restaurant5Waiter waiter) {
		print("Waiter back from break.");
		findWaiter(waiter).ws = WaiterState.Working;
		stateChanged();
	}

	// ///////////// ANIMATION ////////////// //The animation DoXYZ() routines
	// Host has no GUI calls because its job is all stationary (off screen)

	// //////////////UTILITIES /////////////////

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Host#callStateChange()
	 */
	@Override
	public void callStateChange() {
		stateChanged();
	}

	@Override
	public void setGui(Restaurant5HostGui gui) {
		hostGui = gui;
	}

	@Override
	public Restaurant5HostGui getGui() {
		return hostGui;
	}

	public class MyCustomer {
		public Restaurant5Customer c;
		int table;
		CustomerState s;

		MyCustomer(Restaurant5Customer cc, CustomerState sc) {
			c = cc;
			s = sc;
		}

	}

	public class MyWaiter {
		public Restaurant5Waiter w;
		WaiterState ws;
		int numberOfCustomers = 0;
		//boolean onBreak = false;

		MyWaiter(Restaurant5Waiter wc, WaiterState wsc, int num) {
			w = wc;
			ws = wsc;
			numberOfCustomers = num;
		}
	}

	
	public class Table {
		Restaurant5Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Restaurant5Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Restaurant5Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
}


