package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import simCity.interfaces.BankTeller;
import simCity.interfaces.Person;

public class BankTellerGui implements Gui {
	
	ImageIcon icon;
	
	private BankPanel panel;
	private boolean isPresent = true;
    private int xPos, yPos;
    private int xDestination, yDestination;
    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;
    private ImageIcon moneyIcon = new ImageIcon("src/simCity/gui/images/money.gif");
    private Image moneyImg = moneyIcon.getImage();
    private ImageIcon pencilIcon = new ImageIcon("src/simCity/gui/images/pencil.gif");
    private Image pencilImg = pencilIcon.getImage();
    private enum Command {nothing, openingAcct, handlingMoney};
    private Command command = Command.nothing;
    
    public BankTeller teller = null;
    public SimCityGui gui;

    String displayName = "";
    
    Timer timer = new Timer();
    Timer makeAccTimer = new Timer();
	
    public BankTellerGui(BankTeller teller, SimCityGui gui, int xStart, int yStart) {
    	this.teller = teller;
    	this.gui = gui;
    	
    	xPos = 440;
        yPos = 560;
        xDestination = xStart;
        yDestination = yStart;
        setDestination(xStart, yStart);
        newAStar = null;
    }

    public BankTellerGui(BankTeller teller, int xStart, int yStart) {
    	this.teller = teller;
    	
    	xPos = 440;
        yPos = 560;
        xDestination = xStart;
        yDestination = yStart;
        setDestination(xStart, yStart);
        newAStar = null;
    }
    
    public void setBankPanel(BankPanel panel) {
    	this.panel = panel;
    }
    
    public void updatePosition() {
    	if (xPos == xDestination && yPos == yDestination) {
    		
        }
    	if (finalList != null){
    		if (!finalList.isEmpty()){
    			if (finalList.size()>=1){
    				//simCity.gui.CityPanel.grid.release(xPos/10, yPos/10);
    				xPos = finalList.get(finalList.size()-1).getX();
    				yPos = finalList.get(finalList.size()-1).getY();
    				//simCity.gui.CityPanel.grid.set(xPos/10, yPos/10);
    				finalList.remove(finalList.get(finalList.size()-1));
    			}
    		}
    	}
    }
    
    public void draw(Graphics2D g) {
    	Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(11543188));
        g2.fillRect(xPos, yPos, 20, 20);
        
        if(command == Command.openingAcct) {
        	g2.drawImage(pencilImg, 435, 80, pencilIcon.getImageObserver());
        }
        
        else if(command == Command.handlingMoney) {
        	g2.drawImage(moneyImg, 430, 90, moneyIcon.getImageObserver());
        }
    }
    
    public boolean isPresent() {
        return isPresent;  
	}

    public void setPresent(boolean p) {
        isPresent = p;
    }

    public int getX() {
    	return xPos;
    }

    public int getY() {
    	return yPos;
    }

    public void setDestination(int x, int y){
		xDestination = x;
		yDestination = y;
	    newAStar = new aStar(xPos, yPos, xDestination, yDestination, panel.grid);
	    finalList = newAStar.getBestPathSteps();
	}

	public void DoMakeAccount() {
		command = Command.openingAcct;
		makeAccTimer.schedule(new TimerTask() {
			public void run() {
				command = Command.nothing;
				teller.msgDoneWithTask();
			}
		}, 
		4000);
	}
	
	public void DoHandleMoney() {
		command = Command.handlingMoney;
		timer.schedule(new TimerTask() {
			public void run() {
				command = Command.nothing;
				teller.msgDoneWithTask();
			}
		}, 
		4000);
	}

	public void setIcon(ImageIcon icon2) {
		icon = icon2;
	}
}      