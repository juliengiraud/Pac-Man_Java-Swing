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
 * @author fred
 */
public class Pacman extends Entite {

    
    

    public Pacman(Jeu _jeu, Point p) {
        super(_jeu, p);
        d = Direction.droite;

    }
    
    public void setDirection(Direction _d) {
        d = _d;
    }

    @Override
    public void choixDirection() {
        
    }

}
