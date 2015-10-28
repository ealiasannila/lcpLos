/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import lcplos.dataStructures.Coordinates;
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
        ArrayList<Coordinates> triangle = new ArrayList<>();
        triangle.add(new Coordinates(0, 0));
        triangle.add(new Coordinates(3, 0));
        triangle.add(new Coordinates(3, 3));
        
        triangle = EdgeSplitter.splitEdges(triangle, 2);
        assertEquals(7, triangle.size());
        System.out.println(triangle);

    }

}
