package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import simCity.bank.BankCustomerRole;
import simCity.bank.BankGuardRole;
import simCity.bank.BankRobberRole;
import simCity.interfaces.BankGuard;
import simCity.interfaces.BankRobber;
import simCity.interfaces.Person;
import simCity.restaurant4.Restaurant4WaiterRole.Menu;

public class BankGuardGui implements Gui/*, BankGuardGuiInterface*/ {  

	private BankPanel panel;
	private boolean isPresent = true;
    private int xPos, yPos;
    private int xDestination, yDestination;
    private int homeX, homeY, exitX, exitY, tellerX, tellerY;
    //private int speed;
    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;
    public SimCityGui gui;
    private enum Command {nothing, chaseRobber, disposeOfRobber, returnToPos};
    Command command = Command.nothing;
    private Timer timer = new Timer();
    private BankGuard guard;
    private BankRobber robber = null;
    //private Random randSpeed;
    Random random = new Random();

    String displayName = "";
	
    public BankGuardGui(BankGuard guard, SimCityGui gui, int x, int y) {
    	
    	this.guard = guard;
    	this.gui = gui;
    	
    	xPos = 440;
        yPos = 560;
        xDestination = x;
        yDestination = y;
        setDestination(x, y);
        homeX = x;
        homeY = y;
        exitX = 0;
        exitY = 180;
        tellerX = 400;
        tellerY = 140;
        //speed = 1;
        //randSpeed = new Random();
        newAStar = null;
    }
    
    public void setBankPanel(BankPanel panel) {
    	this.panel = panel;
    }

    public void updatePosition() {
    	if ( ( xPos/10 == xDestination/10 ) && ( yPos/10 == yDestination/10 ) ) {
    		if(command == Command.chaseRobber) {
    			((BankRobberRole) robber).getGui().stopRobbing();
    			guard.msgStoppedBankRobber(robber);
    		}
    		else if(command == Command.disposeOfRobber) {
    			guard.msgAnimationComplete();
    		}
    		else if(command == Command.returnToPos) {
    			
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
        g2.setColor(new Color(636380));
        g2.fillRect(xPos, yPos, 20, 20);
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
    
    public void reachedTeller(BankRobber r) { //from BankRobberGui
    	guard.msgFailedtoStopRobber(r);
    }

    public void DoChaseRobber(BankRobber r) { 
    	boolean chase = random.nextBoolean();
    	
    	if(chase) {
    		robber = r;
        	//speed = randSpeed.nextInt(4)+1; 
        	setDestination(tellerX, tellerY);
        	command = Command.chaseRobber;
    	}
		
		/*timer.schedule(new TimerTask() {
			public void run() {
				if(!stoppedRobber) { //the BankGuardGui did not chase down the BankRobber in time
					guard.msgFailedtoStopRobber();
				}
			}
		}, 
		1000);*/
	}
	
	public void DoDisposeOfRobber() {
		//speed = 1;
		setDestination(exitX, exitY);
		command = Command.disposeOfRobber;
	}
	
	public void DoReturnToHomePos() {
		//speed = 1;
		setDestination(homeX, homeY);
		command = Command.returnToPos;
	}

	public void removeGui(BankGuardGui g) {
		panel.removeGui((Gui) g);
	}
}      