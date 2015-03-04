package simCity.test.mock;

import simCity.interfaces.BankCustomer;
import simCity.interfaces.BankTeller;

public class MockBankCustomerRole extends Mock implements BankCustomer{

	/**
	 * Reference to the BankTeller under test that can be set by the unit test.
	 */
	public BankTeller teller;
	
	public EventLog log = new EventLog();

	public MockBankCustomerRole(String name) {
		super(name);
	}
	
	@Override
	public void msgCreateBankAccount() {
		log.add(new LoggedEvent("Received msgCreateBankAccount from ControlPanel."));
	}
	
	@Override
	public void msgMakeDepositFromBank(double amt) {
		log.add(new LoggedEvent("Received msgMakeDepositFromBank from ControlPanel. Amount = "+ amt));
	}
	
	@Override
	public void msgMakeLoanFromBank(double amt) {
		log.add(new LoggedEvent("Received msgMakeLoanFromBank from ControlPanel. Amount = "+ amt));
	}

	@Override
	public void msgWithdrawFromBank(double amt) {
		log.add(new LoggedEvent("Received msgWithdrawFromBank from ControlPanel. Amount = "+ amt));
	}

	@Override
	public void msgHereIsYourAcctNumber(int num, String service) {
		log.add(new LoggedEvent("Received msgHereIsYourAcctNumber from teller. Account number = "+ num +" Service = "+ service));
	}

	@Override
	public void msgMoneyDeposited(double amt, String service) {
		log.add(new LoggedEvent("Received msgMoneyDeposited from teller. Amount = "+ amt +" Service = "+ service));
	}

	@Override
	public void msgMoneyWithdrawn(double amt, String service) {
		log.add(new LoggedEvent("Received msgMoneyWithdrawn from teller. Amount withdrawn = "+ amt +" Service = "+ service));
	}

	@Override
	public void msgBalanceTooLow(double amt, String service) {
		log.add(new LoggedEvent("Received msgBalanceTooLow from teller. Amount requested = "+ amt +" Service = "+ service));
	}

	@Override
	public void msgLoanMade(double amt, String service) {
		log.add(new LoggedEvent("Received msgLoanMade from teller. Amount loaned = "+ amt +" Service = "+ service));
	}

	@Override
	public void msgCannotMakeLoan(double amt, String service) {
		log.add(new LoggedEvent("Received msgLoanMade from teller. Amount requested = "+ amt +" Service = "+ service));
	}

	@Override
	public void setTeller(BankTeller teller) {
		// TODO Auto-generated method stub
	}

	@Override
	public void msgAtLocation() {
		log.add(new LoggedEvent("Received msgAtLocation from BankCustomerGui"));
	}

	@Override
	public void msgCreateAutoTransactions() {
		log.add(new LoggedEvent("Received msgCreateAutoTransactions from BankCustomerGui."));
	}
}
