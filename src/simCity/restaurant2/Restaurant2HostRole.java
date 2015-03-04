package simCity.restaurant2;

import agent.Agent;
import simCity.Role;
import simCity.restaurant2.gui.Restaurant2HostGui;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Host;
import simCity.restaurant2.interfaces.Restaurant2Waiter;

import java.util.*;

/**
 * Restaurant Restaurant2Host Agent
 */

public class Restaurant2HostRole extends Role implements Restaurant2Host {
	
	private String name;
	public Restaurant2HostGui restaurant2HostGui = null;
	List<MyWaiter> myWaiters = Collections.synchronizedList( new ArrayList<MyWaiter>() );
	List<MyCustomer> myCustomers = Collections.synchronizedList( new ArrayList<MyCustomer>() );

	class MyWaiter {
		Restaurant2Waiter w;
		int customersHandled;
		WaiterState state;
		MyWaiter(Restaurant2Waiter w) {
			this.w = w;
			state = WaiterState.Working;
			customersHandled = 0;
		}
	}
	
	enum WaiterState {Working, BreakRequested, OnBreak};
	
	
	class MyCustomer {
		Restaurant2Customer c;
		CustomerState state;
		MyCustomer(Restaurant2Customer c, CustomerState state) {
			this.c = c;
			this.state = state;
		}
	}

	enum CustomerState {Waiting, Served, LeftUnserved};
	
	List<Table> tables;

	private class Table {
		Restaurant2Customer occupiedBy;
		int tableNum;

		Table(int tableNum) {
			this.tableNum = tableNum;
			this.setUnoccupied();
		}

		void setOccupant(Restaurant2Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Restaurant2Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNum;
		}
		
		int getTable() {
			return tableNum;
		}
		
	}

	public Restaurant2HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList( new ArrayList<Table>() );
		
