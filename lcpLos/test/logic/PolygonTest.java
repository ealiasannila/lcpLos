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
        nodelibrary = new NodeLibrary(5);

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
        int o = LosChecker.polyOrientation(0, nodelibrary);
        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 0, 1, this.nodelibrary, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 1, 2, this.nodelibrary, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 2, 0, this.nodelibrary, 0));

        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 1, 0, this.nodelibrary, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 2, 1, this.nodelibrary, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 0, 2, this.nodelibrary, 0));
    }

    @Test
    public void testLosBetweenNodesSquare() {
        int o = LosChecker.polyOrientation(1, nodelibrary);

        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 3, 1, this.nodelibrary, 1));
        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 3, 4, this.nodelibrary, 1)); //old node
        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 4, 2, this.nodelibrary, 1)); //old  node
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 1, 2, this.nodelibrary, 1)); //old  node
        assertEquals(true, LosChecker.losBetweenNodes(o, 3, 2, 1, this.nodelibrary, 1));
        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 4, 1, this.nodelibrary, 1)); //old node

    }

    @Test
    public void testLosBetweenNodesComplex() {
        int o = LosChecker.polyOrientation(2, nodelibrary);

        assertEquals(false, LosChecker.losBetweenNodes(o, 0, 5, 9, this.nodelibrary, 2));
        assertEquals(false, LosChecker.losBetweenNodes(o, 0, 5, 8, this.nodelibrary, 2));
        assertEquals(false, LosChecker.losBetweenNodes(o, 3, 8, 5, this.nodelibrary, 2));
        assertEquals(false, LosChecker.losBetweenNodes(o, 4, 9, 6, this.nodelibrary, 2));

        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 7, 5, this.nodelibrary, 2));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 7, 10, this.nodelibrary, 2));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 7, 6, this.nodelibrary, 2));

        assertEquals(true, LosChecker.losBetweenNodes(o, 5, 10, 5, this.nodelibrary, 2));

    }

    @Test
    public void testSample() {
        assertEquals(true, LosChecker.sample(5, 9, nodelibrary, 2, 3));
        assertEquals(false, LosChecker.sample(6, 3, nodelibrary, 2, 3));
        assertEquals(true, LosChecker.sample(5, 6, nodelibrary, 2, 3));
        assertEquals(true, LosChecker.sample(7, 10, nodelibrary, 2, 3));
    }

    @Test
    public void testPolyOrientation() {
        assertEquals(1, LosChecker.polyOrientation(0, nodelibrary));
        assertEquals(1, LosChecker.polyOrientation(0, nodelibrary));
    }

}
