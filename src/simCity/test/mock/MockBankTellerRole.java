package simCity.test.mock;

import simCity.interfaces.BankCustomer;
import simCity.interfaces.BankTeller;

public class MockBankTellerRole extends Mock implements BankTeller {

	public EventLog log = new EventLog();
	
	public MockBankTellerRole(String name) {
		super(name);
	}

	@Override
	public void msgIWantToOpenAnAccount(BankCustomer customer, String service) {
		log.add(new LoggedEvent("Received msgIWantToOpenAnAccount from bank customer. Service = " +service));
	}

	@Override
	public void msgIWantToWithdrawMoney(BankCustomer customer, double amt,
			String service) {
		log.add(new LoggedEvent("Received msgIWantToWithdrawMoney from bank customer. Amount = " +amt+ " Service = " +service));
	}

	@Override
	public void msgIWantToDepositMoney(BankCustomer customer, double amt,
			String service) {
		log.add(new LoggedEvent("Received msgIWantToDepositMoney from bank customer. Amount = " +amt+ " Service = " +service));
	}

	@Override
	public void msgIWantToMakeALoan(BankCustomer customer, double amt,
			String service) {
		log.add(new LoggedEvent("Received msgIWantToMakeALoan from bank customer. Amount = " +amt+ " Service = " +service));
	}

	@Override
	public void msgDoneWithTask() {
		log.add(new LoggedEvent("Received msgDoneWithTask from BankTellerGui"));
	}

	@Override
	public void msgHandOverMoneyToRobber() {
		log.add(new LoggedEvent("Received msgHandOverMoneyToRobber from BankGuard"));
	}

	@Override
	public void msgDepositRestaurantMoney(BankCustomer customer, double amount,
			String string) {
		// TODO Auto-generated method stub
		
	}

}

