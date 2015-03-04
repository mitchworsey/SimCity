package simCity.gui;

import javax.swing.*;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.Restaurant1.Restaurant1CashierRole;
import simCity.Restaurant1.Restaurant1CookRole;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.Restaurant1.Restaurant1WaiterRole;
import simCity.gui.Restaurant1.Restaurant1CookGui;
import simCity.gui.Restaurant1.Restaurant1WaiterGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Restaurant1Panel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    public static Grid grid;
    public static SemaphoreGrid semaphoreGrid;
    private SimCityGui mainGui;
    private int waiterCount;
    
    static Restaurant1HostRole host;
    
    public List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/restaurant1.png");
        private Image image = imageIcon.getImage();

    public Restaurant1Panel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        waiterCount = 4;
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(90/10, 40/10, 730/10, 450/10);
        grid.setClosedBlock(290/10, 40/10, 80/10, 100/10);
        grid.setClosedBlock(650/10, 40/10, 40/10, 250/10);

        semaphoreGrid = new SemaphoreGrid(WINDOWX, WINDOWY);
        
        Timer timer = new Timer(15, this );
        timer.start();
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
        
        synchronized(guis){
        	for(Gui gui : guis) {
        		gui.updatePosition();
        	}
        }
        
        synchronized(guis){
        	for(Gui gui : guis) {
        		gui.draw(g2);
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
	}
	
	public int getWaiterCount(){
		waiterCount -=1;
		return waiterCount;
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

	public static void setHost(Restaurant1HostRole role) {
		host = role;
	}
}
