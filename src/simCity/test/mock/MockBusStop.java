package simCity.test.mock;

import simCity.interfaces.Bus;
import simCity.interfaces.BusStop;
import simCity.interfaces.Person;
import simCity.BusStopAgent.MyPerson;

public class MockBusStop extends Mock implements BusStop {

	public MockBusStop(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addBus(Bus b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BusIsHere(Bus b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WantToRide(Person p, BusStop bs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BusIsLeaving(Bus b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
