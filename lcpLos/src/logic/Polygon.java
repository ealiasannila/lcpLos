/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.NodeLibrary;

/**
 *
 * @author elias
 */
public class Polygon {

    public static boolean losBetweenNodes(int startNode, int targetNode, NodeLibrary nodelib, int polygon) { //target is index within polygon, start absolute node index
        if (startNode == targetNode) {
            return false;
        }

        ArrayList<Integer> nodes = nodelib.getNodes(polygon);
        Coordinates sc = nodelib.getCoordinates(startNode);
        Coordinates tc = nodelib.getCoordinates(targetNode);

        for (int start = 0; start < nodes.size() - 1; start++) {
            int end = start + 1;
            if (end == nodes.size()) {
                end = 0;
            }
            if (edgesIntersect(sc, tc, nodelib.getCoordinates(nodes.get(start)), nodelib.getCoordinates(nodes.get(end)))) {
//               System.out.println("killing due intersect");
//                System.out.println("es:" + start + " et: " + end + ", s: " + startNode + " t: " + targetNode);

                return false;
            }
        }

        return true;

    }

    public static boolean edgesIntersect(Coordinates s1, Coordinates s2, Coordinates k1, Coordinates k2) {

        if (s1.equals(k1) || s1.equals(k2) || s2.equals(k1) || s2.equals(k2)) {
            return false;
        }
        int o1 = orientation(s1, s2, k1);
        int o2 = orientation(s1, s2, k2);
        int o3 = orientation(k1, k2, s1);
        int o4 = orientation(k1, k2, s2);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special Cases
        // s1, s2 and k1 are colinear and k1 lies on segment s1s2
        if (o1 == 0 && onSegment(s1, k1, s2)) {
            return true;
        }

        // s1, s2 and k2 are colinear and k2 lies on segment s1s2
        if (o2 == 0 && onSegment(s1, k2, s2)) {
            return true;
        }

        // k1, k2 and s1 are colinear and s1 lies on segment k1k2
        if (o3 == 0 && onSegment(k1, s1, k2)) {
            return true;
        }

        // k1, k2 and s2 are colinear and s2 lies on segment k1k2
        if (o4 == 0 && onSegment(k1, s2, k2)) {
            return true;
        }

        return false; // Doesn't fall in any of the above cases

    }

    private static boolean onSegment(Coordinates s1, Coordinates s2, Coordinates p) {
        if (s2.getX() <= Math.max(s1.getX(), p.getX()) && s2.getX() >= Math.min(s1.getX(), p.getX())
                && s2.getY() <= Math.max(s1.getY(), p.getY()) && s2.getY() >= Math.min(s1.getY(), p.getY())) {
            return true;
        }

        return false;
    }

    /**
     * kertoo onko kolmen pisteen kiertosuunta myötä (-1) vai vastapäivään(1),
     * vai ovatko ne samalla viivalla(0)
     *
     * @param xp
     * @param yp
     * @param ed1x
     * @param ed1y
     * @param ed2x
     * @param ed2y
     * @return
     */
    private static int orientation(Coordinates p1, Coordinates p2, Coordinates p3) {
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

    public static boolean sample(int node, int targetNode, NodeLibrary nodelib, int polyIndex, int n) {
        for (int i = 1; i <= n; i++) {

            double px = nodelib.getCoordinates(node).getX() + (nodelib.getCoordinates(targetNode).getX() - nodelib.getCoordinates(node).getX()) / (n+1)*i;
            double py = nodelib.getCoordinates(node).getY() + (nodelib.getCoordinates(targetNode).getY() - nodelib.getCoordinates(node).getY()) / (n+1)*i;

            if (!Polygon.pointInside(new Coordinates(px, py), nodelib, polyIndex)) {
                return false;
            }
        }
        return true;
    }

    public static boolean pointInside(Coordinates p, NodeLibrary nodelib, int polygon) {

        int intersections = 0;
        ArrayList<Integer> nodes = nodelib.getNodes(polygon);

        for (int start = 0; start < nodes.size(); start++) {
            int end = start + 1;
            if (start == nodes.size() - 1) { //last to first
                end = 0;
            }

            Coordinates sc = nodelib.getCoordinates(nodes.get(start));
            Coordinates ec = nodelib.getCoordinates(nodes.get(end));
            if ((Math.abs(sc.getX() - p.getX()) < 0.0001 && Math.abs(ec.getX() - p.getX()) < 0.0001)) {

                if (p.getY() - Math.min(sc.getY(), ec.getY()) > -0.00001 && p.getY() - Math.max(sc.getY(), ec.getY()) < 0.00001) {
                    return true;
                }
            }

            //raycasting method, ray shot due north
            if ((sc.getX() <= p.getX() && ec.getX() > p.getX()) || (sc.getX() > p.getX() && ec.getX() <= p.getX())) { //point between edge x coordinates
                double proportionOfSegment = (p.getX() - sc.getX()) / (ec.getX() - sc.getX());
                double intersectY = sc.getY() + proportionOfSegment * (ec.getY() - sc.getY());

                if (Math.abs(p.getY() - intersectY) < 0.0001) { //point on edge
                    return true;
                }
                if (p.getY() < intersectY) {
                    intersections++;
                }

            }
        }

        return intersections % 2 != 0;
    }

}
