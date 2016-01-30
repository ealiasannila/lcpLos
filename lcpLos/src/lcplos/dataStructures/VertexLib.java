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
import visiGraph.EdgeLocator;

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
    private double maxdist;
    private Map<Integer, EdgeLocator> loclib;
    private Map<Integer, Map<Integer, List<Integer>>> neighbourhood;

    public VertexLib(int nPoly, double maxdist) {
        this.nodeToPolygons = new ArrayList<>();
        this.vertices = new ArrayList<Coords>();
        this.polygonToNodes = new List[nPoly];
        this.coordsToVertex = new HashMap<>();
        this.frictions = new double[nPoly];
        this.maxdist = maxdist;
        this.loclib = new HashMap<>();
        this.neighbourhood = new HashMap<>();
    }

    public void addNeighbours(int v, Map<Integer, List<Integer>> neighbourhood){
            this.neighbourhood.put(v, neighbourhood);
        
    }
    
    public Map<Integer, List<Integer>> getNeighbourhood(int v){
        return this.neighbourhood.get(v);
    }
    
    public Map<Integer, EdgeLocator> getLoclib() {
        return this.loclib;
    }

    public void addPolygon(List<Coords[]> coords, int p, double friction) {
        this.frictions[p] = friction;
        this.polygonToNodes[p] = new ArrayList<List<Integer>>();
        for (int ring = 0; ring < coords.size(); ring++) {
            /*if (coords.get(ring).length < 4) {
             continue;
             }*/
            this.polygonToNodes[p].add(new ArrayList<Integer>());
            for (int i = 0; i < coords.get(ring).length; i++) {
                Coords c = coords.get(ring)[i];
                this.addPoint(c, p, ring);

                int next = i + 1;
                if (next == coords.get(ring).length) {
                    next = 0;
                }
                Coords nc = coords.get(ring)[next];
                this.addExtraPoints(p, ring, c, nc);
            }
        }
    }

    public void addInsidePoints(Coords[] points) {
        for (Coords point : points) {
            this.addInsidePoint(point);
        }
    }

    public void addInsidePoint(Coords coords) {
        this.addInsidePoint(coords, this.pointInsidePolygon(coords));
    }

    public void addInsidePoint(Coords coords, int p) {

        this.polygonToNodes[p].add(new ArrayList<Integer>());
        this.addPoint(coords, p, this.polygonToNodes[p].size() - 1);
        /*
         this.addPoint(new Coords(coords.getX(), coords.getY() + 1), p, this.polygonToNodes[p].size() - 1);
         this.addPoint(new Coords(coords.getX() + 1, coords.getY() + 1), p, this.polygonToNodes[p].size() - 1);
         this.addPoint(new Coords(coords.getX() + 1, coords.getY()), p, this.polygonToNodes[p].size() - 1);
         */
    }

    public int pointInsidePolygon(Coords coords) {
        for (int p = 0; p < this.frictions.length; p++) {
            if (this.pointInside(coords, p)) {
                return p;
            }
        }
        return -1;
    }

    private boolean pointInside(Coords coords, int p) {

        List<List<Integer>> polygon = this.getPolygon(p);
        for (int i = 0; i < polygon.size(); i++) {
            List<Integer> ring = polygon.get(i);
            int intersections = 0;
            for (int start = 0; start < ring.size(); start++) {
                int end = start + 1;
                if (start == ring.size() - 1) { //last to first
                    end = 0;
                }

                Coords sc = this.getCoords(ring.get(start));
                Coords ec = this.getCoords(ring.get(end));

                if ((Math.abs(sc.getX() - coords.getX()) < 0.0001 && Math.abs(ec.getX() - coords.getX()) < 0.0001)) {

                    if (coords.getY() - Math.min(sc.getY(), ec.getY()) > -0.00001 && coords.getY() - Math.max(sc.getY(), ec.getY()) < 0.00001) {
                        return true;
                    }
                }

                //raycasting method, ray shot due north
                if ((sc.getX() <= coords.getX() && ec.getX() > coords.getX()) || (sc.getX() > coords.getX() && ec.getX() <= coords.getX())) { //point between edge x coordinates
                    double proportionOfSegment = (coords.getX() - sc.getX()) / (ec.getX() - sc.getX());
                    double intersectY = sc.getY() + proportionOfSegment * (ec.getY() - sc.getY());

                    if (Math.abs(coords.getY() - intersectY) < 0.0001) { //point on edge
                        return true;
                    }
                    if (coords.getY() < intersectY) {
                        intersections++;
                    }

                }
            }
            if (intersections % 2 != 0) {
                if (i != 0) {
                    return false;
                }
            } else {
                if (i == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addPoint(Coords coords, int p, int ring) {
        int v;
        if (!this.coordsToVertex.containsKey(coords)) {
            v = this.vertices.size();
            this.vertices.add(coords);
            this.coordsToVertex.put(coords, v);
            this.nodeToPolygons.add(new TreeSet<Integer>());
        } else {
            v = this.coordsToVertex.get(coords);
        }
        this.polygonToNodes[p].get(ring).add(v);
        this.nodeToPolygons.get(v).add(p);
    }

    private void addExtraPoints(int p, int ring, Coords sc, Coords ec) {
        double length = HelperFunctions.eucDist(sc, ec);
        if (length > this.maxdist) {
            int nodesToAdd = (int) (length / this.maxdist);
            for (int i = 0; i < nodesToAdd; i++) {
                Coords loc = calcLoc(sc, ec, nodesToAdd, i + 1);
                this.addPoint(loc, p, ring);
            }
        }
    }

    private static double pointFromSegment(double a, double b, int n, int m) {
        return a + (b - a) / (n + 1) * m;
    }

    private static Coords calcLoc(Coords sc, Coords ec, int nodesToadd, int n) {
        double px = pointFromSegment(sc.getX(), ec.getX(), nodesToadd, n);
        double py = pointFromSegment(sc.getY(), ec.getY(), nodesToadd, n);
        return new Coords(px, py);
    }

    public double getFriction(int p) {
        return frictions[p];
    }

    public Coords getCoords(int v) {
        if (v == -1) {
            System.out.println("-1");
        }
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

    public int nPoly() {
        return this.frictions.length;
    }

}
