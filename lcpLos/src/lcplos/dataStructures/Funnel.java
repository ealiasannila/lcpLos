/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayDeque;
import java.util.ArrayList;
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

    public ArrayDeque<Integer> getL() {
        return l;
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

    private void predInChannel(int v, Coords[] coords, ArrayDeque<Integer> c, ArrayDeque<Integer> newChannel, int orient) {

        while (!c.isEmpty()) {
            int t = c.pollLast();
            int tOrient = HelperFunctions.isRight(c.peekFirst(), t, v, coords);

            newChannel.addFirst(t);
            if (orient == tOrient) {
                newChannel.addFirst(t);
                c.addLast(t);
                return;
            }
        }
    }

    public int locatePred(int v, Coords[] coords, ArrayDeque newChannel) {
        int apex = l.pollFirst();
        r.pollFirst();

        int l1 = HelperFunctions.isRight(apex, this.l.peekFirst(), v, coords);
        int r1 = HelperFunctions.isRight(apex, this.r.peekFirst(), v, coords);
        l.addFirst(apex);
        r.addFirst(apex);
        if (l1 != 1) {
            this.predInChannel(v, coords, l, newChannel, -1);
            return -1;
        }
        if (r1 != -1) {
            this.predInChannel(v, coords, r, newChannel, 1);
            return 1;
        } else {
            newChannel = this.l;
            
            this.l = new ArrayDeque<Integer>();
            l.add(apex);
            l.add(v);

            return 0;
        }
    }
}
