/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elias
 */
public class GraphTest {

    private Graph graph;

    public GraphTest() {
    }

    @Before
    public void setUp() {

        NodeLibrary nodelibrary = new NodeLibrary(2);

        ArrayList<Coords> poly0 = new ArrayList<>();
        poly0.add(new Coords(0, 0));
        poly0.add(new Coords(0, 1));
        poly0.add(new Coords(1, 1));

        ArrayList<Coords> poly1 = new ArrayList<>();
        poly1.add(new Coords(0, 2));
        poly1.add(new Coords(0, 1));
        poly1.add(new Coords(1, 1));

        nodelibrary.addPolygon(poly0, 0);
        nodelibrary.addPolygon(poly1, 1);

        FrictionLibrary frictionlib = new FrictionLibrary();
        frictionlib.addFriction(0, 1);
        frictionlib.addFriction(1, 2);

        this.graph = new Graph(nodelibrary, frictionlib);

    }

    @Test
    public void testGetNeighbours() {
        assertEquals("{0=1.0, 2=1.0, 3=2.0}", this.graph.getNeighbours(1).toString());
    }

}
