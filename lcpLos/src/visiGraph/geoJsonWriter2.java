/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiGraph;

import dataManagement.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.NodeLibrary;
import lcplos.dataStructures.VertexLib;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elias
 */
public class geoJsonWriter2 {

    public static boolean kirjoita(String polku, JSONObject data) {
        PrintWriter kirjoittaja = null;
        try {
            kirjoittaja = new PrintWriter(polku, "UTF-8");
            kirjoittaja.print(data.toString());
            kirjoittaja.close();
        } catch (FileNotFoundException ex) {
            return false;
        } catch (UnsupportedEncodingException ex) {
            return false;
        }
        return true;
    }

    private static JSONObject perusJson(String crs) {
        JSONObject perusjson = new JSONObject();
        perusjson.put("type", "FeatureCollection");

        JSONObject pisteetcrsproperties = new JSONObject();
        pisteetcrsproperties.put("name", crs);

        JSONObject pisteetcrs = new JSONObject();
        pisteetcrs.put("type", "name");
        pisteetcrs.put("properties", pisteetcrsproperties);

        perusjson.put("crs", pisteetcrs);
        return perusjson;
    }

    public static JSONObject triangles(List<int[]> triangles, VertexLib vlib, String crs) {
        JSONObject polygons = perusJson(crs);

        JSONArray features = new JSONArray();

        for (int i = 0; i < triangles.size(); i++) {
            JSONObject feature = new JSONObject();

            feature.put("type", "Feature");
            JSONObject properties = new JSONObject();
            feature.put("properties", properties);

            JSONObject geometry = new JSONObject();

            JSONArray rings = new JSONArray();
            JSONArray coordinates = new JSONArray();

            for (int j = 0; j < 3; j++) {
                int v = triangles.get(i)[j];
                double[] reittipiste = new double[]{vlib.getCoords(v).getX(), vlib.getCoords(v).getY()};

                coordinates.put(new JSONArray(reittipiste));
            }
            rings.put(coordinates);
            geometry.put("coordinates", rings);

            geometry.put("type", "Polygon");
            feature.put("geometry", geometry);
            features.put(feature);
        }

        polygons.put("features", features);

        return polygons;

    }

    public static JSONObject boundary(Map<Integer, Coords> coords, Map<Integer, Integer> pred, String crs) {
        JSONObject polygons = perusJson(crs);
        JSONArray features = new JSONArray();
        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");
        JSONObject properties = new JSONObject();
        feature.put("properties", properties);
        JSONObject geometry = new JSONObject();
        JSONArray rings = new JSONArray();
        JSONArray coordinates = new JSONArray();

        for (Integer v : pred.keySet()) {

            double[] reittipiste = new double[]{coords.get(v).getX(), coords.get(v).getY()};

            coordinates.put(new JSONArray(reittipiste));
        }
        rings.put(coordinates);
        geometry.put("coordinates", rings);

        geometry.put("type", "Polygon");
        feature.put("geometry", geometry);
        features.put(feature);

        polygons.put(
                "features", features);

        return polygons;

    }

    public static JSONObject muunnaJsonReitti(Set<CoordEdge> edges, String crs) {
        JSONObject reitti = perusJson(crs);

        JSONArray features = new JSONArray();

        for (CoordEdge edge : edges) {

            JSONObject feature = new JSONObject();

            feature.put("type", "Feature");
            JSONObject properties = new JSONObject();
            feature.put("properties", properties);

            JSONObject geometry = new JSONObject();

            JSONArray coordinates = new JSONArray();

            double[] reittipiste1 = new double[]{edge.getL().getX(), edge.getL().getY()};
            double[] reittipiste2 = new double[]{edge.getR().getX(), edge.getR().getY()};

            coordinates.put(new JSONArray(reittipiste1));
            coordinates.put(new JSONArray(reittipiste2));
            geometry.put("coordinates", coordinates);

            geometry.put("type", "LineString");
            feature.put("geometry", geometry);
            features.put(feature);
        }

        reitti.put("features", features);

        return reitti;

    }

    public static JSONObject vertices(List<Coords> vertices, String crs) {
        JSONObject points = perusJson(crs);

        JSONArray features = new JSONArray();

        for (int v = 0; v < vertices.size(); v++) {
            Coords vertex = vertices.get(v);

            JSONObject feature = new JSONObject();

            feature.put("type", "Feature");
            JSONObject properties = new JSONObject();
            properties.put("vertex", v);
            feature.put("properties", properties);

            JSONObject geometry = new JSONObject();

            double[] coordinates = new double[]{vertex.getX(), vertex.getY()};

            geometry.put("coordinates", coordinates);

            geometry.put("type", "Point");
            feature.put("geometry", geometry);
            features.put(feature);
        }

        points.put("features", features);

        return points;

    }
/*
    public static JSONObject removeRings(JSONArray features, String crs, VertexLib vlib) {
        for (int i = 0; i < features.length(); i++) {
            List<Integer> polygon = vlib.getPolygon(i);
            boolean hole = true;
            if(polygon==null || polygon.isEmpty()){
                features.remove(i);
                continue;
            }
            for (int v: polygon) {
                if(vlib.vertexBelongsTo(v).size()>1){
                    hole = false;
                    break;
                }
            }
            if(hole){
               features.remove(i);
               continue;
            }
            
            JSONObject feature = features.getJSONObject(i);
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONArray outline = geometry.getJSONArray("coordinates").getJSONArray(0);
            geometry.remove("coordinates");
            JSONArray rings = new JSONArray();
            rings.put(outline);
            geometry.put("coordinates", rings);
        }
        JSONObject object = perusJson(crs);
        object.put("features", features);
        return object;
    }
*/
}
