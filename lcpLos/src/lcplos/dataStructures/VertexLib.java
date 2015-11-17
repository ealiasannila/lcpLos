/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import logic.HelperFunctions;
import logic.LosChecker;

/**
 *
 * @author elias
 */
public class VertexLib {

    private List<Set<Integer>> nodeToPolygons;
    private List<Coords> vertices;
    private List<List<Integer>>[] polygonToNodes;
    private HashMap<Coords, Integer> coordsToVertex;
    private double[] frictions;

    public VertexLib(int nPoly) {
        this.nodeToPolygons = new ArrayList<>();
        this.vertices = new ArrayList<Coords>();
        this.polygonToNodes = new List[nPoly];
        this.coordsToVertex = new HashMap<>();
        this.frictions = new double[nPoly];

    }

    public void addPolygon(List<Coords[]> coords, int p, double friction) {
        this.frictions[p] = friction;
        this.polygonToNodes[p] = new ArrayList<List<Integer>>();
        for (int ring = 0; ring < coords.size(); ring++) {
            if(coords.get(ring).length<4){
                continue;
            }
            this.polygonToNodes[p].add(new ArrayList<Integer>());
            for (int i = 0; i < coords.get(ring).length; i++) {
                int v;
                if (!this.coordsToVertex.containsKey(coords.get(ring)[i])) {
                    v = this.vertices.size();
                    this.vertices.add(coords.get(ring)[i]);
                    this.coordsToVertex.put(coords.get(ring)[i], v);
                    this.nodeToPolygons.add(new TreeSet<Integer>());
                } else {
                    v = this.coordsToVertex.get(coords.get(ring)[i]);
                }
                this.polygonToNodes[p].get(ring).add(v);
                this.nodeToPolygons.get(v).add(p);
            }
        }

    }

    public double getFriction(int p) {
        return frictions[p];
    }

    public Coords getCoords(int v) {
        return this.vertices.get(v);
    }

    public List<List<Integer>> getPolygon(int p) {
        return this.polygonToNodes[p];
    }

    public Set<Integer> vertexBelongsTo(int v) {
        return this.nodeToPolygons.get(v);
    }

    public List<Set<Integer>> getNodeToPolygons() {
        return nodeToPolygons;
    }

    public List<Coords> getVertices() {
        return vertices;
    }

    public HashMap<Coords, Integer> getCoordsToVertex() {
        return coordsToVertex;
    }

}
