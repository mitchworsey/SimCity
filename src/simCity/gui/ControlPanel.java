package simCity.gui;

import javax.swing.*;

import simCity.BankComponent;
import simCity.BusAgent;
import simCity.BusStopAgent;
import simCity.BusStopComponent;
import simCity.CityComponent;
import simCity.HousingComponent;
import simCity.HousingOfficeComponent;
import simCity.Location;
import simCity.MarketComponent;
import simCity.OrdinaryPerson;
import simCity.Restaurant1Component;
import simCity.Restaurant2Component;
import simCity.Restaurant3Component;
import simCity.Restaurant4Component;
import simCity.Restaurant5Component;
import simCity.Role;
import simCity.Restaurant1.Restaurant1CustomerRole;
import simCity.Restaurant3.Restaurant3CashierRole;
import simCity.Restaurant3.Restaurant3CustomerRole;
import simCity.Restaurant3.Restaurant3HostRole;
import simCity.Restaurant3.gui.Restaurant3CookGui;
import simCity.Restaurant3.gui.Restaurant3CustomerGui;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;
import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;
import simCity.bank.BankCustomerRole;
import simCity.bank.BankGuardRole;
import simCity.bank.BankRobberRole;
import simCity.bank.BankTellerRole;
import simCity.gui.Restaurant1.Restaurant1CookGui;
import simCity.gui.Restaurant1.Restaurant1CustomerGui;
import simCity.gui.Restaurant1.Restaurant1WaiterGui;
import simCity.house.HouseCustomerRole;
import simCity.house.HouseMaintenanceManagerRole;
import simCity.house.HouseOwnerRole;
import simCity.house.HouseResidentRole;
import simCity.interfaces.*;
import simCity.market.MarketCashierRole;
import simCity.market.MarketCustomerRole;
import simCity.market.MarketCustomerRole.Need;
import simCity.market.MarketDeliveryTruck;
import simCity.market.MarketGrocerRole;
import simCity.restaurant2.gui.Restaurant2CookGui;
import simCity.restaurant2.gui.Restaurant2CustomerGui;
import simCity.restaurant2.gui.Restaurant2WaiterGui;
import simCity.restaurant2.Restaurant2CookRole;
import simCity.restaurant2.Restaurant2CustomerRole;
import simCity.restaurant2.Restaurant2WaiterRole;
import simCity.restaurant2.interfaces.Restaurant2Cashier;
import simCity.restaurant2.interfaces.Restaurant2Cook;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Host;
import simCity.restaurant2.interfaces.Restaurant2Waiter;
import simCity.restaurant4.Restaurant4CustomerRole;
import simCity.restaurant4.gui.Restaurant4CookGui;
import simCity.restaurant4.gui.Restaurant4CustomerGui;
import simCity.restaurant4.gui.Restaurant4WaiterGui;
import simCity.restaurant4.*;
import simCity.restaurant4.interfaces.*;
import simCity.restaurant5.Restaurant5CustomerRole;
import simCity.restaurant5.gui.*;
import simCity.restaurant5.interfaces.Restaurant5Customer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.*;

/**
 * Panel in frame that contains all the SimCity information.
 */
public class ControlPanel extends JPanel implements ActionListener {
	static final int BUTTONSIZE = 50; //static
	
	public Vector<OrdinaryPerson> people = new Vector<OrdinaryPerson>();

	private final JList<JCheckBox> marketCBList = new JList<JCheckBox>();
	private int groceryOptions = 4;
	private JCheckBox[] checkBoxes = new JCheckBox[groceryOptions];
	private ArrayList<String> grocerylist = new ArrayList<String>();
	public boolean glSelected = false;
        
	
	private int rest3CustomerGuiYPos = 10;
    private int rest3CustomerGuiXPos = 10;
    private int rest3WaiterXWatchPos = 80;
    private int rest4WaiterPos = 1;
    private int rest1WaiterPos = 1;
        
    private SimCityGui gui; //reference to main gui

    private Timer timer = new Timer();
    private boolean redLight;
    //buttons
    JButton transportation;
    JButton information;
    JButton marketGroceryList;
    JButton bankOptions;
    JButton phone;
    
    //optionpanes
    private JOptionPane infoPane;
    private JOptionPane transportationPane;
    
    private JFrame marketGroceryListFrame;
    private JPanel marketGroceryListPanel;
    private JOptionPane marketGroceryListPane;
	private JCheckBox carCB = new JCheckBox("Car");
    private JCheckBox breadCB = new JCheckBox("Bread");
    private JCheckBox eggsCB = new JCheckBox("Eggs");
    private JCheckBox milkCB = new JCheckBox("Milk");
    private List<JCheckBox> marketSelectOptions;
    
    private JPanel bankOptionsPanel;
    private JFormattedTextField amountField;
	private NumberFormat amountFormat;
	private double amount = 1000;
	private JPanel bankAmountPanel;
	private JLabel amountLabel;
	private JCheckBox createAcct;
	private List<String> bankSelectOptions;
    
    private ImageIcon buttonInfoIcon = new ImageIcon("src/simCity/gui/images/controlPanel/buttonInfo.png");
    private Image buttonInfoImage = buttonInfoIcon.getImage() ;  
    private Image buttonInfoIconImage2 = buttonInfoImage.getScaledInstance( BUTTONSIZE, BUTTONSIZE,  java.awt.Image.SCALE_SMOOTH );  
    private ImageIcon buttonTransportIcon = new ImageIcon("src/simCity/gui/images/controlPanel/buttonTransport.png");
    private Image buttonTransportImage = buttonTransportIcon.getImage() ;  
    private Image buttonTransportIconImage2 = buttonTransportImage.getScaledInstance( BUTTONSIZE, BUTTONSIZE,  java.awt.Image.SCALE_SMOOTH ); 
    private ImageIcon buttonBankIcon = new ImageIcon("src/simCity/gui/images/controlPanel/bank.png");
    private Image buttonBankImage = buttonBankIcon.getImage();
    private Image buttonBankIconImage2 = buttonBankImage.getScaledInstance( BUTTONSIZE, BUTTONSIZE,  java.awt.Image.SCALE_SMOOTH );
    private ImageIcon buttonMarketIcon = new ImageIcon("src/simCity/gui/images/controlPanel/market.png");
    private Image buttonMarketImage = buttonMarketIcon.getImage();
    private Image buttonMarketIconImage2 = buttonMarketImage.getScaledInstance( BUTTONSIZE, BUTTONSIZE,  java.awt.Image.SCALE_SMOOTH );
    
    
    
