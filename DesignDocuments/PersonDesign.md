# OrdinaryPerson

```
class OrdinaryPerson extends Agent implements Person 
```

## Data:

```
String name;
double money;
PersonGui gui;
boolean anyTrue;
boolean insideComponent = false;
Map<String, BusStop> busStopMap;
BusStop location;
BusStop destination;
Bus currentBus;

List<Roles> roles;

CustomerState state = none;

enum CustomerState {none, normal, traveling, toHousing, toBank, toMarket, toRestaurant, toBusStop, atHousing, atBank, atMarket, atRestaurant, atBusStop, waitingForBus, busArrived, onBus, atDestination};

enum TransportationType (walk, car, bus};

class Role {
	Person personAgent;
	boolean active = false;
	setPersonAgent();
	getPersonAgent();
}
```

## Messages:

```
/*
 * from GUI control panel
 */
 
ToHousing() {
	state = toHousing;
	stateChanged();
}

ToBank() {
	state = toBank;
	stateChanged();
}

ToRestaurant() {
	state =  toRestaurant;
	stateChanged();
}

ToMarket() {
	state = toMarket;
	stateChanged();
}

ToBusStop(String busStop) {
	state = toBusStop;
	location = busStopMap.get(busStop);
	stateChanged();
}


/*
 * from PersonGui
 */

ArrivedAtHousing() {
	state = atHousing;
	stateChanged();
}

ArrivedAtBank() {
	state = atBank;
	stateChanged();
}

ArrivedAtMarket() {
	state = atMarket;
	stateChanged();
}

ArrivedAtRestaurant() {
	state = atRestaurant;
	stateChanged();
}

ArrivedAtBusStop() {
	state = atBusStop;
	stateChanged();
}

BusIsHere(Bus b) {
	currentBus = b;
	state = busArrived;
	stateChanged();
}

OutOfComponent() {
	insideComponent = false;
	stateChanged();
}
```

## Scheduler:

```
pickAndExecuteAnAction() {
	
	anyTrue = false;
	if ( roles.size != 0 ) {
		for (Role r : roles) {
			if (r.active) {
				anyTrue = (anyTrue) || (r.pickAndExecuteAnAction());
			}
		}
	}
	
	if (state == atBusStop) {
		messageBusStop();
		return true;
	}
	
	if (state == atDestination) {
		exitBus();
		return true;
	}
	
	if (insideComponent) {
		return anyTrue; // don't check scheduler for other outside component actions if inside a component at the moment
	}
	
	if (state == toRestaurant) {
		goToRestaurant();
		return true;
	}

	if (state == toBank) {
		goToBank();
		return true;
	}
	
	if (state == toMarket) {
		goToMarket();
		return true;
	}

	if (state == toHousing) {
		goToHousing();
		return true;
	}

	if (state == toBusStop) {
		goToBusStop();
		return true;
	}

	if (state == atHousing) {
		enterHousing();
		return true;
	}
	
	if (state == atMarket) {
		enterMarket();
		return true;
	}
	
	if (state == atRestaurant) {
		enterRestaurant();
		return true;
	}
	
	if (state == atBank) {
		enterBank();
		return true;
	}
	
	return anyTrue; // nothing left to do, return anyTrue
}
```


## Actions:

```
goToRestaurant() {
	gui.DoGoToRestaurant();
	state = traveling;
}

goToBank() {
	gui.DoGoToBank();
	state = traveling;
}

goToMarket() {
	gui.DoGoToMarket();
	state = traveling;
}

goToHousing() {
	gui.DoGoToHousing();
	state = traveling;
}

goToBusStop() {
	gui.DoGoToBusStop();
	state = traveling;
}

enterHousing() {
	roles.find(HousingCustomerRole);
	if (HousingCustomerRole not found) {
		Role r = new HousingCustomerRole();
		r.sendMessage(); //send initial housing message here
	}
	else {
		r.sendMessage(); //send initial housing message here
	}
	gui.DoEnterHousing();
	insideComponent = true;
	state = normal;
}

enterBank() {
	roles.find(BankingCustomerRole);
	if (BankingCustomerRole not found) {
		Role r = new BankingCustmerRole();
		r.sendMessage(); //send initial housing message here
	}
	else {
		r.sendMessage(); //send initial housing message here
	}
	gui.DoEnterBank();
	insideComponent = true;
	state = normal;
}

enterMarket() {
	roles.find(MarketCustomerRole);
	if (MarketCustomerRole not found) {
		Role r = new MarketCustomerRole();
		r.sendMessage(); //send initial housing message here
	}
	else {
		r.sendMessage(); //send initial housing message here
	}
	gui.DoEnterMarket();
	insideComponent = true;
	state = normal;
}

enterRestaurant() {
	roles.find(RestaurantCustomerRole);
	if (RestaurantCustomerRole not found) {
		Role r = new RestaurantCustomerRole();
		r.sendMessage(); //send initial housing message here
	}
	else {
		r.sendMessage(); //send initial housing message here
	}
	gui.DoEnterRestaurant();
	insideComponent = true;
	state = normal;
}

messageBusStop() {
	location.WantToRide(this);
	state = waitingForBus;
}

boardBus() {
	gui.DoBoardBus();
	insideComponent = true;
	state = onBus;
}

exitBus() {
	currentBus.LeavingBus(this);
	gui.DoExitBus();
	insideComponent = false;
}
```





