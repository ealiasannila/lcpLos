/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
import triangulation.Triangulator;
import visiGraph.EdgeLocator;
import visiGraph.Spt2;

/**
 *
 * @author eannila
 */
public class NeighbourFinder {

    private VertexLib vlib;
    private Triangulator triangulator;
    private Spt2 spt;
    private Map<Integer, EdgeLocator> loclib;

    public NeighbourFinder(VertexLib vlib) {
        this.vlib = vlib;
        this.loclib = new HashMap<>();

    }

    public Map<Integer, Set<Integer>> getNeighbours(int node) {

        Set<Integer> polygons = this.vlib.vertexBelongsTo(node);
        //System.out.println("belongs to polygons: ");
        //System.out.println(polygons);
        Map<Integer, Set<Integer>> neighboursInPolygons = new HashMap<>();
        for (int p : polygons) {
            Set<Integer> neighbours = new HashSet<>();
            EdgeLocator locator;
            if (this.loclib.containsKey(p)) {
                locator = this.loclib.get(p);
            } else {
                this.triangulator = new Triangulator(vlib.getPolygon(p), vlib);
                List<int[]> triangles;
                try {
                    triangles = this.triangulator.triangulate();
                } catch (Exception ex) {
                    System.out.println("exception");
                    System.out.println(ex);
                    System.out.println("polygon: " + p);
                    continue;
                }

                locator = new EdgeLocator();
                for (int[] triangle : triangles) {
                    locator.addTriangle(triangle);
                }
                this.loclib.put(p, locator);
            }
            
            this.spt = new Spt2(node, locator, vlib);
            neighbours.addAll(this.spt.getNeighbours());
            neighboursInPolygons.put(p, neighbours);
        }

        return neighboursInPolygons;
    }

}
