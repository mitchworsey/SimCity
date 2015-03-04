package simCity.gui.Restaurant1;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

import simCity.gui.Gui;
import simCity.gui.Restaurant1Panel;
import simCity.gui.aStar;
import simCity.interfaces.Restaurant1Waiter;

public class Restaurant1WaiterGui implements Gui{

        public Restaurant1Waiter agent = null;
        Restaurant1Panel gui;
        private Timer timer = new Timer();
        
        /* Final variables */ 
        public static final int XTABLE = 160;
        public static final int YTABLE = 470;
        static final int DOORX = 200;
        static final int DOORY = 100;
        static final int STARTX = 350;
        static final int STARTY = 80;
        static final int MEETX = 150;
        static final int MEETY = 200;
        static final int WIDTH = 27;
        static final int HEIGHT = 38;
        static final int HEIGHTWF = 65;
        static final int TABLE = 170;
        static final int COOKX = 620;
        static final int COOKY = 200;
        
        /* Initialize Variables */
        private int count = 0;
        private int table = 0;
        private int xPos = DOORX;
            private int yPos = DOORY;
        private int xDestination = STARTX;
        private int yDestination = STARTY;
        private boolean wantsBreak = false;
        private boolean getsBreak = false;
        private boolean steak = false;
        private boolean chicken = false;
        private boolean pizza = false;
        private boolean salad = false;
        private boolean isPresent = true;
        
        /* Commands */
        private enum Command {noCommand, GoOnBreak, NoBreak};
        private Command command = Command.noCommand;

        /* Pathfinding */
        private aStar newAStar;
        private List<simCity.gui.Node> finalList = Collections.synchronizedList(new ArrayList<simCity.gui.Node>());
        
        //images
            private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/host.JPG");
            private Image image = imageIcon.getImage();
            private ImageIcon imageIconPizza = new ImageIcon("src/simCity/gui/images/waiterPizza.JPG");
            private Image imagePizza = imageIconPizza.getImage();
            private ImageIcon imageIconSalad = new ImageIcon("src/simCity/gui/images/waiterSalad.JPG");
            private Image imageSalad = imageIconSalad.getImage();
            private ImageIcon imageIconSteak = new ImageIcon("src/simCity/gui/images/waiterSteak.JPG");
            private Image imageSteak = imageIconSteak.getImage();
            private ImageIcon imageIconChicken = new ImageIcon("src/simCity/gui/images/waiterChicken.JPG");
            private Image imageChicken = imageIconChicken.getImage();
        
        public Restaurant1WaiterGui(Restaurant1Waiter agent, Restaurant1Panel gui, int ct) { 
                        count = ct;
                xPos = DOORX;
                yPos = DOORY;
                    xDestination = STARTX + count*30+150;
                    yDestination = STARTY;
                newAStar = null;
                agent.setGui(this);
                this.gui = gui;
                this.agent = agent;
                
                setDestination(xDestination, yDestination);
        }

        public void updatePosition() {
                
                if (finalList != null) {
                        if (!finalList.isEmpty()){
                                if (finalList.size()>=1){
                                        xPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getX();
                                        yPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getY();
                                        finalList.remove(finalList.get(finalList.size()-1));
                                }
                        }
                }
            if (getxPos()/10 == xDestination/10 && getyPos()/10 == yDestination/10
                            & (yDestination == YTABLE-HEIGHT) & (xDestination ==(XTABLE + (0)*TABLE + HEIGHT) ||
                            (xDestination == XTABLE + (0)*TABLE + HEIGHT + WIDTH))) {
               agent.msgAtTable(1);
            }
            else if (getxPos()/10 == xDestination/10 && getyPos()/10 == yDestination/10
                            & (yDestination == YTABLE-HEIGHT) & (xDestination ==(XTABLE + (1)*TABLE + HEIGHT) ||
                            (xDestination == XTABLE + (1)*TABLE + HEIGHT + WIDTH))) {
               agent.msgAtTable(2);
            }
            else if (getxPos()/10 == xDestination/10 && getyPos()/10 == yDestination/10
                            & (yDestination == YTABLE-HEIGHT) & (xDestination ==(XTABLE + (2)*TABLE + HEIGHT) ||
                            (xDestination == XTABLE + (2)*TABLE + HEIGHT + WIDTH))) {
               agent.msgAtTable(3);
            }
            else if (getxPos()/10 == xDestination/10 && getyPos()/10 == yDestination/10
                            & (yDestination == YTABLE-HEIGHT) & (xDestination == (XTABLE + (3)*TABLE + HEIGHT) ||
                            (xDestination == XTABLE + (3)*TABLE + HEIGHT + WIDTH))) {
               agent.msgAtTable(4);
            }
            
            if (xPos/10 == (STARTX + count*WIDTH*2)/10 && yPos/10 == STARTY/10){
                    xPos -= 10;
                agent.msgAtStart();
            }
            
            if (xPos/10 == COOKX/10 && yPos/10 == COOKY/10){
                    agent.msgAtCook();
            } 
            
            if (command == Command.GoOnBreak)
                    //gui.setWaiterEnabled(agent);
            if (command == Command.NoBreak) {
                    //gui.setWaiterEnabled(agent);
                    command = Command.noCommand;
            }
        }
        

