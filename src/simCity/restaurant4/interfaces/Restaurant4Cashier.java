package simCity.restaurant4.interfaces;


/**
 * A sample Cashier interface built to unit test a CashierAgent.
 *
 * @author Jessica Wang
 *
 */
public interface Restaurant4Cashier {
	/**
	 * @param waiter The waiter instance requesting the check
	 * @param customer The customer instance to whom the check will be delivered
	 * @param choice The menu selection according
	 * @param table The table number associated with the check
	 *
	 * Sent by the waiter prompting the cashier to create the check for the specified customer
	 */
	public abstract void msgCreateCheck(Restaurant4Waiter waiter, Restaurant4Customer customer, String choice, int table);

	/**
	 * @param customer The customer instance who is paying for food
	 *
	 * Sent by the customer to end the transaction between the cashier and the customer if the customer has enough money
	 */
	public abstract void msgCanAffordFood(Restaurant4Customer customer);


	/**
	 * @param customer The customer instance who is paying for food
	 * 
	 * Sent by the customer if he/she does have enough money to pay for the food (in lieu of sending {@link #msgCanAffordFood(Customer customer)}
	 */
	public abstract void msgCannotAffordFood(Restaurant4Customer customer);
	
	/**
	 * @param market The MarketAgent instance who is sending the cashier the bill
	 * @param amount The amount on the bill
	 * 
	 * Sent by the market who has fulfilled an order for the cook
	 */
	public abstract void msgHereIsYourBill(Restaurant4Market market, double amount);
	
}