package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import javax.swing.ImageIcon;

import simCity.interfaces.BankGuard;
import simCity.interfaces.BankRobber;

public class BankRobberGui implements Gui {
	private BankPanel panel;
	private boolean isPresent = true;
    private int xPos, yPos;
    private int xDestination, yDestination;
    private int homeX, homeY, exitX, exitY, tellerX, tellerY;
    //private int speed;
    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;
    private BankRobber robber;
    private BankGuardGui guardGui;
    public SimCityGui gui;
    private enum Command {nothing, runToTeller, leaveBankWithGuard, runAway};
    Command command = Command.nothing;
    private ImageIcon smokeIcon = new ImageIcon("src/simCity/gui/images/smoke.gif");
    private Image smokeImg = smokeIcon.getImage();
    private boolean robberySuccess = false;

    String displayName = "";
	
    public BankRobberGui(BankRobber robber, SimCityGui gui, int x, int y) {
    	
    	this.robber = robber;
    	this.gui = gui;
    	
    	xPos = x;
        yPos = y;
        xDestination = x;
        yDestination = y;
        homeX = x;
        homeY = y;
        exitX = 20;
        exitY = 200;
        tellerX = 420;
        tellerY = 140;
        //speed = 5;
        newAStar = null;
    }
    
    public void setGuardGui(BankGuardGui guard) {
    	guardGui = guard;
    }
    
    public void setBankPanel(BankPanel panel) {
    	this.panel = panel;
    }
    
    public BankRobber getBankRobber() {
    	return robber;
    }

    public void updatePosition() {
    	if ( ( xPos/10 == xDestination/10 ) && ( yPos/10 == yDestination/10 ) ) {
    		if(command == Command.runToTeller) {
    			guardGui.reachedTeller(robber); //beat the guard to the bank teller position 
    		}
    		else if(command == Command.leaveBankWithGuard) {
    			robber.msgAnimationComplete();
    		}
    		else if(command == Command.runAway) {
    			robber.msgAnimationComplete();
    		}
    		command = Command.nothing;
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
        g2.setColor(new Color(687172));
        g2.fillRect(xPos, yPos, 20, 20);
        
        if(command == Command.runAway || command == Command.runToTeller) {
        	g2.drawImage(smokeImg, xPos, yPos+20, smokeIcon.getImageObserver());
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
    
    public void stopRobbing() {
    	command = Command.nothing;
    }
    
    public void robBank() { //from BankPanel
    	setDestination(tellerX, tellerY);
    	command = Command.runToTeller;
    }
    
    public void DoLeaveBank() {
    	//speed = 1;
    	setDestination(exitX, exitY);
    	command = Command.leaveBankWithGuard;
    }
    
    public void DoRunAway() {
    	setDestination(exitX, exitY);
    	command = Command.runAway;
    }

	public void removeGui(BankRobberGui gui2) {
		panel.removeGui((Gui) gui2);
	}
}
