/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.Graph;
import lcplos.dataStructures.NodeLibrary;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elias
 */
public class PathSearchTest {

    private Graph graph;

    public PathSearchTest() {
    }

    @Before
    public void setUp() {
        NodeLibrary nodelibrary = new NodeLibrary(5);

        nodelibrary.addNode(new Coordinates(0, 0), 0);
        nodelibrary.addNode(new Coordinates(0, 1), 0);
        nodelibrary.addNode(new Coordinates(1, 1), 0);

        nodelibrary.addNode(new Coordinates(1, 3), 1);
        nodelibrary.addNode(new Coordinates(0, 3), 1);
        nodelibrary.addNode(new Coordinates(0, 1), 1);
        nodelibrary.addNode(new Coordinates(1, 1), 1);

        nodelibrary.addNode(new Coordinates(4, 4), 2);
        nodelibrary.addNode(new Coordinates(4, 5), 2);
        nodelibrary.addNode(new Coordinates(5, 5), 2);
        nodelibrary.addNode(new Coordinates(5, 6), 2);
        nodelibrary.addNode(new Coordinates(6, 6), 2);
        nodelibrary.addNode(new Coordinates(4, 6), 2);

        FrictionLibrary frictionlib = new FrictionLibrary();
        frictionlib.addFriction(0, 100);
        frictionlib.addFriction(1, 1);
        frictionlib.addFriction(2, 2);

        this.graph = new Graph(nodelibrary, frictionlib);

    }

    @Test
    public void testShortestPath1() {
        PathSearch pathSearch = new PathSearch(graph, 0, 2);
        pathSearch.aStar();

        assertEquals("[0, 1, 2]", pathSearch.shortestPath().toString());
    }

    @Test
    public void testShortestPath2() {
        PathSearch pathSearch = new PathSearch(graph, 4, 2);
        pathSearch.aStar();

        assertEquals("[4, 2]", pathSearch.shortestPath().toString());
    }

    @Test
    public void testShortestPath() {
        PathSearch pathSearch = new PathSearch(graph, 0, 7);
        pathSearch.aStar();
        assertEquals(null, pathSearch.shortestPath());

    }

}
