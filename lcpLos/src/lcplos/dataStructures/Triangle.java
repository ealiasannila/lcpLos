/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

/**
 *
 * @author elias
 */
public class Triangle {

    private int[] nodes;
    private int[] neighbours;
    private int id;

    public Triangle(int[] nodes, int[] neighbours, int id) {
        this.nodes = nodes;
        this.neighbours = neighbours;
        this.id = id;
    }

    public int[] getNodes() {
        return nodes;
    }

    public int[] getNeighbours() {
        return neighbours;
    }

    public int getId() {
        return id;
    }

    public boolean contains(int vertex) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == vertex) {
                return true;
            }
        }
        return false;
    }

    public int missing(Triangle triangle) {

        for (int i = 0; i < this.nodes.length; i++) {
            if (!triangle.contains(this.getNodes()[i])) {
                return this.getNodes()[i];
            }
        }
        return -1;
    }

    public int[] opposingEdge(int v){
        
        for (int i = 0; i < 3; i++) {
            if(this.nodes[i]==v){
                int e[] = new int[2];
                if(i==0){
                    e[0] = this.nodes[1];
                    e[1] = this.nodes[2];
                }if(i==2){
                    e[0] = this.nodes[0];
                    e[1] = this.nodes[1];
                }else{
                    e[0] = this.nodes[2];
                    e[1] = this.nodes[0];
                }
                return e;
            }
        }
        return null;
    }
    
    public int[] commonEdge(Triangle triangle) {
        int missing = -1;
        int[] edge = new int[2];
        for (int i = 0; i < this.nodes.length; i++) {
            if (!triangle.contains(this.getNodes()[i])) {
                missing = i;
            }
        }
        if (missing == 0) {
            edge[0] = this.nodes[2];
            edge[1] = this.nodes[1];
        } else if (missing == 1) {
            edge[0] = this.nodes[0];
            edge[1] = this.nodes[2];
        } else {
            edge[0] = this.nodes[1];
            edge[1] = this.nodes[0];
        }
        return edge;

    }

}
