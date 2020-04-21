package modele;

import java.awt.Point;

public class Pacman extends Entite {

    private Direction futureDirection;
    private final Point spawn;
    private int fantomeMange;
    private int score;
    private int vie;

    public Pacman(Jeu _jeu, Point p) {
        super(_jeu, p);
        d = Direction.droite;
        spawn = p;
        score = 0;
        vie = 2;
        fantomeMange = 0;
    }

    public void resetFantomeMange() {
        fantomeMange = 0;
    }

    public void respawn() {
        coo = spawn;
        d = Direction.droite;
        futureDirection = null;
        jeu.deplacerEntite(this, Direction.neutre); // Pour mise à jour graphique
    }

    public void addVie() {
        vie++;
    }

    public void tuer() {
        if (vie > 0) {
            vie--;
            jeu.setStarting();
            respawn();
            System.out.println("PACMAN a été tué par un fantôme");
        }
        else {
            System.out.println("fin de la partie");
            jeu.stop();
        }
    }

    private void augmenterScore(int s) {
        score += s;
    }

    public void setFutureDirection(Direction d) { // Pour réagir au clavier comme le vrai pacman
        futureDirection = d;
    }

    public int getState() {
        if (d == Direction.haut)
            return 0;
        if (d == Direction.bas)
            return 1;
        if (d == Direction.gauche)
            return 2;
        if (d == Direction.droite)
            return 3;
        return 3; // Direction par défaut
    }

    public int getScore() {
        return score;
    }

    public int getVies() {
        return vie;
    }

    public void manger(Mangeable objet) {
        augmenterScore(objet.getScore());
        objet.getManger();
    }

    public void mangerFantome(Fantome f) {
        int[] scores = {200, 400, 800, 1600};
        augmenterScore(scores[fantomeMange]);
        fantomeMange++;
        f.getManger();
    }

    @Override
    public void choixDirection() {
        if (futureDirection == null) // Si le joueur ne veut pas tourner on ne change rien
            return;

        // Si le joueur peut tourner sans être stoppé par un mur, il peut tourner
        Object cible = (Entite) jeu.regarderDansLaDirection(this, futureDirection);
        if (cible instanceof Mur)
            return;
        d = futureDirection;
    }

}
