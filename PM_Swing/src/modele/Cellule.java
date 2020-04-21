/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;

/**
 *
 * @author ghali
 */
public class Cellule {
    
    private Point r;
    private Cellule parent;
    private int heurestique;
    private int cout;
    public boolean solution;
    
    public Cellule(Point r){
        this.r = r;
        parent = null;
        heurestique = 0;
        cout = 0;
        solution=false;
    }
    
    public void setHeurestique(int r){
        
        heurestique = r;
    }
    
    public int GetHeurestique(){
        return heurestique;
    }
    
     public void setCost(int r){
        
        cout = r;
    }
    
    public int GetCost(){
        return cout;
    }
    
    public void setParent(Cellule r){
        
        parent = r;
    }
    
    public Cellule GetParent(){
        return parent;
    }
    
     public void setR(Point r){
        
        this.r = r;
    }
    
    public Point GetR(){
        return r;
    }
    
}
