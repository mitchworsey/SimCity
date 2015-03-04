package simCity.restaurant4.interfaces;

public interface Restaurant4Cook {

	void msgCheckInventory();

	void msgCanFulfillOrder(String choice);

	void msgCannotFulfillOrder(String choice);
	
	void msgCookOrder();

	void msgCookOrder(Restaurant4Waiter waiter, String order, int table);

	void msgAtLocation();

}
