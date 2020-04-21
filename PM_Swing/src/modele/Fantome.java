package modele;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author freder
 */
public class Fantome extends Entite {

    private static final int LIMITE = 50; // Nombre de tour avant de redevenir invulnérable

    private Vector<Point> pointTraverse = new Vector<>();
    private final Random r = new Random(); // Pour déplacement aléatoire
    private final Color couleur;
    private final Point spawn; // Point d'apparition
    private Vector<Point> mur = new Vector<>();; // contient les coordoneés des murs

    private boolean vulnerable; // Ne peut pas tuer pacman
    private boolean mort; // Ne peut pas être mangé par pacman
    private int temps_vulnerable; // Compteur pour gérer quand redevenir invulnérable

    public Fantome(Jeu _jeu, Point p, Color c) {
        super(_jeu, p);
        d = Direction.droite;
        spawn = p;
        couleur = c;
        vulnerable = false;
        mort = false;
	
        for(int i=0;i<jeu.SIZE; i++){
            for(int j=0;j<jeu.SIZE; j++)
            {
                if(jeu.getGrille()[i][j] instanceof Mur)
                    mur.add(new Point(i,j));
            }
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    /**
     * Le fantôme se fait manger par pacman
     */
    public void getManger() {
        mort = true;
        coo = spawn;
        d = Direction.neutre;
        jeu.deplacerEntite(this, Direction.neutre); // Pour MAJ visuelle
        jeu.getSon().addElement(4);
    }

    /**
     * Pour sélectionner l'icone à afficher (voir la vue)
     */
    public int getState() {
        if (!vulnerable) {
            if (couleur == Color.BLUE) {
                if (d == Direction.bas)
                    return 0;
                if (d == Direction.gauche)
                    return 1;
                if (d == Direction.droite)
                    return 2;
                if (d == Direction.haut)
                    return 3;
                return 2;
            }
            if (couleur == Color.RED) {
                if (d == Direction.bas)
                    return 4;
                if (d == Direction.gauche)
                    return 5;
                if (d == Direction.droite)
                    return 6;
                if (d == Direction.haut)
                    return 7;
                return 6;
            }
            if (couleur == Color.PINK) {
                if (d == Direction.bas)
                    return 8;
                if (d == Direction.gauche)
                    return 9;
                if (d == Direction.droite)
                    return 10;
                if (d == Direction.haut)
                    return 11;
                return 10;
            }
            if(couleur == Color.GREEN){ //corona
                return 14;   
            }
        }
        else {
            if (mort)
                return 12; // Les yeux qui retournent au spawn
            else
                return 13; // Les fantômes bleues
        }
        return -1;
    }

    @Override
    public void choixDirection() {
    
        /**
         * Tant que le fantôme est mort ET vulnérable, il doit rester dans son spawn
         */
        if (mort) {
            d = Direction.neutre;
            return;
        }
        
    	if(this.jeu.getNiveau() == 1)
            choiceDirectionEasy();
        
        if(this.jeu.getNiveau() == 2)
            choiceDirectionMedium(jeu.getPacman().coo,this.coo);
           
        if(this.jeu.getNiveau() == 3)
            choiceDirectionHard();
    }
    
    public boolean autoriser(Direction r ){
        
        boolean aut = !(jeu.regarderDansLaDirection(this, r) instanceof Mur) && !(jeu.regarderDansLaDirection(this, r) instanceof Fantome); 
        
        return aut;
    }

    public void choiceDirectionMedium(Point pacman, Point fantome){
        int dx = pacman.x - fantome.x;
        int dy= pacman.y - fantome.y;
        
        if (dx>0) dx=1;
        if (dx<0) dx=-1;
        if (dy>0) dy=1;
        if (dy<0) dy=-1;
                                
            if(autoriser(Direction.droite) && dx == 1)
            {
                d = Direction.droite;
            }else if(autoriser(Direction.gauche) && dx == -1)
            {
                d = Direction.gauche;
            }
            if(autoriser( Direction.haut)  && dy == -1)
            {
                d = Direction.haut;
            }else if(autoriser(Direction.bas) && dy == 1)
            {
                d = Direction.bas;
            }
            
            if(dx == 0 && dy == 0)
            {
               if(autoriser(Direction.droite)){
                   d = Direction.droite;
               }
               else if(autoriser(Direction.gauche)){
                   d = Direction.gauche;
               }
               else if(autoriser(Direction.haut)){
                   d = Direction.haut;
               }
               else if(autoriser(Direction.bas)){
                   d = Direction.bas;
               }        
            }
        
    }
    
    public void  choiceDirectionHard(){
        
        AlgorithmeAStar algo = new AlgorithmeAStar(jeu.SIZE,this.coo,jeu.getPacman().coo,mur);
        
        pointTraverse = algo.jeuSolution();
        
       // System.out.println("********************************Voici le Point suivant "+pointTraverse.lastElement()+"Voici Mon point"+this.coo +"******************************");
        if(pointTraverse.isEmpty()) choiceDirectionMedium(jeu.getPacman().coo,this.coo);
        else choiceDirectionMedium(pointTraverse.lastElement(),this.coo);  
    }
    
    public void choiceDirectionEasy(){
        switch (d) {
                case bas:
                        d = dirBasHaut(d,Direction.bas);
                    break;
                    
                case haut:
                        d = dirBasHaut(d,Direction.haut);
                    break;
                    
                case gauche:
                        d = dirGaucheDroite(d,Direction.gauche);
                    break;
                    
                case droite:
                        d = dirGaucheDroite(d,Direction.droite);
                    break;      
           } 
    }
    
    public Direction dirBasHaut(Direction d, Direction hautBas)
    { 
        if(autoriser(Direction.droite) && r.nextInt(3) ==0)
        {
            d = Direction.droite;
        }
        else if(!(jeu.regarderDansLaDirection(this, hautBas) instanceof Mur))
        {
            d = hautBas;
        }
        if(autoriser(Direction.gauche))
        {
            d = Direction.gauche;
        }
            return d;
    }
    
    public Direction dirGaucheDroite(Direction d,Direction gaucheDroite)
    {    
        if(autoriser(Direction.haut) && r.nextInt(3) ==0)
               {
                   d = Direction.haut;
               }
        else if(!(jeu.regarderDansLaDirection(this, gaucheDroite) instanceof Mur))
        {
            d = gaucheDroite;
        }
        if(autoriser(Direction.bas))
               {
                   d = Direction.bas;
               }
        
        return d;
    }

    public boolean isMort() {
        return mort;
    }

    public void setVulnerable() {
        vulnerable = true;
        temps_vulnerable = LIMITE;
    }

    private void gestionVulnerable() {
        if (temps_vulnerable > 0)
            temps_vulnerable--;
        if (temps_vulnerable == 0) {
            mort = false;
            vulnerable = false;
        }
    }

    @Override
    public void run() {
        if (vulnerable)
            gestionVulnerable();
        choixDirection();
        avancerDirectionChoisie();
    }
}
