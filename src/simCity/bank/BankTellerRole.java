package simCity.bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.gui.BankTellerGui;
import simCity.interfaces.BankCustomer;
import simCity.interfaces.BankTeller;
import simCity.test.mock.EventLog;


public class BankTellerRole extends Role implements BankTeller {
	
	private String name;
	double bankMoney;
	public enum transactionState {nothing, openingAcct, withdrawing, depositing, depositingRestaurantMoney, loaning};
	public List<Account> accounts = Collections.synchronizedList(new ArrayList<Account>());
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private BankTellerGui gui;
	private Semaphore doneHandling = new Semaphore(0, true);
	public EventLog log = new EventLog();

	public BankTellerRole(String name, double money) {
		super();
		
		this.name = name;
		this.bankMoney = money;
	}
	
	public class Account {
		BankCustomer customer;
		int accountNum; //may not need
		double balance;
		
		public Account(BankCustomer customer, int accountNum, double balance) {
			this.customer = customer;
			this.accountNum = accountNum;
			this.balance = balance;
		}
	}
	
	public void setGui(BankTellerGui gui) {
		this.gui = gui;
	}
	
	public class MyCustomer {
		Account account;
		double amount;
		transactionState state;
		String service;
		
		public MyCustomer(Account account, double amount, transactionState state, String service) {
			this.account = account;
			this.amount = amount;
			this.state = state;
			this.service = service;
		}
	}
	

	// Messages
	/**
	 * From GUI
	 */
	public void msgDoneWithTask() {
		doneHandling.release();
		stateChanged();
	}
	
	/**
	 * From BankGuard
	 */
	public void msgHandOverMoneyToRobber() {
		bankMoney = 0;
		stateChanged();
	}
	
	/**
	 * From BankCashier
	 */
	public void msgDepositRestaurantMoney(BankCustomer customer, double amt, String service) {
		synchronized (accounts) {
			for(Account account : accounts) {
				if(account.customer == customer) {
					customers.add(new MyCustomer(account, amt, transactionState.depositingRestaurantMoney, service));
					stateChanged();
					break;
				}
			}
		}
	}
	
	/**
	 * From BankCustomer
	 */
	public void msgIWantToOpenAnAccount(BankCustomer customer, String service) {
		synchronized (accounts) { //this might be removed if the gui disables the add account button once an account is created
			for(Account account : accounts) {
				if(account.customer == customer) {
					print("An account already exits!");
					return;
				}
			}
		}
		Account account = new Account(customer, accounts.size()+1, 0);
		accounts.add(account);
		customers.add(new MyCustomer(account, 0, transactionState.openingAcct, service));
		stateChanged();
	}
	
	public void msgIWantToWithdrawMoney(BankCustomer customer, double amt, String service) {
		synchronized (accounts) {
			for(Account account : accounts) {
				if(account.customer == customer) {
					customers.add(new MyCustomer(account, amt, transactionState.withdrawing, service));
					stateChanged();
					break;
				}
			}
		}
	}
	
	public void msgIWantToDepositMoney(BankCustomer customer, double amt, String service) {
		synchronized (accounts) {
			for(Account account : accounts) {
				if(account.customer == customer) {
					customers.add(new MyCustomer(account, amt, transactionState.depositing, service));
					stateChanged();
					break;
				}
			}
		}
	}
	
	public void msgIWantToMakeALoan(BankCustomer customer, double amt, String service) {
		synchronized (accounts) {
			for(Account account : accounts) {
				if(account.customer == customer) {
					customers.add(new MyCustomer(account, amt, transactionState.loaning, service));
					stateChanged();
					break;
				}
			}
		}
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized (customers) {
			for(MyCustomer t : customers) {
				if(t.state == transactionState.openingAcct) {
					giveAccountNumber(t);
					return true;
				}
			}
		}
		synchronized (customers) {
			for(MyCustomer t : customers) {
				if(t.state == transactionState.withdrawing) {
					handleWithdrawal(t);
					return true;
				}
			}
		}
		synchronized (customers) {
			for(MyCustomer t : customers) {
				if(t.state == transactionState.depositing) {
					handleDeposit(t);
					return true;
				}
			}
		}
		synchronized (customers) {
			for(MyCustomer t : customers) {
				if(t.state == transactionState.depositingRestaurantMoney) {
					handleRestaurantDeposit(t);
					return true;
				}
			}
		}
		synchronized (customers) {
			for(MyCustomer t : customers) {
				if(t.state == transactionState.loaning) {
					handleLoan(t);
					return true;
				}
			}
		}
		return false;
	}

	
	// Actions
	
	public void giveAccountNumber(MyCustomer t) {
		print("Opening account");
		gui.DoMakeAccount();
		try {
			doneHandling.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.account.customer.msgHereIsYourAcctNumber(t.account.accountNum, t.service);
		customers.remove(t);
	}
	
	private void handleWithdrawal(MyCustomer t) {
		print("Handling money withdrawal");
		gui.DoHandleMoney();
		try {
			doneHandling.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(t.account.balance >= t.amount) {
			t.account.balance -= t.amount;
			print("Withdrew: $"+t.amount+" Balance: $"+t.account.balance);
			t.account.customer.msgMoneyWithdrawn(t.amount, t.service);
		}
		else {
			print("Failed to withdraw: $"+t.amount+" Balance: $"+t.account.balance);
			t.account.customer.msgBalanceTooLow(t.amount, t.service);
		}
		customers.remove(t);
	}
	
	private void handleDeposit(MyCustomer t) {
		print("Handling money deposit");
		gui.DoHandleMoney();
		try {
			doneHandling.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.account.balance += t.amount;
		print("Deposited: $"+t.amount+" Balance: $"+t.account.balance);
		t.account.customer.msgMoneyDeposited(t.amount, t.service);
		customers.remove(t);
	}
	
	private void handleRestaurantDeposit(MyCustomer t) {
		print("Depositing restaurant money");
		gui.DoHandleMoney();
		try {
			doneHandling.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.account.balance += t.amount;
		print("Deposited: $"+t.amount+" Balance: $"+t.account.balance);
		customers.remove(t);
	}
	
	private void handleLoan(MyCustomer t) {
		print("Handling loan");
		gui.DoHandleMoney();
		try {
			doneHandling.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(bankMoney >= t.amount) {
			bankMoney -= t.amount;
			t.account.balance += t.amount;
			print("Loaned: $"+t.amount+" Balance: $"+t.account.balance);
			t.account.customer.msgLoanMade(t.amount, t.service);
		}
		else {
			print("Bank doesn't have enough money to make the loan");
			t.account.customer.msgCannotMakeLoan(t.amount, t.service);
		}
		customers.remove(t);
	}
}

