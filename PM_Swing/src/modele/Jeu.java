/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/** La classe Jeu a deux fonctions 
 *  (1) Gérer les aspects du jeu : condition de défaite, victoire, nombre de vies
 *  (2) Gérer les coordonnées des entités du monde : déplacements, collisions, perception des entités, ... 
 *
 * @author freder
 */
public class Jeu extends Observable implements Runnable {

    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 10;

    private Pacman pm;
    private Bonus bonus;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    
    // TODO : ajouter les murs, couloir, PacGums, et adapter l'ensemble des fonctions (prévoir le raffraichissement également du côté de la vue)
    
    
    public Jeu() {
        initialisationDesEntites();
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Pacman getPacman() {
        return pm;
    }
    
    public Bonus getBonus() {
        return bonus;
    }
    
    private void initialisationDesEntites() {
        
        pm = new Pacman(this, new Point(2, 0));
        grilleEntites[2][0] = pm;
        map.put(pm, new Point(2, 0));
        
        Fantome f = new Fantome(this, new Point(0, 0));
        grilleEntites[0][0] = f;
        map.put(f, new Point(0, 0));
        
        bonus = new Bonus(this, new Point(5, 5));
        //grilleEntites[5][5] = bonus;
        //map.put(bonus, new Point(5, 5));
        
    }
    
    
    /** Permet a une entité  de percevoir sont environnement proche et de définir sa strétégie de déplacement 
     * (fonctionalité utilisée dans choixDirection() de Fantôme)
     */
    public Object regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déclacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        
        boolean retour;
        
        if (d == Direction.neutre) {
            Point p = bonus.getCoo();
            if (/*contenuDansGrille(p) &&*/ objetALaPosition(p) == null) {
                grilleEntites[p.x][p.y] = bonus;
                return true;
            }
            return false;
        }
        
        Point pCourant = map.get(e);
        
        Point pCible = calculerPointCible(pCourant, d);
        
        if (contenuDansGrille(pCible) && (objetALaPosition(pCible) == null || objetALaPosition(pCible) instanceof Bonus)) {
            deplacerEntite(pCourant, pCible, e);
            retour = true;
        } else {
            retour = false;
        }
        return retour;
    }
    
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        switch(d) {
            case haut: pCible = new Point(pCourant.x, (pCourant.y - 1) % SIZE_Y); break;
            case bas : pCible = new Point(pCourant.x, (pCourant.y + 1) % SIZE_Y); break;
            case gauche : pCible = new Point((pCourant.x - 1) % SIZE_X, pCourant.y); break;
            case droite : pCible = new Point((pCourant.x + 1) % SIZE_X, pCourant.y); break;
            case neutre : pCible = pCourant; break;
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
        e.coo = pCible;
    }
    
    /** Vérifie que p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Object objetALaPosition(Point p) {
        Object retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }
    
    /**
     * Un processus est créé et lancé, celui-ci execute la fonction run()
     */
    public void start() {

        new Thread(this).start();

    }

    @Override
    public void run() {
        
        while (true) {
            Date start = new Date();
            for (Entite e : map.keySet()) { // déclenchement de l'activité des entités, map.keySet() correspond à la liste des entités
                if (!(e instanceof Bonus)) {
                    e.run();
                }
            }
            bonus.run();

            setChanged();
            notifyObservers(); // notification de l'observer pour le raffraichisssement graphique
            
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
