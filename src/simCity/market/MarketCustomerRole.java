package simCity.market;

import simCity.Role;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import simCity.gui.Gui;
import simCity.gui.SimCityGui;
import simCity.interfaces.*;
import simCity.market.MarketGrocerRole.MyCustomer;
import agent.Agent;
import simCity.OrdinaryPerson;

public class MarketCustomerRole extends Role implements MarketCustomer { // implements Restaurant2Customer
	//private individual levels and info
	private String name;	
	
	Timer timer = new Timer();
	//private Restaurant2CustomerGui customerGui;
	
	private MarketCustomerGuiInterface mCustomer;
	
	/* GUI SEMAPHORES */
	public Semaphore atMarket = new Semaphore(0, true);
	public Semaphore atGrocer = new Semaphore(0, true);
	public Semaphore atWaitingArea = new Semaphore(0, true);
	public Semaphore atPickUpArea = new Semaphore(0, true);
	public Semaphore atCashier = new Semaphore(0,true);
	public Semaphore atExit = new Semaphore(0, true);

	private int bill = 0;
	private int change = 0;
	private int wallet = 0;
	//private int debt = 0;

	public class Need {
		String type;
		public NeedState nState = NeedState.InNeed;
		
		public Need(String stype){
			type = stype;
		}
	
	}
	
	public ArrayList<Need> myNeeds = new ArrayList<Need>();
	public ArrayList<String> chosenNeeds = new ArrayList<String>();
	
	public enum NeedState{ None, InNeed, WaitingForProduct, GivenProduct, PaidForProduct};
	NeedState nStartState = NeedState.None;
	NeedState nState = NeedState.InNeed;
	
	MarketGrocer grocer;
	MarketCashier cashier;
	
	//myNeeds.add();
	
	
	public void setWallet(int moneyy) {
		wallet = moneyy; 
	}
	/* Constructor for Restaurant2Market Restaurant2Customer */
	public MarketCustomerRole(String name) { // MarketCustomerGui <- set gui function
		super();
		this.name = name;
	}
	
