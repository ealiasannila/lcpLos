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
import lcplos.dataStructures.VertexLib;
import logic.HelperFunctions;

/**
 *
 * @author eannila
 */
public class Spt2 {

    private List<Integer> neighbours;
    private EdgeLocator polygon;
    private List<Sector> uncharted;
    private ArrayDeque<Funnel> splitQueue;
    private int startVertex;
    private VertexLib vlib;

    public Spt2(int s, EdgeLocator polygon, VertexLib vlib) {

        this.splitQueue = new ArrayDeque<Funnel>();
        this.uncharted = new ArrayList<Sector>();
        this.polygon = polygon;
        this.startVertex = s;
        this.neighbours = new ArrayList<Integer>();
        this.vlib = vlib;

        int secr = -1;
        int secl = -1;

        if (this.polygon.getOpposingEdge(this.startVertex) == null) {
            System.out.println("s: " + this.startVertex);
            return;
        }
        if (this.polygon.getOpposingEdge(this.startVertex).isEmpty()) {
            System.out.println("s: " + this.startVertex + "e");
            return;
        }
        for (Edge e : this.polygon.getOpposingEdge(this.startVertex)) {
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
            }
            if (this.polygon.isPolygonEdge(new Edge(r, this.startVertex))) {
                secr = r;
            }
            if (this.polygon.isPolygonEdge(new Edge(l, this.startVertex))) {
                secl = l;
            }
            Funnel funnel = new Funnel(l, this.startVertex, r, this.vlib);
            this.splitQueue.addLast(funnel);

        }

        if (secl != -1 && secr != -1) {

            this.uncharted.add(new Sector(this.startVertex, secl, secr, this.vlib));
        } else {

            int neighbour = this.polygon.getOpposingEdge(this.startVertex).iterator().next().getL();
            Sector sector = new Sector(this.startVertex, neighbour, neighbour, this.vlib);
            System.out.println("sector!!! " + sector);
            this.uncharted.add(sector);
        }
        while (!this.splitQueue.isEmpty()) {
            //System.out.println(this.splitQueue.peekFirst());
            this.split(this.splitQueue.pollFirst());
        }

    }

    private boolean isPolygonEdge(Edge e) {
        return this.polygon.isPolygonEdge(e);
    }

    private int locateOppositeVertex(Edge e, int apex) {
        Edge cross = this.polygon.locateCrossingEdge(e);

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

        this.splitQueue.addLast(suffix);

    }

    //UPDATE TO USING BINARY SEARCH
    private boolean adjustSectors(Edge e, boolean polygonEdge) {
       
        boolean debug = false;
        Sector previous = null;
        for (Iterator<Sector> it = this.uncharted.iterator(); it.hasNext();) {
            if (e.getL() == 6634 && e.getR() == 6633) {
                debug = true;
                System.out.println("uncharted: " + this.uncharted);
            }else {
                debug = false;
            }
            if (debug) {
                System.out.println("doing something");
            }
            Sector sector = it.next();
            if (debug) {
                System.out.println("sector: " + sector);
            }
            if (sector.outside(e)) {
                if (!it.hasNext()) {
                    if (debug) {
                        System.out.println("edge: " + e);
                        System.out.println("outside no next");
                    }
                    return false;
                }
                previous = sector;

                continue;
            }

            if (sector.inside(e.getL()) && sector.inside(e.getR()) && polygonEdge) {
                
                if (HelperFunctions.isRight(sector.getApex(), e.getL(), e.getR(), vlib) == 1) {
                    Sector sr = new Sector(sector.getApex(), e.getR(), sector.getR(), vlib);
                    
                    this.uncharted.add(this.uncharted.indexOf(sector) + 1, sr);
                    sector.setR(e.getL());

                } else {
                    if (debug) {
                        System.out.println("some wierd coming from wrong side inside sector");
                    }
                }
                return false;
            } else if (sector.inside(e.getL()) && polygonEdge) {
                if (HelperFunctions.isRight(sector.getApex(), sector.getR(), e.getR(), vlib) != -1) {
                    //edge R outside sector R
                    sector.setR(e.getL());
                    if (it.hasNext()) {
                        Sector next = it.next();
                        if (next.inside(sector.getR())) {
                            sector.setR(next.getR());
                            it.remove();
                        }
                    }

                } else if (HelperFunctions.isRight(sector.getApex(), sector.getL(), e.getR(), vlib) == -1) {
                    //edge R outside sector L                    
                    //is this even possible?
                    if (true) {
                        System.out.println("sec: " + sector);
                        System.out.println("edg: " + e);
                        System.out.println("coming from wrong side!");
                        System.out.println("sL: " + sector.getL() + "->" + e.getL());
                        sector.setL(e.getL());
                        if (e.getR() == -1) {
                            System.out.println("setting -1");
                        }

                    }
                } else {
                    if (debug) {
                        System.out.println("some wierd at sector edge L");
                        System.out.println(e + " " + sector);
                    }
                }
                return false; //return because base = polygonEdge
            } else if (sector.inside(e.getR()) && polygonEdge) {
                if (HelperFunctions.isRight(sector.getApex(), sector.getR(), e.getL(), vlib) == 1) {
                    //edge L outside sector R
                    //is this even possible?
                    if (debug) {
                        System.out.println("coming from wrong side yea!");
                        System.out.println("sR: " + sector.getR() + "->" + e.getR());
                    }

                    sector.setR(e.getR());
                } else if (HelperFunctions.isRight(sector.getApex(), sector.getL(), e.getL(), vlib) != 1) {
                    //edge L outside sector L  
                    sector.setL(e.getR());

                    if (previous != null) {
                        if (previous.inside(sector.getL())) {
                            previous.setR(sector.getR());
                            it.remove();
                        }
                    }
                } else {
                    if (debug) {
                        System.out.println("some wierd at sector edge R");
                        System.out.println(e + " " + sector);
                    }

                }

                return false; //return because base = polygonEdge
            }
            return true;
        }
        return true;
    }

    private void split(Funnel f) {
        boolean debug = false;
        Edge e = f.getBase();
        if (e.getL() == 6633 && e.getR() == 6640) {
            debug = true;
        }
        if (debug) {
            System.out.println("is polyedge: " + e + this.isPolygonEdge(e));
        }
        boolean polygonEdge = this.isPolygonEdge(e);
        if (!this.adjustSectors(e, polygonEdge)) {
            if (debug) {
                System.out.println("returning from adjustsectors");
            }
            return;
        }
        if (polygonEdge) {
            return;
        }

        int v = this.locateOppositeVertex(e, f.getApex());
        ArrayDeque<Integer> suffixOuter;
        int sChannel = f.inRightChannel(v);
        if (sChannel == -1) {
            suffixOuter = f.splitLeftChannel(v);
        } else {
            suffixOuter = f.splitRightChannel(v);
        }

        int t = suffixOuter.peekFirst();
        if (t == this.startVertex) {
            this.neighbours.add(v);
        }
        this.splitQueue.addLast(f);
        this.splitSuffix(t, v, f, suffixOuter, sChannel);

    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public List<Sector> getUncharted() {
        return uncharted;
    }

}
