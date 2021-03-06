/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.PriorityQueue;



/**
 * 
 * @author ghali
 */
public final class AlgorithmeAStar {
    
    public static final int V_H_COST = 10;
    
    //la grille est passer en parametre une grille d'entite
    
    private Cellule[][] grilleCellule;
    
    private final PriorityQueue<Cellule> openList;
    private boolean[][] closedList;
    
    private final Point debut;
    private final Point arriver;
    
    
    private final ArrayList<Point> vector;


    public AlgorithmeAStar(int size, Point debut, Point arriver, ArrayList<Point> mur){
        
        grilleCellule = new Cellule[size][size];
        closedList = new boolean[size][size];
        
        openList = new PriorityQueue<>( (Cellule e1, Cellule e2)->{
            return e1.GetCost() < e2.GetCost() ? -1 : e1.GetCost()> e2.GetCost() ? 1:0;
        });
        
        this.debut = debut;
        this.arriver = arriver;
        
        initialiserHeurestique();
        
        //mettre les murs a null
        for(int i=0;i< mur.size() ; i++){
            ajouterMurCellule(mur.get(i).x,mur.get(i).y);
        }
        
        vector = new ArrayList<>();
        
        
    };
    
    public void ajouterMurCellule(int i , int j){
        grilleCellule[i][j]=null;
    }
    
    // a appeler au debut de la fontion
    public void initialiserHeurestique(){
        
        for(int i=0; i<grilleCellule.length; i++){
            for(int j=0; j<grilleCellule[i].length; j++){
                grilleCellule[i][j] = new Cellule(new Point(i,j));
                
                int heu = abs(i - arriver.x)+abs(j - arriver.y);
                
                grilleCellule[i][j].setHeurestique(heu);
                grilleCellule[i][j].solution = false;
            }
        }

        grilleCellule[debut.x][debut.y].setCost(0);
            
    }
             
    public void chargerCost(Cellule e, Cellule t, int cout){
        if(t== null|| closedList[t.GetR().x][t.GetR().y])
        {
            return;
        }
        
        int costFinal = t.GetHeurestique()+cout;
                
        boolean isOpen = openList.contains(t);
        
        if(! isOpen || (costFinal < t.GetCost())){
            t.setCost(costFinal);
            t.setParent(e);
            
            if(!isOpen)
                openList.add(t);
        }
    }
    

    
    public void cheminCours(){
        
        openList.add(grilleCellule[debut.x][debut.y]);
        
        Cellule courant;

        while(true){
            courant = openList.poll();
            
            if(courant == null)
            {
                break;
            }
            
            
            closedList[courant.GetR().x][courant.GetR().y] = true;
            
            if(courant.GetR().x == arriver.x && courant.GetR().y == arriver.y)
                return;
            
            Cellule gauche;
            
            if(courant.GetR().x -1 >=0){
                gauche = grilleCellule[courant.GetR().x -1][courant.GetR().y];
                chargerCost(courant,gauche,courant.GetCost()+V_H_COST);
            }
            
            Cellule droite;
            if(courant.GetR().x +1 < grilleCellule.length){
                droite = grilleCellule[courant.GetR().x+1][courant.GetR().y];
                chargerCost(courant,droite,courant.GetCost()+V_H_COST);
            }
            
            Cellule bas;
            if(courant.GetR().y +1 < grilleCellule[1].length){
                bas = grilleCellule[courant.GetR().x][courant.GetR().y+1];
                chargerCost(courant,bas,courant.GetCost()+V_H_COST);
            }
            
            Cellule haut;
            if(courant.GetR().y -1 >=0){
                haut = grilleCellule[courant.GetR().x][courant.GetR().y-1];
                chargerCost(courant,haut,courant.GetCost()+V_H_COST);
            }
            
            
        }
        
    }
    
    //juste pour afficher les grilles
    public void play(){
            System.out.println(" Grind : ");
            
            for(int i=0; i<grilleCellule.length;i++){
                for(int j=0; j<grilleCellule[i].length; j++){
                    
                    if(i== debut.x && j== debut.y)
                        System.out.println("S0 ");
                    else if(i == arriver.x && j== arriver.y )
                        System.out.println("DE ");
                    else if(!(grilleCellule[i][j] == null))
                        System.out.printf("%-3d",0);
                    else
                        System.out.println(" Mur "); //Mur
                }
            }
            System.out.println();
    }
    
    public void scoreEntite(){
        System.out.println("\n Scores des entite");
        
        for(int i=0; i<grilleCellule.length;i++){
                for(int j=0; j<grilleCellule[i].length; j++){
                    if(! (grilleCellule[i][j] == null))
                        System.out.printf("%-3d",grilleCellule[i][j].GetCost());
                    else
                        System.out.println(" Mur "); //Mur
                }
                
        }
        System.out.println();
        
    }
    
    public ArrayList<Point> jeuSolution(){
        
        vector.clear();
        openList.clear();
        
        for (boolean[] closedList1 : closedList) {
            for (int j = 0; j < closedList1.length; j++) {
                closedList1[j] = false;
            }
        }
    
        cheminCours();
        
        if(closedList[arriver.x][arriver.y]){
            
            Cellule courant = grilleCellule[arriver.x][arriver.y];
            vector.add(courant.GetR());
            
            grilleCellule[courant.GetR().x][courant.GetR().y].solution = true;
            
            while(courant.GetParent() != null){
                grilleCellule[courant.GetParent().GetR().x ][courant.GetParent().GetR().y].solution = true;
                vector.add(courant.GetR());
                courant = courant.GetParent();
            }

        } else 
        {
            System.out.println("pas de chemin possible");
        }
            
        
       return vector;
    }
    
    
}
