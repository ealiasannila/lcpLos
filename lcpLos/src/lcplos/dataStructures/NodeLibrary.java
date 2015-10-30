/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import logic.HelperFunctions;
import logic.LosChecker;

/**
 *
 * @author elias
 */
public class NodeLibrary {

    private ArrayList<ArrayList<Integer>> nodeToPolygons;
    private ArrayList<Integer>[] polygonToNodes;
    private HashMap<Coordinates, Integer> coordinatesToNode;
    private ArrayList<Coordinates> coordinates;
    private int[] polyOrientations;

    public NodeLibrary(int numOfPolygons) {
        this.nodeToPolygons = new ArrayList<ArrayList<Integer>>();
        this.polygonToNodes = new ArrayList[numOfPolygons];
        this.polyOrientations =new int[numOfPolygons];
        this.coordinatesToNode = new HashMap<>();
        this.coordinates = new ArrayList<Coordinates>();
    }

    public ArrayList<Integer> getNodes(int poly) {
        return this.polygonToNodes[poly];
    }

    public ArrayList<Integer>[] getPolygons() {
        return this.polygonToNodes;
    }

    public ArrayList<Integer> getPolygons(int node) {
        return this.nodeToPolygons.get(node);
    }

    public int getPolyOrientation(int poly){
        return this.polyOrientations[poly];
    }
    
    public void addPolygon(ArrayList<Coordinates> nodes, int poly) {
        for (Coordinates xy : nodes) {
            this.addNode(xy, poly);
        }
        this.polyOrientations[poly] = LosChecker.polyOrientation(poly, this);
    }

    public void addNode(Coordinates xy, int poly) {
        if (!this.coordinatesToNode.containsKey(xy)) {
            this.addNewNode(xy);
        }

        int node = this.coordinatesToNode.get(xy);
        
        if(nodeToPolygons.get(node).contains(poly)){
            return;
        }
        
        this.nodeToPolygons.get(node).add(poly);
        //System.out.println(poly);
        if (this.polygonToNodes[poly] == null) {
            this.polygonToNodes[poly] = new ArrayList<Integer>();

        }
        this.polygonToNodes[poly].add(node);

    }

    private void addNewNode(Coordinates xy) {
        this.coordinates.add(xy);
        this.nodeToPolygons.add(new ArrayList<Integer>());
        this.coordinatesToNode.put(xy, this.coordinates.size() - 1);
    }

    public Coordinates getCoordinates(int node) {
        return this.coordinates.get(node);
    }

    public int getNumOfNodes() {
        return this.nodeToPolygons.size();
    }

    public int getNumOfPolys() {
        return this.polygonToNodes.length;
    }
    public int getNearestNode(Coordinates xy){
        double d = Double.MAX_VALUE;
        int node = -1;
        for (int i = 0; i < this.nodeToPolygons.size(); i++) {
            if(HelperFunctions.eucDist(this.getCoordinates(i), xy)<d){
                d= HelperFunctions.eucDist(this.getCoordinates(i), xy);
                node = i;
            }
        }
        return node;
    }
    
    

}
