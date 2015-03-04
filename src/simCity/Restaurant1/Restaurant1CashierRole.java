package simCity.Restaurant1;

import simCity.Role;
//import restaurant.gui.CashierGui;
import simCity.interfaces.Restaurant1Cashier;
import simCity.interfaces.Restaurant1Customer;
//import simCity.interfaces.Restaurant1Market;
import simCity.interfaces.Restaurant1Waiter;
import simCity.test.mock.EventLog;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cashier Agent
 */

public class Restaurant1CashierRole extends Role implements Restaurant1Cashier {
 
	private String name;
	//public CashierGui cashierGui = null;
	public List<Bills> bills = Collections.synchronizedList(new ArrayList<Bills>());
	public List<Outstanding> customersWithDebt = Collections.synchronizedList(new ArrayList<Outstanding>());
	public List<Restaurant1Customer> receivedMoneyFrom = Collections.synchronizedList(new ArrayList<Restaurant1Customer>());
	public List<Restaurant1Waiter> allMyWaiters = Collections.synchronizedList(new ArrayList<Restaurant1Waiter>());
	private EventLog log;
	public double restaurantMoney = 1000.0;
	private boolean alertWaiter = false;

	public Restaurant1CashierRole(String name) {
		super();
		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void addWaiters(Restaurant1Waiter wait) {
		wait.msgSetCashier(this);
		allMyWaiters.add(wait);
	}
	
	/*public void addMarkets(Market mark) {
		mark.msgSetCashier(this);
	}*/

	// Messages
	public void msgFoodBill(double price){
		if (restaurantMoney > price)
			restaurantMoney -= price;
		else {
			print("In debt!! Get $500 loan from bank!");
			restaurantMoney += 500 - price;
		}
	}
	
	public void msgHereIsAnOrder(String customerName, double amount, Restaurant1Waiter waiter) {
		Bills nextBill = new Bills();
		nextBill.setCustomer(customerName);
		nextBill.setWaiter(waiter);
		nextBill.setAmount(amount);
		bills.add(nextBill);
		stateChanged();
	}

	public void msgHereIsPayment(Restaurant1Customer customer, String order, Restaurant1Waiter waiter) {
		print("Received money");
		receivedMoneyFrom.add(customer);
		stateChanged();
	}
	
	public void msgCustomerCannotPay(Restaurant1Customer customer, String order, Restaurant1Waiter waiter) {
		print("Restaurant1Customer cannot pay");
		Outstanding nextOutstanding = new Outstanding(customer, order, waiter);
		customersWithDebt.add(nextOutstanding);
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (!bills.isEmpty()) {
			synchronized(bills){
				for(Bills bill : bills) {
					requestMoney(bill);
				}
			}
			bills.clear();
			return true;
		}
		if (!receivedMoneyFrom.isEmpty()) {
			synchronized(bills){
				for (Bills bill : bills) {
					if (receivedMoneyFrom.get(0).getCustomerName() == bill.getCustomer())
						payBill(bill);
				}
			}
			return true;
		}
		if (!customersWithDebt.isEmpty() && alertWaiter){
			customerWithDebt(customersWithDebt.get(0));
		}
		return false;
	}

	// Actions
	
	private void customerWithDebt(Outstanding out){
		synchronized(allMyWaiters){
			for (Restaurant1Waiter waiter : allMyWaiters) {
				if (waiter.getName() == out.getWaiter().getName())
					waiter.msgCustomerHasDebt(out.getCustomer().getCustomerName(), out.getFood());
			}
		}
		alertWaiter = false;
	}

	private void requestMoney(Bills bill) {
		System.out.println("from cash, cust owes money");
		(bill.getWaiter()).msgCustomerOwesMoney(bill.getAmount(), bill.getCustomer());
	}
	
	private void payBill(Bills bill) {
		bill.setAmount(0);
		bills.remove(bill);
	}

	//utilities
	
	public class Bills {
		//Restaurant1Customer customer;
		String customer;
		Restaurant1Waiter waiter;
		double amount;
		String food = new String();
		
		Bills() {
			customer = null;
			waiter = null;
			amount = 0;
		}
		
		Bills(String customerName, String order) {
			customer = customerName;
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
		
		void setAmount(String order) {
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
		
		void setAmount(double amt) {
			amount = amt;
		}
		
		void setWaiter(Restaurant1Waiter wait) {
			waiter = wait;
		}
		
		void setCustomer(String cust) {
			customer = cust;
		}
		
		public String getCustomer() {
			return customer;
		}
		
		public Restaurant1Waiter getWaiter() {
			return waiter;
		}
		
		double getAmount() {
			return amount;
		}
	}
	
	public class Outstanding {
		Restaurant1Waiter waiter;
		Restaurant1Customer customer;
		double amount;
		String food = new String();
		
		Outstanding() {
			customer = null;
			amount = 0;
		}
		
		Outstanding(Restaurant1Customer cust, String order, Restaurant1Waiter wait) {
			waiter = wait;
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
		
		void setCustomer(Restaurant1Customer cust) {
			customer = cust;
		}
		
		void setAmount(double amt) {
			amount = amt;
		}
		
		Restaurant1Customer getCustomer() {
			return customer;
		}
		
		Restaurant1Waiter getWaiter() {
			return waiter;
		}
		
		String getFood() {
			return food;
		}
		
		double getAmount() {
			return amount;
		}
	}

}


