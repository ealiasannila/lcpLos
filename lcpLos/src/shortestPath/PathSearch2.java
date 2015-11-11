/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPath;

import java.util.ArrayList;
import logic.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import lcplos.dataStructures.Coords;

/**
 *
 * @author elias
 */
public class PathSearch2 {

    private PathNode startnode;
    private Coords target;
    private PriorityQueue<PathNode> minheap;
    private NeighbourFinder finder;
    private Set<Coords> used;

    public PathSearch2(Coords start, Coords end, NeighbourFinder finder) {
        this.finder = finder;
        this.used = new HashSet<>();
        double d = HelperFunctions.eucDist(start, end);

        this.startnode = new PathNode(start, 0, d, null);
        this.target = end;

        Comparator<PathNode> smallestEstimate = new Comparator<PathNode>() {
            @Override
            public int compare(PathNode n1, PathNode n2) {
                double d1;
                double d2;
                if (n1.getToStart() == Double.MAX_VALUE) {
                    d1 = Double.MAX_VALUE;
                } else {
                    d1 = n1.getToEnd() + n1.getToStart();
                }

                if (n2.getToStart() == Double.MAX_VALUE) {
                    d2 = Double.MAX_VALUE;
                } else {
                    d2 = n2.getToEnd() + n2.getToStart();
                }

                return (int) (d1 - d2);
            }
        };

        this.minheap = new PriorityQueue<>(smallestEstimate);
        this.minheap.add(startnode);
        this.used.add(startnode.getCoords());
    }

    public PathNode aStar() {
        while (!this.minheap.isEmpty()) {
            PathNode node = this.minheap.poll();
           // System.out.println(node.getCoords());
           // System.out.println(node);
            this.used.add(node.getCoords());
            if (node.getCoords().equals(this.target)) {
                return node;
            }

            Coords[] neighbourcoords = this.finder.getNeighbours(node);
            for (int i = 0; i<neighbourcoords.length; i++) {
                Coords nc = neighbourcoords[i];
               // System.out.println("neighbour: " + nc);
                if (!this.used.contains(nc)) {
                    this.minheap.add(new PathNode(nc, node.getToStart() + this.cost(node, nc), HelperFunctions.eucDist(nc, this.target), node));
                }
            }
        }
        return null;
    }

    private double cost(PathNode n1, Coords n2) {
        return HelperFunctions.eucDist(n1.getCoords(), n2);
    }

}
