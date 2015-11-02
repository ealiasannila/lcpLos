/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 *
 * @author eannila
 */
public class Funnel {

    private ArrayDeque<Integer> funnel;

    public Funnel() {
        this.funnel = new ArrayDeque<>();

    }

    public void Add(int v, Coords vc, int type, ArrayList<Integer> p){
        if(type == -1){
            while(true){
                if(this.funnel.peekFirst()==this.funnel.peekLast()){
                    this.funnel.addFirst(v);
                }
            }
        }
    }
    
}
