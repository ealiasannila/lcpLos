/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import logic.HelperFunctions;

/**
 *
 * @author elias
 */
public class VertexLib {

    private Set<Integer>[] vertexBelongsTo;
    private Coords[] verticeCoords;
    private List<List<Integer>>[] polygonToNodes;
    private HashMap<Coords, Integer> coordsToVertex;
    private double[] frictions;
    private double maxdist;
    private EdgeLocator[] loclib;
    private Map<Integer, List<Integer>>[] neighbourhood;
    private int vpointer;
    private double minfriction;

    public VertexLib(int nPoly, double maxdist, int nVert) {
        this.vertexBelongsTo = new Set[nVert];
        this.verticeCoords = new Coords[nVert];
        this.polygonToNodes = new List[nPoly];
        this.coordsToVertex = new HashMap<>();
        this.frictions = new double[nPoly];
        this.maxdist = maxdist;
        this.loclib = new EdgeLocator[nVert];
        this.neighbourhood = new Map[nVert];
        this.vpointer = 0;
        this.minfriction = Double.MAX_VALUE;
    }

    public VertexLib(int nPoly, double maxdist) {
        int nVert = 200000;
        this.vertexBelongsTo = new Set[nVert];
        this.verticeCoords = new Coords[nVert];
        this.polygonToNodes = new List[nPoly];
        this.coordsToVertex = new HashMap<>();
        this.frictions = new double[nPoly];
        this.maxdist = maxdist;
        this.loclib = new EdgeLocator[nVert];
        this.neighbourhood = new Map[nVert];
        this.vpointer = 0;
        this.minfriction = Double.MAX_VALUE;
    }

    public void addNeighbours(int v, Map<Integer, List<Integer>> neighbourhood) {
        this.neighbourhood[v] = neighbourhood;

    }

    public Map<Integer, List<Integer>> getNeighbourhood(int v) {
        return this.neighbourhood[v];
    }

    public EdgeLocator getLocator(int p) {
        return this.loclib[p];
    }

    public EdgeLocator addLocator(EdgeLocator locator, int p) {

        return this.loclib[p] = locator;
    }

    public void addPolygon(List<Coords[]> coords, int p, double friction) {
        if (friction < this.minfriction) {
            this.minfriction = friction;
        }
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
        int p = this.pointInsidePolygon(coords);
        if (p != -1) {
            this.addInsidePoint(coords, p);
        }
    }

    public double getMinFriction() {
        return this.minfriction;
    }

    public double getFriction(int v1, int v2) {
        double minX = Math.min(this.getCoords(v1).getX(), this.getCoords(v2).getX());
        double minY = Math.min(this.getCoords(v1).getY(), this.getCoords(v2).getY());
        double maxX = Math.max(this.getCoords(v1).getX(), this.getCoords(v2).getX());
        double maxY = Math.max(this.getCoords(v1).getY(), this.getCoords(v2).getY());

        double midX = minX + (maxX - minX) / 2.0;
        double midY = minY + (maxY - minY) / 2.0;
        return this.getFriction(this.pointInsidePolygon(new Coords(midX, midY)));
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
        if (this.getPolygon(p) == null) {
            return false;
        }
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
            v = this.vpointer;
            this.vpointer++;

            this.verticeCoords[v] = coords;
            this.coordsToVertex.put(coords, v);
            this.vertexBelongsTo[v] = new HashSet<Integer>();
        } else {
            v = this.coordsToVertex.get(coords);
        }
        this.polygonToNodes[p].get(ring).add(v);
        this.vertexBelongsTo[v].add(p);
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
        return this.verticeCoords[v];
    }

    public List<List<Integer>> getPolygon(int p) {
        return this.polygonToNodes[p];
    }

    public Set<Integer> vertexBelongsTo(int v) {
        return this.vertexBelongsTo[v];
    }

    public int size() {
        return this.coordsToVertex.size();
    }

    public Coords[] getVertices() {
        return verticeCoords;
    }

    public HashMap<Coords, Integer> getCoordsToVertex() {
        return coordsToVertex;
    }

    public int nPoly() {
        return this.frictions.length;
    }

}