    //Map of buildings
    public Map<String, CityComponent> buildingEntrances = new HashMap<String, CityComponent>();
    private Location house1Entrance;
    private Location house2Entrance;
    private Location apartment1Entrance;
    private Location apartment2Entrance;
    private Location apartment3Entrance;
    private Location apartment4Entrance;
    private Location housingOfficeEntrance;
    private Location bankEntrance; 
    private Location bank1Entrance;
    private Location restaurant1Entrance;
    private Location restaurant2Entrance;
    private Location restaurant3Entrance;
    private Location restaurant4Entrance;
    private Location restaurant5Entrance;
    private Location marketEntrance; 
    private Location market1Entrance; 
    private Location market2Entrance; 
    private Location busStop1Entrance;
    private Location busStop2Entrance;
    private Location parkingGarage1Entrance;
    private Location parkingGarage2Entrance;
    private CityComponent buildingComponent;
    
    //Busstop stuff
    private Bus bus1;
    private BusStopAgent busStop1;
    private BusStopAgent busStop2;
    private BusGui busGui1;
    private List<Bus> busList = Collections.synchronizedList(new ArrayList<Bus>());
    
    /* Delivery Truck Stuff */
    private MarketDeliveryTruck dtruck;
    private DeliveryTruckGui dtGui;
   

