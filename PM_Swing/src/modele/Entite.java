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
