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
import lcplos.dataStructures.VertexLib;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

/**
 *
 * @author elias
 */
public class Triangulator {

    private Polygon polygon;
    private HashMap<Coords, Integer> coordsToVertex;

    public Triangulator(List<List<Integer>> vertices, VertexLib vlib) {

        List points = new ArrayList<PolygonPoint>();
        this.coordsToVertex = new HashMap<>();
        for (int v : vertices.get(0)) {
            Coords coords = vlib.getCoords(v);
            this.coordsToVertex.put(coords, v);
            PolygonPoint point = new PolygonPoint(coords.getX(), coords.getY());
            points.add(point);
        }
        this.polygon = new Polygon(points);

        if (vertices.size() > 1) {
            for (int i = 1; i < vertices.size(); i++) {
                if(vertices.get(i).size()<4){
                    continue;
                }
                List hole = new ArrayList<PolygonPoint>();

                for (int v : vertices.get(i)) {
                    Coords coords = vlib.getCoords(v);
                    if(this.coordsToVertex.containsKey(coords)){
                        System.out.println("trying to add weakly simple polygon");
                        return;
                    }
                    this.coordsToVertex.put(coords, v);
                    PolygonPoint point = new PolygonPoint(coords.getX(), coords.getY());
                    hole.add(point);
                }
                this.polygon.addHole(new Polygon(hole));
            }
        }

    }

    public List<int[]> triangulate() throws Exception {
        
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
