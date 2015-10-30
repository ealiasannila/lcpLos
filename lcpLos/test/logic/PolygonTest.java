/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.NodeLibrary;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elias
 */
public class PolygonTest {

    private NodeLibrary nodelib;

    public PolygonTest() {

    }

    @Before
    public void setUp() {
         nodelib= new NodeLibrary(5);

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
        poly2.add(new Coords(6, 4));
        
        nodelib.addPolygon(poly0, 0);
        nodelib.addPolygon(poly1, 1);
        nodelib.addPolygon(poly2, 2);

    }

    @Test
    public void testLosBetweenNodesTriangle() {
        int o = LosChecker.polyOrientation(0,nodelib );
        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 1, this.nodelib, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 2, this.nodelib, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 0, this.nodelib, 0));

        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 0, this.nodelib, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 1, this.nodelib, 0));
        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 2, this.nodelib, 0));
    }

    @Test
    public void testLosBetweenNodesSquare() {
        int o = LosChecker.polyOrientation(1, nodelib);

        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 2, this.nodelib, 1));
        assertEquals(true, LosChecker.losBetweenNodes(o, 0, 1, this.nodelib, 1)); //old node
        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 3, this.nodelib, 1)); //old  node
        assertEquals(true, LosChecker.losBetweenNodes(o, 2, 3, this.nodelib, 1)); //old  node
        assertEquals(true, LosChecker.losBetweenNodes(o, 3, 2, this.nodelib, 1));
        assertEquals(true, LosChecker.losBetweenNodes(o, 1, 2, this.nodelib, 1)); //old node

    }

    @Test
    public void testLosBetweenNodesComplex() {
        int o = LosChecker.polyOrientation(2,nodelib );

        assertEquals(false, LosChecker.losBetweenNodes(o, 0,  4, this.nodelib, 2));
        assertEquals(false, LosChecker.losBetweenNodes(o, 0,  3, this.nodelib, 2));
        assertEquals(false, LosChecker.losBetweenNodes(o, 3,  0, this.nodelib, 2));
        assertEquals(false, LosChecker.losBetweenNodes(o, 4,  1, this.nodelib, 2));

        assertEquals(true, LosChecker.losBetweenNodes(o, 2,  0, this.nodelib, 2));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2,  5, this.nodelib, 2));
        assertEquals(true, LosChecker.losBetweenNodes(o, 2,  1, this.nodelib, 2));

        assertEquals(true, LosChecker.losBetweenNodes(o, 5,  0, this.nodelib, 2));

    }

    @Test
    public void testSample() {
        assertEquals(true, LosChecker.sample(5, 9,nodelib , 2, 3));
        assertEquals(false, LosChecker.sample(6, 3,nodelib , 2, 3));
        assertEquals(true, LosChecker.sample(5, 6,nodelib , 2, 3));
        assertEquals(true, LosChecker.sample(7, 10,nodelib , 2, 3));
    }

    @Test
    public void testPolyOrientation() {
        assertEquals(1, LosChecker.polyOrientation(0,nodelib ));
        assertEquals(1, LosChecker.polyOrientation(0,nodelib ));
    }

}
