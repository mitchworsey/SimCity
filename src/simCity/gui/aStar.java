package simCity.gui;

import java.util.*;


/*
 * This class find the best path, given a start and goal state, 
 * using the A* search algorithm
 * 
 * Written by Marina Hierl
 */

public class aStar {
        
        private int startX=0, startY=0, goalX=0, goalY=0;
        private List<Node> openList = Collections.synchronizedList(new ArrayList<Node>());
        private List<Node> closedList = Collections.synchronizedList(new ArrayList<Node>());
        private List<Node> successorList = Collections.synchronizedList(new ArrayList<Node>());
        private List<Node> solutionList = Collections.synchronizedList(new ArrayList<Node>());
        private Grid grid;
        
        static final int BLOCKSTARTX = 300/10;
        static final int BLOCKFINALX = 400/10;
        static final int BLOCKSTARTY = 200/10;
        static final int BLOCKFINALY = 400/10;
        
        public aStar(int sX, int sY, int gX, int gY, Grid g){
                startX = sX/10;
                startY = sY/10;
                goalX = gX/10;
                goalY = gY/10;
                grid = g;
                
                //Create node for start and goal positions
                Node goalNode = new Node(goalX, goalY);
                Node startNode = new Node(startX, startY);
                Node next;
                Node nextClosed;
                startNode.setH(startX, startY, goalX, goalY);
                Node currentNode = null;
                Node successorNodeN = null;
                Node successorNodeNW = null;
                Node successorNodeW = null;
                Node successorNodeSW = null;
                Node successorNodeS = null;
                Node successorNodeSE = null;
                Node successorNodeE = null;
                Node successorNodeNE = null;
                
                //Add start node to openList
                openList.add(startNode);
                
                //While openList is not empty
                while(!openList.isEmpty()){
                        
                        //Find node on openList with lowest F
                        for (int i = 0; i < openList.size(); i++) {
                                if (i == 0) 
                                        currentNode = openList.get(0);
                                else {
                                        if (currentNode.getF() > openList.get(i).getF())
                                                currentNode = openList.get(i);
                                }
                        }
                        openList.remove(currentNode);
                        
                        //Exit while loop if current node equals goal node
                        if (currentNode.getX() == goalNode.getX() && currentNode.getY() == goalNode.getY())
                                break;
                        
                        //Generate successor nodes to current node
                        successorNodeN = new Node(currentNode.getX(), currentNode.getY()+1);
                        successorNodeN.setG(1);
                        successorList.add(successorNodeN);
                        successorNodeNW = new Node(currentNode.getX()-1, currentNode.getY()+1);
                        successorNodeNW.setG(2);
                        successorList.add(successorNodeNW);
                        successorNodeW = new Node(currentNode.getX()-1, currentNode.getY());
                        successorNodeW.setG(1);
                        successorList.add(successorNodeW);
                        successorNodeSW = new Node(currentNode.getX()-1, currentNode.getY()-1);
                        successorNodeSW.setG(2);
                        successorList.add(successorNodeSW);
                        successorNodeS = new Node(currentNode.getX(), currentNode.getY()-1);
                        successorNodeS.setG(1);
                        successorList.add(successorNodeS);
                        successorNodeSE = new Node(currentNode.getX()+1, currentNode.getY()-1);
                        successorNodeSE.setG(2);
                        successorList.add(successorNodeSE);
                        successorNodeE = new Node(currentNode.getX()+1, currentNode.getY());
                        successorNodeE.setG(1);
                        successorList.add(successorNodeE);
                        successorNodeNE = new Node(currentNode.getX()+1, currentNode.getY()+1);
                        successorNodeNE.setG(2);
                        successorList.add(successorNodeNE);
                        synchronized(successorList){
                                for (int i = 0; i < successorList.size(); i++) {
                                    if (successorList.get(i).getX() < 100 && successorList.get(i).getX() > 0 && successorList.get(i).getY() < 70 && successorList.get(i).getY()> 0){  
                                        if (!g.acquire(successorList.get(i).getX(), successorList.get(i).getY())){
                                                successorList.remove(successorList.get(i));
                                                i -=1;
                                        }
                                    }
                                    else {
                                    	successorList.remove(successorList.get(i));
                                    	i-=1;
                                    }
                                }
                        }
                        //For each successor of the current node
                        synchronized(successorList){
                                for (Node node: successorList) {
                                        node.setH(node.getX(), node.getY(), goalX, goalY);
                                        boolean done = false;
                                        //Set cost of successorNode
                                        node.setG(currentNode.getG() + node.getG());
                                        //See if node is on open List
                                        for (int i = 0; i < openList.size(); i++) {
                                                if (openList.get(i).getX() == node.getX() && openList.get(i).getY() == node.getY()){
                                                        //If node on open list is better, discard successor
                                                        if (openList.get(i).getF() < node.getF()){
                                                                done = true;
                                                        	//openList.remove(openList.get(i));
                                                        }
                                                        //If successor is better, remove from open list
                                                        else {
                                                            openList.remove(openList.get(i));
                                                        	//done = true;
                                                        }
                                                }
                                        }
                                        //See if node is on closed list
                                        if (!closedList.isEmpty()){
                                                for (int i = 0; i < closedList.size(); i++) {
                                                        if (closedList.get(i).getX() == node.getX() && closedList.get(i).getY() == node.getY()) {
                                                                //If node on closed list is better, discard successor
                                                                if (closedList.get(i).getF() < node.getF()){
                                                                        done = true;
                                                                }
                                                                //If successor is better, remove from closed list
                                                                else {
                                                                        closedList.remove(closedList.get(i));
                                                                }
                                                        }
                                                }
                                        }
                                        //If the successor node was not just discarded
                                        if (!done) {
                                                next = new Node(node.getX(), node.getY());
                                                next.setH(node.getX(), node.getY(), goalX, goalY);
                                                next.setPreviousNode(currentNode);
                                                openList.add(next);
                                                //Add to solution list if it is the goal
                                                if (next.getX() == goalX && next.getY() == goalY) 
                                                        solutionList.add(next);
                                        }
                                }
                                successorList.clear();
                                //openList.remove(currentNode);
                                for (int i = 0; i < openList.size(); i++){
                                        if (openList.get(i).getX() == currentNode.getX() && openList.get(i).getY() == currentNode.getY()){
                                                openList.remove(openList.get(i));
                                                i -= 1;
                                        }
                                }
                                nextClosed = new Node(currentNode.getX(), currentNode.getY());
                                closedList.add(nextClosed);
                        }
                }
        }
        
