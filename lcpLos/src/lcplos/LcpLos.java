/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
import visiGraph.EdgeLocator;
import visiGraph.Spt2;
import org.json.JSONArray;
import shortestPath.NeighbourFinder;
import shortestPath.PathSearch2;
import triangulation.Triangulator;
import visiGraph.CoordEdge;
import visiGraph.GeoJsonReader2;
import visiGraph.geoJsonWriter2;
import visiGraph.geoJsonWriter2;

/**
 *
 * @author elias
 */
public class LcpLos {

    /**
     * @param args the command line arguments
     */
    private static VertexLib createVlib(JSONArray polygons) {
        VertexLib vlib = new VertexLib(polygons.length(), 50);
        for (int p = 0; p < polygons.length(); p++) {
            List<Coords[]> coords = GeoJsonReader2.readPolygon(polygons, p);
            double friction = polygons.getJSONObject(p).getJSONObject("properties").getDouble("Vertices");
            if (coords.get(0) == null || coords.get(0).length < 4) {
                continue;
            }

            vlib.addPolygon(coords, p, friction);
        }
        return vlib;
    }

    private static Set<CoordEdge> aStar(int start, int target, NeighbourFinder finder, VertexLib vlib) {
        PathSearch2 search = new PathSearch2(start, target, finder, vlib);

        System.out.println("init done");
        boolean pathFound = search.aStar(target);
        if (pathFound) {
            System.out.println("path found");
        } else {
            System.out.println("no path found");
            return null;
        }

        Set<CoordEdge> path = new HashSet<>();
        Map<Integer, Integer> pred = search.getPred();
        int node = target;
        while (pred.get(node) != null) {
            int old = node;
            node = pred.get(node);
            path.add(new CoordEdge(vlib.getCoords(old), vlib.getCoords(node)));
        }

        System.out.println("path done");
        return path;

    }

    private static Set<CoordEdge> dijkstra(int start, Set<Integer> targets, NeighbourFinder finder, VertexLib vlib) {

        PathSearch2 search = new PathSearch2(start, targets, finder, vlib);

        System.out.println("init done");
        boolean pathFound = search.dijkstra();
        if (pathFound) {
            System.out.println("all targets found");
        } else {
            System.out.println("some targets not found");
        }

        Set<CoordEdge> path = new HashSet<>();
        Map<Integer, Integer> pred = search.getPred();
        System.out.println(targets);
        for (int node : targets) {
            System.out.println("target " + node);
            while (pred.get(node) != null) {
                int old = node;
                node = pred.get(node);
                path.add(new CoordEdge(vlib.getCoords(old), vlib.getCoords(node)));
            }
        }

        System.out.println("path done");
        return path;
    }

    private static void writeVertices(VertexLib vlib) {
        List<Coords> vertices = vlib.getVertices();
        geoJsonWriter2.kirjoita("testdata/vertices.geojson", geoJsonWriter2.vertices(vertices, "urn:ogc:def:crs:EPSG::3047"));

    }

    private static void writeTriangles(VertexLib vlib) {
        List<int[]> all = new ArrayList<int[]>();
        for (int p = 0; p < vlib.nPoly(); p++) {
            Triangulator triangulator = new Triangulator(p, vlib);
            List<int[]> triangles;
            try {
                triangles = triangulator.triangulate();
            } catch (Exception ex) {
                System.out.println("exception");
                System.out.println(ex);
                ex.printStackTrace();
                System.out.println("polygon: " + p);
                System.exit(1);
                return;
            }
            all.addAll(triangles);
        }
        geoJsonWriter2.kirjoita("testdata/triangles.geojson", geoJsonWriter2.triangles(all, vlib, "urn:ogc:def:crs:EPSG::3047"));
    }

    public static void main(String[] args) {

        JSONArray polygons = GeoJsonReader2.lataaJsonObject(new File("testdata/testarea.geojson"));
        System.out.println("read done: " + polygons.length());

        VertexLib vlib = createVlib(polygons);

        JSONArray targetPointsJson = GeoJsonReader2.lataaJsonObject(new File("testdata/target_points.geojson"));
        Coords[] targetPoints = GeoJsonReader2.readPoints(targetPointsJson);

        JSONArray startPointJson = GeoJsonReader2.lataaJsonObject(new File("testdata/start_point.geojson"));
        Coords startPoint = GeoJsonReader2.readPoints(startPointJson)[0];

        //if no target specified search to all.
        System.out.println("polygon vertices: " + vlib.getVertices().size());
        
        int start = vlib.getVertices().size();
        
        vlib.addInsidePoint(startPoint);
        vlib.addInsidePoints(targetPoints);
        
        
        System.out.println("vcount: " + vlib.getVertices().size());
        writeTriangles(vlib);
        writeVertices(vlib);
        
        System.out.println("vert+tri");
        
        NeighbourFinder finder = new NeighbourFinder(vlib);

        Set<Integer> targets = new HashSet<Integer>();
        for (int i = start+1; i < vlib.getVertices().size(); i++) {
            targets.add(i);
        }

        System.out.println("start: " + start);
        //System.out.println("targets: " + targets);
        //Set<CoordEdge> path = aStar(start, target, finder, vlib);
        Set<CoordEdge> path = dijkstra(start, targets, finder, vlib);
        System.out.println("path: " + path);

        geoJsonWriter2.kirjoita("testdata/path.geojson",
                geoJsonWriter2.muunnaJsonReitti(path, "urn:ogc:def:crs:EPSG::3047"));

        System.out.println("writing done");

    }

}
