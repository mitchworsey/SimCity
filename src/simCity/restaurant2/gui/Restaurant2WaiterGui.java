package simCity.restaurant2.gui;


import simCity.gui.Gui;
import simCity.gui.SimCityGui;
import simCity.restaurant2.Restaurant2CustomerRole;
import simCity.restaurant2.Restaurant2WaiterRole;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Waiter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Restaurant2WaiterGui implements Gui {

    private Restaurant2Waiter agent = null;
    
    private Timer timer = new Timer();
   
    private Map<Integer, Pair<Integer, Integer>> tableMap = new HashMap<Integer, Pair<Integer, Integer>>();
    
    class Pair<X, Y> { 
		public final X x; 
		public final Y y; 
		Pair(X x, Y y) { 
			this.x = x; 
		    this.y = y; 
		} 
	} 

    public int xHome = 50;
    public int yHome = 0;
    public int xChef = 710;
    public int yChef = 290;
    public int xWheel = 775;
    public int yWheel = 235;
    public int xCustomer = 0;
    public int yCustomer = 80;
    final int xCashier = 220;
    final int yCashier = 725;
    final int xBreakArea = 750;
    final int yBreakArea = 725;
    
    final int agentWidth = 20;
    final int agentHeight = 20;
    final int messageWidth = 25;
    final int messageHeight = 17;
    final int foodWidth = 15;
    final int foodHeight = 15;
    final int tableFoodWidth = 25;
    final int tableFoodHeight = 25;
    final int sudWidth = 10;
    final int sudHeight = 10;
    int tableWidth = 50;
    int tableHeight = 50;
    
    private int xPos = xHome, yPos = yHome; //default waiter position
    private int xDestination = -20, yDestination = -20; //default start position
    
    private enum Command 
    {NoCommand, SeatCustomer, GoHome, GoToTable, DeliverOrder, PickUpOrder, GoToCashier, GoOnBreak, GoToCustomer};
    private Command command = Command.NoCommand;
    
    private enum State
    {NoState, TakeOrder, OrderTaken, CarryFood, DeliverFood, FoodDelivered, CleanTable, TableCleaned}
    private State state = State.NoState;
    
    String foodCarried = "";
    String displayName = "";
    
    
    

    public Restaurant2WaiterGui(Restaurant2Waiter agent) {
        this.agent = agent;
        
        if(agent.getName().length() == 0) {
        	displayName = "";
        }
        else if (agent.getName().length() == 1 || agent.getName().length() == 2) {
        	displayName = agent.getName();
        }
        else { // agent name >= 2 
        	displayName = agent.getName().substring(0,2);
        }
    }
    
    public void createTable(int tableNum, int tableX, int tableY) {
    	tableMap.put(tableNum, new Pair(tableX, tableY));
    }
    

    public void updatePosition() {
        if (xPos < xDestination) {
            xPos++;
    	}
        else if (xPos > xDestination) {
            xPos--;
        }

        if (yPos < yDestination) {
        	yPos++;
        }
        else if (yPos > yDestination) {
            yPos--;
        }

        // Check to see if the waiter has reached destination yet
        if (xPos == xDestination && yPos == yDestination) {
        	// Check to see if waiter's destination was home
        	if (command == Command.GoHome) {
        		agent.MsgAtHome();
        		command = Command.NoCommand;
        	}
        	// Check to make sure there was a destination set
        	else if (command == Command.SeatCustomer || command == Command.GoToTable || command == Command.DeliverOrder
        			|| command == Command.PickUpOrder || command == Command.GoToCashier || command == Command.GoToCustomer) {
        		agent.MsgAtDestination();
        		// Check if waiter was getting food from chef
        		if (command == Command.PickUpOrder) {
        			state = State.CarryFood;
        		}
        		command = Command.NoCommand;
        	}
        }
 
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, agentWidth, agentHeight);
        g.setColor(Color.BLACK);
        g.drawString(displayName, xPos, yPos + 2*agentHeight/3);
        
        if (state == State.TakeOrder) {
        	g.setColor(Color.WHITE);
            g.fillOval(xPos, yPos - messageHeight, messageWidth, messageHeight);
            g.setColor(Color.BLACK);
            g.drawOval(xPos, yPos - messageHeight, messageWidth, messageHeight);
            g.drawString("?", xPos + messageWidth/3, yPos - messageHeight/3);
        }
        
        if (state == State.CarryFood) {
        	g.setColor(Color.CYAN);
        	g.fillRect(xPos, yPos + agentHeight, foodWidth, foodHeight);
        	g.setColor(Color.BLACK);
        	g.drawString(foodCarried.substring(0,2), xPos, yPos + agentHeight + 2*foodHeight/3);
        }
        
        if(state == State.DeliverFood) {
        	g.setColor(Color.CYAN);
        	g.fillRect(xPos, yPos + agentHeight, tableFoodWidth, tableFoodHeight);
        	g.setColor(Color.BLACK);
        	g.drawString(foodCarried.substring(0,2) + "!", xPos + tableFoodWidth/5, yPos + agentHeight + 2*tableFoodHeight/3);
        }
        
        if(state == State.CleanTable) {
        	// Draw circular suds to fill entire table
        	for(int i = xPos - 20; i < xPos - 20 + tableWidth; i = i + sudWidth) {
        		for(int j = yPos + agentHeight; j < yPos + agentHeight + tableHeight; j = j + sudHeight) {
        			g.setColor(Color.BLACK);
        			g.drawOval(i, j, sudWidth, sudHeight);
        			g.setColor(Color.WHITE);
        			g.fillOval(i, j, sudWidth, sudHeight);
        		}
        	}
        }
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setBreak() {
    	agent.MsgWantBreak();
    }
    
    public void offBreak() {
    	agent.MsgOffBreak();
    	toggleBreak();
    }
    
    public void enableBreak() {
    	//gui.setAgentEnabled((Restaurant2WaiterRole) agent);
    }
    
    public void toggleBreak() {
    	//gui.toggleAgentText((Restaurant2WaiterRole) agent);
    }

    public void DoSeatCustomer(Restaurant2Customer c, int tableNum) {
    	// infinitely call the BeginMoveToXY until it returns true, meaning the customer has gotten the FollowMe message and the Restaurant2CustomerGui is also ready to move
    	while ( !( c.getGui().BeginMoveToXY( tableMap.get(tableNum).x, tableMap.get(tableNum).y ) ) );
    	DoGoToTable(tableNum);
    	command = Command.SeatCustomer;
    }
    
	public void DoGoHome() {
		xDestination = xHome;
		yDestination = yHome;
		command = Command.GoHome;
	}

	public void DoGoToTable(int tableNum) {
		// USE MAP TO SET xDestination & yDestination
		xDestination = (tableMap.get(tableNum).x + agentWidth);
		yDestination = (tableMap.get(tableNum).y - agentHeight);
		command = Command.GoToTable;
	}
	
	public void DoGoToCashier() {
		xDestination = xCashier;
		yDestination = yCashier;
		command = Command.GoToCashier;
	}
	

	public void DoTakeOrder(int tableNum) {
		// Do gui stuff here
		state = State.TakeOrder;
		timer.schedule(new TimerTask() {
			public void run() {
				// Do the gui cleanup here
				state = State.OrderTaken;
				agent.MsgActionComplete();
			}
		},
		5000);
	}

	public void DoDeliverOrder() {
		DoGoToChef();
		command = Command.DeliverOrder;
	}
	
	public void DoDeliverOrderToWheel() {
		xDestination = xWheel;
		yDestination = yWheel;
		command = Command.DeliverOrder;
	}
	
	public void DoPickUpOrder(String foodChoice) {
		foodCarried = foodChoice;
		DoGoToChef();
		command = Command.PickUpOrder;
	}
	
	public void DoGoToChef() {
		xDestination = xChef;
		yDestination = yChef;
	}

	public void DoDeliverFood(int tableNum) {
		// Do gui stuff here
		state = State.DeliverFood;
		timer.schedule(new TimerTask() {
			public void run() {
				// Do the gui cleanup here
				state = State.FoodDelivered;
				agent.MsgActionComplete();
			}
		},
		4000);		
	}

	public void DoCleanTable(int tableNum) {
		// Do gui stuff here
		state = State.CleanTable;
		timer.schedule(new TimerTask() {
			public void run() {
				// Do the gui cleanup here
				state = State.TableCleaned;
				agent.MsgActionComplete();
			}
		},
		6000);
	}
	
	public void DoGoOnBreak() {
		xDestination = xBreakArea;
		yDestination = yBreakArea;
		command = Command.GoOnBreak;
	}
	
	public void DoGoToCustomer() {
		xDestination = xCustomer + 20;
		yDestination = yCustomer - 20;
		command = Command.GoToCustomer;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	
	
}
