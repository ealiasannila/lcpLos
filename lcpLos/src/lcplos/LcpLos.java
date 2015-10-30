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
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.Graph;
import lcplos.dataStructures.NodeLibrary;
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

        
        
        
        FrictionLibrary frictionlib = new FrictionLibrary();
        NodeLibrary nodelib = GeoJsonReader.readNodes(new File("testdata/testarea.geojson"), 6500, frictionlib, Double.MAX_VALUE, "friction");
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

        

    }

}
