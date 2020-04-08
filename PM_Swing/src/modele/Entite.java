/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;

/**
 *
 * @author freder
 */
public abstract class Entite implements Runnable {

    protected Jeu jeu;
    protected Direction d;
    protected Point coo;

    public abstract void choixDirection();

    public void avancerDirectionChoisie() {
        jeu.deplacerEntite(this, d);
    }

    public Entite(Jeu _jeu, Point p) {
        jeu = _jeu;
        coo = p;
    }

    @Override
    public void run() {
        choixDirection();
        avancerDirectionChoisie();
    }

    @Override
    public String toString() {
        return coo.toString();
    }
}
