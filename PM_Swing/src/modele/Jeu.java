package modele;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jeu extends Observable implements Runnable {

    public static final int SIZE = 25;

    private Pacman pm;
    private Bonus bonus;
    private final Fantome[] fantomes;
    private int niveau;
    private Vector<Integer> son = new Vector<>();

    private int partie_on;
    private boolean starting;

    private final Entite[][] grilleEntites; // entité à afficher sur des coordonnées
    private final ArrayList<Entite> entites;

    public Jeu() {
        fantomes = new Fantome[4];
        grilleEntites = new Entite[SIZE][SIZE];
        entites = new ArrayList<>();
        niveau = 1;
        starting = true;
        son.addElement(0);

        initialisationDesEntites();
    }

    public int getNiveau() {
        return niveau;
    }
    
    public Vector<Integer> getSon(){
        return son;
    }

    public Entite[][] getGrille() {
        return grilleEntites;
    }

    public Pacman getPacman() {
        return pm;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public Boolean getPartieOn() {
        return partie_on > 0;
    }

    private void initialisationDesEntites() {

        if (niveau == 1) {
            pm = new Pacman(this, new Point(0, 12));
            grilleEntites[0][12] = pm;
            entites.add(pm);
        }

        bonus = new Bonus(this, new Point(5, 5), niveau);
        grilleEntites[5][5] = bonus;
        entites.add(bonus);

        if (niveau == 1)
            initialisationMap();

        initialisationBoules();
    }

    private void initialisationBoules() {

        boolean DEBUG = true; // Place un minimum de boules histoire de passer rapidement d'un niveau à l'autre

        ArrayList<Point> interdits = new ArrayList<>(); // Points où il ne faut pas mettre de boules

        ajouterSuperBoule(3, 6);
        if (!DEBUG) {
            ajouterSuperBoule(3, 21);
            ajouterSuperBoule(20, 4);
            ajouterSuperBoule(21, 21);
        }

        // Coordonnées des SuperBoules
        interdits.add(new Point(3, 6));
        interdits.add(new Point(3, 21));
        interdits.add(new Point(20, 4));
        interdits.add(new Point(21, 21));

        // Les coordonnées du spawn des fantomes
        interdits.add(new Point(10, 11));
        interdits.add(new Point(11, 11));
        interdits.add(new Point(12, 11));
        interdits.add(new Point(10, 12));
        interdits.add(new Point(11, 12));
        interdits.add(new Point(12, 12));

        interdits.add(bonus.coo); // Les coordonnées du bonus, évidemment

        // Ajout d'une boule dans tous les autres emplacements
        for (int x = 1; x <= (DEBUG ? 5 : 23); x++)
            for (int y = 1; y <= (DEBUG ? 1 : 23); y++)
                if (!interdits.contains(new Point(x, y)) && !(grilleEntites[x][y] instanceof Mur))
                    ajouterBoule(x, y);
    }

    private void ajouterBoule(int x, int y) {
        Boule boule = new Boule(this, new Point(x, y));
        grilleEntites[x][y]= boule;
        entites.add(boule);
    }

    private void ajouterSuperBoule(int x, int y) {
        SuperBoule boule = new SuperBoule(this, new Point(x, y));
        grilleEntites[x][y]= boule;
        entites.add(boule);
    }

    private void initialisationMap() {

        //Initialisation des 4 coins
        ajouterMur(0,0,13); // coin[0] -> mur[13]
        ajouterMur(SIZE-1,0,14); // coin[1] -> mur[14]
        ajouterMur(0,SIZE-1,15); // coin[2] -> mur[15]
        ajouterMur(SIZE-1,SIZE-1,16); // coin[3] -> mur[16]

        //initialiser les cotés
        for (int x = 1; x < SIZE-1; x++)  ajouterMur(x,0,5);
        for (int x = 1; x < SIZE-1; x++)  ajouterMur(x,SIZE-1,5);

        //pour avoir un passage
        for (int y = 1; y < SIZE-1; y++) if(y!= 13 && y!= 10 && y!=11 && y!=12) ajouterMur(SIZE-1,y,2);
        for (int y = 1; y < SIZE-1; y++) if(y!= 13 && y!= 10 && y!=11 && y!=12) ajouterMur(0,y,2);

        //ajout des murs comme opstacle a la map
        //passage Droite
        ajouterMur(SIZE-1,10,3);
        ajouterMur(SIZE-1,13,1);

        //passage Gauche
        ajouterMur(0,10,3);
        ajouterMur(0,13,1);

        //Mur Milieu
        ajouterMur(13,11,1);
        ajouterMur(13,12,2);
        ajouterMur(9,11,1);
        ajouterMur(9,12,2);
        ajouterMur(9,13,15); // coin[2] -> mur[15]
        ajouterMur(10,13,5);
        ajouterMur(12,13,5);
        ajouterMur(11,13,5);
        ajouterMur(13,13,16); // coin[3] -> mur[16]

        //Mur du bas
        ajouterMur(3,13,1);
        ajouterMur(3,14,2);
        ajouterMur(3,15,2);
        ajouterMur(3,16,3);

        //obstacle du bas
        ajouterMur(2,18,4);
        ajouterMur(3,18,5);
        ajouterMur(4,18,5);
        ajouterMur(5,18,16); // coin[3] -> mur[16]
        ajouterMur(5,17,2);
        ajouterMur(5,16,2);
        ajouterMur(5,15,13); // coin[0] -> mur[13]
        ajouterMur(6,15,5);
        ajouterMur(7,14,2);
        ajouterMur(7,15,17); // coin[4] -> mur[17]
        ajouterMur(7,13,1);
        ajouterMur(8,15,5);
        ajouterMur(9,15,5);
        ajouterMur(10,15,6);

        //Petit UniMur
        ajouterMur(7,17,0);
        ajouterMur(2,22,0);
        ajouterMur(12,15,0);
        ajouterMur(22,22,0);
        ajouterMur(22,16,0);
        ajouterMur(7,11,0);

        //obstacle bas gauche
        ajouterMur(2,20,4);
        ajouterMur(3,20,5);
        ajouterMur(4,20,14); // coin[1] -> mur[14]
        ajouterMur(4,21,2);
        ajouterMur(4,22,3);

        //obstacle milieu gauche
        ajouterMur(9,17,1);
        ajouterMur(9,18,2);
        ajouterMur(9,19,2);
        ajouterMur(9,20,15); // coin[2] -> mur[15]
        ajouterMur(10,20,5);
        ajouterMur(11,20,5);
        ajouterMur(12,20,5);
        ajouterMur(13,20,16); // coin[3] -> mur[16]
        ajouterMur(13,18,2);
        ajouterMur(13,19,2); //fin monte
        ajouterMur(13,17,1);
            //coin milieu 2 cases
        ajouterMur(11,17,1);
        ajouterMur(11,18,3);

        ajouterMur(7,19,1);
        ajouterMur(7,20,2);
        ajouterMur(7,21,18); // coin[5] -> mur[18]
        ajouterMur(6,21,4);
        ajouterMur(7,22,15); // coin[2] -> mur[15]
        ajouterMur(8,22,5);
        ajouterMur(9,22,5);
        ajouterMur(10,22,5);
        ajouterMur(11,22,5);
        ajouterMur(12,22,6);

        //long mur 1
        ajouterMur(15,15,3); //fin
        ajouterMur(15,14,2);
        ajouterMur(15,13,2);
        ajouterMur(15,12,2);
        ajouterMur(15,11,2);
        ajouterMur(15,10,2);
        ajouterMur(15,9,1); //debut

        //long mur 2
        ajouterMur(15,17,1);
        ajouterMur(15,18,2);
        ajouterMur(15,19,2);
        ajouterMur(15,20,2);
        ajouterMur(15,21,2);
        ajouterMur(15,22,3);

        //fond droit
        ajouterMur(20,22,3); //fin
        ajouterMur(20,21,2);
        ajouterMur(20,20,13); // coin[0] -> mur[13]
        ajouterMur(21,20,5);
        ajouterMur(22,20,6); //fin mkesel

        //fond millieu droit
        ajouterMur(18,22,6); //fin mksel
        ajouterMur(17,22,15); // coin[2] -> mur[15]
        ajouterMur(17,21,2);
        ajouterMur(17,20,13); // coin[0] -> mur[13]
        ajouterMur(18,20,6);

        //long mur mkesel
        ajouterMur(17,18,4);
        ajouterMur(18,18,5);
        ajouterMur(19,18,5);
        ajouterMur(20,18,5);
        ajouterMur(21,18,5);
        ajouterMur(22,18,6);

        //droit millieu
        ajouterMur(20,16,3);
        ajouterMur(20,15,2);
        ajouterMur(20,14,13); // coin[0] -> mur[13]
        ajouterMur(21,14,5);
        ajouterMur(22,14,6);

        //rectangle
         //1
        ajouterMur(17,16,9); //end
        ajouterMur(18,16,12);
        ajouterMur(17,15,8); //mill
        ajouterMur(18,15,11);
        ajouterMur(17,14,7); //deb
        ajouterMur(18,14,10);
          //2
        ajouterMur(2,11,9);//fin rec
        ajouterMur(3,11,12);
        ajouterMur(2,10,8);
        ajouterMur(3,10,11);
        ajouterMur(2,9,7);
        ajouterMur(3,9,10);
             //3
        ajouterMur(15,2,10);
        ajouterMur(15,7,12);
        for(int i=3; i<7;i++)
            ajouterMur(15,i,11);
        ajouterMur(14,2,7);
        ajouterMur(14,7,9);
        for(int i=3; i<7;i++)
            ajouterMur(14,i,8);

        //caree
            //1
        ajouterMur(17,12,9); //mill
        ajouterMur(18,12,12);
        ajouterMur(17,11,7); //deb
        ajouterMur(18,11,10);
            //2
        ajouterMur(17,9,9); //mill
        ajouterMur(18,9,12);
        ajouterMur(17,8,7); //deb
        ajouterMur(18,8,10);
            //3
        ajouterMur(22,3,12); //mill
        ajouterMur(21,3,9);
        ajouterMur(22,2,10); //deb
        ajouterMur(21,2,7);

        //long mur
            //1er
        ajouterMur(20,12,3);
        ajouterMur(20,7,1);
        for(int i=11; i>7;i--)
            ajouterMur(20,i,2);
            //2eme
        ajouterMur(22,12,3);
        ajouterMur(22,7,1);
        for(int i=11; i>7;i--)
            ajouterMur(22,i,2);
            //3eme
        ajouterMur(5,9,1);
        ajouterMur(5,13,3);
        for(int i=10; i<13;i++)
            ajouterMur(5,i,2);
             //4eme
        ajouterMur(17,2,1);
        ajouterMur(17,6,3);
        for(int i=3; i<6;i++)
            ajouterMur(17,i,2);
              //5eme
        ajouterMur(12,2,1);
        ajouterMur(12,3,2);
        ajouterMur(12,4,3);
              //6eme
        ajouterMur(2,2,1);
        ajouterMur(2,3,3);

         //Un J pour Julien
        ajouterMur(4,2,1);
        for(int i=3; i<7;i++)
            ajouterMur(4,i,2);
        ajouterMur(4,7,16); // coin[3] -> mur[16]
        ajouterMur(3,7,5);
        ajouterMur(2,7,15); // coin[2] -> mur[15]
        ajouterMur(2,5,1);
        ajouterMur(2,6,2);

         //Un M pour Meriem
        ajouterMur(6,2,1);
        ajouterMur(6,7,3);
        for(int i=3; i<7;i++)
            ajouterMur(6,i,2);
        ajouterMur(7,3,0);
        ajouterMur(9,3,0);
        ajouterMur(8,4,0);
        ajouterMur(10,2,1);
        ajouterMur(10,7,3);
        for(int i=3; i<7;i++)
            ajouterMur(10,i,2);

        //obstacle bizarre
        ajouterMur(12,8,2);
        ajouterMur(8,8,2);
        ajouterMur(12,7,2);
        ajouterMur(8,7,2);
        ajouterMur(12,6,1);
        ajouterMur(8,6,1);

        ajouterMur(7,9,4);
        ajouterMur(13,9,6);
        ajouterMur(12,9,17); // coin[4] -> mur[17]
        ajouterMur(8,9,17); // coin[4] -> mur[17]
        for(int i=9; i<12;i++)
            ajouterMur(i,9,5);

        //droit up
        ajouterMur(19,2,1);
        ajouterMur(19,3,2);
        ajouterMur(19,4,2);
        ajouterMur(19,5,15); // coin[2] -> mur[15]
        ajouterMur(20,5,5);
        ajouterMur(21,5,5);
        ajouterMur(22,5,6);
    }

    private void ajouterMur(int x, int y, int type)
    {
        Mur mur = new Mur(this, new Point(x, y), type);
        grilleEntites[x][y]= mur;
        entites.add(mur);
    }

    public Object regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = e.coo;
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }

    public boolean deplacerEntite(Entite e, Direction d) {

        ArrayList<Point> interdits = new ArrayList<>();

        // Les coordonnées d'accès de la zone du spawn des fantomes
        interdits.add(new Point(10, 11));
        interdits.add(new Point(11, 11));
        interdits.add(new Point(12, 11));

        Point pCourant = e.coo;
        Point pCible = calculerPointCible(pCourant, d);

        if (!contenuDansGrille(pCible)) {
            return false;
        }

        if (e == pm && interdits.contains(pCible)) // Pour empêcher pacman d'aller dans le spawn des fantômes
            return false;

        if (objetALaPosition(pCible) == null) { // Si la case est vide on a rien à faire
            deplacerEntite(pCourant, pCible, e);
            return true;
        }

        Entite new_e = gestionCollision(e, objetALaPosition(pCible));
        if (new_e != null) {
            deplacerEntite(pCourant, pCible, new_e);
            return true;
        }

        return false;
    }

    /**
     * Gère le traitement à effectuer lorsque plusieurs entités
     * se supperposent dans la grille
     *
     * @return l'entité à déplacer s'il en reste une, sinon on renvoi null
     */
    private Entite gestionCollision(Entite src, Entite dest) { // Gère le trantement lorsque deux entités se supperposent

        if (src == dest) // Il s'agit d'une direction neutre : on ne fait rien
            return src;

        if (dest instanceof Mur) // On ne traverse pas un mur
            return null;

        if (src == bonus || src instanceof Boule) // On peut traverser le bonus ou une boule
            return dest; // L'autre entité va écraser temporairement la case

        if (src == pm) { // C'est PACMAN qui se déplace
            if (dest instanceof Mangeable) { // Il arrive sur une boule ou sur le bonus
                pm.manger((Mangeable) dest);
                return pm;
            }
            return gestionCollisionPacmanFantome((Fantome) dest); // Il arrive sur un fantôme
        }

        if (src instanceof Fantome) { // C'est un fantôme qui se déplace
            if (dest == pm)
                return gestionCollisionPacmanFantome((Fantome) src); // Il arrive sur un autre fantôme

            if (dest instanceof Fantome) // Un fantôme ne peut pas traverser un autre fantôme
                return null;
            return src;
        }

        System.out.println("Problème de collision avec " + src.getClass() + " et " + dest.getClass());
        return null;
    }

    /**
     * Gère le traitement à effectuer lorsque pacman et un fantôme
     * se supperposent dans la grille
     *
     * @return l'entité à déplacer
     */
    private Entite gestionCollisionPacmanFantome(Fantome dest) {
        if (!dest.isVulnerable()) {
            pm.tuer();
            return dest;
        }

        if (!dest.isMort()) {
            for (Entite e : entites)
                if (e instanceof Boule && e.coo == dest.coo) // S'il y a une boule sous le fantôme
                    pm.manger((Mangeable) e); // Alors pacman la mange
            pm.mangerFantome(dest);
        }
        return pm;
    }

    protected void pacmanMangeSuperboule() {
        for (Fantome f : fantomes)
            f.setVulnerable();
        pm.resetFantomeMange();
    }

    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        switch(d) {
            case haut:
                pCible = new Point(pCourant.x, (pCourant.y + SIZE - 1) % SIZE);
                break;

            case bas:
                pCible = new Point(pCourant.x, (pCourant.y + 1) % SIZE);
                break;

            case gauche:
                pCible = new Point((pCourant.x + SIZE - 1) % SIZE, pCourant.y);
                break;

            case droite:
                pCible = new Point((pCourant.x + 1) % SIZE, pCourant.y);
                break;

            case neutre:
                pCible = pCourant;
                break;
        }

        return pCible;
    }

    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        e.coo = pCible;
    }

    /** Vérifie que p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        boolean sortie = p.x >= 0 && p.x < SIZE && p.y >= 0 && p.y < SIZE;
        if (!sortie)
            System.out.println("Erreur grille : " + p.toString());
        return sortie;
    }

    private Entite objetALaPosition(Point p) {
        Entite retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }

        return retour;
    }

    /**
     * Un processus est créé et lancé, celui-ci execute la fonction run()
     */
    public void start() {

        new Thread(this).start();
        partie_on = 2;

    }

    protected void stop() {

        partie_on--;
    }

    protected boolean isStarting() {
        return starting;
    }

    protected void setStarting() {
        starting = true;
    }

    private boolean niveauFini() {
        for (Entite e : entites)
            if (e instanceof Boule)
                return false;
        return true;
    }

    private void nextLevel() {

        if (niveau == 3) { // Si le jeu est fini on sort
            son.addElement(6);
            stop();
            return;
        }
        // Sinon on passe au niveau suivant...
        niveau++;
        pm.addVie();

        // On indique qu'il faut réinitialiser les fantômes et marquer la pause de DÉBUT de partie
        setStarting();

        // On fait coucou à la vue pour mettre à jour l'affichage avant de marquer la pause de FIN de partie
        setChanged();
        notifyObservers();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
        }

        // On replace les boules et le joueur
        reinitialisationDesEntites();
    }

    private void reinitialisationDesEntites() {

        for (int i = 0; i < entites.size(); i++) { // On nettoie avant de rajouter
            Entite e = entites.get(i);
            if (e instanceof Fantome)
                entites.remove(e);
        }
        entites.remove(bonus);
        pm.respawn();

        initialisationDesEntites();
    }
    
    private void respawn_fantomes() {
        // Nettoyage des entités
        if (fantomes[0] != null)
            grilleEntites[fantomes[0].coo.x][fantomes[0].coo.y] = null;
        if (fantomes[1] != null)
            grilleEntites[fantomes[1].coo.x][fantomes[1].coo.y] = null;
        if (fantomes[2] != null)
            grilleEntites[fantomes[2].coo.x][fantomes[2].coo.y] = null;
        if (fantomes[3] != null)
            grilleEntites[fantomes[3].coo.x][fantomes[3].coo.y] = null;
        entites.remove(fantomes[0]);
        entites.remove(fantomes[1]);
        entites.remove(fantomes[2]);
        entites.remove(fantomes[3]);

        // Rajout des entités
        fantomes[0] = new Fantome(this, new Point(12, 12), Color.PINK);
        fantomes[1] = new Fantome(this, new Point(11, 12), Color.RED);
        fantomes[2] = new Fantome(this, new Point(10, 12), Color.BLUE);
        fantomes[3] = new Fantome(this, new Point(11, 11), Color.GREEN );
        grilleEntites[12][12] = fantomes[0];
        grilleEntites[11][12] = fantomes[1];
        grilleEntites[10][12] = fantomes[2];
        grilleEntites[11][11] = fantomes[3];
        entites.add(fantomes[0]);
        entites.add(fantomes[1]);
        entites.add(fantomes[2]);
        entites.add(fantomes[3]);
    }

    @Override
    public void run() {

        while (partie_on > 0) {

            // Marque une petite pause avant le début du jeu et après chaque respawn
            if (starting) {

                respawn_fantomes();

                // Coucou à la vue pour mettre à jour l'affichage avant de marquer la pause
                setChanged();
                notifyObservers();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
                }

                starting = false; // C'est sympa le démarrage mais il ne faut pas en abuser
            }

            // Enlève les boules à supprimer et run les autres entités
            for (int i = 0; i < entites.size(); i++) {
                Entite e = entites.get(i);
                if (e instanceof Boule && ((Boule) e).isManger()) // La boule a été mangée
                    entites.remove(e);
                else
                    e.run();
            }

            if (niveauFini()) // Passage au niveau suivant
                nextLevel();

            // Mise à jour de l'affichage entre chaque tour
            setChanged();
            notifyObservers();

            // Petite pause entre chaque tour, on n'est pas des machines quand même :-)
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
