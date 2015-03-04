package simCity.Restaurant1;

import java.util.*;
import java.util.Vector;

import simCity.restaurant2.Restaurant2CookRole.Order;


public class Restaurant1SharedDataWheel extends Object {
	
	private final int N = 5;
    private int count = 0;
    private Vector<Order> theData;
    
    public Restaurant1SharedDataWheel(){
        theData = new Vector<Order>();
    }
    
    synchronized public void insert(simCity.Restaurant1.Restaurant1WaiterRole.Order o) {
        while (count == N) {
            try{ 
                System.out.println("\tFull, waiting");
                wait(5000);                         // Full, wait to add
            } catch (InterruptedException ex) {};
        }
            
        insert(o);
        count++;
        if(count == 1) {
            System.out.println("\tNot Empty, notify");
            notify();                               // Not empty, notify a 
                                                    // waiting consumer
        }
    }
    
    synchronized public Order remove() {
        Order data;
        while(count == 0)
            try{ 
                System.out.println("\tEmpty, waiting");
                wait(5000);                         // Empty, wait to consume
            } catch (InterruptedException ex) {};
 
        data = remove_Order();
        count--;
        if(count == N-1){ 
            System.out.println("\tNot full, notify");
            notify();                               // Not full, notify a 
                                                    // waiting producer
        }
        return data;
    }
    
    synchronized public List<Order> removeAll() {
    	List<Order> dataList = new ArrayList<Order>();
    	Order data;
        while(count == 0)
            try{ 
                System.out.println("\tEmpty, waiting");
                wait(5000);                         // Empty, wait to consume
            } catch (InterruptedException ex) {};
            
        while(count != 0) {
	        data = remove_Order();
	        dataList.add(data);
	        count--;
	        if(count == N-1){ 
	            System.out.println("\tNot full, notify");
	            notify();                               // Not full, notify a 
	                                                    // waiting producer
	        }
        }
        return dataList;
    }
    
    private void insert_Order(Order o){
        theData.addElement(o);
    }
    
    private Order remove_Order(){
        Order data = (Order) theData.firstElement();
        theData.removeElementAt(0);
        return data;
    }

}
