#team07
======

##SimCity201 Project Repository for Team 007.
+Developed: December 2013


###City: Gui Overview (Marina Hierl) (Garv Manocha) 
+ How to run:
   - Make sure you import all the files into eclipse.
   - When you start the city, you have a control panel on the left side
   - The plus sign is used to create a new person. The user creates the image and name. 
   - The question mark has information about the creation of SimCity. 
   - After creating a person with a name, you should click on the blue square representing that person to get more options of what he/she can do.
   - You can click on locations to see inside (hover over and you can see the names of places). 
      + This includes bus stops. 
   - See GUI section for more details. 


###Market (Christine Song)
======
+ All code for messaging and data updating is set up to be correct
 - Problems may arise due to integration...
+ All needs and products are shown as being picked up from the same location
+ The blue square is the person you are "playing"
+ You should take the bus to get the market
 - The city is unable to take you to the market1 and market2
 - But you can be open the panel and see the layout
+ The cyan square is the grocer, the green square is the cashier
+ The gray cart is where the grocer drops off the customer's products
+ Once the grocer has finished picking up all the customer's needs, the grocer will go back to the customer and give them a bill
+ Once the customer receives a bill and all the needs are met, he/she will go to the pick up cart and then pay for the products
+ I set up the gui's for the Market and my restaurant
+ Delivery Truck
 - Messaging and interaction is correct
 - There are a few issues with being able to know everyone due to the integration.
+ Normative Scenario for Market:
   - Grocer/market has unlimited supply of inventory
   - Customer has unlimited amount of money
   - Customer automatically orders all products (car, milk, bread, eggs)
   - Cashier/register has unlimited amount of money
   - Only restaurant can order a delivery truck of supplies from the market
      + Markets do not receive any outside orders yet
      + Restaurants do not run out of food
   - Customer pays exact amount, cashier should give back change of 0
+ Non-Normative Scenario for Market:
   -  Restaurant orders from the grocer
   - Delivery Truck must be used.
	- Gui for the delivery truck is not complete
	- Gui is set up though, you can check it out!
+ Restaurant5 : The Lab
 - The green square is the user/customers
 - The purple are the waiters
 - Red is the cashier and black is the host
 - Cook doesn't move.
+ They move fast, but it works!


###House (Mitch Worsey)
======
+ All messaging and functionally is correct
+ AStar has been implemented.
+ There are individual Houses as well as four Apartment Complexes with 16 Apartments in each.
+ You can either walk, drive, or take the bus to the houses or apartment complexes.
+ For JUnit testing the Resident, please make sure you comment out the Gui calls for the roles, or else the tests will fail at those respective places(in the action calls).
   - Tests are primitive, just checking to make sure before initial messages and first messages handle correctly.
   - They still served the house testing purposes, but will add more for v2 (especially for non-normative scenarios)
+ Normative Scenario for House: Resident gets Hungry
   - Resident walks to the fridge and gets food.
   - Resident walks to the stove and waits as the food cooks.
   - Resident walks to the table and eats the food.
   - Resident walks to the sink to clean the dishes and then places them in the dishwasher.
   - After this, the resident will leave the house and re-enter simCity.
+ Normative Scenario for House: Resident gets sleepy
   - Resident walks to the bed.
   - After this, the resident will leave the house and re-enter simCity.
+ How to run:
   - Make sure you import all the files into eclipse
   - When you start the city, you have a control panel on the left side
   - The plus sign is to add a person
   - After creating a person and name, you should click on the person to get more options of what he/she can do.
   - You will see the picture of the character and some options of who to call, where to go, and more information. 
   - You can choose where to go with the transportation button (aka “House”).
   - You can click on locations (hover over and you can see the names of places) and see inside the house/apartment.
      - When looking inside an apartment complex, you can then click on the individual apartment to see the person.


###Housing Office (Mitch Worsey)
======
+ All messaging and functionally is correct
+ AStar has been implemented.
+ You can either walk, drive, or take the bus to the housing office.
+ For JUnit testing the Owner, please make sure you comment out the Gui calls for the roles, or else the tests will fail at those respective places(in the action calls).
   - Tests are primitive, just checking to make sure before initial messages and first messages handle correctly.
   - They still served the housing office testing purposes, but will add more for v2 (especially for non-normative scenarios)
+ Normative Scenario for Housing Office: Customer wants to buy a house.
   - Customer walks to the Owner (aka Real Estate Agent).
   - Customer asks the Owner if any property is available.
   - Owner will either:
      - Tell Customer to pay the security deposit for the house.
      - Tell Customer no houses available.
   - After receiving security deposit, Owner will tell Customer to move into the house.
   - After this, the customer will leave the housing office and re-enter simCity.
