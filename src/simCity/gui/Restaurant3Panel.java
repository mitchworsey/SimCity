package simCity.gui;

import javax.swing.*;

import agent.Agent;
import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.Restaurant3.Restaurant3CashierRole;
import simCity.Restaurant3.Restaurant3CookRole;
import simCity.Restaurant3.Restaurant3HostRole;
import simCity.Restaurant3.Restaurant3MarketRole;
import simCity.Restaurant3.Restaurant3NormalWaiter;
import simCity.Restaurant3.Restaurant3WaiterRole;
import simCity.Restaurant3.gui.Restaurant3CookGui;
import simCity.Restaurant3.gui.Restaurant3CustomerGui;
import simCity.Restaurant3.gui.Restaurant3FoodGui;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;
import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.Restaurant3.interfaces.Restaurant3Host;
import simCity.Restaurant3.interfaces.Restaurant3Market;
import simCity.interfaces.PersonGuiInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Restaurant3Panel extends JPanel implements ActionListener, MouseListener {  
    private int waiter1GuiXPos = 80;
    private int waiter2GuiXPos = 120;
    private int waiter3GuiXPos = 160;
    private int waiter4GuiXPos = 200;
    
    
    private int customerGuiYPos = 10;
    private int customerGuiXPos = 10;
	
    
    Restaurant3HostRole host;
    Restaurant3CashierRole cashier;

    
    private final int TABLE1X = 180;
    private final int TABLE1Y = 220;
    private final int TABLE2X = 180;
    private final int TABLE2Y = 100;
    private final int TABLE3X = 300;
    private final int TABLE3Y = 220;
    private final int TABLE4X = 300;
    private final int TABLE4Y = 100;
    private final int WINDOWX = 650;
    private final int WINDOWY = 400;
    public static Grid grid;
    private SimCityGui mainGui;
    
    private ImageIcon imageIconFloor = new ImageIcon("src/simCity/gui/images/floor3.png");
    private Image imageFloor = imageIconFloor.getImage();
    private ImageIcon imageIconCashRegister = new ImageIcon("src/simCity/gui/images/cashRegister3.png");
    private Image imageCashRegister = imageIconCashRegister.getImage();
    private ImageIcon imageIconFridge = new ImageIcon("src/simCity/gui/images/fridge3.png");
    private Image imageFridge = imageIconFridge.getImage();
    private ImageIcon imageIconTable = new ImageIcon("src/simCity/gui/images/table3.png");
    private Image imageTable = imageIconTable.getImage();
    private ImageIcon imageIconLogo = new ImageIcon("src/simCity/gui/images/McKaysLogo.png");
    private Image imageLogo = imageIconLogo.getImage();
    
    List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
   // private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/restaurant3.png");
     //   private Image image = imageIcon.getImage();


    public Restaurant3Panel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(0, 0, WINDOWX/10, WINDOWY/10);
        grid.setClosedBlock(TABLE1X/10, (TABLE1Y-40)/10, 50/10, (50+40)/10);
        grid.setClosedBlock(TABLE2X/10, (TABLE2Y-40)/10, 50/10, (50+40)/10);
        grid.setClosedBlock(TABLE3X/10, (TABLE3Y-40)/10, 50/10, (50+40)/10);
        grid.setClosedBlock(TABLE4X/10, (TABLE4Y-40)/10, 50/10, (50+40)/10);
        grid.setClosedBlock(150/10, (320-50)/10, 250/10, (40+50)/10);
        //grid.setClosedBlock(424/10, (75-50)/10, 25/10, (150+50)/10);
        grid.setClosedBlock(510/10, 0, 40/10, (70)/10);
        grid.setClosedBlock(80/10, (270-50)/10, 30/10, (30+50)/10);
        
        
        
        Timer timer = new Timer(10, this );
        timer.start();
        
       /* OrdinaryPerson h = new OrdinaryPerson("Host", 100);
        h.noPersonState();
        Role hr = new Restaurant3HostRole("Host");
        hr.setPersonAgent(h);
        hr.active = true;
        h.roles.add(hr);
    	
    	OrdinaryPerson ck = new OrdinaryPerson("Cook", 100);
    	ck.noPersonState();
    	Role ckr = new Restaurant3CookRole("Cook");
    	ckr.setPersonAgent(ck);
    	ckr.active = true;
    	ck.roles.add(ckr);
    	
    	OrdinaryPerson cs = new OrdinaryPerson("Cashier", 100);
    	cs.noPersonState();
    	Role csr = new Restaurant3CashierRole("Cashier");
    	csr.setPersonAgent(cs);
    	csr.active = true;
    	cs.roles.add(csr);
    	
    	OrdinaryPerson w1 = new OrdinaryPerson("Waiter1", 100);
    	w1.noPersonState();
    	Role w1r = new Restaurant3NormalWaiter("Waiter1");
    	w1r.setPersonAgent(w1);
    	w1r.active = true;
    	w1.roles.add(w1r);
    	
    	OrdinaryPerson w2 = new OrdinaryPerson("Waiter2", 100);
    	w2.noPersonState();
    	Role w2r = new Restaurant3NormalWaiter("Waiter2");
    	w2r.setPersonAgent(w2);
    	w2r.active = true;
    	w2.roles.add(w2r);
   
    	OrdinaryPerson w3 = new OrdinaryPerson("Waiter3", 100);
    	w3.noPersonState();
    	Role w3r = new Restaurant3NormalWaiter("Waiter3");
    	w3r.setPersonAgent(w3);
    	w3r.active = true;
    	w3.roles.add(w3r);
    	
    	OrdinaryPerson w4 = new OrdinaryPerson("Waiter4", 100);
    	w4.noPersonState();
    	Role w4r = new Restaurant3NormalWaiter("Waiter4");
    	w4r.setPersonAgent(w4);
    	w4r.active = true;
    	w4.roles.add(w4r);
    	
    	OrdinaryPerson m1 = new OrdinaryPerson("Market 1", 100);
    	m1.noPersonState();
    	Role m1r = new Restaurant3MarketRole("Market 1");
    	m1r.setPersonAgent(m1);
    	m1r.active = true;
    	m1.roles.add(m1r);
    	
    	OrdinaryPerson m2 = new OrdinaryPerson("Market 2", 100);
    	m2.noPersonState();
    	Role m2r = new Restaurant3MarketRole("Market 2");
    	m2r.setPersonAgent(m2);
    	m2r.active = true;
    	m2.roles.add(m2r);
    	
    	OrdinaryPerson m3 = new OrdinaryPerson("Market 3", 100);
    	m3.noPersonState();
    	Role m3r = new Restaurant3MarketRole("Market 3");
    	m3r.setPersonAgent(m3);
    	m3r.active = true;
    	m3.roles.add(m3r);
    	
    	
    	((Restaurant3CookRole) ckr).setCashier((Restaurant3CashierRole) csr);

    	
    	Restaurant3WaiterGui w1g = new Restaurant3WaiterGui(((Restaurant3WaiterRole) w1r), this);
    	Restaurant3WaiterGui w2g = new Restaurant3WaiterGui(((Restaurant3WaiterRole) w2r), this);
    	Restaurant3WaiterGui w3g = new Restaurant3WaiterGui(((Restaurant3WaiterRole) w3r), this);
    	Restaurant3WaiterGui w4g = new Restaurant3WaiterGui(((Restaurant3WaiterRole) w4r), this);
    	Restaurant3CookGui cg = new Restaurant3CookGui(((Restaurant3CookRole) ckr), this);
    	
    	((Restaurant3WaiterRole) w1r).setGui(w1g);
    	((Restaurant3WaiterRole) w2r).setGui(w2g);
    	((Restaurant3WaiterRole) w3r).setGui(w3g);
    	((Restaurant3WaiterRole) w4r).setGui(w4g);
    	((Restaurant3CookRole) ckr).setGui(cg);
    	
    	((Restaurant3WaiterRole) w1r).setCook((Restaurant3CookRole) ckr);
    	((Restaurant3WaiterRole) w1r).setHost((Restaurant3HostRole) hr);
    	((Restaurant3WaiterRole) w1r).setCashier((Restaurant3CashierRole) csr);
    	
    	((Restaurant3WaiterRole) w2r).setCook((Restaurant3CookRole) ckr);
    	((Restaurant3WaiterRole) w2r).setHost((Restaurant3HostRole) hr);
    	((Restaurant3WaiterRole) w2r).setCashier((Restaurant3CashierRole) csr);
    	
    	((Restaurant3WaiterRole) w3r).setCook((Restaurant3CookRole) ckr);
    	((Restaurant3WaiterRole) w3r).setHost((Restaurant3HostRole) hr);
    	((Restaurant3WaiterRole) w3r).setCashier((Restaurant3CashierRole) csr);
    	
    	((Restaurant3WaiterRole) w4r).setCook((Restaurant3CookRole) ckr);
    	((Restaurant3WaiterRole) w4r).setHost((Restaurant3HostRole) hr);
    	((Restaurant3WaiterRole) w4r).setCashier((Restaurant3CashierRole) csr);
    			
		((Restaurant3CookRole) ckr).setCashier((Restaurant3CashierRole) csr);
		
		((Restaurant3CookRole) ckr).setWaiterName("Waiter1");
    	
		
		w1g.setXWatchPos(waiter1GuiXPos);
		w2g.setXWatchPos(waiter2GuiXPos);
		w3g.setXWatchPos(waiter3GuiXPos);
		w4g.setXWatchPos(waiter4GuiXPos);
		
		
		((Restaurant3HostRole) hr).addWaiters((Restaurant3WaiterRole) w1r);
    	((Restaurant3HostRole) hr).addWaiters((Restaurant3WaiterRole) w2r);
    	((Restaurant3HostRole) hr).addWaiters((Restaurant3WaiterRole) w3r);
    	((Restaurant3HostRole) hr).addWaiters((Restaurant3WaiterRole) w4r);

		/*if(name.equals("one low market"))
			market1.setLowInventory();
		if(name.equals("poor cashier"))
			cashier.setLowCash();*/		
        
		
        /*m1.startThread();
        m2.startThread();
        m3.startThread();

        
        ((Restaurant3CookRole) ckr).addMarket((Restaurant3MarketRole) m1r);
        ((Restaurant3CookRole) ckr).addMarket((Restaurant3MarketRole) m2r);
        ((Restaurant3CookRole) ckr).addMarket((Restaurant3MarketRole) m3r);
		
    	addGui(w1g);
    	addGui(w2g);
    	addGui(w3g);
    	addGui(w4g);
    	addGui(cg);
    	
    	h.startThread();
    	ck.startThread();
    	cs.startThread();
    	w1.startThread();
    	w2.startThread();
    	w3.startThread();
    	w4.startThread();
    	setHost((Restaurant3HostRole) hr);
    	setCashier((Restaurant3CashierRole) csr);*/
    }

	public void setHost(Restaurant3HostRole role) {
		host = role;
	}
	public void setCashier(Restaurant3CashierRole role) {
		cashier = role;
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
        if (imageFloor != null)
            g.drawImage(imageFloor, -20, -20, 575, 450, null);
        
        if (imageLogo != null)
            g.drawImage(imageLogo, 150, 320, 250, 40, null);
        
        
        //Clear the screen by painting a rectangle the size of the frame
        //g2.setColor(getBackground());
        //g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.GRAY);
        g2.fillRect(424, 75, 25, 150);
        
        if (imageFridge != null)
            g.drawImage(imageFridge, 510, 0, 40, 70, null);
        //g2.setColor(Color.BLUE);
        //g2.fillRect(540, 135, 10, 30);
        
        if (imageCashRegister != null)
            g.drawImage(imageCashRegister, 80, 270, 30, 30, null);
        //g2.setColor(Color.GREEN);
        //g2.fillRect(70, 250, 40, 20);
        
        
        //Here is the table
        if (imageTable != null)
            g.drawImage(imageTable,TABLE1X, TABLE1Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE1X, TABLE1Y, 50, 50);//200 and 250 need to be table params
        
        if (imageTable != null)
            g.drawImage(imageTable,TABLE2X, TABLE2Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE2X, TABLE2Y, 50, 50);//200 and 250 need to be table params
        
        if (imageTable != null)
            g.drawImage(imageTable,TABLE3X, TABLE3Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE3X, TABLE3Y, 50, 50);//200 and 250 need to be table params
        
        if (imageTable != null)
            g.drawImage(imageTable,TABLE4X, TABLE4Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE4X, TABLE4Y, 50, 50);//200 and 250 need to be table params

        synchronized(guis) {
	        for(Gui gui : guis) {
	            gui.updatePosition();
	        }
        }
        
        synchronized(guis) {
	        for(Gui gui : guis) {
	            gui.draw(g2);
	        }
        }
    }
    
    public void addGui(Gui gui) {
        guis.add(gui);
    }
    
    public void addGui(Restaurant3CustomerGui gui) {
        guis.add(gui);
    }
    public void addGui(Restaurant3WaiterGui gui){
    	guis.add(gui);
    }
    public void addGui(Restaurant3FoodGui gui){
    	guis.add(gui);
    }
    public void addGui(Restaurant3CookGui gui){
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
	
}
