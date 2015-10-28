/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import lcplos.dataStructures.Coordinates;

/**
 *
 * @author elias
 */
public class HelperFunctions {
    
    public static double eucDist(Coordinates p1, Coordinates p2) {
        return Math.sqrt(eucDistSquared(p1.getX(), p1.getY(), p2.getX(), p2.getY()));

    }

    public static double eucDistSquared(double x1, double y1, double x2, double y2) {
        return Math.pow(Math.abs(y1 - y2), 2)
                + Math.pow(Math.abs(x1 - x2), 2);
    }

    
}