+ Normative Scenario for Housing Office: House needs maintenance.
   - Owner will receive a message saying a house needs maintenance.
   - Owner will walk to the Maintenance Manager and ask for maintenance.
   - Maintenance Manager will place a work-order for the house.
   - The next day, the work-order will be fulfilled and the house will be maintained.
   - Maintenance Manager will tell Owner to pay maintenance fee.
   - Owner will pay maintenance fee and continue performing Real Estate Agent responsibilities.
+ How to run:
   - Make sure you import all the files into eclipse
   - When you start the city, you have a control panel on the left side
   - The plus sign is to add a person
   - After creating a person and name, you should click on the person to get more options of what he/she can do.
   - You will see the picture of the character and some options of who to call, where to go, and more information. 
   - You can choose where to go with the transportation button (aka “Housing Office”).
   - You can click on locations (hover over and you can see the names of places) and see inside the housing office.


###Bank (Jessica Wang)
======
+ GUIs have not yet implemented A*
   - They currently only move in and out the door in a straight line fashion
+ No waiting line management has been implemented
   - Customers will just stack on each other once they arrive at the Bank Teller
+ The magenta square is the Bank Teller
+ The blue square is the the Bank Customer 
+ The Bank Customer will not do anything inside the bank if you do not give it an action by first clicking on the Customer (the blue square) and then clicking on the button in the control panel (the "$" button)
+ Based on the user's selections through the control panel, the appropriate message is sent to the Bank Teller to address the Customer's request
+ The first time the Customer enters the bank, the only option available is to create a bank account and the other options are disabled
   - For all consecutive times that the Customer enters, the "Create an Account" checkbox is disabled and he may only select from the options in the dropdown (Withdraw, Deposit, Receive Loan)
   - The amount that the Customer enters is sent as a parameter of the message to the Teller
   - If a bad (invalid) input is given (i.e. not a double value), the JFormattedTextField by default takes the last valid input given as the new value
+ The Bank Teller will wait indefinitely for the Bank Customer to give an action through the control panel
+ Normative Scenario for Bank:
   - Customer enters the bank and walks up to the Bank Teller
   - If this is the Customer's first time in the bank, the only option is to create a new bank account. If not, the options are to either withdraw money, deposit money, or receive a loan
   - Once the Customer decides what to do, the Teller handles the request
   - Once the request has been handled, the Customer leaves the bank
+ Integrated second instance of bank
+ Bank robbery scenario
+ To run the bank robbery scenario, create a person through the control panel and name him "rob"
+ Producer consumer code in restaurant
+ Bank Guard, Bank Robber roles and gui 
   
###Person & Transportation & Debugging (Albert Chin)
======
+ Implementation of Person, Bus, BusStop
+ NULL POINTER EXCEPTION + CONCURRENT MODIFICATION EXCEPTION HUNTER & DESTROYER
+ STILL BUSTIN NULL POINTERS AND STUFF
+ Made sure all dependencies were set inside GUI & when new roles made so everyone had pointers to the people they needed to talk to (get rid of Null Pointer Exceptions)
+ All People and Buses have implemented A*
    - Buses move along the one way loop (with two lanes) between the two bus stops.
    - People will use A* to calculate the distance of just walking to a location versus the distance of going to a bus stop and walking from there
	- People will pick to walk or take the bus based on the path calculations.
+ When looking to ride a bus, People will walk to the BusStop
	- They will disappear from the city and appear in the BusStop (clickable component)
	- They will send the BusStop a message letting them know where their desired destination is
	- The BusStop knows all the buses that visit the BusStop, and the routes of all these buses
	- So when a bus arrives, everyone who's desired destination is along that bus's route disappears from the BusStopComponent and is loaded onto the bus
	- When the bus reaches a bus stop it will send a message to everyone on board the bus with the current bus stop
	- If the bus stop matches the person's destination bus stop, the person will leave the bus and let the bus know to remove them from the on board list
+ People can drive now (CARS IMPLEMENTED)
+ A* IMPROVEMENTS TO DETECT COLLISIONS AND AVOID
+ Coded in NON-NORMATIVES OF VEHICLE-PEDESTRIAN AND VEHICLE-VEHICLE CRASH
+ People have basic AI functions implemented 
	- They share a global timeOfDay clock with the rest of the city
	- They have a hungerLevel that increments itself every few seconds
	- They have a tiredLevel that increments itself every few seconds
	- If given no commands, they will check their hungerLevel/tiredLevel/moneyAmount/houseStock
	- If hungry, they will automatically pick a restaurant and go to it (Only Restaurant1 is functional at the moment so it will always go to Restaurant1)
	- If tired, they will go to their home to sleep
	- If low on money, they will go to the bank
	- If their houseStock is low they will go to the market
	- Otherwise, person can be given commands to go everywhere
