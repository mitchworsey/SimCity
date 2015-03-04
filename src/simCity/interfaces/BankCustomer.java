package simCity.interfaces;

import java.util.List;

/**
 * A BankCustomer interface built to unit test a BankCustomerRole.
 *
 * @author Jessica Wang
 *
 */
public interface BankCustomer {

	/**
	 * Sent by the GUI during ActionListener 
	 */
	public abstract void msgCreateBankAccount();

	/**
	 * @param amt The amount that the BankCustomer requests to withdraw
	 *
	 * Sent by the GUI during ActionListener 
	 */
	public abstract void msgWithdrawFromBank(double amt);

	/**
	 * @param amt The amount that the BankCustomer requests to deposit
	 * 
	 * Sent by the GUI during ActionListener 
	 */
	public abstract void msgMakeDepositFromBank(double amt);
	
	/**
	 * @param amt The amount that the BankCustomer requests to loan
	 * 
	 * Sent by the GUI during ActionListener 
	 */
	public abstract void msgMakeLoanFromBank(double amt);
	
	/**
	 * @param num The bank account number
	 * 
	 * Sent by the BankTeller after an account has been created for the BankCustomer
	 * @param service 
	 */
	public abstract void msgHereIsYourAcctNumber(int num, String service);
	
	/**
	 * @param amt The amount that the BankCustomer deposited
	 * 
	 * Sent by the BankTeller after money has successfully been deposited
	 * @param service 
	 */
	public abstract void msgMoneyDeposited(double amt, String service);
	
	/**
	 * @param amt The amount that the BankCustomer withdrew
	 * 
	 * Sent by the BankTeller after money has successfully been withdrawn
	 * @param service 
	 */
	public abstract void msgMoneyWithdrawn(double amt, String service);
	
	/**
	 * @param amt The amount that the BankCustomer attempted to withdraw
	 * 
	 * Sent by the BankTeller if the BankCustomer tries to withdraw more money than he has in the account
	 * @param service 
	 */
	public abstract void msgBalanceTooLow(double amt, String service);
	
	/**
	 * @param amt The amount that the BankCustomer is loaning from the bank
	 * 
	 * Sent by the BankTeller after money has successfully been 
	 * @param service 
	 */
	public abstract void msgLoanMade(double amt, String service);
	
	/**
	 * @param amt The amount that the BankCustomer attempted to loan
	 * 
	 * Sent by the BankTeller if the bank does not have enough money to give out the loan
	 * @param service 
	 */
	public abstract void msgCannotMakeLoan(double amt, String service);

	public abstract void setTeller(BankTeller teller);

	public abstract void msgAtLocation();

	public abstract void msgCreateAutoTransactions();
}

