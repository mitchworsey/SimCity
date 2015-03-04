package simCity.restaurant2;

import agent.Agent;
import simCity.Role;
import simCity.restaurant2.Restaurant2CookRole.Food;
import simCity.restaurant2.Restaurant2CookRole.FoodState;
import simCity.restaurant2.interfaces.Restaurant2Cashier;
import simCity.restaurant2.interfaces.Restaurant2Cook;
import simCity.restaurant2.interfaces.Restaurant2Market;

import java.util.*;

/**
 * Restaurant Restaurant2Cook Agent
 */

public class Restaurant2MarketRole extends Role implements Restaurant2Market {
	
	// DATA
	private String name;
	
	private Restaurant2Cashier restaurant2Cashier;

	private List<MyCook> myCooks = Collections.synchronizedList( new ArrayList<MyCook>() );
	
	class MyCook {
		Restaurant2Cook c;
		CookState state = CookState.None;
		List<Food> foodRequests = Collections.synchronizedList( new ArrayList<Food>() );
		MyCook(Restaurant2Cook c, CookState state) {
			this.c = c;
			this.state = state;
		}
	}
	
	enum CookState {None, OrderRequested, OrderProcessed};
	
	private List<Double> payments = Collections.synchronizedList( new ArrayList<Double>() );
	private double money = 0;
	
	Timer timer = new Timer();

	private Map<String, Food> marketInventory = Collections.synchronizedMap( new HashMap<String, Food>() );
	
	class Food {
		String foodName;
		int amount;
		double costPerUnit;
		Food(String foodName, int amount, double costPerUnit) {
			this.foodName = foodName;
			this.amount = amount;
			this.costPerUnit = costPerUnit;
		}
	}
	
	
	
	public Restaurant2MarketRole(String name) {
		super();
		this.name = name;
		
		// populate foodMap with some food items for now
		marketInventory.put("Steak", new Food("Steak", 4, 5.00));
		marketInventory.put("Chicken", new Food("Chicken", 4, 4.00));
		marketInventory.put("Salad", new Food("Salad", 4, 2.00));
		marketInventory.put("Pizza", new Food("Pizza", 4, 3.00));
	}

	/* (non-Javadoc)
	 * @see restaurant.Market#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Market#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	public void setCashier(Restaurant2Cashier restaurant2Cashier) {
		this.restaurant2Cashier = restaurant2Cashier;
	}
	

	
	
	/**
	 * Messages
	 */
	
	// from Restaurant2CookRole
	@Override
	public void INeedFood(Restaurant2Cook c, String foodChoice, int amount) {
		MyCook mc = new MyCook(c, CookState.OrderRequested);
		mc.foodRequests.add(new Food(foodChoice, amount, marketInventory.get(foodChoice).costPerUnit));
		myCooks.add(mc);
		stateChanged();
	}
	
	// from Restaurant2CashierRole
	public void HereIsPayment(double payment) {
		payments.add(payment);
		stateChanged();
	}
	
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		synchronized(myCooks) {
			// Safety check first to avoid null pointer exception that there is some cooks in myCooks
			if (myCooks.size() == 0) {
				// No cooks in myCooks, nothing to do
				return false;
			}
		}
		
		synchronized(myCooks) {
			for(MyCook mc : myCooks) {
				if (mc.state == CookState.OrderRequested) {
					processRequest(mc);
					return true;
				}
			}
		}
		
		synchronized(payments) {
			if (payments.size() == 0) {
				// No payments in payments, nothing to do
				return false;
			}
		}
		
		synchronized(payments) {
			for (double payment : payments) {
				handlePayment(payment);
				return true;
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
	
	private void processRequest(MyCook mc) {
		synchronized(mc.foodRequests) {
			for (Food f : mc.foodRequests) {
				Food marketFood = marketInventory.get(f.foodName);
				if (f.amount <= marketFood.amount) {
					marketFood.amount = marketFood.amount - f.amount;
					deliverFood(mc, f.foodName, f.amount);
				}
				else { // (f.amount > marketFood.amount)
					if (marketFood.amount <= 0) {
						mc.c.NotEnoughFood(f.foodName, f.amount);
					}
					else { // marketFood.amount > 0
						mc.c.NotEnoughFood(f.foodName, (f.amount - marketFood.amount));
						deliverFood(mc, f.foodName, marketFood.amount);
						marketFood.amount = 0;
					}
				}	
			}
		}
		mc.state = CookState.OrderProcessed;
	}
	
	private void deliverFood(final MyCook mc, final String foodName, final int amount) {
		timer.schedule(new TimerTask() {
			public void run() {
				print("Food Delivered to " + mc.c.getName());
				mc.c.HereIsFood(foodName, amount);
				double costPerUnit = marketInventory.get(foodName).costPerUnit;
				print("Billing restaurant2Cashier for " + (amount * costPerUnit) + " dollars");
				messageCashier(amount * costPerUnit);
			}
		},
		25000 );
	}
	
	private void messageCashier(double payment) {
		restaurant2Cashier.HereIsCost(this, payment);
	}
	
	private void handlePayment(double payment) {
		money = money + payment;
		payments.remove(payment);
	}
	
	
	// utilities

	/* (non-Javadoc)
	 * @see restaurant.Market#depleteMarket()
	 */
	@Override
	public void depleteMarket() {
		Food f;
		synchronized (marketInventory) {
			for( Map.Entry<String, Food> entry : marketInventory.entrySet() ) {
				f = entry.getValue();
				f.amount = 0;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Market#replenishMarket()
	 */
	@Override
	public void replenishMarket() {
		Food f;
		synchronized (marketInventory) {
			for( Map.Entry<String, Food> entry : marketInventory.entrySet() ) {
				f= entry.getValue();
				f.amount = 8;
			}
		}
	}
	
	
	/*
	public void setGui(MarketGui gui) {
		marketGui = gui;
	}

	public Restaurant2CookGui getGui() {
		return marketGui;
	}
	*/

}