        public aStar(int sX, int sY, int gX, int gY, Grid g, int blockX, int blockY){
            startX = sX/10;
            startY = sY/10;
            goalX = gX/10;
            goalY = gY/10;
            grid = g;
            
            //Create node for start and goal positions
            Node goalNode = new Node(goalX, goalY);
            Node startNode = new Node(startX, startY);
            Node next;
            Node nextClosed;
            startNode.setH(startX, startY, goalX, goalY);
            Node currentNode = null;
            Node successorNodeN = null;
            Node successorNodeNW = null;
            Node successorNodeW = null;
            Node successorNodeSW = null;
            Node successorNodeS = null;
            Node successorNodeSE = null;
            Node successorNodeE = null;
            Node successorNodeNE = null;
            
            //Add start node to openList
            openList.add(startNode);
            
            //While openList is not empty
            while(!openList.isEmpty()){
                    
                    //Find node on openList with lowest F
                    for (int i = 0; i < openList.size(); i++) {
                            if (i == 0) 
                                    currentNode = openList.get(0);
                            else {
                                    if (currentNode.getF() > openList.get(i).getF())
                                            currentNode = openList.get(i);
                            }
                    }
                    openList.remove(currentNode);
                    
                    //Exit while loop if current node equals goal node
                    if (currentNode.getX() == goalNode.getX() && currentNode.getY() == goalNode.getY())
                            break;
                    
                    //Generate successor nodes to current node
                    successorNodeN = new Node(currentNode.getX(), currentNode.getY()+1);
                    successorNodeN.setG(1);
                    successorList.add(successorNodeN);
                    successorNodeNW = new Node(currentNode.getX()-1, currentNode.getY()+1);
                    successorNodeNW.setG(2);
                    successorList.add(successorNodeNW);
                    successorNodeW = new Node(currentNode.getX()-1, currentNode.getY());
                    successorNodeW.setG(1);
                    successorList.add(successorNodeW);
                    successorNodeSW = new Node(currentNode.getX()-1, currentNode.getY()-1);
                    successorNodeSW.setG(2);
                    successorList.add(successorNodeSW);
                    successorNodeS = new Node(currentNode.getX(), currentNode.getY()-1);
                    successorNodeS.setG(1);
                    successorList.add(successorNodeS);
                    successorNodeSE = new Node(currentNode.getX()+1, currentNode.getY()-1);
                    successorNodeSE.setG(2);
                    successorList.add(successorNodeSE);
                    successorNodeE = new Node(currentNode.getX()+1, currentNode.getY());
                    successorNodeE.setG(1);
                    successorList.add(successorNodeE);
                    successorNodeNE = new Node(currentNode.getX()+1, currentNode.getY()+1);
                    successorNodeNE.setG(2);
                    successorList.add(successorNodeNE);
                    synchronized(successorList){
                            for (int i = 0; i < successorList.size(); i++) {
                                if (successorList.get(i).getX() < 100 && successorList.get(i).getX() > 0 && successorList.get(i).getY() < 70 && successorList.get(i).getY()> 0){  
                                    if (!g.acquire(successorList.get(i).getX(), successorList.get(i).getY())){
                                            successorList.remove(successorList.get(i));
                                            i -=1;
                                    }
                                    else if (successorList.get(i).getX() >= blockX/10 && successorList.get(i).getX() < blockX/10+1
                                    	&& successorList.get(i).getY() >= blockY/10 && successorList.get(i).getY() < blockY/10 +1){
                                    		successorList.remove(successorList.get(i));
                                    		i -=1;
                                    	}
                                }
                                else {
                                	successorList.remove(successorList.get(i));
                                	i-=1;
                                }
                            }
                    }
                    //For each successor of the current node
                    synchronized(successorList){
                            for (Node node: successorList) {
                                    node.setH(node.getX(), node.getY(), goalX, goalY);
                                    boolean done = false;
                                    //Set cost of successorNode
                                    node.setG(currentNode.getG() + node.getG());
                                    //See if node is on open List
                                    for (int i = 0; i < openList.size(); i++) {
                                            if (openList.get(i).getX() == node.getX() && openList.get(i).getY() == node.getY()){
                                                    //If node on open list is better, discard successor
                                                    if (openList.get(i).getF() < node.getF()){
                                                            done = true;
                                                    	//openList.remove(openList.get(i));
                                                    }
                                                    //If successor is better, remove from open list
                                                    else {
                                                        openList.remove(openList.get(i));
                                                    	//done = true;
                                                    }
                                            }
                                    }
                                    //See if node is on closed list
                                    if (!closedList.isEmpty()){
                                            for (int i = 0; i < closedList.size(); i++) {
                                                    if (closedList.get(i).getX() == node.getX() && closedList.get(i).getY() == node.getY()) {
                                                            //If node on closed list is better, discard successor
                                                            if (closedList.get(i).getF() < node.getF()){
                                                                    done = true;
                                                            }
                                                            //If successor is better, remove from closed list
                                                            else {
                                                                    closedList.remove(closedList.get(i));
                                                            }
                                                    }
                                            }
                                    }
                                    //If the successor node was not just discarded
                                    if (!done) {
                                            next = new Node(node.getX(), node.getY());
                                            next.setH(node.getX(), node.getY(), goalX, goalY);
                                            next.setPreviousNode(currentNode);
                                            openList.add(next);
                                            //Add to solution list if it is the goal
                                            if (next.getX() == goalX && next.getY() == goalY) 
                                                    solutionList.add(next);
                                    }
                            }
                            successorList.clear();
                            //openList.remove(currentNode);
                            for (int i = 0; i < openList.size(); i++){
                                    if (openList.get(i).getX() == currentNode.getX() && openList.get(i).getY() == currentNode.getY()){
                                            openList.remove(openList.get(i));
                                            i -= 1;
                                    }
                            }
                            nextClosed = new Node(currentNode.getX(), currentNode.getY());
                            closedList.add(nextClosed);
                    }
            }
    }
        
