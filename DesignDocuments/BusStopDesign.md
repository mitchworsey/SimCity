# BusStop
```
class BusStop extends Agent
```

## Data:

```
BusStopGui gui;
List<MyPerson> waitingPeople = new ArrayList();

class MyPerson {
	Person p;
	BusStop destination;
}

Map<Bus, List<BusStop> > busRoutes; // takes a Bus in, gets out a list of all the stops the bus makes


List<MyBus> buses;

class MyBus {
	Bus b;
	BusState state = none;
}

enum BusState = {none, awaitingBus, busArrived, loading}
```

## Messages:

```
// INSIDE CONSTRUCTOR, POPULATE BUSSTOP WITH ALL KNOWN BUSES THROUGH THAT BUSSTOP

BusIsHere(Bus b) {
	MyBus mb = find(b)
	mb.state = busArrived;
	stateChanged();
}

// from Person

WantToRide(Person p, BusStop bs) {
	waitingPeople.add(new Person(p, bs));
}

BusIsLeaving(Bus b) {
	state = waiting;
}
```

## Scheduler:

```
pickAndExecuteAnAction() {
	
	if (there exists MyBus mb in buses such that mb.state = busArrived) {
		loadPassengers(mb);
		return true;
	}

	return false;
}
```

## Actions:

```
loadPassengers(MyBus mb) {
	mb.state = loading;
	//create deep copy of waitingPeople first;
	List<Person> currentBusPassengers = new ArrayList();
	for (MyPerson mp in waitingPeople) {
		if (mp.destination is in busRoutes.get(mb.b)'s (List<BusStop>) ) {
			currentBusPassengers.add(mp.p);
			waitingPeople.remove(mp);
		}
	}
	currentBus.busPassengers(currentBusPassengers); // this will need to be protected (thread-safe) in case any new customers try to be added to list during deletion
	waitingPeople.removeALL() //pseudocode
}
```


