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
import lcplos.dataStructures.VertexLib;

/**
 *
 * @author elias
 */
public class PathSearch2 {

    private int startnode;
    private Map<Integer, Double> toStart;
    private Map<Integer, Double> toEnd;
    private Map<Integer, Integer> pred;
    private int target;
    private PriorityQueue<Integer> minheap;
    private NeighbourFinder finder;
    private VertexLib vlib;

    public PathSearch2(int start, int end, NeighbourFinder finder, VertexLib vlib) {
        this.finder = finder;
        this.vlib = vlib;

        this.startnode = start;
        this.target = end;

        this.toEnd = new HashMap<>();
        this.toStart = new HashMap<>();
        this.pred = new HashMap<>();

        this.toStart.put(this.startnode, 0.0);
        this.toEnd.put(this.startnode, this.estimate(start, end));

        Comparator<Integer> smallestEstimate = new Comparator<Integer>() {
            @Override
            public int compare(Integer n1, Integer n2) {
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

    public boolean aStar() {
        while (!this.minheap.isEmpty()) {
            int node = this.minheap.poll();
            //System.out.println("node: " + node);
            // System.out.println(node.getCoords());
            // System.out.println(node);
            if (node == this.target) {
                return true;
            }

            Map<Integer, Set<Integer>> neighbours = this.finder.getNeighbours(node);
            for (int polygon : neighbours.keySet()) {
                Set<Integer> neighboursInPolygon = neighbours.get(polygon);
                double friction = this.vlib.getFriction(polygon);
                for (int neighbour : neighboursInPolygon) {
                    // System.out.println("neighbour: " + nc);
                    if (!this.toStart.containsKey(neighbour)) {
                        this.toEnd.put(neighbour, this.estimate(neighbour, this.target));
                        this.toStart.put(neighbour, this.toStart.get(node) + this.cost(node, neighbour, friction));
                        this.pred.put(neighbour, node);
                        this.minheap.add(neighbour);
                    } else {
                        if (this.relax(node, neighbour, friction)) {
                            this.minheap.remove(neighbour);
                            this.minheap.add(neighbour);
                        }
                    }
                }

            }
        }
        return false;
    }

    public Map<Integer, Integer> getPred() {
        return pred;
    }

    private boolean relax(int node, int neighbour, double cost) {
        if (this.toStart.get(node) + this.cost(node, neighbour, cost) < this.toStart.get(neighbour)) {
            this.toStart.put(neighbour, this.toStart.get(node) + this.cost(node, neighbour, cost));
            this.pred.put(neighbour, node);
            return true;
        }
        return false;
    }

    private double estimate(int n1, int n2) {
        return HelperFunctions.eucDist(this.vlib.getCoords(n1), this.vlib.getCoords(n2));
    }

    private double cost(int n1, int n2, double friction) {
        return HelperFunctions.eucDist(this.vlib.getCoords(n1), this.vlib.getCoords(n2)) * friction;
    }

}
