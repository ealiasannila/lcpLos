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
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.Graph;
import lcplos.dataStructures.NodeLibrary;
import lcplos.dataStructures.SPT;
import lcplos.dataStructures.Triangle;
import logic.EdgeSplitter;
import logic.PathSearch;
import logic.LosChecker;

/**
 *
 * @author elias
 */
public class LcpLos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Triangle[] triangles = new Triangle[]{
            new Triangle(new int[]{11, 0, 1}, new int[]{1}, 0),
            new Triangle(new int[]{11, 1, 10}, new int[]{0, 2}, 1),
            new Triangle(new int[]{10, 1, 7}, new int[]{1, 5, 3}, 2),
            new Triangle(new int[]{10, 7, 9}, new int[]{2, 4}, 3),
            new Triangle(new int[]{9, 7, 8}, new int[]{3}, 4),
            new Triangle(new int[]{1, 4, 7}, new int[]{2, 6, 8}, 5),
            new Triangle(new int[]{1, 3, 4}, new int[]{5, 7}, 6),
            new Triangle(new int[]{1, 2, 3}, new int[]{6}, 7),
            new Triangle(new int[]{7, 4, 6}, new int[]{5, 9}, 8),
            new Triangle(new int[]{4, 5, 6}, new int[]{8}, 9)};

        Coords[] channelc = new Coords[]{
            new Coords(2, 3),
            new Coords(2, 2),
            new Coords(3, 2),
            new Coords(3, 1),
            new Coords(2, 1),
            new Coords(2, 0),
            new Coords(1, 0),
            new Coords(1, 1),
            new Coords(0, 1),
            new Coords(0, 2),
            new Coords(1, 2),
            new Coords(1, 3),};

        SPT spt = new SPT(0, 9, triangles, channelc, 1);

        System.out.println("0" + spt.findTriangle(0));
        System.out.println("9" + spt.findTriangle(5));
        
        spt.run();
        

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
