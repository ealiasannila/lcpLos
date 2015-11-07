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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.Graph;
import lcplos.dataStructures.NodeLibrary;
import visiGraph.PolygonOma;
import lcplos.dataStructures.SPT;
import visiGraph.Spt2;
import lcplos.dataStructures.Triangle;
import logic.EdgeSplitter;
import logic.HelperFunctions;
import logic.PathSearch;
import logic.LosChecker;
import triangulation.Triangulator;
import visiGraph.GeoJsonReader2;
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

        Coords[] coords = GeoJsonReader2.readPolygon(new File("testdata/testipolygon.geojson"));

        
        List<Integer> vertices = new ArrayList<Integer>();
        for (int i = 0; i < coords.length; i++) {
            vertices.add(i);
        }
        Triangulator tri = new Triangulator(coords, vertices);
        List<int[]> triangles = tri.triangulate();

        PolygonOma poly = new PolygonOma();
        for (int[] triangle : triangles) {
            for (int i = 0; i < 3; i++) {
                if (triangle[i] == 1) {
                    System.out.println(Arrays.toString(triangle));
                }
            }
            poly.addTriangle(triangle);
        }
        System.out.println("doen");
        Spt2 testpolygon = new Spt2(100, coords, poly);
        geoJsonWriter2.kirjoita("testdata/spt.geojson",
                geoJsonWriter2.muunnaJsonReitti(coords, testpolygon.getPred(), "urn:ogc:def:crs:EPSG::3047"));

        Spt2 testpolygon2 = new Spt2(101, coords, poly);
        geoJsonWriter2.kirjoita("testdata/spt2.geojson",
                geoJsonWriter2.muunnaJsonReitti(coords, testpolygon2.getPred(), "urn:ogc:def:crs:EPSG::3047"));

        
        Map<Integer, Integer> dif = new TreeMap<>();
        Map<Integer, Integer> dif2 = new TreeMap<>();
        
        for (Integer v : testpolygon2.getPred().keySet()) {
            
            if (testpolygon.getPred().get(v) != testpolygon2.getPred().get(v)) {
                dif.put(v, testpolygon.getPred().get(v));
                dif2.put(v, testpolygon2.getPred().get(v));
            }
        }
        geoJsonWriter2.kirjoita("testdata/dif.geojson",
                geoJsonWriter2.muunnaJsonReitti(coords, dif, "urn:ogc:def:crs:EPSG::3047"));

        geoJsonWriter2.kirjoita("testdata/dif2.geojson",
                geoJsonWriter2.muunnaJsonReitti(coords, dif2, "urn:ogc:def:crs:EPSG::3047"));
        geoJsonWriter2.kirjoita("testdata/bound.geojson",
                geoJsonWriter2.boundary(coords, dif2, "urn:ogc:def:crs:EPSG::3047"));

        geoJsonWriter2.kirjoita("testdata/triangulation.geojson",
                geoJsonWriter2.triangles(triangles, coords, "urn:ogc:def:crs:EPSG::3047"));

        /*
         Coords[] coords = new Coords[9];
         coords[0] = new Coords(1, 0);
         coords[1] = new Coords(1, 1);
         coords[2] = new Coords(0, 2);
         coords[3] = new Coords(0, 3);
         coords[4] = new Coords(0.5, 3);
         coords[5] = new Coords(2, 2);
         coords[6] = new Coords(2, 3);
         coords[7] = new Coords(3, 2);
         coords[8] = new Coords(3, 0);
         System.out.println("--asd");
         System.out.println(HelperFunctions.isRight(1, 4, 2, coords));
         System.out.println(HelperFunctions.isRight(1, 4, 4, coords));
         System.out.println(HelperFunctions.isRight(1, 4, 5, coords));
         Polygon polygon = new Polygon();
         polygon.addTriangle(new int[]{0, 1, 5});
         polygon.addTriangle(new int[]{1, 2, 4});
         polygon.addTriangle(new int[]{2, 3, 4});
         polygon.addTriangle(new int[]{4, 5, 1});
         polygon.addTriangle(new int[]{5, 6, 7});
         polygon.addTriangle(new int[]{5, 7, 8});
         polygon.addTriangle(new int[]{5, 8, 0});
         Spt2 s7 = new Spt2(8, coords.length, coords, polygon);
         System.out.println("[0, 1, 2, 3, 4, 5, 6, 7, 8]");
         System.out.println(Arrays.toString(s7.getPred()));
         */
        /*
         FrictionLibrary frictionlib = new FrictionLibrary();
         NodeLibrary nodelib = GeoJsonReader.readNodes(new File("testdata/testarea.geojson"), 7000, frictionlib, Double.MAX_VALUE, "friction");
         System.out.println("nodelib done: " + nodelib.getNumOfNodes() + " nodes");
         Graph graph = new Graph(nodelib, frictionlib);
         System.out.println("graph done");
         System.out.println("n: " + graph.getNumOfNodes());
         int node1 = nodelib.getNearestNode(new Coords(262020,6736501));
         int node2 = nodelib.getNearestNode(new Coords(332952,6666572));
         node2 = graph.getNumOfNodes() - 100;
         PathSearch pathSearch = new PathSearch(graph, node1, node2);
         System.out.println("pathsearch init");
         pathSearch.aStar();
         System.out.println("astar done");
         ArrayList<Integer> shortestPath = pathSearch.shortestPath();
         ArrayList<Double> frictions = graph.getFrictions(shortestPath);
         geoJsonWriter.kirjoita("testdata/path.geojson", geoJsonWriter.muunnaJsonReitti(shortestPath, frictions, nodelib, "urn:ogc:def:crs:EPSG::3047"));
         System.out.println("shortest patht done");
         */
    }

}
