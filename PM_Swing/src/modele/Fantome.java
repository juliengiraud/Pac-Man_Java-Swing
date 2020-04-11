/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.Random;

/**
 *
 * @author freder
 */
public class Fantome extends Entite {

    private Random r = new Random();

    public Fantome(Jeu _jeu, Point p) {
        super(_jeu, p);

    }

    @Override
    public void choixDirection() {
        
        switch (r.nextInt(2)) {
            case 0:
                d = Direction.gauche;
                break;
            case 1:
                d = Direction.haut;
                break;
        }
    }
}
