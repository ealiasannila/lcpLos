/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
import logic.HelperFunctions;

/**
 *
 * @author eannila
 */
public class Funnel {

    private ArrayDeque<Integer> l;
    private ArrayDeque<Integer> r;
    private VertexLib vlib;

    public Funnel(int l, int a, int r, VertexLib vlib) {
        this.l = new ArrayDeque< Integer>();
        this.r = new ArrayDeque<Integer>();
        this.Add(a);
        this.Add(l, r);
        this.vlib = vlib;

    }

    public Edge getBase() {
        return new Edge(this.l.peekLast(), this.r.peekLast());
    }

    public int getApex() {
        return this.l.peekFirst();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.getApex());
        hash = 41 * hash + Objects.hashCode(this.getBase().getL());
        hash = 41 * hash + Objects.hashCode(this.getBase().getR());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Funnel other = (Funnel) obj;
        if (this.getApex() != other.getApex()) {
            return false;
        }
        if (this.getBase().getL() == other.getBase().getL()) {
            return false;
        }
        if (this.getBase().getR() == other.getBase().getR()) {
            return false;
        }
        return true;
    }

    public Funnel(ArrayDeque<Integer> l, ArrayDeque<Integer> r, VertexLib vlib) {
        this.l = l;
        this.r = r;
        this.vlib = vlib;
    }

    public void Add(int l, int r) {
        this.l.addLast(l);
        this.r.addLast(r);
    }

    public void Add(int a) {
        this.l.addLast(a);
        this.r.addLast(a);
    }

    public ArrayDeque<Integer> splitLeftChannel(int v) {
        return this.splitChannel(v, l, -1);
    }

    public ArrayDeque<Integer> splitRightChannel(int v) {
        return this.splitChannel(v, r, 1);
    }

    private ArrayDeque<Integer> splitChannel(int v, ArrayDeque<Integer> c, int orient) {
        ArrayDeque<Integer> newChannel = new ArrayDeque<>();
        int apex = c.peekFirst();

        while (!c.isEmpty()) {
            int t = c.pollLast();
            newChannel.addFirst(t);

            if (t == apex) {
                c.addLast(t);
                c.addLast(v);
                return newChannel;
            }

            int tOrient = HelperFunctions.isRight(c.peekLast(), t, v, this.vlib);
            if (orient != -tOrient) {
                c.addLast(t);
                c.addLast(v);
                return newChannel;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Funnel{" + "l=" + l + ", r=" + r + '}';
    }

    public boolean lIsEmpty() {
        return this.l.size() == 1;
    }

    public boolean rIsEmpty() {
        return this.l.size() == 1;
    }

    public int inRightChannel(int v) {
        int apex = l.pollFirst();
        r.pollFirst();
        int lf = l.peekFirst();
        int rf = r.peekFirst();
        r.addFirst(apex);
        l.addFirst(apex);
        int l1 = HelperFunctions.isRight(apex, lf, v, this.vlib);
        int r1 = HelperFunctions.isRight(apex, rf, v, this.vlib);

        if (l1 == 1 && r1 == -1) {
            return 0;
        } else if (l1 == 1) {
            return 1;
        } else if (r1 == -1) {
            return -1;
        }
        System.out.println("somewierdshit");
        return -99;

    }

}
