/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import lcplos.dataStructures.Edge;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elias
 */
public class EdgeLocator {

    private Map<Edge, Edge> crossingEdges;
    private Map<Integer, Set<Edge>> opposingEdges;

    public EdgeLocator() {
        this.crossingEdges = new HashMap<>();
        this.opposingEdges = new HashMap<>();
    }

    public Set<Edge> getOpposingEdge(int v) {
        return this.opposingEdges.get(v);
    }

    private int thirdCorner(int s, int e) {
        return 3 - (s + e);
    }

    private Edge opposingEdge(int v, int[] vertices) {
        if (v == 0) {
            return new Edge(vertices[1], vertices[2]);
        } else if (v == 1) {
            return new Edge(vertices[0], vertices[2]);
        }

        return new Edge(vertices[0], vertices[1]);
    }

    public void addTriangle(int[] vertices) {
        for (int s = 0; s < 3; s++) {
            int e = s + 1;
            if (e == 3) {
                e = 0;
            }
            if (!this.opposingEdges.containsKey(vertices[s])) {
                
                this.opposingEdges.put(vertices[s], new HashSet<Edge>());
            }

            this.opposingEdges.get(vertices[s]).add(this.opposingEdge(s, vertices));

            Edge edge = new Edge(vertices[s], vertices[e]);
            if (this.isPolygonEdge(edge)) {

                int opposite = vertices[this.thirdCorner(s, e)];
                this.crossingEdges.get(edge).setE(opposite);

            } else {
                int opposite = vertices[this.thirdCorner(s, e)];
                Edge crossing = new Edge(opposite, -1);
                this.crossingEdges.put(edge, crossing);
            }
        }
    }
    

    public Edge locateCrossingEdge(Edge edge) {
        return this.crossingEdges.get(edge);
    }

    public boolean isPolygonEdge(Edge edge) {
        if (!this.crossingEdges.containsKey(edge)) {
            return false;
        }
        return this.crossingEdges.get(edge).getR() == -1;
    }
}
