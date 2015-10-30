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
public class CoordinatesTest {
    
    public CoordinatesTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testGetX() {
    }

    @Test
    public void testGetY() {
    }

    @Test
    public void testHashCode() {
        
        
    }

    @Test
    public void testEquals() {
        Coords c1 = new Coords(1.000000,1.000000);
        Coords c2 = new Coords(1.0,1.0001);
        Coords c3 = new Coords(1.000000,1.000000);
        
        assertEquals(c1, c3);
        assertEquals(c1, c2);
    }
    
}
