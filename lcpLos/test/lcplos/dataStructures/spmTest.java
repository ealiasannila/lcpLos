/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eannila
 */
public class spmTest {

    private SPT spm;

    public spmTest() {
    }

    @Before
    public void setUp() {
        Coords[] channelc = new Coords[]{
            new Coords(1, 3),
            new Coords(2, 3),
            new Coords(2, 2),
            new Coords(3, 2),
            new Coords(3, 1),
            new Coords(2, 1),
            new Coords(2, 0),
            new Coords(1, 0),
            new Coords(1, 1),
            new Coords(0, 1),
            new Coords(0, 2),
            new Coords(1, 2)
        };
        
        this.spm = new SPT(0, new int[]{2, 0, 5, 5, 3, 8, 8, 6, 11, 11, 9, 2}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},channelc,1);
    }

    
    /**
     * Test of run method, of class spm.
     */
    @Test
    public void testRun() {
        this.spm.run();
        
    }

}
