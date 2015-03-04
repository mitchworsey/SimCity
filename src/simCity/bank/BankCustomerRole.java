package simCity.bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.gui.BankGuardGui;
import simCity.interfaces.BankCustomer;
import simCity.interfaces.BankCustomerGuiInterface;
import simCity.interfaces.BankTeller;
import simCity.test.mock.EventLog;

public class BankCustomerRole extends Role implements BankCustomer {

	public BankTeller teller;
	
	String name;
	public int accountNum = 0; 
	public List<Transaction> transactions = new ArrayList<Transaction>();
	public enum state {openingAcct, withdrawing, depositing, depositingRestaurantMoney, loaning, waiting, finished};
	public BankCustomerGuiInterface gui;
	private Semaphore atLocation = new Semaphore(0, true);
	public EventLog log = new EventLog();
	
	public BankCustomerRole(String name) { //money parameter may not be needed
		super();
		
		this.name = name;
	}
	
	public void setTeller(BankTeller teller) {
		this.teller = teller;
	}
	
	public void setGui(BankCustomerGuiInterface gui) {
		this.gui = gui;
	}
	
	public BankCustomerGuiInterface getGui() {
		return this.gui;
	}
	
	public class Transaction {
		double amount;
		state state;
		String service;
		
		public Transaction(double amt, state state, String service) {
			amount = amt;
			this.state = state;
			this.service = service;
		}
	}
	
	
	// Messages
	//I'm assuming that if a person does not have a bank account, the gui will give the option of creating a bank account 
	//and will disable input fields related to withdrawing/depositing/making loans until an account is created, 
	//after which the "create an account" button will be disabled
	
	/**
	 * GUI messages
	 */
	public void msgCreateAutoTransactions() {
		transactions.add(new Transaction(0, state.openingAcct, "OpenAccount"));
		transactions.add(new Transaction(30, state.depositing, "Deposit"));
		stateChanged();
	}
	
	public void msgCreateBankAccount() { 
		transactions.add(new Transaction(0, state.openingAcct, "OpenAccount"));
		stateChanged();
	}

	public void msgWithdrawFromBank(double amt) { 
		transactions.add(new Transaction(amt, state.withdrawing, "Withdraw"));
		stateChanged();
	}
	
	public void msgMakeDepositFromBank(double amt) {
		transactions.add(new Transaction(amt, state.depositing, "Deposit"));
		stateChanged();
	}
	
	public void msgMakeLoanFromBank(double amt) {
		transactions.add(new Transaction(amt, state.loaning, "Loan"));
		stateChanged();
	}
	
	public void msgAtLocation() {
		atLocation.release();
		stateChanged();
	}
	
	/**
	 * Restaurant messages
	 */
	public void msgDepositRestaurantMoney(double amt, String service) {
		if(accountNum == 0) {
			transactions.add(new Transaction(0, state.openingAcct, "OpenAccount"));
		}
		transactions.add(new Transaction(amt, state.depositingRestaurantMoney, service));
		stateChanged();
	}
	
	/**
	 * BankTeller messages
	 */
	public void msgHereIsYourAcctNumber(int num, String service) {
		for (Transaction t : transactions) { 
			if(t.state == state.waiting && t.amount == 0 && t.service.equals(service)) {
				accountNum = num;
				t.state = state.finished;
			}
		}
		stateChanged();
	}
	
	public void msgMoneyWithdrawn(double amt, String service) {
		for (Transaction t : transactions) { 
			if(t.state == state.waiting && t.amount == amt && t.service.equals(service)) {
				((OrdinaryPerson)personAgent).money += amt;
				t.state = state.finished;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgMoneyDeposited(double amt, String service) {
		for (Transaction t : transactions) { 
			if(t.state == state.waiting && t.amount == amt && t.service.equals(service)) {
				((OrdinaryPerson)personAgent).money -= amt;
				t.state = state.finished;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgRestaurantMoneyDeposited(double amt, String service) {
		for (Transaction t : transactions) { 
			if(t.state == state.waiting && t.amount == amt && t.service.equals(service)) {
				t.state = state.finished;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgBalanceTooLow(double amt, String service) {
		for (Transaction t : transactions) { 
			if(t.state == state.waiting && t.service.equals(service)) {
				t.state = state.finished;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgLoanMade(double amt, String service) {
		for (Transaction t : transactions) { 
			if(t.state == state.waiting && t.service.equals(service)) {
				((OrdinaryPerson)personAgent).money += amt; //might also need to keep track of money owed to the bank
				t.state = state.finished;
				stateChanged();
				break;
			}
		}
	}
	
	public void msgCannotMakeLoan(double amt, String service) {
		for (Transaction t : transactions) { 
			if(t.state == state.waiting && t.service.equals(service)) {
				t.state = state.finished;
				stateChanged();
				break;
			}
		}
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		boolean done = true;
		try {
			if(!transactions.isEmpty()) {
				for(Transaction t : transactions) {
					if(t.state == state.openingAcct) {
						goToBankAndOpenAcct(t);
						return true;
					}
				}

				for(Transaction t : transactions) {
					if(t.state == state.withdrawing) {
						goToBankAndWithdraw(t);
						return true;
					}
				}

				for(Transaction t : transactions) {
					if(t.state == state.depositing) {
						goToBankAndDeposit(t);
						return true;
					}
				}
				
				for(Transaction t : transactions) {
					if(t.state == state.depositingRestaurantMoney) {
						goToBankAndDepositRestaurantMoney(t);
						return true;
					}
				}

				for(Transaction t : transactions) {
					if(t.state == state.loaning) {
						goToBankAndMakeLoan(t);
						return true;
					}
				}

				for(Transaction t : transactions) {
					if(t.state != state.finished) {
						done = false;
						break;
					}
				}
				if(done) {
					leaveBank();
					return true;
				}
			}
		}
		catch (ConcurrentModificationException cce) {
			return true;
		}
		return false;
	}
	
	
	// Actions

	private void goToBankAndOpenAcct(Transaction t) {
		gui.DoGoToTeller();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    teller.msgIWantToOpenAnAccount(this, "OpenAccount");
	    t.state = state.waiting;
	}

	private void goToBankAndWithdraw(Transaction t) {
		gui.DoGoToTeller();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    teller.msgIWantToWithdrawMoney(this, t.amount, "Withdraw");
	    t.state = state.waiting;
	}

	private void goToBankAndDeposit(Transaction t) {
		gui.DoGoToTeller();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(((OrdinaryPerson)personAgent).money >= t.amount) {
			teller.msgIWantToDepositMoney(this, t.amount, "Deposit");
		    t.state = state.waiting;
		}
		else { 
			print("Money in wallet: $"+((OrdinaryPerson)personAgent).money+". Not enough money in wallet to make this deposit.");
			t.state = state.finished;
		}
	}
	
	private void goToBankAndDepositRestaurantMoney(Transaction t) {
		gui.DoGoToTeller();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		teller.msgDepositRestaurantMoney(this, t.amount, "Deposit");
		t.state = state.waiting;
	}

	private void goToBankAndMakeLoan(Transaction t) {
		gui.DoGoToTeller();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    teller.msgIWantToMakeALoan(this, t.amount, "Loan");
	    t.state = state.waiting;
	}
	
	private void leaveBank() {
		print("Leaving bank");
		transactions.clear();
		gui.DoExitBank();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.removeGui(gui);
		gui = null;
		personAgent.OutOfComponent(this);
	}
}

