/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author elias
 */
public class NodeLibrary {

    private ArrayList<Integer>[] nodeToPolygons;
    private ArrayList<Integer>[] polygonToNodes;
    private HashMap<Coordinates, Integer> coordinatesToNode;
    private int nodeCounter;
    private int polycounter;

    public NodeLibrary(int numOfNodes, int numOfPolygons) {
        this.nodeToPolygons = new ArrayList[numOfNodes];
        this.polygonToNodes = new ArrayList[numOfPolygons];
        this.coordinatesToNode = new HashMap<>();
        this.nodeCounter = -1;
    }

    public ArrayList<Integer> getNodes(int poly) {
        return this.polygonToNodes[poly];
    }

    public ArrayList<Integer> getPolygons(int node) {
        return this.nodeToPolygons[node];
    }

    public void addNode(Coordinates xy, int poly) {
        if(!this.coordinatesToNode.containsKey(xy)){
            this.addNewNode(xy, poly);
        }
        int node = this.coordinatesToNode.get(xy);
        this.nodeToPolygons[node].add(poly);
        this.polygonToNodes[poly].add(node);
        
    }

    private void addNewNode(Coordinates xy, int poly){
        this.nodeCounter++;
        this.coordinatesToNode.put(xy, this.nodeCounter);
        this.nodeToPolygons[this.nodeCounter] = new ArrayList<Integer>();        
        if(this.polygonToNodes[poly]==null){
            this.polygonToNodes[poly] = new ArrayList<Integer>();
        }
    }
    
}
