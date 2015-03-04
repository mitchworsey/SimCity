# Bus

```
class Bus extends Agent
```

## Data:

```
List<Person> onBoard;
BusGui gui;
Map<Integer, BusStop> busRoute; // will be populated with all the BusStops bus should be at
int stopCounter = 0;
BusStop nextStop;

BusState state = none;

enum BusState {none, driving, atBusStop, doneUnloading, doneLoading}
```

## Messages:

```
// from BusGui
AtBusStop() {
	state = atBusStop;
	stateChanged();
}

LoadPassengers( List<Person> passengers ) {
	for (Person p : passengers) {
		onBoard.add(p);
	}
	stateChanged();
	state = doneLoading;
	stateChanged();
}

LeavingBus( Person p ) {
	onBoard.remove(p);
}
```

## Scheduler:

```
pickAndExecuteAnAction() {

	if (state == normal) {
		startBusRoute();
		return true;
	}

	if (state == atBusStop) {
		unloadPassengers();
		return true;
	}

	if (state == doneLoading) {
		continueBusRoute();
		return true;
	}
	
	return false;
}
```

## Actions:

```
startBusRoute() {
	gui.DoGoToNextBusStop();
	// populate map if necessary
	state = driving;
	nextStop = Map.get(0);
	stopCounter++;
}

atBusStop() {
	state = doneUnloading;
	gui.DoUnloadPassengers(); //this will also involve the bus stopping, of course
	nextStop.BusIsHere(this);
}

continueBusRoute() {
	nextStop.BusIsLeaving();
	gui.DoGoToNextBusStop();
	state = driving;
	nextStop = Map.get(stopCounter);
	stopCounter++;
}
```
