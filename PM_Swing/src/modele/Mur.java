package modele;

import java.awt.Point;

/**
 *
 * @author ghali
 */
public class Mur extends Entite{

    private final int type_mur;

    public Mur(Jeu _jeu, Point p, int t) {
        super(_jeu, p);
        type_mur = t;
    }

    public void setMur(Point pp) {
        coo = pp;
    }

    public int getTypeMur() {
        return type_mur;
    }

    @Override
    public void choixDirection() {}

    @Override
    public void run() {}

}
