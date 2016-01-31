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
    private double[] toStart;
    private int[] pred;
    private Set<Integer> target;
    private PriorityQueue<Integer> minheap;
    private NeighbourFinder finder;
    private VertexLib vlib;

    public PathSearch2(int start, int end, NeighbourFinder finder, VertexLib vlib) {

        this.initAstar(start, end, finder, vlib);
    }

    public PathSearch2(int start, Set<Integer> end, NeighbourFinder finder, VertexLib vlib) {
        this.initDijkstra(start, end, finder, vlib);
    }

    private void init(int start, NeighbourFinder finder, VertexLib vlib) {
        this.finder = finder;
        this.vlib = vlib;

        this.startnode = start;
        this.toStart = new double[vlib.size()];
        this.pred = new int[vlib.size()];

        for (int i = 0; i < this.toStart.length; i++) {
            this.toStart[i] = Double.MAX_VALUE;
            this.pred[i] = -1;
        }
        this.toStart[this.startnode] = 0.0;

    }

    private void initHeap(Comparator smallestEstimate) {
        this.minheap = new PriorityQueue<>(20, smallestEstimate);
        this.minheap.add(startnode);
    }

    private void initDijkstra(int start, Set<Integer> end, NeighbourFinder finder, VertexLib vlib) {

        this.init(start, finder, vlib);
        this.target = end;
        Comparator<Integer> smallestEstimate = new Comparator<Integer>() {
            @Override
            public int compare(Integer n1, Integer n2) {
                return (int) (toStart[n1] - toStart[n2]);
            }

        };
        this.initHeap(smallestEstimate);

    }

    private void initAstar(int start, final int end, NeighbourFinder finder, VertexLib vlib) {

        this.init(start, finder, vlib);
        final double minfriction = this.vlib.getMinFriction();
        Comparator<Integer> smallestEstimate = new Comparator<Integer>() {
            @Override
            public int compare(Integer n1, Integer n2) {
                double d1 = toStart[n1];
                double d2 = toStart[n2];
                d1 = Math.max(d1, d1 + cost(n1, end, minfriction));//update to using minumum friction
                d2 = Math.max(d2, d2 + cost(n2, end, minfriction));//update to using minumum friction

                return (int) (d1 - d2);
            }

        };

        this.initHeap(smallestEstimate);
    }

    private boolean check(int target, int node){
        return target==node;
    }
    
    public boolean findSingle(int target) {
        while (!this.minheap.isEmpty()) {
            int node = this.minheap.poll();

            if (node == target) {
                return true;
            }
            this.iterateNeighbours(node);
        }
        return false;
    }

    public boolean findMany() {
        Set<Integer> found = new HashSet<Integer>();
        while (!this.minheap.isEmpty()) {
            int node = this.minheap.poll();
            if (this.target.contains(node) && !found.contains(node)) {
                found.add(node);
                if (found.size() == target.size()) {
                    return true;
                }
            }
            this.iterateNeighbours(node);
        }
        return false;
    }
    private void iterateNeighbours(int v){
        Map<Integer, List<Integer>> neighbours = this.finder.getNeighbours(v);
            for (int polygon : neighbours.keySet()) {
                List<Integer> neighboursInPolygon = neighbours.get(polygon);
                double friction = this.vlib.getFriction(polygon);
                for (int neighbour : neighboursInPolygon) {
                    if (this.toStart[neighbour] > Double.MAX_VALUE - 1) {
                        this.minheap.add(neighbour);
                    }
                    if (this.relax(v, neighbour, friction)) {
                        this.minheap.remove(neighbour);
                        this.minheap.add(neighbour);
                    }

                }

            }
    }

    public int[] getPred() {
        return pred;
    }

    private boolean relax(int node, int neighbour, double cost) {
        if (this.toStart[node] + this.cost(node, neighbour, cost) < this.toStart[neighbour]) {
            this.toStart[neighbour] = this.toStart[node] + this.cost(node, neighbour, cost);
            this.pred[neighbour] = node;
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
