/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;

/**
 *
 * @author julien
 */
public class Bonus extends Entite {
    
    public static final int LIMITE = 5;
    private int temps;
    private int score;
    private boolean visible;
    
    public Bonus(Jeu _jeu, Point p, int score) {
        super(_jeu, p);
        temps = 0;
        visible = false;
    }
    
    public int getScore() {
        return score;
    }
    
    private void incremente() {
        if (temps < LIMITE) {
            temps++;
        }
        if (temps == LIMITE) {
            visible = true;
        }
    }
    
    public void reinitialiser() {
        visible = false;
        temps = 0;
    }

    @Override
    public void choixDirection() {}
    
    @Override
    public void avancerDirectionChoisie() {
        if (!visible) {
            incremente();
        }
        if (visible) {
            jeu.deplacerEntite(this, Direction.neutre);
        }
    }
    
}