    public ControlPanel(){//SimCityGui gui) {
        //this.gui = gui;
        redLight = false;
        stopLightTimer();
        setLayout(new GridLayout(5, 1, 0, 5));
        setBackground(Color.WHITE);
        
        information = new JButton();
        buttonInfoIcon = new ImageIcon( buttonInfoIconImage2 );
        information.addActionListener(this);
        information.setIcon(buttonInfoIcon);
        information.setVisible(true);
        information.setPreferredSize(new Dimension(50, 50));
        add(information);
        infoPane = new JOptionPane();
        
        transportation = new JButton();
        transportation.addActionListener(this);
        buttonTransportIcon = new ImageIcon(buttonTransportIconImage2);
        transportation.setIcon(buttonTransportIcon);
        transportation.setVisible(true);
        transportation.setPreferredSize(new Dimension(50,50));
        add(transportation);
        transportationPane = new JOptionPane();
        
        /* MARKET BUTTON */
        buttonMarketIcon = new ImageIcon(buttonMarketIconImage2);
        marketGroceryList = new JButton();
        marketGroceryList.setIcon(buttonMarketIcon);
        marketGroceryList.setVisible(true);
        marketGroceryList.setEnabled(false);
        marketGroceryList.addActionListener(this);
        marketGroceryList.setPreferredSize(new Dimension(50,50));
        add(marketGroceryList);
        marketGroceryListPane = new JOptionPane();
        
        
        /* MARKET */
        JButton carButton = new JButton("Car");
        carButton.setSelected(false);
        JButton breadButton = new JButton("Bread");
        breadButton.setSelected(false);
        JButton eggsButton = new JButton("Eggs");
        eggsButton.setSelected(false);
        JButton milkButton = new JButton("Milk");
        milkButton.setSelected(false);
        
        marketGroceryListPane.add(carButton);
        marketGroceryListPane.add(breadButton);
        marketGroceryListPane.add(milkButton);
        marketGroceryListPane.add(eggsButton);
        
        marketGroceryListPanel = new JPanel();
        marketGroceryListPanel.setLayout(new BoxLayout(marketGroceryListPanel, BoxLayout.X_AXIS));
        marketGroceryListPanel.add(carCB);
        marketGroceryListPanel.add(breadCB);
        marketGroceryListPanel.add(milkCB);
        marketGroceryListPanel.add(eggsCB);
        
        /* BANK */
        bankOptionsPanel = new JPanel();
        bankOptionsPanel.setLayout(new BoxLayout(bankOptionsPanel, BoxLayout.Y_AXIS));
        bankOptions = new JButton();
        bankOptions.setVisible(true);
        bankOptions.setEnabled(false);
        buttonBankIcon = new ImageIcon(buttonBankIconImage2);
        bankOptions.addActionListener(this);
        bankOptions.setIcon(buttonBankIcon);
        bankOptions.setPreferredSize(new Dimension(50, 50));
        add(bankOptions);   
        bankAmountPanel = new JPanel();
    	amountLabel = new JLabel("$");
    	amountFormat = NumberFormat.getNumberInstance();
    	amountField = new JFormattedTextField(amountFormat);
    	amountField.setValue(new Double(amount));
    	amountField.setColumns(10);	
    	bankAmountPanel.setAlignmentX(LEFT_ALIGNMENT);
    	bankAmountPanel.add(amountLabel);
    	bankAmountPanel.add(amountField);
    	createAcct = new JCheckBox("Create an Account");
    	bankOptionsPanel.add(createAcct);
    	bankOptionsPanel.add(bankAmountPanel);
        
    	//BusStop stuff
    	bus1 = new BusAgent("Bus1");
    	busList.add(bus1);
    	busStop1 = new BusStopAgent("BusStop1", busList);
    	busStop2 = new BusStopAgent("BusStop2", busList);
    	((BusAgent) bus1).populateMap(0, busStop1);
    	((BusAgent) bus1).populateMap(1, busStop2);
    	List<BusStop> bus1Route = new ArrayList<BusStop>();
    	bus1Route.add(busStop1);
    	bus1Route.add(busStop2);
    	busStop1.addBusRoute(bus1, bus1Route);
    	busStop2.addBusRoute(bus1, bus1Route);
    	
    	busGui1 = new BusGui(bus1, gui, 700, 150);
    	((BusAgent) bus1).setGui(busGui1);
    	
    	busGui1.addBusStopLocation(busStop1, 220, 415);
    	busGui1.addBusStopLocation(busStop2, 690, 150);
    	
   
        //Building map
    	house1Entrance = new Location(230, 520);
    	house2Entrance = new Location(140, 230);
    	apartment1Entrance = new Location(325, 520);
    	apartment2Entrance = new Location(415, 520);
    	apartment3Entrance = new Location(510, 520);
    	apartment4Entrance = new Location(610, 520);
    	housingOfficeEntrance = new Location(150, 520);
        bankEntrance = new Location(780, 240);
        bank1Entrance = new Location(364, 364);
        restaurant1Entrance = new Location(130, 180);
        restaurant2Entrance = new Location(350, 80);
        restaurant3Entrance = new Location(460, 80);
        restaurant4Entrance = new Location(560,80);
        restaurant5Entrance = new Location(660, 80);
        marketEntrance = new Location(140, 365);
        market1Entrance = new Location(605, 305);
        market2Entrance = new Location(741, 90);
        busStop1Entrance = new Location(190, 390);
        busStop2Entrance = new Location(730, 110);
        parkingGarage1Entrance = new Location(140, 55);
        parkingGarage2Entrance = new Location(800, 510);
        buildingComponent = new HousingComponent("House1", house1Entrance, "house1");
        buildingEntrances.put("House1", buildingComponent);
        buildingComponent = new HousingComponent("House2", house2Entrance, "house2");
        buildingEntrances.put("House2", buildingComponent);
        buildingComponent = new HousingComponent("Apartment1", apartment1Entrance, "apartment1");
        buildingEntrances.put("Apartment1", buildingComponent);
        buildingComponent = new HousingComponent("Apartment2", apartment2Entrance, "apartment2");
        buildingEntrances.put("Apartment2", buildingComponent);
        buildingComponent = new HousingComponent("Apartment3", apartment3Entrance, "apartment3");
        buildingEntrances.put("Apartment3", buildingComponent);
        buildingComponent = new HousingComponent("Apartment4", apartment4Entrance, "apartment4");
        buildingEntrances.put("Apartment4", buildingComponent);
        buildingComponent = new HousingOfficeComponent("HouseOffice", housingOfficeEntrance, "houseOffice");
        buildingEntrances.put("HousingOffice", buildingComponent);
        buildingComponent = new BankComponent("Bank", bankEntrance, "bank");
        buildingEntrances.put("Bank", buildingComponent);
        buildingComponent = new BankComponent("Bank1", bank1Entrance, "bank1");
        buildingEntrances.put("Bank1", buildingComponent);
        buildingComponent = new Restaurant1Component("Restaurant1", restaurant1Entrance, "restaurant1");
        buildingEntrances.put("Restaurant1", buildingComponent);
        buildingComponent = new Restaurant2Component("Restaurant2", restaurant2Entrance, "restaurant2");
        buildingEntrances.put("Restaurant2", buildingComponent);
        buildingComponent = new Restaurant3Component("Restaurant3", restaurant3Entrance, "restaurant3");
        buildingEntrances.put("Restaurant3", buildingComponent);
        buildingComponent = new Restaurant4Component("Restaurant4", restaurant4Entrance, "restaurant4");
        buildingEntrances.put("Restaurant4", buildingComponent);
        buildingComponent = new Restaurant5Component("Restaurant5", restaurant5Entrance, "restaurant5");
        buildingEntrances.put("Restaurant5", buildingComponent);
        buildingComponent = new MarketComponent("Market", marketEntrance, "Market");
        buildingEntrances.put("Market", buildingComponent);
        buildingComponent = new MarketComponent("Market1", market1Entrance, "Market1");
        buildingEntrances.put("Market1", buildingComponent);
        buildingComponent = new MarketComponent("Market2", market2Entrance, "Market2");
        buildingEntrances.put("Market2", buildingComponent);
        buildingComponent = new BusStopComponent(busStop1, "BusStop1", busStop1Entrance, "busstop1");
        buildingEntrances.put("BusStop1", buildingComponent);
        buildingComponent = new BusStopComponent(busStop2, "BusStop2", busStop2Entrance, "busstop2");
        buildingEntrances.put("BusStop2", buildingComponent);
        buildingComponent = new CityComponent("ParkingGarage1", parkingGarage1Entrance, "parkinggarage1");
        buildingEntrances.put("ParkingGarage1", buildingComponent);
        buildingComponent = new CityComponent("ParkingGarage2", parkingGarage2Entrance, "parkinggarage2");
        buildingEntrances.put("ParkingGarage2", buildingComponent);
    
    
    
//     	
//    	/* ~~~~~~ DeliveryTruck stuff ~~~~~~~ */
//    	dtruck = new MarketDeliveryTruck("Delivery Truck");
//    	//MARKET
//    	((MarketDeliveryTruck) dtruck).populateMap(0, buildingEntrances.get("Restaurant2Market"));
//    	//RESTAURANT 1
//    	((MarketDeliveryTruck) dtruck).populateMap(1, buildingEntrances.get("Restaurant1"));
//    	//RESTAURANT 2
//    	((MarketDeliveryTruck) dtruck).populateMap(2, buildingEntrances.get("Restaurant2"));
//    	//RESTAURANT 3
//    	((MarketDeliveryTruck) dtruck).populateMap(3, buildingEntrances.get("Restaurant3"));
//    	//RESTAURANT 4
//    	((MarketDeliveryTruck) dtruck).populateMap(4, buildingEntrances.get("Restaurant4"));
//    	//RESTAURANT 5
//    	((MarketDeliveryTruck) dtruck).populateMap(5, buildingEntrances.get("Restaurant5"));
//    	
    	
    }
    
    
    
    public void setGui(SimCityGui gui){
    	this.gui = gui;
    	busStop1.startThread();
    	busStop2.startThread();
    	((BusAgent) bus1).startThread();
    	//dtruck.startThread();
    }
    
    
    
