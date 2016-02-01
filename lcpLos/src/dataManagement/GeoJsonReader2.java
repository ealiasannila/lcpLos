/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lcplos.dataStructures.Coords;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elias
 */
public class GeoJsonReader2 {

    public static JSONArray lataaJsonObject(File tiedosto) {

        Scanner lukija = null;
        try {
            lukija = new Scanner(tiedosto);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeoJsonReader2.class.getName()).log(Level.SEVERE, null, ex);
        }
        lukija.useDelimiter("\\Z");
        String data = lukija.next();

        return new JSONObject(data).getJSONArray("features");

    }

    public static Coords[] readPoints(JSONArray points) {
        
        Coords[] coords = new Coords[points.length()];
        for (int i = 0; i < coords.length; i++) {
            JSONArray coordinates = points.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates");
            coords[i] = new Coords(coordinates.getDouble(0), coordinates.getDouble(1));
        }
        return coords;
    }

    public static List<Coords[]> readPolygon(JSONArray polygonFeatures, int feature) {
        List<Coords[]> polygon = new ArrayList<>();

        if (polygonFeatures.getJSONObject(feature).getJSONObject("geometry").getString("type").equals("Polygon")) {
            JSONArray rings = polygonFeatures.getJSONObject(feature).getJSONObject("geometry").getJSONArray("coordinates");
            for (int i = 0; i < rings.length(); i++) {

                JSONArray coordinates = rings.getJSONArray(i);
                int end = coordinates.length();
                if (new Coords(coordinates.getJSONArray(0).getDouble(0), coordinates.getJSONArray(0).getDouble(1))
                        .equals(new Coords(coordinates.getJSONArray(coordinates.length() - 1).getDouble(0), coordinates.getJSONArray(coordinates.length() - 1).getDouble(1)))) {
                    end--;
                }

                Coords[] coords = new Coords[end];

                for (int k = 0; k < end; k++) {
                    JSONArray coordinatePair = coordinates.getJSONArray(k);
                    coords[k] = new Coords(coordinatePair.getDouble(0), coordinatePair.getDouble(1));
                }
                polygon.add(coords);
            }

        }

        return polygon;
    }

}
