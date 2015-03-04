package simCity.gui;

import javax.swing.*;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.interfaces.Person;
import simCity.restaurant4.Restaurant4CashierRole;
import simCity.restaurant4.Restaurant4CookRole;
import simCity.restaurant4.Restaurant4HostRole;
import simCity.restaurant4.Restaurant4MarketRole;
import simCity.restaurant4.Restaurant4NormalWaiterRole;
import simCity.restaurant4.Restaurant4PCWaiterRole;
import simCity.restaurant4.Restaurant4SharedDataWheel;
import simCity.restaurant4.Restaurant4WaiterRole;
import simCity.restaurant4.gui.Restaurant4CookGui;
import simCity.restaurant4.gui.Restaurant4WaiterGui;
import simCity.restaurant4.interfaces.Restaurant4Customer;
import simCity.restaurant4.interfaces.Restaurant4Waiter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class Restaurant4Panel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    public static Grid grid;
    private SimCityGui mainGui;
    
    public Restaurant4HostRole h1;
    public Restaurant4CashierRole ca;
    public Restaurant4CookRole ck;
    Restaurant4SharedDataWheel wheel;
    
    public List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private Vector<Restaurant4Waiter> waiters = new Vector<Restaurant4Waiter>();
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/restaurant4.png");
    private Image image = imageIcon.getImage();


    public Restaurant4Panel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setClosedBlock(195/10, 246/10, 76/10, 76/10);
        grid.setClosedBlock(380/10, 246/10, 76/10, 76/10);
        grid.setClosedBlock(557/10, 246/10, 76/10, 76/10);
        
        Timer timer = new Timer(10, this );
        timer.start();
        
        wheel = new Restaurant4SharedDataWheel();
        
        /*OrdinaryPerson host = new OrdinaryPerson("Karen", 100);
        h1 = new Restaurant4HostRole("Karen");
        h1.setPersonAgent(host);
        h1.active = true;
        host.roles.add(h1);
        host.noPersonState();
        
        OrdinaryPerson cook = new OrdinaryPerson("Lauren", 100);
        Restaurant4CookRole ck = new Restaurant4CookRole("Lauren");
        ck.setSharedDataWheel(wheel);
        ck.setPersonAgent(cook);
        ck.active = true;
        cook.roles.add(ck);
        cook.noPersonState();
        h1.setCook(ck);//////////////////////////////////////////////////////////////////
        
        OrdinaryPerson cashier = new OrdinaryPerson("Gemma", 100);
        ca = new Restaurant4CashierRole("Gemma");
        ca.setPersonAgent(cashier);
        ca.active = true;
        cashier.roles.add(ca);
        cashier.noPersonState();
        cashier.startThread();
        
        OrdinaryPerson waiter1 = new OrdinaryPerson("Logan", 100);
        Restaurant4WaiterRole w1 = new Restaurant4NormalWaiterRole("Logan");
        w1.setPersonAgent(waiter1);
        w1.active = true;
        w1.setHost(h1);////////////////////////////////////////////////////////////////////////
        w1.setCashier(ca);
        w1.setCook(ck);
        waiter1.roles.add(w1);
        waiter1.noPersonState();
        waiter1.startThread();
        waiters.add(w1);
        
        Restaurant4CookGui ckg = new Restaurant4CookGui(ck, mainGui, 661, 494);
        ck.setGui(ckg);
        addGui(ckg);
        
        Restaurant4WaiterGui wg1 = new Restaurant4WaiterGui(w1, mainGui, waiters.size());
        w1.setGui(wg1);
        wg1.setHost(h1);
        wg1.setCookGui(ckg);
        wg1.addWaiter(w1);
        addGui(wg1);
        
        OrdinaryPerson waiter2 = new OrdinaryPerson("Lynn", 100);
        Restaurant4PCWaiterRole w2 = new Restaurant4PCWaiterRole("Lynn");
        w2.setSharedDataWheel(wheel);
        w2.setPersonAgent(waiter2);
        w2.active = true;
        w2.setHost(h1);
        w2.setCashier(ca);
        w2.setCook(ck);
        waiter2.roles.add(w2);
        waiter2.noPersonState();
        waiter2.startThread();
        waiters.add(w2);
        
        Restaurant4WaiterGui wg2 = new Restaurant4WaiterGui(w2, mainGui, waiters.size());
        w2.setGui(wg2);
        wg2.setHost(h1);
        wg2.setCookGui(ckg);
        wg2.addWaiter(w2);
        addGui(wg2);
        
        for(int i=0; i<3; i++) {
        	OrdinaryPerson market = new OrdinaryPerson("Market"+(i+1), 100);
        	Restaurant4MarketRole mk = new Restaurant4MarketRole("Market"+(i+1));
            mk.setPersonAgent(market);
            mk.active = true;
            market.roles.add(mk);
            market.noPersonState();
        	mk.setCook(ck);
        	mk.setCashier(ca);
        	ck.addMarket(mk);
        	market.startThread();
        }
        
        host.startThread();
        cook.startThread();*/
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
        if (image != null)
            g.drawImage(image, 0, 0, WINDOWX-100, WINDOWY-30, null);

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
		System.out.println(m.getX()+" "+m.getY());
		synchronized(guis) {
			for (Gui gui : guis){
				if(m.getX() >= gui.getX() && m.getX() < gui.getX() + 20
						&& m.getY() >= gui.getY() && m.getY() < gui.getY() + 20){
					mainGui.updateInfoPanel(gui);
				}
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

	public void setCustomerEnabled(Restaurant4Customer agent) {
		
	}

	public void setWaiterEnabled(Restaurant4Waiter findRole) {
		
	}
}
