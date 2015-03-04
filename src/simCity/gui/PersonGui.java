package simCity.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import simCity.BusStopAgent;
import simCity.Location;
import simCity.OrdinaryPerson;
import simCity.OrdinaryPerson.TransportationType;
import simCity.Role;
import simCity.interfaces.Person;
import simCity.interfaces.PersonGuiInterface;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PersonGui implements Gui, PersonGuiInterface{

        SimCityGui gui;
        
        ImageIcon icon;
        BufferedImage image;

    	static final int STOP1X = 260;
    	static final int STOP1Y = 140;
    	static final int STOP2X = 635;
    	static final int STOP2Y = 410;
        private boolean move1;
        private int waitingCounter;
        
        static final int BUSSTOP1X = 180;
        static final int BUSSTOP1Y = 400;
        static final int BUSSTOP2X = 715;
        static final int BUSSTOP2Y = 120;
        
        static final int PARKINGGARAGE1X = 140;
        static final int PARKINGGARAGE1Y = 55;
        static final int PARKINGGARAGE2X = 800;
        static final int PARKINGGARAGE2Y = 510;
        
        static final int CROSSWALKX = 720;
        static final int CROSSWALKY = 410;
        static final int BOTTOMRIGHTX = 690;
        static final int BOTTOMRIGHTY = 410;
        static final int BOTTOMLEFTX = 220;
        static final int BOTTOMLEFTY = 410;
        static final int TOPLEFTX = 220;
        static final int TOPLEFTY = 150;
        static final int TOPRIGHTX = 690;
        static final int TOPRIGHTY = 150;
        
        private int xCar, yCar, carGarage;

        private boolean isPresent = true;
        private int xPos, yPos;
        private int xPosSmall, yPosSmall;
        private int xDestination, yDestination;
        private int xFinalDestination, yFinalDestination;
        private boolean move = true;
        private int countDown = 0;
        private int count;
        private boolean sleep = false;
        private aStar newAStar;
        private List<Node> finalList; 
        private Node tempNode;
        int busStopNumber;
        BusStopPersonGui bspg;
        int xPosOld;
        int yPosOld;
        int slowCount = 0;
        
        boolean onRoad = false;
        
        public Person agent = null;
        String displayName = "";
        String guiFileName = "";
        String carFileName = "";
        
        String corpseFileName = "";
        String explosionFileName = "";
        
        private Timer timer = new Timer();
        public Map<Integer, Location> busStops = new HashMap<Integer, Location>();
        
        final int agentWidth = 15;
        final int agentHeight = 15;
        
        TransportationType transportation = TransportationType.car;
        
        private enum Command 

        {NoCommand, GoToBusStop, GoToHousing, GoToMarket, GoToMarketGrocer, GoToBank, GoToBankTeller, GoToBankGuard, GoToRestaurant1, GoToRestaurant2, GoToRestaurant3, GoToRestaurant4,
        	GoToRestaurant5, GoToRestaurant1Waiter, GoToRestaurant1Host, GoToRestaurant1Cook, GoToRestaurant1Cashier, GoToHousingOffice,
        	GoToHousingOfficeOwner, GoToHousingOfficeManager, GoToGarage, DriveToGarage, CheckTempCommand,
        	GoToCrosswalk, GoToBottomLeft, GoToTopLeft, GoToTopRight, GoToBottomRight, GoToGarage2 // retard commands
        	};

        	
        private Command command = Command.NoCommand;
        private Command tempCommand = Command.NoCommand;
        
        public PersonGui(Person agent, SimCityGui gui, int xStart, int yStart) {
            this.agent = agent;
            this.gui = gui;
            xPos = xStart;
            yPos = yStart;
            xDestination = xStart;
            yDestination = yStart;
            xCar = PARKINGGARAGE2X;
            yCar = PARKINGGARAGE2Y;
            carGarage = 2;
            newAStar = null;
            count = 0;
            waitingCounter = 0;
            
            if(agent.getName().length() == 0) {
            	displayName = "";
            }
            else if (agent.getName().length() == 1 || agent.getName().length() == 2) {
            	displayName = agent.getName();
            }
            else { // agent name >= 2 
            	displayName = agent.getName().substring(0,2);
            }
            loadImage("src/simCity/gui/images/newCity/malegreen20.png");
            setImageString("src/simCity/gui/images/newCity/malegreen20.png");
            setCarString("src/simCity/gui/images/newCity/car.png");
            explosionFileName = "src/simCity/gui/images/explosion.png";
            corpseFileName = "src/simCity/gui/images/skull.png";
            
            /// INITIALIZE corpseFileName && explosionFileName IN CONSTRUCTOR
        }

        public PersonGui(Person agent, SimCityGui gui) { 
                xPos = 500;
                yPos = 210;
                xPosSmall = xPos;
                yPosSmall = yPos;
                xDestination = 50;
                yDestination = 50;
                xCar = PARKINGGARAGE2X;
                yCar = PARKINGGARAGE2Y;
                carGarage = 2;
                newAStar = null;
                this.gui = gui;
                this.agent = agent;
                count = 0;
                move1 = true;
                waitingCounter = 0;
                
                if(agent.getName().length() == 0) {
                	displayName = "";
                }
                else if (agent.getName().length() < 5) {
                	displayName = agent.getName();
                }
                else { // agent name >= 5 
                	displayName = agent.getName().substring(0,5);
                }
                loadImage("src/simCity/gui/images/newCity/malegreen20.png");
                setImageString("src/simCity/gui/images/newCity/malegreen20.png");
                setCarString("src/simCity/gui/images/newCity/car.png");
                explosionFileName = "src/simCity/gui/images/explosion.png";
                corpseFileName = "src/simCity/gui/images/skull.png";
              /// INITIALIZE corpseFileName && explosionFileName IN CONSTRUCTOR
        }
        

        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#updatePosition()
		 */
        @Override
		public void updatePosition() {
        	if (slowCount == 3) {
        		slowCount = 0;
        	}
        	slowCount++;
        	 if ( ( xPos/10 == xDestination/10 ) && ( yPos/10 == yDestination/10 ) ) {
        		if (command != Command.NoCommand) {
        			
        			//// BEGIN RETARD LOOPS
        			if (command == Command.GoToGarage2){
        				loadImage(carFileName);
        				setCarDestination(BOTTOMRIGHTX, BOTTOMRIGHTY);
        				command = Command.GoToBottomRight;
        			}
        			
        			else if (command == Command.GoToCrosswalk){
        				setCarDestination(BOTTOMRIGHTX, BOTTOMRIGHTY);
        				command = Command.GoToBottomRight;
        			}
        			
        			else if (command == Command.GoToBottomRight) {
        				onRoad = true;
        				setCarDestination(BOTTOMLEFTX, BOTTOMLEFTY);
        				command = Command.GoToBottomLeft;
        			}
        			
        			else if (command == Command.GoToBottomLeft) {
        				setCarDestination(TOPLEFTX, TOPLEFTY);
        				command = Command.GoToTopLeft;
        			}
        			
        			else if (command == Command.GoToTopLeft) {
        				setCarDestination(TOPRIGHTX, TOPRIGHTY);
        				command = Command.GoToTopRight;
        			}
        			
        			else if (command == Command.GoToTopRight) {
        				setCarDestination(BOTTOMRIGHTX, BOTTOMRIGHTY);
        				command = Command.GoToBottomRight;
        			}
        			//// END RETARD LOOPS
        			
        			else if (command == Command.GoToBusStop) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtBusStop();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToHousing) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtHousing();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToHousingOffice) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtHousingOffice();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToHousingOfficeOwner) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtHousingOfficeOwner();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToHousingOfficeManager) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtHousingOfficeManager();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToMarket) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtMarket();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToMarketGrocer) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtMarketGrocer();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToBank) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtBank();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToBankTeller) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtBankTeller();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToBankGuard) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtBankGuard();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant1) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    					CityPanel.semaphoreGrid.getSemaphore(xPosOld,  yPosOld).release();
	             		agent.ArrivedAtRestaurant1();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant1Waiter) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    					CityPanel.semaphoreGrid.getSemaphore(xPosOld,  yPosOld).release();
	             		agent.ArrivedAtRestaurant1Waiter();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant1Host) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    					CityPanel.semaphoreGrid.getSemaphore(xPosOld,  yPosOld).release();
	             		agent.ArrivedAtRestaurant1Host();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant1Cook) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    					CityPanel.semaphoreGrid.getSemaphore(xPosOld,  yPosOld).release();
	             		agent.ArrivedAtRestaurant1Cook();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant1Cashier) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    					CityPanel.semaphoreGrid.getSemaphore(xPosOld,  yPosOld).release();
	             		agent.ArrivedAtRestaurant1Cashier();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant2) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtRestaurant2();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant3) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtRestaurant3();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant4) {
    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtRestaurant4();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToRestaurant5) {
	             		CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		agent.ArrivedAtRestaurant5();
	             		command = Command.NoCommand;
	             	}
	             	else if (command == Command.GoToGarage) {
	             		CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		command = Command.DriveToGarage;
	             		if (carGarage == 1) {
	             			carGarage = 2;
	             			loadImage(carFileName);
	             			setCarDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
	             		}
	             		else { // carGarage == 2
	             			carGarage = 1;
	             			loadImage(carFileName);
	             			setCarDestination(PARKINGGARAGE1X, PARKINGGARAGE1Y);
	             		}
	             	}
	             	else if (command == Command.DriveToGarage){
	             		CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	             		loadImage(guiFileName);
	             		setDestination(xFinalDestination, yFinalDestination);
	             		command = Command.CheckTempCommand;
	             	}
	             	else if (command == Command.CheckTempCommand){
	             		command = Command.NoCommand;
	             		if (tempCommand == Command.GoToBusStop) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtBusStop();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToHousing) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtHousing();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToHousingOffice) {
		             		agent.ArrivedAtHousingOffice();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToMarket) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtMarket();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToBank) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtBank();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToBankTeller) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtBankTeller();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant1) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant1();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant1Waiter) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant1Waiter();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant1Host) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant1Host();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant1Cook) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant1Cook();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant1Cashier) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant1Cashier();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant2) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant2();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant3) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant3();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant4) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant4();
		             		tempCommand = Command.NoCommand;
		             	}
		             	else if (tempCommand == Command.GoToRestaurant5) {
	    					CityPanel.semaphoreGrid.getSemaphore(xPos,  yPos).release();
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
		             		agent.ArrivedAtRestaurant5();
		             		tempCommand = Command.NoCommand;
		             	}
	             	}
        		}
            }

             //If bus is at stop light and light is red, disable movement
             if (xPosSmall > STOP1X && xPosSmall < STOP1X+20 && yPosSmall > STOP1Y && yPosSmall < STOP1Y + 60 && SimCityGui.controlPanel.getRedLight()){
             	move = false;
             }
             else if (xPosSmall > STOP2X && xPosSmall < STOP2X+20 && yPosSmall > STOP2Y && yPosSmall < STOP2Y + 60 && SimCityGui.controlPanel.getRedLight()){
             	move = false;
             }
             else
             	move = true;
            if (move){
			if ( transportation == TransportationType.retardDriver || transportation == TransportationType.retardWalker ) {
				if (slowCount == 3) {
					if (finalList != null){
				    	if (!finalList.isEmpty()){
				    		if (finalList.size()>=1){
				    			//simCity.gui.CityPanel.grid.release(xPos/10, yPos/10);
				    			xPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getX() * 10;
				    			yPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getY() * 10;
				    			xPosSmall = xPos;
				    			yPosSmall = yPos;
				    			//simCity.gui.CityPanel.grid.set(xPos/10, yPos/10);
				    			
				    			if (onRoad) {
				        			///// CHECK NUMBER OF PERMITS AT SEMAPHOREGRID.GETSEMAPHORE[X][Y] AT EVERY NODE MOVE
				        			///// (BUT DONT ACTUALLY TRYACQUIRE/ACQUIRE A PERMIT, JUST CHECK THE NUMBER THERE.)
				        			///// (IF THE NUMBER == 0, MAKE THE PERSONGUI STOP, AND THEN UNCOMMENT OUT THE FOLLOWING
				        			///// BLOCK OF CODE:
				        			
				    				if (CityPanel.semaphoreGrid.getSemaphore(finalList.get(finalList.size()-1).getX()*10, 
		        							finalList.get(finalList.size()-1).getY()*10).availablePermits() == 0) {
				    					finalList = null;
					        			if (transportation == TransportationType.retardDriver) {
					        				loadImage(explosionFileName);
					        				timer.schedule(new TimerTask() {
					    							public void run() {
					    								///// DO THE GUI CLEAN UP HERE, COULD JUST LOAD NO IMAGE, COULD DELETE PERSON, COULD CALL FUNCTION ETC
					    								DoGuiCleanUp();
					    							}
					    					}, 1500); // 1.5 second explosion
					        			}
					        			if (transportation == TransportationType.retardWalker) {
					        				loadImage(corpseFileName);
					        				timer.schedule(new TimerTask() {
												public void run() {
													///// DO THE GUI CLEAN UP HERE, COULD JUST LOAD NO IMAGE, COULD DELETE PERSON, COULD CALL FUNCTION ETC
													DoGuiCleanUp();
												}
					        				}, 7500); // corpse lasts 7.5 seconds
					        			}
				        			}
				    			}
				    			
				    			if (finalList != null) {
				    				finalList.remove(finalList.get(finalList.size()-1));
				    			}
				    		}
				    	}
					}
				}
			}

			else {
	        	if (finalList != null){
	        		if (!finalList.isEmpty()){
	        			if (finalList.size()>=1){
	        				if (count % 10 == 0) {
	        					if (waitingCounter > 50){
	        						waitingCounter = 0;
	        						CityPanel.semaphoreGrid.getSemaphore(finalList.get(finalList.size()-1).getX()*10, 
		        							finalList.get(finalList.size()-1).getY()*10).release();
	        						CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
	        						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	        					}
	        					if (CityPanel.semaphoreGrid.getSemaphore(finalList.get(finalList.size()-1).getX()*10, 
	        							finalList.get(finalList.size()-1).getY()*10).tryAcquire()){
	        						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
	        						xPosOld = xPos;
	        						yPosOld = yPos;
	        						xPos = finalList.get(finalList.size()-1).getX()*10;
	        						yPos = finalList.get(finalList.size()-1).getY()*10;
	        						finalList.remove(finalList.get(finalList.size()-1));
	        						xPosSmall = xPos;
	        						yPosSmall = yPos;
	        					}
	        					else {
	        						waitingCounter +=1; 
	        					}
	        				}
	        				else {
	        			        if (xPosSmall < finalList.get(finalList.size()-1).getX()*10)
	        			            xPosSmall = xPosSmall + 1;
	        			        else if (xPosSmall > finalList.get(finalList.size()-1).getX()*10)
	        			            xPosSmall = xPosSmall - 1;
	        			        if (yPosSmall < finalList.get(finalList.size()-1).getY()*10)
	        			            yPosSmall = yPosSmall + 1;
	        			        else if (yPosSmall > finalList.get(finalList.size()-1).getY()*10)
	        			            yPosSmall = yPosSmall -1;
	        				}
	        				count += 1;
	        			}
	        		}
	        	}
			}
            }
			
        }

        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#draw(java.awt.Graphics2D)
		 */
        @Override
		public void draw(Graphics2D g) {
        	Graphics2D g4 = (Graphics2D)g;
        	
        	/*
        	g4.setColor(Color.RED);
        	g4.fillRect(xPosSmall, yPosSmall, 15, 15);
        	g4.setColor(Color.GREEN);
        	g4.drawRect(xPosSmall, yPosSmall, 15, 15);
        	*/
        	
        	g4.drawImage(image, xPosSmall, yPosSmall, null);
        	g4.setColor(Color.BLACK);
        	g4.drawString(displayName, xPosSmall, yPosSmall -5);
        	
        	/*for (Node node : finalList){
        		g4.setColor(Color.BLUE);
        		g4.drawRect(node.getX()*10, node.getY()*10, 10, 10);
        	}*/
        }

        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#isPresent()
		 */
        @Override
		public boolean isPresent() {
                return isPresent;
        }
        
        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#setPresent(boolean)
		 */
        @Override
		public void setPresent(boolean p) {
                isPresent = p;
        }
        
        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#getX()
		 */
        @Override
		public int getX() {
        	return xPos;
        }
        
        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#getY()
		 */
        @Override
		public int getY() {
        	return yPos;
        }
        
        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#setDestination(int, int)
		 */
        @Override
		public void setDestination(int x, int y){
        	//= Collections.synchronizedList(new ArrayList<Node>());
        	xDestination = x;
        	yDestination = y;
            newAStar = new aStar(xPos, yPos, xDestination, yDestination, CityPanel.grid);
            finalList = newAStar.getBestPath();
        }
        
        public void setCarDestination(int x, int y){
        	//= Collections.synchronizedList(new ArrayList<Node>());
         	xDestination = x;
         	yDestination = y;
            newAStar = new aStar(xPos, yPos, xDestination, yDestination, CityPanel.busGrid);
            finalList = newAStar.getBestPath();
        }
        
        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoRideBus(simCity.Location)
		 */
        @Override
		public boolean DoRideBus(Location location) {
        	aStar temp = new aStar(xPos, yPos, location.x, location.y, CityPanel.grid);
        	List<Node> directPath = temp.getBestPath();
        	if (directPath == null) { //this implies you are directly at the component. Don't need to ride the bus
        		return false;
        	}
        	List<Node> toBusStop, toDestination;
        	int distanceToBS1 = (Math.abs(xPos - BUSSTOP1X) + Math.abs(yPos - BUSSTOP1Y) );
        	int distanceToBS2 = (Math.abs(xPos - BUSSTOP2X) + Math.abs(yPos - BUSSTOP2Y) );
        	// implies BUSSTOP1 is closer than BUSSTOP2 to person
        	if ( distanceToBS1 <= distanceToBS2 ) {
        		temp = new aStar(xPos, yPos, BUSSTOP1X, BUSSTOP1Y, CityPanel.grid);
        		toBusStop = temp.getBestPath();
        		temp = new aStar(BUSSTOP2X, BUSSTOP2Y, location.x, location.y, CityPanel.grid);
        		toDestination = temp.getBestPath();
        	}
        	else { // implies BUSSTOP2 is closer than BUSSTOP1 to person
        		temp = new aStar(xPos, yPos, BUSSTOP2X, BUSSTOP2Y, CityPanel.grid);
        		toBusStop = temp.getBestPath();
        		temp = new aStar(BUSSTOP1X, BUSSTOP1Y, location.x, location.y, CityPanel.grid);
        		toDestination = temp.getBestPath();
        	}
        	if (toBusStop == null) {
        		toBusStop = new ArrayList<Node>();
        	}
        	if (toDestination == null) {
        		toDestination = new ArrayList<Node>();
        	}
        	if ( (toBusStop.size() + toDestination.size()) <  (directPath.size() + 2) ) {
        		// implies taking Bus would be shorter than walking (+4 to account for laziness factor)
        		if ( distanceToBS1 <= distanceToBS2 ) {
        			Location l = new Location(BUSSTOP1X, BUSSTOP1Y);
        			agent.setBusLocation("BusStop1");
        			agent.setBusDestination("BusStop2");
        			busStopNumber = 1;
        			DoGoToBusStop(l);
        			return true;
        		}
        		else {
        			Location l = new Location(BUSSTOP2X, BUSSTOP2Y);
        			agent.setBusLocation("BusStop2");
        			agent.setBusDestination("BusStop1");
        			busStopNumber = 2;
        			DoGoToBusStop(l);
        			return true;
        		}
        	}
        	else {
        		return false;
        	}
        }
        
        public boolean DoDriveCar(Location location) {
        	//System.out.println("In DoDriveCar");
        	aStar temp = new aStar(xPos, yPos, location.x, location.y, CityPanel.grid);
        	List<Node> directPath = temp.getBestPath();
        	if (directPath == null) { //this implies you are directly at the component. Don't need to drive car
        		return false;
        	}
        	List<Node> toCarGarage, toDestination;
        	// implies car at garage 2
        	if ( carGarage == 1 ) {
        		temp = new aStar(xPos, yPos, PARKINGGARAGE1X, PARKINGGARAGE1Y, CityPanel.grid);
        		toCarGarage = temp.getBestPath();
        		temp = new aStar(PARKINGGARAGE2X, PARKINGGARAGE2Y, location.x, location.y, CityPanel.grid);
        		toDestination = temp.getBestPath();
        	}
        	else { // implies car at garage 1
        		temp = new aStar(xPos, yPos, PARKINGGARAGE2X, PARKINGGARAGE2Y, CityPanel.grid);
        		toCarGarage = temp.getBestPath();
        		temp = new aStar(PARKINGGARAGE1X, PARKINGGARAGE1Y, location.x, location.y, CityPanel.grid);
        		toDestination = temp.getBestPath();
        	}
        	if (toCarGarage == null) {
        		toCarGarage = new ArrayList<Node>();
        	}
        	if (toDestination == null) {
        		toDestination = new ArrayList<Node>();
        	}
        	if ( (toCarGarage.size() + toDestination.size()) <  (directPath.size() + 35) ) {
        		// implies taking Car would be shorter than walking (+35 to account for laziness factor)
        		if ( carGarage == 1 ) {
        			xFinalDestination = location.x;
        			yFinalDestination = location.y;
        			Location l = new Location(PARKINGGARAGE1X, PARKINGGARAGE1Y);
        			DoWalkToParkingGarage(l);
        			//System.out.println("DoDriveCar true");
        			return true;
        		}
        		else { // car at garage 2
        			xFinalDestination = location.x;
        			yFinalDestination = location.y;
        			Location l = new Location(PARKINGGARAGE2X, PARKINGGARAGE2Y);
        			DoWalkToParkingGarage(l);
        			//System.out.println("DoDriveCar true");
        			return true;
        		}
        	}
        	else {
        		//System.out.println("DoDriveCar false");
        		return false;
        	}
        }
        
        
        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterComponent()
		 */
        @Override
		public void DoEnterComponent() {
        	this.setPresent(false);
        }
        
        /* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoExitComponent()
		 */
        @Override
		public void DoExitComponent() {
        	this.setPresent(true);
        }

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToBusStop(simCity.Location)
		 */
		@Override
		public void DoGoToBusStop(Location location) {
			setDestination(location.x, location.y);
			command = Command.GoToBusStop;
		}
		
		public void DoWalkToParkingGarage(Location location) {
			setDestination(location.x, location.y);
			command = Command.GoToGarage;
		}
		
		public void DoDriveToParkingGarage(Location location) {
			setCarDestination(location.x, location.y);
			command = Command.DriveToGarage;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToHousing(simCity.Location)
		 */
		@Override
		public void DoGoToHousing(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToHousing;
			}
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToHousingOffice(simCity.Location)
		 */
		@Override
		public void DoGoToHousingOffice(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToHousingOffice;
			}
		}

		public void DoGoToHousingOfficeOwner(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToHousingOfficeOwner;
			}
		}

		@Override
		public void DoGoToHousingOfficeManager(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToHousingOfficeManager;
			}
		}
		

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToMarket(simCity.Location)
		 */
		@Override
		public void DoGoToMarket(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToMarket;
			}
		}

		@Override
		public void DoGoToMarketGrocer(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToMarketGrocer;
			}
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToBank(simCity.Location)
		 */
		@Override
		public void DoGoToBank(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToBank;
			}
		}
		

		public void DoGoToBankTeller(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToBankTeller;
			}
		}
		
		public void DoGoToBankGuard(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToBankGuard;
			}
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant5(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant5(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant5;
			}
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant4(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant4(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant4;
			}
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant3(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant3(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant3;
			}
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant2(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant2(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant2;
			}
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant1(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant1(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1;
			}
		}
		
		public void DoGoToRestaurant1Waiter(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Waiter;
			}
		}
		
		public void DoGoToRestaurant1Host(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Host;
			}
		}

		public void DoGoToRestaurant1Cook(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Cook;
			}
		}

		public void DoGoToRestaurant1Cashier(Location location) {
			if ( !(DoRideBus(location)) ) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Cashier;
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~WITH TRANSPORTATIONTYPE ARGUMENT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
		

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToHousing(simCity.Location)
		 */
		@Override
		public void DoGoToHousing(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToHousing;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousing;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousing;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToHousing;
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToHousingOffice(simCity.Location)
		 */
		@Override
		public void DoGoToHousingOffice(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToHousingOffice;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousingOffice;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousingOffice;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToHousingOffice;
		}

		@Override
		public void DoGoToHousingOfficeOwner(Location location, TransportationType type) {
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToHousingOfficeOwner;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousingOfficeOwner;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousingOfficeOwner;
				}
			}
			tempCommand = Command.GoToHousingOfficeOwner;
		}

		@Override
		public void DoGoToHousingOfficeManager(Location location, TransportationType type) {
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToHousingOfficeManager;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousingOfficeManager;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToHousingOfficeManager;
				}
			}
			tempCommand = Command.GoToHousingOfficeManager;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToMarket(simCity.Location)
		 */
		@Override
		public void DoGoToMarket(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToMarket;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToMarket;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToMarket;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToMarket;
		}

		@Override
		public void DoGoToMarketGrocer(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToMarketGrocer;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToMarketGrocer;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToMarketGrocer;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToMarketGrocer;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToBank(simCity.Location)
		 */
		@Override
		public void DoGoToBank(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToBank;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToBank;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToBank;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToBank;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant5(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant5(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant5;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant5;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant5;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant5;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant4(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant4(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant4;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant4;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant4;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant4;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant3(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant3(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant3;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant3;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant3;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant3;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant2(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant2(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant2;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant2;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant2;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant2;
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoGoToRestaurant1(simCity.Location)
		 */
		@Override
		public void DoGoToRestaurant1(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant1;
		}
		
		public void DoGoToRestaurant1Waiter(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Waiter;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Waiter;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Waiter;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant1Waiter;
		}
		
		public void DoGoToRestaurant1Host(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Host;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Host;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Host;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant1Host;
		}

		public void DoGoToRestaurant1Cook(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Cook;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Cook;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Cook;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant1Cook;
		}

		public void DoGoToRestaurant1Cashier(Location location, TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToRestaurant1Cashier;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Cashier;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToRestaurant1Cashier;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToRestaurant1Cashier;
		}
		
		public void DoGoToBankTeller(Location location,
				TransportationType type) {
			finalList = null;
			CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
			transportation = type;
			if (type == TransportationType.walk) {
				setDestination(location.x, location.y);
				command = Command.GoToBankTeller;
			}
			else if (type == TransportationType.bus) {
				if ( !(DoRideBus(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToBankTeller;
				}
			}
			else if (type == TransportationType.car) {
				if ( !(DoDriveCar(location)) ) {
					setDestination(location.x, location.y);
					command = Command.GoToBankTeller;
				}
			}
			else if (type == TransportationType.retardWalker) {
				setDestination(CROSSWALKX, CROSSWALKY);
				command = Command.GoToCrosswalk;
			}
			else if (type == TransportationType.retardDriver) {
				setDestination(PARKINGGARAGE2X, PARKINGGARAGE2Y);
				command = Command.GoToGarage2;
			}
			tempCommand = Command.GoToBankTeller;
		}
		
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterHousing(simCity.Role)
		 */
		@Override
		public void DoEnterHousing(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterHousingOffice(simCity.Role)
		 */
		@Override
		public void DoEnterHousingOffice(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}

		@Override
		public void DoEnterHousingOfficeOwner(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}

		@Override
		public void DoEnterHousingOfficeManager(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterMarket(simCity.Role)
		 */
		@Override
		public void DoEnterMarket(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		@Override
		public void DoEnterMarketGrocer(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterBank(simCity.Role)
		 */
		@Override
		public void DoEnterBank(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		@Override
		public void DoEnterBankTeller(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		@Override
		public void DoEnterBankGuard(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterRestaurant1(simCity.Role)
		 */
		@Override
		public void DoEnterRestaurant1(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		public void DoEnterRestaurant1Waiter(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		public void DoEnterRestaurant1Host(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		public void DoEnterRestaurant1Cook(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		public void DoEnterRestaurant1Cashier(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterRestaurant2(simCity.Role)
		 */
		@Override
		public void DoEnterRestaurant2(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterRestaurant3(simCity.Role)
		 */
		@Override
		public void DoEnterRestaurant3(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterRestaurant4(simCity.Role)
		 */
		@Override
		public void DoEnterRestaurant4(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterRestaurant5(simCity.Role)
		 */
		@Override
		public void DoEnterRestaurant5(Role r) {
			DoEnterComponent();
			SimCityGui.controlPanel.makeNewRoleGui(r);
		}
		
		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoBoardBus()
		 */
		@Override
		public void DoBoardBus() {
			DoEnterComponent();
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoExitBus(simCity.Location)
		 */
		@Override
		public void DoExitBus(Location location) {
			xPos = location.x;
			yPos = location.y;
			DoExitComponent();
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoEnterBusStop()
		 */
		@Override
		public void DoEnterBusStop() {
			if (busStopNumber == 1) {
				int index = SimCityGui.busStopPanel1.nextOpenSeatIndex();
				bspg = new BusStopPersonGui(this, gui, index % 8, index/8);
				SimCityGui.busStopPanel1.addGui(bspg);
			}
			else if (busStopNumber == 2) {
				int index = SimCityGui.busStopPanel2.nextOpenSeatIndex();
				bspg = new BusStopPersonGui(this, gui, index % 8, index/8);
				SimCityGui.busStopPanel2.addGui(bspg);
			}
			DoEnterComponent();
		}

		/* (non-Javadoc)
		 * @see simCity.gui.PersonGuiInterface#DoExitBusStop()
		 */
		@Override
		public void DoExitBusStop() {
			if (busStopNumber == 1) {
				SimCityGui.busStopPanel1.removeGui(bspg);
			}
			else if (busStopNumber == 2) {
				SimCityGui.busStopPanel2.removeGui(bspg);
			}
			DoExitComponent();
		}

		public void setIcon(ImageIcon icon2) {
			icon = icon2;
		}
		
		public void loadImage(String fn) {
			try{
				image = ImageIO.read(new File(fn));
			} catch (IOException e) {}
		}
		
		@Override
		public void didAcquire() {//Semaphore semaphore) {
			//semaphore.acquire();
			countDown = 20;
			move = true;
		}

		@Override
		public void setAgent(OrdinaryPerson ordinaryPerson) {
			agent = ordinaryPerson;
		}
		public void setImageString(String s) {
			guiFileName = s;
		}
		
		public void setCarString(String s) {
			carFileName = s;
		}
		
		private void DoGuiCleanUp() {
			SimCityGui.cityPanel.removeGui(this);
		}

		
}