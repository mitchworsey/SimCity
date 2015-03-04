package simCity.Restaurant3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;
import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.Restaurant3.interfaces.Restaurant3Host;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;

public abstract class Restaurant3WaiterRole extends Role implements Restaurant3Waiter{

	public enum CustomerState
	{waiting, seated, readyToOrder, askedToOrder, ordered, waitingForFoodToCook, waitingForFoodToArrive, needsToReOrder, eating,
		doneEating, waitingForCheckToBeProduced, waitingForCheckToArrive, receivedCheck, leaving};
	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	public List<Restaurant3Check> checks = Collections.synchronizedList(new ArrayList<Restaurant3Check>());
	public List<Restaurant3Check> checksToRemove = new ArrayList<Restaurant3Check>();
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	protected Semaphore atCook = new Semaphore(0, true);
	private Semaphore atHost = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	
	boolean wantToGoOnBreak = false;
	boolean OKToGoOnBreak = false;
	boolean onBreak = false;
	boolean backToWork = false;
	
	public Restaurant3WaiterGui waiterGui = null;
	public Restaurant3Cook cook = null;
	public Restaurant3Host host = null;
	public Restaurant3Cashier cashier = null;
	
	private boolean justOpened = false;
	private boolean begInventoryFilled = true;
	
	RestaurantMenu rm = new RestaurantMenu();
	
