package simCity.gui;

import javax.management.relation.Role;
import javax.swing.*;

import simCity.OrdinaryPerson;
import simCity.bank.BankCustomerRole;
import simCity.bank.BankGuardRole;
import simCity.bank.BankRobberRole;
import simCity.bank.BankTellerRole;
import simCity.interfaces.BankCustomer;
import simCity.interfaces.BankCustomerGuiInterface;
import simCity.interfaces.BankGuard;
import simCity.interfaces.BankRobber;
import simCity.interfaces.BankTeller;
import simCity.interfaces.Person;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class BankPanel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    static Grid grid;
    private SimCityGui mainGui;
    public BankTeller teller;
    private BankTellerGui btg;
    public BankGuard guard;
    private BankGuardGui bgg;

    List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/bankgui2.png");
        private Image image = imageIcon.getImage();


    public BankPanel() { 	

    	/*OrdinaryPerson p = new OrdinaryPerson("Betsy", 500); //hardcoded instance of bank teller
    	teller = new BankTellerRole(p.getName(), 1000);
    	teller.setPersonAgent(p);
		teller.active = true;
		p.roles.add(teller);
    	p.noPersonState();
    	p.startThread();
    	btg = new BankTellerGui(teller, 450, 80);
    	teller.setGui(btg);
    	addGui(btg);
    	
    	OrdinaryPerson p2 = new OrdinaryPerson("Calvin", 200);
    	guard = new BankGuardRole("Calvin");
    	guard.setPersonAgent(p2);
    	guard.active = true;
    	p2.roles.add(guard);
    	p2.noPersonState();
    	p2.startThread();
    	BankGuardGui bgg = new BankGuardGui(guard, mainGui, 80, 250);
    	guard.setGui(bgg);
    	addGui(bgg);*/
    	
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(0, 0, 1000/10, 700/10);
        grid.setClosedBlock(375/10, 250/10, 40/10, 320/10);
        grid.setClosedBlock(475/10, 250/10, 40/10, 320/10);
        
        Timer timer = new Timer(10, this );
        timer.start();
    }
    
    public void setTeller(BankTeller Teller){
    	teller = Teller;
    }
    
    public void setGuard(BankGuard guard) {
    	this.guard = guard;
    }
    
    public void setTellerGui(BankTellerGui tellerGui){
    	btg = tellerGui;
    }
    
    public void setGuardGui(BankGuardGui guardGui) {
    	bgg = guardGui;
    }
    
    public BankGuardGui getGuardGui() {
    	return bgg;
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
            g.drawImage(image, 0, 0, WINDOWX-100, WINDOWY-30, null);

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
        
        if(gui instanceof BankRobberGui) {
        	guard.msgChaseRobber(((BankRobberGui) gui).getBankRobber());
        	//((BankGuardRole) guard).getGui().chaseRobber(((BankRobberGui) gui).getBankRobber());
        }
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
		synchronized (guis) {
			for (Gui gui : guis){
				if(gui instanceof BankCustomerGui && (m.getX() >= gui.getX() && m.getX() < gui.getX() + 20
						&& m.getY() >= gui.getY() && m.getY() < gui.getY() + 20)){
					mainGui.updateInfoPanel(gui);
					return;
				}
			}
		}
		mainGui.hideInfoPanel();
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
