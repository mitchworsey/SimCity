##Animation oversight and GUI;

This will be handled through the Java AWT and Swing toolsets. Each type of SpecializedPerson (RestaurantCustomer, BankCustomer, BankTeller, everything and etc) will have their own SpecializedPersonGUI (that could inherit from an AgentGUI class) that will relay messages to and from that agent to handle its animation. Each of these GUI will include a gui class that will be added to a list in an AnimationPanel-type class... This class will display these characters into a CityPanel and a BuildingPanel. The city panel shows the overview of the city and the building panel has an overview of the currently selected building.

All of this will work in tandem with a ControlPanel class that will handle all the user input... Where the player of the game will actually get to "play" through our SimCity. These panels will be contained within a SimCity class that will contain a main function that will execute all the code.\par

-Garv Manocha 