        public void draw(Graphics2D g) {
                if (!salad && !steak && !pizza && !chicken) {
                        if (image != null)
                                g.drawImage(image, getxPos(), getyPos(), WIDTH, HEIGHT, null);
                }
                else if (salad) {
                        if (imageSalad != null)
                                g.drawImage(imageSalad,  getxPos(),  getyPos(),  WIDTH,  HEIGHTWF,  null);
                }
                else if (chicken) {
                        if (imageChicken != null)
                                g.drawImage(imageChicken,  getxPos(),  getyPos(),  WIDTH, HEIGHTWF,  null);
                }
                else if (steak) {
                        if (imageSteak != null)
                                g.drawImage(imageSteak,  getxPos(),  getyPos(), WIDTH, HEIGHTWF,  null);
                }
                else if (pizza) {
                        if (imagePizza != null)
                                g.drawImage(imagePizza,  getxPos(),  getyPos(), WIDTH, HEIGHTWF,  null);
                }
        }
        
        public boolean isPresent() {
                return isPresent;
        }
        
        public void setPresent(boolean p) {
                isPresent = p;
        }
        
        public void setDestination(int x, int y){
                xDestination = x;
                yDestination = y;
            newAStar = new aStar(xPos, yPos, xDestination, yDestination, Restaurant1Panel.grid);
            finalList = newAStar.getBestPathSteps();
        }
        
        public void setBreak() {
                wantsBreak = true;
                agent.wantsBreak();
        }
        
        public void setReturn() {
                wantsBreak = false;
                getsBreak = false;
                //agent.backToWork();
                command = Command.NoBreak;
        }
        
        public boolean wantsBreak() {
                return wantsBreak;
        }
        
        public void getBreak() {
                wantsBreak = false;
                getsBreak = true;
                command = Command.GoOnBreak;
        }
        
        public boolean getsBreak() {
                return getsBreak;
        }

        public void DoBringToTable(int tbl) {
                table = tbl;
                setDestination(MEETX, MEETY);
                timer.schedule(new TimerTask() {
                            public void run() {
                                    setDestination(XTABLE + (table-1)*TABLE + HEIGHT, YTABLE-HEIGHT);
                            }
                    },
                    2000);
        }
        
        public void DoGetOrder(int table) {
                setDestination(XTABLE + (table-1)*TABLE + HEIGHT + WIDTH, YTABLE-HEIGHT);
        }
        
        public void DoBringFoodToTable(int table, String food) {
                if (food.equals("pizza"))
                        pizza = true;
                else if (food.equals("steak"))
                        steak = true;
                else if (food.equals("salad"))
                        salad = true;
                else
                        chicken = true;
                setDestination(XTABLE + (table-1)*TABLE + HEIGHT + WIDTH, YTABLE-HEIGHT);
            }
        
        public void DoGiveCheck(int table) {
                setDestination(XTABLE + (table-1)*TABLE + HEIGHT + WIDTH, YTABLE-HEIGHT);
        }

        public void DoLeaveCustomer() {
            setDestination(STARTX + count*WIDTH*2, STARTY);
            pizza = false;
            steak = false;
            salad = false;
            chicken = false;
        }
        
        public void DoSeeCook() {
                setDestination(COOKX, COOKY);
        }
        
        public int getXPos() {
            return getxPos();
        }

        public int getYPos() {
            return getyPos();
        }

            public int getxPos() {
                    return xPos;
            }

            public void setxPos(int xPos) {
                    this.xPos = xPos;
            }

            public int getyPos() {
                    return yPos;
            }

            public void setyPos(int yPos) {
                    this.yPos = yPos;
            }

                public int getX() {
                        return xPos;
                }

                public int getY() {
                        return yPos;
                }
                
}