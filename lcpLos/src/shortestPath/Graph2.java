/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import lcplos.dataStructures.Coords;
import logic.HelperFunctions;
import logic.LosChecker;
import triangulation.Triangulator;

/**
 *
 * @author elias
 */
public class Graph2 {

    private Map<Coords, Map<Coords, Double>> al; //adjacency list

    public Graph2() {
        this.al = new HashMap<>();
    }
    
    public void addEdge(Coords s, Coords e){
        if(!this.al.containsKey(s)){
            this.al.put(s, new HashMap<Coords, Double>());
        }
        if(!this.al.containsKey(e)){
            this.al.put(e, new HashMap<Coords, Double>());
        }
        this.al.get(s).put(e,this.cost(s, e) );
        this.al.get(e).put(s,this.cost(e, s) );
        
    }
    
    private double cost(Coords s, Coords e){
       return HelperFunctions.eucDist(e, s);
    }
    
    public Map<Coords, Double> getNeighbors(Coords v){
        return this.al.get(v);
    }
    

  

}
