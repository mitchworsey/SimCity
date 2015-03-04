package simCity.restaurant4.interfaces;

import simCity.interfaces.Person;

public interface Restaurant4Host {

	void msgWantToGoOnBreak(Restaurant4Waiter w);

	void msgGoingBackToWork(Restaurant4Waiter w);

	void newWaiterAdded(Restaurant4Waiter w);

	void msgLeavingBeforeEating(Restaurant4Customer customer, int waitingLocation);

	void msgIWantFood(Restaurant4Customer customer);

	void msgWaitingSeatIsFree(int waitingLocation);

	void msgTableIsFree(Restaurant4Waiter waiter, int table);

	void msgGoingOnBreak(Restaurant4Waiter waiter);

}
