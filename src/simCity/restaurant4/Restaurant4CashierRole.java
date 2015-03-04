package simCity.restaurant4;

import simCity.restaurant4.gui.Restaurant4WaiterGui;
import simCity.restaurant4.interfaces.*;
import simCity.test.mock.EventLog;
import simCity.test.mock.LoggedEvent;
import simCity.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant4 Cashier Role
 */

public class Restaurant4CashierRole extends Role implements Restaurant4Cashier {
	public List<Check> checks = new ArrayList<Check>();
	public static Map<String, Double> priceMap = new HashMap<String, Double>();
	
	Timer timer = new Timer();
	
	public EventLog log = new EventLog();
	
	private Semaphore allow = new Semaphore(0,true);
	
	public List<MarketBill> marketBills = new ArrayList<MarketBill>();
	public double moneyInRegister;
	
	public enum checkState
	{pending, computed, paid, delayed, unsettled};

	private String name;

	public Restaurant4WaiterGui hostGui = null;

	public Restaurant4CashierRole(String name) {
		super();

		this.name = name;
		moneyInRegister = new Random().nextInt(150-50)+50;
		priceMap.put("steak", 15.99);
		priceMap.put("chicken", 10.99);
		priceMap.put("salad", 5.99);
		priceMap.put("pizza", 8.99);
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getChecks() {
		return checks;
	}

	// Messages

	public void msgCreateCheck(Restaurant4Waiter waiter, Restaurant4Customer customer, String choice, int table) {
		print("Received msgCreateCheck");
		log.add(new LoggedEvent("Received msgCreateCheck from waiter. Check for item = "+ choice));
		
		// if the check already exists from a previous visit, update the instance
		for(Check check : checks) {
			if(check.customer == customer) { 
				check.choice = choice;
				check.waiter = waiter;
				check.table = table;
				check.state = checkState.pending;
				stateChanged();
				return;
			}
		}
		checks.add(new Check(waiter, customer, choice, table, checkState.pending));
		stateChanged();
	}
	
	public void msgCanAffordFood(Restaurant4Customer customer) {
		for(Check check : checks) {
			if(check.customer == customer) {
				print("Received msgCanAffordFood");
				//log.add(new LoggedEvent("Received msgCanAffordFood from customer."));
				check.state = checkState.paid;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgCannotAffordFood(Restaurant4Customer customer) {
		for(Check check : checks) {
			if(check.customer == customer) {
				print("Received msgCannotAffordFood");
				//log.add(new LoggedEvent("Received msgCannotAffordFood from customer."));
				check.state = checkState.delayed;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgHereIsYourBill(Restaurant4Market market, double amount) {
		//log.add(new LoggedEvent("Received msgHereIsYourBill from market."));
		marketBills.add(new MarketBill(market, amount));
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try {
			for (Check check : checks) {
				if(check.state == checkState.pending) {
					computeCheck(check);
					return true;
				}
			}
			for (Check check : checks) {
				if(check.state == checkState.paid) {
					closeCheck(check);
					return true;
				}
			}
			for (Check check : checks) {
				if(check.state == checkState.delayed) {
					postponePayment(check);
					return true;
				}
			}
			if(!marketBills.isEmpty()) {
				payMarket(marketBills.get(0));
				return true;
			}
		}
		catch (ConcurrentModificationException cce) {
			return true;
		}
		return false;
	}

	// Actions
	private void computeCheck(Check check) {
		check.amount += priceMap.get(check.choice);
		System.out.println("CHECK AMOUNT: "+check.amount);
		check.waiter.msgCheckComputed(check.amount, check.table);
		check.state = checkState.computed;
	}
	
	private void closeCheck(Check check) {
		moneyInRegister += check.amount;
		checks.remove(check);
	}
	
	private void postponePayment(Check check) {
		print("You can pay next time");
		check.customer.msgPayNextTime(check.amount);
		check.state = checkState.unsettled;
	}
	
	private void payMarket(MarketBill bill) {
		print("Money in cash register: "+moneyInRegister);
		if(moneyInRegister >= bill.amount) {
			moneyInRegister = moneyInRegister - bill.amount;
			bill.market.msgHereIsPayment(bill.amount);
		}	
		else {
			bill.market.msgCannotPayBill(bill.amount);
		}
		marketBills.remove(bill);
	}

	public static class Check {
		public Restaurant4Waiter waiter;
		public Restaurant4Customer customer;
		public String choice;
		public int table;
		public double amount;
		public checkState state;

		public Check(Restaurant4Waiter w, Restaurant4Customer c, String choice, int table, checkState state) {
			waiter = w;
			customer = c;
			this.choice = choice;
			this.table = table;
			this.amount = 0;
			this.state = state;
		}
	}
	
	public static class MarketBill {
		public Restaurant4Market market;
		public double amount;
		
		public MarketBill(Restaurant4Market market, double amount) {
			this.market = market;
			this.amount = amount;
		}
	}
}

