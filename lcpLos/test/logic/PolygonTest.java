/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.NodeLibrary;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elias
 */
public class PolygonTest {

    private NodeLibrary nodelibrary;

    public PolygonTest() {

    }

    @Before
    public void setUp() {
        nodelibrary = new NodeLibrary(20, 5);

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
        nodelibrary.addNode(new Coordinates(6, 4), 2);
        
        

    }

    @Test
    public void testLosBetweenNodesTriangle() {
        assertEquals(true, Polygon.losBetweenNodes(0, 1, this.nodelibrary, 0));
        assertEquals(true, Polygon.losBetweenNodes(1, 2, this.nodelibrary, 0));
        assertEquals(true, Polygon.losBetweenNodes(2, 0, this.nodelibrary, 0));

        assertEquals(true, Polygon.losBetweenNodes(1, 0, this.nodelibrary, 0));
        assertEquals(true, Polygon.losBetweenNodes(2, 1, this.nodelibrary, 0));
        assertEquals(true, Polygon.losBetweenNodes(0, 2, this.nodelibrary, 0));
    }

    @Test
    public void testLosBetweenNodesSquare() {
        assertEquals(true, Polygon.losBetweenNodes(3, 1, this.nodelibrary, 1));
        assertEquals(true, Polygon.losBetweenNodes(3, 4, this.nodelibrary, 1)); //old node
        assertEquals(true, Polygon.losBetweenNodes(4, 2, this.nodelibrary, 1)); //old  node
        assertEquals(true, Polygon.losBetweenNodes(1, 2, this.nodelibrary, 1)); //old  node
        assertEquals(true, Polygon.losBetweenNodes(2, 1, this.nodelibrary, 1));
        assertEquals(true, Polygon.losBetweenNodes(4, 1, this.nodelibrary, 1)); //old node

    }

    @Test
    public void testLosBetweenNodesComplex() {
        assertEquals(false, Polygon.losBetweenNodes(5, 9, this.nodelibrary, 2));
        assertEquals(false, Polygon.losBetweenNodes(5, 8, this.nodelibrary, 2));
        assertEquals(false, Polygon.losBetweenNodes(8, 5, this.nodelibrary, 2));
        assertEquals(false, Polygon.losBetweenNodes(9, 6, this.nodelibrary, 2));

        assertEquals(true, Polygon.losBetweenNodes(7, 5, this.nodelibrary, 2));
        assertEquals(true, Polygon.losBetweenNodes(7, 10, this.nodelibrary, 2));
        assertEquals(true, Polygon.losBetweenNodes(7, 6, this.nodelibrary, 2));
        
//        assertEquals(true, Polygon.losBetweenNodes(5, 10, this.nodelibrary, 2));
        assertEquals(true, Polygon.losBetweenNodes(10, 5, this.nodelibrary, 2));



    }

    @Test
    public void testSample() {
        assertEquals(true, Polygon.sample(5, 9, nodelibrary, 2, 3));
        assertEquals(false, Polygon.sample(6, 3, nodelibrary, 2, 3));
        assertEquals(true, Polygon.sample(5, 6, nodelibrary, 2, 3));
        assertEquals(true, Polygon.sample(7, 10, nodelibrary, 2, 3));
    }

}
