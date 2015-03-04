package simCity.restaurant2;

import agent.Agent;

import simCity.Role;
import simCity.restaurant2.gui.Restaurant2CookGui;
import simCity.restaurant2.interfaces.Restaurant2Cashier;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Market;
import simCity.restaurant2.interfaces.Restaurant2Waiter;
import simCity.test.mock.EventLog;
import simCity.test.mock.LoggedEvent;

import java.util.*;

/**
 * Restaurant Restaurant2Cook Agent
 */

public class Restaurant2CashierRole extends Role implements Restaurant2Cashier {
	
	// DATA
	private String name;
	public EventLog log = new EventLog();
	
	public List<Check> checks = Collections.synchronizedList( new ArrayList<Check>() );
	public List<MyMarket> myMarkets = Collections.synchronizedList( new ArrayList<MyMarket>() );
	
	class MyMarket {
		Restaurant2Market restaurant2Market;
		double payment;
		MarketState state = MarketState.PaymentRequested;
		MyMarket(Restaurant2Market restaurant2Market, double payment) {
			this.restaurant2Market = restaurant2Market;
			this.payment = payment;
		}
	}
	
	enum MarketState {PaymentRequested, Unpaid, Paid}
	
	
	public class Check {
		Restaurant2Customer c;
		Restaurant2Waiter w;
		String foodChoice;
		double price;
		public CheckState state = CheckState.Requested;
		double payment = 0;
		public Check(Restaurant2Customer c, Restaurant2Waiter w, String foodChoice, double price) {
			this.c = c;
			this.w = w;
			this.foodChoice = foodChoice;
			this.price = price;
		}
	}	
	
	public enum CheckState {Requested, Produced, Paid, Debt, Done};
	private Map<String, Double> priceMap = new HashMap<String, Double>();
	public double money = 10.00;
	private boolean moneyOrdered = false;
	
	Timer timer = new Timer();
	
	public Restaurant2CashierRole(String name) {
		super();
		this.name = name;
		
		// populate our map
		priceMap.put("Steak", 15.99);
		priceMap.put("Chicken", 10.99);
		priceMap.put("Salad", 5.99);
		priceMap.put("Pizza", 8.99);
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#ProduceCheck(restaurant.WaiterAgent, restaurant.Customer, java.lang.String)
	 */
	
	/**
	 * Messages
	 */
	
	// from Restaurant2Waiter
	@Override
	public void ProduceCheck(Restaurant2Waiter w, Restaurant2Customer c, String choice) {
		print("Received ProduceCheck from Restaurant2Waiter");
		checks.add(new Check(c, w, choice, priceMap.get(choice)));
		log.add(new LoggedEvent("Received ProduceCheck from Restaurant2Waiter."));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#HereIsPayment(restaurant.CashierAgent.Check, double)
	 */
	// from Restaurant2Customer
	@Override
	public void HereIsPayment(Check check, double cash) {
		print("Received HereIsPayment from Restaurant2Customer");
		check.state = CheckState.Paid;
		check.payment = cash;
		log.add(new LoggedEvent("Received HereIsPayment from Restaurant2Customer."));
		stateChanged();
  	}
	
	// from Restaurant2Market
	public void HereIsCost(Restaurant2Market restaurant2Market, double price) {
		print("Received Bill from Restaurant2Market");
		myMarkets.add(new MyMarket(restaurant2Market, price));
		log.add(new LoggedEvent("Received HereIsCost from Restaurant2Market."));
		stateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized (checks) {
			// Safety check first to avoid null pointer exception that there is some check in checks
			if (checks.size() != 0) {
				for (Check check : checks) {
					if (check.state == CheckState.Paid) {
						giveCustomerChange(check);
						return true;
					}
				}
		
				for (Check check : checks) {
					if (check.state == CheckState.Requested) {
						giveWaiterCheck(check);
						return true;
					}
				}
			}
		}
		
		synchronized (myMarkets) {
			if (myMarkets.size() != 0) {
				for (MyMarket mm : myMarkets) {
					if (mm.state == MarketState.PaymentRequested) {
						payMarket(mm);
						return true;
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
	
	private void giveWaiterCheck(Check check) {
		synchronized (checks) {
			// iterate through all checks, find indebted checks, if customer owes any money, append to price
			for (Check currentcheck : checks) {
				if (currentcheck.state == CheckState.Debt) {
					if (check.c == currentcheck.c) {
						print("Added on previous debt of $" + (currentcheck.price) + " to price for customer: " + currentcheck.c.getName() );
						log.add(new LoggedEvent("Added previous debt to check."));
						check.price = check.price + currentcheck.price; // append the debted value from previous meal to this meals price
						currentcheck.state = CheckState.Done;
					}
				}
			}
		}
		check.state = CheckState.Produced;
		check.w.HereIsCheck(check);
	}
	
	
	private void giveCustomerChange(Check check) {
		if ( check.payment >= check.price ) {
			print("Payment sufficient, $" + (check.payment - check.price) + " is the change for customer: " + check.c.getName() );
			if (check.payment == check.price) { 
				log.add(new LoggedEvent("Restaurant2Customer payment was exact, setting check's state to be marked as Done."));
			}
			else {
				log.add(new LoggedEvent("Restaurant2Customer payment was extra, giving change to Restaurant2Customer."));
			}
			check.state = CheckState.Done;
			check.c.HereIsChange( check.payment - check.price );
		}
		else { //check.payment < check.price (Restaurant2Customer was not able to pay full amount for meal)
			print("Payment insufficient, $" + (check.price - check.payment) + " owed for customer: " + check.c.getName() );
			log.add(new LoggedEvent("Restaurant2Customer payment was insufficient, setting check's state to be marked as Debt."));
			check.state = CheckState.Debt; // mark check as in debt for later use
			check.price = check.price - check.payment; // produce the debt owed for this meal as the price on the check
			check.c.HereIsChange( 0 );
		}
	}
	
	private void payMarket(final MyMarket mm) {
		if (money - mm.payment < 0) { // cashier cannot afford to pay restaurant2Market
			print("Insufficient money!");
			mm.state = MarketState.Unpaid;
			if (!moneyOrdered) {
				print("Out of money! Getting more money to pay restaurant2Market...");
				log.add(new LoggedEvent("Out of money, starting timer for more money"));
				timer.schedule(new TimerTask() {
					public void run() {
						print("Got more money, paying restaurant2Market");
						cashierGetMoney();
						mm.restaurant2Market.HereIsPayment(mm.payment);
						log.add(new LoggedEvent("Paid restaurant2Market"));
						money = money - mm.payment;
					}
				},
				7000 );
				moneyOrdered = true;
			}
		}
		else { // cashier can afford to pay restaurant2Market
			mm.restaurant2Market.HereIsPayment(mm.payment);
			log.add(new LoggedEvent("Sufficient money, paid restaurant2Market"));
			money = money - mm.payment;
			mm.state = MarketState.Paid;
		}
	}
	
	private void cashierGetMoney() {
		money = money + 40.00;
		moneyOrdered = false;
		synchronized (myMarkets) {
			for (MyMarket mm : myMarkets) {
				if (mm.state == MarketState.Unpaid) {
					mm.state = MarketState.PaymentRequested;
				}
			}
		}
		stateChanged();
	}
	
	// utilities

	/*
	public void setGui(CashierGui gui) {
		cashierGui = gui;
	}

	public CashierGui getGui() {
		return cashierGui;
	}
	*/

}

