package simCity.gui;

import javax.swing.*;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.Restaurant3.Restaurant3CashierRole;
import simCity.Restaurant3.Restaurant3CookRole;
import simCity.Restaurant3.Restaurant3HostRole;
import simCity.Restaurant3.Restaurant3MarketRole;
import simCity.Restaurant3.Restaurant3NormalWaiter;
import simCity.Restaurant3.Restaurant3WaiterRole;
import simCity.Restaurant3.gui.Restaurant3CookGui;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;
import simCity.house.HouseMaintenanceManagerRole;
import simCity.house.HouseOwnerRole;
import simCity.interfaces.HouseMaintenanceManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class HousingOfficePanel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    public static Grid grid;
    private SimCityGui mainGui;
    
    public static HouseOwnerRole owner;
    public static HouseMaintenanceManagerRole maintenanceManager;
    
    public List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/housingOffice.png");
        private Image image = imageIcon.getImage();

        
    public HousingOfficePanel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(0, 0, WINDOWX/10, WINDOWY/10);
        
        Timer timer = new Timer(10, this );
        timer.start();
        
        /*OrdinaryPerson o = new OrdinaryPerson("Owner", 100);
        o.noPersonState();
        Role or = new HouseOwnerRole("Owner");
        or.setPersonAgent(o);
        or.active = true;
        o.roles.add(or);
    	
    	OrdinaryPerson mm = new OrdinaryPerson("Maintenance Manager", 100);
    	mm.noPersonState();
    	Role mmr = new HouseMaintenanceManagerRole("Maintenance Manager");
    	mmr.setPersonAgent(mm);
    	mmr.active = true;
    	mm.roles.add(mmr);
    	
    	HouseOwnerGui hog = new HouseOwnerGui(((HouseOwnerRole) or), this);
    	HouseMaintenanceManagerGui hmmg = new HouseMaintenanceManagerGui(((HouseMaintenanceManagerRole) mmr), this);
    	
    	((HouseOwnerRole) or).setGui(hog);
    	((HouseMaintenanceManagerRole) mmr).setGui(hmmg);
    	
    	((HouseOwnerRole) or).setMaintenanceManager((HouseMaintenanceManager) mmr);
    	((HouseOwnerRole) or).createHouses();
    	
    	addGui(hog);
    	addGui(hmmg);
    	
    	o.startThread();
    	mm.startThread();
    	
    	owner = (HouseOwnerRole) or;
    	maintenanceManager = (HouseMaintenanceManagerRole) mmr;*/
        
    }
    
    public void setOwner(HouseOwnerRole r){
    	owner = r;
    }
    
    public void setMaitenanceManager(HouseMaintenanceManagerRole r){
    	maintenanceManager = r;
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
