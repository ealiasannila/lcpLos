/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.Arrays;
import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.NodeLibrary;

/**
 *
 * @author elias
 */
public class EdgeSplitter {

    public static ArrayList<Coordinates> splitEdges(ArrayList<Coordinates> nodes, double step) {
        for (int start = 0; start < nodes.size(); start++) {
            int end = start + 1;
            if (start == nodes.size() - 1) {
                end = 0;
            }

            if (splitEdge(start, end, step, nodes) != null) {
                nodes.addAll(start + 1, splitEdge(start, end, step, nodes));
            }

        }
        return nodes;
    }

    private static ArrayList<Coordinates> splitEdge(int startNode, int endNode, double step, ArrayList<Coordinates> nodes) {
        Coordinates sc = nodes.get(startNode);
        Coordinates ec = nodes.get(endNode);
        return calcCoords(sc, ec, step);

    }

    private static ArrayList<Coordinates> calcCoords(Coordinates sc, Coordinates ec, double step) {
        double length = HelperFunctions.eucDist(sc, ec);
        if (step >= length) {
            return null;
        }
        int nOfNodes = (int) (length / step);
        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
        for (int i = 0; i < nOfNodes; i++) {
            coordinates.add(calcLoc(sc, ec, nOfNodes, i + 1));
        }
        return coordinates;
    }

    private static double pointFromSegment(double a, double b, int n, int m) {
        return a + (b - a) / (n + 1) * m;
    }

    private static Coordinates calcLoc(Coordinates sc, Coordinates ec, int nOfNodes, int n) {
        double px = pointFromSegment(sc.getX(), ec.getX(), nOfNodes, n);
        double py = pointFromSegment(sc.getY(), ec.getY(), nOfNodes, n);
        return new Coordinates(px, py);
    }

}
