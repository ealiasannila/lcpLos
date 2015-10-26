/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elias
 */
public class NodeLibraryTest {

    private NodeLibrary nodelibrary;

    public NodeLibraryTest() {
    }

    @Before
    public void setUp() {
        nodelibrary = new NodeLibrary(10, 10);

        this.nodelibrary.addNode(new Coordinates(0, 0), 0);
        this.nodelibrary.addNode(new Coordinates(0, 1), 0);
        this.nodelibrary.addNode(new Coordinates(0, 2), 0);

        this.nodelibrary.addNode(new Coordinates(0, 3), 1);
        this.nodelibrary.addNode(new Coordinates(0, 1), 1);
        this.nodelibrary.addNode(new Coordinates(0, 2), 1);
    }

    /**
     * Test of getNodes method, of class NodeLibrary.
     */
    @Test
    public void testGetNodes() {
        assertEquals("[0, 1, 2]", this.nodelibrary.getNodes(0).toString());
        assertEquals("[3, 1, 2]", this.nodelibrary.getNodes(1).toString());
    }

    /**
     * Test of getPolygons method, of class NodeLibrary.
     */
    @Test
    public void testGetPolygons() {
        assertEquals("[0]", this.nodelibrary.getPolygons(0).toString());
        assertEquals("[0, 1]", this.nodelibrary.getPolygons(1).toString());
        assertEquals("[0, 1]", this.nodelibrary.getPolygons(2).toString());
        assertEquals("[1]", this.nodelibrary.getPolygons(3).toString());
    }

}
