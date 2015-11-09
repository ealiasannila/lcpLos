package visiGraph;

import java.util.Map;
import lcplos.dataStructures.Coords;
import logic.HelperFunctions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elias
 */
public class Sector {

    private int apex;
    private int l;
    private int r;

    public Sector(int apex, int l, int r) {
        this.apex = apex;
        this.l = l;
        this.r = r;
    }

    private boolean convex(Coords[] coords) {
        return HelperFunctions.isRight(this.apex, this.l, this.r, coords) == -1;
    }

    public boolean inside(int v, Coords[] coords) {
        if (!this.convex(coords)) {
            return HelperFunctions.isRight(this.apex, this.l, v, coords) == 1
                    && HelperFunctions.isRight(this.apex, this.r, v, coords) == -1;
        } else {
            return HelperFunctions.isRight(this.apex, this.l, v, coords) == 1
                    || HelperFunctions.isRight(this.apex, this.r, v, coords) == -1;
        }
    }

    public boolean outside(Edge edge, Coords[] coords) {
        if (this.convex(coords)) {
            if (HelperFunctions.isRight(this.apex, this.l, edge.getL(), coords) != 1 && HelperFunctions.isRight(this.apex, this.l, edge.getR(), coords) != 1
                    && HelperFunctions.isRight(this.apex, this.r, edge.getL(), coords) != -1 && HelperFunctions.isRight(this.apex, this.r, edge.getR(), coords) != -1) {
                return true;
            }
            return false;
        } else {
            if (HelperFunctions.isRight(this.apex, this.l, edge.getL(), coords) != 1 && HelperFunctions.isRight(this.apex, this.l, edge.getR(), coords) != 1) {
                return true;
            }
            if (HelperFunctions.isRight(this.apex, this.r, edge.getL(), coords) != -1 && HelperFunctions.isRight(this.apex, this.r, edge.getR(), coords) != -1) {
                return true;
            }
            return false;
        }
    }

    public int getApex() {
        return apex;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getR() {
        return r;
    }

    @Override
    public String toString() {
        return "Sector{" + "apex=" + apex + ", l=" + l + ", r=" + r + '}';
    }

    public void setR(int r) {
        this.r = r;
    }

}
