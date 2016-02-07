/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import lcplos.dataStructures.EdgeLocator;
import lcplos.dataStructures.Funnel;
import lcplos.dataStructures.Edge;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
import logic.HelperFunctions;

/**
 *
 * @author eannila
 */
public class Spt2 {

    private List<Integer> neighbours;
    private EdgeLocator locator;
    private ArrayDeque<Funnel> splitQueue;
    private int startVertex;
    private VertexLib vlib;
    //private Charter charter;

    private boolean debug;

    public Spt2(int s, EdgeLocator polygon, VertexLib vlib) {
        this.debug = false;

        this.splitQueue = new ArrayDeque<Funnel>();
        //  this.charter = new Charter(s, vlib);
        this.locator = polygon;
        this.startVertex = s;
        this.neighbours = new ArrayList<Integer>();
        this.vlib = vlib;

        // int secr = -1;
        // int secl = -1;
        if (this.locator.getOpposingEdge(this.startVertex) == null) {
            System.out.println("s: " + this.startVertex);
            return;
        }
        if (this.locator.getOpposingEdge(this.startVertex).isEmpty()) {
            System.out.println("s: " + this.startVertex + "e");
            return;
        }
        for (Edge e : this.locator.getOpposingEdge(this.startVertex)) {
            this.neighbours.add(e.getL());
            this.neighbours.add(e.getR());

            int l;
            int r;

            if (HelperFunctions.isRight(this.startVertex, e.getL(), e.getR(), this.vlib) == -1) {
                l = e.getR();
                r = e.getL();
            } else {
                l = e.getL();
                r = e.getR();
            }/*
             if (this.locator.isPolygonEdge(new Edge(r, this.startVertex))) {
             secr = r;
             }
             if (this.locator.isPolygonEdge(new Edge(l, this.startVertex))) {
             secl = l;
             }*/

            Funnel funnel = new Funnel(l, this.startVertex, r, this.vlib);
            this.splitQueue.addLast(funnel);

        }
        /*
         if (secl != -1 && secr != -1) {
         this.charter.addFence(new Edge(secl, secr));
         }*/
        while (!this.splitQueue.isEmpty()) {
            this.split(this.splitQueue.pollFirst());
        }

    }

    private boolean isPolygonEdge(Edge e) {
        return this.locator.isPolygonEdge(e);
    }

    private int locateOppositeVertex(Edge e, int apex) {
        Edge cross = this.locator.locateCrossingEdge(e);

        if (HelperFunctions.isRight(e.getL(), e.getR(), apex, vlib)
                == -HelperFunctions.isRight(e.getL(), e.getR(), cross.getL(), vlib)) {
            return cross.getL();
        } else {
            return cross.getR();
        }
    }

    private void splitSuffix(int t, int v, Funnel f, ArrayDeque<Integer> suffixOuter, int sChannel) {
        if (suffixOuter.size() == 1) {
            return;
        }

        ArrayDeque<Integer> suffixInner = new ArrayDeque<>();
        suffixInner.addLast(t);
        suffixInner.addLast(v);

        Funnel suffix;

        if (sChannel == -1) { //suffix in left channel
            suffix = new Funnel(suffixOuter, suffixInner, this.vlib);
        } else {
            suffix = new Funnel(suffixInner, suffixOuter, this.vlib);
        }
        if (suffix.getApex() == this.startVertex) {
            this.splitQueue.addLast(suffix);
        }

    }

    //UPDATE TO USING BINARY SEARCH
    private void split(Funnel f) {
        
        Edge e = f.getBase();
        /*
         if (!this.charter.hopeless(e)) {
         return;
         }
         */

        if (this.isPolygonEdge(e)) {
            //     this.charter.addFence(e);
            return;
        }

        int v = this.locateOppositeVertex(e, f.getApex());
        ArrayDeque<Integer> suffixOuter;
        int sChannel = f.inRightChannel(v);
        if (sChannel == -99) {
            //funnel is infinately narrow
            return;
        }
        
        if (sChannel == -1) {
            suffixOuter = f.splitLeftChannel(v);
        } else {
            suffixOuter = f.splitRightChannel(v);
        }
        if(suffixOuter==null){//getting into a loop
            return;
        }
        int t = suffixOuter.peekFirst();
        if (t == this.startVertex) {
            this.neighbours.add(v);
        }
        if (f.getApex() == this.startVertex) {
            this.splitQueue.addLast(f);
            this.splitSuffix(t, v, f, suffixOuter, sChannel);

        }

    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

}
