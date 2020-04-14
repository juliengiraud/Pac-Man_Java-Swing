package modele;

import java.awt.Point;

/**
 *
 * @author julien
 */
public class SuperBoule extends Boule {

    public SuperBoule(Jeu _jeu, Point p) {
        super(_jeu, p);
        score = 50;
    }

    @Override
    public void getManger() {
        mange = true;
        jeu.pacmanMangeSuperboule();
        System.out.println("PACMAN mange une superboule");
    }

}
