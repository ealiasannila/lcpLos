/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
import lcplos.dataStructures.EdgeLocator;
import org.json.JSONArray;
import shortestPath.NeighbourFinder;
import shortestPath.PathSearch2;
import triangulation.Triangulator;
import lcplos.dataStructures.CoordEdge;
import dataManagement.GeoJsonReader2;
import dataManagement.geoJsonWriter2;
import java.util.Arrays;
import java.util.HashMap;
import logic.HelperFunctions;

/**
 *
 * @author elias
 */
public class LcpLos {

    /**
     * @param args the command line arguments
     */
    private static VertexLib createVlib(JSONArray polygons, int vnumber, double maxd, String fric) {
        VertexLib vlib = new VertexLib(polygons.length(), maxd, vnumber);
        for (int p = 0; p < polygons.length(); p++) {
            List<Coords[]> coords = GeoJsonReader2.readPolygon(polygons, p);
            if (coords.isEmpty()) {
                continue;
            }
            double friction = polygons.getJSONObject(p).getJSONObject("properties").getDouble(fric);
            if (coords.get(0) == null || coords.get(0).length < 3) {
                continue;
            }

            vlib.addPolygon(coords, p, friction);
        }
        return vlib;
    }

    private static PathSearch2 doAstar(int start, int target, NeighbourFinder finder, VertexLib vlib) {
        PathSearch2 search = new PathSearch2(start, target, finder, vlib);

        System.out.println("init done");
        boolean pathFound = search.findSingle(target);
        if (pathFound) {
            System.out.println("path found");
        } else {
            System.out.println("no path found");;
        }
        return search;
    }

    private static Map<CoordEdge, Double> findOne(PathSearch2 search, int target, VertexLib vlib) {

        Map<CoordEdge, Double> path = new HashMap<>();
        int[] pred = search.getPred();
        int node = target;
        while (pred[node] != -1) {
            int old = node;
            node = pred[node];
            path.put(new CoordEdge(vlib.getCoords(old), vlib.getCoords(node)),vlib.getFriction(old, node) );
        }

        System.out.println("path done");
        return path;

    }

    private static Map<CoordEdge, Double> findMany(PathSearch2 search, Set<Integer> targets, VertexLib vlib) {

        Map<CoordEdge,Double> path = new HashMap<>();
        double[] cost = search.getToStart();
        int[] pred = search.getPred();
        for (int node : targets) {
            while (pred[node] != -1) {
                int old = node;
                node = pred[node];
                double friction = (cost[old]- cost[node])/HelperFunctions.eucDist(vlib.getCoords(old), vlib.getCoords(node));
                path.put(new CoordEdge(vlib.getCoords(old), vlib.getCoords(node)),friction );
            }
        }

        System.out.println("path done");
        return path;

    }

    private static PathSearch2 dijkstra(int start, Set<Integer> targets, NeighbourFinder finder, VertexLib vlib) {

        PathSearch2 search = new PathSearch2(start, targets, finder, vlib);

        System.out.println("init done");
        boolean pathFound = search.findMany();
        if (pathFound) {
            System.out.println("all targets found");
        } else {
            System.out.println("some targets not found");
        }
        return search;
    }

    private static void writeVertices(VertexLib vlib) {
        geoJsonWriter2.kirjoita("testdata/vertices.geojson", geoJsonWriter2.vertices(vlib, "urn:ogc:def:crs:EPSG::3047"));
    }

    private static void writeTargets(Set<Integer> targets, VertexLib vlib, double[] costs, String path) {
        geoJsonWriter2.kirjoita(path, geoJsonWriter2.vertices(vlib, targets, costs, "urn:ogc:def:crs:EPSG::3047"));
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
                continue;
            }

