package modele;

import java.awt.Point;

/**
 *
 * @author julien
 */
public class SuperBoule extends Boule {

    public SuperBoule(Jeu _jeu, Point p) {
        super(_jeu, p); // Ça serait dommage de ne pas profiter du code de la classe parente :P
        score = 50;
    }

    @Override
    public void getManger() {
        super.getManger();
        jeu.pacmanMangeSuperboule();
    }

}
