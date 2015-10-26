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
        nodelibrary.addNode(new Coordinates(4, 6), 2);
        
    }

    @Test
    public void testLosBetweenNodesTriangle() {
        Polygon triangle = new Polygon(this.nodelibrary, 0);
        assertEquals(1, triangle.losBetweenNodes(0, 1));
        assertEquals(2, triangle.losBetweenNodes(1, 2));
        assertEquals(0, triangle.losBetweenNodes(2, 0));
        
        assertEquals(0, triangle.losBetweenNodes(1, 0));
        assertEquals(1, triangle.losBetweenNodes(2, 1));
        assertEquals(2, triangle.losBetweenNodes(0, 2));
    }
    
    @Test
    public void testLosBetweenNodesSquare() {
        Polygon square = new Polygon(this.nodelibrary, 1);
        assertEquals(1+3, square.losBetweenNodes(0+3, 1));
        assertEquals(1, square.losBetweenNodes(1+3, 2)); //old node
        assertEquals(2, square.losBetweenNodes(2+3, 3)); //old  node
        assertEquals(0+3, square.losBetweenNodes(3+3, 0));
        
        assertEquals(1, square.losBetweenNodes(0+3, 2)); //old node
        assertEquals(2, square.losBetweenNodes(1+3, 3));//old node
        
    }
    
    @Test
    public void testLosBetweenNodesComplex() {
        Polygon square = new Polygon(this.nodelibrary, 2);
        assertEquals(-1, square.losBetweenNodes(0, 3));
        assertEquals(-1, square.losBetweenNodes(0, 4));
        assertEquals(5+5, square.losBetweenNodes(2, 5));
        
    }
    
    

}
