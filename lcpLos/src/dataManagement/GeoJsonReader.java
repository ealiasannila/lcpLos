/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.NodeLibrary;
import logic.EdgeSplitter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elias
 */
public class GeoJsonReader {

    private static JSONObject lataaJsonObject(File tiedosto) {

        Scanner lukija = null;
        try {
            lukija = new Scanner(tiedosto);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeoJsonReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        lukija.useDelimiter("\\Z");
        String data = lukija.next();

        return new JSONObject(data);
    }

    public static NodeLibrary readNodes(File tiedosto, int maxPolygons, FrictionLibrary frictionlib, double step) {
        JSONObject obj = lataaJsonObject(tiedosto);
        JSONArray features = obj.getJSONArray("features");

        NodeLibrary nodelib = new NodeLibrary(maxPolygons);

        for (int feature = 0; feature < features.length(); feature++) {
            if (features.getJSONObject(feature).getJSONObject("geometry").getString("type").equals("Polygon")) {
                JSONArray coordinates = features.getJSONObject(feature).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                frictionlib.addFriction(feature, features.getJSONObject(feature).getJSONObject("properties").getDouble("friction"));
                
                ArrayList<Coordinates> polygon = new ArrayList<Coordinates>();
                    
                for (int k = 0; k < coordinates.length() - 1; k++) {
                    JSONArray coordinatePair = coordinates.getJSONArray(k);
                    polygon.add(new Coordinates(coordinatePair.getDouble(0), coordinatePair.getDouble(1)));
                }
                polygon = EdgeSplitter.splitEdges(polygon, step);
                nodelib.addPolygon(polygon, feature);
                
            }
        }

        return nodelib;
    }

}
