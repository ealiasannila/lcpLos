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
public class EdgeSplitterTest {


    public EdgeSplitterTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testSplitEdges() {
        ArrayList<Coords> triangle = new ArrayList<>();
        triangle.add(new Coords(0, 0));
        triangle.add(new Coords(3, 0));
        triangle.add(new Coords(3, 3));
        
        triangle = EdgeSplitter.splitEdges(triangle, 2);
        assertEquals(7, triangle.size());
        System.out.println(triangle);

    }

}
