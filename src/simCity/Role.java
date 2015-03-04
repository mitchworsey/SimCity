package simCity;

import javax.swing.JPanel;

import simCity.interfaces.Person;

public abstract class Role {
	private int componentNumber = 1;
	private JPanel componentPanel;
	public Role() {
		componentNumber = 1;
	}
	public Person personAgent;
	public boolean active = false;
	public void setPersonAgent(Person p) {
		personAgent = p;
	}
	public Person getPersonAgent() {
		return personAgent;
	}
	protected abstract boolean pickAndExecuteAnAction();
	public void stateChanged() {
		personAgent.stateChanged();
	}
	public void print(String s) {
		personAgent.print(s);
	}
	public void setComponentNumber(int num) {
		componentNumber = num;
	}
	public int getComponentNumber() {
		return componentNumber;
	}
	public void setComponentPanel(JPanel panel) {
		componentPanel = panel;
	}
	public JPanel getComponentPanel() {
		return componentPanel;
	}
}


