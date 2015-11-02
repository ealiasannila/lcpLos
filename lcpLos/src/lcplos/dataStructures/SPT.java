/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eannila
 */
public class SPT {

    private int start;
    private Coords[] channelC;
    private int[] path;
    private ArrayDeque<int[]> tripath;
    private Triangle[] triangles;
    private boolean[] usedTri;
    private int co;
    private int end;

    public SPT(int start, int end, Triangle[] triangles, Coords[] channelC, int channelOrientation) {
        this.start = start;
        this.end = end;
        this.path = new int[channelC.length];
        for (int i = 0; i < this.path.length; i++) {
            this.path[i] = -1;
        }
        this.tripath = new ArrayDeque<int[]>();

        this.channelC = channelC;
        this.triangles = triangles;
        this.co = channelOrientation;
        this.usedTri = new boolean[this.triangles.length];
        this.usedTri[start] = true;

    }

    private int orientation(int v1, int v2, int v3) {
        Coords p1 = this.channelC[v1];
        Coords p2 = this.channelC[v2];
        Coords p3 = this.channelC[v3];

        double difference = (p2.getY() - p1.getY()) * (p3.getX() - p2.getX())
                - (p2.getX() - p1.getX()) * (p3.getY() - p2.getY());
        if (difference < -0.00001) {
            return -1;
        }
        if (difference > 0.00001) {
            return 1;
        } else {
            return 0;
        }
    }

    public int findTriangle(int v) {
        for (int i = 0; i < this.triangles.length; i++) {
            if (triangles[i].contains(v)) {
                return i;
            }
        }
        return -1;
    }

    private int ml(int v) {
        if (v == 0) {
            return this.channelC.length - 1;
        }
        return v - 1;
    }

    private int mr(int v) {
        if (v == this.channelC.length - 1) {
            return 0;
        }
        return v + 1;
    }

    public void dfsTripath(int start) {
        for (int i = 0; i < this.triangles[start].getNeighbours().length; i++) {
            int n = this.triangles[start].getNeighbours()[i];
            
            if (!this.usedTri[n]) {
                this.usedTri[n] = true;

                this.tripath.add(this.triangles[n].commonEdge(this.triangles[start]));

                this.dfsTripath(n);
            }

        }
    }

    public void createSpt() {

        int startTri = this.findTriangle(start);
        int[] neighbours = this.triangles[startTri].getNeighbours();

        this.dfsTripath(start);
        int apex = start;

        int l = this.tripath.peekFirst()[0];
        int r = this.tripath.pollFirst()[1];
        this.path[l] = start;
        this.path[r] = start;

        while (!this.tripath.isEmpty()) {
            int[] edge = this.tripath.peekFirst();
            int pL = edge[0];
            int pR = edge[1];

            if (r != pR) {
                System.out.println(" a: " + apex + " r: " + r + " pR: " + pR);
                if (orientation(apex, r, pR) == -co) {//narrowing funnel
                    if (orientation(apex, r, l) == orientation(apex, pR, l) || apex == l) {//does not cross

                        this.path[pR] = apex;
                        r = pR;
                    } else {
                        //cross
                        this.path[l] = apex;
                        apex = l;
                        continue;
                    }
                }
            }
            if (l != pL) {
                System.out.println(" a: " + apex + " l: " + l + " pL: " + pL);
                if (orientation(apex, l, pL) == co) {//narrowing funnel
                    if (orientation(apex, l, r) == orientation(apex, pL, r) || apex == r) {//does not cross

                        this.path[pL] = apex;
                        l = pL;
                    } else {
                        //cross
                        this.path[r] = apex;
                        apex = r;
                        continue;
                    }
                }

            }
            this.tripath.pollFirst();
        }
        System.out.println(Arrays.toString(this.path));
    }

    public ArrayDeque getTripath() {
        return tripath;
    }

}
