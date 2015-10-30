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
import logic.LosChecker;

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

        for (int polyIndex = 0; polyIndex < this.nodelib.getNumOfPolys(); polyIndex++) {

            ArrayList<Integer> nodes = this.nodelib.getNodes(polyIndex);
            for (int startIndex = 0; startIndex < nodes.size(); startIndex++) {
                int startNode = nodes.get(startIndex);
                for (int targetIndex = 0; targetIndex < nodes.size(); targetIndex++) {
                    int targetNode = nodes.get(targetIndex);
                    
                    if (4000 < HelperFunctions.eucDist(nodelib.getCoordinates(startNode), nodelib.getCoordinates(targetNode))) {
                        continue;
                    }
                    if (LosChecker.losBetweenNodes(nodelib.getOrientation(polyIndex),
                            startIndex, targetIndex, nodelib, polyIndex)) {
                        this.addEdge(startNode, targetNode, frictionlib.getFriction(polyIndex));

                    }
                    
                }
            }
        }
    }

    public ArrayList<Double> getFrictions(ArrayList<Integer> nodes) {
        ArrayList<Double> frictions = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            frictions.add(this.getFriction(nodes.get(i), nodes.get(i + 1)));
        }
        return frictions;
    }

    public double getFriction(int node1, int node2) {
        double d = HelperFunctions.eucDist(this.nodelib.getCoordinates(node1), this.nodelib.getCoordinates(node2));

        return this.al[node1].get(node2) / d;
    }

    private void addEdge(int node1, int node2, double friction) { //absolute node indexes
        System.out.println(node1);
        
        Coords n1c = this.nodelib.getCoordinates(node1);
        Coords n2c = this.nodelib.getCoordinates(node2);

        double distance = HelperFunctions.eucDist(n1c, n2c);
        double cost = distance * friction;

        if (this.al[node1].containsKey(node2)) {
            if (this.al[node1].get(node2) < cost) {
                return;
            }
        }

        this.al[node1].put(node2, cost);
        this.al[node2].put(node1, cost);

    }

    public Map<Integer, Double> getNeighbours(int node) {
        return this.al[node];
    }

    public int getNumOfNodes() {
        return this.getNodelib().getNumOfNodes();
    }

    public NodeLibrary getNodelib() {
        return nodelib;
    }

}
