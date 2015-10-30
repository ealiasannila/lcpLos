/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataManagement;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import lcplos.dataStructures.NodeLibrary;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elias
 */
public class geoJsonWriter {

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

    public static JSONObject muunnaJsonReitti(ArrayList<Integer> kirjoitettava, ArrayList<Double> frictions, NodeLibrary nodelib, String crs) {
        JSONObject reitti = perusJson(crs);

        JSONArray features = new JSONArray();

        for (int i = 0; i < kirjoitettava.size() - 1; i++) {

            JSONObject feature = new JSONObject();

            feature.put("type", "Feature");
            JSONObject properties = new JSONObject();
            properties.put("friction",frictions.get(i) );
            feature.put("properties", properties);

            JSONObject geometry = new JSONObject();

            JSONArray coordinates = new JSONArray();

            double[] reittipiste1 = new double[]{nodelib.getCoordinates(kirjoitettava.get(i)).getX(), nodelib.getCoordinates(kirjoitettava.get(i)).getY()};
            double[] reittipiste2 = new double[]{nodelib.getCoordinates(kirjoitettava.get(i + 1)).getX(), nodelib.getCoordinates(kirjoitettava.get(i + 1)).getY()};

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