+ We have a system where the user can "control" the people in the city
	- Move and enter messages split, so when a person is automatically moving somewhere, the person can interrupt movement/override automatic AI with a personal command
+ The blue squares are the people
+ The magenta square is the bus
+ When people enter a component, they disappear from the CityPanel and appear in the ComponentPanel that they enterred
+ When people exit a component, they reappear where they enterred the CityPanel from and are set back to the "normal" (idle) state
	- In their "normal" (idle) state, if given no commands, they will start checking their hungerLevel/tiredLevel/money/houseStock to see if they need to do anything
+ Implemented role creation/activation and corresponding GUI creation for each of the active roles whenever someone enters a component
+ UPDATED ORDINARYPERSON TO WORK WITH NEW CONFIGURATION




Gui & Aesthetic (Marina Hierl) (Garv Manocha)
======
+ The main GUI window contains the control panel, city view panel, and panels for the inside view of every building.
+ There is a smallCityPanel that displays in the lower right when a buildingpanel is being viewed. The map and movements of bus/people are accurate on the small map. 
+ There is a grid (two dimensional array of booleans) for each view panel (city, restaurant, market, bank, and house).
+ The a* algorithm is completely functional to navigate the grid. It does not contain any of the a* that was given to us. 
+ The a* uses a boolean grid to return the most efficient traveling path amongst the permanent fixtures (buildings, roads, etc). A semaphore grid is used for each GUI to acquire every node (10 pixels) that they walk on. If there are no available permits (there are only one for each square) they wait for it. In order to avoid collisions, if they wait for too long, they release their square and push the other GUI aside. 
+ (Not every building panel mapped out a complete grid)
+ People in the city can walk on the sidewalks and on the crosswalks, but nowhere else. 
+ Buses can drive on the main road, and cars can drive on the road and roads leading to parking garages. All cars and buses stop at the stop lights. Cars and busses will stop and wait for people on the crosswalks. 
+ Control Panel:
   - The "?" button on the bottom left opens a JOptionWindow with information about the game.
   - The "+" button on the bottom left opens a window, directing the user for a name, which creates a new Person in SimCity.
   - The person information is only visible when a person is selected (clicked on).
   - The "i" button displays information about the person.
   - The telephone button will be used by the person to order items from the market. It is not implemented yet.
   - The grocery button is used to select a list of grocery items from the market, and is only enabled when the customer goes to the Market.
   - The bank button is used to complete actions in the bank and is only enabled when the person goes to the bank. 
   - Both the grocery button and bank button were created by the market and bank creators, respectively 
+ garv:
+ built the global clock, synced with all the persons in the city.
+ this same clock is synced with elements in the gui - the city's daylight level changes according to the time of day…
+ people are small icons in the city view that change to conform to the gender and color specified...
+ When the buildings are moused over, the name of the building is displayed (implemented by Garv)
+ unifying a lot of images and organizing a lot of the code

Restaurant1 (Marina Hierl)
======
+ All messaging and functionality is correct.
+ The guis inside restaurant1 use a* to navigate paths.
+ People can run into eachother.
+ The gui has no control over restaurant functions. The Restaurant1Role enters the restaurant, decides a random food, eats, and pays on its own system.
+ The restaurant does not yet interact with the bank or market. 
+ As instructed, there is no JUnit testing for restaurant components in SimCity. 
+ How to run: 
   - Instruct the people in the city view to go to restaurant1
   - Click on "Restaurant1" to get an inside view of the building and watch the customers, waiter, cook, host, and cashier interact
+ Our team has only implemented one restaurant (Marina's): Restaurant1
   - The spaces for Restaurant2, Restaurant3, Restaurant4, and Restaurant5 are to be implemented and functioning for v2 
   - While you can choose to move to those restaurants, you cannot run normal restaurant functions
	+ The character stands outside the restaurant and cannot go in.

+ For more information about our team/roles and other, please go to our wiki.
+ If you are concerned about the meeting minutes, please note that they are not accurate. If you have inquiries about our meeting schedule, agendas, we do have documentation that has not been updated on the wiki. 
+ If you have any issues or questions, feel free to email any/all of us!
