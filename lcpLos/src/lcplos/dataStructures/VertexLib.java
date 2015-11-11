/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logic.HelperFunctions;
import logic.LosChecker;

/**
 *
 * @author elias
 */
public class VertexLib {

    private Map<Coords, List<Integer>> nodeToPolygons;
    private CoordsWrapper[] polygons;
    
    public VertexLib(int nPoly) {
        this.nodeToPolygons = new HashMap<>();
        this.polygons = new CoordsWrapper[nPoly];
    }
    
    public void addVertex(Coords v, int p){
        if(!this.nodeToPolygons.containsKey(v)){
            this.nodeToPolygons.put(v, new ArrayList<Integer>());
        }
        this.nodeToPolygons.get(v).add(p);
    }
    
    public void addPolygon(Coords[] coords ,int p){
        this.polygons[p] = new CoordsWrapper(coords);
    }

    public Coords[] getPolygon(int p){
        return this.polygons[p].getCoords();
    }
    
    public List<Integer> vertexBelongsTo(Coords v){
        return this.nodeToPolygons.get(v);
    }
    
    public int pSize(){
        return this.polygons.length;
    }
    
    
    
}
