/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lcplos.dataStructures.Coords;
import logic.HelperFunctions;

/**
 *
 * @author eannila
 */
public class Spt2 {

    private Map<Integer,Integer> pred;
    private PolygonOma polygon;
    private Coords[] coords;

    public Spt2(int s, Coords[] coords, PolygonOma polygon) {
        this.pred = new TreeMap<Integer, Integer>();
        
        this.polygon = polygon;
        this.coords = coords;

        this.pred.put(s, -1);
        
        for (Edge e : this.polygon.getOpposingEdge(s)) {
            
            this.pred.put(e.getL(), s);
            this.pred.put(e.getR(), s);
            
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

    private void splitSuffix(int t, int v, Funnel f, ArrayDeque<Integer> suffixOuter, int sChannel) {
        if (suffixOuter.size() == 1) {

            Edge tv = new Edge(t, v);
            if (this.polygon.isPolygonEdge(tv)) {
                return;
            }
            Edge cross = this.polygon.locateCrossingEdge(tv);
            int l;
            int r;
            if (HelperFunctions.isRight(t, v, cross.getR(), coords) == 1) {
                l = cross.getL();
                r = cross.getR();
            } else {
                l = cross.getR();
                r = cross.getL();
            }

            if (sChannel == -1) {
                this.splitSuffix(t, l, f, suffixOuter, sChannel);
                suffixOuter.addLast(l);
                this.pred.put(l, suffixOuter.peekFirst());
            } else {
                this.splitSuffix(t, r, f, suffixOuter, sChannel);
                suffixOuter.addLast(r);
                this.pred.put(r, suffixOuter.peekFirst());
            }

        }

        ArrayDeque<Integer> suffixInner = new ArrayDeque<>();
        suffixInner.addLast(t);
        suffixInner.addLast(v);

        Funnel suffix;

        if (sChannel == -1) { //suffix in left channel
            suffix = new Funnel(suffixOuter, suffixInner);
        } else {
            suffix = new Funnel(suffixInner, suffixOuter);
        }

        this.split(suffix);

    }

    private void split(Funnel f) {
    Edge e = f.getBase();

        if (this.isPolygonEdge(e)) {
            return;
        }
        int v = this.locateOppositeVertex(e, f.getApex());
        ArrayDeque<Integer> suffixOuter;
        int sChannel = f.inRightChannel(v, coords);
        if (sChannel == -1) {
            suffixOuter = f.splitLeftChannel(v, coords);
        } else {
            suffixOuter = f.splitRightChannel(v, coords);
        }

        int t = suffixOuter.peekFirst();
        this.pred.put(v, t);
        this.split(f);
        this.splitSuffix(t, v, f, suffixOuter, sChannel);

    }

    public Map<Integer,Integer> getPred() {
        return pred;
    }

}
