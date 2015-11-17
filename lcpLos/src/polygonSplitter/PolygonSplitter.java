/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polygonSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lcplos.dataStructures.Coords;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

/**
 *
 * @author eannila
 */
public class PolygonSplitter {

    private Polygon polygon;

    public PolygonSplitter(List<Coords> outline, List<Coords[]> holes) {
        List<PolygonPoint> splitPoints = new ArrayList<>();

        List outlinePoints = new ArrayList<PolygonPoint>();
        for (Coords coords : outline) {
            outlinePoints.add(new PolygonPoint(coords.getX(), coords.getY()));
        }
        this.polygon = new Polygon(outlinePoints);

        for (Coords[] hole : holes) {
            List<PolygonPoint> holePoints = new ArrayList<>();
            for (int i = 0; i < hole.length; i++) {
                PolygonPoint holePoint = new PolygonPoint(hole[i].getX(), hole[i].getY());
                if (i == 0 || i == hole.length / 2) {
                    splitPoints.add(holePoint);
                }
                holePoints.add(holePoint);

            }
            this.polygon.addHole(new Polygon(holePoints));
        }

        Poly2Tri.triangulate(polygon);

        for (DelaunayTriangle triangle : polygon.getTriangles()) {
            for (PolygonPoint splitPoint : splitPoints) {
                if (triangle.contains(splitPoint)) {
                    TriangulationPoint pointCW = triangle.pointCW(splitPoint);
                    if (outline.contains(new Coords(pointCW.getX(), pointCW.getY()))) {
                        
                    }
                }
            }
        }

    }

}
