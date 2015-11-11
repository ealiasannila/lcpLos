/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
import visiGraph.EdgeLocator;
import visiGraph.Spt2;
import org.json.JSONArray;
import shortestPath.NeighbourFinder;
import shortestPath.PathNode;
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
        VertexLib vlib = new VertexLib(polygons.length());
        for (int p = 0; p < polygons.length(); p++) {
            Coords[] coords = GeoJsonReader2.readPolygon(polygons, p);
            if (coords == null || coords.length < 4) {
                continue;
            }
            for (int j = 0; j < coords.length; j++) {
                vlib.addVertex(coords[j], p);
                vlib.addPolygon(coords, p);
            }
        }
        System.out.println(polygons.length());
        System.out.println(vlib.pSize());
        System.out.println("vlib done");

        NeighbourFinder finder = new NeighbourFinder(vlib);
        PathSearch2 search = new PathSearch2(vlib.getPolygon(0)[0], vlib.getPolygon(64)[0], finder);
        System.out.println("init done");
        PathNode target = search.aStar();
        System.out.println("search done");

        Set<CoordEdge> path = new HashSet<>();
        while (target.getPred() != null) {
            Coords old = target.getCoords();
            target = target.getPred();
            path.add(new CoordEdge(old, target.getCoords()));
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
