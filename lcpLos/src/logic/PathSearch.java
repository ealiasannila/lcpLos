/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import lcplos.dataStructures.Coordinates;
import lcplos.dataStructures.Graph;

/**
 *
 * @author elias
 */
public class PathSearch {
    private double[] toStart;
    private double[] toEnd;
    private int[] path;
    
    private Graph graph;
    
    private int startnode;
    private int targetnode;
    

    public PathSearch(Graph graph, int startnode, int targetnode) {
        this.graph = graph;
        this.startnode = startnode;
        this.targetnode = targetnode;
        
        this.toEnd = new double[graph.getNumOfNodes()];
        this.toStart = new double[graph.getNumOfNodes()];
        this.path = new int[graph.getNumOfNodes()];
    
        for (int i = 0; i < graph.getNumOfNodes(); i++) {
            this.toStart[i] = Double.MAX_VALUE;
            this.path[i] = -1;
            this.toEnd[i] = this.estimateCost(i);
        }
        
        this.toStart[this.startnode] = 0;
        
    }
    
    private double estimateCost(int node){
        Coordinates targetc = this.graph.getNodelib().getCoordinates(this.targetnode);
        Coordinates nodec = this.graph.getNodelib().getCoordinates(node);
        return HelperFunctions.eucDist(nodec.getX(), nodec.getY(), targetc.getX(), targetc.getY());
    }
    
    private boolean relax(int node, int neighbour, int naapurinindeksi) {
        if (this.toStart[node] == Double.MAX_VALUE) {
            return false;
        }
        
        double kaari = this.vl[node].ota(naapurinindeksi)[1];
        if (this.toStart[neighbour] > this.toStart[node] + kaari) {
            this.toStart[neighbour] = this.toStart[node] + kaari;
            this.polku[neighbour] = node;
            return true;
        }

        return false;
    }

    /**
     * laskee lyhyimmät etäisyydet maalisolmuun lähtien lähtösolmusta.
     * Heuristiikkafunktiona suora etäisyys * minimivauhti, lopettaa etsinnän
     * kun reitti löytyy.
     *
     * @param lahtoSolmu
     * @return
     */
    public boolean aStar() {

        MinimiKeko keko = new MinimiKeko(this.toStart.length) {

            @Override
            double arvio(int i) {
                if (toStart[i] == Double.MAX_VALUE) {
                    return Double.MAX_VALUE * 0.00001;
                }
                return toStart[i] + loppuun[i] * 0.00001; //0.00001 = minimivauhti jota voidaan kulkea
            }
        };

        for (int i = 0; i < this.toStart.length; i++) {
            keko.lisaa(i);
        }

        while (!keko.tyhja()) {
            int solmu = keko.otaPienin();
            if (solmu == this.maalisolmu) {
                return true;
            }
            for (int i = 0; i < this.vl[solmu].koko(); i++) {
                int naapuri = (int) this.vl[solmu].ota(i)[0];
                if (relax(solmu, naapuri, i)) {
                    keko.paivita(naapuri);
                }
            }
        }
        if (this.toStart[this.maalisolmu] == Double.MAX_VALUE) { //reittiä ei löytynyt
            return false;
        }

        return true;
    }


    
}
