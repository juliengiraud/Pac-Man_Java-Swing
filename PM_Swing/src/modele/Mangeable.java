package modele;

/**
 * Implémenter cette interface assure qu'on possède les méthodes
 * et propriétés pour être mangé par le joueur sans complication
 *
 * @author julien
 */
public interface Mangeable {

    public void getManger(); // L'objet se fait manger
    public int getScore(); // Pour obtenir le score à ajouter au pacman

}
