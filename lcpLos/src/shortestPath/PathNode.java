/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPath;

import java.util.List;
import lcplos.dataStructures.Coords;

/**
 *
 * @author eannila
 */
public class PathNode {
    private Coords coords;
    private PathNode pred;
    private double toStart;
    private double toEnd;
    

    public Coords getCoords() {
        return coords;
    }

   

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public PathNode getPred() {
        return pred;
    }

    public void setPred(PathNode pred) {
        this.pred = pred;
    }

    public double getToStart() {
        return toStart;
    }

    public void setToStart(double toStart) {
        this.toStart = toStart;
    }

    public double getToEnd() {
        return toEnd;
    }

    public void setToEnd(double toEnd) {
        this.toEnd = toEnd;
    }

    public PathNode(Coords coords, double toStart, double toEnd, PathNode pred) {
        this.coords = coords;
        this.toStart = toStart;
        this.toEnd = toEnd;
        this.pred = pred;
    }
}
