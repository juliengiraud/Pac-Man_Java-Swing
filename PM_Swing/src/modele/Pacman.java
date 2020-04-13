/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;


public class Pacman extends Entite {
    
    private int score;
    private int vie;
    private final Point spawn;
    private Direction futureDirection;
    private int fantomeMange;

    public Pacman(Jeu _jeu, Point p) {
        super(_jeu, p);
        d = Direction.droite;
        score = 0;
        vie = 2;
        spawn = p;
        fantomeMange = 0;
    }
    
    public void resetFantomeMange() {
        fantomeMange = 0;
    }
    
    public void respawn() {
        coo = spawn;
        d = Direction.droite;
        futureDirection = null;
        jeu.deplacerEntite(this, Direction.neutre);
    }
    
    public void addVie() {
        vie++;
    }

    public void tuer() {
        if (vie > 0) {
            vie--;
            jeu.setStarting();
            respawn();
        }
        else {
            System.out.println("fin de la partie");
            jeu.stop();
        }
    }

    private void augmenterScore(int s) {
        score += s;
    }
    
    public void setFutureDirection(Direction d) {
        futureDirection = d;
    }

    public int getState() {
        if (d == Direction.haut)
            return 0;
        if (d == Direction.bas)
            return 1;
        if (d == Direction.gauche)
            return 2;
        if (d == Direction.droite)
            return 3;
        return 3; // Direction par défaut
    }

    public int getScore() {
        return score;
    }

    public int getVies() {
        return vie;
    }
    
    public void manger(Mangeable objet) {
        augmenterScore(objet.getScore());
        objet.getManger();
    }
    
    public void mangerFantome(Fantome f) {
        int[] scores = {300, 600, 1200};
        augmenterScore(scores[fantomeMange]);
        fantomeMange++;
        f.getManger();
    }

    @Override
    public void choixDirection() {
        if (futureDirection == null)
            return;
        Object cible = (Entite) jeu.regarderDansLaDirection(this, futureDirection);
        if (cible instanceof Mur)
            return;
        d = futureDirection;
    }

}
