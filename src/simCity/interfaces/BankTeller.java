package simCity.interfaces;

/**
 * A BankTeller interface built to unit test a BankTellerRole.
 * 
 * @author Jessica Wang
 * 
 */
public interface BankTeller {
	
	/**
	 * @param BankCustomer The BankCustomer instance for whom to open a new account
	 * 
	 * Sent by the BankCustomer  
	 */
	public abstract void msgIWantToOpenAnAccount(BankCustomer customer, String service);
	
	/**
	 * @param BankCustomer The BankCustomer instance requesting money withdrawal
	 * @param amt The amount requested for the withdrawal
	 * 
	 * Sent by the BankCustomer
	 */
	public abstract void msgIWantToWithdrawMoney(BankCustomer customer, double amt, String service);
	
	/**
	 * @param BankCustomer The BankCustomer instance requesting money withdrawal
	 * @param amt The amount requested for the deposit
	 * 
	 * Sent by the BankCustomer
	 */
	public abstract void msgIWantToDepositMoney(BankCustomer customer, double amt, String service);
	
	/**
	 * @param BankCustomer The BankCustomer instance requesting a loan
	 * @param amt The amount requested for the loan
	 * 
	 * Sent by the BankCustomer
	 */
	public abstract void msgIWantToMakeALoan(BankCustomer customer, double amt, String service);

	public abstract void msgDoneWithTask();

	public abstract void msgHandOverMoneyToRobber();

	public abstract void msgDepositRestaurantMoney(BankCustomer customer, double amount,
			String string);
}

