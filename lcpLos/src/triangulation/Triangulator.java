/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lcplos.dataStructures.Coords;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

/**
 *
 * @author elias
 */
public class Triangulator {

    private Polygon polygon;
    private HashMap<Coords, Integer> coordsToVertex;
    private List<Integer> vertices;

    public Triangulator(Coords[] coords, List<Integer> vertices) {
        this.vertices = vertices;
        int end = vertices.size()-1;
        if (coords[vertices.get(0)].equals(coords[vertices.get(end)])) {
            end--;
        }
        List points = new ArrayList<PolygonPoint>();
        this.coordsToVertex = new HashMap<>();
        for (int i = 0; i < end; i++) {
            int v = this.vertices.get(i);
            this.coordsToVertex.put(coords[v], v);
            PolygonPoint point = new PolygonPoint(coords[v].getX(), coords[v].getY());
            points.add(point);
        }
        this.polygon = new Polygon(points);

    }

    public List<int[]> triangulate() {
        Poly2Tri.triangulate(polygon);
        ArrayList<int[]> triangles = new ArrayList<>();
        for (DelaunayTriangle triangle : polygon.getTriangles()) {
            int[] tri = new int[3];
            for (int i = 0; i < triangle.points.length; i++) {
                tri[i] = this.coordsToVertex.get(new Coords(triangle.points[i].getX(), triangle.points[i].getY()));
            }
            triangles.add(tri);

        }
        return triangles;
    }
}