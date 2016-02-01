/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import lcplos.dataStructures.Coords;

/**
 *
 * @author elias
 */
public class CoordEdge {
    private Coords l;
    private Coords r;

    public CoordEdge(Coords l, Coords r) {
        this.l = l;
        this.r = r;
    }

    public Coords getL() {
        return l;
    }

    public Coords getR() {
        return r;
    }
    
    
    
}
