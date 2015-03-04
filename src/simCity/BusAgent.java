package simCity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import agent.Agent;
import simCity.BusStopAgent.MyPerson;
import simCity.interfaces.Bus;
import simCity.interfaces.BusStop;
import simCity.interfaces.Person;
import simCity.test.mock.EventLog;
import simCity.test.mock.LoggedEvent;
import simCity.interfaces.BusGuiInterface;


public class BusAgent extends Agent implements Bus {

	/*
	 * Data
	 */
	
	public EventLog log = new EventLog();
	public List<MyPerson> onBoard = Collections.synchronizedList(new ArrayList<MyPerson>());
	BusGuiInterface gui;
	public Map<Integer, BusStop> busRoute = new HashMap<Integer, BusStop>(); // will be populated with all the BusStops bus should be at
	public int stopCounter = 0;
	public BusStop thisStop;
	String name;
	
	public Semaphore actionComplete = new Semaphore(0,true);
	
	BusState state = BusState.none;
	
	enum BusState {none, driving, atBusStop, doneUnloading, doneLoading}


	/*
	 * Constructor and Utility functions
	 */
	public BusAgent() {
		super();
		//this.gui = gui;
		//populate map and stuff;
	}
	
	
	public BusAgent(String BusName) {
		super();
		//this.gui = gui;
		name = BusName;
		// populate map and stuff;
	}
	
	public void setGui(BusGuiInterface gui){
		this.gui = gui;
	}
	
	public void populateMap(int index, BusStop bs) {
		busRoute.put(index, bs);
	}

	/* (non-Javadoc)
	 * @see simCity.Bus#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	
	
	/*
	 * Messages
	 */

	// from BusGui
	public void MsgActionComplete() {
		actionComplete.release();
	}
	
	// from BusGui
	public void MsgAtBusStop() {
		log.add(new LoggedEvent("Arrived at BusStop."));
		actionComplete.release();
		state = BusState.atBusStop;
		stateChanged();
	}

	// from BusStop
	public void LoadPassengers( List<MyPerson> passengers ) {
		log.add(new LoggedEvent("Received LoadPassengers from BusStop."));
		if (passengers.size() > 0) {
			synchronized(passengers) {
				for (MyPerson mp : passengers) {
					onBoard.add(mp);
				}
			}
		}
		state = BusState.doneLoading;
		stateChanged();
	}

	// from Person
	public void LeavingBus( Person p ) {
		log.add(new LoggedEvent("Received LeavingBus from Person."));
		MyPerson leavingPerson = null;
		synchronized (onBoard) {
			for (MyPerson mp : onBoard) {
				if (mp.p == p) {
					leavingPerson = mp;
					break;
				}
			}
		}
		onBoard.remove(leavingPerson);
	}



	/*
	 * Scheduler
	 */

	public boolean pickAndExecuteAnAction() {

		if (state == BusState.none) {
			startBusRoute();
			return true;
		}

		if (state == BusState.atBusStop) {
			unloadPassengers();
			return true;
		}

		if (state == BusState.doneLoading) {
			continueBusRoute();
			return true;
		}
		
		return false;
	}


	/*
	 * Actions
	 */

	private void startBusRoute() {
		// populate map if necessary
		log.add(new LoggedEvent("Started Bus along BusRoute."));

		state = BusState.driving;
		thisStop = busRoute.get(0);
		stopCounter++;
		
		if(stopCounter == busRoute.size()) {
			stopCounter = 0;
		}
		
		gui.DoGoToNextBusStop(thisStop);
		
		// wait to reach next BusStop
		try {
			actionComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void unloadPassengers() {
		log.add(new LoggedEvent("Unloading passengers."));
		
		synchronized(onBoard) {
			for (MyPerson mp : onBoard) {
				if (mp.destination == thisStop) {
					mp.p.BusAtDestination();
				}
			}
		}
		
		thisStop.BusIsHere(this);
		
		gui.DoUnloadPassengers(); //this will also involve the bus stopping, of course
		
		try {
			actionComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void continueBusRoute() {
		log.add(new LoggedEvent("Continuing on bus route."));
		
		thisStop.BusIsLeaving(this);
		
		state = BusState.driving;
		thisStop = busRoute.get(stopCounter);
		stopCounter++;
		if(stopCounter == busRoute.size()) {
			stopCounter = 0;
		}
		
		gui.DoGoToNextBusStop(thisStop);
		
		try {
			actionComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}


