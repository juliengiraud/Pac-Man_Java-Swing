
import VueControleur.VueControleurPacMan;
import modele.Jeu;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author freder
 */
public class Main {
    public static void main(String[] args) {
        
        Jeu jeu = new Jeu();
        
        VueControleurPacMan vc = new VueControleurPacMan(Jeu.SIZE);
        
        jeu.addObserver(vc);
        vc.setJeu(jeu);
        
        vc.setVisible(true);
        
        jeu.start();
    }
}
