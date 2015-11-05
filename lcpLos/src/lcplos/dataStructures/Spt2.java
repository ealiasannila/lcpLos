/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Set;
import logic.HelperFunctions;

/**
 *
 * @author eannila
 */
public class Spt2 {

    private int[] pred;
    private Polygon polygon;
    private Coords[] coords;

    public Spt2(int s, int n, Coords[] coords, Polygon polygon) {
        this.pred = new int[n];
        for (int i = 0; i < this.pred.length; i++) {
            pred[i] = -1;
        }
        this.polygon = polygon;
        this.coords = coords;

        for (Edge e : this.polygon.getOpposingEdge(s)) {
            this.pred[e.getR()] = s;
            this.pred[e.getL()] = s;

            int l;
            int r;

            if (HelperFunctions.isRight(s, e.getL(), e.getR(), coords) == -1) {
                l = e.getR();
                r = e.getL();
            } else {
                l = e.getL();
                r = e.getR();
            }

            Funnel funnel = new Funnel(l, s, r);
            this.split(funnel);
        }

    }

    private boolean isPolygonEdge(Edge e) {
        return this.polygon.isPolygonEdge(e);
    }

    private int locateOppositeVertex(Edge e, int apex) {
        Edge cross = this.polygon.locateCrossingEdge(e);
        if (HelperFunctions.isRight(e.getL(), e.getR(), apex, this.coords) == -HelperFunctions.isRight(e.getL(), e.getR(), cross.getL(), this.coords)) {
            return cross.getL();
        } else {
            return cross.getR();
        }
    }

    private void restart(int apex, int v, int orient) {
        System.out.println("restart, v: " + v + " a: " + apex + " o: " + orient);
        Set<Edge> opposingEdge = this.polygon.getOpposingEdge(apex);
        Edge cross = this.polygon.locateCrossingEdge(new Edge(apex, v));
        if (cross == null) {
            return;
        }
        if ((cross.getR() == -1) || (cross.getL() == -1)) {
            return;
        }

        int cl;
        int cr;
        System.out.println("cr: " + cross);
        if (HelperFunctions.isRight(apex, v, cross.getR(), coords) == 1) {
            cl = cross.getL();
            cr = cross.getR();
        } else {
            cl = cross.getR();
            cr = cross.getL();
        }

        int l;
        int r;
        if (orient == -1) {
            l = cl;
            r = v;
        } else {
            l = v;
            r = cr;
        }
        if (!opposingEdge.contains(new Edge(l, r))) {
            return;
        }

        Funnel funnel = new Funnel(l, apex, r);
        System.out.println("respred: " + l + " to " + apex);
        System.out.println("respred: " + r + " to " + apex);

        this.pred[l] = apex;
        this.pred[l] = apex;
        this.split(funnel);
        if (orient == -1) {
            restart(apex, l, orient);
        } else {
            restart(apex, r, orient);
        }
    }

    private void split(Funnel f) {
        System.out.println("---split---");
        System.out.println(f);
        Edge e = f.getBase();

        if (this.isPolygonEdge(e)) {
            return;
        }
        int v = this.locateOppositeVertex(e, f.getApex());
        System.out.println("v: " + v);

        ArrayDeque<Integer> sf1 = new ArrayDeque<>();
        int sChannel = f.locatePred(v, this.coords, sf1);
        System.out.println("sf1_ " + sf1);
        int t = sf1.peekFirst();
        System.out.println("t: " + t);
        System.out.println("asspred: " + v + " to " + t);
        this.pred[v] = t;

        ArrayDeque<Integer> sf2 = new ArrayDeque<>();
        sf2.addLast(t);
        sf2.addLast(v);

        Edge e1 = new Edge(e.getL(), v);
        Edge e2 = new Edge(v, e.getR());
        System.out.println("e1: " + e1);
        System.out.println("e2: " + e2);
        Funnel suffix;

        if (sChannel == 1) { //suffix in right channel
            suffix = new Funnel(sf2, sf1);
        } else {
            suffix = new Funnel(sf1, sf2);
        }
        System.out.println("s: " + suffix);
        System.out.println("f: " + f);
        if (suffix.lIsEmpty()) {
            this.restart(suffix.getApex(), v, HelperFunctions.isRight(f.getApex(), suffix.getApex(), v, coords));
        } else if (suffix.rIsEmpty()) {
            this.restart(suffix.getApex(), v, HelperFunctions.isRight(f.getApex(), suffix.getApex(), v, coords));
        } else {
            this.split(suffix);
        }
        this.split(f);

    }

    public int[] getPred() {
        return pred;
    }

}
