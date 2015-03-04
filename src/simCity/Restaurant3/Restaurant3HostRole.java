package simCity.Restaurant3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;
import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.Restaurant3.interfaces.Restaurant3Host;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;

public class Restaurant3HostRole extends Role implements Restaurant3Host{
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public List<Restaurant3Waiter> waiters = Collections.synchronizedList(new ArrayList<Restaurant3Waiter>());
	
	public List<Restaurant3Waiter> waitersToRemove = new ArrayList<Restaurant3Waiter>();
	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	public List<MyCustomer> customersToRemove = new ArrayList<MyCustomer>();
	
	int waiterNum = 0;
	
	private boolean justOpened = false;
	
	public enum CustomerState
	{waitingToBeSeated, mayLeave, seated, leftRestaurant};

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public Restaurant3WaiterGui waiterGui = null;
	//public HostGui hostGui = null;
	
	public Restaurant3HostRole(String name) {
		super();

		//waiter = new WaiterAgent("Waiter 1");
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		justOpened = false;
	}


	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}


	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#getCustomers()
	 */
	@Override
	public List getCustomers() {
		return customers;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#getTables()
	 */
	@Override
	public Collection getTables() {
		return tables;
	}
	

	@Override
	public void setWaiter(Restaurant3Waiter w){
		waiters.add(w);
	}
	// Messages

	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#msgIWantFood(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public void msgIWantFood(Restaurant3Customer cust) {
		print("Received msgIWantFood");
		customers.add(new MyCustomer(cust, CustomerState.waitingToBeSeated));
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#msgLeavingImpatiently(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public void msgLeavingImpatiently(Restaurant3Customer cust){
		print("Received msgLeavingImpatiently");
		for(MyCustomer mc: customers){
			if(mc.c == cust)
				customersToRemove.add(mc);
		}
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#msgTableIsFree(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public void msgTableIsFree(Restaurant3Customer cust){
		print("Received msgTableIsFree");
		for(MyCustomer mc: customers){
			if(mc.c == cust)
				customersToRemove.add(mc);
		}
		for(Table t: tables){
			if(t.getOccupant() == cust){
				t.setUnoccupied();
				//t.occupiedBy.cs = CustomerState.leftRestaurant;	
			}
		}
		stateChanged();
	}
	

	@Override
	public void msgAskToGoOnBreak(Restaurant3Waiter w){
		print("Received msgAskToGoOnBreak");
		//if(waiters.size()>1){
		//for(WaiterAgent waiter: waiters){
			//if(waiter == w)
				//waiter.wantToGoOnBreak = true;
		//}
			stateChanged();
		//}
	}
	

	@Override
	public void msgGoingOnBreak(Restaurant3Waiter w){
		waitersToRemove.add(w);
		stateChanged();
	}
	

	@Override
	public void msgBackToWork(Restaurant3Waiter w){
		waiters.add(w);
		stateChanged();
	}


	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#msgAtTable()
	 */
	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Host#readyToBeSeated()
	 */
	@Override
	public void readyToBeSeated(){
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(justOpened){
			if(waiters.size()>0){
				restockInventory(waiters.get(0));
				return true;
			}
		}
		
		if(!customersToRemove.isEmpty()){
			for(MyCustomer mc: customersToRemove){
				customers.remove(mc);
			}
			customersToRemove.clear();
		}
		
		if(!waitersToRemove.isEmpty()){
			for(Restaurant3Waiter w: waitersToRemove){
				waiters.remove(w);
			}
			waitersToRemove.clear();
		}
		
		if(waiters.size()>1){
			for(Restaurant3Waiter w: waiters){
				if(w.wantToGoOnBreak()){
					tellWaiterOKToGoOnBreak(w);
					return true;
				}
			}
		}
		else{
			for(Restaurant3Waiter w: waiters){
				if(w.wantToGoOnBreak()){
					tellWaiterNOTOKToGoOnBreak(w);
					return true;
				}
			}
		}
		synchronized(customers){
			for(int x = 0; x <= waiters.size(); x++){
				if(x == (waiterNum % waiters.size())){
					for(MyCustomer mc: customers){
						if(mc.cs == CustomerState.waitingToBeSeated){
							for (Table table : tables) {
								if (!table.isOccupied()) {
									tellWaiterToSeatCustomer(waiters.get(x), mc, table);//the action
									waiterNum++;
									return true;//return true to the abstract agent to reinvoke the scheduler.
								}
							}
							if(mc.c.isImpatient()){
								tellCustomerRestaurantFull(mc);
								return true;
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

	// Actions
	
	private void restockInventory(Restaurant3Waiter w){
		print("Called restockInventory");
		w.msgRestockInventory();
		justOpened = false;
	}

	private void tellWaiterToSeatCustomer(Restaurant3Waiter w, MyCustomer mc, Table table){
		print("Called tellWaiterToSeatCustomer"); 
		//for(WaiterAgent w: waiters){
			//if(!w.isBusy()){
		mc.c.setWaiter(w);
		w.msgSitAtTable(mc.c, table.tableNumber);
			//}
		//}
		table.setOccupant(mc.c);
		//customers.remove(mc);
		mc.cs = CustomerState.seated;
		//waiterGui.DoLeaveCustomer();
	}
	
	private void tellCustomerRestaurantFull(MyCustomer mc){
		print("Called tellCustomerRestaurantFull");
		mc.c.msgRestaurantFull();
		mc.cs = CustomerState.mayLeave;
	}
	
	private void tellWaiterOKToGoOnBreak(Restaurant3Waiter w){
		print("Called tellWaiterOKToGoOnBreak");
		w.msgOKToGoOnBreak();
	}
	private void tellWaiterNOTOKToGoOnBreak(Restaurant3Waiter w){
		print("Called tellWaiterNOTOKToGoOnBreak");
		w.msgNOTOKToGoOnBreak();
	}
	

	//utilities


	@Override
	public void setGui(Restaurant3WaiterGui gui) {
		waiterGui = gui;
	}


	@Override
	public Restaurant3WaiterGui getGui() {
		return waiterGui;
	}

	private class Table {
		Restaurant3Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Restaurant3Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Restaurant3Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	public class MyCustomer{
		Restaurant3Customer c;
		CustomerState cs;
		
		MyCustomer(Restaurant3Customer cust, CustomerState custstate){
			c = cust;
			cs = custstate;
		}
	}

	public void addWaiters(Restaurant3WaiterRole w) {
		// TODO Auto-generated method stub
		waiters.add(w);
	}

}
