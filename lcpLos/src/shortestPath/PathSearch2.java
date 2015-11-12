/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPath;

import java.util.ArrayList;
import logic.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import lcplos.dataStructures.Coords;

/**
 *
 * @author elias
 */
public class PathSearch2 {

    private Coords startnode;
    private Map<Coords, Double> toStart;
    private Map<Coords, Double> toEnd;
    private Map<Coords, Coords> pred;
    private Coords target;
    private PriorityQueue<Coords> minheap;
    private NeighbourFinder finder;

    public PathSearch2(Coords start, Coords end, NeighbourFinder finder) {
        this.finder = finder;
        double d = HelperFunctions.eucDist(start, end);

        this.startnode = start;
        this.target = end;

        this.toEnd = new HashMap<>();
        this.toStart = new HashMap<>();
        this.pred = new HashMap<>();

        this.toStart.put(this.startnode, 0.0);
        this.toEnd.put(this.startnode, this.cost(start, end));

        Comparator<Coords> smallestEstimate = new Comparator<Coords>() {
            @Override
            public int compare(Coords n1, Coords n2) {
                double d1;
                double d2;
                if (!toStart.containsKey(n1)) {
                    d1 = Double.MAX_VALUE;
                } else {
                    d1 = toStart.get(n1) + toEnd.get(n1);
                }
                if (!toStart.containsKey(n2)) {
                    d2 = Double.MAX_VALUE;
                } else {
                    d2 = toStart.get(n2) + toEnd.get(n2);
                }

                return (int) (d1 - d2);
            }
        };

        this.minheap = new PriorityQueue<>(20, smallestEstimate);
        this.minheap.add(startnode);
    }

    public boolean  aStar() {
        while (!this.minheap.isEmpty()) {
            Coords node = this.minheap.poll();
            System.out.println("node: " + node);
            // System.out.println(node.getCoords());
            // System.out.println(node);
            if (node.equals(this.target)) {
                return true;
            }
            
            Coords[] neighbourcoords = this.finder.getNeighbours(node);
            for (int i = 0; i < neighbourcoords.length; i++) {
                Coords nc = neighbourcoords[i];
                // System.out.println("neighbour: " + nc);
                if (!this.toStart.containsKey(nc)) {
                    this.toEnd.put(nc, this.cost(nc, this.target));
                    this.toStart.put(nc, this.toStart.get(node) + this.cost(node, nc));
                    this.pred.put(nc, node);
                    this.minheap.add(nc);
                } else {
                    if(this.relax(node, nc)){
                        this.minheap.remove(nc);
                        this.minheap.add(nc);
                    }
                }
            }
        }
        return false;
    }

    public Map<Coords, Coords> getPred() {
        return pred;
    }

    private boolean relax(Coords node, Coords nc) {
        if (this.toStart.get(node) + this.cost(node, nc) < this.toStart.get(nc)) {
            this.toStart.put(nc, this.toStart.get(node) + this.cost(node, nc));
            this.pred.put(nc, node);
            return true;
        }
        return false;
    }

    private double cost(Coords n1, Coords n2) {
        return HelperFunctions.eucDist(n1, n2);
    }

}
