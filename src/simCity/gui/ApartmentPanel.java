package simCity.gui;

import javax.management.relation.Role;
import javax.swing.*;

import simCity.OrdinaryPerson;
import simCity.bank.BankCustomerRole;
import simCity.bank.BankTellerRole;
import simCity.house.HouseResidentRole;
import simCity.interfaces.BankCustomer;
import simCity.interfaces.BankCustomerGuiInterface;
import simCity.interfaces.BankTeller;
import simCity.interfaces.Person;
import simCity.interfaces.Restaurant1Customer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ApartmentPanel extends JPanel implements ActionListener, MouseListener {

    private final int WINDOWX = 900;
    private final int WINDOWY = 700;
    static Grid grid;
    private SimCityGui mainGui;
    private int takenApartments; 
    private String apartmentNames[];
    private HashMap<Integer, HouseResidentRole> apartmentTenants = new HashMap<Integer, HouseResidentRole>();

    List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/USCHousingDesign.png");
        private Image image = imageIcon.getImage();


    public ApartmentPanel(int num) {

    	addMouseListener(this);
        setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        grid = new Grid(WINDOWX, WINDOWY);
        apartmentNames = new String[16];
        for (int i=0; i < 16; i++){
        	apartmentNames[i] = new String("noTenant");
        }
        
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
        
        g2.setColor(Color.BLACK);
        if (image != null){
            g2.drawImage(image, 100, 50, 150, 130, null);
           // if (!apartmentNames[0].equals("noTenant"));
            	g2.drawString(apartmentNames[0], 120, 180);
            g2.drawImage(image, 270, 50, 150, 130, null);
           // if (!apartmentNames[1].equals("noTenant"))
            	g2.drawString(apartmentNames[1], 290, 180);
            g2.drawImage(image, 440, 50, 150, 130, null);
            //if (!apartmentNames[2].equals("noTenant"))
            	g2.drawString(apartmentNames[2], 460, 180);
            g2.drawImage(image, 610, 50, 150, 130, null);
            //if (!apartmentNames[3].equals("noTenant"))
            	g2.drawString(apartmentNames[3], 630, 180);
            g2.drawImage(image, 100, 190, 150, 130, null);
            //if (!apartmentNames[4].equals("noTenant"))
            	g2.drawString(apartmentNames[4], 290, 320);
            g2.drawImage(image, 270, 190, 150, 130, null);
            //if (!apartmentNames[5].equals("noTenant"))
            	g2.drawString(apartmentNames[5], 460, 320);
            g2.drawImage(image, 440, 190, 150, 130, null);
            //if (!apartmentNames[6].equals("noTenant"))
            	g2.drawString(apartmentNames[6], 630, 320);
            g2.drawImage(image, 610, 190, 150, 130, null);
            //if (!apartmentNames[7].equals("noTenant"))
            	g2.drawString(apartmentNames[7], 120, 320);
            g2.drawImage(image, 100, 330, 150, 130, null);
            //if (!apartmentNames[8].equals("noTenant"))
            	g2.drawString(apartmentNames[8], 290, 460);
            g2.drawImage(image, 270, 330, 150, 130, null);
            //if (!apartmentNames[9].equals("noTenant"))
            	g2.drawString(apartmentNames[9], 460, 460);
            g2.drawImage(image, 440, 330, 150, 130, null);
            //if (!apartmentNames[10].equals("noTenant"))
            	g2.drawString(apartmentNames[10], 630, 460);
            g2.drawImage(image, 610, 330, 150, 130, null);
            //if (!apartmentNames[11].equals("noTenant"))
            	g2.drawString(apartmentNames[11], 120, 460);
            g2.drawImage(image, 100, 470, 150, 130, null);
            //if (!apartmentNames[12].equals("noTenant"))
            	g2.drawString(apartmentNames[12], 290, 600);
            g2.drawImage(image, 270, 470, 150, 130, null);
            //if (!apartmentNames[13].equals("noTenant"))
            	g2.drawString(apartmentNames[13], 460, 600);
            g2.drawImage(image, 440, 470, 150, 130, null);
            //if (!apartmentNames[14].equals("noTenant"))
            	g2.drawString(apartmentNames[14], 630, 600);
            g2.drawImage(image, 610, 470, 150, 130, null);
            //if (!apartmentNames[15].equals("noTenant"))
            	g2.drawString(apartmentNames[15], 120, 600);
        }
    }
    
    public void arrivedAtApartment(HouseResidentRole residentRole){
    	boolean found = false;
    	synchronized(apartmentTenants){
    		for (int i = 0; i < apartmentTenants.size() ; i++) {
    			if (apartmentTenants.get(i) == residentRole){
    				found = true;
    			}
    		}
    	}
    	if (!found) {
    		System.out.println("added to the list");
    		apartmentTenants.put(takenApartments, residentRole);
    		apartmentNames[takenApartments] = residentRole.getName();
    		takenApartments +=1;
    	}
    }

	public void mouseClicked(MouseEvent m) {
		for (int i = 0; i < 4; i++){
			if (m.getX() >= 100+(170*i) && m.getX() < 250+(170*i) 
					&& m.getY() >= 50 && m.getY() < 180){
				if (apartmentTenants.size() > i){
					System.out.println("Change panel " + apartmentTenants.get(i).getName());
					mainGui.changeHousePanel(apartmentTenants.get(i));
					return;
				}
				/*
				else
					mainGui.changeHousePanel(null);
					*/
			}
			else if (m.getX() >= 100+(170*i) && m.getX() < 250+(170*i) 
					&& m.getY() >= 190 && m.getY() < 320){
				if (apartmentTenants.size() > 4 + i){
					mainGui.changeHousePanel(apartmentTenants.get(4 + i));
					return;
				}
				/*
				else
					mainGui.changeHousePanel(null);
					*/
			}	
			else if (m.getX() >= 100+(170*i) && m.getX() < 250+(170*i) 
					&& m.getY() >= 330 && m.getY() < 460){
				if (apartmentTenants.size() > 8 + i){
					mainGui.changeHousePanel(apartmentTenants.get(8 + i));
					return;
				}
				/*
				else
					mainGui.changeHousePanel(null);
					*/
			}
			else if (m.getX() >= 100+(170*i) && m.getX() < 250+(170*i) 
					&& m.getY() >= 470 && m.getY() < 600){
				if (apartmentTenants.size() > 12 + i){
					mainGui.changeHousePanel(apartmentTenants.get(12 + i));
					return;
				}
				/*
				else
					mainGui.changeHousePanel(null);
					*/
			}
		}
		mainGui.changeHousePanel(null);
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
