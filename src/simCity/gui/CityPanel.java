package simCity.gui;

import javax.management.relation.Role;
import javax.swing.*;

import simCity.Location;
import simCity.OrdinaryPerson;
import simCity.Restaurant1.Restaurant1CustomerRole;
import simCity.bank.BankCustomerRole;
import simCity.house.HouseResidentRole;
import simCity.market.MarketCustomerRole;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;

public class CityPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

	//Static variables
    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    //Main setup
    static Grid grid;
    static SemaphoreGrid semaphoreGrid;
    static Grid busGrid;
    private SimCityGui mainGui;
    static DeliveryTruckGui dtGui;
    List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    //Icons
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/city.JPG");
    private Image image = imageIcon.getImage();

    //Buildings
    private Building housing;
    private Building housing1;
    private Building housingOffice;
    private Building apartment1;
    private Building apartment2;
    private Building apartment3;
    private Building apartment4;
    private Building market;
    private Building market1;
    private Building market2;
    private Building bank;
    private Building bank1;
    private Building restaurant1;
    private Building restaurant2;
    private Building restaurant3;
    private Building restaurant4;
    private Building restaurant5;
    private Building busStop1;
    private Building busStop2;

    //text displayer
    JLabel textDisplayer;

    public CityPanel() {
    	addMouseListener(this);
    	addMouseMotionListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(130/10,  85/10, 660/10, 450/10);
        grid.setClosedBlock(215/10,  150/10, 495/10, 305/10);
        grid.setOpenBlock(265/10,  200/10, 390/10, 210/10);
        grid.setClosedBlock(310/10,  260/10, 290/10, 90/10);
        grid.setOpenBlock(265/10,  400/10, 60/10, 80/10);
        grid.setOpenBlock(650/10, 200/10, 80/10, 60/10);
        grid.setOpenBlock(720/10, 400/10, 140/10, 200/10);
        grid.setOpenBlock(130/10, 20/10, 80/10, 70/10);
        grid.setClosedBlock(750/10, 400/10, 70/10, 60/10);
        
        semaphoreGrid = new SemaphoreGrid(WINDOWX, WINDOWY);
        
        busGrid = new Grid(WINDOWX, WINDOWY);
        busGrid.setClosedBlock(0, 0, WINDOWX/10, WINDOWY/10);
        busGrid.setOpenBlock(210/10, 150/10, 500/10, 310/10);
        busGrid.setClosedBlock(260/10, 200/10, 390/10, 210/10);
        busGrid.setOpenBlock(50/10, 20/10, 210/10, 130/10);
        busGrid.setOpenBlock(710/10, 410/10, 180/10, 190/10);
        busGrid.setClosedBlock(50/10, 70/10, 160/10, 80/10);
        busGrid.setClosedBlock(710/10, 460/10, 80/10, 140/10);
        
        
        housing = new Building(160, 530, 120, 150);
        housing1 = new Building(0, 190, 140, 80);
        housingOffice = new Building(40, 530, 120, 150);
        apartment1 = new Building(280, 530, 120, 150);
        apartment2 = new Building(400, 530, 120, 150);
        apartment3 = new Building(520, 530, 120, 150);
        apartment4 = new Building(640, 530, 120, 150);
        market = new Building(0, 270, 135, 250);
        market1 = new Building(455,  260, 145, 95);
        market2 = new Building(730, 0, 140, 90);
        bank = new Building(790, 120, 135, 220);
        bank1 = new Building(310,  260, 145, 95);
        restaurant1 = new Building(0, 100, 135, 90);
        restaurant2 = new Building(210, 0, 140, 90);
        restaurant3 = new Building(350, 0, 150, 90);
        restaurant4 = new Building(500, 0, 110, 90);
        restaurant5 = new Building(610, 0, 120, 90);
        busStop1 = new Building(180, 400, 40, 70);
        busStop2 = new Building(715, 120, 40, 70);
        
        
        //text displayer setup
        textDisplayer = new JLabel("~~~~~~~~~~~~~~");
        textDisplayer.setVisible(false);
        textDisplayer.setOpaque(true);
        textDisplayer.setFont(new Font(textDisplayer.getName(), Font.PLAIN, 24));
        textDisplayer.setBackground(Color.WHITE);
        //text displayer add
        add(textDisplayer);
        
        Timer timer = new Timer(10, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        repaint();  //Will have paintComponent called
    }
    
    public void setMain(SimCityGui obj){
    	mainGui = obj;
    }

    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (image != null)
            g.drawImage(image, 0, 0, (WINDOWX-100), (WINDOWY-30), null);

        synchronized(guis){
        	for(Gui gui : guis) {
                if (gui.isPresent()){
                    gui.updatePosition();
                }
        	}
        }
        synchronized(SimCityGui.restaurant1Panel.guis){
        	for(Gui gui : SimCityGui.restaurant1Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.restaurant2Panel.guis){
        	for(Gui gui : SimCityGui.restaurant2Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.restaurant3Panel.guis){
        	for(Gui gui : SimCityGui.restaurant3Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.restaurant4Panel.guis){
        	for(Gui gui : SimCityGui.restaurant4Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.restaurant5Panel.guis){
        	for(Gui gui : SimCityGui.restaurant5Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.marketPanel.guis){
        	for(Gui gui : SimCityGui.marketPanel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.market1Panel.guis){
        	for(Gui gui : SimCityGui.market1Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.market2Panel.guis){
        	for(Gui gui : SimCityGui.market2Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.housePanel.guis){
        	for(Gui gui : SimCityGui.housePanel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.housePanel1.guis){
        	for(Gui gui : SimCityGui.housePanel1.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.housingOfficePanel.guis){
        	for(Gui gui : SimCityGui.housingOfficePanel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.apartmentPanel1.guis){
        	for(Gui gui : SimCityGui.apartmentPanel1.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.apartmentPanel2.guis){
        	for(Gui gui : SimCityGui.apartmentPanel2.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.apartmentPanel3.guis){
        	for(Gui gui : SimCityGui.apartmentPanel3.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.apartmentPanel4.guis){
        	for(Gui gui : SimCityGui.apartmentPanel4.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.apartments.guis){
        	for(Gui gui : SimCityGui.apartments.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.bankPanel.guis){
        	for(Gui gui : SimCityGui.bankPanel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.bank1Panel.guis){
        	for(Gui gui : SimCityGui.bank1Panel.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.busStopPanel1.guis){
        	for(Gui gui : SimCityGui.busStopPanel1.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(SimCityGui.busStopPanel2.guis){
        	for(Gui gui : SimCityGui.busStopPanel2.guis){
        		if (gui.isPresent()){
        			gui.updatePosition();
        		}
        	}
        }
        synchronized(guis){
        	for(Gui gui : guis) {
        		if (gui.isPresent()) {
        			gui.draw(g2);
        		}
        	}
        }
    }
    
    public void addGui(Gui gui) {
        guis.add(gui);
    }
    
    public void removeGui(Gui gui) {
    	Gui remove = null;
    	synchronized(guis) {
	    	for (Gui g : guis) {
	    		if ( g == gui ) {
	    			remove = g;
	    			break;
	    		}
	    	}
    	}
    	if (remove != null) {
    		guis.remove(remove);
    	}
    }
    
    /**
     * displays text on the main screen
     * @param txt
     */
    public void displayText(String txt, int x, int y) {
    	textDisplayer.setVisible(true);
    	textDisplayer.setText(txt);
    	textDisplayer.setLocation(x - textDisplayer.getWidth()/2, y - textDisplayer.getHeight()/2);
    }
    public void displayText(String txt, int x, int y, int w, int h) {
    	textDisplayer.setVisible(true);
    	textDisplayer.setText(txt);
    	textDisplayer.setLocation(x + w/2 - textDisplayer.getWidth()/2, y + h/2 - textDisplayer.getHeight()/2);
    }

	public void mouseClicked(MouseEvent m) {
		//Click on person
		for (Gui gui : guis){
			if(gui instanceof PersonGui && (m.getX() >= gui.getX() && m.getX() < gui.getX() + 20
					&& m.getY() >= gui.getY() && m.getY() < gui.getY() + 20)){
				if (gui.isPresent()) {
					mainGui.updateInfoPanel(gui);
					if (((OrdinaryPerson)((PersonGui) gui).agent).roles.size() == 0) { // this means person was freshly created, no roles
						SimCityGui.controlPanel.updateButtons("Main");
					}
					for (simCity.Role role : ((OrdinaryPerson)((PersonGui)gui).agent).roles){
						if (role.active){
							if (role instanceof BankCustomerRole)
								SimCityGui.controlPanel.updateButtons("Bank");
							else if (role instanceof MarketCustomerRole)
								SimCityGui.controlPanel.updateButtons("Market");
							else if (role instanceof Restaurant1CustomerRole)
								SimCityGui.controlPanel.updateButtons("Restaurant");
							else if (role instanceof HouseResidentRole)
								SimCityGui.controlPanel.updateButtons("House");
							else 
								SimCityGui.controlPanel.updateButtons("Main");
							
							return;
						}
						else 
							SimCityGui.controlPanel.updateButtons("Main");
					}
					return;
				}
			}
		}
		
		//Click on building
		mainGui.hideInfoPanel();
		if (m.getX() >= market.getX() && m.getX() < (market.getX()+market.getWidth())
				&& m.getY() >= market.getY() && m.getY() < (market.getY()+market.getHeight())){
			mainGui.changePanel("Market");
		}
		else if (m.getX() >= market1.getX() && m.getX() < (market1.getX()+market1.getWidth())
				&& m.getY() >= market1.getY() && m.getY() < (market1.getY()+market1.getHeight())){
			mainGui.changePanel("Market1");
		}
		else if (m.getX() >= market2.getX() && m.getX() < (market2.getX()+market2.getWidth())
				&& m.getY() >= market2.getY() && m.getY() < (market2.getY()+market2.getHeight())){
			mainGui.changePanel("Market2");
		}
		else if (m.getX() >= bank.getX() && m.getX() < (bank.getX()+bank.getWidth()) 
				&& m.getY() >= bank.getY() && m.getY() < (bank.getY()+bank.getHeight())){
			mainGui.changePanel("Bank");
		}
		else if (m.getX() >= bank1.getX() && m.getX() < (bank1.getX()+bank1.getWidth()) 
				&& m.getY() >= bank1.getY() && m.getY() < (bank1.getY()+bank1.getHeight())){
			mainGui.changePanel("Bank1");
		}
		else if (m.getX() >= housing.getX() && m.getX() < (housing.getX()+housing.getWidth()) 
				&& m.getY() >= housing.getY() && m.getY() < (housing.getY()+housing.getHeight())){
			mainGui.changePanel("House");
		}
		else if (m.getX() >= housing1.getX() && m.getX() < (housing1.getX()+housing1.getWidth()) 
				&& m.getY() >= housing1.getY() && m.getY() < (housing1.getY()+housing1.getHeight())){
			mainGui.changePanel("House1");
		}
		else if (m.getX() >= housingOffice.getX() && m.getX() < (housingOffice.getX()+housingOffice.getWidth()) 
				&& m.getY() >= housingOffice.getY() && m.getY() < (housingOffice.getY()+housingOffice.getHeight())){
			mainGui.changePanel("HousingOffice");
		}
		else if (m.getX() >= apartment1.getX() && m.getX() < (apartment1.getX()+apartment1.getWidth()) 
				&& m.getY() >= apartment1.getY() && m.getY() < (apartment1.getY()+apartment1.getHeight())){
			mainGui.changePanel("Apartment1");
		}
		else if (m.getX() >= apartment2.getX() && m.getX() < (apartment2.getX()+apartment2.getWidth()) 
				&& m.getY() >= apartment2.getY() && m.getY() < (apartment2.getY()+apartment2.getHeight())){
			mainGui.changePanel("Apartment2");
		}
		else if (m.getX() >= apartment3.getX() && m.getX() < (apartment3.getX()+apartment3.getWidth()) 
				&& m.getY() >= apartment3.getY() && m.getY() < (apartment3.getY()+apartment3.getHeight())){
			mainGui.changePanel("Apartment3");
		}
		else if (m.getX() >= apartment4.getX() && m.getX() < (apartment4.getX()+apartment4.getWidth()) 
				&& m.getY() >= apartment4.getY() && m.getY() < (apartment4.getY()+apartment4.getHeight())){
			mainGui.changePanel("Apartment4");
		}
		else if (m.getX() >= restaurant1.getX() && m.getX() < (restaurant1.getX()+restaurant1.getWidth()) 
				&& m.getY() >= restaurant1.getY() && m.getY() < (restaurant1.getY()+restaurant1.getHeight())){
			mainGui.changePanel("Restaurant1");
		}
		else if (m.getX() >= restaurant2.getX() && m.getX() < (restaurant2.getX()+restaurant2.getWidth()) 
				&& m.getY() >= restaurant2.getY() && m.getY() < (restaurant2.getY()+restaurant2.getHeight())){
			mainGui.changePanel("Restaurant2");
		}
		else if (m.getX() >= restaurant3.getX() && m.getX() < (restaurant3.getX()+restaurant3.getWidth()) 
				&& m.getY() >= restaurant3.getY() && m.getY() < (restaurant3.getY()+restaurant3.getHeight())){
			mainGui.changePanel("Restaurant3");
		}
		else if (m.getX() >= restaurant4.getX() && m.getX() < (restaurant4.getX()+restaurant4.getWidth()) 
				&& m.getY() >= restaurant4.getY() && m.getY() < (restaurant4.getY()+restaurant4.getHeight())){
			mainGui.changePanel("Restaurant4");
		}
		else if (m.getX() >= restaurant5.getX() && m.getX() < (restaurant5.getX()+restaurant5.getWidth()) 
				&& m.getY() >= restaurant5.getY() && m.getY() < (restaurant5.getY()+restaurant5.getHeight())){
			mainGui.changePanel("Restaurant5");
		}
		else if (m.getX() >= busStop1.getX() && m.getX() < (busStop1.getX()+busStop1.getWidth()) 
				&& m.getY() >= busStop1.getY() && m.getY() < (busStop1.getY()+busStop1.getHeight())){
			mainGui.changePanel("BusStop1");
		}
		else if (m.getX() >= busStop2.getX() && m.getX() < (busStop2.getX()+busStop2.getWidth()) 
				&& m.getY() >= busStop2.getY() && m.getY() < (busStop2.getY()+busStop2.getHeight())){
			mainGui.changePanel("BusStop2");
		}
	}

	@Override
	public void mouseEntered(MouseEvent m) {
		// TODO Auto-generated method stub
	}
	
	public void mouseMoved(MouseEvent m) {
		if (m.getX() >= market.getX() && m.getX() < (market.getX()+market.getWidth())
				&& m.getY() >= market.getY() && m.getY() < (market.getY()+market.getHeight())){
			displayText("Market", market.getX(), market.getY(), market.getWidth(), market.getHeight());
		}
		else if (m.getX() >= market1.getX() && m.getX() < (market1.getX()+market1.getWidth())
				&& m.getY() >= market1.getY() && m.getY() < (market1.getY()+market1.getHeight())){
			displayText("Market1", market1.getX(), market1.getY(), market1.getWidth(), market1.getHeight());
		}
		else if (m.getX() >= market2.getX() && m.getX() < (market2.getX()+market2.getWidth())
				&& m.getY() >= market2.getY() && m.getY() < (market2.getY()+market2.getHeight())){
			displayText("Market2", market2.getX(), market2.getY(), market2.getWidth(), market2.getHeight());
		}
		else if (m.getX() >= bank.getX() && m.getX() < (bank.getX()+bank.getWidth()) 
				&& m.getY() >= bank.getY() && m.getY() < (bank.getY()+bank.getHeight())){
			displayText("Bank", bank.getX(), bank.getY(), bank.getWidth(), bank.getHeight());
		}
		else if (m.getX() >= bank1.getX() && m.getX() < (bank1.getX()+bank1.getWidth()) 
				&& m.getY() >= bank1.getY() && m.getY() < (bank1.getY()+bank1.getHeight())){
			displayText("Bank1", bank1.getX(), bank1.getY(), bank1.getWidth(), bank1.getHeight());
		}
		else if (m.getX() >= housing.getX() && m.getX() < (housing.getX()+housing.getWidth()) 
				&& m.getY() >= housing.getY() && m.getY() < (housing.getY()+housing.getHeight())){
			displayText("House", housing.getX(), housing.getY(), housing.getWidth(), housing.getHeight());
		}
		else if (m.getX() >= housing1.getX() && m.getX() < (housing1.getX()+housing1.getWidth()) 
				&& m.getY() >= housing1.getY() && m.getY() < (housing1.getY()+housing1.getHeight())){
			displayText("House1", housing1.getX(), housing1.getY(), housing1.getWidth(), housing1.getHeight());
		}
		else if (m.getX() >= housingOffice.getX() && m.getX() < (housingOffice.getX()+housingOffice.getWidth()) 
				&& m.getY() >= housingOffice.getY() && m.getY() < (housingOffice.getY()+housingOffice.getHeight())){
			displayText("HousingOffice", housingOffice.getX(), housingOffice.getY(), housingOffice.getWidth(), housingOffice.getHeight());
		}
		else if (m.getX() >= apartment1.getX() && m.getX() < (apartment1.getX()+apartment1.getWidth()) 
				&& m.getY() >= apartment1.getY() && m.getY() < (apartment1.getY()+apartment1.getHeight())){
			displayText("ApartmentComplex1", apartment1.getX(), apartment1.getY(), apartment1.getWidth(),apartment1.getHeight());
		}
		else if (m.getX() >= apartment2.getX() && m.getX() < (apartment2.getX()+apartment2.getWidth()) 
				&& m.getY() >= apartment2.getY() && m.getY() < (apartment2.getY()+apartment2.getHeight())){
			displayText("ApartmentComplex2", apartment2.getX(), apartment2.getY(), apartment2.getWidth(),apartment2.getHeight());
		}
		else if (m.getX() >= apartment3.getX() && m.getX() < (apartment3.getX()+apartment3.getWidth()) 
				&& m.getY() >= apartment3.getY() && m.getY() < (apartment3.getY()+apartment3.getHeight())){
			displayText("ApartmentComplex3", apartment3.getX(), apartment3.getY(), apartment3.getWidth(),apartment3.getHeight());
		}
		else if (m.getX() >= apartment4.getX() && m.getX() < (apartment4.getX()+apartment4.getWidth()) 
				&& m.getY() >= apartment4.getY() && m.getY() < (apartment4.getY()+apartment4.getHeight())){
			displayText("ApartmentComplex4", apartment4.getX(), apartment4.getY(), apartment4.getWidth(),apartment4.getHeight());
		}
		else if (m.getX() >= restaurant1.getX() && m.getX() < (restaurant1.getX()+restaurant1.getWidth()) 
				&& m.getY() >= restaurant1.getY() && m.getY() < (restaurant1.getY()+restaurant1.getHeight())){
			displayText("Restaurant1", restaurant1.getX(), restaurant1.getY(), restaurant1.getWidth(), restaurant1.getHeight());
		}
		else if (m.getX() >= restaurant2.getX() && m.getX() < (restaurant2.getX()+restaurant2.getWidth()) 
				&& m.getY() >= restaurant2.getY() && m.getY() < (restaurant2.getY()+restaurant2.getHeight())){
			displayText("Restaurant2", restaurant2.getX(), restaurant2.getY(), restaurant2.getWidth(), restaurant2.getHeight());
		}
		else if (m.getX() >= restaurant3.getX() && m.getX() < (restaurant3.getX()+restaurant3.getWidth()) 
				&& m.getY() >= restaurant3.getY() && m.getY() < (restaurant3.getY()+restaurant3.getHeight())){
			displayText("Restaurant3", restaurant3.getX(), restaurant3.getY(), restaurant3.getWidth(), restaurant3.getHeight());
		}
		else if (m.getX() >= restaurant4.getX() && m.getX() < (restaurant4.getX()+restaurant4.getWidth()) 
				&& m.getY() >= restaurant4.getY() && m.getY() < (restaurant4.getY()+restaurant4.getHeight())){
			displayText("Restaurant4", restaurant4.getX(), restaurant4.getY(), restaurant4.getWidth(), restaurant4.getHeight());
		}
		else if (m.getX() >= restaurant5.getX() && m.getX() < (restaurant5.getX()+restaurant5.getWidth()) 
				&& m.getY() >= restaurant5.getY() && m.getY() < (restaurant5.getY()+restaurant5.getHeight())){
			displayText("Restaurant5", restaurant5.getX(), restaurant5.getY(), restaurant5.getWidth(), restaurant5.getHeight());
		}
		else {
			textDisplayer.setVisible(false);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
