package simCity.Restaurant3;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simCity.Role;
import simCity.Restaurant3.Restaurant3Check.CheckState;
import simCity.Restaurant3.Restaurant3MarketBill.MarketBillState;
import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;

public class Restaurant3CashierRole extends Role implements Restaurant3Cashier{
	public List<Restaurant3Check> checks = Collections.synchronizedList(new ArrayList<Restaurant3Check>());
	public List<Restaurant3Check> checksToRemove = new ArrayList<Restaurant3Check>();
	public List<Restaurant3Check> unpaidChecks = new ArrayList<Restaurant3Check>();
	
	public List<Restaurant3MarketBill> bills = Collections.synchronizedList(new ArrayList<Restaurant3MarketBill>());
	public List<Restaurant3MarketBill> billsToRemove = Collections.synchronizedList(new ArrayList<Restaurant3MarketBill>());
			
	private double cashInRegister = 200.0;
	
	public Map<String, Food> foods = new HashMap<String, Food>();
	
	private String name;
	
	public Restaurant3CashierRole(String name) {
		super();
		foods.put("Chicken", new Food("Chicken"));
		foods.put("Steak", new Food("Steak"));
		foods.put("Salad", new Food("Salad"));
		foods.put("Pizza", new Food("Pizza"));
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#getCashInRegister()
	 */
	@Override
	public double getCashInRegister(){
		return cashInRegister;
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#setLowCash()
	 */
	@Override
	public void setLowCash(){
		cashInRegister = 20;
	}

	
	@Override
	public void msgProduceCheck(Restaurant3Waiter w, Restaurant3Customer c, String choice){
		print("Received msgProduceCheck");
		checks.add(new Restaurant3Check(w, c, choice, foods.get(choice).price, CheckState.produced));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#msgMarketBillDelivered(simCity.Restaurant3.Restaurant3MarketBill)
	 */
	@Override
	public void msgMarketBillDelivered(Restaurant3MarketBill mb){
		print("Received msgMarketBillDelivered");
		mb.mbs = MarketBillState.distributed;
		bills.add(mb);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#msgHereIsChange(double)
	 */
	@Override
	public void msgHereIsChange(double change){
		print("Received msgHereIsChange");
		cashInRegister += change;
		DecimalFormat decim = new DecimalFormat("#.00");
		this.cashInRegister = Double.parseDouble(decim.format(this.cashInRegister));
		print("Current cash in Restaurant register balance = $" + cashInRegister);
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#msgHereIsPayment(simCity.Restaurant3.Restaurant3Check, double)
	 */
	@Override
	public void msgHereIsPayment(Restaurant3Check c, double cash){
		print("Received msgHereIsPayment");		
		if(cash < c.bill){
			print(c.c + " can't pay. Letting him pay for it next time he comes");
			unpaidChecks.add(c);
		}
		else{
			this.cashInRegister += cash;
			c.change = cash - c.bill;
			this.cashInRegister -= c.change;
		}
		
		
		DecimalFormat decim = new DecimalFormat("#.00");
		this.cashInRegister = Double.parseDouble(decim.format(this.cashInRegister));
		c.change = Double.parseDouble(decim.format(c.change));
		print("Bill Total including outstanding checks = $" + c.bill);
		print("Received = $" + cash);
		print("Change for " + c.c + " = $" + c.change);
		print("Current cash in register balance = $" + this.cashInRegister);
		
		c.cs = CheckState.paymentFinished;
		
		checks.add(c);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#pickAndExecuteAnAction()
	 */
	
	@Override
	public boolean pickAndExecuteAnAction() {
		if(!checksToRemove.isEmpty()){
			for(Restaurant3Check c: checksToRemove){
				checks.remove(c);
			}
			checksToRemove.clear();
		}
		if(!billsToRemove.isEmpty()){
			for(Restaurant3MarketBill mb: billsToRemove){
				bills.remove(mb);
			}
			billsToRemove.clear();
		}
		synchronized(checks){
			for(Restaurant3Check c: checks){
				if(c.cs == CheckState.paymentFinished){
					giveChangeToCustomer(c);
					return true;
				}
			}
		}
		synchronized(checks){
			for(Restaurant3Check c: checks){
				if(c.cs == CheckState.produced){
					giveCheckToWaiter(c);
					return true;
				}
			}
		}
		synchronized(bills){
			for(Restaurant3MarketBill mb: bills){
				if(mb.mbs == MarketBillState.distributed){
					payMarketBill(mb);
					return true;
				}
			}
		}
		
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#giveCheckToWaiter(simCity.Restaurant3.Restaurant3Check)
	 */
	@Override
	public void giveCheckToWaiter(Restaurant3Check c){
		print("Called giveCheckToWaiter");
		Restaurant3Check temp = null;
		if(!unpaidChecks.isEmpty()){
			for(Restaurant3Check unpaid: unpaidChecks){
				if(unpaid.c == c.c){
					c.bill += unpaid.bill;
					temp = unpaid;
				}
			}
		}
		if(temp!=null)
			unpaidChecks.remove(temp);
		c.cs = CheckState.distributed;
		checksToRemove.add(c);
		c.w.msgCheckIsReady(c);
	}
	

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#giveChangeToCustomer(simCity.Restaurant3.Restaurant3Check)
	 */
	@Override
	public void giveChangeToCustomer(Restaurant3Check c){
		print("Called giveChangeToCustomer");
		checksToRemove.add(c);
		c.c.msgHereIsYourChange(c.change);
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cashier#payMarketBill(simCity.Restaurant3.Restaurant3MarketBill)
	 */
	@Override
	public void payMarketBill(Restaurant3MarketBill mb){
		print("Called payMarketBill");
		if(cashInRegister < mb.bill){
			mb.m.msgHereIsPayment(mb, cashInRegister);
			cashInRegister += 200.00;
		}
		else{
			mb.m.msgHereIsPayment(mb, mb.bill);
			cashInRegister -= mb.bill;
		}
		billsToRemove.add(mb);
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
	}
}
