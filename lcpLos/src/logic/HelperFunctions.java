/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import lcplos.dataStructures.Coords;

/**
 *
 * @author elias
 */
public class HelperFunctions {
    
    public static double eucDist(Coords p1, Coords p2) {
        return Math.sqrt(eucDistSquared(p1.getX(), p1.getY(), p2.getX(), p2.getY()));

    }

    public static double eucDistSquared(double x1, double y1, double x2, double y2) {
        return Math.pow(Math.abs(y1 - y2), 2)
                + Math.pow(Math.abs(x1 - x2), 2);
    }

    
    public static int isRight(int v1, int v2, int v3, Coords[] coords) {
        Coords p1 = coords[v1];
        Coords p2 = coords[v2];
        Coords p3 = coords[v3];

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
    
}
