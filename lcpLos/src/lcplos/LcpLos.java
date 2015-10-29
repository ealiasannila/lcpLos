/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos;

import dataManagement.GeoJsonReader;
import dataManagement.geoJsonWriter;
import java.io.File;
import lcplos.dataStructures.Coordinates;
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

        System.out.println(LosChecker.edgesIntersect(new Coordinates(4, 4), new Coordinates(4, 6), new Coordinates(4, 5), new Coordinates(5, 5)));

        FrictionLibrary frictionlib = new FrictionLibrary();
        NodeLibrary nodelib = GeoJsonReader.readNodes(new File("testdata/testarea.geojson"), 6500, frictionlib, Double.MAX_VALUE, "friction");
        System.out.println("nodelib done: " + nodelib.getNumOfNodes() + " nodes");
        Graph graph = new Graph(nodelib, frictionlib);
        System.out.println("graph done");
        System.out.println("n: " + graph.getNumOfNodes());
        PathSearch pathSearch = new PathSearch(graph, 700, graph.getNumOfNodes() - 10);
        System.out.println("pathsearch init");
        pathSearch.aStar();
        System.out.println("astar done");
        geoJsonWriter.kirjoita("testdata/path.geojson", geoJsonWriter.muunnaJsonReitti(pathSearch.shortestPath(), nodelib, "urn:ogc:def:crs:EPSG::3047"));
        System.out.println("shortest patht done");

        System.out.println("!!!");
        int node1 = nodelib.getNearestNode(new Coordinates(264185, 6733032));
        int node3 = nodelib.getNearestNode(new Coordinates(264430, 6733228));

        int node2 = nodelib.getNearestNode(new Coordinates(264571, 6732975));
        System.out.println("is edge ");
        System.out.println(graph.isEdge(node1, node2));
        System.out.println(graph.isEdge(node3, node2));

    }

}
