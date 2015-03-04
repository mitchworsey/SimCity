package simCity.Restaurant1;

import simCity.Role;
//import simCity.Restaurant1Host.Table;
//import simCity.gui.Restaurant1WaiterGui;
import simCity.gui.Restaurant1.Restaurant1WaiterGui;
import simCity.interfaces.Restaurant1Cashier;
import simCity.interfaces.Restaurant1Cook;
import simCity.interfaces.Restaurant1Customer;
import simCity.interfaces.Restaurant1Host;
import simCity.interfaces.Restaurant1Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */

public abstract class Restaurant1WaiterRole extends Role implements Restaurant1Waiter {
	static final int NTABLES = 4;
	public static List<Table> tables;
	
	//Customer lists
	public List<Restaurant1Customer> waitingCustomers = new ArrayList<Restaurant1Customer>();
	public List<Restaurant1Customer> customerReadyToOrder = new ArrayList<Restaurant1Customer>();
	public List<Restaurant1Customer> customerReOrder = new ArrayList<Restaurant1Customer>();
	public List<Restaurant1Customer> allMyCustomers = new ArrayList<Restaurant1Customer>();
	
	//Bills
	public List<Bills> customerReadyForCheck = new ArrayList<Bills>();
	public List<Bills> customerNeedsToPay = new ArrayList<Bills>();
	
	//Order
	public List<Order> deliverOrders = new ArrayList<Order>();
	public List<Order> orderToCook = new ArrayList<Order>();
	public Order newOrder = new Order();
	public Order orderReady = new Order();
	public Order foodDelivered = new Order();
	
	//Booleans
	public int tableNumber = -1;
	public int gettingMoney = 0;
	public boolean gotOrder = false;
	boolean atStart = true;
	boolean atCook = false;
    int atThisTable = -1;
    int foodBeingDelivered = 0;
    boolean gettingOrder = false;
    boolean busy = false;

	public enum AgentEvent
	{none, wantsBreak, goOnBreak, noBreak, backToWork};
	AgentEvent event = AgentEvent.none;

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public Restaurant1WaiterGui Restaurant1WaiterGui = null;
	
