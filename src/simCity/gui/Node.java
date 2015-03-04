package simCity.gui;


public class Node {
        protected int x;
        protected int y;
        
        private Node previousNode;
        private int g = 0;
        private int h;
                
        Node(int X, int Y){
                x = X;
                y = Y;
        }
                
        void setPreviousNode(Node pn) {
                previousNode = pn;
        }
        
        Node getPreviousNode() {
                return previousNode;
        }
                
        void setG(int G){
                g = G;
        }
        
        void setX(int X){
                x = X;
        }
        
        void setY(int Y){
                y = Y;
        }
                
        void setH(int sX, int sY, int gX, int gY){
                int xDistance = Math.abs(sX - gX);
                int yDistance = Math.abs(sY - gY);
                if (xDistance > yDistance){
                        h = 14*yDistance + 10*(xDistance-yDistance);
                }
                else {
                        h = 14*xDistance + 10*(yDistance-xDistance);
                }
        }
                
        int getG() {
                return g;
        }
        
        int getF() {
                return (h + g);
        }
        
        public int getX() {
                return x;
        }
        
        public int getY() {
                return y;
        }
}