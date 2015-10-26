/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import logic.HelperFunctions;
import logic.Polygon;

/**
 *
 * @author elias
 */
public class Graph {

    private NodeLibrary nodelib;
    private Map<Integer, Double>[] al; //adjacency list
        
    public Graph(NodeLibrary nodelib, FrictionLibrary frictionlib) {
        int numOfNodes = nodelib.getNumOfNodes();

        this.nodelib = nodelib;
        this.al = new TreeMap[numOfNodes];

        for (int i = 0; i < al.length; i++) {
            al[i] = new TreeMap<>();
        }

        for (int node = 0; node < nodelib.getNumOfNodes(); node++) {
            //System.out.println("node: " + node);
            ArrayList<Integer> polygons = nodelib.getPolygons(node);
            for (int polyIndex : polygons) {
                Polygon polygon = new Polygon(nodelib, polyIndex);
                for (int targetIndex = 0; targetIndex < polygon.getNumOfNodes(); targetIndex++) {
                    int targetNode = polygon.losBetweenNodes(node, targetIndex);
                    if (targetNode != -1) {
                        this.addEdge(node, targetNode, frictionlib.getFriction(polyIndex));
                    }
                }
            }

        }

    }

    private void addEdge(int node1, int node2, double friction) { //absolute node indexes

        Coordinates n1c = this.nodelib.getCoordinates(node1);
        Coordinates n2c = this.nodelib.getCoordinates(node2);

        double distance = HelperFunctions.eucDist(n1c.getX(), n1c.getY(), n2c.getX(), n2c.getY());
        double cost = distance * friction;

        if(this.al[node1].containsKey(node2)){
            if(this.al[node1].get(node2)<cost){
                return;
            }
        }
        this.al[node1].put(node2, cost);

    }

    public Map<Integer, Double> getNeighbours(int node) {
        return this.al[node];
    }

    
    public int getNumOfNodes(){
        return this.getNodelib().getNumOfNodes();
    }

    public NodeLibrary getNodelib() {
        return nodelib;
    }

}
