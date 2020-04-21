package modele;

import java.awt.Point;

/**
 *
 * @author julien
 */
public class Bonus extends Entite implements Mangeable {

    public static final int LIMITE = 20;
    private int temps;
    private final int score;
    private boolean visible;

    public Bonus(Jeu _jeu, Point p, int n) {
        super(_jeu, p);
        temps = 0;
        visible = false;
        int[] scores = {100, 200, 500};
        score = scores[n-1];
    }

    private void incremente() {
        if (temps < LIMITE)
            temps++;
        if (temps == LIMITE)
            visible = true;
    }

    public void reinitialiser() {
        visible = false;
        temps = 0;
    }

    @Override
    public void run() {
        if (!visible)
            incremente();
        if (visible)
            jeu.deplacerEntite(this, Direction.neutre);
    }

    @Override
    public void choixDirection() {}

    @Override
    public void getManger() {
        reinitialiser();
        jeu.getSon().addElement(2);
    }

    @Override
    public int getScore() {
        return score;
    }

}
