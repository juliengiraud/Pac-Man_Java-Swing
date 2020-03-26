/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.ArrayList;
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

    //public static final int SIZE_X = 26;
    //public static final int SIZE_Y = 29;
    public static final int SIZE = 10;

    private Pacman pm;
    private Bonus bonus;

    private Entite[][] grilleEntites = new Entite[SIZE][SIZE]; // permet de récupérer une entité à partir de ses coordonnées
    private ArrayList<Entite> entites = new ArrayList<>();
    
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
        entites.add(pm);
        
        Fantome f = new Fantome(this, new Point(0, 0));
        grilleEntites[0][0] = f;
        entites.add(f);
        
        bonus = new Bonus(this, new Point(5, 5), 200);
        entites.add(bonus);
        
    }
    
    
    /** Permet a une entité  de percevoir sont environnement proche et de définir sa strétégie de déplacement 
     * (fonctionalité utilisée dans choixDirection() de Fantôme)
     */
    public Object regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = e.coo;
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déclacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        
        boolean retour;
        
        Point pCourant = e.coo;
        Point pCible = calculerPointCible(pCourant, d);
        
        if (!contenuDansGrille(pCible)) {
            return false;
        }
        
        if (objetALaPosition(pCible) == null) { // Si la case est vide on a rien à faire
            deplacerEntite(pCourant, pCible, e);
            return true;
        }
        
        Entite new_e = gestionCollision(e, objetALaPosition(pCible));
        if (new_e != null) {
            deplacerEntite(pCourant, pCible, new_e);
            return true;
        }
        
        return false;
    }
    
    private Entite gestionCollision(Entite src, Entite dest) { // Gère le trantement lorsque deux entités se supperposent
        
        if (dest == bonus) { // On va arriver aux coordonnées du bonus
            if (src == pm) { // Si on est le joueur
                pm.augmenterScore(bonus.getScore()); // Gestion "pacman mange le bonus"
                bonus.reinitialiser();
                return pm;
            }
            else { // Un fantome arrive sur le bonus
                return dest; // Gestion "fantome sur le bonus"
            }
        }
        return null;
    }
    
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        switch(d) {
            case haut: pCible = new Point(pCourant.x, (pCourant.y + SIZE - 1) % SIZE); break;
            case bas: pCible = new Point(pCourant.x, (pCourant.y + 1) % SIZE); break;
            case gauche: pCible = new Point((pCourant.x + SIZE - 1) % SIZE, pCourant.y); break;
            case droite: pCible = new Point((pCourant.x + 1) % SIZE, pCourant.y); break;
            case neutre: pCible = pCourant; break;
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        e.coo = pCible;
    }
    
    /** Vérifie que p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        boolean sortie = p.x >= 0 && p.x < SIZE && p.y >= 0 && p.y < SIZE;
        if (!sortie)
            System.out.println("Erreur grille : " + p.toString());
        return sortie;
    }
    
    private Entite objetALaPosition(Point p) {
        Entite retour = null;

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
            for (Entite e : entites) {
                e.run();
            }

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
