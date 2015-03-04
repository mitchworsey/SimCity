package simCity.market;

import simCity.Role;

import java.util.ArrayList;

import simCity.gui.MarketPanel;
import simCity.interfaces.*;
import simCity.restaurant5.gui.Restaurant5CustomerGui;
import simCity.test.mock.EventLog;
import agent.Agent;

public class MarketCashierRole extends Role implements MarketCashier{
	private String name;
	public ArrayList<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	public ArrayList<MyRestaurant> myRests = new ArrayList<MyRestaurant>();
	public int register = 0;
	
	private MarketCashierGuiInterface mCashier;
	public EventLog log = new EventLog();
	public class MyCustomer{
		MarketCustomer c;
		int total = 0;
		int payment = 0;
		int change = 0;
		int debt = 0;
		
		MyCustomer(MarketCustomer cc, int totall, int pay) {
			total = totall;
			c = cc;
			payment = pay;
		}
	}
	
	public class MyRestaurant{
		int restaurantNum;
		int totalBill;
		int payment;
		int debt;
		
		public MyRestaurant(int restnum, int tbill) {
			restaurantNum = restnum;
			totalBill = tbill;
		}
	}
	
	MarketGrocer grocer;
	MarketCustomer customer;
	DeliveryTruck deliverytruck;
	
	public MarketCashierRole(String name) {
		super();
		this.name = name;
	}
	
	public void setGui(MarketCashierGuiInterface mp) {
		mCashier = mp;
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCashier#setGrocer(restaurant2Market.MarketGrocerRole)
	 */
	@Override
	public void setGrocer(MarketGrocer grocer) {
		this.grocer = grocer;
	}
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCashier#setCustomer(restaurant2Market.MarketCustomerRole)
	 */
	@Override
	public void setCustomer(MarketCustomer customer) {
		this.customer = customer;
	}
	
	public void setDeliveryTruck(DeliveryTruck dtruck) {
		this.deliverytruck = dtruck;
	}
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCashier#getCashierName()
	 */
	@Override
	public String getCashierName() {
		return name;
	}
	
	/*~~~~~~~~~~~~~~~~~~~~~ MESSAGES ~~~~~~~~~~~~~~~~~~*/
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCashier#msgMyTotalPayment(restaurant2Market.MarketCustomerRole, int, int)
	 */
	@Override
	public void msgMyTotalPayment(MarketCustomer c, int total, int pay) {
		myCustomers.add(new MyCustomer(c, total, pay));
		stateChanged();
	}
	
	public void msgCalculateRestBill(int restNum, int bill, int pay) {
		myRests.add(new MyRestaurant(restNum, bill));
		stateChanged();
	}

	
	/*~~~~~~~~~~~~~~~~~~~~~ SCHEDULER ~~~~~~~~~~~~~~~~~~*/
	public boolean pickAndExecuteAnAction() {
		for(int i=0; i< myCustomers.size(); i++) {
			CompareTotalPayment(myCustomers.get(i));
			return true;
		}
		for(int i=0; i< myRests.size(); i++) {
			CompareTotalPayment(myRests.get(i));
			return true;
		}	
		return false;
	}
	/*~~~~~~~~~~~~~~~~~~~~~ ACTIONS ~~~~~~~~~~~~~~~~~~*/
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCashier#CompareTotalPayment(restaurant2Market.MarketCashierRole.MyCustomer)
	 */
	@Override
	public void CompareTotalPayment(MyCustomer c) {
		if(c.payment >= c.total) { // NORMATIVE CASE
			print("Thank you for your business, " + c.c + "!");
			c.change = c.payment - c.total;
			print("Your change is " + c.change + " dollars.");
			c.c.msgHereIsChange(c.change);
			register += c.total;
			print("Have a great day," + c.c + "!");
			myCustomers.remove(c);
		}
		else{ 
			print("Restaurant2Customer, " + c.c + ", your payment does not cover the total charge.");
			c.debt = c.total - c.payment;
			//c.c.msgYouOweUs(c.debt);
			register += c.payment;
		//	DebtedCustomers.add(c, c.debt);
			myCustomers.remove(c);
		}
	}
	
	public void CompareTotalPayment(MyRestaurant r) {
		if(r.payment >= r.totalBill) {
			print("Thank you for your business, Restaurant " + r.restaurantNum + "!");
			register += r.payment;
			myRests.remove(r);
		}
		else {
			print("Restaurant # " + r.restaurantNum + ", you are in debt ");
		}
	}
	
	
	
	
	/*~~~~~~~~~~~~~~~~~~~~~ ANIMATION ~~~~~~~~~~~~~~~~~~*/
	
	
	
	
	
	
}
