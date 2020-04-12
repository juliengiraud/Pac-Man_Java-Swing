/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author freder
 */
public class Fantome extends Entite {

    private final Random r = new Random();
    //private Level level;
    private boolean vulnerable;
    private boolean mort;
    private final Color couleur;
    private final Point spawn;

    public Fantome(Jeu _jeu, Point p, Color c) {
        super(_jeu, p);
        d = Direction.droite;
        couleur = c;
        vulnerable = false;
        spawn = p;
    }
    
    public boolean isVulnerable() {
        return vulnerable;
    }
    
    /*public void respawn() {
        System.out.println("On passe de " + coo + " à " + spawn);
        jeu.getGrille()[coo.x][coo.y] = null;
        coo = spawn;
        d = Direction.neutre;
        jeu.deplacerEntite(this, Direction.neutre);
    }*/
    
    public void tuPeuxDevenirVulnerable() {
        vulnerable = true;
    }
    
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
        
        switch (r.nextInt(2)) {
            case 0:
                d = Direction.gauche;
                break;
            case 1:
                d = Direction.haut;
                break;
        }
    }
}
