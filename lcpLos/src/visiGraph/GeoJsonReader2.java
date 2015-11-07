/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiGraph;

import dataManagement.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.NodeLibrary;
import logic.EdgeSplitter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elias
 */
public class GeoJsonReader2 {

    private static JSONObject lataaJsonObject(File tiedosto) {

        Scanner lukija = null;
        try {
            lukija = new Scanner(tiedosto);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeoJsonReader2.class.getName()).log(Level.SEVERE, null, ex);
        }
        lukija.useDelimiter("\\Z");
        String data = lukija.next();

        return new JSONObject(data);
    }

    public static Coords[] readPolygon(File polygonFile) {
        JSONObject polygonObject = lataaJsonObject(polygonFile);
        JSONArray polygonFeatures = polygonObject.getJSONArray("features");
        Coords[] coords = null;
        for (int feature = 0; feature < polygonFeatures.length(); feature++) {
            if (polygonFeatures.getJSONObject(feature).getJSONObject("geometry").getString("type").equals("Polygon")) {
                JSONArray coordinates = polygonFeatures.getJSONObject(feature).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                coords = new Coords[coordinates.length()];
                for (int k = 0; k < coordinates.length(); k++) {
                    JSONArray coordinatePair = coordinates.getJSONArray(k);
                    coords[k] = new Coords(coordinatePair.getDouble(0), coordinatePair.getDouble(1));
                }
            }
        }

        return coords;
    }

    public static PolygonOma readTriangles(File triangleFile) {
        JSONObject polygonObject = lataaJsonObject(triangleFile);
        JSONArray polygonFeatures = polygonObject.getJSONArray("features");

        PolygonOma polygon = new PolygonOma();
        for (int feature = 0; feature < polygonFeatures.length(); feature++) {
            if (polygonFeatures.getJSONObject(feature).getJSONObject("geometry").getString("type").equals("Polygon")) {
                JSONObject attributes = polygonFeatures.getJSONObject(feature).getJSONObject("properties");
                int[] triangle = new int[3];
                triangle[0] = attributes.getInt("POINTA");
                triangle[1] = attributes.getInt("POINTB");
                triangle[2] = attributes.getInt("POINTC");
                polygon.addTriangle(triangle);
            }
        }

        return polygon;
    }

}
