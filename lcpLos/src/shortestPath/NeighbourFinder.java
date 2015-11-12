/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    private EdgeLocator locator;
    private Triangulator triangulator;
    private Spt2 spt;

    public NeighbourFinder(VertexLib vlib) {
        this.vlib = vlib;

    }

    public Coords[] getNeighbours(Coords node) {
        List<Integer> polygons = this.vlib.vertexBelongsTo(node);
        System.out.println("belongs to polygons: ");
        System.out.println(polygons);
        Set<Coords> neighbours = new HashSet<Coords>();
        for (int p : polygons) {

            Coords[] coords = vlib.getPolygon(p);
            if(coords==null){
                System.out.println("no nodes p: " + p);
                return  neighbours.toArray(new Coords[neighbours.size()]);
            }

            this.triangulator = new Triangulator(coords);
            List<int[]> triangles;
            try {
                triangles = this.triangulator.triangulate();
            } catch (Exception ex) {
                System.out.println("exception");
                System.out.println(ex);
                System.out.println("polygon: " + p);
                continue;
            }

            this.locator = new EdgeLocator();
            for (int[] triangle : triangles) {
                locator.addTriangle(triangle);
            }

            for (int i = 0; i < coords.length; i++) {
                if (coords[i].equals(node)) {
                    this.spt = new Spt2(i, coords, this.locator);
                    break;
                }
            }
            
            neighbours.addAll(this.spt.getNeighbours());
        }
        return neighbours.toArray(new Coords[neighbours.size()]);
    }

}
