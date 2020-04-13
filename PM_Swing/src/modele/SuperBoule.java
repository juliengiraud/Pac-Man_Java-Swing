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
public class SuperBoule extends Boule {

    public SuperBoule(Jeu _jeu, Point p) {
        super(_jeu, p);
        score = 50;
    }
    
    @Override
    public void getManger() {
        super.getManger();
        jeu.pacmanMangeSuperboule();
    }

}