	public void setGui(MarketCustomerGuiInterface mc) {
		mCustomer = mc;
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#setGrocer(restaurant2Market.MarketGrocerRole)
	 */
	@Override
	public void setGrocer(MarketGrocer grocer) {
		this.grocer = grocer;
	}
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#setCashier(restaurant2Market.MarketCashier)
	 */
	@Override
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#getCustomerName()
	 */
	@Override
	public String getCustomerName() {
		return name;
	}
	
	public Need findNeed(String product){
		for(int i=0; i< myNeeds.size(); i++){
			if(product == myNeeds.get(i).type){
				 return myNeeds.get(i);
			}
		}
		return null;
	}
	
	public boolean givenAllNeeds() {
		for(int i=0 ;i< myNeeds.size(); i++) {
			if(!(myNeeds.get(i).nState == NeedState.GivenProduct))
				return false;
		}
		return true;
	}
	
	public boolean paidAllNeeds() {
		for(int i=0 ;i< myNeeds.size(); i++) {
			if(!(myNeeds.get(i).nState == NeedState.PaidForProduct))
				return false;
		}
		return true;
	}
	
	
	/*~~~~~~~~~~~~~~~~~~~~~ MESSAGES ~~~~~~~~~~~~~~~~~~*/
	
	public void AddNeed(String need) {
		//myNeeds.add(need);
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#IAmInNeed()
	 */
	@Override
	public void IAmInNeed() {// from animation
//		mCustomer.DoEnterMarket();
//		try{
//			atMarket.acquire();
//		} catch (InterruptedException e){
//			e.printStackTrace();
//		}
//		mCustomer.DoGoToGrocer();
		print("I need stuff.");
		Need car = new Need("Car");
		Need bread = new Need("Bread");
		Need milk = new Need("Milk");
		Need eggs = new Need("Eggs");
		myNeeds.add(car);
		myNeeds.add(bread);
		myNeeds.add(milk);
		myNeeds.add(eggs);
		//chosenNeeds = glist;
//		for (int i=0; i< chosenNeeds.size(); i++) {
//			switch (chosenNeeds.get(i)) {
//				case "Car": myNeeds.add(car);
//					break;
//				case "Bread": myNeeds.add(bread);
//					break;
//				case "Milk": myNeeds.add(milk);
//					break;
//				case "Eggs": myNeeds.add(eggs);
//					break;
//				default: 
//					break;
//			}
//		}
		print("My Grocer: " + grocer);
		int needSize = myNeeds.size();
		print("myNeeds size: " + needSize);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#msgIRetrieved(java.lang.String)
	 */
	@Override
	public void msgIRetrieved(String type) {
		print("Grocer retrieved: " + type);
		findNeed(type).nState = NeedState.GivenProduct;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#msgHereIsBill(int)
	 */
	@Override
	public void msgHereIsBill(int total) {
		print("Received bill of " + total + " amount.");
		bill = total;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#msgHereIsChange(int)
	 */
	@Override
	public void msgHereIsChange(int CashierChange) { 
		change = CashierChange;
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketCustomer#msgWeHaveNoMore(java.lang.String)
	 */
	@Override
	public void msgWeHaveNoMore(String product) {
		print(product + " is still needed. Keeping " + product + " on the list.");
		stateChanged(); // should go to another restaurant2Market or order from somewhere else
	}
	/*~~~~~~~~~~~~~~~~~~~~~ GUI MESSAGES ~~~~~~~~~~~~~~~~~~~~~*/
	public void msgAtMarket() {
		atMarket.release();
		stateChanged();
	}
	public void msgAtGrocer() {
		atGrocer.release();
		stateChanged();
	}
	public void msgAtWaitingArea() {
		atWaitingArea.release();
		stateChanged();
	}
	public void msgAtPickUp() {
		atPickUpArea.release();
		stateChanged();
	}
	public void msgAtCashier() {
		atCashier.release();
		stateChanged();
	}
	public void msgLeftMarket() {
		atExit.release();
		print("Left Market");
		stateChanged();
	}
	
	
	/*~~~~~~~~~~~~~~~~~~~~~ SCHEDULER ~~~~~~~~~~~~~~~~~~~~~*/
	public boolean pickAndExecuteAnAction() {
		for(int i=0; i<myNeeds.size(); i++){
			if(myNeeds.get(i).nState == NeedState.InNeed) {
				print("Scheduler: InNeed");
				INeed();
				return true;
			}
		}
		for(int i=0; i<myNeeds.size(); i++){
			//if(myNeeds.get(i).nState == NeedState.GivenProduct) {
			if(givenAllNeeds() == true) {
				print("Scheduler: GivenAllProducts");
				PayForProducts();
				return true;
			}
		}
		for(int i=0; i<myNeeds.size(); i++){
			if(paidAllNeeds() == true) {
				print("Scheduler: PaidForProduct");
				LeaveMarket();
				return true;
			}
		}
		if(!(change==0)) {
			AddChange();
			return true;
		}

		return false;
	}

	/*~~~~~~~~~~~~~~~~~~~~~ ACTIONS ~~~~~~~~~~~~~~~~~~~~~*/
	public void INeed() {
		//Do("Going to Restaurant2Market.");
		mCustomer.DoGoToGrocer();
		try{
			atGrocer.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		print("Hello, " + grocer + ", I would like to order stuff.");
		ArrayList<String> needs = new ArrayList<String>();
		for(int i=0; i<myNeeds.size(); i++) {
			needs.add(myNeeds.get(i).type);
			myNeeds.get(i).nState = NeedState.WaitingForProduct;
		}
		print("I would like: " + needs);
		grocer.msgIWantStuff(this, needs);
	}
	
	private void PayForProducts() { // should pick up items as well
		//Do("Going to pay for my products.");
//		if(getMoneyLevel() >= (bill + debtLevel)) {
//			customerGui.DoGoToCashier();
//			cashier.msgHereIsOrderPayment(this, bill);
//			moneyLevel -= bill;
//			print("Money level is now: " + moneyLevel);
//		}
//		else {
//			cashier.msgIDoNotHaveEnoughMoney(this, bill);
//			print("Sorry! I'll pay next time........");
//			debtLevel = bill;
//		}
		print("Going to go pick up my products and pay at cashier.");
		mCustomer.DoPickUpProducts();
		try{
			atPickUpArea.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		mCustomer.DoGoToCashier();
		try{
			atCashier.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		cashier.msgMyTotalPayment(this, bill, bill); // HACK for payment and bill...***
		wallet -= bill;
		for(int i=0; i<myNeeds.size(); i++) {
			myNeeds.get(i).nState = NeedState.PaidForProduct;
		}
		print("Money level is now: " + wallet);
		stateChanged();
		//msgMyTotal&Payment(int total, int pay);
		//guiGoToCashier();
	}
	
	private void LeaveMarket() {
		print("Leaving market.");
		mCustomer.DoExitMarket();
		
		try{
			atExit.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
		getPersonAgent().OutOfComponent(this);
		SimCityGui.marketPanel.removeGui((Gui) mCustomer);		
	}
	
	private void AddChange() {
		wallet =+ change;
		((OrdinaryPerson) getPersonAgent()).money += change;
		
	}













}
