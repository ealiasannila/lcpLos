/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private Map<Integer, Integer> pred;
    private PolygonOma polygon;
    private Coords[] coords;
    private List<Sector> uncharted;
    private ArrayDeque<Funnel> splitQueue;

    public Spt2(int s, Coords[] coords, PolygonOma polygon) {
        this.splitQueue = new ArrayDeque<Funnel>();
        this.pred = new TreeMap<Integer, Integer>();
        this.uncharted = new ArrayList<Sector>();

        this.polygon = polygon;
        this.coords = coords;

        this.pred.put(s, -1);

        int ln = HelperFunctions.moveLeft(s, coords.length - 1, 1); //UPDATE TO USE REAL POLY ORIENTATION
        int rn = HelperFunctions.moveRight(s, coords.length - 1, 1);//UPDATE TO USE REAL POLY ORIENTATION

        this.uncharted.add(new Sector(s, ln, rn, coords));

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
            this.splitQueue.addLast(funnel);
        }
        while (!this.splitQueue.isEmpty()) {
            this.split(this.splitQueue.pollFirst());
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
            return;
            /* Edge tv = new Edge(t, v);
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
             */

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

        this.splitQueue.addLast(suffix);

    }

    private boolean adjustSectors(Edge e, boolean polygonEdge) {
        Sector previous = null;
        for (Iterator<Sector> it = this.uncharted.iterator(); it.hasNext();) {

            Sector sector = it.next();
            if (sector.outside(e)) {
                if (!it.hasNext()) {
                    System.out.println("not inside: " + e);
                    return false;
                }
                previous = sector;

                continue;
            }

            if (sector.inside(e.getL()) && sector.inside(e.getR()) && polygonEdge) {
                if (HelperFunctions.isRight(sector.getApex(), e.getL(), e.getR(), coords) == 1) {
                    Sector sr = new Sector(sector.getApex(), e.getR(), sector.getR(), coords);
                    this.uncharted.add(this.uncharted.indexOf(sector) + 1, sr);
                    sector.setR(e.getL());

                } else {
                    System.out.println("some wierd coming from wrong side inside sector");
                }
                return false;
            } else if (sector.inside(e.getL()) && polygonEdge) {
                if (HelperFunctions.isRight(sector.getApex(), sector.getR(), e.getR(), coords) != -1) {
                    //edge R outside sector R
                    System.out.println("sL: " + sector.getL() + "->" + e.getL());
                    sector.setR(e.getL());
                    if (it.hasNext()) {
                        Sector next = it.next();
                        if (next.inside(sector.getR())) {
                            sector.setR(next.getR());
                            it.remove();
                        }
                    }

                } else if (HelperFunctions.isRight(sector.getApex(), sector.getL(), e.getR(), coords) != 1) {
                    //edge R outside sector L                    
                    //is this even possible?
                    System.out.println("coming from wrong side!");
                    System.out.println("sL: " + sector.getL() + "->" + e.getL());
                    sector.setL(e.getL());

                } else {
                    System.out.println("some wierd at sector edge L");
                    System.out.println(e + " " + sector);
                }
                return false; //return because base = polygonEdge
            } else if (sector.inside(e.getR()) && polygonEdge) {
                if (HelperFunctions.isRight(sector.getApex(), sector.getR(), e.getL(), coords) != -1) {
                    //edge L outside sector R
                    //is this even possible?
                    System.out.println("coming from wrong side!");
                    System.out.println("sR: " + sector.getR() + "->" + e.getR());

                    sector.setR(e.getR());
                } else if (HelperFunctions.isRight(sector.getApex(), sector.getL(), e.getL(), coords) != 1) {
                    //edge L outside sector L  
                    System.out.println("sR: " + sector.getR() + "->" + e.getR());
                    sector.setL(e.getR());
                    if (previous != null) {
                        if (previous.inside(sector.getL())) {
                            previous.setR(sector.getR());
                            it.remove();
                        }
                    }
                } else {
                    System.out.println("some wierd at sector edge R");
                    System.out.println(e + " " + sector);

                }

                return false; //return because base = polygonEdge
            }
            return true;
        }
        return true;
    }

    private void split(Funnel f) {
        System.out.println("");
        System.out.println(this.uncharted);

        Edge e = f.getBase();
        System.out.println(e);
        boolean polygonEdge = this.isPolygonEdge(e);
        if (!this.adjustSectors(e, polygonEdge)) {
            return;
        }
        if (polygonEdge) {
            return;
        }

        int v = this.locateOppositeVertex(e, f.getApex());
        ArrayDeque<Integer> suffixOuter;
        System.out.println("pv: " + v);
        int sChannel = f.inRightChannel(v, coords);
        if (sChannel == -1) {
            suffixOuter = f.splitLeftChannel(v, coords);
        } else {
            suffixOuter = f.splitRightChannel(v, coords);
        }

        int t = suffixOuter.peekFirst();
        this.pred.put(v, t);
        this.splitQueue.addLast(f);
        this.splitSuffix(t, v, f, suffixOuter, sChannel);

    }

    public Map<Integer, Integer> getPred() {
        return pred;
    }

    public List<Sector> getUncharted() {
        return uncharted;
    }

}
