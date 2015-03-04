package simCity.test.mock;

import java.util.List;

import simCity.BusStopAgent.MyPerson;
import simCity.interfaces.Bus;
import simCity.interfaces.Person;

public class MockBus extends Mock implements Bus {

	public MockBus(String name) {
		super(name);
	}

	@Override
	public void MsgActionComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MsgAtBusStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LoadPassengers(List<MyPerson> passengers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LeavingBus(Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
