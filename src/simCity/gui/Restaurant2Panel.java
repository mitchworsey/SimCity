package simCity.gui;

import javax.swing.*;

import agent.Agent;
import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.Restaurant1.Restaurant1CashierRole;
import simCity.Restaurant1.Restaurant1CookRole;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.Restaurant1.Restaurant1WaiterRole;
import simCity.gui.Restaurant1.Restaurant1CookGui;
import simCity.gui.Restaurant1.Restaurant1WaiterGui;
import simCity.restaurant2.Restaurant2CashierRole;
import simCity.restaurant2.Restaurant2CookRole;
import simCity.restaurant2.Restaurant2HostRole;
import simCity.restaurant2.Restaurant2MarketRole;
import simCity.restaurant2.Restaurant2NormalWaiterRole;
import simCity.restaurant2.Restaurant2PCWaiterRole;
import simCity.restaurant2.Restaurant2SharedDataWheel;
import simCity.restaurant2.Restaurant2WaiterRole;
import simCity.restaurant2.gui.Restaurant2CookGui;
import simCity.restaurant2.gui.Restaurant2CustomerGui;
import simCity.restaurant2.gui.Restaurant2HostGui;
import simCity.restaurant2.gui.Restaurant2WaiterGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Restaurant2Panel extends JPanel implements ActionListener, MouseListener {

	private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    
    private final int TABLEX = 50;
    private final int TABLEY = 50;
    
    private final int TABLE1X = 200;
    private final int TABLE1Y = 200;
    
    private final int TABLE2X = 300;
    private final int TABLE2Y = 400;
    
    private final int TABLE3X = 400;
    private final int TABLE3Y = 200;
    
    private final int TABLE4X = 500;
    private final int TABLE4Y = 400;
    
    private final int WAITINGX = 0;
    private final int WAITINGY = 40;
    private final int WAITINGWIDTH = 50;
    private final int WAITINGHEIGHT = 560;
    
    private final int CHAIRWIDTH = 26;
    private final int CHAIRHEIGHT = 26;
    
    private final int CHEFGRILLX = 850;
    private final int CHEFGRILLY = 280;
    private final int CHEFGRILLHEIGHT = 160;
    private final int CHEFGRILLWIDTH = 40;
    
    private final int PLATEGRILLX = 730;
    private final int PLATEGRILLY = 280;
    private final int PLATEGRILLWIDTH = 40;
    private final int PLATEGRILLHEIGHT = 160;
    
    private final int WHEELX = 795;
    private final int WHEELY = 220;
    private final int WHEELWIDTH = 50;
    private final int WHEELHEIGHT = 50;
    
    
    
    
 // CREATE 1 HARDCODED HOST
    Restaurant2HostRole host;
    Restaurant2HostGui hostGui;
    public static Restaurant2SharedDataWheel sharedDataWheel;
    Restaurant2CashierRole cashier;
    
    // CREATE 1 HARDCODED COOK FOR NOW
    Restaurant2CookGui cookGui;
    
    
    private List<Role> restaurant2Roles = new ArrayList<Role>();
    public boolean customerSeatOpen[] = new boolean[15];
   
    
    
    static Grid grid;
    private SimCityGui mainGui;
    
    List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private int waiterHomePos = 80;
    /*
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/restaurant2.png");
        private Image image = imageIcon.getImage();
	*/

    public Restaurant2Panel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(0, 0, WINDOWX/10, WINDOWY/10);
        
        Timer timer = new Timer(15, this );
        timer.start();
        
        
        for (int i=0; i<15; i++) {
    		customerSeatOpen[i] = true;
    	}
    	
        sharedDataWheel = new Restaurant2SharedDataWheel();
        
    	// Initialize HostAgent & CookAgent
    	
        /*OrdinaryPerson h = new OrdinaryPerson("Sarah", 100);
        h.noPersonState();
        Role hr = new Restaurant2HostRole("Sarah");
        hostGui = new Restaurant2HostGui((Restaurant2HostRole) hr);
        ((Restaurant2HostRole) hr).setGui(hostGui);
        hr.setPersonAgent(h);
        hr.active = true;
        h.roles.add(hr);
    	
    	restaurant2Roles.add(hr);
        addGui((Gui) hostGui);
        setHost((Restaurant2HostRole) hr);
    	
        
    	OrdinaryPerson ck = new OrdinaryPerson("Kevin", 100);
    	ck.noPersonState();
    	Role ckr = new Restaurant2CookRole("Kevin", sharedDataWheel);
        cookGui = new Restaurant2CookGui((Restaurant2CookRole) ckr);
        ((Restaurant2CookRole) ckr).setGui(cookGui);
    	ckr.setPersonAgent(ck);
    	ckr.active = true;
    	ck.roles.add(ckr);
    	
    	restaurant2Roles.add(ckr);
        addGui((Gui) cookGui);
        
        
        OrdinaryPerson cs = new OrdinaryPerson("Sarah", 100);
    	cs.noPersonState();
    	Role csr = new Restaurant2CashierRole("Sarah");
    	csr.setPersonAgent(cs);
    	csr.active = true;
    	cs.roles.add(csr);
    	
    	restaurant2Roles.add(csr);
    	setCashier((Restaurant2CashierRole) csr);
    	
    	OrdinaryPerson w1 = new OrdinaryPerson("Tim", 100);
    	w1.noPersonState();
    	Role w1r = new Restaurant2NormalWaiterRole("Tim");
    	w1r.setPersonAgent(w1);
    	w1r.active = true;
    	w1.roles.add(w1r);
    	
    	restaurant2Roles.add(w1r);
    	
    	OrdinaryPerson w2 = new OrdinaryPerson("Jen", 100);
    	w2.noPersonState();
    	Role w2r = new Restaurant2PCWaiterRole("Jen", sharedDataWheel);
    	w2r.setPersonAgent(w2);
    	w2r.active = true;
    	w2.roles.add(w2r);
    	
    	restaurant2Roles.add(w2r);
   
    	OrdinaryPerson w3 = new OrdinaryPerson("Karissa", 100);
    	w3.noPersonState();
    	Role w3r = new Restaurant2NormalWaiterRole("Karissa");
    	w3r.setPersonAgent(w3);
    	w3r.active = true;
    	w3.roles.add(w3r);
    	
    	restaurant2Roles.add(w3r);
    	
    	OrdinaryPerson w4 = new OrdinaryPerson("Bob", 100);
    	w4.noPersonState();
    	Role w4r = new Restaurant2PCWaiterRole("Bob", sharedDataWheel);
    	w4r.setPersonAgent(w4);
    	w4r.active = true;
    	w4.roles.add(w4r);
    	
    	restaurant2Roles.add(w4r);
    
    	((Restaurant2HostRole) hr).addWaiter((Restaurant2WaiterRole) w1r);
    	((Restaurant2HostRole) hr).addWaiter((Restaurant2WaiterRole) w2r);
    	((Restaurant2HostRole) hr).addWaiter((Restaurant2WaiterRole) w3r);
    	((Restaurant2HostRole) hr).addWaiter((Restaurant2WaiterRole) w4r);
    	
    	Restaurant2WaiterGui w1g = new Restaurant2WaiterGui(((Restaurant2WaiterRole) w1r));
    	Restaurant2WaiterGui w2g = new Restaurant2WaiterGui(((Restaurant2WaiterRole) w2r));
    	Restaurant2WaiterGui w3g = new Restaurant2WaiterGui(((Restaurant2WaiterRole) w3r));
    	Restaurant2WaiterGui w4g = new Restaurant2WaiterGui(((Restaurant2WaiterRole) w4r));
    	
    	((Restaurant2WaiterRole) w1r).setGui(w1g);
    	((Restaurant2WaiterRole) w2r).setGui(w2g);
    	((Restaurant2WaiterRole) w3r).setGui(w3g);
    	((Restaurant2WaiterRole) w4r).setGui(w4g);
    	
    	((Restaurant2WaiterRole) w1r).setCook((Restaurant2CookRole) ckr);
    	((Restaurant2WaiterRole) w1r).setHost((Restaurant2HostRole) hr);
    	((Restaurant2WaiterRole) w1r).setCashier((Restaurant2CashierRole) csr);
    	
    	((Restaurant2WaiterRole) w2r).setCook((Restaurant2CookRole) ckr);
    	((Restaurant2WaiterRole) w2r).setHost((Restaurant2HostRole) hr);
    	((Restaurant2WaiterRole) w2r).setCashier((Restaurant2CashierRole) csr);
    	
    	((Restaurant2WaiterRole) w3r).setCook((Restaurant2CookRole) ckr);
    	((Restaurant2WaiterRole) w3r).setHost((Restaurant2HostRole) hr);
    	((Restaurant2WaiterRole) w3r).setCashier((Restaurant2CashierRole) csr);
    	
    	((Restaurant2WaiterRole) w4r).setCook((Restaurant2CookRole) ckr);
    	((Restaurant2WaiterRole) w4r).setHost((Restaurant2HostRole) hr);
    	((Restaurant2WaiterRole) w4r).setCashier((Restaurant2CashierRole) csr);

        addGui((Gui) w1g);
        addGui((Gui) w2g);
        addGui((Gui) w3g);
        addGui((Gui) w4g);
        


    	
        // Initialize MarketAgent & CookAgent
        OrdinaryPerson m1 = new OrdinaryPerson("Market1", 100);
    	m1.noPersonState();
    	Role m1r = new Restaurant2MarketRole("Market1");
    	m1r.setPersonAgent(m1);
    	m1r.active = true;
    	m1.roles.add(m1r);
    	
    	restaurant2Roles.add(m1r);
    	
    	OrdinaryPerson m2 = new OrdinaryPerson("Market2", 100);
    	m2.noPersonState();
    	Role m2r = new Restaurant2MarketRole("Market2");
    	m2r.setPersonAgent(m2);
    	m2r.active = true;
    	m2.roles.add(m2r);
    	
    	restaurant2Roles.add(m2r);
    	
    	OrdinaryPerson m3 = new OrdinaryPerson("Market3", 100);
    	m3.noPersonState();
    	Role m3r = new Restaurant2MarketRole("Market3");
    	m3r.setPersonAgent(m3);
    	m3r.active = true;
    	m3.roles.add(m3r);
    	
    	restaurant2Roles.add(m3r);
      
    	((Restaurant2MarketRole) m1r).setCashier((Restaurant2CashierRole) csr);
    	((Restaurant2MarketRole) m2r).setCashier((Restaurant2CashierRole) csr);
    	((Restaurant2MarketRole) m3r).setCashier((Restaurant2CashierRole) csr);
        
    	((Restaurant2CookRole) ckr).addMarket((Restaurant2MarketRole) m1r);
    	((Restaurant2CookRole) ckr).addMarket((Restaurant2MarketRole) m2r);
    	((Restaurant2CookRole) ckr).addMarket((Restaurant2MarketRole) m3r);
        
    	
    	
    	h.startThread();
    	ck.startThread();
    	cs.startThread();
    	w1.startThread();
    	w2.startThread();
    	w3.startThread();
    	w4.startThread();
    	m1.startThread();
    	m2.startThread();
    	m3.startThread();*/
    }

    public void actionPerformed(ActionEvent e) {
        repaint();  //Will have paintComponent called
    }
    
    public void setMain(SimCityGui obj){
    	mainGui = obj;
    }

    public void paintComponent(Graphics g) {
    	SimCityGui.cityPanel.paintComponent(g);
    	//super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        /*
        if (image != null)
            g.drawImage(image, 0, 0, WINDOWX-100, WINDOWY-30, null);
		*/

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, 0, WINDOWX, WINDOWY);

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLE1X, TABLE1Y, TABLEX, TABLEY);
        g2.fillRect(TABLE2X, TABLE2Y, TABLEX, TABLEY);
        g2.fillRect(TABLE3X, TABLE3Y, TABLEX, TABLEY);
        g2.fillRect(TABLE4X, TABLE4Y, TABLEX, TABLEY);
        
        // Here is the customer waiting area
        g2.setColor(Color.GRAY);
        g2.fillRect(WAITINGX, WAITINGY, WAITINGWIDTH, WAITINGHEIGHT);
        
        // Restaurant2Customer seats
        for (int i = WAITINGY + 6; i < WAITINGY - 24 + WAITINGHEIGHT; i = i + 30) {
        	g2.setColor(Color.BLACK);
        	g2.fillOval(WAITINGX, i, CHAIRWIDTH, CHAIRHEIGHT);
        	g2.setColor(Color.WHITE);
        	g2.drawOval(WAITINGX, i, CHAIRWIDTH, CHAIRHEIGHT);
        }
        
        // Here is the cook's bar
        // Draw The grill
        g2.setColor(Color.BLACK);
        g2.fillRect(CHEFGRILLX, CHEFGRILLY, CHEFGRILLWIDTH, CHEFGRILLHEIGHT);
        g2.setColor(Color.GRAY);
        g2.drawRect(CHEFGRILLX, CHEFGRILLY, CHEFGRILLWIDTH, CHEFGRILLHEIGHT/4);
        g2.drawRect(CHEFGRILLX, CHEFGRILLY + CHEFGRILLHEIGHT/4, CHEFGRILLWIDTH, CHEFGRILLHEIGHT/4);
        g2.drawRect(CHEFGRILLX, CHEFGRILLY + CHEFGRILLHEIGHT/2, CHEFGRILLWIDTH, CHEFGRILLHEIGHT/4);
        g2.drawRect(CHEFGRILLX, CHEFGRILLY + 3*CHEFGRILLHEIGHT/4, CHEFGRILLWIDTH, CHEFGRILLHEIGHT/4);
        
        // Draw the plating area
        g2.fillRect(PLATEGRILLX, PLATEGRILLY, PLATEGRILLWIDTH, PLATEGRILLHEIGHT);
        g2.setColor(Color.WHITE);
        g2.drawRect(PLATEGRILLX, PLATEGRILLY, PLATEGRILLWIDTH, PLATEGRILLHEIGHT/4);
        g2.drawRect(PLATEGRILLX, PLATEGRILLY + PLATEGRILLHEIGHT/4, PLATEGRILLWIDTH, PLATEGRILLHEIGHT/4);
        g2.drawRect(PLATEGRILLX, PLATEGRILLY + PLATEGRILLHEIGHT/2, PLATEGRILLWIDTH, PLATEGRILLHEIGHT/4);
        g2.drawRect(PLATEGRILLX, PLATEGRILLY + 3*PLATEGRILLHEIGHT/4, PLATEGRILLWIDTH, PLATEGRILLHEIGHT/4);
        
        // Draw the shared data wheel
        g2.drawOval(WHEELX, WHEELY, WHEELWIDTH, WHEELHEIGHT);
        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(WHEELX, WHEELY, WHEELWIDTH, WHEELHEIGHT);

        synchronized(guis) {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
        }

        synchronized(guis) {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }
    
    public void addGui(Gui gui) {
        guis.add(gui);
        
        if (gui instanceof Restaurant2CustomerGui) {
        	int index = 0;
    		for (int i=0; i<15; i++) {
    			if (customerSeatOpen[i]) {
    				index = i;
    				customerSeatOpen[i] = false;
    				break;
    			}
    		}
    		((Restaurant2CustomerGui) gui).yWaiting = 50 + 30*index;
    		((Restaurant2CustomerGui) gui).setRestaurantPanel(this);
        }
        
        if (gui instanceof Restaurant2WaiterGui) {
        	((Restaurant2WaiterGui) gui).createTable(1 , TABLE1X, TABLE1Y);
        	((Restaurant2WaiterGui) gui).createTable(2 , TABLE2X, TABLE2Y);
        	((Restaurant2WaiterGui) gui).createTable(3 , TABLE3X, TABLE3Y);
        	((Restaurant2WaiterGui) gui).createTable(4 , TABLE4X, TABLE4Y);
        	((Restaurant2WaiterGui) gui).xHome = waiterHomePos;
    		waiterHomePos = waiterHomePos + 24;
        }
    }
    
    
    public void removeGui(Gui gui) {
    	synchronized(guis) {
    		guis.remove(gui);
    	}
    }

	public void mouseClicked(MouseEvent m) {
		for (Gui gui : guis){
			if(m.getX() >= gui.getX() && m.getX() < gui.getX() + 20
					&& m.getY() >= gui.getY() && m.getY() < gui.getY() + 20){
				mainGui.updateInfoPanel(gui);
			}
		}
		/*
		for (int i = 180; i < 330; i++){
        	for (int j = 200; j < 380; j++){
        		grid.set(i/10,j/10);
        	}
        }
        for (int i = 580; i < 730; i++){
        	for (int j = 200; j < 380; j++){
        		grid.set(i/10,j/10);
        	}
        }
        for (int j = 125; j < 175; j++){
        	for (int i = 100; i < 150; i++){
        		grid.set(i/10,j/10);
        	}
        	for (int i = 200; i < 600; i++){
        		grid.set(i/10, j/10);
        	}
    	}
    	*/
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
	
	public void setHost(Restaurant2HostRole role) {
		host = role;
	}
	
	public void setCashier(Restaurant2CashierRole role) {
		cashier = role;
	}
}
