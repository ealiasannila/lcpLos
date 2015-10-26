/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author elias
 */
public class HelperFunctions {
    
    public static double eucDist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(eucDistSquared(x1, y1, x2, y2));

    }

    public static double eucDistSquared(double x1, double y1, double x2, double y2) {
        return Math.pow(Math.abs(y1 - y2), 2)
                + Math.pow(Math.abs(x1 - x2), 2);
    }

    
}
