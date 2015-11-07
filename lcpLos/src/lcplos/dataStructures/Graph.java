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

        int oldpercent = 0;
        for (int polyIndex = 0; polyIndex < this.nodelib.getNumOfPolys(); polyIndex++) {
            int percentDone = (int) ((double)polyIndex/this.nodelib.getNumOfPolys()*100);
            if(oldpercent != percentDone){
                System.out.println(percentDone + "%");
                oldpercent = percentDone;
            }
            
            ArrayList<Integer> nodes = this.nodelib.getNodes(polyIndex);
            
            
            
            if(nodes == null || nodes.isEmpty()){
                continue;
            }
            for (int startIndex = 0; startIndex < nodes.size(); startIndex++) {
                int startNode = nodes.get(startIndex);
                for (int targetIndex = 0; targetIndex < nodes.size(); targetIndex++) {
                    int targetNode = nodes.get(targetIndex);

                    Coords sc = this.nodelib.getCoordinates(startNode);
                    Coords tc = this.nodelib.getCoordinates(targetNode);

                    double distance = HelperFunctions.eucDist(sc, tc);
                    double cost = distance * frictionlib.getFriction(polyIndex);

                    if (this.al[startNode].containsKey(targetNode)) {
                        if (this.al[startNode].get(targetNode) < cost) {
                            continue;
                        }
                    }

                    if (4000 < HelperFunctions.eucDist(nodelib.getCoordinates(startNode), nodelib.getCoordinates(targetNode))) {
                        continue;
                    }
                    if (LosChecker.losBetweenNodes(nodelib.getOrientation(polyIndex),
                            startIndex, targetIndex, nodelib, polyIndex)) {
                        this.addEdge(startNode, targetNode, cost);

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

    private void addEdge(int node1, int node2, double cost) { //absolute node indexes
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