            EdgeLocator locator = new EdgeLocator();
            for (int[] triangle : triangles) {
                locator.addTriangle(triangle);
            }
            vlib.addLocator(locator, p);
            all.addAll(triangles);
        }
        geoJsonWriter2.kirjoita("testdata/triangles.geojson", geoJsonWriter2.triangles(all, vlib, "urn:ogc:def:crs:EPSG::3047"));
    }

    private static void writeVisibleBoundary(int v, VertexLib vlib, NeighbourFinder finder) {
        System.out.println(finder.getNeighbours(v));
        geoJsonWriter2.kirjoita("testdata/boundary.geojson", geoJsonWriter2.boundary(vlib, finder.getNeighbours(v), "urn:ogc:def:crs:EPSG::3047"));
    }

    private static void writeNeighboursAsLines(int v, VertexLib vlib, NeighbourFinder finder) {
        Map<Integer, List<Integer>> neighbours = finder.getNeighbours(v);
        Map<CoordEdge, Double> lines = new HashMap<>();
        for (List<Integer> list : neighbours.values()) {
            for (int n : list) {
                lines.put(new CoordEdge(vlib.getCoords(v), vlib.getCoords(n)), vlib.getFriction(v, n));
            }
        }
        geoJsonWriter2.kirjoita("testdata/boundary.geojson", geoJsonWriter2.createJsonPath(lines, "urn:ogc:def:crs:EPSG::3047"));
    }

    public static void main(String[] args) {

        System.out.println(Arrays.toString(args));
        
        JSONArray polygons = GeoJsonReader2.lataaJsonObject(new File(args[0]));
        System.out.println("read done: " + polygons.length());
        int vnumber;
        try {
            vnumber = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Need to know number of vertices, if not known, use sufficiently large. Program will print actual number for future use");
            return;
        }
        VertexLib vlib = createVlib(polygons, vnumber, Double.parseDouble(args[5]), args[6]);
        JSONArray startPointJson = GeoJsonReader2.lataaJsonObject(new File(args[3]));
        Coords startPoint = GeoJsonReader2.readPoints(startPointJson)[0];
        System.out.println("polygon vertices: " + vlib.size());
        int start = vlib.size();
        vlib.addInsidePoint(startPoint);

        Set<Integer> targets = new HashSet<>();
        if (args[2].equals("-a")) {
            JSONArray astarTargetPointJson = GeoJsonReader2.lataaJsonObject(new File(args[4]));
            Coords targetPoint = GeoJsonReader2.readPoints(astarTargetPointJson)[0];
            vlib.addInsidePoint(targetPoint);

        } else if (args[2].equals("-d")) {
            JSONArray targetPointsJson = GeoJsonReader2.lataaJsonObject(new File(args[4]));
            Coords[] targetPoints = GeoJsonReader2.readPoints(targetPointsJson);
            vlib.addInsidePoints(targetPoints);
            for (int i = start; i < vlib.size(); i++) {
                targets.add(i);
            }

        } else {
            System.out.println("mode not specified. Possible modes: "
                    + " -d (dijkstra 1 to many target points) "
                    + "-a (a star 1 target point)"
                    + "Simply writing triangulation or visibility graph, or search to all coming soon");
            return;
        }
        System.out.println("vcount: " + vlib.size());
       // writeTriangles(vlib);
//        System.out.println("triangulation done");
       // writeVertices(vlib);

        NeighbourFinder finder = new NeighbourFinder(vlib);
        Map<CoordEdge, Double> path;
        PathSearch2 search;
        if (args[2].equals("-a")) {
            search = doAstar(start, start + 1, finder, vlib);
            path = findOne(search, start + 1, vlib);
        } else if (args[2].equals("-d")) {
            search = dijkstra(start, targets, finder, vlib);
            path = findMany(search, targets, vlib);
            writeTargets(targets, vlib, search.getToStart(),args[args.length-2]);
        } else {
            return;
        }

        geoJsonWriter2.kirjoita(args[args.length-1],
                geoJsonWriter2.createJsonPath(path, "urn:ogc:def:crs:EPSG::3047"));

        System.out.println("writing done");

    }

}
