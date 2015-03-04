package simCity.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class HousePanel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    public static Grid grid;
    private SimCityGui mainGui;
    private Gui currentGui = null;
    
    public List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/USCHousingDesign.png");
        private Image image = imageIcon.getImage();

        
    public HousePanel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(0, 0, WINDOWX/10, WINDOWY/10);
        grid.setClosedBlock(450/10, 70/10, 50/10, 290/10);
        grid.setClosedBlock(450/10, 420/10, 50/10, 180/10);
        grid.setClosedBlock(280/10, 510/10, 210/10, 100/10);
        grid.setClosedBlock(180/10, 70/10, 50/10, 330/10);
        grid.setClosedBlock(150/10, 70/10, 40/10, 550/10);
        grid.setClosedBlock(150/10, 70/10, 600/10, 50/10);
        grid.setClosedBlock(710/10, 70/10, 40/10, 550/10);
        grid.setClosedBlock(180/10, 120/10, 250/10, 50/10);
        grid.setClosedBlock(490/10, 120/10, 40/10, 150/10);
        grid.setClosedBlock(430/10, 440/10, 60/10, 100/10);
        grid.setClosedBlock(490/10, 370/10, 50/10, 180/10);
        
        
        Timer timer = new Timer(10, this );
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
    	//super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        if (image != null)
            g.drawImage(image, 0, 0, WINDOWX-100, WINDOWY-30, null);
        
        
        //g2.setColor(Color.GRAY);
        //g2.fillRect(430, 490, 60, 60);
        //g2.fillRect(490, 420, 50, 140);
        
        synchronized(guis) {
	        for(Gui gui : guis) {
	            gui.updatePosition();
	        }
        }
        
        if (currentGui != null){
        	currentGui.draw(g2);
        }
    }
    
    public void setGui(Gui hrg) {
    	boolean found = false;
    	System.out.println("in set gui");
    	synchronized(guis) {
    		for (Gui gui : guis) {
    			if (gui == hrg) {
    				System.out.println("found");
    				currentGui = hrg;
    				found = true;
    			}
    		}
    	}
    	if (!found){
    		currentGui = null;
    		System.out.println("not found");
    	}
    }
    
    public void setGuiEmpty(){
    	currentGui = null;
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
