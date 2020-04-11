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
public class Boule extends Entite implements Mangeable {
    
    protected int score;
    private boolean mange = false;

    public Boule(Jeu _jeu, Point p) {
        super(_jeu, p);
        score = 10;
    }

    @Override
    public void run() {
        jeu.deplacerEntite(this, Direction.neutre);
    }

    @Override
    public void choixDirection() {}

    @Override
    public void getManger() {
        mange = true;
    }

    public boolean isManger() {
        return mange;
    }

    @Override
    public int getScore() {
        return score;
    }
    
}
