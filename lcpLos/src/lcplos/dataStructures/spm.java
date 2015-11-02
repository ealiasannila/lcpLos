/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eannila
 */
public class spm {

    private int start;
    private int[] channel;
    private Coords[] channelC;
    private int[] path;
    private int[] triangles;
    private int co;

    public spm(int start, int[] triangles, int[] channel, Coords[] channelC, int channelOrientation) {
        this.start = start;
        this.path = new int[channel.length];
        this.channel = channel;
        this.channelC = channelC;
        this.triangles = triangles;
        this.co = channelOrientation;

    }

    private int moveLeft(int i) {
        if (i == 0) {
            return this.channel.length - 1;
        }
        return i - 1;
    }

    private int moveRight(int i) {
        if (i == this.channel.length - 1) {
            return 0;
        }
        return i + 1;
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

    public void run() {
        this.shortestPaths(moveLeft(start), moveRight(start), start);
    }

    private void shortestPaths(int l, int r, int apex) {
        System.out.println("new: l=" + l + " r=" + r + " a=" + apex);
        if (this.moveLeft(r) == l) {
            return;
        }

        int ll = this.moveLeft(l);
        int rr = this.moveLeft(r);

        while (true) {
            if (r == l) {
                this.path[r] = apex;
                return;
            }
            if (orientation(apex, l, ll) == -co) {
                System.out.println("Lconvex: l=" + l + " a=" + apex);
                this.path[l] = apex;
                l = ll;
                ll = this.moveLeft(ll);
                if (orientation(apex, this.moveRight(l), r) == -orientation(apex, l, r)) {//CROSS
                    System.out.println("Lcross: l=" + l + " r=" + r + " a=" + apex);

                    this.path[r] = apex;
                    apex = r;
                    r = rr;
                    rr = this.moveRight(rr);

                }
            } else { // widens
                System.out.println("Lconcave: l=" + l + " a=" + apex);

                this.path[l] = apex;
                int thirdCorner = this.triangles[ll];
                this.shortestPaths(ll, thirdCorner, l);
                ll = thirdCorner;

            }
            if (r == l) {
                this.path[r] = apex;
                return;
            }
            if (orientation(apex, r, this.moveRight(r)) == co) {
                System.out.println("Rconvex: r=" + r + " a=" + apex);

                this.path[r] = apex;
                r = this.moveRight(r);
                if (orientation(apex, this.moveLeft(r), l) == -orientation(apex, r, l)) {//CROSS
                    System.out.println("Rcross: l=" + l + " r=" + r + " a=" + apex);

                    this.path[l] = apex;
                    apex = l;
                    l = this.moveLeft(l);
                }
            } else {//widens
                System.out.println("Rconcave: r=" + r + " a=" + apex);

                this.path[r] = apex;
                int thirdCorner = this.triangles[r];
                this.shortestPaths(thirdCorner, rr, r);
                rr = thirdCorner;

            }

        }
    }

}