	public Restaurant3WaiterRole(String name) {
		super();
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#getCustomers()
	 */
	@Override
	public List getCustomers() {
		return customers;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#wantToGoOnBreak()
	 */
	@Override
	public boolean wantToGoOnBreak(){
		return wantToGoOnBreak;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#isOnBreak()
	 */
	@Override
	public boolean isOnBreak(){
		return onBreak;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#isOKToGoOnBreak()
	 */
	@Override
	public boolean isOKToGoOnBreak(){
		return OKToGoOnBreak;
	}
	
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#setCook(simCity.Restaurant3.Restaurant3Cook)
	 */
	@Override
	public void setCook(Restaurant3Cook cook){
		this.cook = cook;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#setHost(simCity.Restaurant3.Restaurant3Host)
	 */
	@Override
	public void setHost(Restaurant3Host host){
		this.host = host;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#setCashier(simCity.Restaurant3.Restaurant3Cashier)
	 */
	@Override
	public void setCashier(Restaurant3Cashier cashier){
		this.cashier = cashier;
	}
	
	
	// Messages
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgBegInventoryFilled()
	 */
	@Override
	public void msgBegInventoryFilled(){
		print("Beginning Inventory filled, now can start seeting customers.");
		begInventoryFilled = true;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgAddToMenu(java.lang.String)
	 */
	@Override
	public void msgAddToMenu(String food){
		print("received msgAddToMenu");
		if(!rm.entrees.containsKey(food))
			rm.add(food);
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgRestockInventory()
	 */
	@Override
	public void msgRestockInventory(){
		print("Received msgRestockInventory");
		justOpened = true;
		begInventoryFilled = false;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgCheckIsReady(simCity.Restaurant3.Restaurant3Check)
	 */
	@Override
	public void msgCheckIsReady(Restaurant3Check c){
		print("Received msgCheckIsReady");
		checks.add(c);
		MyCustomer mc = find(c.c);
		mc.cs = CustomerState.waitingForCheckToArrive;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgWantsToGoOnBreak()
	 */
	@Override
	public void msgWantsToGoOnBreak(){
		print("Received msgWantsToGoOnBreak");
		wantToGoOnBreak = true;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgBackToWork()
	 */
	@Override
	public void msgBackToWork(){
		backToWork = true;
		OKToGoOnBreak = false;
		onBreak = false;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgOKToGoOnBreak()
	 */
	@Override
	public void msgOKToGoOnBreak(){
		print("Received msgOKToGoOnBreak");
		wantToGoOnBreak = false;
		OKToGoOnBreak = true;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgNOTOKToGoOnBreak()
	 */
	@Override
	public void msgNOTOKToGoOnBreak(){
		print("Received msgNOTOKToGoOnBreak");
		wantToGoOnBreak = false;
		OKToGoOnBreak = false;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgSitAtTable(simCity.Restaurant3.Restaurant3Customer, int)
	 */
	@Override
	public void msgSitAtTable(Restaurant3Customer c, int table){
		print("Received msgSitAtTable()");
		customers.add(new MyCustomer(c, table, CustomerState.waiting));
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgReadyToOrder(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public void msgReadyToOrder(Restaurant3Customer c){
		print("Received msgReadyToOrder()");
		MyCustomer mc = find(c);
		mc.cs = CustomerState.readyToOrder;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgHereIsMyChoice(simCity.Restaurant3.Restaurant3Customer, java.lang.String)
	 */
	@Override
	public void msgHereIsMyChoice(Restaurant3Customer c, String choice){
		print("Received msgHereIsMyChoice()");
		MyCustomer mc = find(c);
		mc.cs = CustomerState.ordered;
		mc.choice = choice;
		//waiterGui.createFoodGui(choice, mc.table);
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgOutOfFood(java.lang.String, int)
	 */
	@Override
	public void msgOutOfFood(String choice, int table){
		print("Received msgOutOfFood()");
		for(MyCustomer myCust: customers){
			if(myCust.choice == choice && myCust.table == table)
				myCust.cs = CustomerState.needsToReOrder;
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgOrderIsReady(java.lang.String, int)
	 */
	@Override
	public void msgOrderIsReady(String choice, int table){
		print("Received msgOrderIsReady()");
		for(MyCustomer myCust: customers){
			if(myCust.choice == choice && myCust.table == table)
				myCust.cs = CustomerState.waitingForFoodToArrive;
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgReadyForCheck(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public void msgReadyForCheck(Restaurant3Customer c){
		print("Received msgReadyForCheck");
		MyCustomer mc = find(c);
		mc.cs = CustomerState.doneEating;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgDonePayingAndLeaving(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public void msgDonePayingAndLeaving(Restaurant3Customer c){
		print("Received msgDonePayingAndLeaving()");
		//isAvailable = true;
		MyCustomer mc = find(c);
		mc.cs = CustomerState.leaving;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgAtTable()
	 */
	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgAtCook()
	 */
	@Override
	public void msgAtCook(){
		//print("msgAtCook() called");
		atCook.release();
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgAtHost()
	 */
	@Override
	public void msgAtHost(){
		atHost.release();
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#msgAtCashier()
	 */
	@Override
	public void msgAtCashier(){
		atCashier.release();
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
		if(wantToGoOnBreak)
			askToGoOnBreak();
		if(justOpened && cook!=null){
			restockInventory();
			return true;
		}
		
		if(!checksToRemove.isEmpty()){
			for(Restaurant3Check c: checksToRemove){
				checks.remove(c);
			}
			checksToRemove.clear();
		}
		
		if(begInventoryFilled){
			synchronized(customers){
				for(MyCustomer mc: customers){
					if(mc.cs == CustomerState.waiting){
						seatCustomer(mc);
						return true;
					}
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.readyToOrder){	
					takeOrder(mc);
					return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.ordered){
					deliverOrderToCook(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.needsToReOrder){
					reOrder(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.waitingForFoodToArrive){
					deliverOrderToCustomer(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.doneEating){
					tellCashierToProduceCheck(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.waitingForCheckToArrive){
					synchronized(checks){
						for(Restaurant3Check c: checks){
							if(c.c == mc.c){
								deliverCheckToCustomer(mc, c);//the action
								return true;//return true to the abstract agent to reinvoke the scheduler.
							}
						}
					}
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.leaving){
					alertHostTableIsFree(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		waiterGui.DoWatch();
		if(customers.isEmpty() && OKToGoOnBreak)
			goOnBreak();
		if(onBreak)
			waiterGui.DoLeaveCustomer();
		else if(backToWork){
			tellHostYourBack();
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void restockInventory(){
		cook.msgRestockInventory(this);
		justOpened = false;
	}

	private void askToGoOnBreak(){
		host.msgAskToGoOnBreak(this);
	}
	
	private void seatCustomer(MyCustomer mc){
		print("Called seatCustomer()");
		print("x = " +mc.c.getGui().getXWaitPos() + "  y = " + mc.c.getGui().getYWaitPos());
		waiterGui.DoGoToCustomer(mc.c.getGui().getXWaitPos()+20, mc.c.getGui().getYWaitPos()+20);
		try {
			atHost.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mc.c.msgFollowMeToTable(rm);
		waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mc.cs = CustomerState.seated;
		//if(name.equals("go on break")){
			//wantToGoOnBreak = true;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void goOnBreak(){
		print("Called goOnBreak");
		wantToGoOnBreak = false;
		OKToGoOnBreak = false;
		onBreak = true;
		host.msgGoingOnBreak(this);
	}
	
	
	private void tellHostYourBack(){
		backToWork = false;
		host.msgBackToWork(this);
	}
	
	private void takeOrder(MyCustomer mc){
		print("Called takeOrder()");
		/*waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		mc.c.msgWhatDoYouWant();
		mc.cs = CustomerState.askedToOrder;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	protected abstract void deliverOrderToCook(MyCustomer mc);
	
	private void reOrder(MyCustomer mc){
		print("called reOrder()");
		waiterGui.removeFoodGui(mc.choice, mc.table);
		waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rm.removeEntree(mc.choice);
		System.out.println("Removed " + mc.choice);
		//System.out.println(rm.entrees.containsKey(mc.choice));
		mc.c.msgPleaseReOrder(rm);
		mc.cs = CustomerState.askedToOrder;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void deliverOrderToCustomer(MyCustomer mc){
		print("Called deliverOrderToCustomer()");
		
		waiterGui.foodCooked(mc.choice, mc.table);
		waiterGui.DoGoToPlatingArea();
		waiterGui.FoodGuiGoToPlatingArea(mc.choice, mc.table);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		waiterGui.DoGoToTable(mc.table);
		waiterGui.FoodGuiGoToCustomer(mc.choice, mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Here7");
		mc.c.msgHereIsYourFood();
		mc.cs = CustomerState.eating;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void tellCashierToProduceCheck(MyCustomer mc){
		print("called tellCashierToProduceCheck");
		waiterGui.foodAte(mc.choice, mc.table);
		waiterGui.DoGoToCashier();//HACK to Cashier
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cashier.msgProduceCheck(this, mc.c, mc.choice);
		mc.cs = CustomerState.waitingForCheckToBeProduced;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void deliverCheckToCustomer(MyCustomer mc, Restaurant3Check check){
		print("Called deliverCheckToCustomer()");
		/*waiterGui.DoGoToCashier();//HACK to Cashier
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mc.c.msgHereIsYourCheck(check);
		checksToRemove.add(check);
		mc.cs = CustomerState.receivedCheck;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void alertHostTableIsFree(MyCustomer mc){
		print("Called alertHostTableIsFree()");
		waiterGui.removeFoodGui(mc.choice, mc.table);
		/*waiterGui.DoGoToHost();
		try {
			atHost.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		host.msgTableIsFree(mc.c);
		customers.remove(mc);
	}

	//utilities
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#getTableNumber(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public int getTableNumber(Restaurant3Customer c){
		for(MyCustomer myCust: customers){
			if(myCust.c == c)
				return myCust.table;		
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Waiter#find(simCity.Restaurant3.Restaurant3Customer)
	 */
	@Override
	public MyCustomer find(Restaurant3Customer c){
		for(MyCustomer myCust: customers){
			if(myCust.c == c)
				return myCust;		
		}
		return null;
	}


	@Override
	public void setGui(Restaurant3WaiterGui gui) {
		waiterGui = gui;
	}


	@Override
	public Restaurant3WaiterGui getGui() {
		return waiterGui;
	}

	public class MyCustomer{
		Restaurant3Customer c;
		int table;
		CustomerState cs;
		String choice;
		
		MyCustomer(Restaurant3Customer cust, int table, CustomerState custstate){
			c = cust;
			this.table = table;
			cs = custstate;
		}
	}
	public class RestaurantMenu{
		Map<String, Food> entrees = new HashMap<String, Food>();
		
		RestaurantMenu(){
			entrees.put("Chicken", new Food("Chicken"));
			entrees.put("Steak", new Food("Steak"));
			entrees.put("Salad", new Food("Salad"));
			entrees.put("Pizza", new Food("Pizza"));
		}
		
		public void removeEntree(String choice){
			entrees.remove(choice);
		}
		
		public void add(String choice){
			entrees.put(choice, new Food(choice));
		}
		
		
		public String getEntree(double cash){
			Random rand = new Random();
			String choice = null;
			while(choice == null){
				if(cash < 5.99)
					return null;
				int num = rand.nextInt(4);
				if(num==0 && entrees.containsKey("Chicken")){
					if(cash >= entrees.get("Chicken").getPrice())
						choice = "Chicken";
				}
				else if(num==1 && entrees.containsKey("Steak")){
					if(cash >= entrees.get("Steak").getPrice())
						choice = "Steak";
				}
				else if(num==2 && entrees.containsKey("Salad")){
					if(cash >= entrees.get("Salad").getPrice())
						choice = "Salad";
				}
				else if(num==3 && entrees.containsKey("Pizza")){
					if(cash >= entrees.get("Pizza").getPrice())
						choice = "Pizza";
				}
				if((cash >= 5.99 && cash < 8.99) && !entrees.containsKey("Salad"))
					return null;
				if(entrees.isEmpty()){
					System.out.println("empty menu");
					return null;
				}
			}
			return choice;
		}
		
	}
	private class Food{
		String type;
		double price;
		
		Food(String type){
			this.type = type;
			if(type == "Chicken"){
				price = 10.99;
			}
			else if(type == "Steak"){
				price = 15.99;
			}
			else if(type == "Salad"){
				price = 5.99;
			}
			else if(type == "Pizza"){
				price = 8.99;
			}
		}
		
		public double getPrice(){
			return price;
		}
	}
}
