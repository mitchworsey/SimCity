/*package simCity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import simCity.house.HouseResidentRole;
import simCity.interfaces.HouseResident;

public class HouseControlPanel extends JPanel implements ActionListener{

	static final int BUTTONSIZE = 50;
	
	private HouseGui gui; //reference to main gui
	JButton food;
	JButton money;
	JButton transportation;
	JButton information;
	JButton phone;	
	
	HouseResident hr = new HouseResidentRole("Resident");
	HouseResidentGui residentGui;
	
	private JOptionPane infoPane;
	private JOptionPane transportationPane;
	private JOptionPane phonePane;
	
	private ImageIcon buttonInfoIcon = new ImageIcon("src/simCity/gui/images/buttonInfo.png");
	private Image buttonInfoImage = buttonInfoIcon.getImage() ;  
	private Image buttonInfoIconImage2 = buttonInfoImage.getScaledInstance( BUTTONSIZE, BUTTONSIZE,  java.awt.Image.SCALE_SMOOTH );  
	private ImageIcon buttonTransportIcon = new ImageIcon("src/simCity/gui/images/buttonTransport.png");
	private Image buttonTransportImage = buttonTransportIcon.getImage() ;  
	private Image buttonTransportIconImage2 = buttonTransportImage.getScaledInstance( BUTTONSIZE, BUTTONSIZE,  java.awt.Image.SCALE_SMOOTH );  
	private ImageIcon buttonPhoneIcon = new ImageIcon("src/simCity/gui/images/buttonPhone.png");
	private Image buttonPhoneImage = buttonPhoneIcon.getImage() ;  
	private Image buttonPhoneIconImage2 = buttonPhoneImage.getScaledInstance( BUTTONSIZE, BUTTONSIZE,  java.awt.Image.SCALE_SMOOTH );  
	
	public HouseControlPanel(HouseGui gui) {
	    this.gui = gui;
	    
	    setLayout(new GridLayout(4, 1, 0, 5));
	    setBackground(Color.WHITE);
	    
	    information = new JButton();
	    buttonInfoIcon = new ImageIcon( buttonInfoIconImage2 );
	    information.addActionListener(this);
	    information.setIcon(buttonInfoIcon);
	    information.setVisible(true);
	    information.setPreferredSize(new Dimension(50, 50));
	    add(information);
	    infoPane = new JOptionPane();
	    
	    transportation = new JButton();
	    transportation.addActionListener(this);
	    buttonTransportIcon = new ImageIcon(buttonTransportIconImage2);
	    transportation.setIcon(buttonTransportIcon);
	    transportation.setVisible(true);
	    transportation.setPreferredSize(new Dimension(50,50));
	    add(transportation);
	    transportationPane = new JOptionPane();
	    
	    phone = new JButton();
	    phone.addActionListener(this);
	    buttonPhoneIcon = new ImageIcon(buttonPhoneIconImage2);
	    phone.setIcon(buttonPhoneIcon);
	    phone.setVisible(true);
	    phone.setPreferredSize(new Dimension(50,50));
	    add(phone);
	    phonePane = new JOptionPane();
	    
	   // residentGui = new HouseResidentGui(hr, gui);
    	hr.setGui(residentGui);
    	gui.housePanel.addGui(residentGui);
    	
    	hr.msgGotHungry();
	
	}
	
	/**
	 * Action listener method that reacts to things being clicked
	 */
	/*public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == information){
	    	infoPane.showMessageDialog(this,
	    	    "Here is the information.",
	       	    "Person Information",
	       	    JOptionPane.PLAIN_MESSAGE);
	   	}
	    if (e.getSource() == transportation) {
	    	transportationPane.showMessageDialog(this,
	    		"Where would you like to go?",
	    		"Transportation",
	    		JOptionPane.PLAIN_MESSAGE);
	    }
	    if (e.getSource() == phone) {
	    	phonePane.showMessageDialog(this,
	    		"Who would you like to call?",
	    		"Telephone",
	    		JOptionPane.PLAIN_MESSAGE);
	    }
	}
	
	
	/**
	 * When a person is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information
	 * will be shown
	 *
	 * @param name name of person
	 */
	/*public void showInfo(String name) {
	    gui.updateInfoPanel(name);
	}
	
	/**
	 * Adds a person
	 * 
	 * @param name name of person
	 */
	/*public void addPerson(String name) {
	
		//PersonAgent p = new PersonAgent(name);
		//PersonGui g = new PersonGui(p, gui);
		//gui.animationPanel.addGui(g);
		//p.setGui(g);
		//p.startThread();
	
	}
}*/

