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
public class Coords {
    private double x;
    private double y;

    public Coords(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
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
        final Coords other = (Coords) obj;
        if (Math.abs(this.x-other.x)>0.001) {
            return false;
        }
        if (Math.abs(this.y-other.y)>0.001) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "x=" + x + ", y=" + y + '}';
    }
    
    
}
