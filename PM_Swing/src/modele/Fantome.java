package modele;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author freder
 */
public class Fantome extends Entite {

    private static final int LIMITE = 50; // Nombre de tour avant de redevenir invulnérable

    private Vector<Point> pointTraverse = new Vector<>();
    private final Random r = new Random(); // Pour déplacement aléatoire
    private final Color couleur;
    private final Point spawn; // Point d'apparition
    private Vector<Point> mur = new Vector<>();; // contient les coordoneés des murs

    private boolean vulnerable; // Ne peut pas tuer pacman
    private boolean mort; // Ne peut pas être mangé par pacman
    private int temps_vulnerable; // Compteur pour gérer quand redevenir invulnérable

    public Fantome(Jeu _jeu, Point p, Color c) {
        super(_jeu, p);
        d = Direction.droite;
        spawn = p;
        couleur = c;
        vulnerable = false;
        mort = false;
	
        for(int i=0;i<jeu.SIZE; i++){
            for(int j=0;j<jeu.SIZE; j++)
            {
                if(jeu.getGrille()[i][j] instanceof Mur)
                    mur.add(new Point(i,j));
            }
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    /**
     * Le fantôme se fait manger par pacman
     */
    public void getManger() {
        mort = true;
        coo = spawn;
        d = Direction.neutre;
        jeu.deplacerEntite(this, Direction.neutre); // Pour MAJ visuelle
        jeu.getSon().addElement(4);
    }

    /**
     * Pour sélectionner l'icone à afficher (voir la vue)
     */
    public int getState() {
        if (!vulnerable) {
            if (couleur == Color.BLUE) {
                if (d == Direction.bas)
                    return 0;
                if (d == Direction.gauche)
                    return 1;
                if (d == Direction.droite)
                    return 2;
                if (d == Direction.haut)
                    return 3;
                return 2;
            }
            if (couleur == Color.RED) {
                if (d == Direction.bas)
                    return 4;
                if (d == Direction.gauche)
                    return 5;
                if (d == Direction.droite)
                    return 6;
                if (d == Direction.haut)
                    return 7;
                return 6;
            }
            if (couleur == Color.PINK) {
                if (d == Direction.bas)
                    return 8;
                if (d == Direction.gauche)
                    return 9;
                if (d == Direction.droite)
                    return 10;
                if (d == Direction.haut)
                    return 11;
                return 10;
            }
            if(couleur == Color.GREEN){ //corona
                return 14;   
            }
        }
        else {
            if (mort)
                return 12; // Les yeux qui retournent au spawn
            else
                return 13; // Les fantômes bleues
        }
        return -1;
    }

    @Override
    public void choixDirection() {
    
        /**
         * Tant que le fantôme est mort ET vulnérable, il doit rester dans son spawn
         */
        if (mort) {
            d = Direction.neutre;
            return;
        }
    
        switch (r.nextInt(2)) {
            case 0:
                d = Direction.gauche;
                break;
            case 1:
                d = Direction.haut;
                break;
        }
    }

    public boolean isMort() {
        return mort;
    }

    public void setVulnerable() {
        vulnerable = true;
        temps_vulnerable = LIMITE;
    }

    private void gestionVulnerable() {
        if (temps_vulnerable > 0)
            temps_vulnerable--;
        if (temps_vulnerable == 0) {
            mort = false;
            vulnerable = false;
        }
    }

    @Override
    public void run() {
        if (vulnerable)
            gestionVulnerable();
        choixDirection();
        avancerDirectionChoisie();
    }
}
