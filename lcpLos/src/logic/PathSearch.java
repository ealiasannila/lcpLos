/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.Graph;
import lcplos.dataStructures.Minheap;

/**
 *
 * @author elias
 */
public class PathSearch {

    private double[] toStart;
    private double[] toEnd;
    private int[] path;

    private Graph graph;

    private int startnode;
    private int targetnode;

    public PathSearch(Graph graph, int startnode, int targetnode) {
        this.graph = graph;
        this.startnode = startnode;
        this.targetnode = targetnode;

        this.toEnd = new double[graph.getNumOfNodes()];
        this.toStart = new double[graph.getNumOfNodes()];
        this.path = new int[graph.getNumOfNodes()];

        for (int i = 0; i < graph.getNumOfNodes(); i++) {
            this.toStart[i] = Double.MAX_VALUE;
            this.path[i] = -1;
            this.toEnd[i] = this.estimateCost(i);
        }

        this.toStart[this.startnode] = 0;

    }

    private double estimateCost(int node) {
        Coordinates targetc = this.graph.getNodelib().getCoordinates(this.targetnode);
        Coordinates nodec = this.graph.getNodelib().getCoordinates(node);
        return HelperFunctions.eucDist(nodec.getX(), nodec.getY(), targetc.getX(), targetc.getY())*0.000001; //0.000001 = minimum friction
    }

    private boolean relax(int node, int neighbour, double cost) {
        if (this.toStart[node] == Double.MAX_VALUE) {
            return false;
        }

        if (this.toStart[neighbour] > this.toStart[node] + cost) {
            this.toStart[neighbour] = this.toStart[node] + cost;
            this.path[neighbour] = node;
            return true;
        }

        return false;
    }

    /**
     * laskee lyhyimmät etäisyydet maalisolmuun lähtien lähtösolmusta.
     * Heuristiikkafunktiona suora etäisyys * minimivauhti, lopettaa etsinnän
     * kun reitti löytyy.
     *
     * @param lahtoSolmu
     * @return
     */
    public void aStar() {

        Minheap heap = new Minheap(this.toStart.length) {
            
            @Override
            public double estimate(int i) {
                if (toStart[i] == Double.MAX_VALUE) {
                    return Double.MAX_VALUE;
                }
                return toStart[i] + toEnd[i]; 
            }
        };

        for (int i = 0; i < this.toStart.length; i++) {
            heap.add(i);
        }

        while (!heap.empty()) {
            int node = heap.takeSmallest();
            if (node == this.targetnode) {
                return;
            }
            Map<Integer, Double> neighbours = this.graph.getNeighbours(node);
            Set<Integer> neighbourset = neighbours.keySet();
            for (int neighbour : neighbourset) {
                if (relax(node, neighbour, neighbours.get(neighbour))) {
                    heap.update(neighbour);
                }
            }
        }
    }
    
    
    public ArrayList<Integer> shortestPath() {
        
        if (this.startnode == this.targetnode) {
            return null;
        }
        Stack<Integer> stack = new Stack();
        int next = this.path[this.targetnode];
        
        if(next == -1){
            return null;
        }

        while (next != startnode) {
            stack.add(next);
            next = this.path[next];
        }

        ArrayList<Integer> lcp = new ArrayList<Integer>();
        
        lcp.add(this.startnode);
        
        while (!stack.empty()) {
            int node = stack.pop();
            lcp.add(node);
           
        }
        lcp.add(this.targetnode);
        return lcp;

    }


}
