/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos;

import dataManagement.GeoJsonReader;
import dataManagement.geoJsonWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.Graph;
import lcplos.dataStructures.NodeLibrary;
import visiGraph.PolygonOma;
import visiGraph.Spt2;
import lcplos.dataStructures.Triangle;
import logic.EdgeSplitter;
import logic.HelperFunctions;
import logic.PathSearch;
import logic.LosChecker;
import org.json.JSONArray;
import org.json.JSONObject;
import triangulation.Triangulator;
import visiGraph.CoordEdge;
import visiGraph.Edge;
import visiGraph.GeoJsonReader2;
import visiGraph.Sector;
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

        JSONArray polygons = GeoJsonReader2.lataaJsonObject(new File("testdata/toobig.geojson"));
        Set<CoordEdge> visigraph = new HashSet<>();
        
        int oldPercent = 0;
        System.out.println("JSONObject done");
        for (int i = 0; i < polygons.length(); i++) {
            int percentDone = (int) ((double) i / polygons.length() * 100);
            if (oldPercent != percentDone) {
                System.out.println(percentDone + "%");
                oldPercent = percentDone;
            }
            Coords[] coords = GeoJsonReader2.readPolygon(polygons, i);
            if (coords == null || coords.length < 4) {
                continue;
            }
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
           
            PolygonOma poly = new PolygonOma();
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
        /*  geoJsonWriter2.kirjoita("testdata/visigraph.geojson",
         geoJsonWriter2.muunnaJsonReitti(visigraph, "urn:ogc:def:crs:EPSG::3047"));
         System.out.println("writing done");
         */
    }

}
