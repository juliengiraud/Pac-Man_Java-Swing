import VueControleur.VueControleurPacMan;

public class Main {

    public static void main(String[] args) {

        boolean debug = true;
        
        // Le debug à true permet de placer juste quelques boules pour tester
        // toutes les fonctionnalités du jeu en quelques secondes
        
        new VueControleurPacMan(debug); // C'est la vue qui fait tout

    }
}
