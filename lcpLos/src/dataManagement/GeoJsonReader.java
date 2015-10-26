/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.FrictionLibrary;
import lcplos.dataStructures.NodeLibrary;
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

    public static NodeLibrary readNodes(File tiedosto, int maxNodes, int maxPolygons, FrictionLibrary frictionlib) {
        JSONObject obj = lataaJsonObject(tiedosto);
        JSONArray features = obj.getJSONArray("features");

        NodeLibrary nodelib = new NodeLibrary(maxNodes, maxPolygons);

        for (int feature = 0; feature < Math.min(features.length(), maxPolygons); feature++) {
            if (features.getJSONObject(feature).getJSONObject("geometry").getString("type").equals("Polygon")) {
                JSONArray polygonRings = features.getJSONObject(feature).getJSONObject("geometry").getJSONArray("coordinates");
                frictionlib.addFriction(feature, features.getJSONObject(feature).getJSONObject("properties").getDouble("friction"));
                for (int polygonRing = 0; polygonRing < polygonRings.length(); polygonRing++) {
                    JSONArray coordinates = polygonRings.getJSONArray(polygonRing);
                    for (int k = 0; k < coordinates.length(); k++) {
                        JSONArray coordinatePair = coordinates.getJSONArray(k);
                      
                        nodelib.addNode(new Coordinates(coordinatePair.getDouble(0), coordinatePair.getDouble(1)), 1);
                        if (nodelib.getNumOfNodes() >= maxNodes) {
                            return nodelib;
                        }
                    }
                }
            }
        }
        return nodelib;
    }
    
}
