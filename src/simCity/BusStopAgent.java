package simCity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simCity.interfaces.Bus;
import simCity.interfaces.BusStop;
import simCity.interfaces.Person;
import simCity.test.mock.EventLog;
import simCity.test.mock.LoggedEvent;
import agent.Agent;

public class BusStopAgent extends Agent implements BusStop {

	/*
	 * Data
	 */
	 
	
	String name;
	//BusStopGui gui; // TEMPORARY COMMENT
	public List<MyPerson> waitingPeople = Collections.synchronizedList(new ArrayList<MyPerson>());
	public EventLog log = new EventLog();

	public class MyPerson {
		public Person p;
		BusStop destination;
		public MyPerson(Person p, BusStop destination) {
			this.p = p;
			this.destination = destination;
		}
	}

	Map<Bus, List<BusStop> > busRoutes = new HashMap<Bus, List<BusStop> >(); // takes a Bus in, gets out a list of all the stops the bus makes

	public List<MyBus> buses = new ArrayList<MyBus>();

	class MyBus {
		Bus b;
		BusState state = BusState.none;
		MyBus(Bus b) {
			this.b = b;
		}
	}

	enum BusState {none, awaitingBus, busArrived, loading}

	
	/*
	 * Constructor and Utility Functions
	 */
	
	public BusStopAgent(String name) {
		super();
		this.name = name;
	}
	
	public BusStopAgent(String name, List<Bus> knownBuses) {
		super();
		this.name = name;
		// INSIDE CONSTRUCTOR, POPULATE BUSSTOP WITH ALL KNOWN BUSES THROUGH THAT BUSSTOP
		for (Bus b : knownBuses) {
			buses.add(new MyBus(b));
		}
	}
	
	/* (non-Javadoc)
	 * @see simCity.BusStop#addBus(simCity.interfaces.Bus)
	 */
	@Override
	public void addBus(Bus b) {
		buses.add(new MyBus(b));
	}
	
	public void addBusRoute(Bus b, List<BusStop> route) {
		busRoutes.put(b, route);
	}
	
	
	private MyBus findBus(Bus b) {
		for (MyBus mb : buses) {
			if (mb.b == b) {
				return mb;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see simCity.BusStop#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	
	
	/*
	 * Messages
	 */

	
	/* (non-Javadoc)
	 * @see simCity.BusStop#BusIsHere(simCity.interfaces.Bus)
	 */
	@Override
	public void BusIsHere(Bus b) {
		log.add(new LoggedEvent("Received BusIsHere msg"));
		MyBus mb = findBus(b);
		mb.state = BusState.busArrived;
		stateChanged();
	}

	// from Person

	/* (non-Javadoc)
	 * @see simCity.BusStop#WantToRide(simCity.interfaces.Person, simCity.BusStop)
	 */
	@Override
	public void WantToRide(Person p, BusStop bs) {
		log.add(new LoggedEvent("Received WantToRide msg"));
		print("Received WantToRide msg");
		waitingPeople.add(new MyPerson(p, bs));
	}

	/* (non-Javadoc)
	 * @see simCity.BusStop#BusIsLeaving(simCity.interfaces.Bus)
	 */
	@Override
	public void BusIsLeaving(Bus b) {
		log.add(new LoggedEvent("Received BusIsLeaving msg"));
		MyBus mb = findBus(b);
		mb.state = BusState.awaitingBus;
	}
	



	/*
	 * Scheduler
	 */

	/* (non-Javadoc)
	 * @see simCity.BusStop#pickAndExecuteAnAction()
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		/*
		if (there exists MyBus mb in buses such that mb.state = busArrived) {
			loadPassengers(mb);
			return true;
		}
		*/
		
		if (buses.size() == 0) {
			return false;
		}
		
		for (MyBus mb : buses) {
			if (mb.state == BusState.busArrived) {
				loadPassengers(mb);
				return true;
			}
		}
		
		return false;
	}



	/*
	 * Actions
	 */

	private void loadPassengers(MyBus mb) {
		log.add(new LoggedEvent("Loading passengers to bus"));
		mb.state = BusState.loading;
		//create deep copy of waitingPeople first;
		List<MyPerson> currentBusPassengers = Collections.synchronizedList(new ArrayList<MyPerson>());
		List<BusStop> busRoute = busRoutes.get(mb.b);
		
		/*
		for (MyPerson mp : waitingPeople) {
			if (mp.destination is in busRoutes.get(mb.b)'s (List<BusStop>) ) {
				currentBusPassengers.add(mp.p);
				waitingPeople.remove(mp);
			}
		}
		*/
		if (waitingPeople.size() > 0) {
			synchronized(waitingPeople) {
				for (MyPerson mp : waitingPeople) {
					if (busRoute.size() > 0) {
						for (BusStop bs : busRoute) {
							if (mp.destination == bs) {
								currentBusPassengers.add(mp);
								mp.p.BusIsHere(mb.b);
							}
						}
					}
				}
			}
		}
		if (currentBusPassengers.size() > 0) {
			synchronized (currentBusPassengers){
				for (MyPerson mp : currentBusPassengers) {
					waitingPeople.remove(mp);
				}
			}
		}
		mb.b.LoadPassengers(currentBusPassengers); // this will need to be protected (thread-safe) in case any new customers try to be added to list during deletion
	}
}