        public List<Node> getBestPathSteps() {
                int temp = 0;
                Node best = null;
                Node tempNode1, tempNode2, tempRepNode1, tempRepNode2, tempParentNode, tempBest;
                List<Node> bestList = Collections.synchronizedList(new ArrayList<Node>());
                for (int i = 0; i < solutionList.size(); i++) {
                        if (i == 0)
                                best = solutionList.get(0);
                        else {
                                if (best.getF() > solutionList.get(i).getF())
                                        best = solutionList.get(i);
                        }
                }
                if (best == null){
                	//aStar again = new aStar(startX*10, startY*10, goalX*10, goalY*10);
                	//return again.getBestPath();
                	return null;
                }
                tempBest = new Node(best.getX()*10, best.getY()*10);
                bestList.add(tempBest);
                while (best.getPreviousNode() != null){
                        tempParentNode = best.getPreviousNode();
                        tempNode1 = new Node(0,0);
                        best.setPreviousNode(tempNode1);
                        if (temp == 0){
                                if (best.getX() > tempParentNode.getX()) {
                                        tempNode1.setX(best.getX()*10-1);
                                }
                                else if (best.getX() < tempParentNode.getX()) {
                                        tempNode1.setX(best.getX()*10+1);
                                }
                                else {
                                        tempNode1.setX(best.getX()*10);
                                }
                                if (best.getY() > tempParentNode.getY()) {
                                        tempNode1.setY(best.getY()*10-1);
                                }
                                else if (best.getY() < tempParentNode.getY()) {
                                        tempNode1.setY(best.getY()*10+1);
                                }
                                else {
                                        tempNode1.setY(best.getY()*10);
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode1);
                        //while (temp < 9){
                        tempNode2 = new Node(0,0);
                        tempNode1.setPreviousNode(tempNode2);
                        if (temp == 1){
                        //if (temp%2 == 1){
                                if (tempNode1.getX() > tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()-1);
                                }
                                else if (tempNode1.getX() < tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()+1);
                                }
                                else {
                                        tempNode2.setX(tempNode1.getX());
                                }
                                if (tempNode1.getY() > tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()-1);
                                }
                                else if (tempNode1.getY() < tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()+1);
                                }
                                else {
                                        tempNode2.setY(tempNode1.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode2);
                        tempNode1 = new Node(0,0);
                        tempNode2.setPreviousNode(tempNode1);
                        if (temp == 2){
                        //if (temp%2 == 1){
                                if (tempNode2.getX() > tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()-1);
                                }
                                else if (tempNode2.getX() < tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()+1);
                                }
                                else {
                                        tempNode1.setX(tempNode2.getX());
                                }
                                if (tempNode2.getY() > tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()-1);
                                }
                                else if (tempNode2.getY() < tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()+1);
                                }
                                else {
                                        tempNode1.setY(tempNode2.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode1);
                        tempNode2 = new Node(0,0);
                        tempNode1.setPreviousNode(tempNode2);
                        if (temp == 3){
                                if (tempNode1.getX() > tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()-1);
                                }
                                else if (tempNode1.getX() < tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()+1);
                                }
                                else {
                                        tempNode2.setX(tempNode1.getX());
                                }
                                if (tempNode1.getY() > tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()-1);
                                }
                                else if (tempNode1.getY() < tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()+1);
                                }
                                else {
                                        tempNode2.setY(tempNode1.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode2);
                        tempNode1 = new Node(0,0);
                        tempNode2.setPreviousNode(tempNode1);
                        if (temp == 4){
                                if (tempNode2.getX() > tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()-1);
                                }
                                else if (tempNode2.getX() < tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()+1);
                                }
                                else {
                                        tempNode1.setX(tempNode2.getX());
                                }
                                if (tempNode2.getY() > tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()-1);
                                }
                                else if (tempNode2.getY() < tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()+1);
                                }
                                else {
                                        tempNode1.setY(tempNode2.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode1);
                        tempNode2 = new Node(0,0);
                        tempNode1.setPreviousNode(tempNode2);
                        if (temp == 5){
                                if (tempNode1.getX() > tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()-1);
                                }
                                else if (tempNode1.getX() < tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()+1);
                                }
                                else {
                                        tempNode2.setX(tempNode1.getX());
                                }
                                if (tempNode1.getY() > tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()-1);
                                }
                                else if (tempNode1.getY() < tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()+1);
                                }
                                else {
                                        tempNode2.setY(tempNode1.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode2);
                        tempNode1 = new Node(0,0);
                        tempNode2.setPreviousNode(tempNode1);
                        if (temp == 6){
                                if (tempNode2.getX() > tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()-1);
                                }
                                else if (tempNode2.getX() < tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()+1);
                                }
                                else {
                                        tempNode1.setX(tempNode2.getX());
                                }
                                if (tempNode2.getY() > tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()-1);
                                }
                                else if (tempNode2.getY() < tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()+1);
                                }
                                else {
                                        tempNode1.setY(tempNode2.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode1);
                        tempNode2 = new Node(0,0);
                        tempNode1.setPreviousNode(tempNode2);
                        if (temp == 7){
                                if (tempNode1.getX() > tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()-1);
                                }
                                else if (tempNode1.getX() < tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()+1);
                                }
                                else {
                                        tempNode2.setX(tempNode1.getX());
                                }
                                if (tempNode1.getY() > tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()-1);
                                }
                                else if (tempNode1.getY() < tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()+1);
                                }
                                else {
                                        tempNode2.setY(tempNode1.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode2);
                        tempNode1 = new Node(0,0);
                        tempNode2.setPreviousNode(tempNode1);
                        if (temp == 8){
                                if (tempNode2.getX() > tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()-1);
                                }
                                else if (tempNode2.getX() < tempParentNode.getX()*10) {
                                        tempNode1.setX(tempNode2.getX()+1);
                                }
                                else {
                                        tempNode1.setX(tempNode2.getX());
                                }
                                if (tempNode2.getY() > tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()-1);
                                }
                                else if (tempNode2.getY() < tempParentNode.getY()*10) {
                                        tempNode1.setY(tempNode2.getY()+1);
                                }
                                else {
                                        tempNode1.setY(tempNode2.getY());
                                }
                                temp +=1;
                        }
                        bestList.add(tempNode1);
                        tempNode2 = new Node(0,0);
                        tempNode1.setPreviousNode(tempNode2);
                        if (temp == 9){
                                if (tempNode1.getX() > tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()-1);
                                }
                                else if (tempNode1.getX() < tempParentNode.getX()*10) {
                                        tempNode2.setX(tempNode1.getX()+1);
                                }
                                else {
                                        tempNode2.setX(tempNode1.getX());
                                }
                                if (tempNode1.getY() > tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()-1);
                                }
                                else if (tempNode1.getY() < tempParentNode.getY()*10) {
                                        tempNode2.setY(tempNode1.getY()+1);
                                }
                                else {
                                        tempNode2.setY(tempNode1.getY());
                                }
                                temp = 0;
                        }
                        bestList.add(tempNode2);
                        best = tempParentNode;
                        tempBest = new Node(best.getX()*10, best.getY()*10);
                        tempNode2.setPreviousNode(tempBest);
                        bestList.add(tempBest);
                }
                return bestList;
        }
        
        public List<Node> getBestPath() {
            int temp = 0;
            Node best = null;
            List<Node> bestList = Collections.synchronizedList(new ArrayList<Node>());
            for (int i = 0; i < solutionList.size(); i++) {
                    if (i == 0)
                            best = solutionList.get(0);
                    else {
                            if (best.getF() > solutionList.get(i).getF())
                                    best = solutionList.get(i);
                    }
            }
            if (best == null){
            	return null;
            }
            while (best != null){
            	bestList.add(best);
            	best = best.getPreviousNode();
            }
            return bestList;
        }
}