/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;



public class Pacman extends Entite {
    
    private int score;
    private boolean big;
    private int vie;
    private final Point spawn;

    public Pacman(Jeu _jeu, Point p) {
        super(_jeu, p);
        d = Direction.droite;
        score = 0;
        big = false;
        vie = 2;
        spawn = p;
    }
    
    public void tuer() {
        if (vie > 0) {
            vie--;
            d = Direction.droite;
            coo = spawn;
        }
        else {
            System.out.println("fin de la partie");
            jeu.stop();
        }
    }
    
    public void augmenterScore(int s) {
        score += s;
    }
    
    public void setDirection(Direction _d) {
        d = _d;
    }
    
    public int getState() {
        if (!big) {
            if (d == Direction.haut)
                return 0;
            if (d == Direction.bas)
                return 1;
            if (d == Direction.gauche)
                return 2;
            if (d == Direction.droite)
                return 3;
        }
        else {
            if (d == Direction.haut)
                return 4;
            if (d == Direction.bas)
                return 5;
            if (d == Direction.gauche)
                return 6;
            if (d == Direction.droite)
                return 7;
        }
        return -1;
    }

    @Override
    public void choixDirection() {}

}