		//HARDCODING ADDING TABLES 
		tables.add(new Table(1));
		tables.add(new Table(2));
		tables.add(new Table(3));
		tables.add(new Table(4));
	}

	@Override
	public void addWaiter(Restaurant2Waiter w) {
		myWaiters.add(new MyWaiter(w));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getMyCustomers()
	 */
	@Override
	public List getMyCustomers() {
		return myCustomers;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getWaiters()
	 */
	@Override
	public List getWaiters() {
		return myWaiters;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#getTables()
	 */
	@Override
	public Collection getTables() {
		return tables;
	}
	
	private Table findTable(int tableNum) {
		synchronized(tables) {
			for (int i = 0; i < tables.size(); i++) {
				if (tableNum == tables.get(i).getTable()) {
					return tables.get(i);
				}
			}
		}
		// this should never happen, but if no matching table is found return null
		return null;
	}
	
	private MyWaiter findWaiter(Restaurant2Waiter w) {
		synchronized(myWaiters) {
			for (MyWaiter waiter : myWaiters) {
				if (w == waiter.w) {
					return waiter;
				}
			}
		}
		// this should never happen, but if no matching waiter is found return null
		return null;
	}
	
	
	
	/**
	 *  Messages
	 */

	// from Restaurant2WaiterRole
	@Override
	public void WantBreak(Restaurant2Waiter w) {
		print("Received WantBreak msg");
		MyWaiter mw = findWaiter(w);
		mw.state = WaiterState.BreakRequested;
		stateChanged();
	}
	
	
	// from Restaurant2CustomerRole
	@Override
	public void IWantFood(Restaurant2Customer c) {
		print("Received IWantFood msg");
		myCustomers.add(new MyCustomer(c, CustomerState.Waiting));
		stateChanged();
	}
	
	
	// from Restaurant2CustomerRole
	@Override
	public void IAmLeaving(Restaurant2Customer c) {
		print("Received IAmLeaving msg");
		for (MyCustomer mc : myCustomers) {
			if (mc.c == c) {
				mc.state = CustomerState.LeftUnserved;
			}
		}
		stateChanged();
	}
	
	// from Restaurant2WaiterRole
	@Override
	public void TableAvailable(Restaurant2Waiter w, int tableNum) {
		print("Received TableAvailable msg");
		MyWaiter mw = findWaiter(w);
		mw.customersHandled--;
		Table table = findTable(tableNum);
		table.setUnoccupied();
		stateChanged();
	}
	
	
	// from Restaurant GUI
	@Override
	public void WaiterOffBreak(Restaurant2Waiter w) {
		print("Received WaiterOffBreak msg");
		MyWaiter mw = findWaiter(w);
		mw.state = WaiterState.Working;
		stateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		// Safety check to make sure there is at least 1 waiter
		synchronized(myWaiters) {
			if (myWaiters.size() == 0) {
				return false;
			}
		}
				
		synchronized(myWaiters) {
			// Check for waiters requesting for break
				for (MyWaiter mw : myWaiters) {
					if (mw.state == WaiterState.BreakRequested) {
						for (MyWaiter mw2 : myWaiters) {
							if (mw2.state == WaiterState.Working) {
								approveBreak(mw);
								return true;
							}
						}
						// if it makes it here, that means no other waiters are Working, deny Break
						denyBreak(mw);
						return true;
					}
				}
		}
		
		// Safety checks to make sure there is at least 1 table, 1 customer
		synchronized(tables) {
			if (tables.size() == 0 || myCustomers.size() == 0) {
				// No tables, or no customers, or no waiters, nothing further to do
				return false;
			}
		}
			
		
		synchronized(tables) {
			/* Think of this next rule as:
	            Does there exist a table and customer,
	            so that table is unoccupied and customer is waiting.
	            If so seat him at the table.
			 */
			
			// Check for empty table
			for (Table table : tables) {
				if (!table.isOccupied()) {
					// Check for waiting customer
					synchronized(myCustomers) {
						for (MyCustomer mc : myCustomers) {
							if (mc.state == CustomerState.Waiting) {
		
								/**
								 *  WAITER SELECTING MECHANISM
								 */
								
								
								
								// Iterate through myWaiters list once to find first customer with state Working
								MyWaiter chosenWaiter = myWaiters.get(0);
								synchronized (myWaiters) {
									for (MyWaiter mw : myWaiters) {
										if (mw.state == WaiterState.Working) {
											// found first waiter who is Working, leave the loop
											chosenWaiter = mw;
											break;
										}
									}
								}
								
								// After table and customer have been selected, select waiter
								// Restaurant2Waiter is selected based on whomever is handling the least
								// customers. If there is a tie, first waiter with lowest
								// # of cust handled on list gets picked
								
								// There must be at least one working Restaurant2Waiter picked initially before going through this
								if (chosenWaiter.state == WaiterState.Working) {
									synchronized (myWaiters) {
										for (MyWaiter mw : myWaiters) {
											if (mw.state == WaiterState.Working) {
												if (mw.customersHandled < chosenWaiter.customersHandled) {
													chosenWaiter = mw;
												}
											}
										}
									}
									// Now, after iterating through all the waiters, we have the waiter
									// handling the fewest number of customers in our chosenWaiter variable
									// assign customer to waiter chosenWaiter.
									seatCustomer(chosenWaiter, mc, table);
									return true;
								}
							}
						}
					}
				}
			}
		}
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	
	
	/**
	 *  Actions
	 */
	
	private void approveBreak(MyWaiter mw) {
		print("Break approved for " + mw.w.getName());
		mw.state = WaiterState.OnBreak;
		mw.w.BreakApproved();
	}
	
	private void denyBreak(MyWaiter mw) {
		print("Break denied for " + mw.w.getName());
		mw.state = WaiterState.Working;
		mw.w.BreakDenied();
	}
	
	private void seatCustomer(MyWaiter mw, MyCustomer mc, Table t) {
		print("Seating customer");
		
		mc.state = CustomerState.Served;
		t.setOccupant(mc.c);
		mw.w.PleaseSeatCustomer(this, mc.c, t.getTable());
		mw.customersHandled++;
	}


	//utilities

	/* (non-Javadoc)
	 * @see restaurant.Host#setGui(restaurant.gui.HostGui)
	 */
	@Override
	public void setGui(Restaurant2HostGui gui) {
		restaurant2HostGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getGui()
	 */
	@Override
	public Restaurant2HostGui getGui() {
		return restaurant2HostGui;
	}

}

