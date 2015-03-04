package simCity.interfaces;

import java.util.List;

import simCity.BusStopAgent.MyPerson;
import simCity.interfaces.Person;

public interface Bus {

	public abstract String getName();

	// from BusGui
	public abstract void MsgActionComplete();

	// from BusGui
	public abstract void MsgAtBusStop();

	public abstract void LoadPassengers(List<MyPerson> passengers);

	public abstract void LeavingBus(Person p);

	public abstract boolean pickAndExecuteAnAction();

}