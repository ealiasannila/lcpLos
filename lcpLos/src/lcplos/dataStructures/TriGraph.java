/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eannila
 */
class TriGraph {

    private List<Integer>[] al;
    
    public TriGraph(int n, int[] from, int[] to) {
        this.al = new List[n];
        for (int i = 0; i < n; i++) {
            this.al[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            this.al[from[i]].add(to[i]);
            this.al[to[i]].add(from[i]);
        }
    }

    public List<Integer> getNeighbours(int v) {
        return this.al[v];
    }
    
    public int size(){
        return this.al.length;
    }
   

}
