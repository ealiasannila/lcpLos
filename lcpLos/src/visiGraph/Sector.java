package visiGraph;

import java.util.Map;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
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
    private VertexLib vlib;

    public Sector(int apex, int l, int r, VertexLib vlib) {
        this.apex = apex;
        this.l = l;
        this.r = r;
        this.vlib = vlib;
    }

    private boolean convex() {
        return HelperFunctions.isRight(this.apex, this.l, this.r, vlib) == -1;
    }

    public boolean inside(int v) {
        if (!this.convex()) {
            return HelperFunctions.isRight(this.apex, this.l, v, vlib) == 1
                    && HelperFunctions.isRight(this.apex, this.r, v, vlib) == -1;
        } else {

            return HelperFunctions.isRight(this.apex, this.l, v, vlib) == 1
                    || HelperFunctions.isRight(this.apex, this.r, v, vlib) == -1;
        }
    }

    public boolean outside(Edge edge) {
        if (this.convex()) {
            if (HelperFunctions.isRight(this.apex, this.l, edge.getL(), vlib) != 1 && HelperFunctions.isRight(this.apex, this.l, edge.getR(), vlib) != 1
                    && HelperFunctions.isRight(this.apex, this.r, edge.getL(), vlib) != -1 && HelperFunctions.isRight(this.apex, this.r, edge.getR(), vlib) != -1) {
                return true;
            }
            return false;
        } else {
            if (HelperFunctions.isRight(this.apex, this.l, edge.getL(), vlib) != 1 && HelperFunctions.isRight(this.apex, this.l, edge.getR(), vlib) != 1) {
                return true;
            }
            if (HelperFunctions.isRight(this.apex, this.r, edge.getL(), vlib) != -1 && HelperFunctions.isRight(this.apex, this.r, edge.getR(), vlib) != -1) {
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
