package simCity.interfaces;

import simCity.interfaces.Bus;
import simCity.BusStopAgent.MyPerson;
import simCity.interfaces.Person;

public interface BusStop {

	public abstract void addBus(Bus b);

	public abstract String getName();

	public abstract void BusIsHere(Bus b);

	public abstract void WantToRide(Person p, BusStop bs);

	public abstract void BusIsLeaving(Bus b);

	public abstract boolean pickAndExecuteAnAction();

}