/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 *
 * @author eannila
 */
public class Spt2 {

    private int[] pred;
    private Coords[] coords;

    public Spt2(int s, Triangle[] triangles, int n, Coords[] coords) {
        this.pred = new int[n];
        this.coords = coords;
        ArrayList<Integer> sTriangles = new ArrayList<>();
        for (int i = 0; i < triangles.length; i++) {
            if (triangles[i].contains(s)) {
                sTriangles.add(i);
            }
        }

        for (Integer i : sTriangles) {
            int[] e = triangles[i].opposingEdge(s);
            this.pred[e[0]] = s;
            this.pred[e[1]] = s;
            Funnel funnel = new Funnel(e[0], s, e[1]);
            this.split(e, funnel);
        }

    }

    private boolean isPolygonEdge(int[] e) {
        return false;
    }

    private int locateOppositeVertex(int[] e) {
        return -1;
    }

    public void split(int[] e, Funnel f) {
        if (this.isPolygonEdge(e)) {
            return;
        }
        int v = this.locateOppositeVertex(e);

        ArrayDeque<Integer> sf1 = new ArrayDeque<>();
        int sChannel = f.locatePred(v, this.coords, sf1);
        int t = sf1.getFirst();

        ArrayDeque<Integer> sf2 = new ArrayDeque<>();
        sf2.addLast(t);
        sf2.addLast(v);

        
        
        int[] e1 = new int[]{e[0], v};
        int[] e2 = new int[]{v, e[1]};
        if (sChannel == 1) { //suffix in right channel
            Funnel suffix = new Funnel(sf2, sf1);
            this.split(e1, f);
            this.split(e2, suffix);
        } else {
            Funnel suffix = new Funnel(sf1, sf2);
            this.split(e1, suffix);
            this.split(e2, f);
        }

    }
}
