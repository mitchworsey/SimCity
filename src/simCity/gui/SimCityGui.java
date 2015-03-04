package simCity.gui;

import javax.swing.*;
import javax.swing.border.Border;

import simCity.CitySetUpBaseline;
import simCity.CitySetUpNormal;
import simCity.Clock;
import simCity.OrdinaryPerson;
import simCity.house.HouseResidentRole;
import simCity.interfaces.Person;
import simCity.restaurant4.interfaces.Restaurant4Customer;
import simCity.Restaurant1.Restaurant1CashierRole;
import simCity.Restaurant1.Restaurant1CookRole;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.Restaurant1.Restaurant1WaiterRole;
import simCity.gui.Restaurant1.Restaurant1CookGui;
import simCity.gui.Restaurant1.Restaurant1CustomerGui;
import simCity.gui.Restaurant1.Restaurant1WaiterGui;
import simCity.interfaces.Restaurant1Host;

import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame implements ActionListener {
	
    //globals - dimensions
    static final int WINDOWX = 960;
    static final int WINDOWY = 700;
    static final int CITYPANELDIMX = 900;
    static final int CITYPANELDIMY = 700;
    static final int CONTROLPANELDIMX = 50;
    static final int CONTROLPANELDIMY = 60;
    static final int PICTUREPANELDIMX = 100;
    static final int PICTUREPANELDIMY = 100;
   
    //Set up
    public static CitySetUpBaseline citySetUpBaseline;
    public static CitySetUpNormal citySetUpNormal;
    
	//all panels in main window 
    public static ControlPanel controlPanel = new ControlPanel();
    public static CityPanel cityPanel = new CityPanel(); 
    public static CityPanelSmall cityPanelSmall = new CityPanelSmall();
    public static MarketPanel marketPanel = new MarketPanel(); 
    public static Market1Panel market1Panel = new Market1Panel();
    public static Market2Panel market2Panel = new Market2Panel();
    public static BankPanel bankPanel = new BankPanel();
    public static BankPanel  bank1Panel = new BankPanel();
    public static HousePanel housePanel = new HousePanel();
    public static HousePanel housePanel1 = new HousePanel();
    public static HousingOfficePanel housingOfficePanel = new HousingOfficePanel();
    public static ApartmentPanel apartmentPanel1 = new ApartmentPanel(1);
    public static ApartmentPanel apartmentPanel2 = new ApartmentPanel(2);
    public static ApartmentPanel apartmentPanel3 = new ApartmentPanel(3);
    public static ApartmentPanel apartmentPanel4 = new ApartmentPanel(4);
    public static HousePanel apartments = new HousePanel();
    public static Restaurant1Panel restaurant1Panel = new Restaurant1Panel();
    public static Restaurant2Panel restaurant2Panel = new Restaurant2Panel();
    public static Restaurant3Panel restaurant3Panel = new Restaurant3Panel();
    public static Restaurant4Panel restaurant4Panel = new Restaurant4Panel();
    public static Restaurant5Panel restaurant5Panel = new Restaurant5Panel();
    public static BusStopPanel busStopPanel1 = new BusStopPanel();
    public static BusStopPanel busStopPanel2 = new BusStopPanel();
    private JLayeredPane locationPanels; //layout for building panels  
    
    //state variables - gui/person that is selected
    Gui currentPersonGui;
    
    //create panel
    JRadioButton girlButton;
    JRadioButton boyButton;
    JRadioButton redButton;
    JRadioButton blueButton;
    JRadioButton greenButton;
    JLabel createPicture;
    private ImageIcon boyBlueIcon = new ImageIcon("src/simCity/gui/images/controlPanel/boyBlue.png");
    private Image boyBlue = boyBlueIcon.getImage();
    private Image boyBlue2 = boyBlue.getScaledInstance(PICTUREPANELDIMX, PICTUREPANELDIMY, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon girlBlueIcon = new ImageIcon("src/simCity/gui/images/controlPanel/girlBlue.png");
    private Image girlBlue = girlBlueIcon.getImage();
    private Image girlBlue2 = girlBlue.getScaledInstance(PICTUREPANELDIMX, PICTUREPANELDIMY, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon boyRedIcon = new ImageIcon("src/simCity/gui/images/controlPanel/boyRed.png");
    private Image boyRed = boyRedIcon.getImage();
    private Image boyRed2 = boyRed.getScaledInstance(PICTUREPANELDIMX, PICTUREPANELDIMY, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon girlRedIcon = new ImageIcon("src/simCity/gui/images/controlPanel/girlRed.png");
    private Image girlRed = girlRedIcon.getImage();
    private Image girlRed2 = girlRed.getScaledInstance(PICTUREPANELDIMX, PICTUREPANELDIMY, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon boyGreenIcon = new ImageIcon("src/simCity/gui/images/controlPanel/boyGreen.png");
    private Image boyGreen = boyGreenIcon.getImage();
    private Image boyGreen2 = boyGreen.getScaledInstance(PICTUREPANELDIMX, PICTUREPANELDIMY, java.awt.Image.SCALE_SMOOTH);
    private ImageIcon girlGreenIcon = new ImageIcon("src/simCity/gui/images/controlPanel/girlGreen.png");
    private Image girlGreen = girlGreenIcon.getImage();
    private Image girlGreen2 = girlGreen.getScaledInstance(PICTUREPANELDIMX, PICTUREPANELDIMY, java.awt.Image.SCALE_SMOOTH);
    
    //instruction panel
    JButton scenarioAButton;
    JButton scenarioBButton;
    JButton scenarioCButton;
    JButton scenarioDButton;
    JButton scenarioEButton;
    JButton scenarioFButton;
    JButton scenarioGButton;
    JButton scenarioHButton;
    JButton scenarioIButton;
    
    //control panel
    //Background
    private JPanel backControlPanel;
    private int numOfButtons;
	//Picture
	private JPanel picturePanel; 
	private JLabel picture;
	//Exit button
	private JButton exitButton;
	private ImageIcon icon007 = new ImageIcon("src/simCity/gui/images/controlPanel/007.png");
	private Image image007 = icon007.getImage() ;  
	private Image image0072 = image007.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ); 
	private JLabel label007;
	//Info button 
	private JButton question;
	private ImageIcon questionIcon = new ImageIcon("src/simCity/gui/images/controlPanel/question.png");
	private Image questionImage = questionIcon.getImage() ;  
	private Image questionImage2 = questionImage.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH );  
	//Create person button
	private JButton create;
	private ImageIcon createIcon = new ImageIcon("src/simCity/gui/images/controlPanel/create.png");
	private Image createImage = createIcon.getImage() ;  
	private Image createImage2 = createImage.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ); 
	//Run  Baseline Scenarios 
	private JButton scenarios;
	private ImageIcon scenariosIcon = new ImageIcon("src/simCity/gui/images/controlPanel/buttonPhone.png");
	private Image scenariosImage = scenariosIcon.getImage() ;  
	private Image scenariosImage2 = scenariosImage.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ); 
	//Back to city view button 
	private JButton back;
	private ImageIcon backIcon = new ImageIcon("src/simCity/gui/images/controlPanel/back.png");
	private Image backImage = backIcon.getImage() ;  
	private Image backImage2 = backImage.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH );
    
	//time jlabel
	JPanel timePanel;
	JLabel timeLabel;
	JLabel cityLight;
	
	//clock
	
    /**
     * Constructor for SimCityGui class.
     * Sets up all the gui components.
     */
    public SimCityGui(String setup) {
    	if (setup.equals("Baseline")){
	    	citySetUpBaseline = new CitySetUpBaseline(this);
    	}
    	else {
    		citySetUpNormal = new CitySetUpNormal(this);
    	}
    //time label setup
    	timePanel = new JPanel();   	
    	
        timePanel.setBackground(Color.WHITE);
        timePanel.setOpaque(false);
		timePanel.setLayout(null);
		timePanel.setSize(900, 700);
		
        JLabel timeLabel = new JLabel("0.0, 00/00/0000");
        timeLabel.setFont(new Font(timeLabel.getName(), Font.PLAIN, 24));
        timeLabel.setBounds(10, WINDOWY-70, 235, 24);
        timeLabel.setBackground(Color.WHITE);
        //timeLabel.setOpaque(true);
        
        JLabel cityLight = new JLabel();
        cityLight.setBounds(0, 0, 900, 700);
        
        timePanel.add(cityLight);
        timePanel.add(timeLabel);
        
    //global clock setup
        Clock.globalClock = new Clock(timeLabel, cityLight);
   
    //window setup
        setSize(WINDOWX, WINDOWY);
        setLocationRelativeTo(null);
        //controlPanel.addRestaurant1Employees();   //possible v2 functionality
        
    //location panels creation
    	cityPanel.setMain(this);
    	cityPanelSmall.setMain(this);
    	cityPanelSmall.setVisible(false);
    	marketPanel.setMain(this);
    	market1Panel.setMain(this);
    	market2Panel.setMain(this);
    	bankPanel.setMain(this);
    	bank1Panel.setMain(this);
    	housePanel.setMain(this);
    	housePanel1.setMain(this);
    	housingOfficePanel.setMain(this);
    	apartmentPanel1.setMain(this);
    	apartmentPanel2.setMain(this);
    	apartmentPanel3.setMain(this);
    	apartmentPanel4.setMain(this);
    	restaurant1Panel.setMain(this);
    	restaurant2Panel.setMain(this);
    	restaurant3Panel.setMain(this);
    	restaurant4Panel.setMain(this);
    	restaurant5Panel.setMain(this);
    	busStopPanel1.setMain(this);
    	busStopPanel2.setMain(this);
    	controlPanel.setGui(this);
    	controlPanel.addCityGuis();
    	apartments.setMain(this);
        
		//upper picture
    	numOfButtons = 4;
		picturePanel = new JPanel();
		picturePanel.setBackground(Color.WHITE);
		picturePanel.setLayout(null);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		picturePanel.setBorder(BorderFactory.createCompoundBorder(
		raisedbevel, loweredbevel));
		picture = new JLabel();
		picture.setBounds(5, 5, PICTUREPANELDIMX-10, PICTUREPANELDIMY-10);
		picture.setVisible(true);
		picturePanel.add(picture);
        picturePanel.setLayout(null);
        cityPanelSmall.setBorder(BorderFactory.createCompoundBorder(
		raisedbevel, loweredbevel));
        
    	//location panels setup
        cityPanel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        cityPanelSmall.setBounds(750, 550, 150, 150);
        marketPanel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        market1Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        market2Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        bankPanel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        bank1Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        housePanel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        housePanel1.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        housingOfficePanel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        apartments.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        apartmentPanel1.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        apartmentPanel2.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        apartmentPanel3.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        apartmentPanel4.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        restaurant1Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        restaurant2Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        restaurant3Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        restaurant4Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        restaurant5Panel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        busStopPanel1.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        busStopPanel2.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
        picturePanel.setVisible(false);
        picturePanel.setBounds(5, 5, PICTUREPANELDIMX, PICTUREPANELDIMY);
        timePanel.setVisible(true);
        
        //adds to location panels
        locationPanels = new JLayeredPane();
        locationPanels.add(cityPanelSmall, new Integer(1));
        locationPanels.add(marketPanel, new Integer(1));
        locationPanels.add(market1Panel, new Integer(1));
        locationPanels.add(market2Panel, new Integer(1));
        locationPanels.add(bankPanel, new Integer(1));
        locationPanels.add(bank1Panel, new Integer(1));
        locationPanels.add(housePanel, new Integer(1));
        locationPanels.add(housePanel1, new Integer(1));
        locationPanels.add(housingOfficePanel, new Integer(1));
        locationPanels.add(apartmentPanel1, new Integer(1));
        locationPanels.add(apartmentPanel2, new Integer(1));
        locationPanels.add(apartmentPanel3, new Integer(1));
        locationPanels.add(apartmentPanel4, new Integer(1));
        locationPanels.add(restaurant1Panel, new Integer(1));
        locationPanels.add(restaurant2Panel, new Integer(1));
        locationPanels.add(restaurant3Panel, new Integer(1));
        locationPanels.add(restaurant4Panel, new Integer(1));
        locationPanels.add(restaurant5Panel, new Integer(1));
        locationPanels.add(busStopPanel1, new Integer(1));
        locationPanels.add(busStopPanel2, new Integer(1));
        locationPanels.add(picturePanel, new Integer(1));
        locationPanels.add(apartments, new Integer(1));
        locationPanels.add(timePanel, new Integer(4));
        locationPanels.add(cityPanel, new Integer(3));

        //create panel
        boyBlueIcon = new ImageIcon(boyBlue2);
        createPicture = new JLabel(boyBlueIcon);
        girlBlueIcon = new ImageIcon(girlBlue2);
        boyRedIcon = new ImageIcon(boyRed2);
        girlRedIcon = new ImageIcon(girlRed2);
        boyGreenIcon = new ImageIcon(boyGreen2);
        girlGreenIcon = new ImageIcon(girlGreen2);
      
    //control panel setup 
        backControlPanel = new JPanel();
        backControlPanel.setBackground(Color.WHITE);
        
        ///exit button
        exitButton = new JButton("x");
        exitButton.addActionListener(this);
		
		//lower control panel 
		icon007 = new ImageIcon(image0072);
		label007 = new JLabel(icon007);
		questionIcon = new ImageIcon(questionImage2);
		createIcon = new ImageIcon(createImage2);
		scenariosIcon = new ImageIcon(scenariosImage2);
		backIcon = new ImageIcon(backImage2);
		question = new JButton(questionIcon);
		question.setVisible(false);
		question.addActionListener(this);
		create = new JButton(createIcon);
		create.setVisible(false);
		create.addActionListener(this);
		scenarios = new JButton(scenariosIcon);
		scenarios.setVisible(false);
		scenarios.addActionListener(this);
		back = new JButton(backIcon);
		back.setVisible(false);
		back.addActionListener(this);
        
	/* Market and Restaurant Interaction */	
		//restaurant5Panel.setRestaurantGrocer(marketPanel.grocer);
		
    //main window adds
        //main window layout
        setLayout(null);
        setBackground(Color.WHITE);
        
        //control panel adds
        question.setBounds(5, WINDOWY-125, 50, 50);
        question.setVisible(true);
        add(question);
        
        create.setBounds(5, WINDOWY-185, 50, 50);
        create.setVisible(true);
        add(create);
        scenarios.setBounds(5, WINDOWY-245, 50, 50);
        scenarios.setVisible(true);
        add(scenarios);
        back.setBounds(5, WINDOWY-305, 50, 50);
        back.setVisible(false);
        add(back);
        label007.setBounds(5, WINDOWY-75, 50, 50);
        label007.setVisible(true);
        add(label007);
        controlPanel.setVisible(false);
        controlPanel.setBounds( 5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
        add(controlPanel);
        //picturePanel.setVisible(false);
        //picturePanel.setBounds(5, 5, PICTUREPANELDIMX, PICTUREPANELDIMY);
        //add(picturePanel);
        exitButton.setVisible(false);
        exitButton.setBounds(70, PICTUREPANELDIMY+10, 20, 20);
        add(exitButton);
        backControlPanel.setBounds(0, 0, 60, WINDOWY);
        add(backControlPanel);
    
        //location panels adds
        locationPanels.setBorder(null);
        locationPanels.setBounds(60, 0, CITYPANELDIMX, CITYPANELDIMY);
        add(locationPanels);
    }
    
    /**
     * updateInfoPanel() takes the given person object and
     * changes the information panel to hold that person's info.
     *
     * @param gui Person object
     */
    public void updateInfoPanel(Gui gui) {
        currentPersonGui = gui;
        controlPanel.setVisible(true);
        
        if (gui instanceof PersonGui) {
        	picturePanel.remove(picture);
        	picture = new JLabel((ImageIcon)((PersonGui)gui).icon);
        	picturePanel.remove(picture);
        	picture.setBounds(5, 5, PICTUREPANELDIMX-10, PICTUREPANELDIMY-10);
            picturePanel.add(picture);
            picturePanel.setVisible(true);
        }
    }
    
    public void hideInfoPanel() {
    	controlPanel.setVisible(false);
    	picturePanel.setVisible(false);
    }
    
    /**
     * changePanel() switches the layers of panels (for main city and each location)
     * Clicking on a building switches the view for that building, and going back 
     * switches the view back to the city
     * 
     * @param panel
     */
    public void changePanel(String panel){
    	if (panel.equals("Market")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(marketPanel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Market1")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(market1Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Market2")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(market2Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Main")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(false);
    		back.setVisible(false);
    		locationPanels.setLayer(marketPanel, new Integer(1));
    		locationPanels.setLayer(market1Panel, new Integer(1));
    		locationPanels.setLayer(market2Panel, new Integer(1));
    		locationPanels.setLayer(bankPanel, new Integer(1));
    		locationPanels.setLayer(bank1Panel, new Integer(1));
    		locationPanels.setLayer(cityPanel, new Integer(3));
    		locationPanels.setLayer(timePanel, new Integer(4));
    		locationPanels.setLayer(housePanel,  new Integer(1));
    		locationPanels.setLayer(housePanel1,  new Integer(1));
    		locationPanels.setLayer(housingOfficePanel, new Integer(1));
    		locationPanels.setLayer(restaurant1Panel, new Integer(1));
    		locationPanels.setLayer(restaurant2Panel, new Integer(1));
    		locationPanels.setLayer(restaurant3Panel, new Integer(1));
    		locationPanels.setLayer(restaurant4Panel, new Integer(1));
    		locationPanels.setLayer(restaurant5Panel, new Integer(1));
    		locationPanels.setLayer(busStopPanel1, new Integer(1));
    		locationPanels.setLayer(busStopPanel2, new Integer(1));
            locationPanels.setLayer(apartmentPanel1, new Integer(1));
            locationPanels.setLayer(apartmentPanel2, new Integer(1));
            locationPanels.setLayer(apartmentPanel3, new Integer(1));
            locationPanels.setLayer(apartmentPanel4, new Integer(1));
            locationPanels.setLayer(apartments, new Integer(1));
            
            timePanel.setVisible(true);
    	}
    	if (panel.equals("Bank")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*(numOfButtons));
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(bankPanel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Bank1")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*(numOfButtons));
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(bank1Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("House")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(housePanel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("House1")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(housePanel1, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("HousingOffice")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(housingOfficePanel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Apartment1")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(apartmentPanel1, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Apartment2")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(apartmentPanel2, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Apartment3")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(apartmentPanel3, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Apartment4")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(apartmentPanel4, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Restaurant1")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(restaurant1Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Restaurant2")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(restaurant2Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Restaurant3")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(restaurant3Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 4);
    	}
    	if (panel.equals("Restaurant4")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(restaurant4Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("Restaurant5")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(restaurant5Panel, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("BusStop1")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(busStopPanel1, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}
    	if (panel.equals("BusStop2")){
    		controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    		cityPanelSmall.setVisible(true);
    		back.setVisible(true);
    		locationPanels.setLayer(busStopPanel2, locationPanels.highestLayer() -2);
    		locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    		locationPanels.setLayer(cityPanel, locationPanels.highestLayer() - 3);
    	}

    	if(panel.equals("Market") || panel.equals("Market1") || panel.equals("Market2")
    		|| panel.equals("Bank") || panel.equals("Bank1") || panel.equals("House") || panel.equals("House1")
    		|| panel.equals("HousingOffice") || panel.equals("Apartment1") || panel.equals("Apartment2")
    		|| panel.equals("Apartment3") || panel.equals("Apartment4") || panel.equals("Restaurant1")
    		|| panel.equals("Restaurant2") || panel.equals("Restaurant3") || panel.equals("Restaurant4")
    		|| panel.equals("Restaurant5") || panel.equals("BusStop1") || panel.equals("BusStop2"))
    	{
    		timePanel.setVisible(false);
    	}
    }
    
    public void changeHousePanel(HouseResidentRole hrr){
    	controlPanel.setBounds(5, 5, CONTROLPANELDIMX, CONTROLPANELDIMY*numOfButtons);
    	cityPanelSmall.setVisible(true);
    	
    	back.setVisible(true);
    	if (hrr!= null)
    		apartments.setGui(hrr.residentGui);
    	else
    		apartments.setGuiEmpty();
    	locationPanels.setLayer(apartments, locationPanels.highestLayer()-2);
    	locationPanels.setLayer(cityPanelSmall, locationPanels.highestLayer()-1);
    	locationPanels.setLayer(apartmentPanel1, locationPanels.highestLayer()-3);
    	locationPanels.setLayer(apartmentPanel2, locationPanels.highestLayer()-3);
    	locationPanels.setLayer(apartmentPanel3, locationPanels.highestLayer()-3);
    	locationPanels.setLayer(apartmentPanel4, locationPanels.highestLayer()-3);
    }

    /**
     * Action Performed function
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            controlPanel.setVisible(false);
            picturePanel.setVisible(false);
            exitButton.setVisible(false);
        }
        if (e.getSource() == question){
            JOptionPane.showMessageDialog(this,
                "This SimCity game was created by team07:\n   Christine Song \n   Marina Hierl\n   Jessica Wang"
            	+ "\n   Garv Manocha\n   Albert Chin\n   Mitch Worsey"
                + "\nTo play the game, create a new character with the"
                + "\nplus button. Interact with characters by clicking on them.",
                "SimCity Information",
                JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == create){
        	createPicture.setIcon(boyBlueIcon);
            boyButton = new JRadioButton();
            boyButton.setSelected(true);
            girlButton = new JRadioButton();

            blueButton = new JRadioButton();
            blueButton.setSelected(true);
            redButton = new JRadioButton();
            greenButton = new JRadioButton();

            //Group the radio buttons.
            ButtonGroup genderGroup = new ButtonGroup();
            genderGroup.add(girlButton);
            genderGroup.add(boyButton);
            ButtonGroup colorGroup = new ButtonGroup();
            colorGroup.add(blueButton);
            colorGroup.add(redButton);
            colorGroup.add(greenButton);

            //Register a listener for the radio buttons.
            boyButton.addActionListener(this);
            girlButton.addActionListener(this);
            blueButton.addActionListener(this);
            redButton.addActionListener(this);
            greenButton.addActionListener(this);

            JPanel createPanel = new JPanel();
            JTextField nameField = new JTextField(10);
            createPanel.setLayout(new BorderLayout(10, 10));
            JLabel nameLabel = new JLabel("Name:");
            JPanel namePanel = new JPanel(new BorderLayout(10, 10));
            namePanel.add(nameLabel, BorderLayout.CENTER);
            namePanel.add(nameField, BorderLayout.PAGE_END);
            createPanel.add(namePanel, BorderLayout.PAGE_END);
            JLabel genderLabel = new JLabel("Gender:     ");
            JLabel colorLabel = new JLabel("Color:      ");
            JPanel colorPanel = new JPanel(new GridLayout(2, 4));
            colorPanel.add(colorLabel);
            colorPanel.add(blueButton);
            colorPanel.add(redButton);
            colorPanel.add(greenButton);
            colorPanel.add(genderLabel);
            colorPanel.add(boyButton);
            colorPanel.add(girlButton);
            createPanel.add(colorPanel, BorderLayout.CENTER);
            createPanel.add(createPicture, BorderLayout.LINE_END);

            JOptionPane.showMessageDialog(null, 
            		createPanel,
            		"Create new person",
            		JOptionPane.PLAIN_MESSAGE);
            if((nameField.getText() != null) && (nameField.getText().length() > 0)) {
        		ImageIcon tmpIcon = (ImageIcon) createPicture.getIcon();
            	if(tmpIcon == boyBlueIcon)
            		controlPanel.addPerson(nameField.getText(), tmpIcon, "blueboy");
            	else if(tmpIcon == boyGreenIcon)
            		controlPanel.addPerson(nameField.getText(), tmpIcon, "greenboy");
            	else if(tmpIcon == boyRedIcon)
            		controlPanel.addPerson(nameField.getText(), tmpIcon, "redboy");
            	else if(tmpIcon == girlBlueIcon)
            		controlPanel.addPerson(nameField.getText(), tmpIcon, "bluegirl");
            	else if(tmpIcon == girlGreenIcon)
            		controlPanel.addPerson(nameField.getText(), tmpIcon, "greengirl");
            	else if(tmpIcon == girlRedIcon)
            		controlPanel.addPerson(nameField.getText(), tmpIcon, "redgirl");
            	else
            		controlPanel.addPerson(nameField.getText(), tmpIcon);
        	}
        }
        if (e.getSource() == scenarios) {
        	if (citySetUpBaseline.getActive()){
        		scenarioAButton = new JButton("A");
        		scenarioAButton.addActionListener(this);
        		scenarioBButton = new JButton("B");
        		scenarioBButton.addActionListener(this);
        		scenarioCButton = new JButton("C");
        		scenarioCButton.addActionListener(this);
        		scenarioDButton = new JButton("D");
        		scenarioDButton.addActionListener(this);
        		scenarioEButton = new JButton("E");
        		scenarioEButton.addActionListener(this);
        		scenarioFButton = new JButton("F");
        		scenarioFButton.addActionListener(this);
        		scenarioGButton = new JButton("G");
        		scenarioGButton.addActionListener(this);
        		scenarioHButton = new JButton("H");
        		scenarioHButton.addActionListener(this);
        		scenarioIButton = new JButton("I");
        		scenarioIButton.addActionListener(this);
        		JPanel instructionPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        		instructionPanel.add(scenarioAButton);
        		instructionPanel.add(scenarioBButton);
        		instructionPanel.add(scenarioCButton);
        		instructionPanel.add(scenarioDButton);
        		instructionPanel.add(scenarioEButton);
        		instructionPanel.add(scenarioFButton);
        		instructionPanel.add(scenarioGButton);
        		instructionPanel.add(scenarioHButton);
        		instructionPanel.add(scenarioIButton);
        		JOptionPane.showMessageDialog(null,
        				instructionPanel,
        				"Instructions for Scenarios",
        				JOptionPane.PLAIN_MESSAGE);
        	}
        }
        if (e.getSource() == scenarioAButton){
        	JOptionPane.showMessageDialog(null,
        			"A. Scenario: [Tests all the behaviors]\n"
        				+ "     Click on apartment complex one. Click on Tom's apartment, the first apartment. \n"
        				+ "     Click on the eat button on the left, have him eat food at home. When he is done, \n"
        				+ "     click on the transportation button and select which workplace to go to. Whenever \n"
        				+ "     he exits a workplace, click on the transportation button and repeat. Do this until \n"
        				+ "     he visits all the workplaces. \n",
        			"Instructions for scenario A",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioBButton){
        	JOptionPane.showMessageDialog(null,
        			"B. Scenario: [Tests all the behaviors]\n"
        				+ "     Click on apartment complex one. In apartment one is Tom, he walks everywhere. In \n"
        				+ "     apartment 2 is Jen, she takes the bus (unless her destination location is closer \n"
        				+ "     than the bus stop). In apartment 3 is Joe, he drives his car everywhere. Each of \n"
        				+ "     them can eat in their home, then visit each workplace as determined by the buttons.\n"
        				+ "     The functionality is described in scenario 1.\n",
        			"Instructions for scenario B",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioCButton){
        	JOptionPane.showMessageDialog(null,
        			"C. Scenario: [Tests cook, cashier, market interaction]\n"
        				+ "     Click on a restaurant. Click on the left button to change the inventory of the \n"
        				+ "     restaurant. When a customer orders food, the restaurant will order more food from the\n"
        				+ "     market. The market will send a delivery truck in the city panel. When the truck reaches\n"
        				+ "     the restaurant, the restaurant will pay for the order. You can view the money before and\n"
        				+ "     after the transaction by viewing the information panel for the restaurant (button on left)\n",
        			"Instructions for scenario C",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioDButton){
        	JOptionPane.showMessageDialog(null,
        			"D. Scenario: For large teams [Tests party behavior]\n"
        				+ "     This does not apply to our team.\n",
        			"Instructions for scenario D",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioEButton){
        	JOptionPane.showMessageDialog(null,
        			"E. Scenario: [Shows bus-stop behavior]\n"
        				+ "     This will be viewable almost everytime a person travels to a building on the other side\n"
        				+ "     of the city. To test it specifically, go to apartment complex one, apartment 2, and have\n"
        				+ "     Jen go somewhere. She always takes the bus.\n",
        			"Instructions for scenario E",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioFButton){
        	JOptionPane.showMessageDialog(null,
        			"F. Scenario: [Shows that people know they can't visit certain workplaces]\n"
        				+ "     Each workplace closes automatically at a certain time. To close a workplace manually, go\n"
        				+ "     to the workplace panel and close them with the buttons on the side. Now the employees will\n"
        				+ "     go home and ordinary people cannot enter the workplace. You can verify this by selecting a\n"
        				+ "     person and clicking on the transportation button. The closed workplaces will not be \n"
        				+ "     avaliable. The majority of people who automatically function will also avoid this \n"
        				+ "     workplace.\n",
        			"Instructions for scenario F",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioGButton){
        	JOptionPane.showMessageDialog(null,
        			"G. Scenario: [Tests market behavior]\n"
        				+ "     The only time this scenario will happen is if a restaurant orders food, the closes\n"
        				+ "     before the delivery truck can make it there. If that happens, the delivery truck will\n"
        				+ "     check if the restaurant is open, and if it is not, it will return to the market without\n"
        				+ "     collecting money and without depositin the material. The restaurant will reorder the food\n"
        				+ "     when it opens again. The restaurants will not order food when the market is closed.\n",
        			"Instructions for scenario G",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioHButton){
        	JOptionPane.showMessageDialog(null,
        			"H. Scenario: For large teams [Tests party behavior]\n"
        				+ "     This does not apply to our team.\n",
        			"Instructions for scenario H",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == scenarioIButton){
        	JOptionPane.showMessageDialog(null,
        			"I. Scenario: For large teams [Tests party behavior]\n"
        				+ "     This does not apply to our team.\n",
        			"Instructions for scenario I",
        			JOptionPane.PLAIN_MESSAGE);
        }
        if (e.getSource() == boyButton){
        	if (blueButton.isSelected())
        		createPicture.setIcon(boyBlueIcon);
        	else if (redButton.isSelected())
        		createPicture.setIcon(boyRedIcon);
        	else if (greenButton.isSelected())
        		createPicture.setIcon(boyGreenIcon);
        }
        if (e.getSource() == girlButton){
        	if (blueButton.isSelected())
        		createPicture.setIcon(girlBlueIcon);
        	if (redButton.isSelected())
        		createPicture.setIcon(girlRedIcon);
        	if (greenButton.isSelected())
        		createPicture.setIcon(girlGreenIcon);
        }
        if (e.getSource() == blueButton){
        	if (boyButton.isSelected())
        		createPicture.setIcon(boyBlueIcon);
        	else if (girlButton.isSelected())
        		createPicture.setIcon(girlBlueIcon);
        }
        if (e.getSource() == redButton){
        	if (boyButton.isSelected())
        		createPicture.setIcon(boyRedIcon);
        	else if (girlButton.isSelected())
        		createPicture.setIcon(girlRedIcon);
        }
        if (e.getSource() == greenButton){
        	if (boyButton.isSelected())
        		createPicture.setIcon(boyGreenIcon);
        	else if (girlButton.isSelected())
        		createPicture.setIcon(girlGreenIcon);
        }
        if (e.getSource() == back){
        	changePanel("Main");
        }
        
    }
    
    

    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
    	Object[] options = {"Baseline Scenarios",
                "Normal Operation"};
    	int n = JOptionPane.showOptionDialog(null,
    			"Would you like to run baseline scenarios (with minimum interleaving)\n"
    			+ "or normal scenarios (with normal interleaving)?",
    			"Start Game",
    			JOptionPane.YES_NO_CANCEL_OPTION,
    			JOptionPane.QUESTION_MESSAGE,
    			null,
    			options,
    			options[1]);
    	switch(n) {
    		case JOptionPane.YES_OPTION:
    			startBaselineGame(); break;
    		case JOptionPane.NO_OPTION:
    			startGame(); break;
    		default:
    			break;
    	}
    }
    
    public static void startBaselineGame() {
        SimCityGui gui = new SimCityGui("Baseline");
        gui.setTitle("SimCity team007");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void startGame() {
        SimCityGui gui = new SimCityGui("Normal");
        gui.setTitle("SimCity team007");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

