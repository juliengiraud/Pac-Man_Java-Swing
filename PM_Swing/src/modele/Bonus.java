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
    
    public static final int LIMITE = 6;
    private int temps;
    private boolean visible;
    
    public Bonus(Jeu _jeu, Point p) {
        super(_jeu, p);
        temps = 0;
        visible = false;
    }
    
    private void incremente() {
        if (temps < LIMITE) {
            temps++;
        }
        if (temps == LIMITE) {
            visible = true;
            System.out.println("Visible");
        }
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
