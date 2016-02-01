/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lcplos.dataStructures.Coords;
import lcplos.dataStructures.VertexLib;
import logic.HelperFunctions;
import lcplos.dataStructures.Edge;
import lcplos.dataStructures.Sector;

/**
 *
 * @author elias
 */
public class Charter {

    private int center;
    private List<Sector> charted;
    private VertexLib vlib;

    public Charter(int center, VertexLib vlib) {
        this.center = center;
        this.charted = new ArrayList<>();
        this.vlib = vlib;
    }

    public boolean hopeless(Edge e) {
        for (Sector sector : this.charted) {
            if (sector.notOutside(e.getL()) && sector.notOutside(e.getR())) {
                return false;
            }
        }
        return true;
    }

    private void removeUseless(Sector sector) {
        for (Iterator<Sector> it = this.charted.iterator(); it.hasNext();) {
            Sector next = it.next();
            if(sector.notOutside(next.getL()) && sector.notOutside(next.getR())){
                it.remove();
            }
        }
    }

    public void addFence(Edge e) {
        int l;
        int r;
        if (HelperFunctions.isRight(center, e.getL(), e.getR(), vlib) == 1) {
            l = e.getL();
            r = e.getR();
        } else if (HelperFunctions.isRight(center, e.getL(), e.getR(), vlib) == -1) {
            r = e.getL();
            l = e.getR();
        } else {
            return;
        }
        Sector lSector = null;
        Sector rSector = null;
        for (Sector sector : this.charted) {
            if (sector.notOutside(l)) {
                lSector = sector;
            }
            if (sector.notOutside(r)) {
                rSector = sector;
            }
            //sanity check:
            if (lSector == null && rSector == null) {
                this.charted.add(new Sector(this.center, l, r, this.vlib));
            } 
            
            else if (rSector.equals(lSector)) {
                System.out.println("Should be hopeless...");
            } else if (rSector == null) {
                lSector.setR(r);
                this.removeUseless(lSector);
            } else if (lSector == null) {
                rSector.setL(l);
                this.removeUseless(rSector);
            } else {
                lSector.setR(rSector.getR());
                this.removeUseless(lSector);
            }

        }

    }

}
