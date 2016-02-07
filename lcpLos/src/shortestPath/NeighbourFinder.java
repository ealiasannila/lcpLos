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
import lcplos.dataStructures.EdgeLocator;
import logic.Spt2;

/**
 *
 * @author eannila
 */
public class NeighbourFinder {

    private VertexLib vlib;
    private Spt2 spt;

    public NeighbourFinder(VertexLib vlib) {
        this.vlib = vlib;
    }

    public Map<Integer, List<Integer>> getNeighbours(int node) {
        if (this.vlib.getNeighbourhood(node) != null) {
            return this.vlib.getNeighbourhood(node);
        }
        Set<Integer> polygons = this.vlib.vertexBelongsTo(node);
        //System.out.println("belongs to polygons: ");
        //System.out.println(polygons);
        Map<Integer, List<Integer>> neighboursInPolygons = new HashMap<>();
        for (int p : polygons) {
            List<Integer> neighbours = new ArrayList<Integer>();
            EdgeLocator locator = this.vlib.getLocator(p);
            if (locator == null) {
                Triangulator triangulator = new Triangulator(p, vlib);
                List<int[]> triangles;
                try {
                    triangles = triangulator.triangulate();
                } catch (Exception ex) {
                    System.out.println("exception");
                    System.out.println(ex);
                    ex.printStackTrace();
                    System.out.println("polygon: " + p);
                    continue;
                }

                locator = new EdgeLocator();
                for (int[] triangle : triangles) {
                    locator.addTriangle(triangle);
                }
                this.vlib.addLocator(locator, p);
            }
            this.spt = new Spt2(node, locator, vlib);
            neighbours.addAll(this.spt.getNeighbours());
            neighboursInPolygons.put(p, neighbours);
        }
        this.vlib.addNeighbours(node, neighboursInPolygons);
        return neighboursInPolygons;
    }

}
