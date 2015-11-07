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
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.NodeLibrary;
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

    public static JSONObject triangles(List<int[]> triangles, Coords[] coords, String crs) {
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
                double[] reittipiste = new double[]{coords[v].getX(), coords[v].getY()};

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

    public static JSONObject muunnaJsonReitti(Coords[] coords, int[] pred, String crs) {
        JSONObject reitti = perusJson(crs);

        JSONArray features = new JSONArray();

        for (int i = 0; i < pred.length; i++) {
            if (pred[i] == -1) {
                continue;
            }

            JSONObject feature = new JSONObject();

            feature.put("type", "Feature");
            JSONObject properties = new JSONObject();
            feature.put("properties", properties);

            JSONObject geometry = new JSONObject();

            JSONArray coordinates = new JSONArray();

            double[] reittipiste1 = new double[]{coords[i].getX(), coords[i].getY()};
            double[] reittipiste2 = new double[]{coords[pred[i]].getX(), coords[pred[i]].getY()};

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
}
