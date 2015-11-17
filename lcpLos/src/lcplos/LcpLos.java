/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos;

import java.io.File;
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
    public static void main(String[] args) {

        JSONArray polygons = GeoJsonReader2.lataaJsonObject(new File("testdata/testarea.geojson"));
        System.out.println("read done: " + polygons.length());


        VertexLib vlib = new VertexLib(polygons.length());
        for (int p = 0; p < polygons.length(); p++) {
            Coords[] coords = GeoJsonReader2.readPolygon(polygons, p);
            double friction  = polygons.getJSONObject(p).getJSONObject("properties").getDouble("Vertices");
            if (coords == null || coords.length < 4) {
                continue;
            }
            vlib.addPolygon(coords, p, friction);

        }
        System.out.println("vcount: " + vlib.getVertices().size());
//        geoJsonWriter2.kirjoita("testdata/toobig.geojson", geoJsonWriter2.removeRings(polygons, "urn:ogc:def:crs:EPSG::3047", vlib));

        
         List<Coords> vertices = vlib.getVertices();
         geoJsonWriter2.kirjoita("testdata/vertices.geojson", geoJsonWriter2.vertices(vertices, "urn:ogc:def:crs:EPSG::3047"));
        
        NeighbourFinder finder = new NeighbourFinder(vlib);

        int start = 200;
        System.out.println("start: " + start);
        int target = 1299;
        System.out.println("target: " + target);
        PathSearch2 search = new PathSearch2(start, target, finder, vlib);

        System.out.println("init done");
        boolean pathFound = search.aStar();
        if (pathFound) {
            System.out.println("path found");
        } else {
            System.out.println("no path found");
            return;
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
        geoJsonWriter2.kirjoita("testdata/path.geojson",
                geoJsonWriter2.muunnaJsonReitti(path, "urn:ogc:def:crs:EPSG::3047"));
        System.out.println("writing done");
        
        
        /*
         Set<CoordEdge> visigraph = new HashSet<>();

         int oldPercent = 0;
         for (int i = 0; i < polygons.length(); i++) {
         int percentDone = (int) ((double) i / polygons.length() * 100);
         if (oldPercent != percentDone) {
         System.out.println(percentDone + "%");
         oldPercent = percentDone;
         }
         Coords[] coords = vlib.getPolygon(i);
         Triangulator tri = new Triangulator(coords);
         List<int[]> triangles;
         try {
         triangles = tri.triangulate();
         } catch (Exception ex) {
         System.out.println("exception");
         System.out.println(ex);
         System.out.println("polygon: " + i);
         continue;
         }
         EdgeLocator poly = new EdgeLocator();
         for (int[] triangle : triangles) {
         poly.addTriangle(triangle);
         }
         for (int s1 = 0; s1 < coords.length; s1++) {
         boolean debug = false;
         if (debug) {
         System.out.println("");
         System.out.println("START " + s1);
         }
         Spt2 spt = new Spt2(s1, coords, poly, visigraph);
         }
         }
         System.out.println("visigraph done");
         geoJsonWriter2.kirjoita("testdata/visigraph.geojson",
         geoJsonWriter2.muunnaJsonReitti(visigraph, "urn:ogc:def:crs:EPSG::3047"));
         System.out.println("writing done");
         */
    }

}