	// agent correspondents
	private Restaurant1Host host;
	protected Restaurant1Cook cook;
	private Restaurant1Cashier cashier;
	
	
	public Restaurant1WaiterRole(String name) {
		super();
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		this.name = name;
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
	
	public static int getNumTables() {
		return NTABLES;
	}
	// Messages
	
	public void msgSetHost(Restaurant1Host hos) {
		this.host = hos;
	}
	
	public void msgSetCashier(Restaurant1Cashier cash) {
		this.cashier = cash;
	}
	
	public void msgSetCook(Restaurant1Cook cook) {
		this.cook = cook;
	}
	
	//Messages from Host
	public void msgUpdateTables(Restaurant1Customer customer, int tableNumber) {
		for (Table table : tables) {
			if (table.getNumber() == tableNumber)
				table.setOccupant(customer);
		}
	}
	
	public void msgUpdateTablesNull(int tableNumber) {
		for (Table table : tables) {
			if (table.getNumber() == tableNumber)
				table.setUnoccupied();
		}
	}
	
	public void wantsBreak() {  //from GUI
		print("I want a break");
		event = AgentEvent.wantsBreak;
		stateChanged();
	}
	
	public void backToWork() {
		event = AgentEvent.backToWork;
		stateChanged();
	}
	
	public void msgGoOnBreak() {
		event = AgentEvent.goOnBreak;
		stateChanged();
	}
	
	public void msgNoBreak() {
		event = AgentEvent.noBreak;
		stateChanged();
	}

	public void msgSeatCustomer(Restaurant1Customer customer, int table) { 
		System.out.println("Got seat customer message");
		waitingCustomers.add(customer);
	    tableNumber = table;
	    allMyCustomers.add(customer);
		stateChanged();
	}

	public void msgAtTable(int tableNumber) {//from GUI
		//atTable.release();
		atThisTable = tableNumber;
		atStart = false;
		stateChanged();
	}
	
	public void msgAtStart() {//from GUI
		atStart = true;
		busy = false;
		host.msgAtStart(this);
		stateChanged();
	}
	
	public void msgAtCook() {
		atCook = true;
		stateChanged();
	}
	
	//Messages from Customer

	public void msgReadyToOrder(String customerName) {
		for (Restaurant1Customer customer : allMyCustomers) {
			if (customer.getCustomerName() == customerName)
				customerReadyToOrder.add(customer);
		}
		stateChanged();
	}
	
	public void msgHereIsMyChoice(String customerName, String food) {
		gotOrder = true;
		newOrder.setFood(food);
		for (Restaurant1Customer customer : allMyCustomers) {
			if (customer.getCustomerName() == customerName)
				newOrder.setCustomer(customer);
		}
		newOrder.setWaiter(this);
		stateChanged();
	}
		
	public void msgReadyForCheck(String customerName, String order) {
		Bills bill = new Bills();
		bill.setOrder(order);
		for (Restaurant1Customer customer : allMyCustomers) {
			if (customer.getCustomerName() == customerName)
				bill.setCustomer(customer);
		}
		customerReadyForCheck.add(bill);
		stateChanged();
	}
	
	public void msgLeavingTable(String customerName) {
		for (int i = 0; i < allMyCustomers.size(); i++) {
			if (allMyCustomers.get(i).getCustomerName() == customerName)
				allMyCustomers.remove(allMyCustomers.get(i));
		}
		stateChanged();
	}
	
	//Messages from Cook
	public void msgNotEnoughFood(Restaurant1Customer customer){
		customerReOrder.add(customer);
		stateChanged();
	}
	
	public void msgOrderIsReady(Restaurant1Customer cust, String food) {
		Order nextOrder = new Order();
		nextOrder.setCustomer(cust);
		nextOrder.setPreperationTime(food);
		deliverOrders.add(nextOrder);
		stateChanged();
	}
	
	//Messages from Cashier
	public void msgCustomerPaid(String customerName) {
		for (Restaurant1Customer customer : allMyCustomers) {
			if (customer.getCustomerName() == customerName) {
				for (int i = 0; i < customerNeedsToPay.size(); i++){
					if (customerNeedsToPay.get(i).getCustomer().getCustomerName() == customerName)
						customerNeedsToPay.remove(customerNeedsToPay.get(i));
				}
			}
		}
		stateChanged();
	}
	
	public void msgCustomerHasDebt(String customerName, String order) {
		for (Restaurant1Customer customer : allMyCustomers) {
			if (customer.getCustomerName() == customerName) {
				for (int i = 0; i < customerNeedsToPay.size(); i++){
					if (customerNeedsToPay.get(i).getCustomer().getCustomerName() == customerName)
						customerNeedsToPay.remove(customerNeedsToPay.get(i));
				}
			}
		}
		double amount = 0;
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
		for (Restaurant1Customer customer : allMyCustomers) {
			if (customer.getCustomerName() == customerName) {
						Bills nextCustomer = new Bills(customer, amount);
						host.msgCustomerWithDebt(customer.getCustomerName(), order);
			}
		}

		stateChanged();
	}
	
	public void msgCustomerOwesMoney(double amount, String customerName) {
		Bills nextCustomer = new Bills();
		nextCustomer.setAmount(amount);
		for (Restaurant1Customer customer : allMyCustomers) {
			if (customer.getCustomerName() == customerName)
				nextCustomer.setCustomer(customer);
		}
		customerNeedsToPay.add(nextCustomer);
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try{
		if (event == AgentEvent.wantsBreak){
			event = AgentEvent.none;
			askForBreak();
			return true;
		}
		if (event == AgentEvent.backToWork) {
			backAtWork();
			event = AgentEvent.none;
			return true;
		}
		if (event == AgentEvent.goOnBreak){
			goOnBreak();
			return true;
		}
		if (event == AgentEvent.noBreak){
			noBreak();
			return true;
		}
		if (event == AgentEvent.none){ 
		if (atCook){
			if (!orderToCook.isEmpty()) {
				orderCook(orderToCook.get(0));
				goToStart();
				return true;
			}
			else if(!deliverOrders.isEmpty()) {
				deliverOrder(deliverOrders.get(0));
				return true;
			}
		}
		else if (atStart){
			if (!customerReadyToOrder.isEmpty() && !gettingOrder) {
				gettingOrder = true;
				getOrder(customerReadyToOrder.get(0));
				return true;
			}
			else if (!customerReOrder.isEmpty() && !gettingOrder) {
				gettingOrder = true;
				getReOrder(customerReOrder.get(0));
				return true;
			}
			else if(!deliverOrders.isEmpty()) {
				//deliverOrder(deliverOrders.get(0));
				toCook();
				return true;
			}
			else if (!waitingCustomers.isEmpty()) {
				System.out.println("Waiting customers is not empty");
				for (Table table : tables) {
					if(table.getNumber() == tableNumber){
						table.setUnoccupied();
    				    if(!table.isOccupied()) {
    				    	if(!gettingOrder && atStart && !busy){
	    			        	seatCustomer(waitingCustomers.get(0), table);
		    			        return true;//return true to the abstract agent to reinvoke the scheduler.
    				    	}
			    	    }
					}
			    }
			}
			else if (!customerReadyForCheck.isEmpty()) {
				getBill(customerReadyForCheck.get(0));
				return true;
			}
			else if (!customerNeedsToPay.isEmpty()) {
				getMoney(customerNeedsToPay.get(0));
				return true;
			}
		}
		else {
			if (atThisTable != 0) {
				if (foodBeingDelivered == atThisTable && foodDelivered != null) 
					handOffFood();
				else if (gettingMoney == atThisTable) {
					if(!customerNeedsToPay.isEmpty())
						takeMoney(customerNeedsToPay.get(0));
				}
				else if (gettingOrder)
					toCook();
				else
					takeOrder();
		    }
			return true;
		}
		}
		}
		catch (ConcurrentModificationException cme) {
			return false;
		}
		return false;
	}

	// Actions

	private void goToStart() {
		Restaurant1WaiterGui.DoLeaveCustomer();
		atCook = false;
	}
	
	private void askForBreak() {
		host.msgAskForBreak(this);
	}
	
	private void backAtWork() {
		Restaurant1WaiterGui.setReturn();
		host.msgBackAtWork(this);
	}
	
	private void goOnBreak() {
		Restaurant1WaiterGui.getBreak();
	}

	private void noBreak(){
		Restaurant1WaiterGui.setReturn();
		event = AgentEvent.none;
	}
	
	private void seatCustomer(Restaurant1Customer restaurant1Customer, Table table) {
		atStart = false;
		restaurant1Customer.msgSitAtTable(table.getNumber());
		DoSeatCustomer(restaurant1Customer, table);
		for (Table tabless : tables) {
			if (tabless.getNumber() == table.getNumber()) {
				tabless.setOccupant(restaurant1Customer);
			}
		}
		waitingCustomers.remove(restaurant1Customer);
		atThisTable = 0;
	}
	
	private void getOrder(Restaurant1Customer restaurant1Customer) {
		atStart = false;
		for (Table table : tables) {
			if (table.getOccupant() == restaurant1Customer) {
				DoGetOrder(table);
				restaurant1Customer.msgWhatWouldYouLike();
				customerReadyToOrder.remove(restaurant1Customer);
			}
		}
	}
	
	private void getReOrder(Restaurant1Customer restaurant1Customer) {
		atStart = false;
		for (Table table : tables) {
			if (table.getOccupant() == restaurant1Customer) {
				DoGetOrder(table);
				restaurant1Customer.msgReOrder();
				customerReOrder.remove(restaurant1Customer);
			}
		}
	}
	
	private void toCook() {
		if (gettingOrder)
			orderToCook.add(newOrder);
		gettingOrder = false;
		atStart = false;
		Restaurant1WaiterGui.DoSeeCook();
		atThisTable = 0;
	}
	
	private void takeOrder() {
		Restaurant1WaiterGui.DoLeaveCustomer();
		atThisTable = 0;
	}
	
	protected abstract void orderCook(Order order);
	
	private void deliverOrder(Order order) {
		atStart = false;
		atCook = false;
		deliverOrders.remove(order);
		cook.msgGotOrder(order.getFood());
		DoBringFoodToTable(order.getSendTo(), order.getFood());
		for (Table table : tables) {
			if (table.getOccupant() == order.getSendTo())
			    foodBeingDelivered = table.getNumber();
		}
		foodDelivered = order;
	}
	
	private void handOffFood() {
		(foodDelivered.getSendTo()).msgHereIsYourFood();
		foodBeingDelivered = 0;
	}
	
	private void getBill(Bills bill) {
		cashier.msgHereIsAnOrder(bill.getCustomer().getCustomerName(), bill.getAmount(), this);
		customerReadyForCheck.remove(bill);
	}
	
	private void getMoney(Bills bill) {
		atStart = false;
		
		for (Table table : tables) {
			if (table.getOccupant() == bill.getCustomer()) {
				DoGiveCheck(table);
				gettingMoney = table.getNumber();
				//(bill.getCustomer()).msgHereIsYourCheck(bill.getAmount());
			}
		}
	}
	
	private void takeMoney(Bills bill) {
		customerNeedsToPay.remove(customerNeedsToPay.get(0));
		gettingMoney = 0;
		(bill.getCustomer()).msgHereIsYourCheck(bill.getAmount());
		Restaurant1WaiterGui.DoLeaveCustomer();
		atThisTable = 0;
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(Restaurant1Customer restaurant1Customer, Table table) {
		System.out.println("telling to bring to table");
		Restaurant1WaiterGui.DoBringToTable(table.getNumber()); 
	}
	
	private void DoGetOrder(Table table) {
		Restaurant1WaiterGui.DoGetOrder(table.getNumber());
	}
	
	private void DoBringFoodToTable(Restaurant1Customer restaurant1Customer, String food) {
	    for (Table table : tables) {
			if (table.getOccupant() == restaurant1Customer) {
				Restaurant1WaiterGui.DoBringFoodToTable(table.getNumber(), food);
			}
		}
	}
	
	private void DoGiveCheck(Table table) {
		Restaurant1WaiterGui.DoGiveCheck(table.getNumber());
	}

	//utilities

	public void setGui(Restaurant1WaiterGui gui) {
		Restaurant1WaiterGui = gui;
	}

	public Restaurant1WaiterGui getGui() {
		return Restaurant1WaiterGui;
	}
	
	public class Table {
		Restaurant1Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		

		void setOccupant(Restaurant1Customer restaurant1Customer) {
			occupiedBy = restaurant1Customer;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Restaurant1Customer getOccupant() {
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
	
	public class Order {
		Restaurant1Customer sendTo;
		String food = new String();
		Restaurant1Waiter waiter;
		int preperationTime;
		
		Order() {
			
		}
		
		void setPreperationTime(String order) {
			food = order;
			if(order.equals("pizza")) {
				preperationTime = 3;
			}
			if(order.equals("steak")) {
				preperationTime = 10;
			}
			if(order.equals("salad")) {
				preperationTime = 7;
			}
			if(order.equals("chicken")) {
				preperationTime = 8;
			}
		}
		
		int getPreperationTime() {
			return preperationTime;
		}
		
		String getFood() {
			return food;
		}
		
		Restaurant1Customer getSendTo() {
			return sendTo;
		}
		
		Restaurant1Waiter getWaiter() {
			return waiter;
		}
		
		void setWaiter(Restaurant1Waiter wait) {
			waiter = wait;
		}
		
		void setCustomer(Restaurant1Customer cust) {
			sendTo = cust;
		}
		
		void setFood(String foo) {
			food = foo;
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
		
		void setOrder(String order){
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
		
		void setCustomer(Restaurant1Customer customer2) {
			customer = customer2;
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
