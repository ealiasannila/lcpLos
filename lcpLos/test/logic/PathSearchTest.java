/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import lcplos.dataStructures.Coords;
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

        ArrayList<Coords> poly0 = new ArrayList<>();
        ArrayList<Coords> poly1 = new ArrayList<>();
        ArrayList<Coords> poly2 = new ArrayList<>();

        poly0.add(new Coords(0, 0));
        poly0.add(new Coords(0, 1));
        poly0.add(new Coords(1, 1));

        poly1.add(new Coords(1, 3));
        poly1.add(new Coords(0, 3));
        poly1.add(new Coords(0, 1));
        poly1.add(new Coords(1, 1));

        poly2.add(new Coords(4, 4));
        poly2.add(new Coords(4, 5));
        poly2.add(new Coords(5, 5));
        poly2.add(new Coords(5, 6));
        poly2.add(new Coords(6, 6));
        poly2.add(new Coords(4, 6));

        nodelibrary.addPolygon(poly0, 0);
        nodelibrary.addPolygon(poly1, 1);
        nodelibrary.addPolygon(poly2, 2);
        
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