    public void addCityGuis() {
    	SimCityGui.cityPanel.addGui(busGui1);
    	//SimCityGui.cityPanel.addGui(dtGui);
    }
    
    /**
     * Action listener method that reacts to things being clicked
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == information){
        	if(gui.currentPersonGui instanceof PersonGui){
                JOptionPane.showMessageDialog(gui,
                		("This is " + ((PersonGui)gui.currentPersonGui).agent.getName() + ". "),
                		"Information",
                       JOptionPane.PLAIN_MESSAGE);
            }
        	else if(gui.currentPersonGui instanceof MarketCustomerGui){
                JOptionPane.showMessageDialog(gui,
                		("This is " + ((MarketCustomerRole)((MarketCustomerGui)gui.currentPersonGui).mcust).personAgent.getName() + ". "),
                		"Information",
                       JOptionPane.PLAIN_MESSAGE);
            }
        	else if(gui.currentPersonGui instanceof BankCustomerGui){
                JOptionPane.showMessageDialog(gui,
                		("This is " + ((BankCustomerRole)((BankCustomerGui)gui.currentPersonGui).customer).personAgent.getName() + ". "),
                		"Information",
                       JOptionPane.PLAIN_MESSAGE);
            }
        	else if(gui.currentPersonGui instanceof Restaurant1CustomerGui){
                JOptionPane.showMessageDialog(gui,
                		("This is " + ((Restaurant1CustomerRole)((Restaurant1CustomerGui)gui.currentPersonGui).agent).personAgent.getName() + ". "),
                		"Information",
                       JOptionPane.PLAIN_MESSAGE);
            }
        	else if(gui.currentPersonGui instanceof HouseResidentGui){
                JOptionPane.showMessageDialog(gui,
                		("This is " + ((HouseResidentRole)((HouseResidentGui)gui.currentPersonGui).role).personAgent.getName() + ". "),
                		"Information",
                       JOptionPane.PLAIN_MESSAGE);
            }
        }
        if (e.getSource() == transportation) {
        	Object[] possibilities = {"Market", "Market1", "Market2", "Restaurant1", "Restaurant2", "Restaurant3", "Restaurant4", "Restaurant5", "Bank", "Bank1", "Home", "Housing Office"};
        	String s = (String)JOptionPane.showInputDialog(gui,
                        "Where would you like to go?",
                        "Transportation",
                        JOptionPane.PLAIN_MESSAGE,
                        buttonTransportIcon, 
                        
                        possibilities,
                        "Home");
        	if ((s != null) && (s.length()>0)) {
        		if (s.equals("Home")){
        			((PersonGui)gui.currentPersonGui).agent.ToHousing();
        		}
        		else if (s.equals("Restaurant1")){
        			((PersonGui)gui.currentPersonGui).agent.ToRestaurant1();
        		}
        		else if (s.equals("Restaurant2")){
        			((PersonGui)gui.currentPersonGui).agent.ToRestaurant2();
        		}
        		else if (s.equals("Restaurant3")){
        			((PersonGui)gui.currentPersonGui).agent.ToRestaurant3();
        		}
        		else if (s.equals("Restaurant4")){
        			((PersonGui)gui.currentPersonGui).agent.ToRestaurant4();
        		}
        		else if (s.equals("Restaurant5")){
        			((PersonGui)gui.currentPersonGui).agent.ToRestaurant5();
        		}
        		else if (s.equals("Bank")){
        			((PersonGui)gui.currentPersonGui).agent.ToBank();
        		}
        		else if (s.equals("Bank1")){
        			Role r = ((OrdinaryPerson)((PersonGui)gui.currentPersonGui).agent).RoleFactory("Bank1Teller");
        			((PersonGui)gui.currentPersonGui).agent.ToBank(r);
        		}
        		else if (s.equals("Market")){
        			((PersonGui)gui.currentPersonGui).agent.ToMarket();
        		}
           		else if (s.equals("Market1")){
        			((PersonGui)gui.currentPersonGui).agent.ToMarket1();
        		}
           		else if (s.equals("Market2")){
        			((PersonGui)gui.currentPersonGui).agent.ToMarket2();
        		}
        		else if (s.equals("Housing Office")){
        			((PersonGui)gui.currentPersonGui).agent.ToHousingOffice();
        		}
        	}
        }
        if (e.getSource() == phone) {
                JOptionPane.showMessageDialog(gui,
                        "Who would you like to call?",
                        "Telephone",
                        JOptionPane.PLAIN_MESSAGE);
        }
        /*~~~~~ MARKET GROCERY LIST ~~~~~*/
        if (e.getSource() == marketGroceryList) { 
        	MarketCustomerRole mcr = (MarketCustomerRole)((MarketCustomerGui)gui.currentPersonGui).mcust;
            JCheckBox carCB = new JCheckBox("Car");
            carCB.setSelected(false);
            marketCBList.add(carCB);
            checkBoxes[0] = carCB;
            JCheckBox breadCB = new JCheckBox("Bread");
            breadCB.setSelected(false);
            marketCBList.add(breadCB);
            checkBoxes[1] = breadCB;
            JCheckBox eggsCB = new JCheckBox("Eggs");
            eggsCB.setSelected(false);
            marketCBList.add(eggsCB);
            checkBoxes[2] = eggsCB;
            JCheckBox milkCB = new JCheckBox("Milk");
            milkCB.setSelected(false);
            marketCBList.add(milkCB);
            checkBoxes[3] = milkCB;
            
            carCB.addActionListener(this);
            breadCB.addActionListener(this);
            milkCB.addActionListener(this);
            eggsCB.addActionListener(this);
            
                marketGroceryListPanel = new JPanel();
                marketGroceryListPanel.setAlignmentY(CENTER_ALIGNMENT);
                marketGroceryListPanel.add(carCB);
                marketGroceryListPanel.add(breadCB);
                marketGroceryListPanel.add(milkCB);
                marketGroceryListPanel.add(eggsCB);

                  
                JOptionPane.showMessageDialog(null, marketGroceryListPanel, "What would you like to order?",
                		JOptionPane.PLAIN_MESSAGE);  
                if(e.getSource() == carCB) {
                	if(carCB.isSelected())
                		mcr.myNeeds.add(mcr.new Need("Car"));
                }
                if(e.getSource() == breadCB) {
                	if(breadCB.isSelected())
                		mcr.myNeeds.add(mcr.new Need("Bread"));
                }
                if(e.getSource() == milkCB) {
                	if(milkCB.isSelected())
                		mcr.myNeeds.add(mcr.new Need("Milk"));
                }
                if(e.getSource() == eggsCB) {
                	if(eggsCB.isSelected())
                		mcr.myNeeds.add(mcr.new Need("Eggs"));
                }

            
            
            glSelected = true;
         
              mcr.IAmInNeed();
        }
        if (e.getSource() == bankOptions) {
        	BankCustomerRole bcr = (BankCustomerRole)((BankCustomerGui)gui.currentPersonGui).customer;
        	bankSelectOptions = new ArrayList<String>();
        	bankSelectOptions.add("Withdraw");
        	bankSelectOptions.add("Deposit");
        	bankSelectOptions.add("Receive Loan");
        	
        	if(bcr.accountNum != 0) {
        		createAcct.setSelected(false);
        		createAcct.setEnabled(false);
        		amountField.setEnabled(true);
        	}
        	else {
        		createAcct.setSelected(true);
        		createAcct.setEnabled(true);
        		amountField.setEnabled(false);
        		bankSelectOptions.clear();
        	}
        	Object[] options = bankSelectOptions.toArray();
        	String s = (String)JOptionPane.showInputDialog(gui,
        			bankOptionsPanel,
                    "What would you like to do?",
                    JOptionPane.PLAIN_MESSAGE,
                    buttonBankIcon,
                    options,
                    "Withdraw");
        	if(createAcct.isSelected()) {
        		((BankCustomerGui) gui.currentPersonGui).DoGoToTeller();
        		bcr.msgCreateBankAccount();
    		}
        	else if ((s != null) && (s.length()>0)) {
        		Number num = (Number)amountField.getValue();
        		double amt = num.doubleValue();
        		((BankCustomerGui) gui.currentPersonGui).DoGoToTeller();
        		if (s.equals("Withdraw")) {
        			bcr.msgWithdrawFromBank(amt);
        		}
        		else if (s.equals("Deposit")) {
        			bcr.msgMakeDepositFromBank(amt);
        		}
        		else if (s.equals("Receive Loan")) {
        			bcr.msgMakeLoanFromBank(amt);
        		}
        		//this.setEnabled(false);
        	}
        }
    }
    
    public void updateButtons(String location){
    	if (location.equals("House")){
    		marketGroceryList.setEnabled(false);
    		transportation.setEnabled(false);
    		bankOptions.setEnabled(false);
    	}
    	else if (location.equals("Bank")){
    		marketGroceryList.setEnabled(false);
    		transportation.setEnabled(false);
    		bankOptions.setEnabled(true);
    	}
    	else if (location.equals("Market")){
    		marketGroceryList.setEnabled(true);
    		transportation.setEnabled(false);
    		bankOptions.setEnabled(false);
    	}
    	else if (location.equals("Restaurant")){
    		marketGroceryList.setEnabled(false);
    		transportation.setEnabled(false);
    		bankOptions.setEnabled(false);
    	}
    	else if (location.equals("Main")){
    		marketGroceryList.setEnabled(false);
    		transportation.setEnabled(true);
    		bankOptions.setEnabled(false);
    	}
    	else {
    		marketGroceryList.setEnabled(false);
    		transportation.setEnabled(true);
    		bankOptions.setEnabled(false);
    	}
    }
    
    public void makeNewRoleGui(Role r) {
    	if (r instanceof MarketCustomerRole) {
    		//SimCityGui.marketPanel.addGui(((MarketCustomerRole) r).gui);
    		MarketCustomerGui mcg = new MarketCustomerGui((MarketCustomerRole) r, gui);
    		mcg.setPresent(true);
    		((MarketCustomerRole) r).setGui(mcg);
    		
    		/*
    		((MarketCustomerRole) r).setGrocer(SimCityGui.marketPanel.grocer);
    		((MarketCustomerRole) r).setCashier(SimCityGui.marketPanel.cashier);
    		*/
    		
    		if (r.getComponentPanel() == SimCityGui.marketPanel) {
    			SimCityGui.marketPanel.newMarketCustomer(r);
        		SimCityGui.marketPanel.addGui(mcg);
    		}
    		
    		else if (r.getComponentPanel() == SimCityGui.market1Panel) {
    			SimCityGui.market1Panel.newMarketCustomer(r);
        		SimCityGui.market1Panel.addGui(mcg);
    		}
    		
    		else if (r.getComponentPanel() == SimCityGui.market2Panel) {
    			SimCityGui.market2Panel.newMarketCustomer(r);
        		SimCityGui.market2Panel.addGui(mcg);
    		}
    		mcg.setNeedy();
//    		((MarketCustomerRole) r).IAmInNeed(grocerylist);

    		updateButtons("Market");
    	}
    	else if (r instanceof MarketGrocerRole) {
    		MarketGrocerGui mgg = new MarketGrocerGui((MarketGrocerRole) r, gui);
    		mgg.setPresent(true);
    		((MarketGrocerRole) r).setGui(mgg);
    		
    		if (r.getComponentPanel() == SimCityGui.marketPanel){
    			SimCityGui.marketPanel.addGui(mgg);
    		}
    		else if (r.getComponentPanel() == SimCityGui.market1Panel){
    			SimCityGui.market1Panel.addGui(mgg);
    		}
    		else if (r.getComponentPanel() == SimCityGui.market2Panel){
    			SimCityGui.market2Panel.addGui(mgg);
    		}
    	}
    	else if (r instanceof MarketCashierRole) {
    		MarketCashierGui mcg = new MarketCashierGui((MarketCashierRole) r, gui);
    		mcg.setPresent(true);
    		((MarketCashierRole) r).setGui(mcg);
    		
    		if (r.getComponentPanel() == SimCityGui.marketPanel) {
    			SimCityGui.marketPanel.addGui(mcg);
    		}
    		else if (r.getComponentPanel() == SimCityGui.market1Panel) {
    			SimCityGui.market1Panel.addGui(mcg);
    		}
    		else if (r.getComponentPanel() == SimCityGui.market2Panel) {
    			SimCityGui.market2Panel.addGui(mcg);
    		}
    	}
    	else if (r instanceof BankCustomerRole) {
    		BankCustomerGui bcg = new BankCustomerGui((BankCustomerRole) r, gui);
    		//gui.updateInfoPanel(bcg);
    		((BankCustomerRole) r).setGui(bcg);

    		if (r.getComponentPanel() == SimCityGui.bankPanel){ 
	    		bcg.customer.setTeller(SimCityGui.bankPanel.teller);
	    		bcg.setBankPanel(SimCityGui.bankPanel);
	    		SimCityGui.bankPanel.addGui(bcg);
    		}
    		else if (r.getComponentPanel() == SimCityGui.bank1Panel){
    			bcg.customer.setTeller(SimCityGui.bank1Panel.teller);
    			bcg.setBankPanel(SimCityGui.bank1Panel);
	    		SimCityGui.bank1Panel.addGui(bcg);
    		}

    		bcg.DoEnterBank();
    		bankOptions.setVisible(true);
    		updateButtons("Bank");
    	}
    	else if (r instanceof BankRobberRole) {
    		BankRobberGui brg = new BankRobberGui((BankRobberRole) r, gui, 400, 600);
    		((BankRobberRole) r).setGui(brg);
    		if (r.getComponentPanel() == SimCityGui.bankPanel) {
	    		brg.setGuardGui(SimCityGui.bankPanel.getGuardGui());
	    		brg.setBankPanel(SimCityGui.bankPanel);
	    		SimCityGui.bankPanel.addGui(brg);
    		}
    		else if (r.getComponentPanel() == SimCityGui.bank1Panel) {
    			brg.setGuardGui(SimCityGui.bank1Panel.getGuardGui());
    			brg.setBankPanel(SimCityGui.bank1Panel);
	    		SimCityGui.bank1Panel.addGui(brg);
    		}
    		brg.robBank();
    		bankOptions.setVisible(true);
    		updateButtons("Bank");
    	}
    	else if (r instanceof BankTellerRole) {
    		BankTellerGui btg = new BankTellerGui((BankTellerRole) r, gui, 450, 80);
    		//gui.updateInfoPanel(bcg);
    		((BankTellerRole) r).setGui(btg);
    		bankOptions.setVisible(true);
    		
    		if (r.getComponentPanel() == SimCityGui.bankPanel) {
    			SimCityGui.bankPanel.addGui(btg);
    			btg.setBankPanel(SimCityGui.bankPanel);
    		}
    		else if (r.getComponentPanel() == SimCityGui.bank1Panel) {
    			SimCityGui.bank1Panel.addGui(btg);
    			btg.setBankPanel(SimCityGui.bank1Panel);
    		}
    	}
    	else if (r instanceof BankGuardRole) {
    		BankGuardGui bgg = new BankGuardGui((BankGuardRole) r, gui, 80, 250);
    		((BankGuardRole) r).setGui(bgg);
    		bankOptions.setVisible(true);
    		
    		if (r.getComponentPanel() == SimCityGui.bankPanel) {
	    		SimCityGui.bankPanel.addGui(bgg);
	    		SimCityGui.bankPanel.setGuardGui(bgg);
	    		bgg.setBankPanel(SimCityGui.bankPanel);
    		}
    		else if (r.getComponentPanel() == SimCityGui.bank1Panel) {
    			SimCityGui.bank1Panel.addGui(bgg);
	    		SimCityGui.bank1Panel.setGuardGui(bgg);
	    		bgg.setBankPanel(SimCityGui.bank1Panel);
    		}
    	}
    	else if (r instanceof HouseResident) {
			//SimCityGui.housePanel.addGui(((HouseResidentRole) r).residentGui);
    		HouseResidentGui hrg = new HouseResidentGui((HouseResidentRole) r, SimCityGui.housePanel);
    		((HouseResidentRole) r).setGui(hrg);
    		SimCityGui.apartments.addGui((hrg));
    		
    		updateButtons("House");
    	}
    	else if (r instanceof HouseCustomer) {
			//SimCityGui.housePanel.addGui(((HouseResidentRole) r).residentGui);
    		HouseCustomerGui hcg = new HouseCustomerGui((HouseCustomerRole) r, SimCityGui.housingOfficePanel);
    		((HouseCustomerRole) r).setGui(hcg);
    		hcg.role.setOwner(SimCityGui.housingOfficePanel.owner);
    		SimCityGui.housingOfficePanel.addGui((hcg));
    		updateButtons("HousingOffice");
    	}
    	else if (r instanceof HouseOwner) {
    		HouseOwnerGui hog = new HouseOwnerGui((HouseOwnerRole) r, SimCityGui.housingOfficePanel);
    		((HouseOwnerRole) r).setGui(hog);
    		SimCityGui.housingOfficePanel.addGui((hog));
    	}
    	else if (r instanceof HouseMaintenanceManager) {
    		HouseMaintenanceManagerGui hmmg = new HouseMaintenanceManagerGui((HouseMaintenanceManagerRole) r, SimCityGui.housingOfficePanel);
    		((HouseMaintenanceManagerRole) r).setGui(hmmg);
    		SimCityGui.housingOfficePanel.addGui((hmmg));
    	}
    	else if (r instanceof Restaurant1Customer) {
    		Restaurant1CustomerGui r1cg = new Restaurant1CustomerGui((Restaurant1CustomerRole) r, SimCityGui.restaurant1Panel, 1);
    		((Restaurant1CustomerRole) r).setGui(r1cg);
    		r1cg.agent.setHost(SimCityGui.restaurant1Panel.host);
    		r1cg.agent.gotHungry();
    		SimCityGui.restaurant1Panel.addGui(r1cg);
    		updateButtons("Restaurant");
    	}
    	else if (r instanceof Restaurant1Waiter) {
    		Restaurant1WaiterGui r1wg = new Restaurant1WaiterGui((Restaurant1Waiter) r, SimCityGui.restaurant1Panel, rest1WaiterPos);
    		((Restaurant1Waiter) r).setGui(r1wg);
    		SimCityGui.restaurant1Panel.addGui(r1wg);
    		rest1WaiterPos++;
    		updateButtons("RestaurantWaiter");
    	}
    	else if (r instanceof Restaurant1Host) {
    		/*Restaurant1HostGui r1hg = new Restaurant1HostGui((Restaurant1Host) r, SimCityGui.restaurant1Panel, 1);
    		((Restaurant1Host) r).setGui(r1hg);
    		SimCityGui.restaurant1Panel.addGui(r1hg);
    		updateButtons("RestaurantHost");*/
    	}
    	else if (r instanceof Restaurant1Cook) {
    		Restaurant1CookGui r1ckg = new Restaurant1CookGui((Restaurant1Cook) r, SimCityGui.restaurant1Panel, 1);
    		((Restaurant1Cook) r).setGui(r1ckg);
    		SimCityGui.restaurant1Panel.addGui(r1ckg);
    		updateButtons("RestaurantCook");
    	}
    	else if (r instanceof Restaurant1Cashier) {
    		/*Restaurant1WaiterGui r1wg = new Restaurant1WaiterGui((Restaurant1Waiter) r, SimCityGui.restaurant1Panel, 1);
    		((Restaurant1Waiter) r).setGui(r1wg);
    		SimCityGui.restaurant1Panel.addGui(r1wg);
    		updateButtons("RestaurantWaiter");*/
    	}
    	else if (r instanceof Restaurant2Customer) {
    		Restaurant2CustomerGui r2cg = new Restaurant2CustomerGui((Restaurant2CustomerRole) r, SimCityGui.restaurant2Panel);
    		((Restaurant2CustomerRole) r).setGui(r2cg);	
    		r2cg.agent.setHost(SimCityGui.restaurant2Panel.host);
    		r2cg.agent.setCashier(SimCityGui.restaurant2Panel.cashier);
    		SimCityGui.restaurant2Panel.addGui(r2cg);
    		r2cg.agent.GotHungry();
    		updateButtons("Restaurant");
    	}
    	else if (r instanceof Restaurant2Waiter) {
    		Restaurant2WaiterGui r2wg = new Restaurant2WaiterGui((Restaurant2WaiterRole) r);
    		((Restaurant2WaiterRole) r).setGui(r2wg);	
    		/*
    		r2cg.agent.setHost(SimCityGui.restaurant2Panel.host);
    		r2cg.agent.setCashier(SimCityGui.restaurant2Panel.cashier);
    		*/
    		SimCityGui.restaurant2Panel.addGui(r2wg);
    		updateButtons("Restaurant");
    	}
    	else if (r instanceof Restaurant2Cook) {
    		Restaurant2CookGui r2cg = new Restaurant2CookGui((Restaurant2CookRole) r);
    		((Restaurant2CookRole) r).setGui(r2cg);	
    		/*
    		r2cg.agent.setHost(SimCityGui.restaurant2Panel.host);
    		r2cg.agent.setCashier(SimCityGui.restaurant2Panel.cashier);
    		*/
    		SimCityGui.restaurant2Panel.addGui(r2cg);
    		updateButtons("Restaurant");
    	}
    	
    	else if (r instanceof Restaurant2Cashier) {
    		
    	}
    	
    	else if (r instanceof Restaurant2Host) {
    		
    	}
    	
    	else if (r instanceof Restaurant3Customer) {
    		Restaurant3CustomerGui r3cg = new Restaurant3CustomerGui((Restaurant3CustomerRole) r, SimCityGui.restaurant3Panel);
    		r3cg.setYWaitPos(rest3CustomerGuiYPos);
    		r3cg.setXWaitPos(rest3CustomerGuiXPos);
    		rest3CustomerGuiYPos += 50;
    		if(rest3CustomerGuiYPos >= 340){
    			rest3CustomerGuiYPos = 10;
    			rest3CustomerGuiXPos += 30;
    		}
    		((Restaurant3CustomerRole) r).setGui(r3cg);
    		r3cg.agent.setCashier(SimCityGui.restaurant3Panel.cashier);
    		r3cg.agent.setHost(SimCityGui.restaurant3Panel.host);
    		r3cg.agent.gotHungry();
    		SimCityGui.restaurant3Panel.addGui(r3cg);
    		updateButtons("Restaurant");
    	}
    	else if (r instanceof Restaurant3Waiter) {
    		Restaurant3WaiterGui r3wg = new Restaurant3WaiterGui((Restaurant3Waiter) r, SimCityGui.restaurant3Panel);
    		((Restaurant3Waiter) r).setGui(r3wg);
    		SimCityGui.restaurant3Panel.addGui(r3wg);
    		r3wg.setXWatchPos(rest3WaiterXWatchPos);
    		rest3WaiterXWatchPos += 40;
    		updateButtons("RestaurantWaiter");
    	}
    	else if (r instanceof Restaurant3Cook) {
    		Restaurant3CookGui r3ckg = new Restaurant3CookGui((Restaurant3Cook) r, SimCityGui.restaurant3Panel);
    		((Restaurant3Cook) r).setGui(r3ckg);
    		SimCityGui.restaurant3Panel.addGui(r3ckg);
    		updateButtons("RestaurantCook");
    	}
    	
    	else if (r instanceof Restaurant4Customer) {
    		Restaurant4CustomerGui r4cg = new Restaurant4CustomerGui((Restaurant4CustomerRole) r, gui, 95, 170);
    		((Restaurant4CustomerRole) r).setGui(r4cg);
    		r4cg.customer.setCashier(SimCityGui.restaurant4Panel.ca);
    		r4cg.customer.setHost((Restaurant4Host) SimCityGui.restaurant4Panel.h1);
    		r4cg.customer.gotHungry();
    		SimCityGui.restaurant4Panel.addGui(r4cg);
    		updateButtons("Restaurant");
    	}
    	else if (r instanceof Restaurant4Waiter) {
    		Restaurant4WaiterGui r4wg = new Restaurant4WaiterGui((Restaurant4WaiterRole) r, gui, rest4WaiterPos);
    		((Restaurant4WaiterRole) r).setGui(r4wg);
    		r4wg.setHost(SimCityGui.restaurant4Panel.h1);
    		r4wg.setCookGui(SimCityGui.restaurant4Panel.ck.cookGui);
    		r4wg.addWaiter((Restaurant4WaiterRole) r);
    		/*
    		r4cg.customer.setCashier(SimCityGui.restaurant4Panel.ca);
    		r4cg.customer.setHost((Restaurant4Host) SimCityGui.restaurant4Panel.h1);
    		r4cg.customer.gotHungry();
    		*/
    		SimCityGui.restaurant4Panel.addGui(r4wg);
    		rest4WaiterPos++;
    		updateButtons("Restaurant");
    	}
    	else if (r instanceof Restaurant4Cook) {
    		Restaurant4CookGui r4cg = new Restaurant4CookGui((Restaurant4CookRole) r, gui, 661, 494);
    		((Restaurant4CookRole) r).setGui(r4cg);
    		/*
    		r4cg.customer.setCashier(SimCityGui.restaurant4Panel.ca);
    		r4cg.customer.setHost((Restaurant4Host) SimCityGui.restaurant4Panel.h1);
    		r4cg.customer.gotHungry();
    		*/
    		SimCityGui.restaurant4Panel.addGui(r4cg);
    		updateButtons("Restaurant");
    	}
    	else if (r instanceof Restaurant4Cashier) {
    		
    	}
    	else if (r instanceof Restaurant4Host) {
    		
    	}
    	
    	
    	else if (r instanceof Restaurant5Customer) {
    		Restaurant5CustomerGui r5cg = new Restaurant5CustomerGui((Restaurant5CustomerRole) r, SimCityGui.restaurant5Panel); 
    		((Restaurant5CustomerRole) r).setGui(r5cg);
    		r5cg.agent.setHost(SimCityGui.restaurant5Panel.host);
    		r5cg.agent.setCashier(SimCityGui.restaurant5Panel.cashier);
    		r5cg.agent.gotHungry();
    		SimCityGui.restaurant5Panel.addGui(r5cg);
    		updateButtons("Restaurant");
    		
    	}
    	// add Restaurants 
    }

    /**
     * Adds a person
     * 
     * @param name name of person
     */
    public void addPerson(String name) {
            OrdinaryPerson p = new OrdinaryPerson(name, 100);
            p.addComponents(buildingEntrances);
            PersonGui g = new PersonGui(p, gui);
            SimCityGui.cityPanel.addGui(g);
            p.setGui(g);
            people.add(p);
            p.startThread();
    }
    
	public void addPerson(String text, ImageIcon icon) {

        OrdinaryPerson p = new OrdinaryPerson(text, 100);
        p.addComponents(buildingEntrances);
        PersonGui g = new PersonGui(p, gui);
        SimCityGui.cityPanel.addGui(g);
        p.setGui(g);
        people.add(p);
        p.startThread();
        
        g.setIcon(icon);
        g.loadImage("src/simCity/gui/images/newCity/malegreen20.png");
        g.setImageString("src/simCity/gui/images/newCity/malegreen20.png");
        g.setCarString("src/simCity/gui/images/newCity/car.png");
	}
	
	public void addPerson(String text, ImageIcon icon, String color) {

        OrdinaryPerson p = new OrdinaryPerson(text, 100);
        p.addComponents(buildingEntrances);
        PersonGui g = new PersonGui(p, gui);
        SimCityGui.cityPanel.addGui(g);
        p.setGui(g);
        people.add(p);
        p.startThread();
        
        if(p.getName().equals("rob")) {
        	p.noPersonState();
        	p.setAIEnabled(false);
        	Role r = p.RoleFactory("BankRobber");
        	p.roles.add(r);
			p.ToBank(r);
        }
        
        g.setIcon(icon);
        if(color == "greenboy") {
		    g.loadImage("src/simCity/gui/images/newCity/malegreen20.png");
		    g.setImageString("src/simCity/gui/images/newCity/malegreen20.png");
        } else if(color == "blueboy") {
        	g.loadImage("src/simCity/gui/images/newCity/maleblue20.png");
		    g.setImageString("src/simCity/gui/images/newCity/maleblue20.png");
        } else if(color == "redboy") {
        	g.loadImage("src/simCity/gui/images/newCity/malered20.png");
		    g.setImageString("src/simCity/gui/images/newCity/malered20.png");
        } else if(color == "greengirl") {
        	g.loadImage("src/simCity/gui/images/newCity/femalegreen20.png");
		    g.setImageString("src/simCity/gui/images/newCity/femalegreen20.png");
        } else if(color == "bluegirl") {
        	g.loadImage("src/simCity/gui/images/newCity/femaleblue20.png");
		    g.setImageString("src/simCity/gui/images/newCity/femaleblue20.png");
        } else if(color == "redgirl") {
        	g.loadImage("src/simCity/gui/images/newCity/femalered20.png");
		    g.setImageString("src/simCity/gui/images/newCity/femalered20.png");
        }
        g.setCarString("src/simCity/gui/images/newCity/car.png");
	}

	public void leavingBank() {
		bankOptions.setEnabled(false);
		//bankOptions.setVisible(false);
	}
	

	public void stopLightTimer(){
		timer.schedule(new TimerTask() {
			public void run() {
				redLight = false;
				//System.out.println("its green");
				timer.schedule(new TimerTask() {
	    			public void run() {
	    				//System.out.println("its red");
	    				redLight = true;
	    				stopLightTimer();
	    			}
	    		},
	    		2000);
			}
		},
		6000);
	}
	
	public boolean getRedLight(){
		return redLight;
	}
}
