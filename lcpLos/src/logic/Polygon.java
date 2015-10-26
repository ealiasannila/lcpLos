/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.NodeLibrary;

/**
 *
 * @author elias
 */
public class Polygon {

    private ArrayList<Integer> nodes;
    private NodeLibrary nodelib;

    public Polygon(NodeLibrary nodelib, int polygon) {
        this.nodelib = nodelib;
        this.nodes = this.nodelib.getNodes(polygon);
    }

    
    public int losBetweenNodes(int startNode, int targetIndex) { //target is index within polygon, start absolute node index
        if(this.nodes.get(targetIndex)==startNode){
            return -1;
        }
        
        for (int edgenode = 0; edgenode < nodes.size()-1; edgenode++) {
            if (edgesIntersect(edgenode, startNode, targetIndex)) {
                return -1;
            }
        }
        return this.nodes.get(targetIndex);

    }

    private boolean edgesIntersect(int ed1, int startNode, int targetNodeIndex) {
        int ed2 = ed1 + 1;
        
        if (this.nodes.get(ed1) == startNode || ed1 == targetNodeIndex || this.nodes.get(ed2) == startNode || ed2 == targetNodeIndex) {
            return false;
        }

        Coordinates ic = this.nodelib.getCoordinates(this.nodes.get(ed1));
        Coordinates jc = this.nodelib.getCoordinates(this.nodes.get(ed2));
        Coordinates n1c = this.nodelib.getCoordinates(startNode);
        Coordinates n2c = this.nodelib.getCoordinates(this.nodes.get(targetNodeIndex));

        int ijn1 = directionOfRotation(ic.getX(), ic.getY(), jc.getX(), jc.getY(), n1c.getX(), n1c.getY());
        int ijn2 = directionOfRotation(ic.getX(), ic.getY(), jc.getX(), jc.getY(), n2c.getX(), n2c.getY());
        int n1n2i = directionOfRotation(n1c.getX(), n1c.getY(), n2c.getX(), n2c.getY(), ic.getX(), ic.getY());
        int n1n2j = directionOfRotation(n1c.getX(), n1c.getY(), n2c.getX(), n2c.getY(), jc.getX(), jc.getY());

        //edge cuts los
        if (ijn1 != ijn2 && n1n2i != n1n2j) {
            return true;
        }

        //edge is same direction as los and on los
        if (ijn1 == 0 && this.pointOnSameDirEdge(n1c.getX(), n1c.getY(), ic.getX(), ic.getY(), jc.getX(), jc.getY())) {
            return true;
        }

        if (ijn2 == 0 && this.pointOnSameDirEdge(n2c.getX(), n2c.getY(), ic.getX(), ic.getY(), jc.getX(), jc.getY())) {
            return true;
        }
        if (n1n2i == 0 && this.pointOnSameDirEdge(ic.getX(), ic.getY(), n1c.getX(), n1c.getY(), n2c.getX(), n2c.getY())) {
            return true;
        }
        if (n1n2j == 0 && this.pointOnSameDirEdge(jc.getX(), jc.getY(), n1c.getX(), n1c.getY(), n2c.getX(), n2c.getY())) {
            return true;
        }

        return false;

    }

    private boolean pointOnSameDirEdge(double xp, double yp, double xe1, double ye1, double xe2, double ye2) {

        if (yp <= Math.max(ye1, ye2) && yp >= Math.min(ye1, ye2)
                && xp <= Math.max(xe1, xe2) && xp >= Math.min(xe1, xe2)) {
            return true;
        }

        return false;
    }

    /**
     * kertoo onko kolmen pisteen kiertosuunta myötä (-1) vai vastapäivään(1),
     * vai ovatko ne samalla viivalla(0)
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    private int directionOfRotation(double x1, double y1, double x2, double y2, double x3, double y3) {
        double difference = (x2 - x1) * (y3 - y2) - (x3 - x2) * (y2 - y1);
        if (difference < -0.00001) {
            return -1;
        }
        if (difference > 0.00001) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public int getNumOfNodes(){
        return this.nodes.size();
    }
}
