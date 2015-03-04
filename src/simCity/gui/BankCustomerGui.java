package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import simCity.bank.BankCustomerRole;
import simCity.interfaces.BankCustomer;
import simCity.interfaces.BankCustomerGuiInterface;
import simCity.interfaces.Person;

public class BankCustomerGui implements Gui, BankCustomerGuiInterface {  
	
	ImageIcon icon;
	
	private boolean isPresent = true;
	private BankPanel panel;
    private int xPos, yPos;
    private int xDestination, yDestination;
    private int exitX, exitY;
    private int tellerX, tellerY;
    private final int xStart = 440;
    private final int yStart = 560;
    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;
    public SimCityGui gui;
    private enum Command {nothing, enterBank, goToTeller, exit};
    Command command = Command.nothing;
    private Timer timer = new Timer();
    
    public BankCustomer customer = null;
    String displayName = "";
	
    public BankCustomerGui(BankCustomer customer, SimCityGui gui) {
    	
    	this.customer = customer;
    	this.gui = gui;
    	
    	xPos = xStart;
        yPos = yStart;
        xDestination = xStart;
        yDestination = yStart;
        exitX = 20;
        exitY = 200;
        tellerX = 450;
        tellerY = 140;
        newAStar = null;
    }

    public void updatePosition() {
    	if ( ( xPos/10 == xDestination/10 ) && ( yPos/10 == yDestination/10 ) ) {
    		if(command == Command.enterBank) {
    			timer.schedule(new TimerTask() { //if bank customer does not manually make a decision fast enough, 
    				public void run() {				//automatic transactions are done
    					if(((BankCustomerRole) customer).transactions.isEmpty()) {
    						DoGoToTeller();
    						
    						if(((BankCustomerRole) customer).accountNum == 0) {
    							customer.msgCreateAutoTransactions();
    						}
    						else {
    							customer.msgWithdrawFromBank(15);
    						}
    					}
    				}
    			}, 
    			3000);
    		}
    		if(command == Command.goToTeller) {
    			AtLocation();
    			command = Command.nothing;
    		}
    		else if(command == Command.exit) {
    			AtLocation();
    			gui.controlPanel.leavingBank();
    			command = Command.nothing;
    		}
        }
    	if(finalList != null){
    		if (!finalList.isEmpty()){
    			if (finalList.size()>=1){
    				xPos = finalList.get(finalList.size()-1).getX();
    				yPos = finalList.get(finalList.size()-1).getY();
    				finalList.remove(finalList.get(finalList.size()-1));
    			}
    		}
    	}
    	else {
    		if (xPos < xDestination)
                xPos++;
            else if (xPos > xDestination)
                xPos--;

            if (yPos < yDestination)
                yPos++;
            else if (yPos > yDestination)
                yPos--;
    	}
    }
    
    public void draw(Graphics2D g) {
    	Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLUE);
        g2.fillRect(xPos, yPos, 20, 20);
    }
    
    public boolean isPresent() {
        return isPresent;
	}

    public void setPresent(boolean p) {
        isPresent = p;
    }
    
    public void setBankPanel(BankPanel panel) {
    	this.panel = panel;
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

	public void DoExitBank() {
		setDestination(exitX, exitY);
		command = Command.exit;
	}

	public void DoGoToTeller() {
		setDestination(tellerX, tellerY);
		command = Command.goToTeller;
	}
	
	public void DoEnterBank() {
		setDestination(xStart, yStart);
		command = Command.enterBank;
	}
	
	public void AtLocation() {
		customer.msgAtLocation();
	}

	public void setIcon(ImageIcon icon2) {
		icon = icon2;
	}

	public void removeGui(BankCustomerGuiInterface g) {
		panel.removeGui((Gui) g);
	}
}      