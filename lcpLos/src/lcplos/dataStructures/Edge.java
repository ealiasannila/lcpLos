/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

/**
 *
 * @author eannila
 */
public class Edge {

    @Override
    public String toString() {
        return "Edge{" + "l=" + l + ", r=" + r + '}';
    }

    private int l;
    private int r;

    public Edge(int s, int e) {
        this.l = s;
        this.r = e;
    }

    public void setE(int e) {
        this.r = e;
    }
    @Override
    public int hashCode() {
        int hash = 5;
        int sum = this.l + this.r;
        int prod = this.l * this.r;
        hash = 53 * hash + sum;
        hash = 53 * hash + prod;
        return hash;
    }

    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (this.l != other.l && this.l != other.r) {
            return false;
        }
        if (this.r != other.r && this.r != other.l) {
            return false;
        }
        return true;
    }

    public int getL() {
        return l;
    }

    public int getR() {
        return r;
    }



}
