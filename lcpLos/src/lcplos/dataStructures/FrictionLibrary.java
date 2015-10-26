/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.HashMap;

/**
 *
 * @author elias
 */
public class FrictionLibrary {
    private HashMap<Integer, Double> frictions;
    

    public FrictionLibrary() {
        this.frictions = new HashMap<Integer, Double>();
    }
    
    public void addFriction(int polygon, double friction){
        this.frictions.put(polygon, friction);
    }
    
    public double getFriction(int polygon){
        return this.frictions.get(polygon);
    }
    
    
}
