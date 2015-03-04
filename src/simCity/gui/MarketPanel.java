package simCity.gui;

import javax.swing.*;

import agent.Agent;
import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.interfaces.*;
import simCity.market.MarketCashierRole;
import simCity.market.MarketCustomerRole;
import simCity.market.MarketDeliveryTruck;
import simCity.market.MarketGrocerRole;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MarketPanel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    static Grid grid;
    private SimCityGui mainGui;
    //private MarketGrocer mg2;
    //private MarketCashier mc2;
   // OrdinaryPerson ordinaryp = new OrdinaryPerson("MarketCustomer", 40.0);
   // Role role = new MarketCustomerRole("MarketCustomerROle");
    
    public MarketGrocerRole grocer;
    public MarketCashierRole cashier;
    Role mgr1; // MarketGrocerRole
    Role mcr1; // MarketCustomerRole
    public MarketDeliveryTruck mdTruck;//= new MarketDeliveryTruck("Delivery Truck");
   // Agent mdt; // MarketDeliveryTruckRole
   
    
    public List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    /* Creating MarketGrocer Guis */
    //private MarketGrocerGui mgGui2 = new MarketGrocerGui(mg2, mainGui);
    //private MarketGrocerGui mgGui3 = new MarketGrocerGui(mg, mainGui);
    //private MarketGrocerGui mgGui4 = new MarketGrocerGui(mg, mainGui);
    /* Creating MarketCashier Guis */
    //private MarketCashierGui mcGui2 = new MarketCashierGui(mc2, mainGui);
    //private MarketCashierGui mcashGui3 = new MarketCashierGui(mcash, mainGui);
    //private MarketCashierGui mcashGui4 = new MarketCashierGui(mcash, mainGui);
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/marketGuiv2.PNG");//("src/simCity/gui/images/marketGuiv1.png");
        private Image image = imageIcon.getImage();


    public MarketPanel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        mdTruck = new MarketDeliveryTruck("DeliveryTruck");
        System.out.println("MDTRUCK initialized");
        mdTruck.startThread();
        mdTruck.setGui(CityPanel.dtGui);
        
        /*
>>>>>>> Albert
        mgr1 = new MarketGrocerRole("MarketGrocer", mdTruck);
        System.out.println("mgr1 initialized");

        mgr1 = new MarketGrocerRole("MarketGrocer");
        mcr1 = new MarketCashierRole("MarketCashier");
        OrdinaryPerson mg1 = new OrdinaryPerson("MarketGrocer", 100);
        mg1.noPersonState();
        mgr1.setPersonAgent(mg1);
        mgr1.active = true;
        mg1.roles.add(mgr1);
        mg1.startThread();
        
        OrdinaryPerson mc1 = new OrdinaryPerson("MarketCashier", 100);
        mc1.noPersonState();
        mcr1.setPersonAgent(mc1);
        mcr1.active = true;
        mc1.roles.add(mcr1);
        mc1.startThread();
        

        mdTruck.setGui(CityPanel.dtGui);
 
        MarketGrocerGui mgGui1 = new MarketGrocerGui((MarketGrocerRole) mgr1, mainGui);
        MarketCashierGui mcGui1 = new MarketCashierGui((MarketCashierRole) mcr1, mainGui);
   
        
        ((MarketGrocerRole) mgr1).setCashier((MarketCashierRole) mcr1);
        ((MarketCashierRole) mcr1).setGrocer((MarketGrocerRole) mgr1);
        ((MarketGrocerRole) mgr1).setGui(mgGui1);
        ((MarketCashierRole) mcr1).setGui(mcGui1);
        ((MarketGrocerRole) mgr1).setDeliveryTruck(mdTruck);
        ((MarketCashierRole) mcr1).setDeliveryTruck(mdTruck);
        
        grocer = (MarketGrocerRole) mgr1;
        
        System.out.println("market grocer pointed to mgr1");
        
        */
        /* Adding MarketGrocers into the gui */
        //addGui(mgGui1);
        //mgGui1.setPresent(true);
        //addGui(mgGui2);
        //mgGui2.setPresent(true);
        
        /*
        addGui(mgGui3);
        mgGui3.setPresent(true);
        addGui(mgGui4);
        mgGui4.setPresent(true);
        */
        
        /* Adding MarketCashiers into the gui */
        //addGui(mcGui1);
        //mcGui1.setPresent(true);
        //addGui(mcGui2);
        //mcGui2.setPresent(true);
        
        /*
        addGui(mcashGui3);
        mcashGui3.setPresent(true);
        addGui(mcashGui4);
        mcashGui4.setPresent(true);
        */
        
        
        grid = new Grid(WINDOWX, WINDOWY);
        
        Timer timer = new Timer(10, this );
        timer.start();
    }
    
    // WILL SET ALL DEPENDENCES FOR THAT MARKETCUSTOMER (all marketGrocers will know about customer,
    // all marketCashiers will know about customer and vice versa
	public void newMarketCustomer(Role r) {
		((MarketCustomerRole) r).setGrocer((MarketGrocerRole) grocer);
		((MarketCustomerRole) r).setCashier((MarketCashierRole) cashier);
		
	}
	
	public void setGrocer(MarketGrocerRole m1gr) {
		grocer = m1gr;
	}
	
	public void setCashier(MarketCashierRole m1cr) {
		cashier = m1cr;
	}
	
	public void setDeliveryTruck(MarketDeliveryTruck mdt) {
		mdTruck = mdt;
	}

    public void actionPerformed(ActionEvent e) {
        repaint();  //Will have paintComponent called
    }
    
    public void setMain(SimCityGui obj){
    	mainGui = obj;
    }

    public void paintComponent(Graphics g) {
    	SimCityGui.cityPanel.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (image != null)
            g.drawImage(image, -150, 0, WINDOWX, WINDOWY, null);

        synchronized (guis) {
	        for(Gui gui : guis) {
	                if (gui.isPresent()){
	                        gui.updatePosition();
	                }
	        }
        }

        synchronized (guis) {
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
    	synchronized (guis) {
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

	public void mouseClicked(MouseEvent m) {
		for (Gui gui : guis){
			if(m.getX() >= gui.getX() && m.getX() < gui.getX() + 20
					&& m.getY() >= gui.getY() && m.getY() < gui.getY() + 20){
				mainGui.updateInfoPanel(gui);
			}
		}
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
	}

	@Override
	public void mouseEntered(MouseEvent e) {
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
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
