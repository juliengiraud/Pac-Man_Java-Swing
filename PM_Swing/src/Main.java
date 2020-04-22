import VueControleur.VueControleurPacMan;

public class Main {

    public static void main(String[] args) {

        boolean debug = false;
        
        if (args.length > 0) {
            System.out.println(args[0]);
            if (args[0].equals("--debug")) {
            debug = true;
            }
        }
        
        // Le debug à true permet de placer juste quelques boules pour tester
        // toutes les fonctionnalités du jeu en quelques secondes
        VueControleurPacMan vue = new VueControleurPacMan(debug); // C'est la vue qui fait tout

    }
}
