/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos;

import dataManagement.GeoJsonReader;
import java.io.File;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.Graph;
import lcplos.dataStructures.NodeLibrary;
import logic.PathSearch;

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
        NodeLibrary nodelib = GeoJsonReader.readNodes(new File("testdata/testarea.geojson"), 1000000, 100, frictionlib);
        System.out.println("nodelib done");
        Graph graph = new Graph(nodelib, frictionlib);
        System.out.println("graph done");
        PathSearch pathSearch = new PathSearch(graph, 100, Math.min(500, nodelib.getNumOfNodes()));
        System.out.println("pathsearch init");
        pathSearch.aStar();
        System.out.println("astar done");
        System.out.println(pathSearch.shortestPath());
        System.out.println("shortest patht done");
    }
    
}
