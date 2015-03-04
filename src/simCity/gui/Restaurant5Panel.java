package simCity.gui;

import javax.swing.*;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.market.MarketGrocerRole;
import simCity.restaurant5.*;
import simCity.restaurant5.gui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Restaurant5Panel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    static Grid grid;
    private SimCityGui mainGui;
    
    // Creating hardcoded people
    Restaurant5HostRole host;
    Restaurant5CashierRole cashier;
    //MarketGrocerRole mgrocer;

    // Hardcoding guis
    Restaurant5HostGui hostGui;
    Restaurant5CookGui cookGui;
    
    List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());


    private int tableX = 208;
	private int tableY = 375;
	private int tableX2 = 502;
	private int tableY2 = 150;
	private int tableW = 55;
	private int tableZ = 105;
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/Restaurant5Background.jpg");
        private Image image = imageIcon.getImage();

    public static Restaurant5SharedDataWheel sharedDataWheel;

    /*
    public void setRestaurantGrocer(MarketGrocerRole marketgrocer){
    	mgrocer = marketgrocer;
    }
    */
    
    public Restaurant5Panel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        sharedDataWheel = new Restaurant5SharedDataWheel();
        
        grid = new Grid(WINDOWX, WINDOWY);
        
        Timer timer = new Timer(10, this );
        timer.start();
        
        /* Hacking people for jobs */
        /* ~~ HOST ~~ */
        OrdinaryPerson h = new OrdinaryPerson("Jesse Pinkman", 100);
        h.noPersonState();
        Role hr = new Restaurant5HostRole("Jesse Pinkman");
        hostGui = new Restaurant5HostGui((Restaurant5HostRole) hr);
        guis.add(hostGui);
        ((Restaurant5HostRole) hr).setGui(hostGui);
        hr.setPersonAgent(h);
        hr.active = true;
        h.roles.add(hr);
        h.startThread();
        /* ~~ COOK ~~ */
        OrdinaryPerson ck = new OrdinaryPerson("Walter White", 100);
        ck.noPersonState();
        Role ckr = new Restaurant5CookRole("Walter White", sharedDataWheel, SimCityGui.marketPanel.grocer);
        System.out.println("rest5cookrole instantiated & given mgrocer pointer");
        System.out.println("marketgrocer" + SimCityGui.marketPanel.grocer);
        cookGui = new Restaurant5CookGui(((Restaurant5CookRole) ckr), this, 1);
        guis.add(cookGui);
        ((Restaurant5CookRole) ckr).setGui(cookGui);;
        ckr.setPersonAgent(ck);
        ckr.active = true;
        ((Restaurant5CookRole) ckr).setGrocer(SimCityGui.marketPanel.grocer);
        ck.roles.add(ckr);
        ck.startThread();
        /* ~~ CASHIER ~~ */
        OrdinaryPerson cs = new OrdinaryPerson("Skyler White", 100);
        cs.noPersonState();
        Role csr = new Restaurant5CashierRole("Skyler White"); 
        csr.setPersonAgent(cs);
        csr.active = true;
        cs.roles.add(csr);
        cs.startThread();
        /* ~~ WAITER #1 ~~ */
        OrdinaryPerson w1 = new OrdinaryPerson("Hank Schrader", 100);
        w1.noPersonState();
        Role w1r = new Restaurant5NormalWaiterRole("Hank Schrader"); // NORMAL
        w1r.setPersonAgent(w1);
        w1r.active = true;
        w1.roles.add(w1r);
        w1.startThread();
        /* ~~ WAITER #2 ~~ */
        OrdinaryPerson w2 = new OrdinaryPerson("Walter White, Jr.", 100);
        w2.noPersonState();
        Role w2r = new Restaurant5PCWaiterRole("Walter White, Jr.", sharedDataWheel);// NORMAL
        w2r.setPersonAgent(w2);
        w2r.active = true;
        w2.roles.add(w2r);
        w2.startThread();
        /* ~~ WAITER #3 ~~ */
        OrdinaryPerson w3 = new OrdinaryPerson("Skinny Pete", 100);
        w3.noPersonState();
        Role w3r = new Restaurant5WaiterRole("Skinny Pete");// NORMAL
        w3r.setPersonAgent(w3);
        w3r.active = true;
        w3.roles.add(w3r);
        w3.startThread();
        /* ~~ WAITER #4 ~~ */
        OrdinaryPerson w4 = new OrdinaryPerson("Badger", 100);
        w4.noPersonState();
        Role w4r = new Restaurant5WaiterRole("Badger");// NORMAL
        w4r.setPersonAgent(w4);
        w4r.active = true;
        w4.roles.add(w4r);
        w4.startThread();
        
        /* Adding waiters to Host's list */
        ((Restaurant5HostRole) hr).AddWaiter((Restaurant5WaiterRole) w1r);
        ((Restaurant5HostRole) hr).AddWaiter((Restaurant5WaiterRole) w2r);
        ((Restaurant5HostRole) hr).AddWaiter((Restaurant5WaiterRole) w3r);
        ((Restaurant5HostRole) hr).AddWaiter((Restaurant5WaiterRole) w4r);
        /* Connecting Gui's to respective Roles */
        Restaurant5WaiterGui w1g = new Restaurant5WaiterGui(((Restaurant5WaiterRole) w1r), this, 1);
        Restaurant5WaiterGui w2g = new Restaurant5WaiterGui(((Restaurant5WaiterRole) w2r), this, 2);
        Restaurant5WaiterGui w3g = new Restaurant5WaiterGui(((Restaurant5WaiterRole) w3r), this, 3);
        Restaurant5WaiterGui w4g = new Restaurant5WaiterGui(((Restaurant5WaiterRole) w4r), this, 4);
        Restaurant5CookGui cg = new Restaurant5CookGui(((Restaurant5CookRole) ckr), this, 1);
        Restaurant5HostGui hg = new Restaurant5HostGui(((Restaurant5HostRole) hr));
        guis.add(w1g);
        guis.add(w2g);
        guis.add(w3g);
        guis.add(w4g);
        guis.add(cg);
        guis.add(hg);
        ((Restaurant5WaiterRole) w1r).setGui(w1g);
        ((Restaurant5WaiterRole) w2r).setGui(w2g);
        ((Restaurant5WaiterRole) w3r).setGui(w3g);
        ((Restaurant5WaiterRole) w4r).setGui(w4g);
        ((Restaurant5CookRole) ckr).setGui(cg);
        ((Restaurant5HostRole) hr).setGui(hg);
        
        ((Restaurant5WaiterRole) w1r).setCook((Restaurant5CookRole) ckr);
        ((Restaurant5WaiterRole) w2r).setCook((Restaurant5CookRole) ckr);
        ((Restaurant5WaiterRole) w3r).setCook((Restaurant5CookRole) ckr);
        ((Restaurant5WaiterRole) w4r).setCook((Restaurant5CookRole) ckr);
        
        ((Restaurant5WaiterRole) w1r).setHost((Restaurant5HostRole) hr);
        ((Restaurant5WaiterRole) w2r).setHost((Restaurant5HostRole) hr);
        ((Restaurant5WaiterRole) w3r).setHost((Restaurant5HostRole) hr);
        ((Restaurant5WaiterRole) w4r).setHost((Restaurant5HostRole) hr);
        
        ((Restaurant5WaiterRole) w1r).setCashier((Restaurant5CashierRole) csr);
        ((Restaurant5WaiterRole) w2r).setCashier((Restaurant5CashierRole) csr);
        ((Restaurant5WaiterRole) w3r).setCashier((Restaurant5CashierRole) csr);
        ((Restaurant5WaiterRole) w4r).setCashier((Restaurant5CashierRole) csr);
       
        //((Restaurant5CookRole) ckr).setGrocer((MarketGrocerRole) mgrocer);
        /* Adding Gui's to Restaurant5Panel */
        addGui(w1g);
        addGui(w2g);
        addGui(w3g);
        addGui(w4g);
        addGui(cg);
        addGui(hg);
        setHost((Restaurant5HostRole) hr);
        setCashier((Restaurant5CashierRole) csr);
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
        if (image != null)
            g.drawImage(image, 0, 0, WINDOWX -100, WINDOWY -25, null);

        Graphics2D g2 = (Graphics2D) g;
		Graphics2D g3 = (Graphics2D) g;
		Graphics2D g4 = (Graphics2D) g;
		Graphics2D g5 = (Graphics2D) g;
		Graphics2D grill = (Graphics2D) g;
		Graphics2D plating = (Graphics2D) g;
		Graphics2D cashier = (Graphics2D) g;


		// Here is the table
//		g2.setColor(Color.ORANGE);
//		g2.fillRect(tableX, tableY, tableW, tableZ);// 200 and 250 need to be
//													// table params
//
//		g3.setColor(Color.ORANGE);
//		g3.fillRect(tableX + 175, tableY, tableW + 5, tableZ);// 200 and 250 need to
//															// be table params
//
//		g4.setColor(Color.ORANGE);
//		g4.fillRect(tableX2, tableY2, tableW + 5, tableZ);// 200 and 250 need to															// be table params
//
//		g5.setColor(Color.ORANGE);
//		g5.fillRect(tableX2 + 160, tableY2, tableW +5, tableZ);// 200 and 250
//																// need to be
	

		cashier.setColor(Color.red);
		cashier.fillRect(545, 565, 20, 20);
		
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
					gui.draw(g3);
					gui.draw(g4);
					gui.draw(grill);
				}
			}
		}
    }
    
    public void addGui(Gui gui) {
        guis.add(gui);
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
	
	public void setHost(Restaurant5HostRole role) {
		host = role;
	}
	
	public void setCashier(Restaurant5CashierRole role) {
		cashier = role;
	}


}
