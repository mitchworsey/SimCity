package simCity.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

public class BusStopPanel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 1000;
    private final int WINDOWY = 700;
    static Grid grid;
    private SimCityGui mainGui;
    
    public final int xLeft = 170;
    public final int yTop = 205;
    public final int xSpacing = 60;
    public final int ySpacing = 110;
    
    public boolean openSeats[][] = new boolean[8][3];
    
    List<Gui> guis = new ArrayList<Gui>();
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/busStop.png");
        private Image image = imageIcon.getImage();


    public BusStopPanel() {
    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        //initialize boolean array to all open seats
        for (int i=0; i<8; i++) {
        	for (int j=0; j<3; j++) {
        		openSeats[i][j] = true;
        	}
        }
        
        grid = new Grid(WINDOWX, WINDOWY);
        grid.setOpenBlock(0, 0, WINDOWX/10, WINDOWY/10);
        
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
        Graphics2D g2 = (Graphics2D)g;
        if (image != null)
            g.drawImage(image, 0, 0, WINDOWX-140, WINDOWY-30, null);
        
        
        for(Gui gui : guis) {
            gui.updatePosition();
        }
        
        for(Gui gui : guis) {
            gui.draw(g2);
        }
    }
    
    public void addGui(Gui gui) {
    	openSeats[((BusStopPersonGui) gui).xIndex][((BusStopPersonGui) gui).yIndex] = false;
        guis.add(gui);
    }
    
    public void removeGui(Gui gui) {
    	openSeats[((BusStopPersonGui) gui).xIndex][((BusStopPersonGui) gui).yIndex] = true;
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
    
    public int nextOpenSeatIndex() {
    	for (int j = 0; j < 3; j++) {
    		for (int i = 0; i < 8; i++) {
    			if (openSeats[i][j]) {
    				return (j*8 + i);
    			}
    		}
    	}
    	return -1;
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