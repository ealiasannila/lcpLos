/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import lcplos.dataStructures.Coords;
import logic.HelperFunctions;

/**
 *
 * @author eannila
 */
public class Funnel {

    private ArrayDeque<Integer> l;
    private ArrayDeque<Integer> r;

    public Funnel(int l, int a, int r) {
        this.l = new ArrayDeque< Integer>();
        this.r = new ArrayDeque<Integer>();
        this.Add(a);
        this.Add(l, r);

    }

    public Edge getBase() {
        return new Edge(this.l.peekLast(), this.r.peekLast());
    }

    public int getApex() {
        return this.l.peekFirst();
    }

    public Funnel(ArrayDeque<Integer> l, ArrayDeque<Integer> r) {
        this.l = l;
        this.r = r;
    }

    public void Add(int l, int r) {
        this.l.addLast(l);
        this.r.addLast(r);
    }

    public void Add(int a) {
        this.l.addLast(a);
        this.r.addLast(a);
    }

    public ArrayDeque<Integer> splitLeftChannel(int v, Coords[] coords) {
        return this.splitChannel(v, coords, l, -1);
    }
    
    public ArrayDeque<Integer> splitRightChannel(int v, Coords[] coords) {
        return this.splitChannel(v, coords, r, 1);
    }
    
    private ArrayDeque<Integer> splitChannel(int v, Coords[] coords, ArrayDeque<Integer> c, int orient) {
        ArrayDeque<Integer> newChannel = new ArrayDeque<>();
        int apex = c.peekFirst();
        
        while (!c.isEmpty()) {
            int t = c.pollLast();
            newChannel.addFirst(t);
            
            if(t==apex){
                c.addLast(t);
                c.addLast(v);
                return newChannel;
            }
            
            int tOrient = HelperFunctions.isRight(c.peekLast(), t, v, coords);
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

    public int inRightChannel(int v, Coords[] coords){
        int apex = l.pollFirst();
        r.pollFirst();
        int lf = l.peekFirst();
        int rf = r.peekFirst();
        r.addFirst(apex);
        l.addFirst(apex);
        int l1 = HelperFunctions.isRight(apex, lf, v, coords);
        int r1 = HelperFunctions.isRight(apex, rf, v, coords);
        
        if(l1==1 && r1 == -1){
            return 0;
        }else if(l1 == 1 ){
            return 1;
        }else if(r1 == -1){
            return -1;
        }
        System.out.println("somewierdshit");
        return -99;
        
        
    }
    
    
}
