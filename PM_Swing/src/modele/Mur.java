/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author ghali
 */
public class Mur extends Entite{
    
    private int type_mur;
    
   
     public Mur(Jeu _jeu, Point p, int t) {
        super(_jeu, p);
        type_mur = t;
    }
     
     public void setMur(Point pp){
         coo = pp;
     }
     
     public int getTypeMur() {
         return type_mur;
     }
     
     
    @Override
    public void choixDirection() {}
    
    
    @Override
    public void run() {}
    
}
